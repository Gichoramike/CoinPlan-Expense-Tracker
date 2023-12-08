package com.example.coinplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    Button btnreg;
    EditText name,email,password;

    String txtname,txtemail,txtpassword;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);


        btnreg= findViewById(R.id.btnreg);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        auth =FirebaseAuth.getInstance();


        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtname = name.getText().toString();
                txtemail = email.getText().toString();
                txtpassword = password.getText().toString();

                if (TextUtils.isEmpty(txtname) ||TextUtils.isEmpty(txtemail) || TextUtils.isEmpty(txtpassword))
                    {
                        Toast.makeText(Register.this, "Enter credentials", Toast.LENGTH_SHORT).show();

                    }
                else if (txtpassword.length()<6){
                    Toast.makeText(Register.this, "password too short!", Toast.LENGTH_SHORT).show();
                }
                else {
                    registerUser(txtname,txtemail,txtpassword);
                    startActivity(new Intent(Register.this, Login.class));
                }

            }
        });
    }

    private void registerUser(String name,String email,String password){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Register.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(Register.this, "Registering user successful", Toast.LENGTH_SHORT).show();
                    clearInputFields();
                    startActivity(new Intent(Register.this, MainActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clearInputFields() {
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        name.setText("");
        email.setText("");
        password.setText("");
    }
};