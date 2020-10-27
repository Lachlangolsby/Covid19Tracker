package au.edu.unsw.infs3634.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private TextView mCountry;
    private TextView mNewCases;
    private TextView mTotalCases;
    private TextView mTotalDeaths;
    private TextView mNewRecovered;
    private ImageView mSearch;
    private TextView mNewDeaths;
    private TextView mTotalRecovered;

    public static final String INTENT_MESSAGE = "au.unsw.infs3634.covidtracker.intent";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mCountry = findViewById(R.id.tvCountry);
        mNewCases = findViewById(R.id.tvNewCases);
        mNewDeaths = findViewById(R.id.tvNewDeaths);
        mNewRecovered = findViewById(R.id.tvNewRecovered);
        mTotalCases = findViewById(R.id.tvTotalCases);
        mTotalDeaths = findViewById(R.id.tvTotalDeaths);
        mTotalRecovered = findViewById(R.id.tvTotalRecovered);
        mSearch = findViewById(R.id.Search);


        Intent intent = getIntent();
        final String countryCode = intent.getStringExtra(INTENT_MESSAGE);

        Gson gson = new Gson();
        Response response = gson.fromJson(Response.json, Response.class);
        List<Country> countries = response.getCountries();
        for(final Country country: countries){
            if(country.getCountryCode().equals(countryCode)){
                setTitle(country.getCountryCode());
                mCountry.setText(country.getCountry());
                mNewCases.setText(String.valueOf(country.getNewConfirmed()));
                mTotalCases.setText(String.valueOf(country.getTotalConfirmed()));
                mNewDeaths.setText(String.valueOf(country.getNewDeaths()));
                mTotalDeaths.setText(String.valueOf(country.getTotalDeaths()));
                mNewRecovered.setText(String.valueOf(country.getNewRecovered()));
                mTotalRecovered.setText(String.valueOf(country.getTotalRecovered()));

                mSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchCountry(country.getCountry());
                    }
                });
            }
        }
    }
    private void searchCountry (String country){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com/search?q=covid " + country));
        startActivity(intent);
    }
}
