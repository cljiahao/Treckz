package com.example.foodscanner;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BarcodeAdapter extends RecyclerView.Adapter<BarcodeAdapter.ViewHolder> {

    private final static String TAG = "BarcodeAdapter";

    private Context context;
    private ArrayList<BarcodeList> barcodeLists = new ArrayList<>();

    public BarcodeAdapter(Context context) {
        this.context = context;
    }

    public void setBarcodeLists(ArrayList<BarcodeList> barcodeLists) {
        this.barcodeLists = barcodeLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_barcode_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.barCardBrand.setText(barcodeLists.get(position).getBrand());
        holder.barCardName.setText(barcodeLists.get(position).getName());
        holder.barCardCode.setText(barcodeLists.get(position).getBarcode());
    }

    @Override
    public int getItemCount() {
        return barcodeLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView barcodeCard;
        private TextView barCardName, barCardCode, barCardBrand;

        DataBaseHelper databh = new DataBaseHelper(context);

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            barcodeCard = itemView.findViewById(R.id.barcodeCard);
            barCardBrand = itemView.findViewById(R.id.barCardBrand);
            barCardCode = itemView.findViewById(R.id.barCardCode);
            barCardName = itemView.findViewById(R.id.barCardName);

            barcodeCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to remove " + barcodeLists.get(getLayoutPosition()).getName()
                            + " expiring on " + barcodeLists.get(getLayoutPosition()).getName() + " ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            databh.deleteOneBarcode(barcodeLists.get(getLayoutPosition()));
                            notifyItemRemoved(getLayoutPosition());
                            setBarcodeLists(databh.barcodeLists());
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                }
            });

        }
    }

    ;
}
