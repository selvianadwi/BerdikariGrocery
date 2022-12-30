package com.example.aplikasikelontong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

public class ProdukAdapter extends FirestoreRecyclerAdapter<Produk, ProdukAdapter.ProdukHolder> {
    private Context context;
    private OnItemClickListener listener;

    public ProdukAdapter(@NonNull FirestoreRecyclerOptions<Produk> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProdukHolder holder, int position, @NonNull Produk model) {
        holder.txtNama.setText(model.getNama());
        holder.txtHarga.setText(model.getHarga());
        Glide.with(holder.imgProduk.getContext())
                .load(model.getGambar())
                .into(holder.imgProduk);
    }

    @NonNull
    @Override
    public ProdukHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        return new ProdukHolder(v);
    }

    class ProdukHolder extends RecyclerView.ViewHolder {
        TextView txtNama;
        TextView txtHarga;
        ImageView imgProduk;
        ConstraintLayout layout;

        public ProdukHolder(@NonNull View itemView) {
            super(itemView);
            txtNama = itemView.findViewById(R.id.txtNamaProduct);
            txtHarga = itemView.findViewById(R.id.txtHargaProduct);
            imgProduk = itemView.findViewById(R.id.imgProduk);
            layout = itemView.findViewById(R.id.item_layout);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
