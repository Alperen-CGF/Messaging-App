package Adapter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.corumgaz.AnaActivity;
import com.example.corumgaz.ChatActivity;
import com.example.corumgaz.KullaniciProfileActivity;
import com.example.corumgaz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Models.Kullanicilar;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.internal.cache.DiskLruCache;


public class UserActivity extends AppCompatActivity {
    TextView kullaniciismi2, input_egitim2, input_dogumtarih2, input_hakkinda2, arkadastext, takipcitext;
    CircleImageView profile_image2;

    TextView usertext1,usertext2;

    ImageView arkadasimg, takipimg, messageimg;
    String otherid, userid;
    String kontrol = "", begenikontrol = "";
    FirebaseAuth auth;
    FirebaseUser user;

    StorageReference storageReference;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference, reference_Arkadaslik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        tanimla();
        action();
        getBegeniText();
        getArkadasText();
    }

    public void tanimla() {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userid = user.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        reference_Arkadaslik = firebaseDatabase.getReference().child("Arkadaslik_Istek");

        otherid = getIntent().getStringExtra("userid");

        input_egitim2 = (TextView) findViewById(R.id.input_egitim2);
        kullaniciismi2 = (TextView) findViewById(R.id.kullaniciismi2);
        input_dogumtarih2 = (TextView) findViewById(R.id.input_dogumtarih2);
        input_hakkinda2 = (TextView) findViewById(R.id.input_hakkinda2);
        arkadastext = (TextView) findViewById(R.id.arkadastext);
        takipcitext = (TextView) findViewById(R.id.takipcitext);
        profile_image2 = (CircleImageView) findViewById(R.id.profile_image2);
        arkadasimg = (ImageView) findViewById(R.id.arkadasimg);
        takipimg = (ImageView) findViewById(R.id.takipimg);
        messageimg = (ImageView) findViewById(R.id.messageimg);

        usertext1 = (TextView) findViewById(R.id.usertext1);
        usertext2 = (TextView) findViewById(R.id.usertext2);

        storageReference = FirebaseStorage.getInstance().getReference();
        reference_Arkadaslik.child(otherid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userid)) {
                    kontrol = "istek";
                    arkadasimg.setImageResource(R.drawable.ark1);
                    usertext2.setText("İstek Yollandı");

                } else {

                    arkadasimg.setImageResource(R.drawable.ark2);
                    usertext2.setText("Arkadaş Ekle");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("Arkadaslar").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(otherid)) {
                    kontrol = "arkadas";
                    arkadasimg.setImageResource(R.drawable.arkadas);
                    usertext2.setText("Arkadaşlıktan Çıkar");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("Begeniler").child(otherid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userid)) {
                    begenikontrol = "begendi";
                    takipimg.setImageResource(R.drawable.unfollow);
                    usertext1.setText("Takip Ediliyor");

                } else {

                    takipimg.setImageResource(R.drawable.follow);
                    usertext1.setText("Takip Et");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void action() {
        reference.child("Kullanicilar").child(otherid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanicilar kl = snapshot.getValue(Kullanicilar.class);
                kullaniciismi2.setText((kl.getIsim()));
                input_dogumtarih2.setText("Doğum Tarihi :" + kl.getDogumtarih());
                input_hakkinda2.setText("Hakkında :" + kl.getHakkımda());
                input_egitim2.setText("Egitim Seviyesi :" + kl.getEgitim());


                storageReference.child("kullaniciresimleri/" + kl.getResim()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (!kl.getResim().equals("null")) {
                            Picasso.get().load(uri).into(profile_image2);
                        }
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
        messageimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent=new Intent(UserActivity.this, ChatActivity.class);
                ıntent.putExtra("userName",kullaniciismi2.getText().toString());
                ıntent.putExtra("id",otherid);
                startActivity(ıntent);
            }
        });
        arkadasimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kontrol.equals("istek")) {
                    arkadasiptal(otherid, userid);
                } else if (kontrol.equals("arkadas")) {
                    arkadasTablosundanCikar(otherid, userid);
                } else {
                    arkadasistek(otherid, userid);
                }
            }
        });
        takipimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (begenikontrol.equals("begendi")) {
                    begeniiptal(userid, otherid);
                } else {
                    begen(userid, otherid);
                }
            }
        });
    }

    private void begeniiptal(String userid, String otherid) {
        reference.child("Begeniler").child(otherid).child(userid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                takipimg.setImageResource(R.drawable.follow);
                usertext1.setText("Takip Et");
                begenikontrol = "";
                Toast.makeText(getApplicationContext(), "Kullanıcı Takipten Çıkarıldı.", Toast.LENGTH_LONG).show();
                getBegeniText();
            }
        });
    }

    private void arkadasTablosundanCikar(final String otherid, final String userid) {
        reference.child("Arkadaslar").child(otherid).child(userid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                reference.child("Arkadaslar").child(userid).child(otherid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        kontrol = "";
                        arkadasimg.setImageResource(R.drawable.ark2);
                        usertext2.setText("Arkadaş Ekle");
                        Toast.makeText(getApplicationContext(), "Arkadaşlıktan Çıkarıldı.", Toast.LENGTH_LONG).show();
                        getArkadasText();
                    }
                });
            }
        });
    }

    public void arkadasistek(String otherid, String userid) {
        reference_Arkadaslik.child(userid).child(otherid).child("tip").setValue("gonderdi").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    reference_Arkadaslik.child(otherid).child(userid).child("tip").setValue("aldi").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                kontrol = "istek";
                                Toast.makeText(getApplicationContext(), "Arkadaşlık İsteği Başarıyla Gönderildi.", Toast.LENGTH_LONG).show();
                                arkadasimg.setImageResource(R.drawable.ark1);
                                usertext2.setText("İstek Yollandı");

                            } else {
                                Toast.makeText(getApplicationContext(), "Bir Hata Oluştu.Uygulamayı kapatıp tekrar deneyin.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Bir Hata Oluştu.Uygulamayı kapatıp tekrar deneyin.", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void arkadasiptal(String otherid, String userid) {
        reference_Arkadaslik.child(otherid).child(userid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                reference_Arkadaslik.child(userid).child(otherid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        kontrol = "";
                        arkadasimg.setImageResource(R.drawable.ark2);
                        usertext2.setText("Arkadaş Ekle");
                        Toast.makeText(getApplicationContext(), "Arkadaşlık İsteği İptal Edildi.", Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    public void begen(String userid, String otherid) {
        reference.child("Begeniler").child(otherid).child(userid).child("tip").setValue("begendi").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Kullanıcıyı Başarılı Bir Şekilde Takip Ettiniz.", Toast.LENGTH_LONG).show();
                    takipimg.setImageResource(R.drawable.unfollow);
                    usertext1.setText("Takip Ediliyor");
                    begenikontrol = "begendi";
                    getBegeniText();
                }
            }
        });
    }
    public void getBegeniText(){
        //takipcitext.setText("0 Takipçi");
        //final List<String> begeniList=new ArrayList<>();

        reference.child("Begeniler").child(otherid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                takipcitext.setText(snapshot.getChildrenCount()+" Takipçi");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getArkadasText(){
        //final List<String> arkList=new ArrayList<>();
        //takipcitext.setText("0 Arkadaş");
        reference.child("Arkadaslar").child(otherid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arkadastext.setText(snapshot.getChildrenCount()+" Arkadaş");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
