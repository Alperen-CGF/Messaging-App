package Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.corumgaz.AnaActivity;
import com.example.corumgaz.ArkadaslarFragment;
import com.example.corumgaz.ChatActivity;
import com.example.corumgaz.Fragment2;
import com.example.corumgaz.Fragment3;
import com.example.corumgaz.KullaniciProfileActivity;
import com.example.corumgaz.MainActivity;
import com.example.corumgaz.R;
import com.example.corumgaz.ui.main.SectionsPagerAdapter;
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
import com.google.firebase.firestore.auth.User;
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

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

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

    public FriendAdapter(List<String> userKeysList, Activity activity, Context context) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.friend_layout, parent, false);

        return new ViewHolder(view);
    }

    //view setlemesi
    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        //holder.usernameTextView.setText(userKeysList.get(position).toString());
        reference.child("Kullanicilar").child(userKeysList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String userName = snapshot.child("isim").getValue().toString();
                String userImage = snapshot.child("resim").getValue().toString();

                Kullanicilar kl = snapshot.getValue(Kullanicilar.class);

                storageReference.child("kullaniciresimleri/" + userImage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (!userName.equals("null")) {
                            Picasso.get().load(uri).into(holder.profile_image4);
                            holder.kullaniciismi4.setText(userName);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

                holder.arkadaslarlinear.setOnClickListener(new View.OnClickListener() {
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
        TextView kullaniciismi4;
        CircleImageView profile_image4;
        LinearLayout arkadaslarlinear;
        ImageButton kisisec;
        ImageView onlineImage;


        ViewHolder(View itemView) {
            super(itemView);
            kullaniciismi4 = (TextView) itemView.findViewById(R.id.kullaniciismi4);
            profile_image4 = (CircleImageView) itemView.findViewById(R.id.profile_image4);
           // kisisec = (ImageButton) itemView.findViewById(R.id.kisisec);
            onlineImage = (ImageView) itemView.findViewById(R.id.onlineImage);
            arkadaslarlinear=(LinearLayout) itemView.findViewById(R.id.arkadaslarlinear);

        }
    }
}