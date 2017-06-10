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

/**
 * Created by andrea on 01/04/17.
 */

public class FeedAdapterProductCartList extends ArrayAdapter<Prodotto> {

    public int resource;

    public Boolean dataCahnged = false;


    public FeedAdapterProductCartList(Context context, int resource) {
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
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView2);

        title.setText(prodotto.title);
        final Float i = prodotto.price * Integer.parseInt(prodotto.qta);

        price.setText((i.toString()+" €"));
        qta.setText(prodotto.qta);
        if(!prodotto.thumbnail.equals(""))
            Picasso.with(getContext()).load(prodotto.thumbnail).into(imageView);

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