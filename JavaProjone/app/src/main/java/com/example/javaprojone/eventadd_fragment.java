package com.example.javaprojone;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;


public class eventadd_fragment extends Fragment {
    public static EditText Edittitle;
    public static EditText edittext1;
    public static EditText edittext2;
    public static EditText editContact1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.bottomNavigationView.getMenu().getItem(4).setChecked(true);
        final View view = inflater.inflate(R.layout.fragment_add_event, container, false);


        //Toast.makeText(getContext(), "Events.......", Toast.LENGTH_SHORT).show();
        final Calendar myCalendar = Calendar.getInstance();

        Edittitle = view.findViewById(R.id.title);
         edittext1=  view.findViewById(R.id.startdate);
         edittext2=  view.findViewById(R.id.enddate);
         editContact1 = view.findViewById(R.id.contact);


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                edittext1.setText(sdf.format(myCalendar.getTime()));

            }

        };
        DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                edittext2.setText(sdf.format(myCalendar.getTime()));

            }

        };

        edittext1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        edittext2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        Button b = view.findViewById(R.id.add_event);
        b.setOnClickListener((View v)-> {
            String title = Edittitle.getText().toString().trim();

            String startDate = edittext1.getText().toString().trim();
            String endDate = edittext2.getText().toString().trim();
            String contact = editContact1.getText().toString().trim();

            DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");

            Date d = null;
            try {

                d = new Date();
                d = sourceFormat.parse(sourceFormat.format(d));

            }
            catch(Exception e)
            {
                Log.i("InIH",e.toString());
            }

            Date startD=new Date(),endD=new Date();


            int flag =0,flag2=0;
            try{
                startD = sourceFormat.parse(startDate.trim());
            }
            catch(Exception e)
            {
                try{
                    startD = sourceFormat.parse(sourceFormat.format(d));
                }
                catch(Exception e1)
                {
                    //
                }
                flag=1;
                Log.i("Start Date","Start Date"+startDate);
            }
            try {
                endD = sourceFormat.parse(endDate.trim());
            }
            catch(Exception e)
            {
                try{
                    endD = sourceFormat.parse(sourceFormat.format(endD));
                }
                catch(Exception e1)
                {
                    //
                }
                flag+=2;
            }

            for(int i=0;i<contact.length();i++) {
                if (!Character.isDigit(contact.charAt(i)))
                {
                    flag2=1;
                    break;
                }
            }



            Log.i("Start d",""+startD.compareTo(d));

            if(title.isEmpty() || startDate.isEmpty() || endDate.isEmpty() ||contact.isEmpty() || contact.length()!=10 || flag2==1)
            {
                if(title.isEmpty())
                    Edittitle.setError("Field can't be empty");


                if(startDate.isEmpty())
                    edittext1.setError("Field can't be empty");

                else
                    edittext1.setError(null);

                if(endDate.isEmpty())
                    edittext2.setError("Field can't be empty");

                else
                    edittext2.setError(null);

                if(contact.isEmpty())
                    editContact1.setError("Field can't be empty");

                else if(contact.length()!=10 || flag2==1)
                    editContact1.setError("Enter valid 10 digit number)");


            }
            else if(flag>0 || startD.compareTo(d)<0 || startD.compareTo(endD)>0)
            {

               if(flag==1) {
                   edittext1.setError(null);
                   edittext2.setError(null);
                    edittext1.setError("Invalid Date entered1");

                }

                else if(flag==2) {
                   edittext1.setError(null);
                   edittext2.setError(null);
                    edittext2.setError("Invalid Date entered2");
                }

                else if(flag==3){
                    edittext1.setError("Invalid Date entered3");
                    edittext2.setError("Invalid Date entered3");
                }


                else {


                    Log.i("Start date compare",""+startD.compareTo(d));
                   Log.i("Start d",""+startD);
                   Log.i("Start d",""+d);
                   if (startD.compareTo(d) < 0) {
                       edittext1.setError(null);
                       edittext2.setError(null);
                       edittext1.setError("Invalid Date entered4");
                   }
                   else {
                       edittext1.setError(null);
                       edittext2.setError(null);


                       if (startD.compareTo(endD) > 0) {
                           edittext1.setError("Invalid Date entered5");
                           edittext2.setError("Invalid Date entered5");
                       } else {
                           edittext1.setError(null);
                           edittext2.setError(null);
                       }

                   }
               }

            }



            else {
                //
                //GCPP
                edittext1.setError(null);
                edittext2.setError(null);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new event_add_fragment_two()).addToBackStack("Tag").commit();
            }


        });






        return view;
    }

}