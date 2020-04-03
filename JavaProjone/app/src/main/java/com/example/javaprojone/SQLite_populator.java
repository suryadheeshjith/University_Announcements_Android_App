package com.example.javaprojone;

import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

//import static androidx.constraintlayout.Constraints.TAG;

public class SQLite_populator {

    private FirebaseFirestore db;
    private String type;
    public static DatabaseHelper_CE sqldb;
    private Context context;


    SQLite_populator(Context context)
    {

        this.context = context;
        sqldb = DatabaseHelper_CE.getInstance(context);
    }
    int populate_all()
    {
        populate_events();
        populate_club_events();
        populate_clubs();
        populate_club_admins();
        populate_registrar();
        populate_Venue_capacity();
        return 1;

    }



    void populate_events()
    {

        db = FirebaseFirestore.getInstance();
        Log.i("DB",db.toString());


        sqldb.deleteAllData("EVENT");
        sqldb.deleteAllData("EVENT_SCHEDULE1");
        sqldb.deleteAllData("EVENT_DURATION");

        db.collection("Dynamic").document("EVENT").collection("EVENT")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                try {

                                    String vals[] = new String[10];
//                                    {"EV_NAME","EV_CRD1_NAME","EV_CRD1_CONTACT","EV_CRD2_NAME","EV_CRD2_CONTACT","MGR_REGISTRAR_ID"},
//                                    {"EV_START_DATE","EV_END_DATE","EV_VENUE","EV_TARGET_AUDIENCE","EV_ID"},


                                    vals[0] = document.get("EV_NAME").toString();
                                    vals[1] = document.get("EV_CRD1_NAME").toString();
                                    vals[2] = document.get("EV_CRD1_CONTACT").toString();
                                    vals[3] = document.get("EV_CRD2_NAME").toString();
                                    vals[4] = document.get("EV_CRD2_CONTACT").toString();
                                    vals[5] = document.get("MGR_REGISTRAR_ID").toString();


                                    vals[6] = document.get("EV_START_DATE").toString();
                                    vals[7] = document.get("EV_END_DATE").toString();


                                    vals[8] = document.get("EV_VENUE").toString();
                                    vals[9] = document.get("EV_TARGET_AUDIENCE").toString();

                                    String event_row[] = new String[6];

                                    for(int i=0;i<6;i++)
                                    {
                                        event_row[i]=vals[i];
                                    }

                                    try {
                                        sqldb.insertData(event_row, 7);
                                    }
                                    catch(Exception e) {
                                    }

                                    Log.i("1","Table 1");
                                    Cursor res = sqldb.getData("EVENT","EV_NAME",vals[0]);
                                    Log.i("1","In here 1");
                                    String ev_id="-1";

                                    if(res!=null && res.moveToFirst())
                                    {
                                        while(true)
                                        {
                                            ev_id = res.getString(0);
                                            if(res.isLast())
                                                break;
                                            res.moveToNext();
                                        }
                                    }

                                    res.close();
                                    Log.i("1","In here 2");
                                    String event_schedule_row[] = new String[5];


                                    for(int i=0;i<4;i++)
                                    {
                                        event_schedule_row[i]=vals[i+6];
                                    }

                                    Log.i("1","In here 3");
                                    event_schedule_row[4]=ev_id;

                                    try {
                                        sqldb.insertData(event_schedule_row, 8);
                                    }
                                    catch(Exception e)
                                    {

                                    }

                                    Log.i("2","Table 2");

                                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(vals[6]);
                                    Date date2=new SimpleDateFormat("yyyy-MM-dd").parse(vals[7]);
                                    Long diff = date2.getTime() - date1.getTime();
                                    diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                                    diff+=1;
                                    String dur = diff.toString();

                                    String event_duration_row[] = new String[3];

                                    event_duration_row[0] = vals[6];
                                    event_duration_row[1] = vals[7];
                                    event_duration_row[2] = dur;


                                    try {
                                        sqldb.insertData(event_duration_row, 10);
                                    }
                                    catch(Exception e)
                                    {

                                    }

                                    Log.i("3","Table 3");


                                } catch (Exception e) {
                                    //
                                    Log.i("bdj",e.toString());

                                }


                            }

                        }
                    }
                });

    }


    void populate_club_events()
    {

        db = FirebaseFirestore.getInstance();
        Log.i("DB",db.toString());


        sqldb.deleteAllData("CLUB_EVENT");
        sqldb.deleteAllData("CLUB_EVENT_SCHEDULE1");
        sqldb.deleteAllData("CLUB_EVENT_DURATION");

        db.collection("Dynamic").document("CLUB_EVENT").collection("CLUB_EVENT")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG PopClubEvents", document.getId() + " => " + document.getData());
                                try {

                                    String vals[] = new String[10];

                                    //{"CL_EV_NAME","CL_EV_CRD1_NAME","CL_EV_CRD1_CONTACT","CL_EV_CRD2_NAME","CL_EV_CRD2_CONTACT","ORG_CLUB_ID"},
                                    //{"CL_EV_START_DATE","CL_EV_END_DATE","CL_EV_VENUE","CL_EV_TARGET_AUDIENCE","CL_EV_ID"},

                                    vals[0] = document.get("CL_EV_NAME").toString();
                                    vals[1] = document.get("CL_EV_CRD1_NAME").toString();
                                    vals[2] = document.get("CL_EV_CRD1_CONTACT").toString();
                                    vals[3] = document.get("CL_EV_CRD2_NAME").toString();
                                    vals[4] = document.get("CL_EV_CRD2_CONTACT").toString();
                                    vals[5] = document.get("ORG_CLUB_ID").toString();

                                    vals[6] = document.get("CL_EV_START_DATE").toString();
                                    vals[7] = document.get("CL_EV_END_DATE").toString();


                                    vals[8] = document.get("CL_EV_VENUE").toString();
                                    vals[9] = document.get("CL_EV_TARGET_AUDIENCE").toString();

                                    String cl_event_row[] = new String[6];

                                    for(int i=0;i<6;i++)
                                    {
                                        cl_event_row[i]=vals[i];
                                    }

                                    try {
                                        sqldb.insertData(cl_event_row, 3);
                                    }
                                    catch(Exception e) {
                                    }

                                    Log.i("1","Table 1 Club");
                                    Cursor res = sqldb.getData("CLUB_EVENT","CL_EV_NAME",vals[0]);
                                    Log.i("1","In here 1 Club");
                                    String ev_id="-1";

                                    if(res!=null && res.moveToFirst())
                                    {
                                        while(true)
                                        {
                                            ev_id = res.getString(0);
                                            if(res.isLast())
                                                break;
                                            res.moveToNext();
                                        }
                                    }

                                    res.close();
                                    Log.i("1","In here 2 Club Events");
                                    String cl_event_schedule_row[] = new String[5];


                                    for(int i=0;i<4;i++)
                                    {
                                        cl_event_schedule_row[i]=vals[i+6];
                                    }

                                    Log.i("1","In here 3 Club Events");
                                    cl_event_schedule_row[4]=ev_id;

                                    try {
                                        sqldb.insertData(cl_event_schedule_row, 4);
                                    }
                                    catch(Exception e)
                                    {

                                    }

                                    Log.i("2","Table 2 Club Events");

                                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(vals[6]);
                                    Date date2=new SimpleDateFormat("yyyy-MM-dd").parse(vals[7]);
                                    Long diff = date2.getTime() - date1.getTime();
                                    diff = TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS);
                                    diff+=1;
                                    String dur = diff.toString();

                                    String cl_event_duration_row[] = new String[3];

                                    cl_event_duration_row[0] = vals[6];
                                    cl_event_duration_row[1] = vals[7];
                                    cl_event_duration_row[2] = dur;


                                    try {
                                        sqldb.insertData(cl_event_duration_row, 6);
                                    }
                                    catch(Exception e)
                                    {

                                    }

                                    Log.i("3","Table 3 Club Events");


                                } catch (Exception e) {
                                    //
                                    Log.i("bdj",e.toString());

                                }


                            }

                        }
                    }
                });

    }

    void populate_clubs()
    {
        db = FirebaseFirestore.getInstance();
        Log.i("DB",db.toString());

        if(db==null) {
            Log.i("Clubs Not Getting","Deleted");
            return;
        }
        sqldb.deleteAllData("CLUB");


        db.collection("Static").document("CLUB").collection("CLUB")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                try {

                                    String vals[] = new String[3];

                                    //{"CL_ID", "CL_NAME", "CL_DESCRIPTION"},


                                    vals[0] = document.get("CL_ID").toString();
                                    vals[1] = document.get("CL_NAME").toString();
                                    vals[2] = document.get("CL_DESCRIPTION").toString();

                                    String club_row[] = new String[3];

                                    for(int i=0;i<3;i++)
                                    {
                                        club_row[i]=vals[i];
                                    }

                                    try {
                                        sqldb.insertData(club_row, 2);
                                    }
                                    catch(Exception e) {
                                    }



                                } catch (Exception e) {
                                    //
                                    Log.i("bdj",e.toString());

                                }


                            }

                        }
                    }
                });
        sqldb.close();
    }

    void populate_club_admins()
    {
        db = FirebaseFirestore.getInstance();
        Log.i("DB",db.toString());

        if(db==null) {
            Log.i("Club admins not Getting","Deleted");
            return;
        }
        sqldb.deleteAllData("CLUB_ADMIN");


        db.collection("Static").document("CLUB_ADMIN").collection("CLUB_ADMIN")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                try {

                                    String vals[] = new String[8];

                                    Log.i("LOLVAC","EVAC");
//{"ADM_ID", "ADM_NAME", "PASSWORD", "ADM_DEPARTMENT", "ADM_EMAIL", "ADM_USN", "MNGD_CLUB_ID", "MGR_REGISTRAR_ID"},


                                    vals[0] = document.get("ADM_ID").toString();
                                    vals[1] = document.get("ADM_NAME").toString();
                                    vals[2] = document.get("PASSWORD").toString();
                                    vals[3] = document.get("ADM_DEPARTMENT").toString();
                                    vals[4] = document.get("ADM_EMAIL").toString();
                                    vals[5] = document.get("ADM_USN").toString();
                                    vals[6] = document.get("MNGD_CLUB_ID").toString();
                                    vals[7] = document.get("MGR_REGISTRAR_ID").toString();

                                    String club_adm_row[] = new String[8];

                                    for(int i=0;i<8;i++)
                                    {
                                        club_adm_row[i]=vals[i];
                                    }

                                    try {
                                        sqldb.insertData(club_adm_row, 1);
                                    }
                                    catch(Exception e) {
                                    }



                                } catch (Exception e) {
                                    //
                                    Log.i("bdj",e.toString());

                                }


                            }

                        }
                    }
                });
        sqldb.close();
    }

    void populate_registrar()
    {
        db = FirebaseFirestore.getInstance();
        Log.i("DB",db.toString());

        if(db==null) {
            Log.i("Registrars Not Getting","Deleted");
            return;
        }
        sqldb.deleteAllData("REGISTRAR");


        db.collection("Static").document("REGISTRAR").collection("REGISTRAR")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                try {

                                    String vals[] = new String[3];

                                    // {"REGISTRAR_ID", "NAME", "DESIGNATION"}


                                    vals[0] = document.get("REGISTRAR_ID").toString();
                                    vals[1] = document.get("NAME").toString();
                                    vals[2] = document.get("DESIGNATION").toString();

                                    String registrar_row[] = new String[3];

                                    for(int i=0;i<3;i++)
                                    {
                                        registrar_row[i]=vals[i];
                                    }

                                    try {
                                        sqldb.insertData(registrar_row, 0);
                                    }
                                    catch(Exception e) {
                                    }



                                } catch (Exception e) {
                                    //
                                    Log.i("bdj",e.toString());

                                }


                            }

                        }
                    }
                });
        sqldb.close();
    }


    void populate_Venue_capacity()
    {
        db = FirebaseFirestore.getInstance();
        Log.i("DB",db.toString());

        if(db==null) {
            Log.i("Ven_cap not getting","Deleted");
            return;
        }
        sqldb.deleteAllData("EVENT_VENUE_CAPACITY");
        sqldb.deleteAllData("CLUB_EVENT_VENUE_CAPACITY");


        db.collection("Static").document("Venue_capacity").collection("Venue_capacity")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                try {

                                    String vals[] = new String[2];

                                    // {"EV_VENUE","EV_SEATING_CAPACITY"}
                                    // {"CL_EV_VENUE","CL_EV_SEATING_CAPACITY"}


                                    vals[0] = document.get("Venue").toString();
                                    vals[1] = document.get("Capacity").toString();


                                    String venue_cap_row[] = new String[2];

                                    for(int i=0;i<2;i++)
                                    {
                                        venue_cap_row[i]=vals[i];
                                    }

                                    try {
                                        sqldb.insertData(venue_cap_row, 5);
                                        sqldb.insertData(venue_cap_row, 9);
                                    }
                                    catch(Exception e) {
                                    }



                                } catch (Exception e) {
                                    //
                                    Log.i("bdj",e.toString());

                                }


                            }

                        }
                    }
                });
        sqldb.close();

    }







}