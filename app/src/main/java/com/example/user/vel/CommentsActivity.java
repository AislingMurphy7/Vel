package com.example.user.vel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/*
    This class is used for the comments within the posts.
    Here users can type comments and post them to the post
    they wish. these comments are them saved in FireBase
    along with the username and image of the user who
    posted the comment
 */

public class CommentsActivity extends AppCompatActivity
{
    //XML variable
    private EditText comment;

    //Adapter and List variables
    private CommentsAdapter commentsAdapter;
    private List<Comments> commentsList;

    //FireBase variables
    private FirebaseFirestore firebaseFirestore;

    //Hold the post ID na the Current user ID
    private String Post_ID;
    private String current_id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_comments);

        //Instantiate FireBase variables
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Retrieving current ID from FireBase
        current_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        Log.d("CURRENT ID FOR COMMENTS", current_id);

        Post_ID = getIntent().getStringExtra("Post_ID");
        Log.d("POST ID FOR COMMENTS: ", Post_ID);

        //XML Variables
        comment = findViewById(R.id.comment_field);
        ImageView comment_post_btn = findViewById(R.id.comment_post_btn);
        RecyclerView comments_list = findViewById(R.id.comment_list);

        //ArrayList for Comments
        commentsList =  new ArrayList<>();
        commentsAdapter = new CommentsAdapter(commentsList);

        //Setting Fixed size, Layout and Adapter
        comments_list.setHasFixedSize(true);
        comments_list.setLayoutManager(new LinearLayoutManager(this));
        comments_list.setAdapter(commentsAdapter);

        //The posts will be displayed in ascending order depending on the timestamp attached
        Query firstquery = firebaseFirestore.collection("Parts/" + Post_ID + "/Comments").orderBy("timestamp", Query.Direction.ASCENDING);

        //Creates/accesses the collection for storing comments on FireBase
        firstquery
                .addSnapshotListener(CommentsActivity.this, new EventListener<QuerySnapshot>()
                {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e)
                    {
                        //If not empty
                        if (!documentSnapshots.isEmpty())
                        {
                            //If there is changes made to the document
                            for (DocumentChange doc : documentSnapshots.getDocumentChanges())
                            {
                                //If the data is added
                                if (doc.getType() == DocumentChange.Type.ADDED)
                                {
                                    //Document is gathered and passed to Comments class
                                    Comments comments = doc.getDocument().toObject(Comments.class);
                                    //Adds the comment to the commentList
                                    commentsList.add(comments);

                                    //Changes when data is changed
                                    commentsAdapter.notifyDataSetChanged();
                                }//End if()
                            }//End for()
                        }//End if()
                    }//End onEvent()
                });//End addSnapshotListener

        //When the user taps on the 'post comment' button
        comment_post_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //The comment is read into message variable
                String message = comment.getText().toString();

                //If the message is not empty
                if(!message.isEmpty())
                {
                    //Create a Map with key: String, Value: String
                    Map<String, Object> commentMap = new HashMap<>();
                    //Data is stored inside the HashMap()
                    commentMap.put("message", message);
                    commentMap.put("user_id", current_id);
                    commentMap.put("timestamp", FieldValue.serverTimestamp());

                    //Accesses the collection on FireBase
                    firebaseFirestore.collection("Parts/" + Post_ID + "/Comments").add(commentMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>()
                    {
                        @Override
                        //When the process is complete
                        public void onComplete(@NonNull Task<DocumentReference> task)
                        {
                            //If successful
                            if (!task.isSuccessful())
                            {
                                //Success message is displayed to the user
                                Toast.makeText(CommentsActivity.this, "Comment Posted", Toast.LENGTH_LONG).show();
                            } //End if()
                            //Else the comment is set to blank
                            else {
                                comment.setText("");
                            }//End else()
                        }//End onComplete()
                    });//End OnCompleteListener()
                }//End if()
            }//End onClick()
        });//End setOnClickListener()
    }//End onCreate()

    //Function creates the dropdown toolbar menu
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }//End onCreateOptionsMenu()

    //If one of the options from the dropdown menu is selected the following will occur
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //Variable to hold id of selected menu option
        int option_id = item.getItemId();
        if (option_id == R.id.action_home)
        {
            Intent home_intent = new Intent(CommentsActivity.this, Homepage.class);
            startActivity(home_intent);
        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (option_id == R.id.action_help)
        {
            Intent help_intent = new Intent(CommentsActivity.this, UserHelp.class);
            startActivity(help_intent);
        }//End if()

        //The user is already located within this screen
        if (option_id == R.id.action_prof)
        {
            Intent prof_intent = new Intent(CommentsActivity.this, UserProfile.class);
            startActivity(prof_intent);
        }//End if()

        //If the exit option is selected, the app will close
        if (option_id == R.id.action_exit)
        {
            finishAffinity();
        }//End if()

        return super.onOptionsItemSelected(item);
    }//End onOptionsItemSelected()
}//End CommentsActivity()
