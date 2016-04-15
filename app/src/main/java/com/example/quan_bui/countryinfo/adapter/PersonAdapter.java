package com.example.quan_bui.countryinfo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.quan_bui.countryinfo.Person;
import com.example.quan_bui.countryinfo.R;
import java.util.List;

/**
 * Created by Quan Bui on 4/14/16.
 */
public class PersonAdapter
    extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    private List<Person> personList;

    public PersonAdapter(List<Person> personList) {
        this.personList = personList;
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
        Person person = personList.get(position);
        holder.tvPersonName.setText(person.getName());
        holder.tvCountryCode.setText(person.getCountryCode());
        holder.tvCountry.setText(person.getCountryName());
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public void removeAt(int position) {
        personList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, personList.size());
    }

    public void add(Person person) {
        personList.add(person);
        notifyDataSetChanged();
    }

    class ViewHolder
        extends RecyclerView.ViewHolder {
        public TextView tvPersonName;
        public TextView tvCountryCode;
        public TextView tvCountry;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPersonName = (TextView) itemView.findViewById(R.id.tvPersonName);
            tvCountryCode = (TextView) itemView.findViewById(R.id.tvCountryCode);
            tvCountry = (TextView) itemView.findViewById(R.id.tvCountry);
        }
    }
}

