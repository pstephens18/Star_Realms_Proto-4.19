package edu.whitworth.test1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class VsfriendActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vsfriend);
    }

    public void profile(View view){
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void play(){
        Intent intent = new Intent(this, Play.class);
        startActivity(intent);
    }

}
