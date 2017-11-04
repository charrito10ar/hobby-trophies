package com.marbit.hobbytrophies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.iid.FirebaseInstanceId;
import com.marbit.hobbytrophies.chat.ChatActivity;
import com.marbit.hobbytrophies.chat.model.Chat;
import com.marbit.hobbytrophies.dialogs.DialogAlertLogin;
import com.marbit.hobbytrophies.dialogs.DialogGeneric;
import com.marbit.hobbytrophies.firebase.dao.FirebaseNotificationDAO;
import com.marbit.hobbytrophies.fragments.AllGamesFragment;
import com.marbit.hobbytrophies.fragments.FavouritesFragment;
import com.marbit.hobbytrophies.fragments.MarketFragment;
import com.marbit.hobbytrophies.fragments.MeetingFragment;
import com.marbit.hobbytrophies.fragments.MessagesFragment;
import com.marbit.hobbytrophies.fragments.ProfileFragment;
import com.marbit.hobbytrophies.fragments.RankingFragment;
import com.marbit.hobbytrophies.fragments.WishListFragment;
import com.marbit.hobbytrophies.interfaces.MainActivityView;
import com.marbit.hobbytrophies.market.ItemDetailActivity;
import com.marbit.hobbytrophies.model.Meeting;
import com.marbit.hobbytrophies.overwrite.CircleTransform;
import com.marbit.hobbytrophies.presenters.MainActivityPresenter;
import com.marbit.hobbytrophies.utilities.Constants;
import com.marbit.hobbytrophies.utilities.DialogCodes;
import com.marbit.hobbytrophies.utilities.Preferences;
import com.squareup.picasso.Picasso;

import java.util.Calendar;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, AllGamesFragment.OnAllGamesFragmentInteractionListener, ProfileFragment.ProfileOnFragmentInteractionListener,
        DialogGeneric.OnDialogGenericInteractionListener, MeetingFragment.MeetingInteractionListener, RankingFragment.OnRankingFragmentInteractionListener,
        MarketFragment.OnMarketFragmentInteractionListener, MessagesFragment.MessagesFragmentInteractionListener, View.OnClickListener,
        FavouritesFragment.FavouritesFragmentListener, MainActivityView{

    private TextView userNameNav;
    private ImageView avatarNav;
    private String[] months;
    private int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
    private LinearLayout headerMenuLeft;
    private MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.presenter = new MainActivityPresenter(getApplicationContext(), this);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-8211299087542513~8699467989");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("2843E996E3E48320E27B16741947CF56").build();
        mAdView.loadAd(adRequest);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
         navigationView.setNavigationItemSelectedListener(this);

        this.userNameNav = (TextView) navigationView.getHeaderView(0).findViewById(R.id.text_view_nav_user_name);
        this.avatarNav = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.ic_nav_avatar);
        this.headerMenuLeft = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.header_menu_left);
        this.headerMenuLeft.setOnClickListener(this);

        this.setSection();

        this.months = getResources().getStringArray(R.array.months);
        this.presenter.handleDeepLinks(this, getIntent());
    }

    @Override
    public void onResume(){
        super.onResume();
        this.userNameNav.setText(Preferences.getUserName(getApplicationContext()));
        Picasso.with(this).load(Preferences.getAvatar(getApplicationContext())).transform(new CircleTransform()).into(this.avatarNav);
    }

    private void setSection() {
        Fragment fragment;
        String title;
        String fragmentTag;
        if (Preferences.getBoolean(getApplicationContext(), Constants.PREFERENCE_IS_USER_LOGIN)) {
            fragment = ProfileFragment.newInstance(Preferences.getString(getApplicationContext(), Constants.PREFERENCE_USER_NAME));
            title = "Perfil";
            fragmentTag = "Profile";
        }else {
            fragment = new AllGamesFragment();
            title  = "Todos los Juegos";
            fragmentTag = "AllGames";
        }
        this.setFragment(fragment, fragmentTag, title);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        String fragmentTag = "";

        int id = item.getItemId();

        if (id == R.id.nav_favourites) {
            fragment = FavouritesFragment.newInstance();
            title = "Favoritos";
            fragmentTag = "Favoritos";
        } else if (id == R.id.nav_all_games) {
            fragment = new AllGamesFragment();
            title  = "Todos los Juegos";
            fragmentTag = "AllGames";
        } else if (id == R.id.nav_meetings) {
            fragment = new MeetingFragment();
            title  = "Quedadas";
            fragmentTag = "Mettings";

        } else if (id == R.id.nav_ranking) {
            fragment = RankingFragment.newInstance("", "");
            title  = "Ranking de " + this.months[currentMonth];
            fragmentTag = "Ranking";
        } else if (id == R.id.nav_market) {
            fragment = MarketFragment.newInstance();
            title  = "Mercadillo";
            fragmentTag = "Market";

        } else if (id == R.id.nav_messages) {
            if(Preferences.getBoolean(getApplicationContext(), Constants.PREFERENCE_IS_USER_LOGIN)){
                fragment = MessagesFragment.newInstance();
                title  = "Mensajes";
                fragmentTag = "Messages";
            }else {
                DialogAlertLogin dialogAlertLogin = DialogAlertLogin.newInstance();
                dialogAlertLogin.show(getSupportFragmentManager(), "dialogAlertLogin");
            }

        } else if (id == R.id.nav_wish_list) {
            if(Preferences.getBoolean(getApplicationContext(), Constants.PREFERENCE_IS_USER_LOGIN)){
                fragment = WishListFragment.newInstance();
                title  = "Lista de deseos";
                fragmentTag = "WishList";
            }else {
                DialogAlertLogin dialogAlertLogin = DialogAlertLogin.newInstance();
                dialogAlertLogin.show(getSupportFragmentManager(), "dialogAlertLogin");
            }
        }
        else if (id == R.id.nav_rate) {

        } else if (id == R.id.nav_log_out) {
            DialogGeneric dialogGeneric = DialogGeneric.newInstance("Atención", "¿Estas seguro que deseas salir?", "Salir", DialogCodes.DIALOG_ACTION_LOG_OUT);
            dialogGeneric.show(getSupportFragmentManager(), "DialogLogOut");
        }

        this.setFragment(fragment, fragmentTag, title);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(Fragment fragment, String fragmentTag, String title){
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment, fragmentTag);
            fragmentTransaction.commit();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void clickLetter(View view){
        TextView textView = (TextView) view;
        Toast.makeText(getApplicationContext(), "Buscando juegos que comienzan con: " + textView.getText().toString() , Toast.LENGTH_LONG).show();
        AllGamesFragment allGamesFragment = (AllGamesFragment) getSupportFragmentManager().findFragmentByTag("AllGames");
        allGamesFragment.clickLetter(textView.getText().toString());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void profileOnFragmentInteractionListener(Uri uri) {
    }

    @Override
    public void onDialogGenericInteraction(int code) {
        switch (code){
            case DialogCodes.DIALOG_ACTION_LOG_OUT:
                unregisterFirebaseToken();
                Preferences.logOut(getApplicationContext());
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        }
    }

    private void unregisterFirebaseToken() {
        FirebaseNotificationDAO firebaseNotificationDAO = new FirebaseNotificationDAO();
        String token = FirebaseInstanceId.getInstance().getToken();
        if(token != null) {
            firebaseNotificationDAO.unregisterToken(Preferences.getUserName(getApplicationContext()), token);
        }
    }

    @Override
    public void meetingOnFragmentInteraction(Meeting item) {
            // Juego digital o fisico. Tipo de venta trueque o dinero
    }


    @Override
    public void onFragmentInteraction() {
    }

    @Override
    public void openChat(Chat chat) {
        Intent intentChat = new Intent(getApplicationContext(), ChatActivity.class);
        intentChat.putExtra(ChatActivity.PARAM_ITEM_ID, chat.getItem());
        intentChat.putExtra(ChatActivity.PARAM_ITEM_TITLE, chat.getTitleItem());
        intentChat.putExtra(ChatActivity.PARAM_SELLER, chat.getSeller());
        intentChat.putExtra(ChatActivity.PARAM_BUYER, chat.getBuyer());
        startActivity(intentChat);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_menu_left:
                Fragment fragment = null;
                String title = getString(R.string.app_name);
                String fragmentTag = "";
                if(Preferences.getBoolean(getApplicationContext(), Constants.PREFERENCE_IS_USER_LOGIN)){
                    fragment = ProfileFragment.newInstance(Preferences.getString(getApplicationContext(), Constants.PREFERENCE_USER_NAME));
                    title = "Perfil";
                    fragmentTag = "Profile";
                }else {
                    DialogAlertLogin dialogAlertLogin = DialogAlertLogin.newInstance();
                    dialogAlertLogin.show(getSupportFragmentManager(), "dialogAlertLogin");
                }
                this.setFragment(fragment, fragmentTag, title);

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                break;
        }
    }

    @Override
    public void openActivityItemDetail(String itemId) {
        Intent itemIntent = new Intent(getApplicationContext(), ItemDetailActivity.class);
        itemIntent.putExtra("FROM", "DEEP-LINK");
        itemIntent.putExtra("itemId", itemId);
        startActivity(itemIntent);
    }

    @Override
    public void openActivityMeetingDetail(String meetingId) {
        Intent itemIntent = new Intent(getApplicationContext(), MeetingDetailActivity.class);
        itemIntent.putExtra("FROM", "DEEP-LINK");
        itemIntent.putExtra("meetingId", meetingId);
        startActivity(itemIntent);
    }
}
