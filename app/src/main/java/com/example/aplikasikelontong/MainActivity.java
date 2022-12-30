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

public class MainActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword;
    //public static final String KEY_USER = "DataUser";
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user != null){
            startActivity(new Intent(getBaseContext(), DashboardActivity.class));
            finish();
        }
    }

    public void login(View v){
        String emailUser = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        auth.signInWithEmailAndPassword(emailUser, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    Toast.makeText(getBaseContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(MainActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                                    //Log.w("Auth_error", "error login", task.getException());
                                }
                            }
                        });
    }

    public void toRegister(View v){
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
