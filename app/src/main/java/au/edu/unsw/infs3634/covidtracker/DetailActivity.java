package au.edu.unsw.infs3634.covidtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {
    public static final String INTENT_MESSAGE = "au.edu.unsw.infs3634.covidtracker.intent_message";

    private TextView mCountry;
    private TextView mNewCases;
    private TextView mTotalCases;
    private TextView mNewDeaths;
    private TextView mTotalDeaths;
    private TextView mNewRecovered;
    private TextView mTotalRecovered;
    private ImageView mSearch;
    private ImageView mFlag;
    private CountryDatabase CountryDB;
    private CheckBox mHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mCountry = findViewById(R.id.tvCountry);
        mNewCases = findViewById(R.id.tvNewCases);
        mTotalCases = findViewById(R.id.tvTotalCases);
        mNewDeaths = findViewById(R.id.tvNewDeaths);
        mTotalDeaths = findViewById(R.id.tvTotalDeaths);
        mNewRecovered = findViewById(R.id.tvNewRecovered);
        mTotalRecovered = findViewById(R.id.tvTotalRecovered);
        mSearch = findViewById(R.id.Search);
        mFlag = findViewById(R.id.ivFlag);
        mHome = findViewById(R.id.cbHome);


        Intent intent = getIntent();
        String countryCode = intent.getStringExtra(INTENT_MESSAGE);


        CountryDB = Room.databaseBuilder(getApplicationContext(), CountryDatabase.class, "country-database").build();

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Country country = CountryDB.countryDao().getCountry(countryCode);
                DecimalFormat df = new DecimalFormat("#,###,###,###");
                setTitle(country.getCountryCode());
                mCountry.setText(country.getCountry());
                mNewCases.setText(df.format(country.getNewConfirmed()));
                mTotalCases.setText(df.format(country.getTotalConfirmed()));
                mTotalDeaths.setText(df.format(country.getTotalDeaths()));
                mNewDeaths.setText(df.format(country.getNewDeaths()));
                mNewRecovered.setText(df.format(country.getNewRecovered()));
                mTotalRecovered.setText(df.format(country.getTotalRecovered()));
//                if (country.getCountryCode() != null) {
//                    Glide.with(mFlag).load("https://www.countryflags.io/" + country.getCountryCode().trim().toLowerCase() + "/shiny/64.png").into(mFlag);
//                }


                mSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchCountry(country.getCountry());
                    }
                });
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference messageRef = database.getReference(FirebaseAuth.getInstance().getUid());
                messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String result = (String) snapshot.getValue();
                        if (result != null && result.equals(country.getCountryCode())) {
                            mHome.setChecked(true);
                        } else {
                            mHome.setChecked(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                mHome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        DatabaseReference messageRef = database.getReference(FirebaseAuth.getInstance().getUid());
                        if(isChecked) {
                            messageRef.setValue(country.getCountryCode());
                        }else{
                            messageRef.setValue("");
                        }
                    }
                });
            }
        });


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.covid19api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CovidService service = retrofit.create(CovidService.class);

        Call<Response> responseCall = service.getResponse();
        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                List<Country> countries = response.body().getCountries();
                for (final Country country : countries) {
                    if (country.getCountryCode().equals(countryCode)) {
                        // update the TextViews with the data of the country object
                        if (country.getCountryCode() != null) {
                            Glide.with(mFlag).load("https://www.countryflags.io/" + country.getCountryCode().trim().toLowerCase() + "/shiny/64.png").into(mFlag);
                        }


                        mSearch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                searchCountry(country.getCountry());
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });

        }

    private void searchCountry(String country){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com/search?q=covid" + country));
        startActivity(intent);

    }
}