package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Redouane on 06/04/2016.
 */
public class ChargesActivity extends ColoredStatusBarActivity {

    @Bind(R.id.spinner)
    ImageView spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charges_add);
        ButterKnife.bind(this);

        setupActionBar();
        spinner.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(),
                        SpinnerChargeActivity.class)));
    }

    private void setupActionBar() {
        mActionBar.setActionBarListener(this);
        mActionBar.setTitle("Ajouter Une Charge");
        mActionBar.disableAddButton();
    }
}