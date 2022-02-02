package Adapter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.corumgaz.R;


public class UserFragment extends Fragment {

    TextView denemeText;
    View view;
    String userid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.activity_user, container, false);
        tanimla();
        action();
        return view;
    }
    public void tanimla(){

        userid= getArguments().getString("userid");

    }
    public  void  action(){
        denemeText.setText(userid);
    }
}