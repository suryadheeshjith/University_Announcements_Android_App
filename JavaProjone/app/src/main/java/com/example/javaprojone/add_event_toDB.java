package com.example.javaprojone;

import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class add_event_toDB {

    private Context context;
    private String title;
    private String body;
    private String startDate;
    private String endDate;
    private String contact;
    private String venue;
    private String target;
    private String name2;
    private String contact2;
    private String mgr;

    private int flag=0;
    private DatabaseHelper_CE sqldb;
    private FirebaseFirestore fdb;


    private Map< String, Object > event;


    add_event_toDB(Context ctxt)
    {
        context=ctxt;
    }

    boolean addToDB(String title,String startDate,String endDate,String contact,String venue,String target,String name2,String contact2,String mgr)
    {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contact = contact;
        this.venue = venue;
        this.target = target;
        this.name2 = name2;
        this.contact2 = contact2;
        this.mgr = mgr;

        event = new HashMap<>();


        //{"CL_EV_NAME","CL_EV_CRD1_NAME","CL_EV_CRD1_CONTACT","CL_EV_CRD2_NAME","CL_EV_CRD2_CONTACT","ORG_CLUB_ID"}
        //{"CL_EV_START_DATE","CL_EV_END_DATE","CL_EV_VENUE","CL_EV_TARGET_AUDIENCE","CL_EV_ID"},

        sqldb = DatabaseHelper_CE.getInstance(context);



        String event_row[] = new String[6];

        event_row[0] = title;
        event_row[1] = MainActivity.posted_by;
        event_row[2] = contact;
        event_row[3] = name2;
        event_row[4] = contact2;



        if(MainActivity.type.equals("Club"))
        {


            //
            flag = 0;
            Cursor check = sqldb.getData("CLUB_EVENT_SCHEDULE1", "CL_EV_VENUE", venue);
            if (check != null && check.moveToFirst()) {
                while (true) {
                    String checkStart = check.getString(1);
                    String checkEnd = check.getString(2);

                    Log.i("Checking for Exception","Check from sql"+checkStart+" "+checkEnd);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                    Date cstart1=null,cend1=null,cstart2=null,cend2=null;
                    try {
                        cstart1 = sdf.parse(checkStart);
                    }
                    catch(Exception e)
                    {
                        try{
                            cstart1 = sdf2.parse(checkStart);
                        }
                        catch(Exception e2)
                        { Log.i("Double except","Except1"+checkStart.toString());}
                    }

                    try {
                        cend1 = sdf.parse(checkEnd);
                    }
                    catch(Exception e)
                    {
                        try{
                            cend1 = sdf2.parse(checkEnd);
                        }
                        catch(Exception e2)
                        { Log.i("Double except","Except2");}
                    }
                    try {
                        cstart2 = sdf.parse(startDate);
                    }
                    catch(Exception e)
                    {
                        try{
                            cstart2 = sdf2.parse(startDate);
                        }
                        catch(Exception e2)
                        { Log.i("Double except","Except3");}
                    }
                    try {
                        cend2 = sdf.parse(endDate);
                    }
                    catch(Exception e)
                    {
                        try{
                            cend2 = sdf2.parse(endDate);
                        }
                        catch(Exception e2)
                        { Log.i("Double except","Except4");}
                    }



                    if (!(cend1.compareTo(cstart2) < 0) && !(cstart1.compareTo(cend2) > 0)) {
                        sqldb.close();
                        return false;
                    }

                    if (check.isLast())
                        break;
                    check.moveToNext();
                }
            }
            check.close();
            //

            if (flag != 1) {
                try {

                    String org_id = "-1";

                    Cursor res = sqldb.getData("CLUB", "CL_NAME", MainActivity.heads);
                    if (res != null && res.moveToFirst()) {
                        while (true) {
                            org_id = res.getString(0);
                            if (res.isLast())
                                break;
                            res.moveToNext();
                        }
                    }

                    res.close();

                    event_row[5] = org_id;

                    try {
                        sqldb.insertData(event_row, 3);
                    } catch (Exception e) {
                        Log.i("ADD_CLUB_EVENT", e.toString());
                    }


                    Cursor res1 = sqldb.getData("CLUB_EVENT", "CL_EV_NAME", title);
                    Log.i("1", "Club Event Schedule add 1");
                    String cl_ev_id = "-1";

                    if (res1 != null && res1.moveToFirst()) {
                        while (true) {
                            cl_ev_id = res1.getString(0);
                            if (res1.isLast())
                                break;
                            res1.moveToNext();
                        }
                    }

                    res1.close();
                    Log.i("1", "Club Event Schedule add 2");
                    String cl_event_schedule_row[] = new String[5];

                    cl_event_schedule_row[0] = startDate;
                    cl_event_schedule_row[1] = endDate;
                    cl_event_schedule_row[2] = venue;
                    cl_event_schedule_row[3] = target;
                    cl_event_schedule_row[4] = cl_ev_id;


                    try {
                        sqldb.insertData(cl_event_schedule_row, 4);
                    } catch (Exception e) {
                        Log.i("ADD_CLUB_EVENT_SCHEDULE", e.toString());
                    }

                    Log.i("1", "Club Event Duration add 1");
                    Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                    Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                    Long diff = date2.getTime() - date1.getTime();
                    diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    diff += 1;
                    String dur = diff.toString();

                    Log.i("1", "Club Event Duration add 2");

                    DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                    startDate = targetFormat.format(date1);
                    endDate = targetFormat.format(date2);

                    Log.i("1", "Club Event Duration add 3");

                    String cl_event_duration_row[] = new String[3];

                    cl_event_duration_row[0] = startDate;
                    cl_event_duration_row[1] = endDate;
                    cl_event_duration_row[2] = dur;


                    try {
                        sqldb.insertData(cl_event_duration_row, 6);
                    } catch (Exception e) {
                        Log.i("ADD_CLUB_EVENT_DURATION", e.toString());

                    }

                    Log.i("1", "Club Event Duration add 4");


                    event.put("CL_EV_NAME", title);
                    event.put("CL_EV_CRD1_NAME", MainActivity.posted_by);
                    event.put("CL_EV_CRD1_CONTACT", contact);
                    event.put("CL_EV_CRD2_NAME", name2);
                    event.put("CL_EV_CRD2_CONTACT", contact2);
                    event.put("ORG_CLUB_ID", org_id);
                    event.put("CL_EV_START_DATE", startDate);
                    event.put("CL_EV_END_DATE", endDate);
                    event.put("CL_EV_VENUE", venue);
                    event.put("CL_EV_TARGET_AUDIENCE", target);
                    event.put("CL_EV_ID", cl_ev_id);
                    event.put("Head", MainActivity.heads);

                    Log.i("1", "Club Event Firestore add 1");

                    fdb = FirebaseFirestore.getInstance();
                    Log.i("1", "Club Event Firestore add 2");
                    fdb.collection("Dynamic").document("CLUB_EVENT").collection("CLUB_EVENT").document(cl_ev_id).set(event)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "Event added",
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "ERROR ADDING EVENT!!" + e.toString(),
                                            Toast.LENGTH_SHORT).show();
                                    Log.d("TAG", e.toString());
                                }
                            });
                    Log.i("1", "Club Event Firestore add 3");


                } catch (Exception e) {

                }
                sqldb.close();
                return true;
            }
        }
        else {

            //
            flag = 0;
            Cursor check = sqldb.getData("EVENT_SCHEDULE1", "EV_VENUE", venue);
            if (check != null && check.moveToFirst()) {
                while (true) {
                    String checkStart = check.getString(1);
                    String checkEnd = check.getString(2);

                    Log.i("Checking for Exception","Check from sql"+checkStart+" "+checkEnd);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                    Date cstart1=null,cend1=null,cstart2=null,cend2=null;
                    try {
                        cstart1 = sdf.parse(checkStart);
                    }
                    catch(Exception e)
                    {
                        try{
                            cstart1 = sdf2.parse(checkStart);
                        }
                        catch(Exception e2)
                        { Log.i("Double except","Except1"+checkStart.toString());}
                    }

                    try {
                        cend1 = sdf.parse(checkEnd);
                    }
                    catch(Exception e)
                    {
                        try{
                            cend1 = sdf2.parse(checkEnd);
                        }
                        catch(Exception e2)
                        { Log.i("Double except","Except2");}
                    }
                    try {
                        cstart2 = sdf.parse(startDate);
                    }
                    catch(Exception e)
                    {
                        try{
                            cstart2 = sdf2.parse(startDate);
                        }
                        catch(Exception e2)
                        { Log.i("Double except","Except3");}
                    }
                    try {
                        cend2 = sdf.parse(endDate);
                    }
                    catch(Exception e)
                    {
                        try{
                            cend2 = sdf2.parse(endDate);
                        }
                        catch(Exception e2)
                        { Log.i("Double except","Except4");}
                    }



                    if (!(cend1.compareTo(cstart2) < 0) && !(cstart1.compareTo(cend2) > 0)) {
                        sqldb.close();
                        return false;
                    }

                    if (check.isLast())
                        break;
                    check.moveToNext();
                }
            }
            check.close();
            //

            if (flag != 1) {
                try {

                    event_row[5] = mgr;

                    try {
                        sqldb.insertData(event_row, 7);
                    } catch (Exception e) {
                        Log.i("ADD_EVENT", e.toString());

                    }

                    Cursor res = sqldb.getData("EVENT", "EV_NAME", title);
                    Log.i("1", " Event Schedule add 1");
                    String ev_id = "-1";

                    if (res != null && res.moveToFirst()) {
                        while (true) {
                            ev_id = res.getString(0);
                            if (res.isLast())
                                break;
                            res.moveToNext();
                        }
                    }

                    res.close();

                    String event_schedule_row[] = new String[5];

                    event_schedule_row[0] = startDate;
                    event_schedule_row[1] = endDate;
                    event_schedule_row[2] = venue;
                    event_schedule_row[3] = target;
                    event_schedule_row[4] = ev_id;

                    try {
                        sqldb.insertData(event_schedule_row, 8);
                    } catch (Exception e) {
                        Log.i("ADD_EVENT_SCHEDULE", e.toString());
                    }

                    Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                    Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                    Long diff = date2.getTime() - date1.getTime();
                    diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    diff += 1;
                    String dur = diff.toString();


                    DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                    startDate = targetFormat.format(date1);
                    endDate = targetFormat.format(date2);

                    String event_duration_row[] = new String[3];

                    event_duration_row[0] = startDate;
                    event_duration_row[1] = endDate;
                    event_duration_row[2] = dur;

                    try {
                        sqldb.insertData(event_duration_row, 10);
                    } catch (Exception e) {
                        Log.i("ADD_EVENT_DURATION", e.toString());

                    }


                    event.put("EV_NAME", title);
                    event.put("EV_CRD1_NAME", MainActivity.posted_by);
                    event.put("EV_CRD1_CONTACT", contact);
                    event.put("EV_CRD2_NAME", name2);
                    event.put("EV_CRD2_CONTACT", contact2);
                    event.put("MGR_REGISTRAR_ID", mgr);
                    event.put("EV_START_DATE", startDate);
                    event.put("EV_END_DATE", endDate);
                    event.put("EV_VENUE", venue);
                    event.put("EV_TARGET_AUDIENCE", target);
                    event.put("EV_ID", ev_id);
                    event.put("Head", MainActivity.heads);

                    fdb = FirebaseFirestore.getInstance();

                    fdb.collection("Dynamic").document("EVENT").collection("EVENT").document(ev_id).set(event)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "Event added",
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "ERROR ADDING EVENT!!" + e.toString(),
                                            Toast.LENGTH_SHORT).show();
                                    Log.d("TAG", e.toString());
                                }
                            });


                } catch (Exception e) {

                }
                sqldb.close();
                return true;
            }
        }
        sqldb.close();
        return true;
    }
}
