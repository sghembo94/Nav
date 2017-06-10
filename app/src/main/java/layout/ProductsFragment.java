package layout;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.andrea.nav.Categoria;
import com.example.andrea.nav.FeedAdapterListProduct;


import com.example.andrea.nav.MobileApi;
import com.example.andrea.nav.Prodotto;
import com.example.andrea.nav.R;
import com.example.andrea.nav.RestApi;
import com.example.andrea.nav.UrlTag;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends StandardListFragment {

    TextView fragmentTitle;
    ListView listViewCategory;
    Context context;
    String category;


    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        category = args.getString("category");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View view=inflater.inflate(R.layout.fragment_products, container, false);
        // Inflate the layout for this fragment

        //fragmentTitle = (TextView) view.findViewById(R.id.text);
        //fragmentTitle.setText(category);

        context = getActivity();

        /*
        final RestApi api = new RestApi("ck_70cac7d595c237c071ed2e1b04992c33314b5d35", "cs_5ad2850616cc44b6620f096f3b279fd13e43fb45", "http://littlebonfi.altervista.org/wc-api/v2/");
        List<UrlTag> urlTagList = new ArrayList<UrlTag>();

        urlTagList.add(new UrlTag("filter[category]", category));
        urlTagList.add(new UrlTag("filter[limit]", "100"));

        String url = api.makeRequest(RestApi.Method.GET, RestApi.Endpoint.PRODOTTI, urlTagList, -1);
        if(url != null) {

            final RequestQueue queue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            api.result = response;
                            ArrayList<Prodotto> prodotti = api.parseResponseToProductsList();

                            FeedAdapterListProduct feedAdapter;
                            feedAdapter = new FeedAdapterListProduct(context, R.layout.layout_lista_prodotti);
                            feedAdapter.addAll(prodotti);
                            setListAdapter(feedAdapter);


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
        List<UrlTag> urlTagList = new ArrayList<UrlTag>();

        urlTagList.add(new UrlTag("request", "prodotti"));
        urlTagList.add(new UrlTag("categoria", category));
        String url = mobileApi.makeRequest(MobileApi.Endpoint.PRODOTTI,urlTagList);
        if(url != null) {

            final RequestQueue queue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            mobileApi.result = response;

                            ArrayList<Prodotto> prodotti = mobileApi.parseResponseToProductsList(backEndIpAddress);

                            FeedAdapterListProduct feedAdapter;
                            feedAdapter = new FeedAdapterListProduct(context, R.layout.layout_lista_prodotti);
                            feedAdapter.addAll(prodotti);
                            setListAdapter(feedAdapter);



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




        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Integer i = new Integer(position);
        Prodotto post = (Prodotto) l.getItemAtPosition(i);
        //Toast.makeText(context, post.title ,Toast.LENGTH_LONG).show();
        ProductFragment productFragment = new ProductFragment();
        Bundle bundle = new Bundle();
        //bundle.putString("product", post.id );
        bundle.putSerializable("product", post);
        productFragment.setArguments(bundle);
        FragmentManager manager = getActivity().getSupportFragmentManager();
        //manager.beginTransaction().replace(R.id.content_main, productFragment, productFragment.getTag()).commit();
        manager.beginTransaction().replace(R.id.content_main, productFragment, productFragment.getTag()).addToBackStack(null).commit();
        manager.executePendingTransactions();



    }

}