package com.example.foodscanner;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{

    private final static String TAG = "ListAdapter";

    private Context context;
    private ArrayList<InfoList> infoList = new ArrayList<>();

    public ListAdapter(Context context) {
        this.context = context;
    }

    public void setInfoList(ArrayList<InfoList> infoList) {
        this.infoList = infoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listcardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder: Called");
        holder.brandName.setText(infoList.get(position).getBrand());
        holder.productName.setText(infoList.get(position).getName());
        holder.earliestDate.setText(infoList.get(position).getDate());
        holder.singleQuantity.setText(infoList.get(position).getQuantity());
        holder.TotalQuantity.setText(infoList.get(position).getTotalquantity());

        ExpiryListAdapter exAdapter = new ExpiryListAdapter(context, holder.productName.getText().toString());
        holder.expiryRecView.setAdapter(exAdapter);
        holder.expiryRecView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        exAdapter.setExpiryList(holder.dbh.expiryLists());

        if (infoList.get(position).isExpanded()) {
            TransitionManager.beginDelayedTransition(holder.listCard);
            holder.expandExpiry.setVisibility(View.VISIBLE);
            holder.downArrow.setVisibility(View.GONE);
        } else {
            TransitionManager.beginDelayedTransition(holder.listCard);
            holder.expandExpiry.setVisibility(View.GONE);
            holder.downArrow.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView listCard;
        private TextView brandName, productName, earliestDate, singleQuantity, TotalQuantity;

        private RecyclerView expiryRecView;
        private RelativeLayout expandExpiry;
        private ImageView upArrow, downArrow;

        DataBaseHelper dbh = new DataBaseHelper(context);

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            listCard = itemView.findViewById(R.id.listCard);

            brandName = itemView.findViewById(R.id.brandName);
            productName = itemView.findViewById(R.id.productName);
            earliestDate = itemView.findViewById(R.id.earliestDate);
            singleQuantity = itemView.findViewById(R.id.singleQuantity);
            TotalQuantity = itemView.findViewById(R.id.totalQuantity);

            expiryRecView = itemView.findViewById(R.id.expiryRecView);
            expandExpiry = itemView.findViewById(R.id.expandExpiry);
            upArrow = itemView.findViewById(R.id.upArrow);
            downArrow = itemView.findViewById(R.id.downArrow);

            downArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InfoList food = infoList.get(getAdapterPosition());
                    food.setExpanded(!food.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

            upArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InfoList food = infoList.get(getAdapterPosition());
                    food.setExpanded(!food.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

            earliestDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to remove " + infoList.get(getLayoutPosition()).getName()
                            + " expiring on " + infoList.get(getLayoutPosition()).getDate() + " ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbh.deleteOne(infoList.get(getLayoutPosition()));
                            notifyItemRemoved(getAdapterPosition());
                            setInfoList(dbh.mainfoodlist());
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
