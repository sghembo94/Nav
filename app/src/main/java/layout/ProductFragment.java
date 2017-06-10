package layout;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrea.nav.DatabaseOperations;
import com.example.andrea.nav.Prodotto;
import com.example.andrea.nav.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends Fragment {
    public String id;
    public TextView productName;

    TextView title ;
    TextView description;
    TextView price;


    EditText quantita;
    Context context;
    Button addToCart;
    Button orderListButton;
    ImageView imageView;
    Prodotto prodotto = null;


    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        prodotto = (Prodotto) args.getSerializable("product");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getContext();
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productName = (TextView) getView().findViewById(R.id.title);
        productName.setText(id);

        title = (TextView) getView().findViewById(R.id.title);
        description = (TextView) getView().findViewById(R.id.description);
        price = (TextView) getView().findViewById(R.id.price);
        //textViewRating = (TextView) findViewById(R.id.rating);
        imageView = (ImageView) getView().findViewById(R.id.imageView);

        quantita = (EditText) getView().findViewById(R.id.quantita);

        addToCart = (Button) getView().findViewById(R.id.addToCart);




        final Prodotto p = prodotto ;




        if(!p.thumbnail.equals(""))
            Picasso.with(context).load(p.thumbnail).into(imageView);

        //Post.getProduct2(getApplicationContext(), prodotto, title, description, price, imageView, textViewRating, rating);
        title.setText(p.title);
        description.setText("Descrizione:\n" + p.description);
        price.setText("Prezzo: " + p.price.toString() + " €");



        quantita.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Integer i = new Integer(quantita.getText().toString());
                //Toast.makeText(context,  .toString(), Toast.LENGTH_SHORT).show();
                Float f ;
                f = p.price * i;
                price.setText("Prezzo: " + f.toString() + " €");
                return false;
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseOperations DB = new DatabaseOperations(context);
                DB.insertProductOnCart2(DB, p.id, p.title, p.price.toString(), quantita.getText().toString(), p.thumbnail);
                Toast.makeText(context,  "Prodotto aggiunto al carrello", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
