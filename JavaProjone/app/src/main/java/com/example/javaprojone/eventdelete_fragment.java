package com.example.javaprojone;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class eventdelete_fragment extends Fragment {

    private FirebaseFirestore db;
    private DatabaseHelper_CE sqldb;
    private String idl;
    private String club_id;
    private int flag=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.bottomNavigationView.getMenu().getItem(4).setChecked(true);
        final View view = inflater.inflate(R.layout.fragment_delete_ann, container, false);

        TextView t = view.findViewById(R.id.textViewdel);
        t.setText("Delete Event");
        club_id = "";
        idl = "";
        Button b = view.findViewById(R.id.delete_ann);
        b.setOnClickListener((View v)-> {
                    EditText Edittitle = view.findViewById(R.id.titleDelete);
                    String title = Edittitle.getText().toString().trim().toUpperCase();
                    if (title.isEmpty())
                        Edittitle.setError("Field can't be empty");
                    else {
                        String ch;
                        if (MainActivity.type.equals("Club"))
                            ch = "CLUB_EVENT";
                        else
                            ch = "EVENT";


                        db = FirebaseFirestore.getInstance();
                        sqldb = DatabaseHelper_CE.getInstance(getContext());

                        if (ch.equals("CLUB_EVENT")) {

                            Cursor res = sqldb.getAllData("CLUB");

                            if (res != null && res.moveToFirst()) {

                                while (true) {
                                    if (MainActivity.heads.equals(res.getString(1))) {
                                        club_id = res.getString(0);
                                        Log.i("Found", "Foundmeha"+club_id);
                                        break;
                                    }

                                    if (res.isLast())
                                        break;
                                    res.moveToNext();
                                }
                            }
                            res.close();


                            res = sqldb.getAllData(ch);


                            if (res != null && res.moveToFirst()) {
                                Log.i("Mess", "Ini here");
                                while (true) {
                                    if (res.getString(1).equals(title) && res.getString(6).equals(club_id)) {
                                        idl = res.getString(0);
                                        Log.i("Mess", "Deleting club event in sqlite "+idl);
                                        sqldb.deleteClubEvent(res.getString(0));

                                    }
                                    if (res.isLast())
                                        break;
                                    res.moveToNext();
                                }
                            }


                            res.close();

                            sqldb.close();

                            db.collection("Dynamic").document(ch).collection(ch)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.i("IDs",document.getId()+"  "+idl);
                                                    if (document.getString("CL_EV_NAME").equals(title)&&document.getString("ORG_CLUB_ID").equals(club_id)) {

                                                        idl = document.getId();
                                                        flag=1;
                                                        db.collection("Dynamic").document(ch).collection(ch).document(idl).delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(getContext(), "Event Deleted!",
                                                                                Toast.LENGTH_SHORT).show();

                                                                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                                                                new announcements_fragment()).addToBackStack("Tag").commit();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(getContext(), "ERROR!! " + e.toString(),
                                                                                Toast.LENGTH_SHORT).show();
                                                                        Log.d("TAG", e.toString());
                                                                    }
                                                                });
                                                        break;
                                                    }
                                                }
                                                if(flag==0)
                                                    Toast.makeText(getContext(),"Error!! No such document exists!",Toast.LENGTH_SHORT).show();
                                            }


                                        else{
                                            Toast.makeText(getContext(),"Error!! No such document exists!",Toast.LENGTH_SHORT).show();
                                        }

                                    }

                            });

                        }


                        else
                        {
                            Cursor res = sqldb.getAllData(ch);
                            String id ="";
                            if (res != null && res.moveToFirst()) {

                                while (true) {
                                    Log.i("LOL","POSTED BY : "+MainActivity.posted_by+"Res.get "+res.getString(2)+res.getString(4));
                                    if (title.equals(res.getString(1))) {
                                        id = res.getString(0);
                                        Log.i("Mess", "Deleting  event in sqlite "+idl);
                                        sqldb.deleteEvent(res.getString(0));
                                        break;
                                    }

                                    if (res.isLast())
                                        break;
                                    res.moveToNext();
                                }
                            }
                            res.close();
                            sqldb.close();
                            db.collection("Dynamic").document(ch).collection(ch)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.i("IDs",document.getId()+"  "+idl);
                                                    if (document.getString("EV_NAME").equals(title) && document.getString("Head").equals(MainActivity.heads)) {

                                                        idl = document.getId();
                                                        flag=1;
                                                        db.collection("Dynamic").document(ch).collection(ch).document(idl).delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(getContext(), "Event Deleted!",
                                                                                Toast.LENGTH_SHORT).show();

                                                                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                                                                new announcements_fragment()).addToBackStack("Tag").commit();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(getContext(), "ERROR!! " + e.toString(),
                                                                                Toast.LENGTH_SHORT).show();
                                                                        Log.d("TAG", e.toString());
                                                                    }
                                                                });
                                                        break;
                                                    }
                                                }
                                                if(flag==0)
                                                    Toast.makeText(getContext(),"Error!! No such document exists!",Toast.LENGTH_SHORT).show();
                                            }


                                            else{
                                                Toast.makeText(getContext(),"Error!! No such document exists!",Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                    });


                        }




                    }


                });


        return view;
    }

}