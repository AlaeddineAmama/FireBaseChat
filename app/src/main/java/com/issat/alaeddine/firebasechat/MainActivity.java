package com.issat.alaeddine.firebasechat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private Button buttonAddRoom;
    private EditText editTextRoomName ;


    private ListView listViewRoomsNames ;
    private ArrayAdapter<String> arrayAdapter ;
    private ArrayList<String>listOfRooms = new ArrayList<>();
    private  String userName;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonAddRoom =(Button) findViewById(R.id.buttonAddRoom);
        editTextRoomName=(EditText)findViewById(R.id.editTextRoomName);
        listViewRoomsNames=(ListView) findViewById(R.id.listViewRoomsNames);
        arrayAdapter=new  ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,listOfRooms);
        listViewRoomsNames.setAdapter(arrayAdapter);
       requestUserName();
        buttonAddRoom.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Map<String,Object> map= new HashMap<String, Object>();
        map.put(editTextRoomName.getText().toString().trim(),"");
        root.updateChildren(map);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                   set.add(((DataSnapshot) iterator.next()).getKey());
                }
                listOfRooms.clear();
                listOfRooms.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
listViewRoomsNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent =new Intent(getApplicationContext(),ChatRoom.class);
        intent.putExtra("roomName",((TextView)view).getText().toString());
        intent.putExtra("userName",userName);
        startActivity(intent);
    }
});

    }
});

    }

    private void requestUserName() {
        final AlertDialog.Builder builder= new  AlertDialog.Builder(this);
        builder.setTitle("Enter Name ");

        final EditText editText = new EditText(this );
        builder.setView(editText);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userName = editText.getText().toString().trim();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                requestUserName();
            }
        });
        builder.show();
    }
}
