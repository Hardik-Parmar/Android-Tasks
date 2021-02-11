
package com.example.androidjavaloginsystem.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.androidjavaloginsystem.R;

import java.util.Timer;
import java.util.TimerTask;

public class Spalsh extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);

        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                User user = new User(Spalsh.this);
                if(user.getName() != "")
                {
                    Intent intent = new Intent(Spalsh.this, Welcome_User_Page.class);
                    intent.putExtra("email_sent",user.getName());
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(Spalsh.this, Login_Page.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }
}