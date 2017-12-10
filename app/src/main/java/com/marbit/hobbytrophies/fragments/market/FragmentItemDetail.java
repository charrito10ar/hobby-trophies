package com.marbit.hobbytrophies.fragments.market;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.ServerValue;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.dialogs.DialogSearchGame;
import com.marbit.hobbytrophies.interfaces.market.FragmentItemDetailView;
import com.marbit.hobbytrophies.model.Game;
import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.presenters.market.FragmentItemDetailPresenter;
import com.marbit.hobbytrophies.utilities.Constants;
import com.marbit.hobbytrophies.utilities.Preferences;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class FragmentItemDetail extends Fragment implements View.OnClickListener, FragmentItemDetailView, DialogSearchGame.DialogSearchGameListener, AdapterView.OnItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 99;
    private static final String TAG = "MainActivity";
    private static final int SELECT_FILE_CODE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static int PHOTO_POSITION;
    private RelativeLayout layoutLoading;
    private FragmentItemDetailPresenter presenter;
    private EditText categoryItem;
    private EditText descriptionItem;
    private EditText priceItem;
    private Item item;
    private FrameLayout buttonFirstPhoto;
    private FrameLayout buttonSecondPhoto;
    private ImageView firstPhoto, secondPhoto, thirdPhoto;
    private ArrayList<File> photosList;
    private CheckBox checkBoxSaleType;
    private CheckBox checkBoxDigitalVersion;

    public Button sendItemButton;

    private int itemType;

    private OnFragmentItemDetailInteractionListener mListener;
    private String[] itemTypes;
    private TextInputLayout textInputLayoutPrice;
    private TextView titleItem;
    private Spinner titleItemConsole;
    private EditText titleItemOthers;
    private Game gameSelected;
    private String[] consolesArray;

    public FragmentItemDetail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemType = getArguments().getInt(ARG_PARAM1);
        }
    }

    public static FragmentItemDetail newInstance(int itemType) {
        FragmentItemDetail fragment = new FragmentItemDetail();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, itemType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_item_detail, container, false);
        this.photosList = new ArrayList<>();
        this.consolesArray  = getResources().getStringArray(R.array.consoles);
        this.layoutLoading = (RelativeLayout) view.findViewById(R.id.layout_loading);
        this.presenter = new FragmentItemDetailPresenter(getContext(), this);
        this.titleItem = (TextView) view.findViewById(R.id.title_item_game);
        this.titleItem.setOnClickListener(this);
        this.titleItemConsole = (Spinner) view.findViewById(R.id.title_item_console);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, consolesArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.titleItemConsole.setAdapter(adapter);
        this.titleItemOthers = (EditText) view.findViewById(R.id.title_item_others);
        this.itemTypes = getResources().getStringArray(R.array.item_types);
        this.categoryItem = (EditText) view.findViewById(R.id.text_input_item_type);
        this.descriptionItem = (EditText) view.findViewById(R.id.item_description);
        this.priceItem = (EditText) view.findViewById(R.id.item_price);
        this.checkBoxSaleType = (CheckBox) view.findViewById(R.id.checkbox_is_barter);
        this.checkBoxDigitalVersion = (CheckBox) view.findViewById(R.id.checkbox_digital_version);
        this.textInputLayoutPrice = (TextInputLayout) view.findViewById(R.id.text_input_layout_price);
        this.sendItemButton = (Button) view.findViewById(R.id.button_send_item);
        this.sendItemButton.setOnClickListener(this);
        this.buttonFirstPhoto = (FrameLayout) view.findViewById(R.id.first_photo);
        this.buttonFirstPhoto.setOnClickListener(this);
        this.buttonSecondPhoto = (FrameLayout) view.findViewById(R.id.second_photo);
        this.buttonSecondPhoto.setOnClickListener(this);
        FrameLayout buttonThirdPhoto = (FrameLayout) view.findViewById(R.id.third_photo);
        buttonThirdPhoto.setOnClickListener(this);
        this.firstPhoto = (ImageView) view.findViewById(R.id.image_first_photo);
        this.secondPhoto = (ImageView) view.findViewById(R.id.image_second_photo);
        this.thirdPhoto = (ImageView) view.findViewById(R.id.image_third_photo);
        
        setItemType(itemType);
        return view;
    }

    private void setItemType(int itemType){
        this.categoryItem.setText(itemTypes[itemType+1]);

        if(itemType != Constants.PREFERENCE_ITEM_CATEGORY_GAME){
            checkBoxDigitalVersion.setVisibility(View.INVISIBLE);
            titleItem.setVisibility(View.GONE);
            if (itemType == Constants.PREFERENCE_ITEM_CATEGORY_CONSOLE){
                titleItemConsole.setVisibility(View.VISIBLE);
            }else {
                titleItemOthers.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentItemDetailInteractionListener) {
            mListener = (OnFragmentItemDetailInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentItemDetailInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_send_item:
                if(validItem()){
                    this.item = new Item(Preferences.getUserId(getContext() ), Preferences.getUserName(getContext()), descriptionItem.getText().toString(), itemType,
                            priceItem.getText().toString(), checkBoxDigitalVersion.isChecked(), checkBoxSaleType.isChecked());
                    this.item.setImages(photosList);
                    this.item.setImageAmount(photosList.size());
                    Map<String, Object> value = new HashMap<>();
                    value.put("timestamp", ServerValue.TIMESTAMP);
                    this.setItem();
                    this.showLoading();
                    this.presenter.publishNewItem(item);
                }else {
                    Toast.makeText(getContext(), "Faltan campos obligatorios", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.first_photo:
                PHOTO_POSITION = 1;
                tryOpenFileBrowser();
                break;
            case R.id.second_photo:
                PHOTO_POSITION = 2;
                tryOpenFileBrowser();
                break;
            case R.id.third_photo:
                PHOTO_POSITION = 3;
                tryOpenFileBrowser();
                break;
            case R.id.title_item_game:
                DialogSearchGame dialogSearchGame = DialogSearchGame.newInstance();
                dialogSearchGame.setTargetFragment(this, 0);
                dialogSearchGame.show(getFragmentManager(), "DialogSearchGame");
                break;
            case R.id.title_item_console:

        }
    }

    private void setItem() {
        if(itemType == Constants.PREFERENCE_ITEM_CATEGORY_GAME){
            this.item.setGameId(gameSelected.getId());
            this.item.setTitle(gameSelected.getName());
        }else {
            if(itemType == Constants.PREFERENCE_ITEM_CATEGORY_CONSOLE){
                item.setTitle(titleItemConsole.getSelectedItem().toString());
                item.setConsoleId(titleItemConsole.getSelectedItemPosition() - 1);
            }else {
                this.item.setTitle(titleItemOthers.getText().toString());
            }
        }
    }

    private void tryOpenFileBrowser() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showPermissionDescription();
                } else {
                    requestWritePermissions();
                }
            } else {
                this.presenter.openFileBrowser();
            }
        }else {
            this.presenter.openFileBrowser();
        }
    }

    private void showPermissionDescription() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
        builder.setTitle("getString(R.string.read_permission)");
        builder.setMessage("getString(R.string.read_permission_body)");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestWritePermissions();
            }
        });
        builder.show();
    }

    private void requestWritePermissions() {
        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    private boolean validItem() {
        if(completeTitle()){
            if(setImages()){
                if(completeDescription(descriptionItem.getText().toString())){
                    if(completePrice(priceItem.getText().toString())){
                        return true;
                    }else {
                        priceItem.setError("Campo obligatorio");
                        return false;
                    }
                }else {
                    descriptionItem.setError("Campo obligatorio");
                    return false;
                }
            }else {
                YoYo.with(Techniques.Shake).duration(500).playOn(firstPhoto);
                YoYo.with(Techniques.Shake).duration(500).playOn(secondPhoto);
                YoYo.with(Techniques.Shake).duration(500).playOn(thirdPhoto);
                return false;
            }
        }else {
            YoYo.with(Techniques.Shake).duration(500).playOn(titleItem);
            YoYo.with(Techniques.Shake).duration(500).playOn(titleItemConsole);
            YoYo.with(Techniques.Shake).duration(500).playOn(titleItemOthers);
            titleItemOthers.setError("Campo obligatorio");
            return false;
        }
    }

    private boolean setImages() {
        return photosList.size() > 0;
    }

    private boolean completeTitle() {
        return !titleItem.getText().toString().equals("Seleccione juego") || !titleItemConsole.getSelectedItem().toString().equals("Seleccione Consola")
                || !titleItemOthers.getText().toString().equals("");
    }

    private boolean completeDescription(String description) {
        return description.length() > 0;
    }

    private boolean completePrice(String price) {
        String regExp = "[0-9]+([,.][0-9]{1,2})?";
        return price.matches(regExp);
    }

    @Override
    public void startActivityForResultAccessFiles(Intent intent) {
        startActivityForResult(intent, SELECT_FILE_CODE);
    }

    @Override
    public void setPhoto(ArrayList<File> listFilesItemToSend) {
        File compressedImageFileThumbnail;
        switch (PHOTO_POSITION){
            case 1:
                Picasso.with(getContext()).load(listFilesItemToSend.get(0)).fit().centerCrop().error(R.drawable.avatar).into(firstPhoto);
                try {
                    compressedImageFileThumbnail = new Compressor(getContext())
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.WEBP)
                            .compressToFile(listFilesItemToSend.get(0));
                    this.photosList.add(0, compressedImageFileThumbnail);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                Picasso.with(getContext()).load(listFilesItemToSend.get(0)).fit().centerCrop().into(secondPhoto);
                try {
                    compressedImageFileThumbnail = new Compressor(getContext())
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.WEBP)
                            .compressToFile(listFilesItemToSend.get(0));
                    this.photosList.add(1, compressedImageFileThumbnail);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                Picasso.with(getContext()).load(listFilesItemToSend.get(0)).fit().centerCrop().into(thirdPhoto);
                try {
                    compressedImageFileThumbnail = new Compressor(getContext())
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.WEBP)
                            .compressToFile(listFilesItemToSend.get(0));
                    this.photosList.add(2, compressedImageFileThumbnail);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void showLoading() {
        this.layoutLoading.setVisibility(View.VISIBLE);
        this.sendItemButton.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        this.layoutLoading.setVisibility(View.GONE);
        this.sendItemButton.setEnabled(true);
    }

    @Override
    public void publishItemSuccess(Item item) {
        getActivity().finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.presenter.openFileBrowser();
            } else {
                Toast.makeText(getContext(), "getString(R.string.permissions_denied)", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FILE_CODE && resultCode == Activity.RESULT_OK) {
            this.presenter.processFilesSelected(data);
        }
    }

    @Override
    public void selectGame(Game game) {
        this.titleItem.setText(game.getName());
        this.gameSelected = game;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String consoleSelected = consolesArray[position];
    }

    public interface OnFragmentItemDetailInteractionListener {
        void onFragmentInteraction();
    }
}