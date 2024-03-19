package com.example.plantzone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText e1, e2;
    Button b2, b3;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        e1 = findViewById(R.id.user);
        e2 = findViewById(R.id.pass);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        DB = new DBHelper(this);

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String user = e1.getText().toString();
                String pass = e2.getText().toString();

                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass))
                    Toast.makeText(LoginActivity.this, "All Fields Required", Toast.LENGTH_LONG).show();
                else {
                    Boolean checkuserpass = DB.checkuserpassword(user, pass);
                    if (checkuserpass) {
                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), Dashboardactivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i1 = new Intent(getApplicationContext(), Register.class);
                startActivity(i1);
            }
        });
    }
}
