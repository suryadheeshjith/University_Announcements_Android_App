package com.example.javaprojone;

import android.util.Log;

import com.google.firebase.Timestamp;

import java.util.Comparator;


class Sort_by_Date implements Comparator<List_Item>
{
    public int compare(List_Item a, List_Item b)
    {

        try {
            Timestamp timestamp1 = a.getDate();
            Timestamp timestamp2 = b.getDate();
            if(timestamp1.getSeconds()>timestamp2.getSeconds())
                return -1;

        } catch(Exception e) { //this generic but you can control another types of exception
            // look the origin of excption

        }

        return 1;
    }
}