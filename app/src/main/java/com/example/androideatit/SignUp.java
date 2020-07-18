package com.example.androideatit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.androideatit.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import info.hoang8f.widget.FButton;

public class SignUp extends AppCompatActivity {

    MaterialEditText editPhone,editPassword,editName;
    Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        editName = (MaterialEditText)findViewById(R.id.editName);
        editPassword = (MaterialEditText)findViewById(R.id.editPassword);
        editPhone = (MaterialEditText)findViewById(R.id.editPhone);

        btnSignUp = (FButton)findViewById(R.id.btnSignUp);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

          btnSignUp.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                  mDialog.setMessage("Please waiting...");
                  mDialog.show();



                  table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                     if(dataSnapshot.child(editPhone.getText().toString()).exists()){
                         mDialog.dismiss();
                         Toast.makeText(SignUp.this,"No user typed", Toast.LENGTH_SHORT).show();

                     }else{
                         mDialog.dismiss();
                         User user = new User(editName.getText().toString(),editPassword.getText().toString());
                         table_user.child(editPhone.getText().toString()).setValue(user);
                         Toast.makeText(SignUp.this,"Sign up Successfully.", Toast.LENGTH_SHORT).show();
                         finish();
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
