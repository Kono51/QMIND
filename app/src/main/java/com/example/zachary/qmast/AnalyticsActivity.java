package com.example.zachary.qmast;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class AnalyticsActivity extends AppCompatActivity {

    private final Queue<Double> window = new LinkedList<Double>();
    final static List<String> dArr = new LinkedList<>();


    private int period;
    private double sum;

    public void newNum(double num) {
        sum += num;
        window.add(num);
        if (window.size() > period) {
            sum -= window.remove();
        }
    }

    public double getAvg() {
        if (window.isEmpty()) return 0; // technically the average is undefined
        return sum / window.size();
    }

    public void getData() throws IOException{

        BufferedReader br=new BufferedReader(new FileReader("Data.txt"));

        String b=br.readLine();
        String[] token = b.split(" ");      //Split into String array by space
        Double[] num = new Double[token.length];  //Create int array

        for(int x=0; x<token.length; x++)
            num[x] = Double.parseDouble(token[x]);  //Store all string array into int array
        br.close();
        int[] windowSizes = {2};
        //   List<String> dArr = new ArrayList<>();
        for (int windSize : windowSizes) {
            this.period = windSize;
            for (double x : num) {
                newNum(x);
                dArr.add(String.valueOf(getAvg()));
//            System.out.println("Next number = " + x + ", SMA = " + ma.getAvg());
            }
//        System.out.println();
        }
        System.out.println(AnalyticsActivity.dArr);

    }

    private CountDownTimer timer;

    void displayVoltage() {

        List<Double> doubleList = new ArrayList<Double>();
        for(String s : dArr) doubleList.add(Double.valueOf(s));

        int i=0;
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setProgress(Integer.parseInt(String.valueOf(doubleList.get(i))));
        timer.start();
        i = i+1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //stringArr = stringArr;
       // int i = 0;
        try {
            this.getData();
        } catch (Exception e) {
            Log.e("Error", "Error: " + e.toString());
        }

        timer = new CountDownTimer(500, 20) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                try {
                    displayVoltage();
                } catch (Exception e) {
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        }.start();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
