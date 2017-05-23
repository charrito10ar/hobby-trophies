package com.marbit.hobbytrophies;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.marbit.hobbytrophies.model.Trophy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrophyDetailActivity extends AppCompatActivity {

    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private String gameId;
    private Trophy trophy;
    private ImageView imageTrophy;
    private TextView titleTrophy;
    private TextView descriptionTrophy;
    private TextView descriptionGuide;
    private FloatingActionButton floatingButtonSendGuide;

    private View headLayout;
    private View firsIconsRow;
    private View secondIconsRow;
    private ImageView firstIcon;
    private ImageView secondIcon;
    private ImageView thirdIcon;
    private ImageView fourthIcon;
    private ImageView fifthIcon;
    private ImageView sixthIcon;
    private ImageView seventhIcon;
    private ImageView eighthIcon;
    private ImageView ninthIcon;
    private ImageView tenthIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy_detail);

        this.headLayout = (RelativeLayout) findViewById(R.id.head_layout);
        this.firsIconsRow = (LinearLayout) findViewById(R.id.layout_icons_first_row);
        this.secondIconsRow = (LinearLayout) findViewById(R.id.layout_icons_second_row);
        this.firstIcon = (ImageView) findViewById(R.id.ic_trophy_first);
        this.secondIcon = (ImageView) findViewById(R.id.ic_trophy_second);
        this.thirdIcon = (ImageView) findViewById(R.id.ic_trophy_third);
        this.fourthIcon = (ImageView) findViewById(R.id.ic_trophy_fourth);
        this.fifthIcon = (ImageView) findViewById(R.id.ic_trophy_fifth);
        this.sixthIcon = (ImageView) findViewById(R.id.ic_trophy_sixth);
        this.seventhIcon = (ImageView) findViewById(R.id.ic_trophy_seventh);
        this.eighthIcon = (ImageView) findViewById(R.id.ic_trophy_eighth);
        this.ninthIcon = (ImageView) findViewById(R.id.ic_trophy_nineth);
        this.tenthIcon= (ImageView) findViewById(R.id.ic_trophy_tenth);

        this.gameId = getIntent().getStringExtra("GAME-ID");
        this.trophy = getIntent().getParcelableExtra("TROPHY");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        collapsingToolbarLayout.setTitle(trophy.getTitle());

        this.imageTrophy = (ImageView) findViewById(R.id.ic_trophy_detail );
        //this.titleTrophy = (TextView) findViewById(R.id.text_title_trophy_detail);
        this.descriptionTrophy = (TextView) findViewById(R.id.text_description_trophy_detail);
        this.descriptionGuide = (TextView) findViewById(R.id.text_guide_description_trophy_detail);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        Picasso.with(getApplicationContext()).load(trophy.getImg()).placeholder(R.drawable.ic_placeholder_trophy).into(this.imageTrophy);
        //this.titleTrophy.setText(this.trophy.getTitle());
        this.descriptionTrophy.setText(this.trophy.getDescription());

        this.sendGetGuide(this.trophy.getId());

        floatingButtonSendGuide = (FloatingActionButton) findViewById(R.id.floatin_action_new_meeting);
        floatingButtonSendGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"hobbytrophies@gmail.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Quiero escribir una guía:");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Identificador de Juego: " + gameId +  "\nTrofeo: " + trophy.getDescription());

                emailIntent.setType("message/rfc822");

                try {
                    startActivity(Intent.createChooser(emailIntent,
                            "Elije una aplicación para enviar un mail de contacto"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(),
                            "No email clients installed.",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public void sendGetGuide(int trophyId){

        stringRequest = new StringRequest(Request.Method.GET,
                "http://hobbytrophies.com/foros/ps3/get-trofeo-detail.php?id=" + gameId + "&idTrofeo=" + trophyId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray jsonArrayGuide = jsonObject.getJSONArray("trophie-detail");
                            if (jsonArrayGuide.length() > 0) {
                                JSONObject jsonObjectGuide = (JSONObject) jsonArrayGuide.get(0);
                                descriptionGuide.setText(stringToHtml(jsonObjectGuide.getString("descripcio_moderar")));
                                String icons = jsonObjectGuide.getString("icones");
                                String extraIcon = jsonObjectGuide.getString("icona_extra");
                                setIcons(icons, extraIcon);
                                floatingButtonSendGuide.setVisibility(View.GONE);
                            } else {
                                descriptionGuide.setText("No existe guía para este trofeo. Puede colaborar poniendote en contacto con HobbyTrophies desde el botón rojo de abajo.");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("ERROR", error.toString());
            }
        });

        requestQueue.add(stringRequest);
    }

    private Spanned stringToHtml(String text) {
        String stringTemp = text.replace("[B]", "<b>");
        stringTemp = stringTemp.replace("[/B]", "</b>");

        stringTemp = stringTemp.replace("[I]", "<i>");
        stringTemp = stringTemp.replace("[/I]", "</i>");

        stringTemp = stringTemp.replace("[U]", "<u>");
        stringTemp = stringTemp.replace("[/U]", "</u>");

        stringTemp = stringTemp.replace("[CENTER]","<p align=\"center\">");
        stringTemp = stringTemp.replace("[/CENTER]","</p>");

        stringTemp = stringTemp.replace("[RIGHT]","<p align=\"right\">");
        stringTemp = stringTemp.replace("[/RIGHT]","</p>");

        stringTemp = stringTemp.replace("[COLOR=#","<font color=\"#");
        stringTemp = stringTemp.replace("[/COLOR]","</font>");

        stringTemp = stringTemp.replace("[SIZE=","<font size=\"");
        stringTemp = stringTemp.replace("[/SIZE]","</font>");

        stringTemp = stringTemp.replace("[IMG]","<img>");
        stringTemp = stringTemp.replace("[/IMG]","</img>");



        stringTemp = stringTemp.replace("]","\">");

        Spanned resultString;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resultString = Html.fromHtml(stringTemp,  Html.FROM_HTML_MODE_COMPACT);
        }else {
            resultString = Html.fromHtml(stringTemp);
        }
        return resultString;
    }

    public void setIcons(String icons, String extraIcon) {

        if(icons.length() > 0){
            this.headLayout.setVisibility(View.VISIBLE);
        }

        List<Integer> icList = new ArrayList<>();
        if(icons.contains("icona_0")){
            icList.add(R.drawable.ic_game_one_player);
        }
        if(icons.contains("icona_1")){
            icList.add(R.drawable.ic_game_two_player);
        }
        if(icons.contains("icona_2")){
            icList.add(R.drawable.ic_game_lots_players);
        }
        if(icons.contains("icona_3")){
            icList.add(R.drawable.ic_game_history);
        }
        if(icons.contains("icona_4")){
            icList.add(R.drawable.ic_game_time);
        }
        if(icons.contains("icona_5")){
            icList.add(R.drawable.ic_game_secret_way);
        }
        if(icons.contains("icona_6")){
            icList.add(R.drawable.ic_game_online);
        }
        if(icons.contains("icona_7")){
            icList.add(R.drawable.ic_game_cumulative);
        }
        if(icons.contains("icona_8")){
            icList.add(R.drawable.ic_game_collectable);
        }
        if(icons.contains("icona_9")){
            icList.add(R.drawable.ic_game_bug);
        }
        if(icons.contains("icona_10")){
            icList.add(R.drawable.ic_game_one_player);
        }
        if(icons.contains("icona_11")){
            icList.add(R.drawable.ic_game_one_player);
        }
        if(icons.contains("icona_12")){
            icList.add(R.drawable.ic_game_one_player);
        }
        if(icons.contains("icona_13")){
            icList.add(R.drawable.ic_game_one_player);
        }
        if(icons.contains("icona_14")){
            icList.add(R.drawable.ic_game_random);
        }

        if(icList.size() < 6){
            secondIconsRow.setVisibility(View.GONE);
        }

        for (int i = 0; i < icList.size(); i++ ){
            switch (i){
                case 0:
                    firstIcon.setImageResource(icList.get(0));
                    firstIcon.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    secondIcon.setImageResource(icList.get(1));
                    secondIcon.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    thirdIcon.setImageResource(icList.get(2));
                    thirdIcon.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    fourthIcon.setImageResource(icList.get(3));
                    fourthIcon.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    fifthIcon.setImageResource(icList.get(4));
                    fifthIcon.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    sixthIcon.setImageResource(icList.get(5));
                    sixthIcon.setVisibility(View.VISIBLE);
                    break;
                case 6:
                    seventhIcon.setImageResource(icList.get(6));
                    seventhIcon.setVisibility(View.VISIBLE);
                    break;
                case 7:
                    eighthIcon.setImageResource(icList.get(7));
                    eighthIcon.setVisibility(View.VISIBLE);
                    break;
                case 8:
                    ninthIcon.setImageResource(icList.get(8));
                    ninthIcon.setVisibility(View.VISIBLE);
                    break;
                case 9:
                    tenthIcon.setImageResource(icList.get(9));
                    tenthIcon.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
