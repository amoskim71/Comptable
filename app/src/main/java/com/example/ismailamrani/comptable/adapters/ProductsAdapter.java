package com.example.ismailamrani.comptable.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.base.BaseSearchAdapter;
import com.example.ismailamrani.comptable.adapters.base.BinderViewHolder;
import com.example.ismailamrani.comptable.customitems.CustomTextView;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.ui.ModifierProduitActivity;
import com.example.ismailamrani.comptable.ui.ProductDetailsActivity;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.parsing.JSONUtils;
import com.example.ismailamrani.comptable.webservice.PhpAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mohammed Aouf ZOUAG on 04/06/2016.
 */
public class ProductsAdapter extends BaseSearchAdapter<ProductsAdapter.ViewHolder, Product> {

    private ProductListener listener;

    public ProductsAdapter(Context context, List<Product> products) {
        super(context, products);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.produit_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
    }

    public class ViewHolder extends BinderViewHolder<Product> {

        @Bind(R.id.Info)
        RelativeLayout infos;
        @Bind(R.id.Actions)
        RelativeLayout actions;

        @Bind(R.id.Image)
        ImageView image;
        @Bind(R.id.Libelle)
        CustomTextView label;
        @Bind(R.id.Qte)
        CustomTextView quantity;
        @Bind(R.id.Prix)
        CustomTextView price;
        @Bind(R.id.supprimer)
        CustomTextView delete;
        @Bind(R.id.modifier)
        CustomTextView update;
        @Bind(R.id.afficher)
        CustomTextView show;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            actions.setVisibility(View.GONE);
            infos.setOnClickListener(v -> actions.setVisibility(View.VISIBLE));

            delete.setOnClickListener(v -> {
                String id = mItems.get(getLayoutPosition()).getID() + "";
                if (listener != null) {
                    listener.onDeleteProduct(PhpAPI.removeProduit,
                            JSONUtils.bundleIDToJSON(id), Method.POST);
                }
            });
            update.setOnClickListener(v -> {
                Intent i = new Intent(mContext, ModifierProduitActivity.class);
                i.putExtra("id", mItems.get(getLayoutPosition()).getID());
                mContext.startActivity(i);
            });

            show.setOnClickListener(v -> {
                Intent i = new Intent(mContext, ProductDetailsActivity.class);
                i.putExtra("product", mItems.get(getLayoutPosition()));
                mContext.startActivity(i);
            });
        }

        @Override
        public void bind(Product product) {
            Picasso.with(mContext)
                    .load(product.getPhoto())
                    .into(image);

            label.SetText(product.getLibelle());
            quantity.SetText("" + product.getQte());
            price.SetText(product.getPrixTTC() + " DH");
        }
    }

    public void setProductListener(ProductListener listener) {
        this.listener = listener;
    }

    public interface ProductListener {
        void onDeleteProduct(String url, JSONObject params, Method method);
    }
}