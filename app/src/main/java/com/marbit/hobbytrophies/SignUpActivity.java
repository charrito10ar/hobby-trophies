package com.marbit.hobbytrophies;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.marbit.hobbytrophies.dialogs.DialogAlertLogin;
import com.marbit.hobbytrophies.dialogs.DialogGeneric;
import com.marbit.hobbytrophies.model.User;
import com.marbit.hobbytrophies.utilities.Constants;
import com.marbit.hobbytrophies.utilities.DialogCodes;
import com.marbit.hobbytrophies.utilities.Preferences;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends BaseActivity implements DialogGeneric.OnDialogGenericInteractionListener {

    private static final String TAG = "AnonymousAuth";

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    private View layoutInputUserName;
    private EditText editTextUserName;
    private ProgressBar progressBar;
    private TextView buttonAccept;
    private View relativeLayoutAccept;

    private View layoutInputCodeGenerated;
    private TextView textViewRandomPsnCode;
    private ProgressBar progressBarValidateCode;
    private TextView buttonAcceptValidateCode;
    private TextView titleValidateCode;

    private ImageView icLogoBottom;


    //private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //mDatabase = FirebaseDatabase.getInstance().getReference();
        this.requestQueue = Volley.newRequestQueue(getApplicationContext());
        this.layoutInputUserName = (RelativeLayout) findViewById(R.id.layout_input_user_name);
        this.editTextUserName = (EditText) findViewById(R.id.edit_text_user_name);
        this.editTextUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                if(charSequence.length() > 0){
                    relativeLayoutAccept.setEnabled(true);
                    buttonAccept.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.material_white));
                }else {
                    relativeLayoutAccept.setEnabled(false);
                    buttonAccept.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.material_grey_400));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        this.progressBar = (ProgressBar) findViewById(R.id.progress_bar_sign_up);
        this.buttonAccept = (TextView) findViewById(R.id.text_view_button_accept);
        this.relativeLayoutAccept = (RelativeLayout) findViewById(R.id.button_sign_up_accept);
        this.relativeLayoutAccept.setEnabled(false);

        this.layoutInputCodeGenerated = (RelativeLayout) findViewById(R.id.layout_input_code_generated);
        this.textViewRandomPsnCode = (TextView) findViewById(R.id.text_view_random_psn_code);
        this.textViewRandomPsnCode.setText(Preferences.getString(getApplicationContext(), Constants.PREFERENCE_RANDOM_PSN_CODE));
        this.buttonAcceptValidateCode = (TextView) findViewById(R.id.text_view_button_validate_code);
        this.progressBarValidateCode = (ProgressBar) findViewById(R.id.progress_bar_validate_code);
        this.titleValidateCode = (TextView) findViewById(R.id.title_code_generated);

        this.icLogoBottom = (ImageView) findViewById(R.id.login_logo_bottom);

        this.setLayout();
    }

    private void setLayout() {
        if(Preferences.getBoolean(getApplicationContext(), Constants.PREFERENCE_IS_PSN_CODE_GENERATED)){
            this.titleValidateCode.setText(getString(R.string.title_validate_code, Preferences.getString(getApplicationContext(), Constants.PREFERENCE_USER_NAME)));
            this.layoutInputUserName.setVisibility(View.INVISIBLE);
            this.layoutInputCodeGenerated.setVisibility(View.VISIBLE);
            this.icLogoBottom.setVisibility(View.VISIBLE);
        }else {
            this.layoutInputUserName.setVisibility(View.VISIBLE);
            this.layoutInputCodeGenerated.setVisibility(View.INVISIBLE);
            this.icLogoBottom.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        getWindow().getDecorView().clearFocus();
    }

    public void clickSignUp(View view){
        this.showProgressBar();
        this.stringRequest = this.getStringRequest(this.editTextUserName.getText().toString());
        requestQueue.add(stringRequest);
        this.editTextUserName.onEditorAction(EditorInfo.IME_ACTION_DONE);
    }

    private void showProgressBar(){
        this.progressBar.setVisibility(View.VISIBLE);
        this.buttonAccept.setVisibility(View.INVISIBLE);
    }

    private void hideProgressBar(){
        this.progressBar.setVisibility(View.INVISIBLE);
        this.buttonAccept.setVisibility(View.VISIBLE);
    }

    private void showProgressBarValidateCode(){
        this.progressBarValidateCode.setVisibility(View.VISIBLE);
        this.buttonAcceptValidateCode.setVisibility(View.INVISIBLE);
    }

    private void hideProgressBarValidateCode(){
        this.progressBarValidateCode.setVisibility(View.INVISIBLE);
        this.buttonAcceptValidateCode.setVisibility(View.VISIBLE);
    }

    private StringRequest getStringRequest(final String psnName){
        return new StringRequest(Request.Method.POST,
                "http://www.hobbytrophies.com/foros/ps3/class/psnAPI.php?method=psnAuthUser&sPSNID=" + psnName,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String responseCode = jsonObject.getString("success");

                            if(responseCode.equals("0")){
                                hideProgressBar();
                                Toast.makeText(getApplicationContext(), jsonObject.getString("error_message"), Toast.LENGTH_LONG).show();
                            }else {
                                if(Preferences.getBoolean(getApplicationContext(), Constants.PREFERENCE_IS_PSN_CODE_GENERATED)){
                                    hideProgressBarValidateCode();
                                    String userAuthenticated = jsonObject.getString("authenticated");
                                    if(userAuthenticated.equals("0")){//VALIDACION
                                        Toast.makeText(getApplicationContext(), "CODIGOS VALIDOS", Toast.LENGTH_LONG).show();
                                        Preferences.saveBoolean(getApplicationContext(), Constants.PREFERENCE_IS_PSN_CODE_OK, true);
                                        signInDataBase(psnName);
                                    }else {
                                        Toast.makeText(getApplicationContext(), "CÓDIGO INGRESADO EN LA PSN INVALIDO", Toast.LENGTH_LONG).show();
                                    }
                                }else {
                                    hideProgressBar();
                                    Preferences.saveString(getApplicationContext(), Constants.PREFERENCE_USER_NAME, psnName);
                                    saveGeneratedCode(jsonObject.getString("authentication_key"));
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            hideProgressBar();
                            Toast.makeText(SignUpActivity.this, "Error de formato en la respuesta", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
                hideProgressBar();
                Toast.makeText(SignUpActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveGeneratedCode(String code) {
        Preferences.saveString(getApplicationContext(), Constants.PREFERENCE_RANDOM_PSN_CODE, code);
        this.textViewRandomPsnCode.setText(String.valueOf(code));
        Preferences.saveBoolean(getApplicationContext(), Constants.PREFERENCE_IS_PSN_CODE_GENERATED, true);
        this.setLayout();
    }

    public void clickQuestionMark(View view){
        DialogGeneric dialogGeneric = DialogGeneric.newInstance("Usuario PSN", "Este es el usuario con el que estás registrado en la PSN. Ingresalo tal cual, respetando mayúsculas y minúsculas");
        dialogGeneric.show(getSupportFragmentManager(), "DialogHelp");
    }

    public void clickQuestionMarkInputCode(View view){
        DialogGeneric dialogGeneric = DialogGeneric.newInstance("¿Dónde ingresar el código?", "Si tienes: -PS3: Administración de cuentas->Información de cuenta->Perfil->Acerca de mi. \n" +
                "- Vita o PS4: Ajustes->Playstation network->Perfil->Acerma de mi ");
        dialogGeneric.show(getSupportFragmentManager(), "DialogHelpInputCode");
    }

    public void clickValidateCode(View view){
        this.showProgressBarValidateCode();
        String userName = Preferences.getString(getApplicationContext(), Constants.PREFERENCE_USER_NAME);
        Toast.makeText(getApplicationContext(), "Validando Codigo de: " + userName, Toast.LENGTH_SHORT).show();
        this.stringRequest = this.getStringRequest(userName);
        requestQueue.add(this.stringRequest);
    }

    public void clickChangeUser(View view){
        this.resetUser();
        this.setLayout();
    }

    private void resetUser() {
        Preferences.saveBoolean(getApplicationContext(), Constants.PREFERENCE_IS_PSN_CODE_GENERATED, false);
        Preferences.saveBoolean(getApplicationContext(), Constants.PREFERENCE_IS_PSN_CODE_OK, false);
        Preferences.saveString(getApplicationContext(), Constants.PREFERENCE_USER_NAME, "");
        Preferences.saveString(getApplicationContext(), Constants.PREFERENCE_RANDOM_PSN_CODE, "");
    }

    public void skipSignUp(View view){
        DialogGeneric dialogGeneric = DialogGeneric.newInstance("Atención", "Si saltas el registro no podrás: \n - Crear quedadas \n - Ver tu perfil PSN \n - Comentar en las quedadas", "Saltar registro", DialogCodes.DIALOG_ACTION_INPUT_LOGIN_CODE_HELP);
        dialogGeneric.show(getSupportFragmentManager(), "DialogInputLoginCodeHelp");
    }


    private void signInDataBase(String psnName) {
        this.showProgressDialog();
        Preferences.saveBoolean(getApplicationContext(), Constants.PREFERENCE_IS_USER_LOGIN, true);
        this.writeNewUser(psnName);
        this.goToMainActivity();
    }

    public void writeNewUser(String psnName) {
        this.stringRequest = this.getStringRequestSignUp(psnName);
        requestQueue.add(this.stringRequest);
    }


    private StringRequest getStringRequestSignUp(String psnName){
        return new StringRequest(Request.Method.POST,
                "http://www.hobbytrophies.com/foros/ps3/inc/sign-up-user-app.php?sPSNID=" + psnName,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int responseCode = jsonObject.getInt("code");

                            if(responseCode == 1){
                                Toast.makeText(getApplicationContext(), "Nuevo usuario creado en Hobby Trophies" , Toast.LENGTH_LONG).show();
                            }else {
                                if(responseCode == 2) {
                                    Toast.makeText(getApplicationContext(), "Bienvenido de nuevo", Toast.LENGTH_LONG).show();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SignUpActivity.this, "Error de formato en la respuesta", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
                hideProgressBar();
                Toast.makeText(SignUpActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onDialogGenericInteraction(int code) {
        switch (code){
            case DialogCodes.DIALOG_ACTION_INPUT_LOGIN_CODE_HELP:
                this.goToMainActivity();
                break;
        }
    }

    private void goToMainActivity() {
        Intent intentDashboard = new Intent(getApplicationContext(), MainActivity.class);
        intentDashboard.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentDashboard);
    }
}
