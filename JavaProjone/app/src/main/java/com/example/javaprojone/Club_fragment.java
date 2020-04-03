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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

//import static androidx.constraintlayout.Constraints.TAG;


public class Club_fragment extends Fragment {

    ListView listView;
    ArrayAdapter adapter;
    String value;
    String type;
    private FirebaseFirestore fdb;
    private View view;
    private SwipeRefreshLayout s;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        MainActivity.bottomNavigationView.getMenu().getItem(2).setChecked(true);
        view = inflater.inflate(R.layout.fragment_tv_lv, container, false);
        TextView t = view.findViewById(R.id.textViewGen);
        t.setText("CLUBS");

        s = view.findViewById(R.id.swiper);
        club_();
        s.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                club_();
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
    void club_()
    {
        ArrayList<String> listItem = new ArrayList<>();

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
                                    if (type.equals("Club")) {
                                        listItem.add(document.getId());

                                    }
                                } catch (Exception e) {
                                    //
                                }

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

                                    value = adapter.getItem(position).toString();
                                    MainActivity.club = value;

                                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                            new Gen_club_fragment()).addToBackStack("Tag").commit();
                                }
                            });


                        }
                    }
                });

    }

}