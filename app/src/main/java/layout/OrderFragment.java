package layout;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.andrea.nav.FeedAdapterProductListOrder;
import com.example.andrea.nav.MainActivity;
import com.example.andrea.nav.MobileApi;
import com.example.andrea.nav.Ordine;
import com.example.andrea.nav.Prodotto;
import com.example.andrea.nav.R;
import com.example.andrea.nav.RestApi;
import com.example.andrea.nav.UrlTag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends StandardListFragment {
    TextView orderId;
    TextView orderStatus;
    TextView orderTotal;
    TextView date;
    TextView time;
    Ordine ordine;
    FeedAdapterProductListOrder ordineArrayAdapter = null;


    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        ordine = (Ordine) args.getSerializable("order");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        orderId = (TextView) getView().findViewById(R.id.idMyOrder);
        orderStatus = (TextView) getView().findViewById(R.id.statusMyOrder);
        orderTotal = (TextView) getView().findViewById(R.id.totalMyOrder);
        date = (TextView) getView().findViewById(R.id.dateMyOrder);
        time = (TextView) getView().findViewById(R.id.timeMyOrder);
        final ListView listView = (ListView) getView().findViewById(R.id.pippo);

        /*
        final RestApi api = new RestApi("ck_70cac7d595c237c071ed2e1b04992c33314b5d35", "cs_5ad2850616cc44b6620f096f3b279fd13e43fb45", "http://littlebonfi.altervista.org/wc-api/v2/");
        List<UrlTag> urlTagList = new ArrayList<UrlTag>();
        // aggiungi filtri per customer
        //urlTagList.add(new UrlTag("fields","id%2Cnote%2Cstatus%2Ccreated_at%2Ctotal"));

        String url = api.makeRequest(RestApi.Method.GET,RestApi.Endpoint.ORDINE, urlTagList, Integer.parseInt(ordine.id));

        if(url != null){
            final RequestQueue queue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            api.result = response;

                            Ordine temp = api.parseResponseToOrder();
                            if(temp!=null) {

                                orderId.setText("Numero Ordine: " + temp.id);
                                orderStatus.setText("Stato: " + temp.status);

                                FeedAdapterProductListOrder feedAdapter ;
                                feedAdapter = new FeedAdapterProductListOrder(getContext(), R.layout.layout_lista_prodotti_ordine);
                                Log.d("pippo", new Integer(temp.prodotti.size()).toString());

                                feedAdapter.addAll(temp.prodotti);


                                listView.setAdapter(feedAdapter);
                                orderTotal.setText("Totale: " + temp.total.toString() + " €");

                                //feedAdapter.addAll(ordini);
                            }
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
        */

        final MobileApi mobileApi = new MobileApi("ck_1370a4d992c642956279385e7824f523b760510b", "cs_d73a50cec9f97ee7c033b1f57ba602b671fa8994", backEndIpAddress + "php/mobile_api.php");
        List<UrlTag> urlTagArrayList = new ArrayList<UrlTag>();
        urlTagArrayList.add(new UrlTag("request", "ordine"));
        urlTagArrayList.add(new UrlTag("id", ordine.id));



        String url = mobileApi.makeRequest(MobileApi.Endpoint.ORDINE,urlTagArrayList);

        if(url != null){
            final RequestQueue queue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            mobileApi.result = response;

                            Ordine temp = mobileApi.parseResponseToOrder(backEndIpAddress);
                            Log.d("pippo",response);

                            if(temp!=null) {

                                orderId.setText("Ordine #: " + temp.id);
                                orderStatus.setText("Stato: " + temp.status);
                                date.setText("Data di creazione: "+temp.createdAt.getDay() + "/" + temp.createdAt.getMonth());
                                time.setText("Ora di creazione: " + temp.createdAt.getHours() + ":" + temp.createdAt.getMinutes());

                                FeedAdapterProductListOrder feedAdapter ;
                                feedAdapter = new FeedAdapterProductListOrder(getContext(), R.layout.layout_lista_prodotti_ordine);
                                //Log.d("pippo", new Integer(temp.prodotti.size()).toString());

                                feedAdapter.addAll(temp.prodotti);


                                listView.setAdapter(feedAdapter);
                                orderTotal.setText("Totale: " + temp.total.toString() + " €");

                                //feedAdapter.addAll(ordini);
                            }

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



       // ListView listView = (ListView) getListView().findViewById(R.id.list);
        //listView.setAdapter(ordineArrayAdapter);
    }



}
