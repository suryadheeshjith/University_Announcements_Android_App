package com.example.javaprojone;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class event_info_fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_info, container, false);

        TextView t = view.findViewById(R.id.textViewHead);
        t.setText(MainActivity.li.getTitl());


        TextView t2 = view.findViewById(R.id.body);
        t2.setText(MainActivity.li.getBody());

        TextView t3 = view.findViewById(R.id.postdate);
        t3.setText(MainActivity.li.getFooter());

//    t3.setLinksClickable(false);
//    t3.setAutoLinkMask(0);
    return view;
    }
}
