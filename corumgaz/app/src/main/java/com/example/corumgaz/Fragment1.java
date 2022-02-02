package com.example.corumgaz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.FriendAdapter;
import Adapter.MessageAdapter;
import Adapter.SohbetAdapter;
import Models.Kullanicilar;
import Models.MessageModel;


public class Fragment1 extends Fragment {
    View view;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    RecyclerView userListRecyclerView;
    SohbetAdapter sohbetAdapter;
    List<String> keyList;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fire, container, false);
        tanimla();
        getArkadasList();
        return  view;
    }
    public void tanimla(){
        auth=FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        keyList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("Mesajlar");
        userListRecyclerView = (RecyclerView) view.findViewById(R.id.userListRecyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        userListRecyclerView.setLayoutManager((layoutManager));
        sohbetAdapter= new SohbetAdapter(keyList,getActivity(),getContext());
        userListRecyclerView.setAdapter(sohbetAdapter);

    }

    public void getArkadasList(){
        reference.child(firebaseUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                keyList.add(snapshot.getKey());
                sohbetAdapter.notifyDataSetChanged();
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