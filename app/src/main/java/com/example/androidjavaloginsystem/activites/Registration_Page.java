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

public class Registration_Page extends AppCompatActivity
{
    EditText name, email, phone, password;
    Button signup_register;

    String name_text, email_text, phone_text, password_text;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);

        signup_register = findViewById(R.id.signup_register);

        signup_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                name_text = name.getText().toString();
                email_text = email.getText().toString();
                phone_text = phone.getText().toString();
                password_text = password.getText().toString();

                if(name_text.equals("") || email_text.equals("") || phone_text.equals("") || password_text.equals(""))
                {
                    Toast.makeText(Registration_Page.this, "Please Fill All the Fields.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(Utility.isNetworkAvailable(Registration_Page.this))
                    {
                        getRegister(name_text, email_text, phone_text, password_text);
                    }
                    else
                    {
                        Toast.makeText(Registration_Page.this, "No Internet Connection. Please Try Again.", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            private void getRegister(String name_temp, String email_temp, String phone_temp, String password_temp)
            {
                RequestQueue queue = Volley.newRequestQueue(Registration_Page.this);

                Map<String, String> jsonParams = new HashMap<String, String>();

                jsonParams.put("name",name_temp);
                jsonParams.put("email", email_temp);
                jsonParams.put("phone", phone_temp);
                jsonParams.put("password", password_temp);

                // Progress Dialog
                final ProgressDialog progressDialog = new ProgressDialog(Registration_Page.this);
                progressDialog.setMessage("Please Wait Registration is in process");
                progressDialog.setTitle("REGISTRATION");
                progressDialog.show();
                progressDialog.setMax(100);
                progressDialog.setCancelable(false);
                progressDialog.setIcon(R.mipmap.ic_launcher);

                //Alert Dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(Registration_Page.this);

                Log.d("EVENT","JSON : "+ new JSONObject(jsonParams));

                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, WebServiceUrls.REGISTRATION, new JSONObject(jsonParams), new Response.Listener<JSONObject>()
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

                        if (response_from_api.equals("User Already Exist"))
                        {
                            progressDialog.dismiss();

                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.setTitle("User Already Exist");
                            builder.setMessage("The same User Already Exist into the System whose E-mail I'd is as same as yours\n\nPlease click on 'Ok' to try again with different mail I'd");

                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    Intent intent = new Intent(Registration_Page.this, Registration_Page.class);
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
                        else if (response_from_api.equals("Register Fail"))
                        {
                            progressDialog.dismiss();

                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.setTitle("Registration Failed");
                            builder.setMessage("Oops...!!! Something went wrong some how Registration process has been failed.\n\nPlease click on 'Ok' to try again.");

                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    Intent intent = new Intent(Registration_Page.this, Registration_Page.class);
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
                        else if (response_from_api.equals("Something went wrong in Register"))
                        {
                            progressDialog.dismiss();

                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.setTitle("Unexpected Error");
                            builder.setMessage("OOps...!!! Something went wrong there is an unexpected error has occured.\n\nPlease click on 'Ok' to try again");

                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    Intent intent = new Intent(Registration_Page.this, Registration_Page.class);
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
                        else if (response_from_api.equals("Register Success and Email Sent Successfully"))
                        {
                            progressDialog.dismiss();

                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.setTitle("Alert");
                            builder.setMessage("Woo-Hoo Your Registration has been successfully completed\n\nPlease click on 'Verify Account' to Verify and Activate Your Account");

                            builder.setPositiveButton("Verify Account", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Registration_Page.this, Account_Verification.class);
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
                                    Intent intent = new Intent(Registration_Page.this, Registration_Page.class);
                                    startActivity(intent);
                                    finish();
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
                        Toast.makeText(Registration_Page.this, "ERROR : "+error+" \n message" + error.getMessage(), Toast.LENGTH_SHORT).show();
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
    }

    /*boolean exit = false;

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Registration_Page.this, Login_Page.class);
        startActivity(intent);
        finish();
        /*if(exit)
        {
            finish();
        }
        else
        {
            Toast.makeText(Registration_Page.this, "Tap Back Button Again to Exit", Toast.LENGTH_SHORT).show();
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
    }*/
}