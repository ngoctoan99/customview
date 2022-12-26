package com.sanghm2.customview.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sanghm2.customview.R;
import com.sanghm2.customview.customUI.CustomEditText;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ImageView go_back_res ;
    private CustomEditText edit_res_email , edit_res_pass ,edit_res_name ;
    private Button btn_res ;
    private ProgressDialog dialog ;
    private FirebaseAuth mAuth  ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        mAuth = FirebaseAuth.getInstance() ;
        Objects.requireNonNull(getSupportActionBar()).hide();
        go_back_res = findViewById(R.id.go_back_res);
        go_back_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
        edit_res_email = findViewById(R.id.edit_res_email);
        edit_res_pass = findViewById(R.id.edit_res_pass);
        edit_res_name = findViewById(R.id.edit_res_name);
        btn_res = findViewById(R.id.btn_res) ;
        btn_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = edit_res_pass.getText();
                String email  = edit_res_email.getText();
                String name  = edit_res_name.getText() ;
                edit_res_email.setTextView("");
                edit_res_pass.setTextView("");
                edit_res_name.setTextView("");
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        register(email, pass,name);
                    }
                }, 2000);
                dialog = ProgressDialog.show(RegisterActivity.this ,"" , getResources().getString(R.string.content_load), true ) ;

            }
        });
    }

    private void register(String email, String pass,String name) {
       if(!email.isEmpty() && !pass.isEmpty() && !name.isEmpty()){
           mAuth.createUserWithEmailAndPassword(email, pass)
                   .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {
                               FirebaseUser user  = mAuth.getCurrentUser() ;
                               assert user != null;
                               String email =  user.getEmail() ;
                               String uId  = user.getUid() ;
                               HashMap<String , String> hashMap = new HashMap<>( );
                               hashMap.put("email" , email);
                               hashMap.put("uid" ,uId);
                               hashMap.put("name" ,name);
                               FirebaseDatabase database = FirebaseDatabase.getInstance() ;
                               DatabaseReference reference = database.getReference("Users") ;
                               reference.child(uId).setValue(hashMap) ;
                               Toast.makeText(RegisterActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(RegisterActivity.this , LoginActivity.class));
                               finish();
                           } else {
                               dialog.dismiss();
                               Toast.makeText(RegisterActivity.this, "Đăng kí thất bại.",
                                       Toast.LENGTH_SHORT).show();
                           }
                       }
                   }).addOnFailureListener(this, new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           dialog.dismiss();
                           Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   });
       }else {
           Toast.makeText(this,  "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
       }

    }

}