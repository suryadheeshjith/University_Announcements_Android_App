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


public class SuperAddFragment extends Fragment {


    ListView listView;
    ArrayAdapter adapter;
    String value;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        MainActivity.bottomNavigationView.getMenu().getItem(4).setChecked(true);
        View view = inflater.inflate(R.layout.fragment_tv_lv, container, false);
        TextView t = view.findViewById(R.id.textViewGen);
        t.setText("Section to Add");

        SwipeRefreshLayout s = view.findViewById(R.id.swiper);
        s.setEnabled(false);

        String[] listItem = {"Courses", "Clubs"};
        listView = view.findViewById(R.id.listView);
        adapter = new ArrayAdapter(getActivity(), R.layout.list_item, R.id.textView_title, listItem);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub

                value = adapter.getItem(position).toString();
                Fragment selectedFragment = null;
                if(value.equals("Courses"))
                {
                    selectedFragment = new course_add_fragment();
                }
                else if(value.equals("Clubs"))
                {
                    selectedFragment = new Club_add_fragment();
                }

                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).addToBackStack("Tag").commit();
            }
        });

        return view;


    }

}