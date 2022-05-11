package com.example.foodscanner;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

    private static final String TAG = "SettingsAdapter";

    private Context context;
    private ArrayList<SettingList> settingLists;

    public SettingsAdapter(Context context) {
        this.context = context;
    }

    public void setSettingLists(ArrayList<SettingList> settingLists) {
        this.settingLists = settingLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_settings_card_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: Called");

        holder.setNameRec.setText(settingLists.get(position).getSettingName());
        holder.seticonRec.setImageResource(settingLists.get(position).getDrawableId());

    }

    @Override
    public int getItemCount() {
        return settingLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView seticonRec;
        private TextView setNameRec;
        private CardView settingCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            seticonRec = itemView.findViewById(R.id.seticonRec);
            setNameRec = itemView.findViewById(R.id.setNameRec);
            settingCard = itemView.findViewById(R.id.settingCard);

            settingCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (settingLists.get(getLayoutPosition()).getSettingName().equals("Scan Barcode")) {
                        Intent intent = new Intent(context, BarcodeActivity.class);
                        intent.putExtra("from","barcodeButton");
                        context.startActivity(intent);
                    }
                    else if (settingLists.get(getLayoutPosition()).getSettingName().equals("Barcode List")) {
                        Intent intent = new Intent(context, BarcodeShowAll.class);
                        context.startActivity(intent);
                    }
                }
            });

        }
    }

}
