package com.example.corumgaz;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.corumgaz.ui.main.SectionsPagerAdapter;
import com.example.corumgaz.databinding.ActivityAnaBinding;
import com.google.firebase.auth.FirebaseAuth;

import Adapter.UserActivity;

public class AnaActivity extends AppCompatActivity {

    private ActivityAnaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_ana);

        binding = ActivityAnaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Çorumgaz");
        setSupportActionBar(toolbar);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;
        tabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, new ArkadaslarFragment()).commit();
            }
        });

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.deneme:
                MainActivity.auth.signOut();
                Intent intent = new Intent(AnaActivity.this,MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.deneme3:
                getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, new ArkadaslarFragment()).commit();
                return true;
            case R.id.deneme2:
                Intent ıntent=new Intent(AnaActivity.this,KullaniciProfileActivity.class);
                startActivity(ıntent);

                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }


}

