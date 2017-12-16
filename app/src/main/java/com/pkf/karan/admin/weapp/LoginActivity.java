package com.pkf.karan.admin.weapp;


import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pkf.karan.admin.weapp.MainPackage.EngagementsActivity;

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

    String empId, empName, empEmail, empPhone;
    Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userInfo = (UserInformation) getApplicationContext();
        initializeViews();

    }

    private void initializeViews() {

        font  = Typeface.createFromAsset(getAssets(),  "fonts/OpenSans-Regular.ttf");

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

        signIn = (Button) findViewById(R.id.button_signin);
        signIn.setTypeface(font);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();

                if(email.length()>0 && password.length()>0 && (clientRadio.isChecked() | AcsRadio.isChecked() ))
                {
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

    private void Login() {


        OkHttpClient client = new OkHttpClient();
        Log.e("username", email);
        Log.e("password",password);

        FormBody body = new FormBody.Builder()
                .add("EmailID", email)
                .add("Password", password)
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




