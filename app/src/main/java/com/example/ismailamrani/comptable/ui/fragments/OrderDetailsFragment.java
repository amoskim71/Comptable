package com.example.ismailamrani.comptable.ui.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.OrderDetailsAdapter;
import com.example.ismailamrani.comptable.models.OrderDetail;
import com.example.ismailamrani.comptable.utils.http.RequestListener;
import com.example.ismailamrani.comptable.utils.http.SuccessRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailsFragment extends Fragment {

    private int currentOrderStatus;
    private OrderDetailsFragListener listener;
    private OrderDetailsAdapter adapter;

    @Bind(R.id.detailsListView)
    ListView detailsListView;
    @Bind(R.id.totalValueLabel)
    TextView totalValueLabel;
    @Bind(R.id.facturerButton)
    Button facturerButton;

    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    public static OrderDetailsFragment newInstance(int orderID, int factureStatus) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();

        args.putInt("orderID", orderID);
        args.putInt("factureStatus", factureStatus);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        currentOrderStatus = getCurrentOrderStatus(args);

        setupFacturerButton();

        if (listener != null)
            listener.fetchOrderDetails(new OrderDetailsListener());

        return view;
    }

    private void setupFacturerButton() {
        facturerButton.setVisibility(currentOrderStatus == 1 ? View.INVISIBLE : View.VISIBLE);
        facturerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.chargeOrder(new SuccessRequestListener() {
                        @Override
                        public void onRequestSucceeded(JSONObject response) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    facturerButton.setVisibility(View.INVISIBLE);
                                    listener.onOrderCharged();
                                }
                            });
                        }
                    });
            }
        });
    }

    private int getCurrentOrderStatus(Bundle data) {
        if (data != null)
            return data.getInt("factureStatus");

        throw new IllegalStateException("You need to pass an order status to the fragment.");
    }

    private class OrderDetailsListener extends SuccessRequestListener {
        @Override
        public void onRequestSucceeded(JSONObject response) {
            try {
                JSONArray jsonArray = response.getJSONArray("orderDetails");
                final List<OrderDetail> details = OrderDetail.parseSuppliers(jsonArray);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new OrderDetailsAdapter(getActivity(), details);
                        totalValueLabel.setText("" + adapter.getTotalPrice() + " DH");
                        detailsListView.setAdapter(adapter);
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OrderDetailsFragListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OrderDetailsFragListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OrderDetailsFragListener {
        void fetchOrderDetails(RequestListener requestListener);

        void chargeOrder(RequestListener requestListener);

        void onOrderCharged();
    }
}
