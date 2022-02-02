package com.example.corumgaz;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import Adapter.FriendAdapter;


public class ArkadaslarFragment extends Fragment {
    View view;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    RecyclerView friend_recy;
    FriendAdapter friendAdapter;
    List<String> keyList;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    ImageButton geridonnn;
    private LinearLayout lnrUyari;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_arkadaslar, container, false);
        tanimla();
        getArkadasList();
        return  view;
    }
    public void tanimla(){
        auth=FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        keyList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("Arkadaslar");
        friend_recy = (RecyclerView) view.findViewById(R.id.friend_recy);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        friend_recy.setLayoutManager((layoutManager));
        friendAdapter= new FriendAdapter(keyList,getActivity(),getContext());
        friend_recy.setAdapter(friendAdapter);
        geridonnn= (ImageButton) view.findViewById(R.id.geridonnn);
        lnrUyari = view.findViewById(R.id.lnrUyari);
    }
    public void getArkadasList(){
        geridonnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(),AnaActivity.class));
            }
        });
        reference.child(firebaseUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                friend_recy.setVisibility(View.VISIBLE);
                lnrUyari.setVisibility(View.GONE);
                keyList.add(snapshot.getKey());
                friendAdapter.notifyDataSetChanged();
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
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().startActivity(new Intent(getActivity(),AnaActivity.class));
                    return true;
                }
                return false;
            }
        });
    }

}