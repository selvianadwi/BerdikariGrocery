package com.example.aplikasikelontong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DashboardActivity extends AppCompatActivity {
    RecyclerView recMain;
    String namaUser;
    Button fabAdd;
    static String KEY_USER = "username";
    SharedPreferences sharedPref;

    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseFirestore fireDb;
    ProdukAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initFab();

        recMain = findViewById(R.id.rec_main);
        recMain.setLayoutManager(new LinearLayoutManager(this));


        fireDb = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        namaUser = user.getEmail();

        Toast.makeText(this, "Anda login sebagai: "+namaUser, Toast.LENGTH_SHORT).show();

        //menampilkan data dari firebase ke recyclerview
        Query query = fireDb.collection("produk");
        //RecyclerOptions
        FirestoreRecyclerOptions<Produk> options = new FirestoreRecyclerOptions.Builder<Produk>()
                .setQuery(query, Produk.class)
                .build();

        adapter = new ProdukAdapter(options);
        recMain.setAdapter(adapter);
        adapter.startListening();
    }


    //menapilkan data dari firebase
    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initFab() {
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), PostProductActivity.class));
            }
        });
    }

    public void logout(View view) {
        auth.signOut();
        startActivity(new Intent(getBaseContext(), MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }


    //checkout activity
    public void checkout(View view) {
        startActivity(new Intent(getBaseContext(), CheckoutActivity.class));
    }

}