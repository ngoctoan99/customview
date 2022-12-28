package com.sanghm2.customview.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sanghm2.customview.R;
import com.sanghm2.customview.customUI.CustomEditText;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

   private CustomEditText edit_phone ;
   private CustomEditText edit_pass ;
   private TextView go_res ;

   private ProgressDialog dialog ;
   private FirebaseAuth mAuth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        actionView();

    }

    private void actionView() {

        go_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    public void btnPressed(View view){
       if(!edit_pass.getText().isEmpty() && !edit_phone.getText().isEmpty()){
           String pass = edit_pass.getText() ;
           String email = edit_phone.getText() ;
           edit_pass.setTextView("");
           edit_phone.setTextView("");
           login(email,pass);
       }else {
           Toast.makeText(this, "Vui lòng nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
       }
    }
    public  void initView(){
        mAuth = FirebaseAuth.getInstance() ;
        Objects.requireNonNull(getSupportActionBar()).hide();
        edit_phone  = findViewById(R.id.edit_phone) ;
        edit_pass  = findViewById(R.id.edit_pass) ;
        go_res  = findViewById(R.id.go_res) ;
    }
    public void login(String email , String pass ){
        dialog = ProgressDialog.show(LoginActivity.this ,"" , getResources().getString(R.string.content_load), true ) ;
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this,"Xin chào " + email, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this ,MainActivity.class ));
                }else{
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}