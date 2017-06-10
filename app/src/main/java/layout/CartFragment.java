package layout;


import android.app.Activity;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.andrea.nav.BaseActivity;
import com.example.andrea.nav.DatabaseOperations;
import com.example.andrea.nav.FeedAdapterProductCartList;
import com.example.andrea.nav.MobileApi;
import com.example.andrea.nav.Prodotto;
import com.example.andrea.nav.R;
import com.example.andrea.nav.UrlTag;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends StandardFragment {

    public TextView total;
    public Button emptyCart;
    public ListView cart;
    public Float totalPrice;
    public Button order;


    public CartFragment() {

        // Required empty public constructor
    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        total = (TextView) getView().findViewById(R.id.total);
        emptyCart = (Button) getView().findViewById(R.id.emptyCart);
        cart = (ListView) getView().findViewById(R.id.cart);
        order = (Button) getView().findViewById(R.id.order);


        /*
        DatabaseOperations DOP = new DatabaseOperations(getApplicationContext());
        Cursor CR = DOP.getCartProduct(DOP);
        if( CR.getCount() !=0 ) {
            ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();
            CR.moveToFirst();
            Float tot = new Float(0);
            do {
                Prodotto p = new Prodotto();
                p.title = CR.getString(1);
                p.qta = CR.getString(3);
                p.price = Float.parseFloat(CR.getString(2));
                tot += p.price * Integer.parseInt(p.qta);
                prodotti.add(p);
            } while (CR.moveToNext());

            total.setText("Totale: " + tot.toString() + " €");

            FeedAdapterListProductCart feedAdapter ;
            feedAdapter = new FeedAdapterListProductCart(getApplicationContext(), R.layout.layout_lista_carrello);
            feedAdapter.addAll(prodotti);
            cart.setAdapter(feedAdapter);
        }
        */
        this.updateCartView();
        emptyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseOperations DOP = new DatabaseOperations(getContext());
                DOP.deleteAllCart(DOP);
                updateCartView();
                Toast.makeText(getContext(), "Carrello svuotato", Toast.LENGTH_SHORT).show();
            }
        });


        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                final RestApi api = new RestApi("ck_1370a4d992c642956279385e7824f523b760510b", "cs_d73a50cec9f97ee7c033b1f57ba602b671fa8994", "http://littlebonfi.altervista.org/wc-api/v2/");

                String url = api.makeRequest(RestApi.Method.POST, RestApi.Endpoint.ORDINI, new ArrayList<UrlTag>(), -1);
                Log.d("pippo", url);
                if (url != null) {
                    final RequestQueue queue = Volley.newRequestQueue(getContext());
                    final JSONObject jsonBody ;
                    final String requestBody;
                    DatabaseOperations DOP = new DatabaseOperations(getContext());
                    Cursor CR = DOP.getCartProduct(DOP);
                    try {
                        jsonBody = new JSONObject("{\"order\":{" +
                                "\"line_items\":"+ Prodotto.getJsonEncode(CR)+"," + " \"status\":\"processing\"," + " \"note\":\"pippone\""
                                + "}}");

                        requestBody = jsonBody.toString();

                        StringRequest stringRequest = new StringRequest(1, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("pippo", response);
                                DatabaseOperations DOP = new DatabaseOperations(getContext());
                                DOP.deleteAllCart(DOP);
                                updateCartView();
                                Toast.makeText(getContext(), "Ordine effettuato con successo", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                            @Override
                            public String getBodyContentType() {
                                return String.format("application/json; charset=utf-8");
                            }

                            @Override
                            public byte[] getBody() throws AuthFailureError {
                                try {
                                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                                } catch (UnsupportedEncodingException uee) {
                                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                            requestBody, "utf-8");
                                    return null;
                                }
                            }
                        };
                        queue.add(stringRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("pippo", e.toString());
                    }
                }
                */
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.order_confirm_dialog, null);
                Button mButtonOk = (Button) mView.findViewById(R.id.mBtnOk);
                Button mButtonCancel = (Button) mView.findViewById(R.id.mBtnCancel);
                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
                final TimePicker timePicker = (TimePicker) mView.findViewById(R.id.timePicker);

                timePicker.setEnabled(false);

                mBuilder.setView(mView);
                final AlertDialog  dialog = mBuilder.create();
                dialog.show();
               // Toast.makeText(getContext(), "Cliccato", Toast.LENGTH_SHORT).show();
                mButtonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                mButtonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int hour = -1;
                        int minutes = -1;
                        Date date = new Date();
                        List<UrlTag> urlTagArrayList = new ArrayList<UrlTag>();
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            hour = timePicker.getHour();
                            minutes = timePicker.getMinute();
                        }
                        if(hour >= serviceBeginHour && hour < serviceEndHour){
                            if(mSpinner.getSelectedItem().toString().equals("Oggi")){
                               // Toast.makeText(getContext(), new Integer(date.getHours()).toString(), Toast.LENGTH_SHORT).show();
                                if(hour > date.getHours() || (hour == date.getHours() && minutes > date.getMinutes())){
                                    String data = "2" + new Integer(date.getYear()).toString() + "-" + new Integer(date.getMonth()).toString() + "-" + new Integer(date.getDay()).toString();
                                    Log.d("pippo", data);

                                    String orario = new Integer(hour).toString() + ":" +  new Integer(minutes).toString() +":00";
                                    urlTagArrayList.add(new UrlTag("orario_ricezione", data + " " + orario  ));
                                    //makeOrder(new ArrayList<UrlTag>());
                                }else{
                                    Toast.makeText(getContext(), "Non abbiamo una macchina del tempo ci scusiamo per il disagio", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                makeOrder(new ArrayList<UrlTag>());
                            }
                        }else{
                            Toast.makeText(getContext(), "Il servizio è attivo dalle ore " + new Integer(serviceBeginHour).toString() + ":00 alle " + new Integer(serviceEndHour).toString() +":00" , Toast.LENGTH_LONG).show();
                        }




                      //makeOrder();
                    }
                });

                mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        String msupplier=mSpinner.getSelectedItem().toString();

                        if(!msupplier.equals("Ora")){
                            timePicker.setEnabled(true);
                            timePicker.setVisibility(View.VISIBLE);
                        }else {
                            //if(mLayout!=null) {
                                timePicker.setVisibility(View.INVISIBLE);
                                timePicker.setEnabled(false);
                            //}

                        }



                        Toast.makeText(getContext(), msupplier, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });





            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment






        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    public void makeOrder(List<UrlTag> urlTagArrayList){

            final MobileApi mobileApi = new MobileApi("ck_1370a4d992c642956279385e7824f523b760510b", "cs_d73a50cec9f97ee7c033b1f57ba602b671fa8994", backEndIpAddress + "/php/mobile_api.php");

            DatabaseOperations DOP = new DatabaseOperations(getContext());
            Cursor CR = DOP.getCartProduct(DOP);
            if(CR.getCount()>0) {

                urlTagArrayList.add(new UrlTag("request", "crea_ordine"));
                urlTagArrayList.add(new UrlTag("prodotti", Prodotto.getJsonEncode(CR)));

                String url = mobileApi.makeRequest(MobileApi.Endpoint.ORDINI, urlTagArrayList);


                if (url != null) {
                    final RequestQueue queue = Volley.newRequestQueue(getContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    DatabaseOperations DOP = new DatabaseOperations(getContext());
                                    DOP.deleteAllCart(DOP);
                                    updateCartView();
                                    Toast.makeText(getContext(), "Ordine effettuato con successo", Toast.LENGTH_SHORT).show();

                                    queue.stop();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    queue.stop();
                                }
                            });
                    queue.add(stringRequest);
                }
            }else
                Toast.makeText(getContext(), "Inserisci almeno un prodotto nel carrello", Toast.LENGTH_SHORT).show();
    }

    public void updateCartView(){
        // Toast.makeText(getApplicationContext(), "Update", Toast.LENGTH_SHORT).show();
        FeedAdapterProductCartList feedAdapter ;
        feedAdapter = new FeedAdapterProductCartList(getContext(), R.layout.layout_lista_carrello);

        feedAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                Log.e("pippo", "changed");
                Float tot = new Float(0);
                DatabaseOperations DOP = new DatabaseOperations(getContext());
                Cursor CR = DOP.getCartProduct(DOP);
                if( CR.getCount() !=0 ) {

                    CR.moveToFirst();

                    do {
                        Prodotto p = new Prodotto();
                        p.id = CR.getString(0);
                        p.title = CR.getString(1);
                        p.qta = CR.getString(3);
                        p.price = Float.parseFloat(CR.getString(2));
                        tot += p.price * Integer.parseInt(p.qta);

                    } while (CR.moveToNext());

                }

                totalPrice = tot;
                total.setText("Totale: " + tot.toString() + " €");
            }
        });

        Float tot = new Float(0);
        DatabaseOperations DOP = new DatabaseOperations(getContext());
        Cursor CR = DOP.getCartProduct(DOP);
        if( CR.getCount() !=0 ) {
            ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();
            CR.moveToFirst();

            do {
                Prodotto p = new Prodotto();
                p.id = CR.getString(0);
                p.title = CR.getString(1);
                p.qta = CR.getString(3);
                p.price = Float.parseFloat(CR.getString(2));
                p.thumbnail = CR.getString(4);
                tot += p.price * Integer.parseInt(p.qta);
                prodotti.add(p);
            } while (CR.moveToNext());
            feedAdapter.addAll(prodotti);
        }
        cart.setAdapter(feedAdapter);
        totalPrice = tot;
        total.setText("Totale: " + tot.toString() + " €");
    }

    public void updateTotal(Float diff){
        this.totalPrice = this.totalPrice + diff;
        total.setText("Totale: " + totalPrice.toString() + " €");
    }

}
