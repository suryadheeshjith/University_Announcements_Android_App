package com.example.javaprojone;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class SuperWriteFragment extends Fragment {


    ListView listView;
    ArrayAdapter adapter;
    String value;
    public static int add=1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (Login_fragment.logged_in == false) {
            //
            return inflater.inflate(R.layout.fragment_tv_lv, container, false);
        } else {
            MainActivity.bottomNavigationView.getMenu().getItem(4).setChecked(true);
            MainActivity.inSWrite = true;
            View view = inflater.inflate(R.layout.fragment_tv_lv, container, false);
            TextView t = view.findViewById(R.id.textViewGen);
            t.setText("Add/Delete to Database");

            SwipeRefreshLayout s = view.findViewById(R.id.swiper);
            s.setEnabled(false);


            MainActivity.navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            MainActivity.navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    Login_fragment.logged_in = false;
                    Log.i("Before", "" + R.id.nav_login);
                    MainActivity.navigationView.getMenu().findItem(R.id.nav_login).setTitle("Login");
                    Log.i("After", "" + R.id.nav_login);
                    MainActivity.navigationView.getMenu().findItem(R.id.writeondb).setVisible(false);
                    MainActivity.navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
                    MainActivity.navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
                    MainActivity.inSWrite = false;
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new announcements_fragment()).addToBackStack("Tag").commit();
                    Toast.makeText(getContext(), "Logged Out!", Toast.LENGTH_SHORT).show();
                    MainActivity.drawer.closeDrawer(GravityCompat.START);

                    return true;
                }
            });
            MainActivity.navigationView.getMenu().findItem(R.id.writeondb).setVisible(true);
            MainActivity.navigationView.getMenu().findItem(R.id.writeondb).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    MainActivity.inSWrite = false;
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new SuperWriteFragment()).addToBackStack("Tag").commit();
                    MainActivity.drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
            });
            String[] listItem = {"Add", "Delete"};
            listView = view.findViewById(R.id.listView);
            adapter = new ArrayAdapter(getActivity(), R.layout.list_item, R.id.textView_title, listItem);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    // TODO Auto-generated method stub

                    value = adapter.getItem(position).toString();

                    if (value.equals("Add")) {
                        add = 1;
                    } else if (value.equals("Delete")) {
                        add = 0;
                        // selectedFragment = new Event_write_fragment();
                    }
                    MainActivity.inSWrite = false;
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ChildWriteFragment()).addToBackStack("Tag").commit();

                }
            });


            return view;


        }


    }
}