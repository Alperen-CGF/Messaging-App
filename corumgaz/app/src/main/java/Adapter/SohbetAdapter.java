package Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.corumgaz.ChatActivity;
import com.example.corumgaz.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.List;

import Models.Kullanicilar;
import Models.MessageModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class SohbetAdapter extends RecyclerView.Adapter<SohbetAdapter.ViewHolder> {

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
    View view;

    public SohbetAdapter(List<String> userKeysList, Activity activity, Context context) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.sohbet, parent, false);

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
                            Picasso.get().load(uri).into(holder.profile_image7);
                            holder.kullaniciismi7.setText(kl.getIsim());;

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
                holder.sohbetlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //ChangeFragment fragment = new ChangeFragment(context);
                        //fragment.changeWithParamater(new UserFragment(),userKeysList.get(position));
                        Intent intent = new Intent(activity, ChatActivity.class);
                        intent.putExtra("id",userKeysList.get(position));
                        intent.putExtra("userName",kl.getIsim());
                        activity.startActivity(intent);



                    }
                });
                holder.profile_image7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //ChangeFragment fragment = new ChangeFragment(context);
                        //fragment.changeWithParamater(new UserFragment(),userKeysList.get(position));
                        Intent intent = new Intent(activity, UserActivity.class);
                        intent.putExtra("userid",userKeysList.get(position));
                        activity.startActivity(intent);



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



    //view tanımla
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView kullaniciismi7,sohbetmesajı7;
        CircleImageView profile_image7;
        LinearLayout sohbetlayout;



        ViewHolder(View itemView) {
            super(itemView);
            kullaniciismi7 = (TextView) itemView.findViewById(R.id.kullaniciismi7);
            profile_image7 = (CircleImageView) itemView.findViewById(R.id.profile_image7);
            sohbetmesajı7 = (TextView) itemView.findViewById(R.id.sohbetmesajı7);
            sohbetlayout = (LinearLayout) itemView.findViewById(R.id.sohbetlayout);
        }
    }
}