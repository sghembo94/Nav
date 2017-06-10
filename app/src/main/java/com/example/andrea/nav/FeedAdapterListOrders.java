package com.example.andrea.nav;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.andrea.nav.Ordine;

import java.util.Date;

/**
 * Created by andrea on 01/04/17.
 */

public class FeedAdapterListOrders extends ArrayAdapter<Ordine> {

    public int resource;

    public FeedAdapterListOrders(Context context, int resource) {
        super(context, resource);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(resource, null);
        }

        Ordine ordine = getItem(position);
        TextView title = (TextView) convertView.findViewById(R.id.orderTitle);
        TextView price = (TextView) convertView.findViewById(R.id.orderTotalPrice);
        TextView id = (TextView) convertView.findViewById(R.id.orderId);


        id.setText(ordine.id);

        //Date date = ordine.createdAt.getTime();
        price.setText(("Totale: " + ordine.total + " €"));
        title.setText(new Integer(ordine.createdAt.getDay()) + "/" + new Integer(ordine.createdAt.getMonth()) + " " + new Integer(ordine.createdAt.getHours()) + ":" + new Integer(ordine.createdAt.getMinutes()) + "     " + "Stato: " );
        return convertView;
    }
}
