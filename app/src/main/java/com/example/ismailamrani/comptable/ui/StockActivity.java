package com.example.ismailamrani.comptable.ui;

import android.app.Activity;
import android.os.Bundle;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.customitems.ColorStatutBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;

public class StockActivity extends Activity implements OGActionBarInterface {

    OGActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        new ColorStatutBar().ColorStatutBar(this);

        mActionBar = (OGActionBar) findViewById(R.id.MyActionBar);
        mActionBar.setActionBarListener(this);
        mActionBar.setTitle("Stock");
    }

    @Override
    public void onMenuPressed() {

    }

    @Override
    public void onAddPressed() {

    }
}
