package edu.whitworth.test1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.ParseException;

public class Profile extends ActionBarActivity {
    public SQLiteDatabase user;
    public double totalWins;
    public double totalloses;
    public String textSkill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        // Import the users name if it has already been stored in DQLite

        user = openOrCreateDatabase("Users", Context.MODE_PRIVATE, null);
        user.execSQL("CREATE TABLE IF NOT EXISTS userName(Name VARCHAR);");


        TextView nameText = (TextView) findViewById(R.id.textView2);

        Cursor c = user.rawQuery("SELECT * FROM userName", null);

        if(c.getCount()==0){}
        else {

            StringBuffer buffer = new StringBuffer();

            while (c.moveToNext()) {
                buffer.append(c.getString(0));
            }

            nameText.setText(buffer.toString());


        }


        // Import the User's wins if they have been stored
        user = openOrCreateDatabase("Wins", Context.MODE_PRIVATE, null);
        user.execSQL("CREATE TABLE IF NOT EXISTS Wins(Wins VARCHAR);");
        //user.execSQL("UPDATE Wins SET Wins = '" +String.valueOf(1)+ "'");


        TextView winText = (TextView) findViewById(R.id.textView4);

        c = user.rawQuery("SELECT * FROM Wins", null);
        StringBuffer buffer1 = new StringBuffer();

        while (c.moveToNext()) {
            buffer1.append(c.getString(0));
        }

        winText.setText("Wins: " + buffer1.toString());

        // Update variable totalwins for skill calculation
        int j =0;
        try
        {
            int i= NumberFormat.getInstance().parse(buffer1.toString()).intValue();
            j = i;
        }
        catch (ParseException e) {}
        totalWins = j;


        // Import the User's Loses if they have been stored
        user = openOrCreateDatabase("Loss", Context.MODE_PRIVATE, null);
        user.execSQL("CREATE TABLE IF NOT EXISTS Loss(Loss VARCHAR);");
        //user.execSQL("UPDATE Loss SET LOSS = '" +String.valueOf(0)+ "'");


        TextView lossText = (TextView) findViewById(R.id.textView5);

        c = user.rawQuery("SELECT * FROM Loss", null);
        StringBuffer buffer2 = new StringBuffer();

        while (c.moveToNext()) {
            buffer2.append(c.getString(0));
        }

        lossText.setText("Losses: " + buffer2.toString());

        // Update variable totallosses for skill calculation
        j =0;
        try
        {
            int i= NumberFormat.getInstance().parse(buffer2.toString()).intValue();
            j = i;
        }
            catch (ParseException e) {}
        totalloses = j;

        assignSkill();

        user.close();

        /*
        user = openOrCreateDatabase("Users", Context.MODE_PRIVATE, null);
        user.execSQL("CREATE TABLE IF NOT EXISTS user(Name VARCHAR, Wins VARCHAR, Loses VARCHAR);");

        String myName = "Name";
        String myWins = "five";
        String myLoses = "two";
        int Wins = 5;
        int Loses = 2;

        user.execSQL("INSERT INTO user VALUES('" + myName + "','" + Wins + "','" + Loses + "');");
        user.delete("user",null,null);

        Cursor c= user.rawQuery("SELECT * FROM user", null);
        if(c.getCount()==0)
        {
            System.out.println("Error No records found");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext())
        {
            buffer.append("Name: "+c.getString(0)+"\n");
            buffer.append("Wins: "+c.getString(1)+"\n");
            buffer.append("Loses: "+c.getString(2)+"\n\n");
        }
        System.out.println("Student Details" + buffer.toString());

        Loses =1;
        Wins = 1;
        myName = "Joe";
        user.execSQL("UPDATE user SET Wins='"+Wins+"',Loses='"+Loses+"' WHERE Name='"+myName+"'");

        c= user.rawQuery("SELECT * FROM user", null);
        if(c.getCount()==0)
        {
            System.out.println("Error No records found");
            return;
        }

        StringBuffer buffer1 =new StringBuffer();
        while(c.moveToNext())
        {
            buffer1.append("Name: "+c.getString(0)+"\n");
            buffer1.append("Wins: "+c.getString(1)+"\n");
            buffer1.append("Loses: "+c.getString(2)+"\n\n");
        }
        System.out.println("Student Details" + buffer1.toString());
        */
    }



    public void switcher(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



    public void changeName(View view){
        EditText nameIn = (EditText) findViewById(R.id.editText);
        String name = nameIn.getText().toString();

        int Wins = 0;
        int Loses =0;

        user = openOrCreateDatabase("Users", Context.MODE_PRIVATE, null);
        user.execSQL("CREATE TABLE IF NOT EXISTS userName(Name VARCHAR);");

        user.delete("userName", null, null);
        user.execSQL("CREATE TABLE IF NOT EXISTS userName(Name VARCHAR);");

        user.execSQL("INSERT INTO userNAME VALUES('" + name + "')");
        user.execSQL("UPDATE userName SET Name = '" + name + "'");


        TextView nameText = (TextView) findViewById(R.id.textView2);


        Cursor c = user.rawQuery("SELECT * FROM userName", null);
        StringBuffer buffer = new StringBuffer();

        while(c.moveToNext())
        {
            buffer.append(c.getString(0));
        }

        System.out.println(buffer.toString());

        nameText.setText(buffer.toString());

        user.close();

    }

    public void assignSkill(){

        // Avoid dividing by 0
        if(totalloses == 0){totalloses = .1;}

        // Calculate Skill
        double skill = totalWins / totalloses;

        // Assign Skill Value
        if(skill < 0.25){textSkill = "Disgrace";}
        if(skill < 0.75 && skill >=0.25 ){textSkill = "Noob";}
        if(skill >=.75 && skill < 1.25){textSkill = "Average";}
        if(skill >= 1.25 && skill < 2){textSkill = "Campaigner";}
        if(skill >= 2 && skill < 10){textSkill = "Champion";}
        if(skill >= 10){textSkill = "Destroyer of Worlds";}

        TextView skillText = (TextView) findViewById(R.id.textView10);
        skillText.setText("Skill Level: " + textSkill);
    }

}
