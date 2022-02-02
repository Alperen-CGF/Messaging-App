package com.example.corumgaz;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity{

    public static FirebaseAuth auth;
    public static FirebaseUser user;
    private EditText input_emaill,input_parolal;
    Button girisyapl, cikisyapl , kayitoll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tanimlaa();




    }

    public void tanimlaa() {



        input_emaill = (EditText) findViewById(R.id.input_emaill);
        input_parolal = (EditText) findViewById(R.id.input_parolal);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {

        kayitoll = (Button) findViewById(R.id.kayitoll);
        kayitoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent = new Intent(MainActivity.this, KayitOlActivity.class);
                startActivity(ıntent);

            }
        });
        cikisyapl = (Button) findViewById(R.id.cikisyapl);
        cikisyapl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        girisyapl = (Button) findViewById(R.id.girisyapl);
        girisyapl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = input_emaill.getText().toString();
                String parola = input_parolal.getText().toString();
                if (!email.equals("") && !parola.equals("")) {
                    sistemegiris(email, parola);
                } else {
                    Toast.makeText(getApplicationContext(), "Lütfen Boş Duran Alanları Doldurunuz.", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
        else {
            Intent ıntent=new Intent(MainActivity.this,AnaActivity.class);
            startActivity(ıntent);
            finish();
        }

    }


    public void sistemegiris(String email,String parola){
        auth.signInWithEmailAndPassword(email, parola).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent ıntent=new Intent(MainActivity.this,MainActivity.class);
                    startActivity(ıntent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"E-mail ve Parola Hatalıdır.Lütfen Kayıt Olun!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}


