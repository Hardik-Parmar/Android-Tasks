package com.example.androidjavaloginsystem.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidjavaloginsystem.R;
import com.example.androidjavaloginsystem.URL.WebServiceUrls;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Welcome_User_Page extends AppCompatActivity
{
    TextView message;
    String receive_name_from_intent;
    Button logout;

    String name_from_api, email_from_api, phone_from_api, password_from_api, status_from_api, otp_from_api;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user_page);

        Bundle bundle = getIntent().getExtras();
        receive_name_from_intent = bundle.getString("email_sent");

        message = findViewById(R.id.message);
        logout = findViewById(R.id.logout);

        // API CALL PART
        RequestQueue queue = Volley.newRequestQueue(Welcome_User_Page.this);

        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("email", receive_name_from_intent);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, WebServiceUrls.FETCH_DATA, new JSONObject(jsonParams), new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    name_from_api = response.getString("name");
                    email_from_api = response.getString("email");
                    phone_from_api = response.getString("phone");
                    password_from_api = response.getString("password");
                    status_from_api = response.getString("status");
                    otp_from_api = response.getString("otp");
                    message.setText("Hello \n"+receive_name_from_intent+" \n You Have Succesfully Logged-In to Our Page.\n\n\nname: - "+name_from_api+"\nemail: - "+email_from_api+"\nphone: - "+phone_from_api+"\npassword: - "+password_from_api+"\nstatus: - "+status_from_api+"\notp: - "+otp_from_api);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(Welcome_User_Page.this, "ERROR : "+error+" \n message" + error.getMessage(), Toast.LENGTH_SHORT).show();
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

        // API CALL PART COMPLETE

        // REMAINING ANOTHER PART OF CODE
        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                User user = new User(Welcome_User_Page.this);
                user.removeUser();

                Intent intent = new Intent(Welcome_User_Page.this, Login_Page.class);
                startActivity(intent);
                finish();
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
            Toast.makeText(Welcome_User_Page.this, "Tap Back Button Again to Exit", Toast.LENGTH_SHORT).show();
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