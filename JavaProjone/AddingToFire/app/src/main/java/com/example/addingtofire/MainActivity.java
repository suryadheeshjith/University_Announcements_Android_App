package com.example.addingtofire;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.Constraints.TAG;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String type;
    private Map< String, Object > detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = FirebaseFirestore.getInstance();

        detail = new HashMap< >();

        String map[][] = {{"REGISTRAR_ID", "NAME", "DESIGNATION"},
                {"ADM_ID", "ADM_NAME", "PASSWORD", "ADM_DEPARTMENT", "ADM_EMAIL", "ADM_USN", "MNGD_CLUB_ID", "MGR_REGISTRAR_ID"},
                {"CL_ID", "CL_NAME", "CL_DESCRIPTION"},
                {"CL_EV_ID", "CL_EV_NAME", "CL_EV_CRD1_NAME", "CL_EV_CRD1_CONTACT", "CL_EV_CRD2_NAME", "CL_EV_CRD2_CONTACT", "ORG_CLUB_ID", "CL_EV_START_DATE", "CL_EV_END_DATE", "CL_EV_VENUE", "CL_EV_TARGET_AUDIENCE"},
                {"EV_ID", "EV_NAME", "EV_CRD1_NAME", "EV_CRD1_CONTACT", "EV_CRD2_NAME", "EV_CRD2_CONTACT", "MGR_REGISTRAR_ID", "EV_START_DATE", "EV_END_DATE", "EV_VENUE", "EV_TARGET_AUDIENCE","Type"}
        };

        String vals[][] = {
                {"6", "Shashwati", "Physical Director"},
                {"4","Phani","123456","IEM","phanik@gmail.com","1RV17IM100","4","6"},
                {"6", "E-Cell", "Entrepreneurship Development Cell"},
                {"4","Ashwa Exhibition","Harini","892838282","Vidhath","988493922","1","2020-02-19","2020-02-22","IEM Auditorium","Students"},
                {"4","Talk on Politics","Shanav","93339932","Gagan","944939928","1","2019-10-10","2019-10-10","IEM Auditorium","All","General"}
        };


        String table_names[] ={
            "REGISTRAR",    //0
                    "CLUB_ADMIN",   //1
                    "CLUB",         //2
                    "CLUB_EVENT",   //3
                    "EVENT"           //4

        };


        String stdyn = "Dynamic";
        int no =4;



        String table = table_names[no];
        detail.clear();
        for(int i=0;i<map[no].length;i++)
        {

            detail.put(map[no][i],vals[no][i]);

        }



        db.collection(stdyn).document(table).collection(table).document(vals[no][0]).set(detail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplication(), "Announcement Posted!",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplication(), "ERROR!!" + e.toString(),
                                Toast.LENGTH_SHORT).show();
                        Log.d("TAG", e.toString());
                    }
                });

    }
}