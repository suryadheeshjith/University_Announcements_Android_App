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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

//import static androidx.constraintlayout.Constraints.TAG;


public class ann_delete_fragment extends Fragment {

    private FirebaseFirestore db;
    private Map< String, Object > announce;
    private Date date1;
    private SimpleDateFormat formatter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.bottomNavigationView.getMenu().getItem(1).setChecked(true);
        final View view = inflater.inflate(R.layout.fragment_delete_ann, container, false);



        db = FirebaseFirestore.getInstance();

        Button b = view.findViewById(R.id.delete_ann);
        b.setOnClickListener((View v)-> {//Lambda Expression

            EditText Edittitle = view.findViewById(R.id.titleDelete);
            String title = Edittitle.getText().toString().trim().toUpperCase();
            if(title.isEmpty())
                Edittitle.setError("Field can't be empty");

            else{
                db.collection("Announcements").document(MainActivity.heads).collection(MainActivity.heads+" Announcements").document(title).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot != null && documentSnapshot.exists()) {
                                db.collection("Announcements").document(MainActivity.heads).collection(MainActivity.heads+" Announcements").document(title).delete()
                                        .addOnSuccessListener(new OnSuccessListener< Void >() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getContext(), "Announcement Deleted!",
                                                        Toast.LENGTH_SHORT).show();
                                                db.collection("Announcements").document(MainActivity.heads).collection(MainActivity.heads+" Announcements")
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                           if (task.isSuccessful()) {
                                                                if(task.getResult().isEmpty())
                                                                    db.collection("Announcements").document(MainActivity.heads).delete();
                                                               }

                                                           }

                                                   });
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

                            }else{
                                Toast.makeText(getContext(),"Error!! No such document exists!",Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });



                }

        });


        return view;
    }
}