package com.example.andrea.nav;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.Date;

/**
 * Created by andrea on 21/03/17.
 */

public class FeedAdapterCategoria extends ArrayAdapter<Categoria> {

    public int resource;
    public Context context;


    public FeedAdapterCategoria(Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, null);
        }



        Categoria categoria = (Categoria) getItem(position);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.thumbnail);
        if(!categoria.thumbnail.equals(""))
            Picasso.with(getContext()).load(categoria.thumbnail).into(imageView);

        title.setText(categoria.title);
        Log.d("pippo", categoria.title);
        return convertView;
    }

    @Override
    public void addAll(Collection<? extends Categoria> collection) {
        super.addAll(collection);
        Log.d("pippo", "fmrlfmlr");
    }
}





/*
class FeedAdapterListProductCart extends ArrayAdapter<Prodotto> {

    public int resource;

    public Boolean dataCahnged = false;


    public FeedAdapterListProductCart(Context context, int resource) {
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
        final TextView title = (TextView) convertView.findViewById(R.id.title);
        final TextView price = (TextView) convertView.findViewById(R.id.price);
        final EditText qta = (EditText) convertView.findViewById(R.id.qta);
        final ImageButton deleteItem = (ImageButton) convertView.findViewById(R.id.deleteItem);

        title.setText(prodotto.title);
        final Float i = prodotto.price * Integer.parseInt(prodotto.qta);

        price.setText((i.toString()+" €"));
        qta.setText(prodotto.qta);

        final Long[] timePrec = {System.currentTimeMillis() / 1000};
        qta.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Long tsLong = System.currentTimeMillis()/1000;
                if(tsLong> timePrec[0]) {
                    timePrec[0] = tsLong;
                    if (!hasFocus){

                        String res = qta.getText().toString();
                        Log.d("pippo", res);
                        if(!res.equals("0")){

                            Integer i = Integer.parseInt(res);
                            DatabaseOperations DOP = new DatabaseOperations(getContext());
                            Float newPrice = DOP.updateQtaProductOnCart(DOP, prodotto.id, res);
                            prodotto.qta = res;
                            price.setText(newPrice.toString() + " €/");
                            qta.setText(res);
                            notifyDataSetChanged();

                            Log.d("pippo", "false");
                        }
                    }

                }
            }
        });

        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseOperations DOP = new DatabaseOperations(getContext());
                DOP.deleteItemFromCart(DOP, prodotto.id);
                remove(prodotto);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}


class FeedAdapterListProductOrder extends ArrayAdapter<Prodotto> {

    public int resource;

    public Boolean dataCahnged = false;


    public FeedAdapterListProductOrder(Context context, int resource) {
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



        title.setText(prodotto.title);
        final Float i = prodotto.price * Integer.parseInt(prodotto.qta);

        price.setText((i.toString()+" €"));
        qta.setText(prodotto.qta);

        final Long[] timePrec = {System.currentTimeMillis() / 1000};
        qta.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Long tsLong = System.currentTimeMillis()/1000;
                if(tsLong> timePrec[0]) {
                    timePrec[0] = tsLong;
                    if (!hasFocus){

                        String res = qta.getText().toString();
                        Log.d("pippo", res);
                        if(!res.equals("0")){

                            Integer i = Integer.parseInt(res);
                            DatabaseOperations DOP = new DatabaseOperations(getContext());
                            Float newPrice = DOP.updateQtaProductOnCart(DOP, prodotto.id, res);
                            prodotto.qta = res;
                            price.setText(newPrice.toString() + " €/");
                            qta.setText(res);
                            notifyDataSetChanged();

                            Log.d("pippo", "false");
                        }
                    }

                }
            }
        });


        return convertView;
    }
}


class FeedAdapterListOrders extends ArrayAdapter<Ordine> {

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

        Date date = ordine.createdAt.getTime();
        price.setText((ordine.total+" €"));
        title.setText(new Integer(date.getDay()) + "/" + new Integer(date.getMonth()) + " " + new Integer(date.getHours()) + ":" + new Integer(date.getMinutes()) + "     " + "Stato: " +ordine.status);
        return convertView;
    }
}
*/
