package com.example.andrea.nav;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by andrea on 01/04/17.
 */

public class FeedAdapterProductListOrder extends ArrayAdapter<Prodotto> {

    public int resource;

    public Boolean dataCahnged = false;


    public FeedAdapterProductListOrder(Context context, int resource) {
        super(context, resource);
        this.resource = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(resource, null);
        }

        final Prodotto prodotto = getItem(position);
        final TextView title = (TextView) convertView.findViewById(R.id.nameMyOrder);
        final TextView price = (TextView) convertView.findViewById(R.id.priceMyOrder);
        final TextView qta = (TextView) convertView.findViewById(R.id.qtaMyOrder);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.thumbanilOrder);
        if(!prodotto.thumbnail.equals(""))
            Picasso.with(getContext()).load(prodotto.thumbnail).into(imageView);



        title.setText(prodotto.title);
        final Float i = prodotto.price * Integer.parseInt(prodotto.qta);

        price.setText((i.toString() + " €"));
        qta.setText(prodotto.qta);





        return convertView;
    }
}