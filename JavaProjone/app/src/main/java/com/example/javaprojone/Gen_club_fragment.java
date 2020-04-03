package com.example.javaprojone;

import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

//import static androidx.constraintlayout.Constraints.TAG;


public class Gen_club_fragment extends Fragment {

    private ListView listView;
    ArrayAdapter adapter;


    String value;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.bottomNavigationView.getMenu().getItem(2).setChecked(true);
        // MainActivity.bottomNavigationView.setSelectedItemId(R.id.nav_fav);
        View view = inflater.inflate(R.layout.fragment_tv_tv_lv, container, false);
        TextView t = view.findViewById(R.id.textViewGen);
        t.setText(MainActivity.club);

        TextView tinfo = view.findViewById(R.id.textViewInfo);



        Cursor res = MainActivity.s.sqldb.getAllData("CLUB");
        String desc = "";


        if(res!=null && res.moveToFirst())
        {
            Log.i("Mess","Ini here");
            while(true)
            {
                if(MainActivity.club.equals(res.getString(1))) {
                    desc = res.getString(2);
                    break;
                }
                //event_list.add(ev_name);
                if(res.isLast())
                    break;
                res.moveToNext();
            }
        }


        res.close();

        tinfo.setText(desc);

        String[] listItem = {"Announcements", "Events"};
        listView = view.findViewById(R.id.listView);
        adapter = new ArrayAdapter(getActivity(), R.layout.list_item, R.id.textView_title, listItem);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub


                value = adapter.getItem(position).toString();
                Fragment selectedFragment = null;
                if(value.equals("Announcements"))
                {
                    selectedFragment = new Club_ann_fragment();
                }
                else if(value.equals("Events"))
                {
                    selectedFragment = new Club_event_fragment();
                }

                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).addToBackStack("Tag").commit();
            }
        });

        return view;

    }
}
