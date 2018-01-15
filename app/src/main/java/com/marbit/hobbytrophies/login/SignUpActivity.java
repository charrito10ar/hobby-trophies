package com.marbit.hobbytrophies.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.marbit.hobbytrophies.BaseActivity;
import com.marbit.hobbytrophies.MainActivity;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.dialogs.DialogGeneric;
import com.marbit.hobbytrophies.firebase.dao.FirebaseNotificationDAO;
import com.marbit.hobbytrophies.login.interfaces.SignUpActivityView;
import com.marbit.hobbytrophies.utilities.Constants;
import com.marbit.hobbytrophies.utilities.DialogCodes;
import com.marbit.hobbytrophies.utilities.Preferences;
import com.marbit.hobbytrophies.login.presenters.*;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends BaseActivity implements DialogGeneric.OnDialogGenericInteractionListener, SignUpActivityView, TextWatcher {

    private static final String TAG = "AnonymousAuth";
    private static final int RC_SIGN_IN = 9001;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private SignUpPresenter presenter;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;

    @BindView(R.id.layout_input_user_name) RelativeLayout layoutInputUserName;
    @BindView(R.id.edit_text_user_name) EditText editTextUserName;
    @BindView(R.id.progress_bar_sign_up) ProgressBar progressBar;
    @BindView(R.id.text_view_button_accept) TextView buttonAccept;
    @BindView(R.id.button_sign_up_accept) RelativeLayout relativeLayoutAccept;
    @BindView(R.id.layout_input_code_generated) RelativeLayout layoutInputCodeGenerated;
    @BindView(R.id.text_view_random_psn_code) TextView textViewRandomPsnCode;
    @BindView(R.id.text_view_button_validate_code) TextView buttonAcceptValidateCode;
    @BindView(R.id.progress_bar_validate_code) ProgressBar progressBarValidateCode;
    @BindView(R.id.title_code_generated) TextView titleValidateCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        ButterKnife.bind(this);
        this.presenter = new SignUpPresenter(getApplicationContext(), this);
        this.requestQueue = Volley.newRequestQueue(getApplicationContext());
        this.editTextUserName.addTextChangedListener(this);
        this.relativeLayoutAccept.setEnabled(false);
        this.textViewRandomPsnCode.setText(Preferences.getString(getApplicationContext(), Constants.PREFERENCE_RANDOM_PSN_CODE));
        this.setLayout();

        mCallbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

        // [START initialize_fblogin]
        LoginButton loginButton = findViewById(R.id.sign_in_button_facebook);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
            @Override
            public void onError(FacebookException error) {
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        });
        // [END initialize_fblogin]
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_facebook]

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                updateUI(null);
            }
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        showProgressDialog();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        hideProgressDialog();
                    }
                });
    }

    @Override
    public void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            Preferences.saveUserName(getApplicationContext(), user.getDisplayName());
            Preferences.saveUserId(getApplicationContext(), user.getUid());
            Preferences.saveString(getApplicationContext(), Constants.PREFERENCE_USER_AVATAR, user.getPhotoUrl().toString());
            Preferences.saveBoolean(getApplicationContext(), Constants.PREFERENCE_IS_USER_LOGIN, true);
            registerTokenNotification(user.getUid());
            this.goToMainActivity();
        }else {
            Toast.makeText(getApplicationContext(), "No se pudo iniciar sesión", Toast.LENGTH_LONG).show();
        }
    }

    private void setLayout() {
        if(Preferences.getBoolean(getApplicationContext(), Constants.PREFERENCE_IS_PSN_CODE_GENERATED)){
            this.titleValidateCode.setText(getString(R.string.title_validate_code, Preferences.getUserName(getApplicationContext())));
            this.layoutInputUserName.setVisibility(View.INVISIBLE);
            this.layoutInputCodeGenerated.setVisibility(View.VISIBLE);
        }else {
            this.layoutInputUserName.setVisibility(View.VISIBLE);
            this.layoutInputCodeGenerated.setVisibility(View.INVISIBLE);
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
                                    if(userAuthenticated.equals("1")){//VALIDACION
                                        Toast.makeText(getApplicationContext(), "CODIGOS VALIDOS", Toast.LENGTH_LONG).show();
                                        Preferences.saveBoolean(getApplicationContext(), Constants.PREFERENCE_IS_PSN_CODE_OK, true);
                                        signInDataBase(psnName);
                                    }else {
                                        Toast.makeText(getApplicationContext(), "CÓDIGO INGRESADO EN LA PSN INVALIDO", Toast.LENGTH_LONG).show();
                                    }
                                }else {
                                    hideProgressBar();
                                    Preferences.saveUserName(getApplicationContext(), psnName);
                                    Preferences.saveUserId(getApplicationContext(), psnName);
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
        String userName = Preferences.getUserName(getApplicationContext());
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
        Preferences.saveUserName(getApplicationContext(), "");
        Preferences.saveUserId(getApplicationContext(), "");
        Preferences.saveString(getApplicationContext(), Constants.PREFERENCE_RANDOM_PSN_CODE, "");
    }

    public void skipSignUp(View view){
        DialogGeneric dialogGeneric = DialogGeneric.newInstance("Atención", "Si saltas el registro no podrás: \n - Crear quedadas \n - Ver tu perfil PSN \n - Comentar en las quedadas \n - Participar del ranking semanal \n - Comprar o vender en el mercadillo" , "Saltar registro", DialogCodes.DIALOG_ACTION_INPUT_LOGIN_CODE_HELP);
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
        registerTokenNotification(psnName);
    }

    private void registerTokenNotification(String psnName) {
        String tokenNotification = FirebaseInstanceId.getInstance().getToken();
        if(tokenNotification != null){
            FirebaseNotificationDAO firebaseNotificationDAO = new FirebaseNotificationDAO();
            firebaseNotificationDAO.registerToken(psnName, tokenNotification);
        }
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
            case DialogCodes.DIALOG_ACTION_INPUT_LOGIN_GOOGLE:
                presenter.loginGoogle();
                break;
        }
    }

    private void goToMainActivity() {
        Intent intentDashboard = new Intent(getApplicationContext(), MainActivity.class);
        intentDashboard.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentDashboard);
    }

    @Override
    public void clickGoogleButton(View view) {
        DialogGeneric dialogGeneric = DialogGeneric.newInstance("Atención", "Al iniciar sesión con google no podrás ver tu perfil en PSN, crear quedadas ni participar en el ranking mensual", "Login con Google", DialogCodes.DIALOG_ACTION_INPUT_LOGIN_GOOGLE);
        dialogGeneric.show(getSupportFragmentManager(), "DialogInputLoginCodeHelp");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if(charSequence.length() > 0){
            relativeLayoutAccept.setEnabled(true);
            buttonAccept.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.material_white));
        }else {
            relativeLayoutAccept.setEnabled(false);
            buttonAccept.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.material_grey_400));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {    }

    public void clickFacebookButton(View view) {

    }
}