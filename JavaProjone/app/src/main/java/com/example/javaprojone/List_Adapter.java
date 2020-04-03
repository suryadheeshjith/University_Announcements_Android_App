package com.example.javaprojone;


import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class List_Adapter extends ArrayAdapter<List_Item> {

    private Context mContext;
    private List<List_Item> ls = new ArrayList<>();

    public List_Adapter(@NonNull Context context, @LayoutRes ArrayList<List_Item> list) {
        super(context, 0 , list);
        mContext = context;
        ls = list;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);

        List_Item current = ls.get(position);


        TextView Title = (TextView) listItem.findViewById(R.id.textView_title);
        Title.setText(current.getTitl());


        TextView Footer = (TextView) listItem.findViewById(R.id.textView_footer);
        Footer.setText(current.getFooter());

        return listItem;
    }

    @Override
    public List_Item getItem(int Position){
        return ls.get(Position);
    }


}

