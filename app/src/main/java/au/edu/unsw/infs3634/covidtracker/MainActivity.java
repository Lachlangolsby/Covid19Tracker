package au.edu.unsw.infs3634.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //1. Define a button in the layout file and assign it a unique ID
        //From the onCreate method of an activity, find the button
        Button button = findViewById(R.id.btLaunchActivty);



        //2. Create an onClickListener to launch detail activity. Run and test app.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.launchDetailActivity("US");

            }
        });

        }
        //3. Create launchDetailActivity method. Uses explicit intent to start activity
    private void launchDetailActivity (String message) {
        Intent intent = new Intent ( this, DetailActivity.class);
        intent.putExtra(DetailActivity.INTENT_MESSAGE, message);
        startActivity(intent);


    }
}
