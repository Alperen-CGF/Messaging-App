package Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.corumgaz.R;
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
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Models.Kullanicilar;
import de.hdodenhof.circleimageview.CircleImageView;

public class Friend_Req_Adapter extends RecyclerView.Adapter<Friend_Req_Adapter.ViewHolder> {

    List<String> userKeysList;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    StorageReference storageReference;
    FragmentManager fragmentManager;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String userid;

    public Friend_Req_Adapter(List<String> userKeysList, Activity activity, Context context) {
        this.userKeysList = userKeysList;
        this.activity = activity;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userid = firebaseUser.getUid();

    }

    //layout tanımla
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_req_key_list, parent, false);

        return new ViewHolder(view);
    }

    //view setlemesi
    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        //holder.usernameTextView.setText(userKeysList.get(position).toString());
        reference.child("Kullanicilar").child(userKeysList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Kullanicilar kl = snapshot.getValue(Kullanicilar.class);

                storageReference.child("kullaniciresimleri/" + kl.getResim()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (!kl.getIsim().equals("null")) {
                            Picasso.get().load(uri).into(holder.profile_image3);
                            holder.kullaniciismi3.setText(kl.getIsim());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
                holder.arkekle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        kabulet(userid, userKeysList.get(position));
                    }
                });
                holder.arkekleme.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reddet(userid, userKeysList.get(position));
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //adapteri oluştur
    @Override
    public int getItemCount() {
        return userKeysList.size();
    }

    public void kabulet(String userid, String otherid) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        final String reportDate = df.format(today);
        reference.child("Arkadaslar").child(userid).child(otherid).child("tarih").setValue(reportDate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                reference.child("Arkadaslar").child(otherid).child(userid).child("tarih").setValue(reportDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Arkadaşlık İsteğini Kabul Ettiniz", Toast.LENGTH_LONG).show();
                        reference.child("Arkadaslik_Istek").child(userid).child(otherid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                reference.child("Arkadaslik_Istek").child(otherid).child(userid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    public void reddet(String userid, String otherid) {
        reference.child("Arkadaslik_Istek").child(userid).child(otherid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                reference.child("Arkadaslik_Istek").child(otherid).child(userid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Arkadaşlık İsteğini Reddettiniz", Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    //view tanımla
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView kullaniciismi3;
        CircleImageView profile_image3;
        Button arkekle, arkekleme;


        ViewHolder(View itemView) {
            super(itemView);
            kullaniciismi3 = (TextView) itemView.findViewById(R.id.kullaniciismi3);
            profile_image3 = (CircleImageView) itemView.findViewById(R.id.profile_image3);
            arkekle = (Button) itemView.findViewById(R.id.arkekle);
            arkekleme = (Button) itemView.findViewById(R.id.arkekleme);
        }
    }
}