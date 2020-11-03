package au.edu.unsw.infs3634.covidtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> implements Filterable {
    public static final int SORT_METHOD_NEW = 1;
    public static final int SORT_METHOD_TOTAL = 2;
    private List<Country> mCountries;
    private List<Country> mCountriesFiltered;
    private RecyclerViewClickListener mListener;




    public CountryAdapter(List<Country> countries, RecyclerViewClickListener listener) {
        mCountries = countries;
        mCountriesFiltered = countries;
        mListener = listener;


    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mCountriesFiltered = mCountries;
                } else {
                    List<Country> filteredList = new ArrayList<>();
                    for (Country country : mCountries) {
                        if (country.getCountry().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(country);
                        }
                    }
                    mCountriesFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mCountriesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mCountriesFiltered = (List<Country>) filterResults.values;
            }
        };
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewtype) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new CountryViewHolder(v, mListener);
    }



    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        Country country = mCountriesFiltered.get(position);
        DecimalFormat df = new DecimalFormat("#,###,###,###");
        holder.country.setText(country.getCountry());
        holder.totalcases.setText(df.format(country.getTotalConfirmed()));
        holder.newCases.setText("+" + df.format(country.getNewConfirmed()));
        holder.itemView.setTag(country.getCountryCode());
       Glide.with(holder.context).load("https://www.countryflags.io/"+country.getCountryCode()+"/shiny/64.png").into(holder.Image);
    }

    @Override
    public int getItemCount() {
       return mCountriesFiltered.size();
    }

    public void sort(final int sortMethod) {
        if (mCountriesFiltered.size() > 0) {
            Collections.sort(mCountriesFiltered, new Comparator<Country>() {
                @Override
                public int compare(Country o1, Country o2) {
                    if (sortMethod == SORT_METHOD_NEW) {
                        return o2.getNewConfirmed().compareTo(o1.getNewConfirmed());
                    } else if (sortMethod == SORT_METHOD_TOTAL) {
                        return o2.getTotalConfirmed().compareTo(o1.getTotalConfirmed());
                    }
                    return o2.getTotalConfirmed().compareTo(o1.getTotalConfirmed());
                }
            });
        }
        notifyDataSetChanged();
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, String countryCode);
    }

    public static class CountryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView country, totalcases, newCases;
        private CountryAdapter.RecyclerViewClickListener listener;
        public ImageView Image;
       public Context context;


        public CountryViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            this.listener = listener;
            itemView.setOnClickListener(this);
            country = itemView.findViewById(R.id.tvCountry);
            totalcases = itemView.findViewById(R.id.tvTotalCases);
            newCases = itemView.findViewById(R.id.tvNewCases);
            Image = itemView.findViewById(R.id.ivFlag);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, (String) v.getTag());
        }
    }

    public void setCountries(List<Country> countries) {
        mCountriesFiltered.clear();
        mCountriesFiltered.addAll(countries);
        notifyDataSetChanged();
    }
}