package com.example.aplikasikelontong;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostProductActivity<intent> extends AppCompatActivity {
    EditText edtNama, edtHarga;
    ImageView imgProduk;
    Button btnPostProduk;
    FirebaseFirestore fireDb;
    FirebaseUser user;
    FirebaseStorage fireStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_product);
        edtNama = findViewById(R.id.edtNama);
        edtHarga = findViewById(R.id.edtHarga);
        imgProduk = findViewById(R.id.imgProduk);
        btnPostProduk = findViewById(R.id.btnPostProduk);

        user = FirebaseAuth.getInstance().getCurrentUser();
        fireDb = FirebaseFirestore.getInstance();
        fireStorage = FirebaseStorage.getInstance();

        imgProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // harusnya ada dialog untuk memilih gambar dari galeri
                pilihGambar();
            }
        });
        btnPostProduk.setOnClickListener(view ->  {
            if(edtNama.getText().length() > 0 && edtHarga.getText().length() > 0){
                uploadData(edtNama.getText().toString(), edtHarga.getText().toString(), user.getUid());
                Toast.makeText(PostProductActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Nama dan Harga harus diisi", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadData(String userId, String namaProduk, String hargaProduk) {
        imgProduk.setDrawingCacheEnabled(true);
        imgProduk.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgProduk.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        //upload gambar ke firebase storage
        FirebaseStorage  storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("images").child("IMG"+new Date().getTime() + ".jpeg");
        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if(taskSnapshot.getMetadata() != null){
                    if(taskSnapshot.getMetadata().getReference()!=null){
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.getResult()!=null) {
                                    saveData(userId, namaProduk, hargaProduk, task.getResult().toString());
                                }else{
                                    Toast.makeText(getApplicationContext(), "Gagal mengambil url gambar", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Gagal mengupload gambar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //simpan data ke firestore
    private void saveData(String userId, String namaProduk, String hargaProduk, String imgProduk){
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("nama", namaProduk);
        data.put("harga", hargaProduk);
        data.put("gambar", imgProduk);

        fireDb.collection("produk").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    Toast.makeText(PostProductActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Data gagal disimpan", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//
    private void pilihGambar() {
        Intent inten = new Intent(Intent.ACTION_PICK);
        inten.setType("image/*");
        startActivityForResult(Intent.createChooser(inten, "Pilih Gambar"), 20);
    }

    //menerima data dari galeri
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            final Uri uri = data.getData();
            Thread thread = new Thread(() -> {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imgProduk.post(() -> {
                        imgProduk.setImageBitmap(bitmap);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }
    }
}