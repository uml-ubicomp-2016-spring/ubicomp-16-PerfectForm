package com.sunshine.coldashes.perfectform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;

/**
 * Created by Coldashes on 4/29/2016.
 */
public class Results extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceStates) {
        super.onCreate(savedInstanceStates);
        setContentView(R.layout.activity_done);

        ArrayList<Double> paceArray = (ArrayList<Double>) getIntent().getSerializableExtra("paceArray");
        String paceCond = getIntent().getStringExtra("paceCond");
        ArrayList<Float> pointsArr = (ArrayList<Float>) getIntent().getSerializableExtra("pointsArr");

        ImageView paceresult_imageview = (ImageView) findViewById(R.id.paceresult_imageview);
        ImageView formresult_imageview = (ImageView) findViewById(R.id.formresults_imageview);

        TextView paceSuggestion_tv = (TextView) findViewById(R.id.workon_textview);
        TextView formSuggestion_tv = (TextView) findViewById(R.id.formresults_tv);

        if (paceCond.equals("slow")) {
            paceSuggestion_tv.setText("Speed up next time!");
            paceresult_imageview.setImageResource(R.drawable.ic_slow);
        } else if (paceCond.equals("fast")) {
            paceSuggestion_tv.setText("Slow it down next time!");
            paceresult_imageview.setImageResource(R.drawable.ic_fast);
        } else {
            paceSuggestion_tv.setText("Really nice pace!");
            paceresult_imageview.setImageResource(R.drawable.ic_good);
        }

        GraphView pace_graph = (GraphView) findViewById(R.id.pace_graph);
        GraphView form_graph = (GraphView) findViewById(R.id.form_graph);

        int x = 0;
        while (x < 25) {
            paceArray.add(200.0);
            x++;
        }
        while (x < 35) {
            paceArray.add(190.0);
            x++;
        }
        while (x < 60) {
            paceArray.add(175.0);
            x++;
        }
        while (x < 120) {
            paceArray.add(180.0);
            x++;
        }

        x = 0;
        while (x < 25) {
            pointsArr.add(0f);
            x++;
        }
        while (x < 35) {
            pointsArr.add(50f);
            x++;
        }
        while (x < 60) {
            pointsArr.add(100f);
            x++;
        }
        while (x < 120) {
            pointsArr.add(58f);
            x++;
        }

        int size = pointsArr.size() - 1;
        float total = pointsArr.get(size);
        if (total < (size / 2)) {
            formSuggestion_tv.setText("You need to pay more attention to form!");
            formresult_imageview.setImageResource(R.drawable.ic_badform);
        } else {
            formSuggestion_tv.setText("Good form keep it up!");
            formresult_imageview.setImageResource(R.drawable.ic_goodform);
        }

        DataPoint[] paces = convertToArray(paceArray, 0);
        DataPoint[] pointsOT = convertToArray(pointsArr);

        LineGraphSeries<DataPoint> paceSeries = new LineGraphSeries<DataPoint>(paces);
        pace_graph.addSeries(paceSeries);

        LineGraphSeries<DataPoint> pointsSeries = new LineGraphSeries<DataPoint>(pointsOT);
        form_graph.addSeries(pointsSeries);

        Viewport paceview = pace_graph.getViewport();
        paceview.setYAxisBoundsManual(true);
        paceview.setMaxY(220);
        paceview.setMinY(140);
        paceview.setXAxisBoundsManual(true);
        paceview.setMaxX(paceArray.size());
        paceview.setMinX(0);

        Viewport formview = form_graph.getViewport();
        formview.setYAxisBoundsManual(true);
        double maxY = formview.getMaxY(true);
        double minY = formview.getMinY(true);
        formview.setMaxY(maxY);
        formview.setMinY(minY);
        formview.setXAxisBoundsManual(true);
        formview.setMaxX(paceArray.size());
        formview.setMinX(0);
    }

    public DataPoint[] convertToArray(ArrayList<Double> theList, int num) {
        int i = 0;
        DataPoint[] newArray = new DataPoint[theList.size()];
        while (i < theList.size()) {
            newArray[i] = new DataPoint(i, theList.get(i));
            i++;
        }
        return newArray;
    }

    public DataPoint[] convertToArray(ArrayList<Float> theList) {
        int i = 0;
        DataPoint[] newArray = new DataPoint[theList.size()];
        while (i < theList.size()) {
            newArray[i] = new DataPoint(i, theList.get(i));
            i++;
        }
        return newArray;
    }

    public void restart(View view) {
        Intent main_menu = new Intent(this, MainActivity.class);
        startActivity(main_menu);
    }
}
