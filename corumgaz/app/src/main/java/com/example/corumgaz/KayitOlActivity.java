package com.example.corumgaz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class KayitOlActivity extends AppCompatActivity {

    EditText input_email, input_parola;
    Button kayitol, cikisyap, geri;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);
        tanimla();



    }


    public void tanimla() {
        input_email = (EditText) findViewById(R.id.input_email);
        input_parola = (EditText) findViewById(R.id.input_parola);
        kayitol = (Button) findViewById(R.id.kayitol);
        geri=(Button) findViewById(R.id.geri);
        cikisyap = (Button) findViewById(R.id.cikisyap);
        auth = FirebaseAuth.getInstance();
        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent=new Intent(KayitOlActivity.this,MainActivity.class);
                startActivity(ıntent);
                finish();
            }
        });
        cikisyap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        kayitol.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                String email=input_email.getText().toString();
                String parola=input_parola.getText().toString();
                if(!email.equals("") && !parola.equals("")){
                    input_email.setText("");
                    input_parola.setText("");
                    kayitolma(email,parola);
                }

                else{
                    Toast.makeText(getApplicationContext(),"Bilgileri Boş Giremezsiniz",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void kayitolma(String email,String parola)
    {

        auth.createUserWithEmailAndPassword(email,parola).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    reference = firebaseDatabase.getReference().child("Kullanicilar").child(auth.getUid());
                    Map map=new HashMap();
                    map.put("resim","null");
                    map.put("isim","null");
                    map.put("egitim","null");
                    map.put("hakkımda","null");
                    map.put("dogumtarih","null");

                    reference.setValue(map);


                    Intent ıntent=new Intent(KayitOlActivity.this,MainActivity.class);
                    startActivity(ıntent);
                    finish();
                    Toast.makeText(getApplicationContext(),"Başarılı Bir Şekilde Kayıt Oldunuz.",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Kayit Olma İşlemi Başarısız.Lütfen Tekrar Deneyiniz.",Toast.LENGTH_LONG).show();

                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        finish();
    }

}
