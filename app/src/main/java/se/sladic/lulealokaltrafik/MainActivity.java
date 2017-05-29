package se.sladic.lulealokaltrafik;

import android.content.Intent;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchButton = (Button) findViewById(R.id.button);
        final EditText fromEdit   = (EditText) findViewById(R.id.editText);
        final EditText toEdit     = (EditText) findViewById(R.id.editText2);

        Calendar c  = Calendar.getInstance();
        final String date = buildDate(c);
        final ArrayList<String> times = buildTime(c);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormFiller formFiller = new FormFiller();
                ArrayList<Result> res = formFiller.search(fromEdit.getText().toString(),
                        toEdit.getText().toString(),
                        times.get(0),
                        date, times.get(1),
                        date);
                //put extra here
                Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
                startActivity(intent);
            }
        });
    }

    private String buildDate(Calendar c){
        int day     = c.get(Calendar.DATE);
        int month   = c.get(Calendar.MONTH);
        int year    = c.get(Calendar.YEAR);
        String date = year + "-" + month + "-" + day;
        return date;
    }

    private ArrayList<String> buildTime(Calendar c){
        int hour    = c.get(Calendar.HOUR_OF_DAY);
        int minute  = c.get(Calendar.MINUTE);
        String time = hour + ":" + minute;
        String time2 = hour+2 + ":" + minute;
        ArrayList<String> times = new ArrayList<>();
        times.add(0, time);
        times.add(1, time2);
        return times;
    }
}
