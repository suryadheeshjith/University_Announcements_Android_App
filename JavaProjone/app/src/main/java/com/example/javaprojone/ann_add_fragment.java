package com.example.javaprojone;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import static androidx.constraintlayout.Constraints.TAG;


public class ann_add_fragment extends Fragment {

    private FirebaseFirestore db;
    private Map< String, Object > announce;
    private Map< String, Object > detail;
    private SimpleDateFormat formatter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.bottomNavigationView.getMenu().getItem(1).setChecked(true);
        final View view = inflater.inflate(R.layout.fragment_add_ann, container, false);


        final gen_ann a = new gen_ann();
        db = FirebaseFirestore.getInstance();
        announce = new HashMap< >();
        detail = new HashMap< >();

        formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Button b = view.findViewById(R.id.add_ann);
        b.setOnClickListener((View v)-> {//Lambda Expression

                EditText title = view.findViewById(R.id.title);
                EditText body = view.findViewById(R.id.body);



                a.Body = body.getText().toString().trim();
                a.Title = title.getText().toString().trim();



            if(a.Body.isEmpty() || a.Title.isEmpty())
            {
                if(a.Body.isEmpty())
                    body.setError("Field can't be empty");

                if(a.Title.isEmpty())
                    title.setError("Field can't be empty");
            }

            else
                {

                String ann = a.Title;

                announce.put("Body", a.Body);
                announce.put("Title", a.Title);

                if(MainActivity.posted_by.equals("Admin"))
                announce.put("Posted_by", MainActivity.posted_by);
                else
                announce.put("Posted_by", MainActivity.posted_by + " - " + MainActivity.heads);
                announce.put("Date",new Date(System.currentTimeMillis()));
                Log.i("Heads",MainActivity.heads);

                db.collection("Announcements").document(MainActivity.heads).collection(MainActivity.heads+" Announcements").document(a.Title.trim().toUpperCase()).set(announce)
                        .addOnSuccessListener(new OnSuccessListener< Void >() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Announcement Posted!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "ERROR!!" + e.toString(),
                                        Toast.LENGTH_SHORT).show();
                                Log.d("TAG", e.toString());
                            }
                        });

                detail.put("Type",MainActivity.type);
                db.collection("Announcements").document(MainActivity.heads).set(detail)
                        .addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //
                    }
                });

                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new announcements_fragment()).addToBackStack("Tag").commit();
            }

        });

        return view;
    }
}