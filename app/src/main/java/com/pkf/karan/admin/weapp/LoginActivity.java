package com.pkf.karan.admin.weapp;


import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.pkf.karan.admin.weapp.MainPackage.EngagementsActivity;
import com.pkf.karan.admin.weapp.MainPackage.FirebaseInstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {

    Button signIn;
    EditText emailEditText, passwordEditText;
    String email, password, responseString, responseStatus;
    ProgressBar progressBar;
    TextInputLayout emailLayout, passwordLayout;
    UserInformation userInfo;
    RadioButton clientRadio, AcsRadio;
    CheckBox rememberMe;

    String empId, empName, empEmail, empPhone;
    Typeface font;
    SharedPreferences sharedpreferences;
    FirebaseInstance currentInstance;
    TextView forgotPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userInfo = (UserInformation) getApplicationContext();

        sharedpreferences = getPreferences(Context.MODE_PRIVATE);

        if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }

        initializeViews();

    }

    private void initializeViews() {

        font  = Typeface.createFromAsset(getAssets(),  "fonts/OpenSans-Regular.ttf");

        currentInstance = new FirebaseInstance();
        currentInstance.onTokenRefresh();

        forgotPassword = (TextView)findViewById(R.id.forgotPassword);
        forgotPassword.setTypeface(font);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailEditText.getText().length()>0)
                {
                    sendResetPasswordEmail(v);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please enter the Email Id", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rememberMe = (CheckBox)findViewById(R.id.rememberMe);
        rememberMe.setTypeface(font);

        clientRadio = (RadioButton)findViewById(R.id.clientRadio);
        AcsRadio = (RadioButton)findViewById(R.id.acsRadio);
        emailLayout = (TextInputLayout)findViewById(R.id.input_layout_email);
        passwordLayout = (TextInputLayout)findViewById(R.id.input_layout_password);
        emailLayout.setTypeface(font);
        passwordLayout.setTypeface(font);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        emailEditText = (EditText) findViewById(R.id.input_email);
        passwordEditText = (EditText) findViewById(R.id.input_password);

        emailEditText.setTypeface(font);
        passwordEditText.setTypeface(font);

        emailEditText.setText(sharedpreferences.getString("Email", ""));
        passwordEditText.setText(sharedpreferences.getString("Password", ""));

        signIn = (Button) findViewById(R.id.button_signin);
        signIn.setTypeface(font);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();

                if(email.length()>0 && password.length()>0 && (clientRadio.isChecked() | AcsRadio.isChecked() ))
                {
                    if(rememberMe.isChecked())
                    {
                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString("Email", email);
                        editor.putString("Password", password);
                        editor.apply();
                    }
                    else {
                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString("Email", "");
                        editor.putString("Password", "");
                        editor.apply();
                    }

                    progressBar.setVisibility(View.VISIBLE);
                    if(clientRadio.isChecked()) userInfo.setServerUrl("http://35.154.18.27:10002");
                    else userInfo.setServerUrl("http://13.127.11.204:10002");

                    Login();

                }

                else
                {
                    Toast.makeText(getApplicationContext(),"Please fill all the fields first",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void sendResetPasswordEmail(final View v) {

        progressBar.setVisibility(View.VISIBLE);

        if(clientRadio.isChecked()) userInfo.setServerUrl("http://35.154.18.27:10002");
        else if(AcsRadio.isChecked()) userInfo.setServerUrl("http://13.127.11.204:10002");

        OkHttpClient client = new OkHttpClient();
        email = emailEditText.getText().toString();

        FormBody body = new FormBody.Builder()
                .add("EmailID", email)
                .build();


        final Request request = new Request.Builder()
                .url(userInfo.getServerUrl() + "/api/LoginApi/ForgetPassword").post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        {

                            progressBar.setVisibility(View.GONE);
                            responseStatus = String.valueOf(response.code());
                            if(responseStatus.equals("200"))
                            {
                                showPasswordChangedDialog("200", v);
                            }
                            else if(responseStatus.equals("204"))
                            {
                                showPasswordChangedDialog("204", v);
                            }
                        }

                    }
                });
            }
        });


    }

    private void showPasswordChangedDialog(String s, View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(s.equals("200"))
        {
            builder.setMessage("A Password Reset link has been sent to your email Id.");
        }

        else if(s.equals("204"))
        {
            builder.setMessage("This user does not exist.");
        }

        builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTypeface(font);
    }

    private void Login() {

        Log.e("deviceId", currentInstance.getRefreshedToken());

        OkHttpClient client = new OkHttpClient();
        Log.e("username", email);
        Log.e("password",password);

        FormBody body = new FormBody.Builder()
                .add("EmailID", email)
                .add("Password", password)
                .add("DeviceId", currentInstance.getRefreshedToken())
                .build();


        final Request request = new Request.Builder()
                .url(userInfo.getServerUrl() + "/api/LoginApi/ValidateEmployeeLogin").post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        {

                            responseStatus = String.valueOf(response.code());

                            if(responseStatus.equals("200"))
                            {
                                try {
                                    responseString = response.body().string();
                                    Log.e("details",responseString);
                                    JSONObject details = new JSONObject(responseString);
                                    empId = details.getString("EmployeeId");
                                    empName = details.getString("EmployeeName");
                                    empEmail = details.getString("EmailId");
                                    empPhone = details.getString("ContactNumber");

                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                }

                                progressBar.setVisibility(View.GONE);

                                Intent intent = new Intent(LoginActivity.this, EngagementsActivity.class);
                                intent.putExtra("empId", empId);
                                intent.putExtra("empName", empName);
                                userInfo.setUserId(empId);
                                userInfo.setUserName(empName);
                                userInfo.setUserEmail(empEmail);
                                userInfo.setUserPhone(empPhone);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                            }
                        }

                    }
                });
            }
        });


    }
}




