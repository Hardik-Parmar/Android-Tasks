package com.example.androidjavaloginsystem.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidjavaloginsystem.R;
import com.example.androidjavaloginsystem.Support.Utility;
import com.example.androidjavaloginsystem.URL.WebServiceUrls;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Login_Page extends AppCompatActivity
{
    EditText email, password;
    Button login, signup;

    String email_text, password_text;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                email_text = email.getText().toString();
                password_text = password.getText().toString();

                if(email_text.equals("") || password_text.equals(""))
                {
                    Toast.makeText(Login_Page.this, "Please Fill All the Fields.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(Utility.isNetworkAvailable(Login_Page.this))
                    {
                        getLogin(email_text, password_text);
                    }
                    else
                    {
                        Toast.makeText(Login_Page.this, "No Internet Connection. Please Try Again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            private void getLogin(String email_temp, String password_temp)
            {
                RequestQueue queue = Volley.newRequestQueue(Login_Page.this);

                Map<String, String> jsonParams = new HashMap<String, String>();

                jsonParams.put("email", email_temp);
                jsonParams.put("password", password_temp);

                // Progress Dialog
                final ProgressDialog progressDialog = new ProgressDialog(Login_Page.this);
                progressDialog.setMessage("Please Wait Login task is in process");
                progressDialog.setTitle("LOG-IN");
                progressDialog.show();
                progressDialog.setMax(100);
                progressDialog.setCancelable(false);
                progressDialog.setIcon(R.mipmap.ic_launcher);

                //Alert Dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(Login_Page.this);

                Log.d("EVENT","JSON : "+ new JSONObject(jsonParams));

                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, WebServiceUrls.LOGIN, new JSONObject(jsonParams), new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        String response_from_api = null;

                        try
                        {
                            response_from_api = (String) response.get("return_message");
                            Log.d("response message", "" + response_from_api);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        if (response_from_api.equals("Please Activate Your Account"))
                        {
                            progressDialog.dismiss();

                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.setTitle("Please Activate Your Account");
                            builder.setMessage("Please click on 'Verify Account' to Verify and Activate Your Account\n\nAfter Verifying Your Acount you are able to do Log-in Successfully.");

                            builder.setPositiveButton("Verify Account", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    Intent intent = new Intent(Login_Page.this, Account_Verification.class);
                                    intent.putExtra("sent_email", email_temp);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                        else if (response_from_api.equals("Login Success"))
                        {
                            progressDialog.dismiss();

                            User user = new User(Login_Page.this);
                            user.setName(email_temp);
                            user.setPass(password_temp);


                            Intent intent = new Intent(Login_Page.this, Welcome_User_Page.class);
                            intent.putExtra("email_sent", email_temp);
                            startActivity(intent);
                            finish();
                        }
                        else if (response_from_api.equals("Login Fail"))
                        {
                            progressDialog.dismiss();

                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.setTitle("Login Failed");
                            builder.setMessage("Oops...!!! You have entered wrong Password.\n\nPlease click on 'Ok' to try again.");

                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Login_Page.this, Login_Page.class);

                                    startActivity(intent);
                                    finish();
                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }
                        else if (response_from_api.equals("Sorry User does not exist into the system please Register yourself first"))
                        {
                            progressDialog.dismiss();

                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.setTitle("User Does Not Exist");
                            builder.setMessage("Sorry User does not exist into the system please Register yourself first\n\nPlease click on 'Register' to get Register in our System");

                            builder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Login_Page.this, Registration_Page.class);

                                    startActivity(intent);

                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }
                        else if (response_from_api.equals("Something went wrong in Login"))
                        {
                            progressDialog.dismiss();

                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.setTitle("Unexpected Error");
                            builder.setMessage("OOps...!!! Something went wrong there is an unexpected error has been occurred.\n\nPlease click on 'Ok' to try again");

                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    Intent intent = new Intent(Login_Page.this, Login_Page.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }

                        Log.d("RESPONSE", "JSON "+ response);

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(Login_Page.this, "ERROR : "+error+" \n message" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                postRequest.setRetryPolicy(new RetryPolicy()
                {
                    @Override
                    public int getCurrentTimeout()
                    {
                        return 50000;
                    }

                    @Override
                    public int getCurrentRetryCount()
                    {
                        return 50000;
                    }

                    @Override
                    public void retry(VolleyError error) throws VolleyError
                    {

                    }
                });

                queue.add(postRequest);

            }
        });

        signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Login_Page.this, Registration_Page.class);
                startActivity(intent);
                //finish();
            }
        });
    }


    boolean exit = false;

    @Override
    public void onBackPressed()
    {
        if(exit)
        {
            finish();
        }
        else
        {
            Toast.makeText(Login_Page.this, "Tap Back Button Again to Exit", Toast.LENGTH_SHORT).show();
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                exit = false;
            }
        },2000);
        exit = true;
    }
}