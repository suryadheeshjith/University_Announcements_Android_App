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
//

public class course_add_fragment extends Fragment {


    ListView listView;
    ArrayAdapter adapter;
    public static String value="",value1="";
    public static Menu submenu;
    DatabaseHelper db;
    private FirebaseFirestore fdb;
    private String type;
    private View view;
    private SwipeRefreshLayout s;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.bottomNavigationView.getMenu().getItem(4).setChecked(true);
        view = inflater.inflate(R.layout.fragment_tv_lv, container, false);
        TextView t = view.findViewById(R.id.textViewGen);

        t.setText("Click to add into Navigation Drawer");

        course_add();


        s = view.findViewById(R.id.swiper);
        s.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                course_add();
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


    void course_add()
    {
        ArrayList<String> listItem = new ArrayList<>();
        db = new DatabaseHelper(getContext());

        fdb = FirebaseFirestore.getInstance();
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
                                    if (type.equals("Academic")) {
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
                                    db.insertData(s, "0", "Academic");
                                    Log.i("Inserted","Inserted "+s);
                                }
                            }


                            Cursor res =  db.getAllData();

                            if(res!=null && res.moveToFirst()) { // Check if all announcements been removed from a particular course in firestore
                                while (true) {


                                    if (!listItem.contains(res.getString(0)) && res.getString(2).equals("Academic")) {
                                        db.deleteData(res.getString(0));
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
                                Log.i("2","Start");

                                if(res!=null && res.moveToFirst()) {
                                    while (true) {

                                        Log.i("Res.getString(0)Course:",res.getString(0));
                                        if (res.getString(1).equals("1") && res.getString(2).equals("Academic")) {
                                            value = res.getString(0);
                                            Log.i("Course is 1:",res.getString(0));
                                            addMenuItemInNavMenuDrawer(listItem);
                                            MainActivity.course = true;
                                            break;
                                        }
                                        if(res.isLast())
                                            break;
                                        res.moveToNext();
                                    }
                                }
                                res.close();
                                db.close();
                                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                        new Club_add_fragment()).addToBackStack("Tag").commit();

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

                            if(submenu==null)
                            {
                                for(int i=0;i<listItem.size();i++)
                                {
                                    db.updateData(listItem.get(i),"0","Academic");
                                }
                            }

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                    // TODO Auto-generated method stub
                                    value = adapter.getItem(position).toString();
                                    if(MainActivity.course==false) {

                                        //
                                        addMenuItemInNavMenuDrawer(listItem);
                                        MainActivity.course=true;
                                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                                new SuperAddFragment()).addToBackStack("Tag").commit();

                                    }
                                    else{
                                        if((value1.equals(value)||value.equals("")))
                                        {
                                            MainActivity.course=false;
                                            Menu menu = MainActivity.navigationView.getMenu();
                                            Toast.makeText(getActivity(), "Course Removed!", Toast.LENGTH_SHORT).show();
                                            submenu.clear();
                                            submenu = null;
                                            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                                    new SuperAddFragment()).addToBackStack("Tag").commit();
                                        }
                                        else {

                                            Toast.makeText(getActivity(), "Course already added!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });

                        }
                    }
                });
    }
        private void addMenuItemInNavMenuDrawer (ArrayList<String> listItem) {
          //

            if(MainActivity.start)
                Log.i("Im","Im here");
            if(listItem.size()>0)
            for(int i=0;i<listItem.size();i++)
            {
                db.updateData(listItem.get(i),"0","Academic");
            }
            db.updateData(value,"1","Academic");

            Menu menu = MainActivity.navigationView.getMenu();

            if(submenu==null) {
                submenu = menu.addSubMenu("Courses");

                submenu.add("Clear").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        MainActivity.course=false;
                        submenu.clear();
                        Toast.makeText(getActivity(), "Course Removed!", Toast.LENGTH_SHORT).show();
                        value = "";
                        submenu = null;

                        for (int i = 0; i < listItem.size(); i++) {
                            db.updateData(listItem.get(i),"0","Academic");
                        }

                        return true;
                    }

                });

            }
            ((SubMenu) submenu).setIcon(R.drawable.ic_course);
            value1 = value;
            if(MainActivity.start==false)
            Toast.makeText(getActivity(), "Course added!", Toast.LENGTH_SHORT).show();


            submenu.add(value1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {


                        MainActivity.department = item.toString();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                               new Gen_Course_fragment()).addToBackStack("Tag").commit();


                    MainActivity.drawer.closeDrawer(GravityCompat.START);
                    return true;
                }


            });



            MainActivity.navigationView.invalidate();

            //getActivity().onBackPressed();


    }



}