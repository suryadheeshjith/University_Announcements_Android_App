package com.example.javaprojone;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class ChildWriteFragment extends Fragment {


    ListView listView;
    ArrayAdapter adapter;
    String value;
    private View view;
    private SwipeRefreshLayout s;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.bottomNavigationView.getMenu().getItem(4).setChecked(true);

        view = inflater.inflate(R.layout.fragment_tv_lv, container, false);
        TextView t = view.findViewById(R.id.textViewGen);
        t.setText("Announcement/Event");
        String[] listItem = {"Announcement","Event"};

        s = view.findViewById(R.id.swiper);
        s.setEnabled(false);

        listView = view.findViewById(R.id.listView);
        adapter = new ArrayAdapter(getActivity(), R.layout.list_item, R.id.textView_title, listItem);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub

                value = adapter.getItem(position).toString();
                Fragment selectedFragment = null;
                if(SuperWriteFragment.add==1) {
                    if (value.equals("Announcement")) {
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ann_add_fragment()).addToBackStack("Tag").commit();
                    } else if (value.equals("Event")) {
                        // selectedFragment = new Event_write_fragment();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new eventadd_fragment()).addToBackStack("Tag").commit();
                    }
                }
                else
                {
                    if (value.equals("Announcement")) {
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ann_delete_fragment()).addToBackStack("Tag").commit();
                    } else if (value.equals("Event")) {
                        // selectedFragment = new Event_write_fragment();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new eventdelete_fragment()).addToBackStack("Tag").commit();
                    }
                }

            }
        });

        return view;


    }

}