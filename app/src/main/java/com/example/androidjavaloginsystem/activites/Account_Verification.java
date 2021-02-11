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

public class Account_Verification extends AppCompatActivity
{
    EditText otp;
    Button verify_account;
    String otp_text, receive_email_from_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_verification);

        Bundle bundle = getIntent().getExtras();
        receive_email_from_intent = bundle.getString("sent_email");

        otp = findViewById(R.id.otp);
        verify_account = findViewById(R.id.verify_account);

        verify_account.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                otp_text = otp.getText().toString();
                if(otp_text.equals(""))
                {
                    Toast.makeText(Account_Verification.this, "Please Fill All this Fields.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(Utility.isNetworkAvailable(Account_Verification.this))
                    {
                        getVerified(receive_email_from_intent, otp_text);
                    }
                    else
                    {
                        Toast.makeText(Account_Verification.this, "No Internet Connection. Please Try Again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            private void getVerified(String email_temp, String otp_temp)
            {
                RequestQueue queue = Volley.newRequestQueue(Account_Verification.this);

                Map<String, String> jsonParams = new HashMap<String, String>();

                jsonParams.put("email", email_temp);
                jsonParams.put("otp", otp_temp);

                // Progress Dialog
                final ProgressDialog progressDialog = new ProgressDialog(Account_Verification.this);
                progressDialog.setMessage("Please Wait Account Verification is in process");
                progressDialog.setTitle("VERIFICATION");
                progressDialog.show();
                progressDialog.setMax(100);
                progressDialog.setCancelable(false);
                progressDialog.setIcon(R.mipmap.ic_launcher);

                //Alert Dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(Account_Verification.this);

                Log.d("EVENT","JSON : "+ new JSONObject(jsonParams));

                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, WebServiceUrls.OTP_VERIFY, new JSONObject(jsonParams), new Response.Listener<JSONObject>()
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

                        if (response_from_api.equals("Verified and your account is activated"))
                        {
                            progressDialog.dismiss();

                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.setTitle("Account Verified");
                            builder.setMessage("Woo-hoo Your Account is Verified and Activated Successfully.\n\nPlease click on 'Log-in' to go into Log-In Page for Login purpose");

                            builder.setPositiveButton("Log-In", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    Intent intent = new Intent(Account_Verification.this, Login_Page.class);

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
                        else if (response_from_api.equals("Something went wrong in OTP Verification"))
                        {
                            progressDialog.dismiss();

                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.setTitle("Something Went Wrong");
                            builder.setMessage("Something Went wrong in Account Verification Process\n\nPlease click on 'Ok' to try again.");

                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    Intent intent = new Intent(Account_Verification.this, Account_Verification.class);
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
                        else if (response_from_api.equals("Not Verified"))
                        {
                            progressDialog.dismiss();

                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.setTitle("Not Verified");
                            builder.setMessage("Oops...!!! some how your entered OTP did not match with our record so Your Account is did not Verified.\n\nPlease click on 'Ok' to try again.");

                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    Intent intent = new Intent(Account_Verification.this, Account_Verification.class);
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
                        else if (response_from_api.equals("Sorry User does not exist into the system please Register yourself first"))
                        {
                            progressDialog.dismiss();

                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.setTitle("User Does Not Exist");
                            builder.setMessage("Sorry User does not exist into the system please Register yourself first\n\nPlease click on 'Register' to get Register in our System");

                            builder.setPositiveButton("Register", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    Intent intent = new Intent(Account_Verification.this, Registration_Page.class);

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
                        else if (response_from_api.equals("Something went wrong in OTP Verification"))
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
                                    Intent intent = new Intent(Account_Verification.this, Account_Verification.class);
                                    intent.putExtra("email_sent", email_temp);
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
                        Toast.makeText(Account_Verification.this, "ERROR : "+error+" \n message" + error.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Account_Verification.this, Login_Page.class);
        startActivity(intent);
        finish();
    }
}