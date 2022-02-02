package com.example.corumgaz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.MessageAdapter;
import Models.GetDate;
import Models.MessageModel;

public class ChatActivity extends AppCompatActivity {
    TextView chatusername;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FloatingActionButton sendmessagebutton;
    EditText sendmessagetext;
    List<MessageModel> messageModelList;
    RecyclerView chat_recy;
    MessageAdapter messageAdapter;
    List<String> keylist;
    ImageButton kisisec2;
    View view = this.getCurrentFocus();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        tanimla();
        action();
        loadMessage();
    }

    public String getUserName(){
        Bundle bundle = getIntent().getExtras();
        String userName = bundle.getString("userName");
        return userName;
    }
    public String getId(){
        String id =getIntent().getExtras().getString("id").toString();
        return id;

    }
    public void tanimla(){
        chatusername = (TextView) findViewById(R.id.chatusername);
        chatusername.setText(getUserName());
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        sendmessagebutton = (FloatingActionButton) findViewById(R.id.sendmessagebutton);
        sendmessagetext = (EditText) findViewById(R.id.sendmessagetext);
        messageModelList = new ArrayList<>();
        chat_recy = (RecyclerView) findViewById(R.id.chat_recy);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ChatActivity.this,1);
        chat_recy.setLayoutManager(layoutManager);
        keylist = new ArrayList<>();
        messageAdapter = new MessageAdapter(keylist,ChatActivity.this,ChatActivity.this,messageModelList);
        chat_recy.setAdapter(messageAdapter);
        kisisec2=(ImageButton) findViewById(R.id.kisisec2);
    }
    public void action(){

        kisisec2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ChatActivity.this,AnaActivity.class);
                startActivity(intent);
            }
        });
        sendmessagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message= sendmessagetext.getText().toString();
                if(!message.equals("")){
                sendmessagetext.setText("");
                sendMessage(firebaseUser.getUid(),getId(),"text", GetDate.getDate(),false,message);
                    chat_recy.scrollToPosition(messageModelList.size() - 1);}
            }
        });


    }
    public void sendMessage(final String userid,final String otherid,String textType,String date,Boolean seen,String messageText){
        String mesajid = reference.child("Mesajlar").child(userid).child(otherid).push().getKey();
        Map messagemap = new HashMap();
        messagemap.put("type",textType);
        messagemap.put("seen",seen);
        messagemap.put("time",date);
        messagemap.put("text",messageText);
        messagemap.put("from",userid);

        reference.child("Mesajlar").child(userid).child(otherid).child(mesajid).setValue(messagemap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                reference.child("Mesajlar").child(otherid).child(userid).child(mesajid).setValue(messagemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });
    }
    public void loadMessage(){
        reference.child("Mesajlar").child(firebaseUser.getUid()).child(getId()).addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MessageModel messageModel = snapshot.getValue(MessageModel.class);
                messageModelList.add(messageModel);
                messageAdapter.notifyDataSetChanged();
                chat_recy.scrollToPosition(messageModelList.size() - 1);
                keylist.add(snapshot.getKey());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}