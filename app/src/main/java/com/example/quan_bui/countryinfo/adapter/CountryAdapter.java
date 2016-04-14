package com.example.quan_bui.countryinfo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.quan_bui.countryinfo.Country;
import com.example.quan_bui.countryinfo.R;
import java.util.List;

/**
 * Created by Quan Bui on 4/14/16.
 */
public class CountryAdapter
    extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    List<Country> countries;

    public CountryAdapter(List<Country> countries) {
        this.countries = countries;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.card_view_country, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Country country = countries.get(position);
        holder.tvName.setText(country.getName());
        holder.tvCapital.setText(country.getCapital());
        holder.tvRegion.setText(country.getRegion());
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    class ViewHolder
        extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvCapital;
        public TextView tvRegion;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvCapital = (TextView) itemView.findViewById(R.id.tvCapital);
            tvRegion = (TextView) itemView.findViewById(R.id.tvRegion);
        }
    }

    public void addData(Country country) {
        countries.add(country);
        notifyItemInserted(0);
    }

    public void removeAt(int position) {
        countries.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, countries.size());
    }
}

