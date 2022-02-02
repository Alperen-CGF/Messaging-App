package Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.corumgaz.AnaActivity;
import com.example.corumgaz.KullaniciProfileActivity;
import com.example.corumgaz.MainActivity;
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

import Models.MessageModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

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
    List<MessageModel> messageModelList;
    Boolean state;
    int sent=1,recieved=2;
    public MessageAdapter(List<String> userKeysList, Activity activity, Context context,List<MessageModel> messageModelList) {
        this.userKeysList = userKeysList;
        this.activity = activity;
        this.context = context;
        this.messageModelList = messageModelList;
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userid = firebaseUser.getUid();
        state = false;


    }

    //layout tanımla
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==sent){

            view = LayoutInflater.from(context).inflate(R.layout.message_sent_layout, parent, false);
            return new ViewHolder(view);

        }else
        {
            view = LayoutInflater.from(context).inflate(R.layout.message_recieved_layout, parent, false);
            return new ViewHolder(view);
        }

    }

    //view setlemesi
    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.messageText.setText(messageModelList.get(position).getText());
        MessageModel messageModel = messageModelList.get(position);

    }

    //adapteri oluştur
    @Override
    public int getItemCount() {
        return messageModelList.size();
    }



    //view tanımla
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;




        ViewHolder(View itemView) {
            super(itemView);


            if(state==true){
                messageText = (TextView) itemView.findViewById(R.id.messagesent);
            }
            else{
                messageText = (TextView) itemView.findViewById(R.id.messagerecieved);
            }


        }

    }

    @Override
    public int getItemViewType(int position) {
        if (messageModelList.get(position).getFrom().equals(userid)){
            state = true;
            return sent;
        }
        else {
            state = false;
            return recieved;
        }
    }
}