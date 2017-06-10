package com.example.andrea.nav;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Stack;

import layout.CartFragment;
import layout.CategoryFragment;
import layout.HomeFragment;
import layout.OrdersFragment;

/**
 * Created by andrea on 30/03/17.
 */

public class BaseActivity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener {
    public Stack<String> fragmentStack = new Stack<String>();
    public String backEndIpAddress = "http://127.0.0.1/prec/app";

    public String getBackEndIpAddress(){
        return  backEndIpAddress;
    }
    @Override

    public void onBackPressed() {

        Log.d("pippo", "onBackPressed Called");
        getSupportFragmentManager().popBackStack(null, 0);
        /*if (getFragmentManager().getBackStackEntryCount() > 0) {
            Log.d("pippo", new Integer(getFragmentManager().getBackStackEntryCount()).toString());
            getFragmentManager().popBackStack();
        }*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            HomeFragment homeFragment = new HomeFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, homeFragment, "home").addToBackStack(null).commit();
            manager.executePendingTransactions();
            fragmentStack.push("home");
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            CategoryFragment categoryFragment = new CategoryFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, categoryFragment, "category").addToBackStack(null).commit();
            manager.executePendingTransactions();
            fragmentStack.push("categoria");

        } else if (id == R.id.nav_slideshow) {
            OrdersFragment ordersFragment = new OrdersFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, ordersFragment, "pippo").addToBackStack(null).commit();
            manager.executePendingTransactions();
            fragmentStack.push("ordini");
        } else if (id == R.id.nav_manage) {
            CartFragment cartFragment = new CartFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, cartFragment, "ertyui").addToBackStack(null).commit();
            manager.executePendingTransactions();
            fragmentStack.push("carrello");
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
