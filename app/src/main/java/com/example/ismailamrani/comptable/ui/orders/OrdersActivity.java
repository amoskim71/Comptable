package com.example.ismailamrani.comptable.ui.orders;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.ui.fragments.OrdersListFragment;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrdersActivity extends ColoredStatusBarActivity
        implements OrdersListFragment.OrderListFragListener {

    @Bind(R.id.MyActionBar)
    protected OGActionBar mActionBar;

    private String currentOrderType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ButterKnife.bind(this);

        setupOrdersType();
        setupActionBar();

        displayFragment(0); // always display first fragment at startup
    }

    /**
     * @param position of the fragment to show
     */
    private void displayFragment(int position) {
        Fragment fragment = null;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        switch (position) {
            case 0:
                fragment = OrdersListFragment.newInstance(currentOrderType);
                ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
                break;
        }

        ft.replace(R.id.frame_container, fragment, "fragment" + position);
        ft.commit();
    }

    protected void setupActionBar() {
        mActionBar.setActionBarListener(this);
        mActionBar.setTitle(currentOrderType.equals("PURCHASE") ?
                "Commandes achats" : "Commandes ventes");
    }

    /**
     * Retrieves the type of orders to be currently displayed.
     */
    private void setupOrdersType() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle data = intent.getExtras();
            if (data != null) {
                currentOrderType = data.getString("orderType");
            }
        }
    }

    @Override
    public void onAddPressed() {
        startActivity(new Intent(this, "PURCHASE".equals(currentOrderType) ?
                PurchasesActivity.class : SalesActivity.class));
    }

    /**
     * Fetches orders' data on behalf of the fragment.
     * @param url
     * @param listener
     */
    @Override
    public void fetchOrders(String url, RequestListener listener) {
        sendHTTPRequest(url, null, Method.GET, listener);
    }
}
