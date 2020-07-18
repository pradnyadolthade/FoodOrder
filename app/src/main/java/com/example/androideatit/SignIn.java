package com.example.androideatit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androideatit.Model.User;
import com.example.androideatit.common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import info.hoang8f.widget.FButton;

public class SignIn extends AppCompatActivity {

    EditText editPhone,editPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editPassword = (MaterialEditText)findViewById(R.id.editPassword);
        editPhone = (MaterialEditText)findViewById(R.id.editPhone);
        btnSignIn = (FButton)findViewById(R.id.btnSignIn);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

       btnSignIn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
               mDialog.setMessage("Please waiting...");
               mDialog.show();


               table_user.addValueEventListener(new ValueEventListener() {
                   @Override

                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if (dataSnapshot.child(editPhone.getText().toString()).exists()) {
                           mDialog.dismiss();
                           User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);
                           user.setPhone(editPhone.getText().toString());
                           if (user.getPassword().equals(editPassword.getText().toString())) {

                               Intent HomeIntent = new Intent(SignIn.this,Home.class);
                               Common.currentUser =user;
                               startActivity(HomeIntent);
                               finish();

                           } else {
                               Toast.makeText(SignIn.this, "Wrong Password...", Toast.LENGTH_SHORT).show();

                           }
                       }else {
                           mDialog.dismiss();

                           Toast.makeText(SignIn.this, "User not exists..", Toast.LENGTH_SHORT).show();
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });
           }
       });


    }
}
