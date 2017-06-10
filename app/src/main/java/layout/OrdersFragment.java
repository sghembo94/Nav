package layout;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.andrea.nav.FeedAdapterListOrders;
import com.example.andrea.nav.MobileApi;
import com.example.andrea.nav.Ordine;
import com.example.andrea.nav.Prodotto;
import com.example.andrea.nav.R;
import com.example.andrea.nav.RestApi;
import com.example.andrea.nav.UrlTag;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends StandardListFragment {


    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /*
        final RestApi api = new RestApi("ck_70cac7d595c237c071ed2e1b04992c33314b5d35", "cs_5ad2850616cc44b6620f096f3b279fd13e43fb45", "http://littlebonfi.altervista.org/wc-api/v2/");
        List<UrlTag> urlTagList = new ArrayList<UrlTag>();
        // aggiungi filtri per customer
        urlTagList.add(new UrlTag("fields", "id%2Cnote%2Cstatus%2Ccreated_at%2Ctotal"));
        String url = api.makeRequest(RestApi.Method.GET, RestApi.Endpoint.ORDINI, urlTagList, -1);

        if (url != null) {
            final RequestQueue queue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            api.result = response;
                            Log.d("pippo", response);
                            ArrayList<Ordine> ordini = api.parseResponseToOrdersList();
                            if (ordini != null) {
                                FeedAdapterListOrders feedAdapter;
                                feedAdapter = new FeedAdapterListOrders(getContext(), R.layout.layout_lista_ordini);
                                for (int i = 0; i < ordini.size(); i++) {
                                    Ordine temp = ordini.get(i);
                                    if (temp.note.equals("pippone") && !temp.status.equals("completed")) {
                                        feedAdapter.add(temp);
                                    }
                                }
                                //feedAdapter.addAll(ordini);
                                setListAdapter(feedAdapter);
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
        urlTagArrayList.add(new UrlTag("request", "ordini"));
        String url = mobileApi.makeRequest(MobileApi.Endpoint.ORDINI,urlTagArrayList);

        if (url != null) {
            final RequestQueue queue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            mobileApi.result = response;
                            Log.d("pippo", response);

                            ArrayList<Ordine> ordini = mobileApi.parseResponseToOrdersList();
                            if (ordini != null) {
                                FeedAdapterListOrders feedAdapter;
                                feedAdapter = new FeedAdapterListOrders(getContext(), R.layout.layout_lista_ordini);
                                for (int i = 0; i < ordini.size(); i++) {
                                    Ordine temp = ordini.get(i);
                                    //if (temp.note.equals("pippone") && !temp.status.equals("completed")) {
                                        feedAdapter.add(temp);
                                    //}
                                }
                                //feedAdapter.addAll(ordini);
                                setListAdapter(feedAdapter);
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




        return inflater.inflate(R.layout.fragment_orders, container, false);
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Integer i = new Integer(position);
        Ordine post = (Ordine) l.getItemAtPosition(i);
        Toast.makeText(getContext(), post.id ,Toast.LENGTH_LONG).show();
        OrderFragment orderFragment = new OrderFragment();
        Bundle bundle = new Bundle();
        //bundle.putString("product", post.id );
        bundle.putSerializable("order", post);
        orderFragment.setArguments(bundle);
        FragmentManager manager = getActivity().getSupportFragmentManager();
        //manager.beginTransaction().replace(R.id.content_main, orderFragment, orderFragment.getTag()).commit();
        manager.beginTransaction().replace(R.id.content_main, orderFragment, orderFragment.getTag()).addToBackStack(null).commit();
        manager.executePendingTransactions();



    }



}

