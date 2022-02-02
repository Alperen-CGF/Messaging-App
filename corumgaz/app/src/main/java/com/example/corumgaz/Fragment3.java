package com.example.corumgaz;

import static java.security.AccessController.getContext;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Adapter.UserAdapter;
import Models.Kullanicilar;

public class Fragment3 extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    List<String> userKeyList;
    RecyclerView userListRecyclerView;
    View view;
    UserAdapter userAdapter;
    StorageReference storageReference;
    FirebaseAuth auth;
    FirebaseUser user;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        storageReference = FirebaseStorage.getInstance().getReference();
        view = inflater.inflate(R.layout.fragment_fire,container,false);
        tanımla();
        kullaniciGetir();
        return view;
    }

    public void tanımla() {
        userKeyList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        userListRecyclerView = view.findViewById(R.id.userListRecyclerView);
        RecyclerView.LayoutManager mng = new GridLayoutManager(getContext(),2);
        userListRecyclerView.setLayoutManager(mng);
        userAdapter = new UserAdapter(userKeyList,getActivity(),getContext());
        userListRecyclerView.setAdapter(userAdapter);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    public void kullaniciGetir() {
        reference.child("Kullanicilar").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                reference.child("Kullanicilar").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Kullanicilar kl = snapshot.getValue(Kullanicilar.class);

                        storageReference.child("kullaniciresimleri/"+kl.getResim()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                if (!kl.getIsim().equals("null") && !snapshot.getKey().equals(user.getUid())) {
                                    userKeyList.add(snapshot.getKey());
                                    userAdapter.notifyDataSetChanged();
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
