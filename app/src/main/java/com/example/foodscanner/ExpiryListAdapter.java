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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ExpiryListAdapter extends RecyclerView.Adapter<ExpiryListAdapter.Viewholder> {

    private final static String TAG = "ExpiryAdapter";

    private Context context;
    private ArrayList<ExpiryList> expiryList = new ArrayList<>();
    private String productName;

    public ExpiryListAdapter(Context context, String productName) {
        this.context = context;
        this.productName = productName;
    }

    public void setExpiryList(ArrayList<ExpiryList> expiryList) {
        this.expiryList = expiryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_expiry_card_view,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Log.d(TAG,"onBindViewHolder: Called");

        if (productName.equals(expiryList.get(position).getName())) {
            holder.manyExpiryTxt.setText(expiryList.get(position).getDate());
            holder.listQuantity.setText(expiryList.get(position).getQuantity());
        } else {
            holder.expiryCard.setLayoutParams(new RecyclerView.LayoutParams(0, 0));;
        }
    }

    @Override
    public int getItemCount() {
        return expiryList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView manyExpiryTxt, listQuantity;
        private CardView expiryCard;

        DataBaseHelper dbh = new DataBaseHelper(context);

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            expiryCard = itemView.findViewById(R.id.expiryCard);
            manyExpiryTxt = itemView.findViewById(R.id.manyExpiryTxt);
            listQuantity = itemView.findViewById(R.id.listQuantity);

            expiryCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to remove " + expiryList.get(getLayoutPosition()).getName()
                            + " expiring on " + expiryList.get(getLayoutPosition()).getDate() + " ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbh.deleteOneEx(expiryList.get(getLayoutPosition()));
                            notifyItemRemoved(getLayoutPosition());
                            setExpiryList(dbh.expiryLists());
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

}
