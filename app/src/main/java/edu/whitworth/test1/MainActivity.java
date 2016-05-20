package edu.whitworth.test1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void profile(View view){
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void learn(View view){
        Firebase.setAndroidContext(this);
        Firebase myFirebaseRef = new Firebase("https://popping-torch-1994.firebaseio.com/");
        myFirebaseRef.child("message").setValue("Test Main.");

        myFirebaseRef.child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
            }
            @Override
            public void onCancelled(FirebaseError error) {
            }
        });

        Intent intent = new Intent(this, learnPlay.class);
        startActivity(intent);
    }

    public void play(View view){
        Intent intent = new Intent(this, Play.class);
        startActivity(intent);
    }

    // Not being used
    public void view(View view){
        Firebase.setAndroidContext(this);
        Firebase myFirebaseRef = new Firebase("https://popping-torch-1994.firebaseio.com/");

        class Card {
            public int combat;
            public int trade;
            public String type;

            public Card() {
            }

            public Card(int combat, int trade, String type) {
                this.combat = combat;
                this.trade = trade;
                this.type = type;
            }

            public int getCombat() {
                return combat;
            }

            public int getTrade() {
                return trade;
            }

            public String getType() {
                return type;
            }
        }

        Firebase testCards = myFirebaseRef.child("cards");
        Card blob = new Card(1,1,"blob");
        Card fighter = new Card(2,0,"fighter");
        //testCards.push().setValue(blob);
        //testCards.push().setValue(fighter);

    }
}
