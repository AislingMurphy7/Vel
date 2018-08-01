package com.example.user.vel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PartList extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private List<PartLogs> part_list;
    private Recycler_Adapter recycler_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_list);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        FloatingActionButton addPostBtn = findViewById(R.id.add_post_btn);
        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PartList.this, NewPartAdd.class);
                startActivity(intent);
            }
        });

        part_list = new ArrayList<>();
        RecyclerView parts_view = findViewById(R.id.parts_list_view);

        recycler_adapter = new Recycler_Adapter(part_list);
        parts_view.setLayoutManager(new LinearLayoutManager(this));
        parts_view.setAdapter(recycler_adapter);


        if(mAuth.getCurrentUser() != null) {
            FirebaseFirestore firebaseFirestore2 = FirebaseFirestore.getInstance();

            Query firstquery = firebaseFirestore2.collection("Parts").orderBy("timestamp", Query.Direction.DESCENDING);
            firstquery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            String PostID = doc.getDocument().getId();
                            PartLogs partLogs = doc.getDocument().toObject(PartLogs.class).withId(PostID);
                            part_list.add(partLogs);

                            recycler_adapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }




    }

    //Function creates the dropdown toolbar menu
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);

    }//End onCreateOptionsMenu()

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser == null)
        {
            sendToLogin();
        } else {

            String current_user = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

            firebaseFirestore.collection("Users").document(current_user).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        if(!task.getResult().exists())
                        {
                            Intent intent = new Intent(PartList.this, userProfile.class);
                            startActivity(intent);
                            finish();
                        }
                    } else
                    {
                        String error = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(PartList.this, "Error: " + error, Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    private void sendToLogin()
    {
        Intent intent =  new Intent(PartList.this, LoginUser.class);
        startActivity(intent);
        finish();
    }

    //If one of the options from the dropdown menu is selected the following will occur
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //Variable to hold id of selected menu option
        int option_id = item.getItemId();
        if (option_id == R.id.action_home)
        {
            Intent home_intent = new Intent(PartList.this, user_options.class);
            startActivity(home_intent);
        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (option_id == R.id.action_help)
        {
            Intent help_intent = new Intent(PartList.this, UserHelp.class);
            startActivity(help_intent);
        }//End if()

        //The user is already located within this screen
        if (option_id == R.id.action_prof)
        {
            Intent prof_intent = new Intent(PartList.this, userProfile.class);
            startActivity(prof_intent);
        }//End if()

        //If the exit option is selected, the app will close
        if (option_id == R.id.action_exit)
        {
            Intent exit_intent = new Intent(Intent.ACTION_MAIN);
            exit_intent.addCategory(Intent.CATEGORY_HOME);
            exit_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(exit_intent);
            finish();
        }//End if()

        return super.onOptionsItemSelected(item);
    }//End onOptionsItemSelected()

}
