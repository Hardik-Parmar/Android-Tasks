package com.example.androidjavaloginsystem.activites;

import android.content.Context;
import android.content.SharedPreferences;

public class User
{
    Context context;

    SharedPreferences sharedPreferences;

    public User(Context context)
    {
        this.context = context;
        sharedPreferences= context.getSharedPreferences("login_details",Context.MODE_PRIVATE);
    }
    private String name,pass;

    public String getName()
    {
        name = sharedPreferences.getString("user name","");

        return name;
    }

    public void setName(String name)
    {
        this.name = name;

        sharedPreferences.edit().putString("user name",name).commit();
    }

    public String getPass()
    {
        pass = sharedPreferences.getString("password", "");

        return pass;
    }

    public void setPass(String pass)
    {
        this.pass = pass;

        sharedPreferences.edit().putString("password",pass).commit();
    }

    public void removeUser()
    {
        sharedPreferences.edit().clear().commit();
    }
}