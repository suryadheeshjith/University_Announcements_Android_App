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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;



public class Login_fragment extends Fragment {

    private FirebaseFirestore db;
    public static boolean logged_in=false;
    private ArrayList<login_c> c;
    private EditText n;
    private EditText p;
    private String name;
    private String password;
    private String key;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.bottomNavigationView.getMenu().getItem(4).setChecked(true);
        final View view = inflater.inflate(R.layout.fragment_login,container,false);
        key = "1323132312";

        n = (EditText) view.findViewById(R.id.editText);
        p = (EditText) view.findViewById(R.id.editTextPass);
        db = FirebaseFirestore.getInstance();
        c = new ArrayList<>();
        LinearLayout ll = view.findViewById(R.id.login_layout);



        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());

                                login_c a = new login_c();
                                a.name = document.get("Name").toString();
                                a.password = document.get("Password").toString();
                                a.password = decrypt(a.password);

                                a.head = document.get("Head").toString();
                                a.type = document.get("Type").toString();
                                c.add(a);

                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });



        Button b = view.findViewById(R.id.button);
        b.setOnClickListener((View v)-> {//Lamda Expression

            int flag = 0;
            name = n.getText().toString().trim();
            password = p.getText().toString().trim();
            if(name.isEmpty() || password.isEmpty()) {
                if (name.isEmpty())
                    n.setError("Field can't be empty");
                if (password.isEmpty())
                    p.setError("Field can't be empty");
            }

            else {
                for (login_c a : c) {
                    if (a.name.equals(name) && a.password.equals(password)) {
                        Log.i("B", "in here");
                        logged_in = true;
                        flag = 1;
                        MainActivity.heads = a.head;
                        Log.i("B", MainActivity.heads);
                        MainActivity.type = a.type;
                        MainActivity.posted_by = a.name;
                        MainActivity.navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
                        Toast.makeText(getContext(), "Welcome Back " + name + "!", Toast.LENGTH_SHORT).show();
                        p.setText("");
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new SuperWriteFragment()).addToBackStack("Tag").commit();

                    }
                }

                if (flag == 0) {
                    Toast.makeText(getContext(), "Wrong Username or password", Toast.LENGTH_SHORT).show();
                }
            }
        Log.i("A",name);
        Log.i("A",password);


        });


        return view;
    }

    String decrypt(String encrypted)
    {
        String decrypted="";
        for(int i=0;i<encrypted.length();i++)
        {
            int x =(int)encrypted.charAt(i)-'0';
            int y = (int)key.charAt(i)-'0';
            x = x-y;
            decrypted+= Integer.toString(x);
        }
        Log.i("Decrypted",decrypted);
        return decrypted;
    }


}
