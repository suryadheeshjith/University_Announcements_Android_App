package com.example.javaprojone;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

//import static androidx.constraintlayout.Constraints.TAG;


public class Club_add_fragment extends Fragment {


    private ListView listView;
    private ArrayAdapter adapter;
    public static String value2="";
    public static ArrayList<String> value3 = new ArrayList<String>();
    ArrayList<String> store = new ArrayList<String>();
    public static Menu submenu2;
    boolean k;
    DatabaseHelper db;
    private SwipeRefreshLayout s;
    public static boolean club = false;
    private FirebaseFirestore fdb;
    private String type;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.bottomNavigationView.getMenu().getItem(4).setChecked(true);
        view = inflater.inflate(R.layout.fragment_tv_lv, container, false);
        s = view.findViewById(R.id.swiper);
        TextView t = view.findViewById(R.id.textViewGen);
        t.setText("Click to add into Navigation Drawer");


        club_add();
        s.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                club_add();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        s.setRefreshing(false);
                    }
                },2000);
            }
        });

        return view;


    }


    private void club_add()
    {
        db = new DatabaseHelper(getContext());

        fdb = FirebaseFirestore.getInstance();
        ArrayList<String> listItem = new ArrayList<>();

        fdb.collection("Announcements")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                try {
                                    type = document.get("Type").toString();
                                    if (type.equals("Club")) {
                                        Log.i("dfs", "List1 " + listItem);
                                        Log.i("wef", "Document ID : " + document.getId());//list.add(document.ge);
                                        listItem.add(document.getId());
                                        Log.i("dfs", "List2 " + listItem);
                                    }
                                } catch (Exception e) {
                                    //
                                }

                            }

                            for(String s:listItem)   // Check if there in database
                            {
                                if(!db.findData(s)) {
                                    db.insertData(s, "0", "Club");
                                    Log.i("Inserted","Inserted "+s);
                                }
                            }


                            Cursor res =  db.getAllData();

                            if(res!=null && res.moveToFirst()) { // Check if all announcements been removed from a particular club in firestore
                                while (true) {


                                    if (!listItem.contains(res.getString(0)) && res.getString(2).equals("Club")) {
                                        db.deleteData(res.getString(0));
                                        Log.i("Removed","Deleted "+res.getString(0));
                                    }
                                    if (res.isLast())
                                        break;
                                    res.moveToNext();
                                }
                            }
                            res.close();
                            if(MainActivity.start)
                            {

                                res =  db.getAllData();
                                Log.i("3","Start");

                                if(res!=null && res.moveToFirst()) {
                                    while (true) {

                                        Log.i("Res.getString(0) Club: ",res.getString(0));
                                        if (res.getString(1).equals("1") && res.getString(2).equals("Club")) {
                                            value2 = res.getString(0);
                                            addMenuItemInNavMenuDrawer(listItem);
                                            MainActivity.course = true;
                                        }
                                        if(res.isLast())
                                            break;
                                        res.moveToNext();
                                    }


                                }
                                res.close();
                                db.close();


                                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                        new announcements_fragment()).addToBackStack("Tag").commit();

                                Log.i("sjhf", "LAOLS");
                            }



                            listView = view.findViewById(R.id.listView);


                            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                                private boolean scrollEnabled;

                                @Override
                                public void onScrollStateChanged(AbsListView view, int scrollState) {
                                }

                                @Override
                                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                    int topRowVerticalPosition =
                                            (listView == null || listView.getChildCount() == 0) ?
                                                    0 : listView.getChildAt(0).getTop();

                                    boolean newScrollEnabled =
                                            (firstVisibleItem == 0 && topRowVerticalPosition >= 0) ?
                                                    true : false;

                                    if (null != s && scrollEnabled != newScrollEnabled) {
                                        // Start refreshing....
                                        s.setEnabled(newScrollEnabled);
                                        scrollEnabled = newScrollEnabled;
                                    }

                                }
                            });

                            adapter = new ArrayAdapter(getActivity(), R.layout.list_item, R.id.textView_title, listItem);
                            listView.setAdapter(adapter);


                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                    // TODO Auto-generated method stub
                                    value2 = adapter.getItem(position).toString();
                                    k=false;


                                    if(value3.contains(value2))
                                    {
                                        Toast.makeText(getActivity(), "Club already added!", Toast.LENGTH_SHORT).show();

                                    }


                                    else
                                    {

                                        addMenuItemInNavMenuDrawer(listItem);
                                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                                new SuperAddFragment()).addToBackStack("Tag").commit();
                                    }
                                }
                            });

                        }
                    }
                });


    }
        private void addMenuItemInNavMenuDrawer (ArrayList<String> listItem) {
          //


            Menu menu = MainActivity.navigationView.getMenu();
            if(submenu2==null) {
                submenu2 = menu.addSubMenu("Clubs");
                ((SubMenu) submenu2).setIcon(R.drawable.ic_club);

                submenu2.add("Clear All").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                     @Override
                     public boolean onMenuItemClick(MenuItem item) {

                         submenu2.clear();
                         Toast.makeText(getActivity(), "Clubs Removed!", Toast.LENGTH_SHORT).show();
                         value3.clear();
                         submenu2 = null;

                         for(int i=0;i<listItem.size();i++)
                         {
                             db.updateData(listItem.get(i),"0","Club");
                         }
                         return true;
                     }

                });
            }

            db.updateData(value2,"1","Club");
            value3.add(value2);
            if(MainActivity.start==false)
            Toast.makeText(getActivity(), "Club added!", Toast.LENGTH_SHORT).show();
            submenu2.add(value2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {


                    MainActivity.club = item.toString();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new Gen_club_fragment()).addToBackStack("Tag").commit();


                    MainActivity.drawer.closeDrawer(GravityCompat.START);
                    return true;


                }

                });



            MainActivity.navigationView.invalidate();



    }


}