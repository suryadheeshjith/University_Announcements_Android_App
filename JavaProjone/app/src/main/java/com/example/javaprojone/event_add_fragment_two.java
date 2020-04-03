package com.example.javaprojone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class event_add_fragment_two extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.bottomNavigationView.getMenu().getItem(4).setChecked(true);
        final View view = inflater.inflate(R.layout.fragment_add_event2, container, false);

        EditText EditVen = view.findViewById(R.id.venue);
        EditText Edittarget = view.findViewById(R.id.target);
        EditText EditName = view.findViewById(R.id.name2);
        EditText EditContact2 = view.findViewById(R.id.contact2);
        EditText Editmgr = view.findViewById(R.id.mgr);
        TextInputLayout mgVis = view.findViewById(R.id.mgrID);

        EditVen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHelper_CE query = DatabaseHelper_CE.getInstance(getContext());
                ArrayList<String> venues = new ArrayList<>();
                Cursor res = query.getAllData("EVENT_VENUE_CAPACITY");

                if (res != null && res.moveToFirst()) {
                    Log.i("DropDown", "In here");
                    while (true) {

                        venues.add(res.getString(0));
                        if(res.isLast())
                            break;
                        res.moveToNext();

                    }
                }

                String [] venue_list = new String[venues.size()];

                for(int i=0;i<venues.size();i++)
                {
                    venue_list[i] = venues.get(i);
                }

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                mBuilder.setTitle("Select a Venue");
                mBuilder. setSingleChoiceItems(venue_list, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditVen.setText(venue_list[which]);
                        dialog.dismiss();
                    }

                });

                mBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog ven_dialog = mBuilder.create();
                ven_dialog.show();

            }
        });

        if(!MainActivity.type.equals("Club")) {
            mgVis.setVisibility(View.VISIBLE);

        }

        Button b = view.findViewById(R.id.add_event);
        b.setOnClickListener((View v)-> {
            String title = eventadd_fragment.Edittitle.getText().toString().trim();
            String startDate = eventadd_fragment.edittext1.getText().toString().trim();
            String endDate = eventadd_fragment.edittext2.getText().toString().trim();
            String contact = eventadd_fragment.editContact1.getText().toString().trim();


            String venue = EditVen.getText().toString().trim();
            String target = Edittarget.getText().toString().trim();
            String name2 = EditName.getText().toString().trim();
            String contact2 = EditContact2.getText().toString().trim();
            String mgr= "";
            if(!MainActivity.type.equals("Club")) {
                mgr = Editmgr.getText().toString().trim();

            }
            int flag2=0;
            for(int i=0;i<contact2.length();i++) {
                if (!Character.isDigit(contact2.charAt(i)))
                {
                    flag2=1;
                    break;
                }
            }

                if (target.isEmpty() || name2.isEmpty() || contact2.isEmpty() || venue.isEmpty() || (!(MainActivity.type.equals("Club")) && mgr.isEmpty())|| contact.length()!=10 || flag2==1) {

                    if (target.isEmpty())
                        Edittarget.setError("Field can't be empty");

                    if (name2.isEmpty())
                        EditName.setError("Field can't be empty");

                    if(contact2.isEmpty())
                        EditContact2.setError("Field can't be empty");

                    if(contact2.length()!=10)
                        EditContact2.setError("Enter valid 10 digit number)");

                    if(flag2==1)
                        EditContact2.setError("Enter valid number");


                    if (venue.isEmpty())
                        EditVen.setError("Field can't be empty");


                    if (MainActivity.type.equals("Club") && mgr.isEmpty())
                        Editmgr.setError("Field can't be empty");


                }

                else
                {


                    add_event_toDB s = new add_event_toDB(getContext());

                    boolean check = s.addToDB(title.trim().toUpperCase(),startDate,endDate,contact,venue,target,name2,contact2,mgr);
                    if(!check)
                    {
                        Toast.makeText(getContext(),"Cannot add overlapping events with the same venue!",Toast.LENGTH_SHORT).show();
                    }
                    else
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ChildWriteFragment()).addToBackStack("Tag").commit();

                }




        });
        return view;

    }
}
