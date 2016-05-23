package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.utils.JSONUtils;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;
import com.example.ismailamrani.comptable.webservice.PhpAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

/**
 * Created by Ismail Amrani on 25/03/2016.
 */
public class ProductDetailsActivity extends ColoredStatusBarActivity {
    ImageView Image;
    int id;

    TextView PrixHT, PrixTTC, Stock, Code, Libelle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produit_info);
        ButterKnife.bind(this);

        setupActionBar();

        Image = (ImageView) findViewById(R.id.Imageaff);
        PrixHT = (TextView) findViewById(R.id.PrixHTaff);
        PrixTTC = (TextView) findViewById(R.id.PrixTTCaff);
        Code = (TextView) findViewById(R.id.Codeaff);
        Stock = (TextView) findViewById(R.id.Stockaff);
        Libelle = (TextView) findViewById(R.id.Libelleaff);

        Intent i = getIntent();
        id = i.getExtras().getInt("id");
        sendHTTPRequest(PhpAPI.getProduitById, JSONUtils.bundleIDToJSON(id), Method.POST,
                new RequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response, int status) {
                        if (status == 1) {
                            try {
                                JSONArray listproduits = response.getJSONArray("produit");

                                for (int i = 0; i < listproduits.length(); i++) {
                                    JSONObject usr = listproduits.getJSONObject(i);
                                    Picasso.with(getApplicationContext()).load(PhpAPI.IpBackend + "produits/" + usr.getString("photo")).into(Image);
                                    Libelle.setText(usr.getString("libelle"));
                                    PrixHT.setText(usr.getString("prixHT"));
                                    PrixTTC.setText(usr.getString("prixTTC"));
                                    Code.setText(usr.getString("codeBar"));
                                    Stock.setText(usr.getString("qte"));

                                    mActionBar.setTitle(usr.getString("libelle"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else if (status == 0) {
                            Toast.makeText(getApplicationContext(), "Produit Not Found  !!!!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onRequestFailed() {
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(),
                                "Produit Not Found  !!!!", Toast.LENGTH_LONG).show());
                    }
                });
    }

    private void setupActionBar() {
        mActionBar.setActionBarListener(this);
    }

    @Override
    public void onAddPressed() {
        startActivity(new Intent(this, AddProductActivity.class));
    }
}
