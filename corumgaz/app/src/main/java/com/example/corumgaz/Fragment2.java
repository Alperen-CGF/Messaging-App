package com.example.corumgaz;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Adapter.Friend_Req_Adapter;
import Adapter.UserAdapter;

public class Fragment2 extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    View view;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String userid;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    List<String> friend_req_key_list;
    RecyclerView friend_req_recy;
    Friend_Req_Adapter adapter;
    TextView fragment2text;
    public Fragment2() {
        // Required empty public constructor
    }


    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment2_layout, container, false);
        tanimla();
        istekler();
        return view;
    }

    public void tanimla(){
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userid = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("Arkadaslik_Istek");
        friend_req_key_list = new ArrayList<>();
        friend_req_recy = (RecyclerView) view.findViewById(R.id.friend_req_recy);
        RecyclerView.LayoutManager layoutManager =new GridLayoutManager(getContext(),1);
        friend_req_recy.setLayoutManager(layoutManager);
        adapter = new Friend_Req_Adapter(friend_req_key_list,getActivity(),getContext());
        friend_req_recy.setAdapter(adapter);
        fragment2text=(TextView) view.findViewById(R.id.fragment2text);
    }

    public void istekler(){
        reference.child(userid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String kontrol=snapshot.child("tip").getValue().toString();
                if (kontrol.equals("aldi")){
                    fragment2text.setVisibility(View.GONE);
                    friend_req_key_list.add(snapshot.getKey());
                    adapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                friend_req_key_list.remove(snapshot.getKey());
                adapter.notifyDataSetChanged();
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