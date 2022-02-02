package com.example.corumgaz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import Models.Kullanicilar;
import de.hdodenhof.circleimageview.CircleImageView;

public class KullaniciProfileActivity extends AppCompatActivity {
    static final int SElECT_IMAGE=5;
    //String imageurl;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database,firebaseDatabase;
    DatabaseReference reference,reference1;
    EditText kullaniciismi,input_egitim,input_dogumtarih,input_hakkında;
    TextView arkadastext2,takipcitext2;
    CircleImageView profile_image;
    Button güncelle,geridonme;
    StorageReference storageReference;
    String userid;
    private String firebaseImage ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_profile);

        tanımla();
        bilgileriGetir();
    }
    public void tanımla(){

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userid = user.getUid();
        database=FirebaseDatabase.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Kullanicilar").child(user.getUid());
        reference1 = firebaseDatabase.getReference();

        kullaniciismi=findViewById(R.id.kullaniciismi);
        input_egitim=findViewById(R.id.input_egitim);
        input_dogumtarih=findViewById(R.id.input_dogumtarih);
        input_hakkında=findViewById(R.id.input_hakkında);
        profile_image=findViewById(R.id.profile_image);

        arkadastext2=(TextView) findViewById(R.id.arkadastext2);
        takipcitext2=(TextView) findViewById(R.id.takipcitext2);
        storageReference = FirebaseStorage.getInstance().getReference();


        geridonme=findViewById(R.id.geridonme);
        geridonme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent = new Intent(KullaniciProfileActivity.this, AnaActivity.class);
                startActivity(ıntent);
            }
        });
        güncelle=findViewById(R.id.güncelle);
        güncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                güncelleme();
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galeryac();
            }
        });

//***********************************************************************************************************
    }
    private void galeryac(){
        Intent ıntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(ıntent,SElECT_IMAGE);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SElECT_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri filePath = data.getData();

            String imageName = RandomName.getSaltString()+".jpg";
            StorageReference ref = storageReference.child("kullaniciresimleri").child(imageName);
            ref.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Profil Resmi Eklendi", Toast.LENGTH_LONG).show();
                        String isim = kullaniciismi.getText().toString();
                        String egitim = input_egitim.getText().toString();
                        String dogum = input_dogumtarih.getText().toString();
                        String hakkımda = input_hakkında.getText().toString();

                        reference = database.getReference().child("Kullanicilar").child(auth.getUid());
                        Map map = new HashMap();

                        map.put("isim", isim);
                        map.put("egitim", egitim);
                        map.put("hakkımda", hakkımda);
                        map.put("dogumtarih", dogum);
                        //imageurl=task.getResult().getStorage().getDownloadUrl().toString();
                        String ssdd = String.valueOf(ref);
                        map.put("resim",imageName);




                        reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent ıntent = new Intent(KullaniciProfileActivity.this, KullaniciProfileActivity.class);
                                    startActivity(ıntent);
                                    Toast.makeText(getApplicationContext(), "Profil Bilgileriniz Güncellendi.", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Profil Bilgileriniz Güncellenemedi!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "Profil Resmi Eklenemedi", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    //************************************************************************************************

    public void güncelleme(){
        String isim= kullaniciismi.getText().toString();
        String egitim= input_egitim.getText().toString();
        String dogum = input_dogumtarih.getText().toString();
        String hakkımda = input_hakkında.getText().toString();


        reference = database.getReference().child("Kullanicilar").child(auth.getUid());


        Map map=new HashMap();
        map.put("isim",isim);
        map.put("egitim",egitim);
        map.put("hakkımda",hakkımda);
        map.put("dogumtarih",dogum);
        map.put("resim",firebaseImage);

        reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Intent ıntent = new Intent(KullaniciProfileActivity.this, KullaniciProfileActivity.class);
                    startActivity(ıntent);
                    Toast.makeText(getApplicationContext(), "Profil Bilgileriniz Güncellendi.", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Profil Bilgileriniz Güncellenemedi!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //**************************************************************************************************

    public void bilgileriGetir(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Kullanicilar kl = snapshot.getValue(Kullanicilar.class);
                kullaniciismi.setText((kl.getIsim()));
                input_dogumtarih.setText(kl.getDogumtarih());
                input_hakkında.setText(kl.getHakkımda());
                input_egitim.setText(kl.getEgitim());
                firebaseImage = kl.getResim();
                reference1.child("Arkadaslar").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arkadastext2.setText(snapshot.getChildrenCount()+" Arkadaş");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                reference1.child("Begeniler").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        takipcitext2.setText(snapshot.getChildrenCount()+" Takipçi");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                storageReference.child("kullaniciresimleri/"+kl.getResim()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profile_image);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}