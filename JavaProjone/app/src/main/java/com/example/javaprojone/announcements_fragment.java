package com.example.javaprojone;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;




public class announcements_fragment extends Fragment {

    private ListView listView;
    private List_Adapter mAdapter;
    private FirebaseFirestore db;
    private String type;
    private View view;
    private SwipeRefreshLayout s;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if(MainActivity.start) {

            MainActivity.start=false;
            Log.i("4","Start");
            MainActivity.l.setVisibility(View.VISIBLE);
            Log.i("Value of stack", "Value of stack" + getFragmentManager().getBackStackEntryCount());
            MainActivity.s = new SQLite_populator(getContext());
            MainActivity.s.populate_all();

        }

        MainActivity.bottomNavigationView.getMenu().getItem(1).setChecked(true);
        view = inflater.inflate(R.layout.fragment_tv_lv, container, false);
        TextView t = view.findViewById(R.id.textViewGen);
        t.setText("ANNOUNCEMENTS");

        announcements();
        s = view.findViewById(R.id.swiper);
        s.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                announcements();
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
    void announcements()
    {
        db = FirebaseFirestore.getInstance();

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

        ArrayList<List_Item> ls = new ArrayList<>();


        ArrayList<String> list = new ArrayList<>();//= {"General"};//,"Rangataranga","Momentum","8mile","E-Summit"}; // GCP apart from general
        list.add("General");

        db.collection("Announcements")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                try {
                                    type = document.get("Type").toString();
                                    if (type.equals("Event")) {
                                        Log.i("dfs","List1 "+list);
                                        Log.i("wef", "Document ID : " + document.getId());//list.add(document.ge);
                                        list.add(document.getId());
                                        Log.i("dfs","List2 "+list);
                                    }
                                }
                                catch(Exception e)
                                {
                                    //
                                }

                            }


                            Log.i("dfs","List4 "+list);

                            for(String doc: list) {

                                db.collection("Announcements").document(doc).collection(doc + " Announcements")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Log.d("TAG", document.getId() + " => " + document.getData());

                                                        gen_ann a = new gen_ann();
                                                        try {
                                                            Log.i("Happy", "l");
                                                            a.Body = document.get("Body").toString();
                                                            a.Date = document.getTimestamp("Date");
                                                            a.Posted_By = document.get("Posted_by").toString();
                                                            a.Title = document.get("Title").toString();
                                                            Log.i("Happy", a.Posted_By);
                                                            ls.add(new List_Item(a.Title, a.Body, "Posted By : " + a.Posted_By + "\n" + "Date : " + a.Date.toDate().toString(), a.Date));
                                                        } catch (Exception e) {
                                                            continue;
                                                        }


                                                    }


                                                    Collections.sort(ls, new Sort_by_Date());
                                                    mAdapter = new List_Adapter(getContext(), ls);

                                                    listView.setAdapter(mAdapter);

                                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                                            // TODO Auto-generated method stub

                                                            MainActivity.li = mAdapter.getItem(position);
                                                            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                                                    new ann_info_fragment()).addToBackStack("Tag").commit();

                                                        }
                                                    });


                                                } else {
                                                    Log.w("TAG", "Error getting documents.", task.getException());
                                                }
                                            }
                                        });
                            }


                        }
                    }
                });

    }


}
