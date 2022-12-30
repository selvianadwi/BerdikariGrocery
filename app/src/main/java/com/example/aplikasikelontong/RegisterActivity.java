package com.example.aplikasikelontong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    EditText edtUser, edtEmail, edtPass, edtConPass;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtUser = findViewById(R.id.edtUser);
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPass);
        edtConPass = findViewById(R.id.edtConPass);

        auth = FirebaseAuth.getInstance();
    }

    public void register(View v){
        String user, email, password, conPass;
        user = edtUser.getText().toString();
        email = edtEmail.getText().toString();
        password = edtPass.getText().toString();
        conPass = edtConPass.getText().toString();

        if(password.equals(conPass)){
            auth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseUser user = auth.getCurrentUser();
                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    }else{
                                        Log.w("Auth_error", "error registrasi", task.getException());
                                    }
                                }
                            });
        } else {
            Toast.makeText(this,"Password dan Konfirmasi tidak sama", Toast.LENGTH_LONG).show();
        }
    }

    public void toLogin(View v){
        startActivity(new Intent(this, MainActivity.class));
    }
}