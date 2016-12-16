package com.issat.alaeddine.firebasechat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatRoom extends AppCompatActivity {
    private Button buttonSendMessage;
    private TextView textViewCHatConversation;
    private EditText editTextMessage;
    private  String userName,roomName;
    private DatabaseReference root;
    private  String tempKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
           buttonSendMessage=(Button)findViewById(R.id.buttonSendMessage);
           textViewCHatConversation=(TextView)findViewById(R.id.textViewChatConersation);
           editTextMessage=(EditText)findViewById(R.id.editTextMessage);
           userName=getIntent().getExtras().get("userName").toString();
           roomName=getIntent().getExtras().get("roomName").toString();
            setTitle("Room - "+roomName);


        //root of our  Room
        root= FirebaseDatabase.getInstance().getReference().child(roomName);

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<String, Object>();
                tempKey = root.push().getKey();
                root.updateChildren(map);
                DatabaseReference rootMessage=root.child(tempKey);
                Map<String,Object> map2=new HashMap<String,Object>();
                map2.put("name",userName);
                map2.put("msg",editTextMessage.getText().toString().trim());
                rootMessage.updateChildren(map2);
                editTextMessage.setText("");
            }
        });
         root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                appendChatConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                appendChatConversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private String chat_msg,chat_user_name;

    private void appendChatConversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){

            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();

            textViewCHatConversation.append(chat_user_name +" : "+chat_msg +" \n");
        }


    }
}
