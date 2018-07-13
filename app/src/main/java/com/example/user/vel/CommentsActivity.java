package com.example.user.vel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {

    private EditText comment;
    private ImageView comment_post_btn;

    private RecyclerView comments_list;
    private CommentsAdapter commentsAdapter;
    private List<Comments> commentsList;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private String Post_ID;
    private String current_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        current_id = mAuth.getCurrentUser().getUid();
        Post_ID = getIntent().getStringExtra("Post_ID");

        comment = findViewById(R.id.comment_field);
        comment_post_btn =  findViewById(R.id.comment_post_btn);
        comments_list = findViewById(R.id.comment_list);

        commentsList =  new ArrayList<>();
        commentsAdapter = new CommentsAdapter(commentsList);
        comments_list.setHasFixedSize(true);
        comments_list.setLayoutManager(new LinearLayoutManager(this));
        comments_list.setAdapter(commentsAdapter);

        firebaseFirestore.collection("Parts/" + Post_ID + "/Comments")
                .addSnapshotListener(CommentsActivity.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (!documentSnapshots.isEmpty()) {

                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            String commentId = doc.getDocument().getId();
                            Comments comments = doc.getDocument().toObject(Comments.class);
                            commentsList.add(comments);
                            commentsAdapter.notifyDataSetChanged();
                        }
                    }

                }
            }
        });

        comment_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = comment.getText().toString();

                if(!message.isEmpty()) {
                    Map<String, Object> commentMap = new HashMap<>();
                    commentMap.put("message", message);
                    commentMap.put("user_id", current_id);
                    commentMap.put("timestamp", FieldValue.serverTimestamp());

                    firebaseFirestore.collection("Parts/" + Post_ID + "/Comments").add(commentMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(CommentsActivity.this, "Comment Posted", Toast.LENGTH_LONG).show();
                            } else {
                                comment.setText("");
                            }
                        }
                    });
                }
            }
        });
    }

    //If one of the options from the dropdown menu is selected the following will occur
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //Variable to hold id of selected menu option
        int option_id = item.getItemId();
        //If the language option is selected, user will be re-directed to language screen
        if (option_id == R.id.action_Language)
        {
            Intent Language_intent = new Intent(CommentsActivity.this, LanguageSelect.class);
            startActivity(Language_intent);
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
            Intent prof_intent = new Intent(CommentsActivity.this, userProfile.class);
            Toast.makeText(CommentsActivity.this, R.string.in_prof, Toast.LENGTH_LONG).show();
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
