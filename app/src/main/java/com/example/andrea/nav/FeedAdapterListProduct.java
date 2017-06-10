package com.example.andrea.nav;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class FeedAdapterListProduct extends ArrayAdapter<Prodotto> {

    public int resource;

    public FeedAdapterListProduct(Context context, int resource) {
        super(context, resource);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(resource, null);
        }

        final Prodotto prodotto = getItem(position);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        TextView description = (TextView) convertView.findViewById(R.id.description);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.thumbanil);
        ImageButton imageButton = (ImageButton) convertView.findViewById((R.id.imageButton));
        imageButton.setFocusable(false);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseOperations DB = new DatabaseOperations(getContext());
                DB.insertProductOnCart2(DB, prodotto.id, prodotto.title, prodotto.price.toString(), "1", prodotto.thumbnail);
                Toast.makeText(getContext(),  "Prodotto aggiunto al carrello", Toast.LENGTH_SHORT).show();
            }
        });
        if(!prodotto.thumbnail.equals(""))
            Picasso.with(getContext()).load(prodotto.thumbnail).into(imageView);
        title.setText(prodotto.title);
        price.setText((prodotto.price+" €"));
        description.setText(prodotto.shortDescripton);
        return convertView;
    }
}