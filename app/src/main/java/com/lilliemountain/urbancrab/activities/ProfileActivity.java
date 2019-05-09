package com.lilliemountain.urbancrab.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lilliemountain.urbancrab.R;
import com.lilliemountain.urbancrab.model.Profile;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ProfileActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    TextInputEditText name,vmodel,noplate,email,phoneno;
    ImageView imageView4;
    String uid;
    FirebaseDatabase database;
    DatabaseReference users,me;
    Uri imageUri,downlaodUrl;
    StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name=findViewById(R.id.name);
        vmodel=findViewById(R.id.vmodel);
        noplate=findViewById(R.id.noplate);
        email=findViewById(R.id.email);
        phoneno=findViewById(R.id.phone);
        imageView4=findViewById(R.id.imageView4);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        database=FirebaseDatabase.getInstance();
        users=database.getReference("users");
        me=users.child(uid);
        me.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Profile profile=dataSnapshot.getValue(Profile.class);
                    if (profile!=null) {
                        name.setText(profile.getName());
                        vmodel.setText(profile.getVmodel());
                        noplate.setText(profile.getNoplate());
                        email.setText(profile.getEmail());
                        String ph=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                        phoneno.setText(ph);

                        File localFile = File.createTempFile("images", "jpg");
                        final StorageReference rref = mStorageRef.child("images/"+uid+".jpg");

                        rref.getFile(localFile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        //TODO LOAD IMAGE INTO IMAGEVIEW
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle failed download
                                // ...
                            }
                        });
//                        Picasso.get().load(profile.getPicture()).into(imageView4);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final ProgressDialog dialog=new ProgressDialog(ProfileActivity.this);
                dialog.setTitle("Uploading your data...");
                dialog.setCancelable(false);
                dialog.show();
                Log.e( "startStep: ", "put File" );
                final StorageReference rref = mStorageRef.child("images/"+uid+".jpg");

                rref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.e( "startStep: ", "put File done" );
                            Profile myProfile=new Profile(name.getText().toString(),vmodel.getText().toString(),noplate.getText().toString(),email.getText().toString());
                            me.setValue(myProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Snackbar.make(v,"Profile Updated",Snackbar.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    startActivity(new Intent(ProfileActivity.this,HomeActivity.class));
                                }
                            });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("onFailure: ",e.getMessage() );
                    }
                });

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.e( "getImageUri: ",path );
        return Uri.parse(path);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView4.setImageBitmap(photo);
            imageUri=getImageUri(getApplicationContext(),photo);
    }

}
