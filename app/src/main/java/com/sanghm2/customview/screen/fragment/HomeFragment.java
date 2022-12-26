package com.sanghm2.customview.screen.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sanghm2.customview.R;
import com.sanghm2.customview.model.User;

public class HomeFragment extends Fragment {

    private FirebaseAuth firebaseAuth ;
    private TextView txt_home ;
    private View view ;
    public HomeFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseAuth = FirebaseAuth.getInstance() ;
        loadData() ;
        initView();
        return  view ;
    }

    private void initView() {
        txt_home = view.findViewById(R.id.txt_home);
    }

    private void loadData() {
        FirebaseDatabase database =  FirebaseDatabase.getInstance() ;
        FirebaseUser user = firebaseAuth.getCurrentUser() ;
        DatabaseReference databaseReference = database.getReference("Users");
        assert user != null;
        databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user1 =  snapshot.getValue(User.class) ;
                assert user1 != null;
                txt_home.setText(user1.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}