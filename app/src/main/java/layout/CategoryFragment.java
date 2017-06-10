package layout;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.andrea.nav.FeedAdapterCategoria;
import com.example.andrea.nav.MobileApi;
import com.example.andrea.nav.R;
import com.example.andrea.nav.RestApi;
import com.example.andrea.nav.UrlTag;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends StandardListFragment {

    TextView fragmentTitle;
    ListView listViewCategory;
    Context context;


    public CategoryFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View view=inflater.inflate(R.layout.fragment_category, container, false);
        // Inflate the layout for this fragment

        fragmentTitle = (TextView) view.findViewById(R.id.text);
        listViewCategory = (ListView)  view.findViewById(R.id.list);
        context = getActivity();

        final RestApi api = new RestApi("ck_70cac7d595c237c071ed2e1b04992c33314b5d35", "cs_5ad2850616cc44b6620f096f3b279fd13e43fb45", "http://littlebonfi.altervista.org/wc-api/v2/");
        String url = api.makeRequest(RestApi.Method.GET,RestApi.Endpoint.CATEGORIE, new ArrayList<UrlTag>(), -1);



        /*
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
        */

    /*
        if(url != null){
            final RequestQueue queue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            api.result = response;
                            Log.d("pippo",response);
                            ArrayList<Categoria> categorie = api.getAllCategories();

                            FeedAdapterCategoria feedAdapter ;
                            feedAdapter = new FeedAdapterCategoria(context, R.layout.layout_lista_categorie);
                            feedAdapter.addAll(categorie);
                            setListAdapter(feedAdapter);
                            queue.stop();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                               // Log.d("pippo",error.getMessage().toString());
                            queue.stop();
                        }
                    });
            queue.add(stringRequest);
        }

*/


        final MobileApi mobileApi = new MobileApi("ck_1370a4d992c642956279385e7824f523b760510b", "cs_d73a50cec9f97ee7c033b1f57ba602b671fa8994", backEndIpAddress + "php/mobile_api.php");
        List<UrlTag> urlTagArrayList = new ArrayList<UrlTag>();
        urlTagArrayList.add(new UrlTag("request", "categorie"));
        String url2 = mobileApi.makeRequest(MobileApi.Endpoint.CATEGORIE,urlTagArrayList);

        if(url2 != null){
            final RequestQueue queue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url2,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("pippo", response);
                            mobileApi.result = response;


                            ArrayList<Categoria> categorie = mobileApi.getAllCategories(backEndIpAddress);


                            FeedAdapterCategoria feedAdapter ;
                            feedAdapter = new FeedAdapterCategoria(context, R.layout.layout_lista_categorie);
                            feedAdapter.addAll(categorie);
                            setListAdapter(feedAdapter);


                            //Log.d("pippo",categorie.get(0).title);
                            queue.stop();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Log.d("pippo",error.getMessage().toString());
                            queue.stop();
                        }
                    });
            queue.add(stringRequest);
        }








        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Integer i = new Integer(position);
        Categoria post = (Categoria) l.getItemAtPosition(i);
        //Toast.makeText(context, post.title ,Toast.LENGTH_LONG).show();
        ProductsFragment productsFragment = new ProductsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("category", post.slug);
        productsFragment.setArguments(bundle);
        FragmentManager manager = getActivity().getSupportFragmentManager();
        //manager.beginTransaction().replace(R.id.content_main, productsFragment, productsFragment.getTag()).commit();
        manager.beginTransaction().replace(R.id.content_main, productsFragment, productsFragment.getTag()).addToBackStack(null).commit();
        manager.executePendingTransactions();



    }

}
