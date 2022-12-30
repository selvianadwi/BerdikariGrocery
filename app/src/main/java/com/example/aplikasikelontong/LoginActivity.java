package com.example.aplikasikelontong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUsernameL, edtPassL;
    private String username, password;
    Button btnLogin;
    static String KEY_USER = "username";
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = password = "";

        edtUsernameL = findViewById(R.id.edtUsername);
        edtPassL = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);

        sharedPref = getSharedPreferences("", MODE_PRIVATE);
        //kwtika membuka activity pertama check shared pref ada tau tidak
        String namaUser = sharedPref.getString(LoginActivity.KEY_USER, null);
        if (namaUser != null) {
            //jika ada langsung pindah ke dashboard activity
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(KEY_USER, edtUsernameL.getText().toString());
                editor.apply();
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(intent);

                Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void toRegister(View view) {
        Intent it = new Intent(this, RegisterActivity.class);
        startActivity(it);
        finish();
    }
}