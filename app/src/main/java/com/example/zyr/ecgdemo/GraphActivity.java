package com.example.zyr.ecgdemo;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GraphActivity extends AppCompatActivity {
    private SensorManager msensorManager;
    private Sensor mlight;
    private SensorEventListener msensorEventListener;
    private StringBuffer mBuffer;
    private final Handler mHandler = new Handler();

    private static float sensorValue;
    private Runnable mTimer;
    private LineGraphSeries mSeries;
    private double graphLastXValue = 5d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        //layout init
        Button muploadButton = (Button)findViewById(R.id.upload_button);
        GraphView mgraph = (GraphView)findViewById(R.id.graph);

        mSeries = new LineGraphSeries<>();
        mgraph.addSeries(mSeries);
        initGraph(mgraph);

        //sensor init
        msensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mlight = msensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        msensorManager.registerListener(msensorEventListener,mlight,SensorManager.SENSOR_DELAY_NORMAL);
        msensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float value = event.values[0];
                //mBuffer.append(value);
                //mBuffer.append("  ");
                sensorValue = value;
                //writeCsvFile(mBuffer.toString());
                //mtv.setText(mBuffer.toString());


                //always show new data in the bottom of textview
                /*
                mtv.setMovementMethod(ScrollingMovementMethod.getInstance());
                int offset = mtv.getLineCount() * mtv.getLineHeight();
                if (offset > mtv.getHeight()) {
                    mtv.scrollTo(0, offset - mtv.getHeight());
                }
                */

                graphLastXValue += 1d;
                mSeries.appendData(new DataPoint(graphLastXValue,value),true,40);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        //Button responses
        muploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showIntent = new Intent(GraphActivity.this, ShowDBDataActivity.class);
                startActivity(showIntent);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        msensorManager.registerListener(msensorEventListener,mlight,SensorManager.SENSOR_DELAY_NORMAL);
        mHandler.postDelayed(mTimer,1000);
        mTimer = new Runnable() {
            @Override
            public void run() {
                graphLastXValue += 1d;
                mSeries.appendData(new DataPoint(graphLastXValue,sensorValue),true,40);
                mHandler.postDelayed(this, 200);
            }
        };

    }

    @Override
    public void onPause(){
        super.onPause();
        mHandler.removeCallbacks(mTimer);
        msensorManager.unregisterListener(msensorEventListener,mlight);
    }

    private String getCurrentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDate = dateFormat.format(Calendar.getInstance().getTime());
        return  currentDate;
    }

    private void initGraph(GraphView graphView){
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setYAxisBoundsManual(true);

        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(600);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(40);
        graphView.getGridLabelRenderer().setTextSize(30);
        graphView.setTitle(getCurrentDate());
        graphView.getGridLabelRenderer().setVerticalAxisTitle("Sensor Value");
    }

    //potential useful methods
    private static void writeCsvFile(String string) {
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String date = df.format(Calendar.getInstance().getTime());
        try {
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "SensorData.csv");
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append(string);
            //bw.newLine();
            bw.flush();
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    private double getDateX() {

        long totalMilliSeconds = System.currentTimeMillis();
        long currentSecond = Long.parseLong((new SimpleDateFormat("s", Locale.US)).format(new Date(totalMilliSeconds)));
        //System.out.println("Seconds: " + currentSecond);
        long currentMinute = Long.parseLong((new SimpleDateFormat("m", Locale.US)).format(new Date(totalMilliSeconds)));
        //System.out.println("Minutes: " + currentMinute);
        long currentHour = Long.parseLong((new SimpleDateFormat("H", Locale.US)).format(new Date(totalMilliSeconds)));
        //System.out.println("Hour:" + currentHour);
        double myTime = currentSecond + currentMinute * 60 + currentHour * 60 * 60;
        //System.out.println("Time: " + myTime);
        return myTime;

    }
    private  void readCsvFile(){
        BufferedReader br = null;
        StringBuilder content = new StringBuilder();
        try{
            //each time readline() called, the implicit cursor moves to the next line
            String sCurrentLine;
            br = new BufferedReader(new FileReader(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM+ "SensorData.csv")));
            while((sCurrentLine = br.readLine()) != null) {
                content.append(sCurrentLine);
                System.out.println("the Current Line value is :" + content);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try{
                if (br != null){
                    br.close();
                }
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}
