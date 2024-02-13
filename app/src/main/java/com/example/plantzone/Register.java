package com.example.plantzone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.text.TextUtils;
import android.view.Menu;
import android.widget.EditText;




public class Register extends AppCompatActivity  implements View.OnClickListener {
    EditText e1,e2,e3;
    Button b1,b2;
    DBHelper DB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        e1=(EditText)findViewById(R.id.user);
        e2=(EditText)findViewById(R.id.password);
        e3=(EditText)findViewById(R.id.pass);
        b1=(Button)findViewById(R.id.button1);
        b2=(Button)findViewById(R.id.button2);
        DB=new DBHelper(this);

        b1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String user=e1.getText().toString();
                String pass=e2.getText().toString();
                String repass=e3.getText().toString();

                if(TextUtils.isEmpty(user)||TextUtils.isEmpty(pass)||TextUtils.isEmpty(repass))
                    Toast.makeText(Register.this,"All Fields Required",Toast.LENGTH_LONG).show();
                else{
                    if(pass.equals(repass)){
                        Boolean checkuser =DB.checkusername(user);
                        if(checkuser==false){
                            Boolean insert=DB.insertData(user, pass);
                            if(insert==true){
                                Toast.makeText(Register.this,"Registered Successfully",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(Register.this,"Registration Failed",Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(Register.this,"Users already Exists",Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(Register.this,"Password are not matching",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });

    }


    @Override
    public void onClick(View view) {


    }


}
