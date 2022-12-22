package com.sanghm2.customview.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.sanghm2.customview.R;
import com.sanghm2.customview.customUI.CustomEditText;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

   private CustomEditText edit_phone ;
   private CustomEditText edit_pass ;
   private ProgressDialog dialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

    }
    public void btnPressed(View view){
        String pass = edit_pass.getText() ;
        String phone = edit_phone.getText() ;
        edit_pass.setTextView("");
        edit_phone.setTextView("");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                login(phone, pass);
            }
        }, 3000);
        dialog = ProgressDialog.show(LoginActivity.this ,"" , getResources().getString(R.string.content_load), true ) ;
    }
    public  void initView(){
        Objects.requireNonNull(getSupportActionBar()).hide();
        edit_phone  = findViewById(R.id.edit_phone) ;
        edit_pass  = findViewById(R.id.edit_pass) ;
    }
    public void login(String phone , String pass ){
        if(phone.equals("toan") && pass.equals("123")){ /// login success
            dialog.dismiss();
            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this , MainActivity.class));
        }else{   // login false
            dialog.dismiss();
            Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}