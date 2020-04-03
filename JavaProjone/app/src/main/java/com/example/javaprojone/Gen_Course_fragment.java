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
import java.util.Collections;

//import static androidx.constraintlayout.Constraints.TAG;



public class Gen_Course_fragment extends Fragment {


    private ListView listView;
    private List_Adapter mAdapter;
    private FirebaseFirestore db;
    private View view;
    private SwipeRefreshLayout s;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.bottomNavigationView.getMenu().getItem(0).setChecked(true);
        // MainActivity.bottomNavigationView.setSelectedItemId(R.id.nav_fav);
        view = inflater.inflate(R.layout.fragment_tv_lv, container, false);
        TextView t = view.findViewById(R.id.textViewGen);
        t.setText(MainActivity.department);
        gen_course();
        s = view.findViewById(R.id.swiper);
        s.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                gen_course();
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
    void gen_course()
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


        String doc = MainActivity.department;
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
                                    a.Body = document.get("Body").toString();
                                    a.Date = document.getTimestamp("Date");
                                    a.Posted_By = document.get("Posted_by").toString();
                                    a.Title = document.get("Title").toString();
                                    ls.add(new List_Item(a.Title,a.Body,"Posted By : "+a.Posted_By+"\n"+"Date : "+a.Date.toDate().toString(),a.Date));

                                }
                                catch(Exception e)
                                {
                                    continue;
                                }

                            }
                            if(ls.isEmpty())
                            {
                                String[] lsempt =  {" No Announcements yet ! :)) "};
                                ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.textincontainer, R.id.container_text,lsempt);

                                listView.setAdapter(adapter);
                            }
                            else {
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
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
