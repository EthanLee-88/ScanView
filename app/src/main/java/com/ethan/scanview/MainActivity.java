package com.ethan.scanview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.ethan.scanview.view.PointPosition;
import com.ethan.scanview.view.PointPositionKt;
import com.ethan.scanview.view.ScanView;
import com.ethan.scanview.view.ScanViewKt;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ScanViewKt mScanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScanView = findViewById(R.id.id_radar_view);
    }

    public void viewClick(View view) {
        mScanView.addPoint(new PointPositionKt().setRank(new Random().nextInt(80)));
    }

    public void buttonClick(View view) {
        mScanView.clearPoint();
    }

    public void stopScan(View view) {
        mScanView.setScanStop();
    }

    public void startScan(View view) {
        mScanView.setStartScan();
    }
}