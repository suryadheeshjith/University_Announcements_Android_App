package com.example.javaprojone;
import android.database.Cursor;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class event_list_fragment extends Fragment {

    ListView listView;
    ArrayAdapter adapter;
    private List_Adapter mAdapter;
    String value;
    private DatabaseHelper_CE sqldb;
    private View view;
    private SwipeRefreshLayout s;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        MainActivity.bottomNavigationView.getMenu().getItem(3).setChecked(true);
        view = inflater.inflate(R.layout.fragment_tv_lv, container, false);
        TextView t = view.findViewById(R.id.textViewGen);
        t.setText("EVENTS");
        s = view.findViewById(R.id.swiper);
        Event_list();
        s.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Event_list();
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
    void Event_list()
    {
        sqldb = DatabaseHelper_CE.getInstance(getContext());


        ArrayList<List_Item> ls = new ArrayList<>();
        Cursor res = sqldb.getAllData("EVENT");


        if(MainActivity.Day!=null) {


            if (res != null && res.moveToFirst()) {
                Log.i("Mess", "Ini here");
                while (true) {
                    String ev_name = res.getString(1);
                    String crd1_name = res.getString(2);
                    String crd1_phone = res.getString(3);
                    String crd2_name = res.getString(4);
                    String crd2_phone = res.getString(5);

                    String id = res.getString(0);
                    String start, end, venue, targetaud, cap = "Unknown Venue";
                    Cursor res2 = sqldb.getAllData("EVENT_SCHEDULE1");
                    if (res2 != null && res2.moveToFirst()) {

                        while (true) {
                            if (id.equals(res2.getString(5))) {
                                start = res2.getString(1);
                                end = res2.getString(2);
                                venue = res2.getString(3);
                                targetaud = res2.getString(4);

                                Cursor res3 = sqldb.getData("EVENT_VENUE_CAPACITY", "EV_VENUE", venue);
                                if (res3 != null && res3.moveToFirst()) {

                                    cap = res3.getString(1);
                                }
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                                Date sD = null, eD = null;
                                try {
                                    sD = sdf.parse(start);
                                    Log.i("Start", "" + start.toString());
                                } catch (Exception e) {
                                    try {
                                        sD = sdf2.parse(start);
                                        Log.i("Start", "" + start.toString());
                                    } catch (Exception e1) {

                                    }
                                }


                                try {
                                    eD = sdf.parse(end);
                                    Log.i("Start", "" + start.toString());
                                } catch (Exception e) {
                                    try {
                                        eD = sdf2.parse(end);
                                        Log.i("Start", "" + start.toString());
                                    } catch (Exception e1) {

                                    }
                                }
                                if (sD.compareTo(MainActivity.Day.getTime()) * MainActivity.Day.getTime().compareTo(eD) >= 0)
                                    ls.add(new List_Item(ev_name,
                                            "Start Date : " + start + "\n\n\nEnd Date : " + end + "\n\n\nVenue : " + venue + "  Capacity : " + cap + "\n\n\nTarget Audience : " + targetaud,
                                            "Coordinator 1 : " + crd1_name + ", " + crd1_phone + "\nCoordinator 2 : " + crd2_name + ", " + crd2_phone));
                            }
                            //event_list.add(ev_name);
                            if (res2.isLast())
                                break;
                            res2.moveToNext();
                        }

                    }
                    res2.close();
                    //event_list.add(ev_name);
                    if (res.isLast())
                        break;
                    res.moveToNext();
                }
            }


            res.close();

        }
        else
        {
            if(res!=null && res.moveToFirst())
            {
                Log.i("Mess","Ini here");
                while(true)
                {
                    String ev_name = res.getString(1);
                    String crd1_name = res.getString(2);
                    String crd1_phone = res.getString(3);
                    String crd2_name = res.getString(4);
                    String crd2_phone = res.getString(5);

                    String id = res.getString(0);
                    String start,end,venue,targetaud,cap="Unknown Venue";
                    Cursor res2 = sqldb.getAllData("EVENT_SCHEDULE1");
                    if(res2!=null && res2.moveToFirst())
                    {

                        while(true)
                        {
                            if(id.equals(res2.getString(5)))
                            {
                                start = res2.getString(1);
                                end= res2.getString(2);
                                venue = res2.getString(3);
                                targetaud = res2.getString(4);

                                Cursor res3 = sqldb.getData("EVENT_VENUE_CAPACITY","EV_VENUE",venue);
                                if(res3!=null && res3.moveToFirst()) {

                                    cap =res3.getString(1);
                                }
                                ls.add(new List_Item(ev_name,
                                        "Start Date : "+start+"\n\n\nEnd Date : "+end+"\n\n\nVenue : "+venue+"  Capacity : "+cap+"\n\n\nTarget Audience : "+targetaud,
                                        "Coordinator 1 : "+crd1_name+", "+crd1_phone+"\nCoordinator 2 : "+crd2_name+", "+crd2_phone));
                            }
                            //event_list.add(ev_name);
                            if(res2.isLast())
                                break;
                            res2.moveToNext();
                        }

                    }
                    res2.close();
                    //event_list.add(ev_name);
                    if(res.isLast())
                        break;
                    res.moveToNext();
                }
            }


            res.close();
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
        if(ls.isEmpty())
        {
            String[] lsempt =  {" No Events yet ! :)) "};
            ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.textincontainer, R.id.container_text,lsempt);

            listView.setAdapter(adapter);
        }
        else {

            mAdapter = new List_Adapter(getContext(), ls);
            listView.setAdapter(mAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    // TODO Auto-generated method stub

                    MainActivity.li = mAdapter.getItem(position);

                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new event_info_fragment()).addToBackStack("Tag").commit();

                }
            });
        }
    }

}