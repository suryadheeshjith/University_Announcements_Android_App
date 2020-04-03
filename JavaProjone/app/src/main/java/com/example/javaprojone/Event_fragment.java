package com.example.javaprojone;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarUtils;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class Event_fragment extends Fragment {

    ListView listView;
    ArrayAdapter adapter;
    private LinearLayout eventView;
    String value;
    private DatabaseHelper_CE sqldb;
    private CalendarView calView;
    private int k;
    private Button b;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        MainActivity.bottomNavigationView.getMenu().getItem(3).setChecked(true);
        view = inflater.inflate(R.layout.fragment_tv_cal, container, false);
        TextView t = view.findViewById(R.id.textViewGen);
        t.setText("EVENTS");

        calView = view.findViewById(R.id.calView);
        b = view.findViewById(R.id.vList);

        b.setVisibility(View.INVISIBLE);
        calView.setVisibility(View.INVISIBLE);

        int m = MainActivity.s.populate_all();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(m==1)
                    Event(); // Post delays run after delay.
                b.setVisibility(View.VISIBLE);
                calView.setVisibility(View.VISIBLE);
            }
        },1000);

        SwipeRefreshLayout s = view.findViewById(R.id.swiper);
        s.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int m = MainActivity.s.populate_all();
                b.setVisibility(View.INVISIBLE);
                calView.setVisibility(View.INVISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(m==1)
                            Event();
                        b.setVisibility(View.VISIBLE);
                        calView.setVisibility(View.VISIBLE);
                    }
                },1000);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        s.setRefreshing(false);
                    }
                },3000);



            }
        });



        return view;



    }
    void Event()
    {
        sqldb = DatabaseHelper_CE.getInstance(getContext());


        try {
            calView.setDate(Calendar.getInstance());
        }
        catch(Exception e)
        {
            Log.i("Exception",e.toString());
        }




        ArrayList<Pair<String,String>> dates= new ArrayList<>();
        Cursor res = sqldb.getAllData("EVENT");


        if(res!=null && res.moveToFirst())
        {
            Log.i("Mess","Ini here");
            while(true)
            {

                String id = res.getString(0);
                String start,end;
                Cursor res2 = sqldb.getAllData("EVENT_SCHEDULE1");
                if(res2!=null && res2.moveToFirst())
                {

                    while(true)
                    {
                        if(id.equals(res2.getString(5)))
                        {
                            start = res2.getString(1);
                            end= res2.getString(2);

                            dates.add(Pair.create(start,end));
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

        sqldb.close();

        List<Calendar> l = new ArrayList<>();
        for(Pair<String,String> x:dates)
        {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
            Date start=null,end=null;
            try {
                start = sdf.parse(x.first);
                Log.i("Start",""+start.toString());
            }
            catch(Exception e)
            {
                try {
                    start = sdf2.parse(x.first);
                    Log.i("Start",""+start.toString());
                }
                catch(Exception e1)
                {

                }
            }
            try {
                end = sdf.parse(x.second);
                Log.i("Start",""+start.toString());
            }
            catch(Exception e)
            {
                try {
                    end = sdf2.parse(x.second);
                    Log.i("Start",""+start.toString());
                }
                catch(Exception e1)
                {

                }
            }
            long difference=0;

            difference = (end.getTime() - start.getTime()) / 86400000;


            Log.i("Difference",""+difference);
            Calendar scal = Calendar.getInstance();
            scal.setTimeInMillis(start.getTime());
            l.add(scal);
            Log.i("scal",""+scal.getTime());


            if(difference==0)
            {

                //calView.setHighlightedDays(l);
                calView.setSelectedDates(l);
            }
            else {

                for(int i=1;i<=difference;i++) {
                    Calendar day = Calendar.getInstance();
                    day.setTime(start);
                    day.add(Calendar.DAY_OF_YEAR, i);
                    l.add(day);

                }

                //calView.setHighlightedDays(l);
                calView.setSelectedDates(l);

            }

        }



        calView.setOnDayClickListener(new OnDayClickListener() {

            @Override
            public void onDayClick(EventDay eventDay) {
                k=0;
                for(Pair<String,String> x:dates) {

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                    Date start=null,end=null;
                    try {
                        start = sdf.parse(x.first);
                        Log.i("Start",""+start.toString());
                    }
                    catch(Exception e)
                    {
                        try {
                            start = sdf2.parse(x.first);
                            Log.i("Start",""+start.toString());
                        }
                        catch(Exception e1)
                        {

                        }
                    }
                    try {
                        end = sdf.parse(x.second);
                        Log.i("Start",""+start.toString());
                    }
                    catch(Exception e)
                    {
                        try {
                            end = sdf2.parse(x.second);
                            Log.i("Start",""+start.toString());
                        }
                        catch(Exception e1)
                        {

                        }
                    }
                    Calendar scal = Calendar.getInstance();
                    scal.setTimeInMillis(start.getTime());
                    Calendar ecal = Calendar.getInstance();
                    ecal.setTimeInMillis(end.getTime());


                    if(scal.compareTo(eventDay.getCalendar()) * eventDay.getCalendar().compareTo(ecal)>=0 )
                    {
                        MainActivity.Day = eventDay.getCalendar();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new event_list_fragment()).addToBackStack("Tag").commit();
                        break;
                    }

                    k+=1;
                }
                if(k==dates.size())
                {
                    calView.setSelectedDates(l);
                }

            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.Day = null;
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new event_list_fragment()).addToBackStack("Tag").commit();
            }
        });
    }
}