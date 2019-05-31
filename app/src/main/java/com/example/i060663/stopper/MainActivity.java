package com.example.i060663.stopper;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView minutesTextView;
    TextView secondsTextView;
    TextView millisecTextView;

    long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            minutesTextView.setText(String.format("%02d", minutes));
            secondsTextView.setText(String.format("%02d", seconds));
            millisecTextView.setText(String.format("%03d", millis - seconds * 1000));

            timerHandler.postDelayed(this, 50);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        minutesTextView = (TextView) findViewById(R.id.minuteTextView);
        secondsTextView = (TextView) findViewById(R.id.secondTextView);
        millisecTextView = (TextView) findViewById(R.id.millisecTextView);

        Button b = (Button) findViewById(R.id.startButton);
        b.setText("start");
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals("stop")) {
                    timerHandler.removeCallbacks(timerRunnable);
                    b.setText("start");
                } else {
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                    b.setText("stop");
                }
            }
        });

    }
    
    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        Button b = (Button)findViewById(R.id.startButton);
        b.setText("start");
    }


}
