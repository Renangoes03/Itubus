package com.viajet.itubus.activity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.viajet.itubus.R;
import com.viajet.itubus.activity.model.BusLine;

import java.util.List;

public class BusLineAdapter extends RecyclerView.Adapter<BusLineAdapter.BusLineViewHolder> {

    private final List<BusLine> busLines;

    public BusLineAdapter(List<BusLine> busLines) {
        this.busLines = busLines;
    }

    @NonNull
    @Override
    public BusLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_add_bus_line, parent, false);
        return new BusLineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusLineViewHolder holder, int position) {
        BusLine busLine = busLines.get(position);
        holder.title.setText(busLine.getTitle());
        holder.from.setText(busLine.getFrom());
        holder.to.setText(busLine.getTo());
        holder.price.setText(busLine.getPrice());
    }

    @Override
    public int getItemCount() {
        return busLines.size();
    }

    static class BusLineViewHolder extends RecyclerView.ViewHolder {
        TextView title, from, to, price;

        public BusLineViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitle);
            from = itemView.findViewById(R.id.textViewFrom);
            to = itemView.findViewById(R.id.textViewTo);
            price = itemView.findViewById(R.id.textViewPrice);
        }
    }
}
