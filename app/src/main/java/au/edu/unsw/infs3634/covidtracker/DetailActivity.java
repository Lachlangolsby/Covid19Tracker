package au.edu.unsw.infs3634.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    public static final String INTENT_MESSAGE = "au.edu.unsw.infs3634.covidtracker.intent_message";

    // Create variables for each textView
    private TextView mcountry;
    private TextView mNewCases;
    private TextView mTotalCases;
    private TextView mNewDeaths;
    private TextView mTotalDeaths;
    private TextView mnewRecovered;
    private TextView mTotalRecovered;
    private ImageView msearch;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String value = getIntent().getStringExtra("TestData");
        setContentView(R.layout.activity_detail);

        mcountry = findViewById(R.id.tvCountry);
        mNewCases = findViewById(R.id.tvNewCases);
        mTotalCases = findViewById(R.id.tvTotalCases);
         mNewDeaths = findViewById(R.id.tvNewDeaths);
         mTotalDeaths = findViewById(R.id.tvTotalDeaths);
         mnewRecovered = findViewById(R.id.tvNewRecovered);
         mTotalRecovered = findViewById(R.id.tvTotalRecovered);
         msearch = findViewById(R.id.Search);


        Intent intent = getIntent();
        String countryCode = intent.getStringExtra(INTENT_MESSAGE);




        ArrayList<Country> countries = Country.getCountries();
        for (final Country country: countries){
            if (country.getCountryCode().equals(countryCode)){
    // update all text views with all info
                setTitle(country.getCountryCode());
                mcountry.setText(country.getCountry());
                mNewCases.setText(String.valueOf(country.getNewConfirmed()));
                mTotalCases.setText(String.valueOf(country.getTotalConfirmed()));
                mNewDeaths.setText(String.valueOf(country.getNewDeaths()));
                mTotalDeaths.setText(String.valueOf(country.getTotalDeaths()));
                mnewRecovered.setText(String.valueOf(country.getNewRecovered()));
                mTotalRecovered.setText(String.valueOf(country.getTotalRecovered()));
                // Make the search Icon lnk
                ImageView videoButton = findViewById(R.id.Search);
                msearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        searchCountry(country.getCountry());
                    }
                });



            }
        }
    }

            private void searchCountry (String country) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com/search?q=covid "+ country));
                startActivity(intent);
            }

        }






