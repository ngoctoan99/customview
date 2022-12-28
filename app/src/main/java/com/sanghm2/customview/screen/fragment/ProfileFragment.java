package com.sanghm2.customview.screen.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sanghm2.customview.R;
import com.sanghm2.customview.customUI.CustomEditText;
import com.sanghm2.customview.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth ;
    private TextView txt_name , txt_age , txt_address ;
    private View view ;
    private CardView icon_edit ;
    private RelativeLayout layout_loading ,layout_data;
    private FirebaseDatabase database ;
    private FirebaseUser user ;
    private ProgressDialog pd;
    private DatabaseReference databaseReference;
    private ImageView avatar ;


    private static  final int CAMERA_REQUEST_CODE = 100;
    private static  final int STORAGE_REQUEST_CODE = 200;
    private static  final int IMAGE_PICK_CAMERA_REQUEST_CODE = 300;
    private static  final int IMAGE_PICK_GALLERY_REQUEST_CODE = 400;
    String CameraPermission[];
    String storagePermission[];
    Uri image_uri;
    String profileOrCoverPhoto;
    StorageReference storageReference;
    String storagePath = "Users_Profile_Cover_Imgs/";

    public ProfileFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        firebaseAuth = FirebaseAuth.getInstance() ;
        initView();
        loadData() ;
        return  view ;
    }

    private void initView() {

        txt_name = view.findViewById(R.id.txt_name);
        txt_age = view.findViewById(R.id.txt_age);
        txt_address = view.findViewById(R.id.txt_address);

        layout_data = view.findViewById(R.id.layout_data);
        layout_loading = view.findViewById(R.id.layout_loading);

        icon_edit = view.findViewById(R.id.icon_edit) ;
        database =  FirebaseDatabase.getInstance() ;
        user = firebaseAuth.getCurrentUser() ;
        pd = new ProgressDialog(getActivity());
        avatar = view.findViewById(R.id.avatar) ;
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Update Avatar");
                showImagePicDialog();
            }
        });
        databaseReference = database.getReference("Profile");

        storageReference = FirebaseStorage.getInstance().getReference();
        CameraPermission = new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        icon_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Update Profile");
                showEditDialog() ;
            }
        });
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update profile");
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(20,20,20,20);
        EditText editText1 = new EditText(getActivity());
        editText1.setHint("Nhập tên người dùng");
        EditText editText2 = new EditText(getActivity());
        editText2.setHint("Nhập tuổi người dùng");
        EditText editText3= new EditText(getActivity());
        editText3.setHint("Nhập địa chỉ");
        linearLayout.addView(editText1);
        linearLayout.addView(editText2);
        linearLayout.addView(editText3);
        builder.setView(linearLayout);
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String  name = editText1.getText().toString().trim();
                String  age = editText2.getText().toString().trim();
                String  address = editText3.getText().toString().trim();
                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(age) && TextUtils.isEmpty(address)){
                    pd.show();
                    HashMap hashMap = new HashMap() ;
                    hashMap.put("name",name) ;
                    hashMap.put("age",Long.parseLong(age)) ;
                    hashMap.put("address",address) ;
                    databaseReference.child(Objects.requireNonNull(firebaseAuth.getUid())).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            pd.dismiss();
                            Toast.makeText(getActivity(),"Updated " ,Toast.LENGTH_SHORT).show();
                        }
                    }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getContext(), e.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    pd.dismiss();
            }
        });
        builder.create().show();
    }

    private void loadData() {
        databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                layout_loading.setVisibility(View.GONE);
                layout_data.setVisibility(View.VISIBLE);
                User user1 =  snapshot.getValue(User.class) ;
                if(user1 != null ){
                    txt_name.setText(user1.getName());
                    txt_age.setText(user1.getAge() + "");
                    txt_address.setText(user1.getAddress());
                }else {
                    txt_name.setText(getResources().getString(R.string.null_sub));
                    txt_age.setText(getResources().getString(R.string.null_sub));
                    txt_address.setText(getResources().getString(R.string.null_sub));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestStoragePermission() {
        requestPermissions(storagePermission,STORAGE_REQUEST_CODE);
    }
    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    private void requestCameraPermission() {
        requestPermissions(CameraPermission,CAMERA_REQUEST_CODE);
    }
    private void showImagePicDialog() {
        String options[] = {"Camera","Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0) {
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }else {
                        pickfromCamera();
                    }
                }
                else if(which == 1) {
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else{
                        pickfromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length > 0) {
                    boolean CameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(CameraAccepted && writeStorageAccepted) {
                        pickfromCamera();
                    }
                    else {
                        Toast.makeText(getActivity(),"Please enable camera & storage permission",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted) {
                        pickfromGallery();
                    }
                    else {
                        Toast.makeText(getActivity(),"Please enable storage permission",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_REQUEST_CODE){
                image_uri = data.getData();
                uploadProFileCoverPhoto(image_uri);
            }
            if(requestCode == IMAGE_PICK_CAMERA_REQUEST_CODE) {
                uploadProFileCoverPhoto(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProFileCoverPhoto(Uri image_uri) {
        pd.show();
        profileOrCoverPhoto = "image";
        String filePathAndName = storagePath+ "" + profileOrCoverPhoto+"_"+firebaseAuth.getUid();
        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                Uri downloadUri = uriTask.getResult();
                if(uriTask.isSuccessful()) {
                    HashMap<String ,Object> results = new HashMap<>();
                    results.put(profileOrCoverPhoto,downloadUri.toString());
                    databaseReference.child(Objects.requireNonNull(firebaseAuth.getUid())).updateChildren(results).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();
                            Toast.makeText(getActivity(),"Image Updating...",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getActivity(),"Error Updating...",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    pd.dismiss();
                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getActivity(),""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pickfromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_REQUEST_CODE);
    }

    private void pickfromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_REQUEST_CODE);
    }
}