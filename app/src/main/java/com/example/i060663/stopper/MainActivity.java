package com.example.i060663.stopper;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView minutesTextView;
    private TextView secondsTextView;
    private TextView millisecTextView;

    private long startTime = 0;
    private long pauseTime = 0;
    private long resumeTime = 0;
    private long idleAmount = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime - idleAmount;
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


        final Button pauseButton = (Button) findViewById(R.id.pauseButton);
        pauseButton.setText("Pause");
        pauseButton.setEnabled(false);

        Button b = (Button) findViewById(R.id.startButton);
        b.setText("start");
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals("stop")) {
                    timerHandler.removeCallbacks(timerRunnable);
                    b.setText("start");
                    pauseButton.setEnabled(false);
                    pauseButton.setText("Pause");
                } else {
                    startTime = System.currentTimeMillis();
                    pauseTime = 0;
                    resumeTime = 0;
                    idleAmount = 0;
                    timerHandler.postDelayed(timerRunnable, 0);
                    b.setText("stop");
                    pauseButton.setEnabled(true);
                    pauseButton.setText("Pause");
                }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals("Pause")) {
                    pauseTime = System.currentTimeMillis();
                    timerHandler.removeCallbacks(timerRunnable);
                    b.setText("Resume");
                } else {
                    resumeTime = System.currentTimeMillis();
                    idleAmount = idleAmount + (resumeTime - pauseTime);
                    timerHandler.postDelayed(timerRunnable, 0);
                    b.setText("Pause");
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
