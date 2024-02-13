package com.example.plantzone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.text.TextUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button b3,b2;
    EditText e1,e2;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        e1=(EditText)findViewById(R.id.user);
        e2=(EditText)findViewById(R.id.pass);
        b2=(Button)findViewById(R.id.button2);
        b3=(Button)findViewById(R.id.button3);
        DB=new DBHelper(this);

        b2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String user=e1.getText().toString();
                String pass=e2.getText().toString();

                if(TextUtils.isEmpty(user)||TextUtils.isEmpty(pass))
                    Toast.makeText(LoginActivity.this,"All Fields Required",Toast.LENGTH_LONG).show();
                else{
                    Boolean checkuserpass=DB.checkuserpassword(user,pass);
                    if(checkuserpass==true){
                        Toast.makeText(LoginActivity.this,"Login Sucessfully",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(),Dashboardactivity.class);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        b3.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent i1 = new Intent(getApplicationContext(),Register.class);
                startActivity(i1);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button3) {


            Intent i1 = new Intent(this, Register.class);
            startActivity(i1);
        }
        if (view.getId() == R.id.button2) {


            Intent i1 = new Intent(this, Dashboardactivity.class);
            Toast.makeText(this, "Login  Successfully", Toast.LENGTH_LONG).show();
            startActivity(i1);
        }
    }
}