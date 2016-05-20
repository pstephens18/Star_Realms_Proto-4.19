package edu.whitworth.test1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Random;
import java.util.Vector;

public class Play extends ActionBarActivity
{
    public SQLiteDatabase user;
    public int opponentAuthority;
    public int[] hand = new int[5];
    public int[] trade = new int[6];
    public long[] stats = new long[3];  // Not being utilized yet
    public int[] useablePoints = new int[3]; // Holds current trade, combat, and authority in that order
    public Vector<Card> myCards = new Vector<>();
    boolean canBuyCard;
    boolean won = false;
    boolean lost = false;

    class Card extends Play
    {
        public int image;
        public View view;
        public int combat;
        public int trade;
        public int authority;
        public String name;
        public String owner;
        public int cost;
        public int background;
        Card(){};
    }

    Card[] deck = new Card[11];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);
        initViews();
        initCards();
        opponentAuthority = 200;

        // Set Long Click Listeners to show stats of Cards in Trade Row
        findViewById(R.id.imageView5).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showStats(findViewById(R.id.imageView5));
                return false;
            }
        });
        findViewById(R.id.imageView6).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showStats(findViewById(R.id.imageView6));
                return false;
            }
        });
        findViewById(R.id.imageView7).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showStats(findViewById(R.id.imageView7));
                return false;
            }
        });
        findViewById(R.id.imageView8).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showStats(findViewById(R.id.imageView8));
                return false;
            }
        });
        findViewById(R.id.imageView9).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showStats(findViewById(R.id.imageView9));
                return false;
            }
        });
        findViewById(R.id.imageView10).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showStats(findViewById(R.id.imageView10));
                return false;
            }
        });
    }


    public void changeCard(View view) {
        // Access images
        Resources res = getResources();
        TypedArray imgs = res.obtainTypedArray(R.array.imgs);

        View[] cards = new View[11];
        // Create array of card Views
        for (int i = 0; i < 5; i++) {
            cards[i] = findViewById(hand[i]);
        }


        // Randomize the array
        Random rand = new Random();
        /*
        View[] temp = new View[1];
        int int1;
        int int2;
        for (int i = 0; i < 20; i++) {
            int1 = rand.nextInt(5);
            int2 = rand.nextInt(5);
            temp[0] = cards[int1];
            cards[int1] = cards[int2];
            cards[int2] = temp[0];
        }
        */

        Card temp = new Card();
        int int1;
        int int2;
        for(int i=0; i < 30;i++)
        {
            int1 = rand.nextInt(myCards.size());
            int2 = rand.nextInt(myCards.size());
            temp = myCards.get(int1);
            myCards.setElementAt(myCards.get(int2),int1);
            myCards.setElementAt(temp,int2);
        }


        // Set Backgrounds
        int j;
        int t = rand.nextInt(100)+25;
        for (int i = 0; i < 5; i++) {
            j = t%myCards.size();
            cards[i].setBackgroundResource(imgs.getResourceId(myCards.get(j).background, 0));
            t++;
        }

    }

    public void quit(View view) {
        // Quiting counts as a loss
        if(won == false && lost == false){lose();}

        /*
        Firebase.setAndroidContext(this);
        Firebase myFirebaseRef = new Firebase("https://popping-torch-1994.firebaseio.com/");
        myFirebaseRef.child("message").setValue("Play Message.");

        myFirebaseRef.child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });
        */

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void win()
    {
        // Tell the User they Won
        TextView winning = (TextView) findViewById(R.id.textView3);
        winning.setVisibility(View.VISIBLE);

        // Update the number of Wins
        user = openOrCreateDatabase("Wins", Context.MODE_PRIVATE, null);
        user.execSQL("CREATE TABLE IF NOT EXISTS Wins(Wins VARCHAR);");

        Cursor c = user.rawQuery("SELECT * FROM Wins", null);
        StringBuffer buffer2 = new StringBuffer();

        while (c.moveToNext()) {
            buffer2.append(c.getString(0));
        }


        int j =0;
        try
        {
            int i= NumberFormat.getInstance().parse(buffer2.toString()).intValue();
            i++;
            j = i;
        }
        catch (ParseException e) {}

        user.execSQL("UPDATE Wins SET Wins = '" +j+ "'");

        user.close();

    }

    public void lose()
    {
        // Tell the User that they lost
        TextView lossing = (TextView) findViewById(R.id.textView9);
        lossing.setVisibility(View.VISIBLE);


        // Update the number of loses
        user = openOrCreateDatabase("Loss", Context.MODE_PRIVATE, null);

        Cursor c = user.rawQuery("SELECT * FROM Loss", null);
        StringBuffer buffer2 = new StringBuffer();

        while (c.moveToNext()) {
            buffer2.append(c.getString(0));
        }

        int j =0;
        try
        {
            int i= NumberFormat.getInstance().parse(buffer2.toString()).intValue();
            i++;
            j = i;
        }
        catch (ParseException e) {}

        user.execSQL("UPDATE Loss SET Loss = '" + j + "'");

        user.close();
    }

    public void initViews() {

        TextView oA = (TextView) findViewById(R.id.textView14);
        oA.setText("Opponent Authority: 200");

        // Image view IDs for cards in hand

        this.hand[0] = R.id.imageView11;
        this.hand[1] = R.id.imageView12;
        this.hand[2] = R.id.imageView13;
        this.hand[3] = R.id.imageView14;
        this.hand[4] = R.id.imageView15;

        // Image view IDs for cards in trade row

        this.trade[0] = R.id.imageView5;
        this.trade[1] = R.id.imageView6;
        this.trade[2] = R.id.imageView7;
        this.trade[3] = R.id.imageView8;
        this.trade[4] = R.id.imageView9;
        this.trade[5] = R.id.imageView10;

        // Import image references
        Resources res = getResources();
        TypedArray imgs = res.obtainTypedArray(R.array.imgs);

        // Create cardView holders
        View[] handCards = new View[5];
        View[] tradeCards = new View[6];

        for(int i = 0; i < 5; i++)
        {
            handCards[i] = findViewById(this.hand[i]);
        }
        for(int i = 0; i < 6; i++)
        {
            tradeCards[i] = findViewById(this.trade[i]);
        }

        // Place images in Views
        for (int i = 0; i < 5; i++) {
            handCards[i].setBackgroundResource(imgs.getResourceId(i, 0)); // set hand
        }
        for (int i = 0; i < 6; i++) {
            tradeCards[i].setBackgroundResource(imgs.getResourceId(i + 5, 0)); // set trade row
        }


    }

    public void initCards()
    {
        // Set Points to 0
        useablePoints[0] =0;
        useablePoints[1] =0;
        useablePoints[2]= 200;

        // Import images
        Resources res = getResources();
        TypedArray imgs = res.obtainTypedArray(R.array.imgs);

        // Create array of names to be used latter
        String[] cardNames = new String[11];
        cardNames[0] = "Ram"; cardNames[1] = "Trade Pod"; cardNames[2] = "Battle Pod"; cardNames[3] = "Blob Carrier"; cardNames[4] = "Blob Destroyer";
        cardNames[5] = "Battle Blob"; cardNames[6] = "Blob Fighter"; cardNames[7] = "Blob Carrier"; cardNames[8] = "Blob Destroyer"; cardNames[9] = "Explorer";
        cardNames[10] = "Mother Ship";

        for(int i =0;i<11;i++){
            deck[i] = new Card();
        }

        // Populate each card with images, views, owners, and names
        // Default Owner is "none"
        for(int i =0;i<5;i++)
        {
            deck[i].view = findViewById(this.hand[i]);
            deck[i].image = imgs.getResourceId(i, 0);
            deck[i].name = cardNames[i];
            deck[i].owner = "none";
            myCards.addElement(deck[i]);
        }
        for(int i =0;i<6;i++)
        {
            deck[i+5].view = findViewById(this.trade[i]);
            deck[i+5].image = imgs.getResourceId(i + 5, 0);
            deck[i+5].name = cardNames[i+5];
            deck[i+5].owner = "none";
        }

        // Randomize stats of cards
        Random rand = new Random();
        for(int i =0; i<11;i++)
        {
            if(i == 0 || i == 2 || i==4 || i == 5 || i ==6 || i ==10 || i == 9 || i ==8  ){deck[i].combat = rand.nextInt(3)+1;}
            else{ deck[i].combat = 1;}

            if(i == 3 || i == 10){deck[i].authority = rand.nextInt(3)+1;}
            else{deck[i].authority = 0;}

            if(i == 0 || i == 2 || i==4 || i == 5 || i ==6 || i ==10 || i == 9 || i ==8){ deck[i].trade = rand.nextInt(2)+1;}
            else{deck[i].trade = rand.nextInt(4)+1;}


            deck[i].cost = deck[i].combat + (deck[i].trade / 2) + deck[i].authority;
            deck[i].background = i;
        }

        // Push Deck to Cloud
        Firebase.setAndroidContext(this);
        final Firebase myFirebaseRef = new Firebase("https://popping-torch-1994.firebaseio.com/");
        myFirebaseRef.child("Cards").setValue("Cards");

        for(int i=0;i<11;i++)
        {
            myFirebaseRef.child("Cards").child(deck[i].name).child("Authority").setValue(deck[i].authority);
            myFirebaseRef.child("Cards").child(deck[i].name).child("Trade").setValue(deck[i].trade);
            myFirebaseRef.child("Cards").child(deck[i].name).child("Combat").setValue(deck[i].combat);
            myFirebaseRef.child("Cards").child(deck[i].name).child("Owner").setValue(deck[i].owner);
            myFirebaseRef.child("Cards").child(deck[i].name).child("Cost").setValue(deck[i].cost);
        }

    }

    // This function is not yet being used
    public void getStats(View view)
    {
        /*
        Firebase.setAndroidContext(this);
        final Firebase myFirebaseRef = new Firebase("https://popping-torch-1994.firebaseio.com/");

        myFirebaseRef.child("Cards").child("Blob Fighter").child("Trade").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                stats[0] = (Long) snapshot.getValue();
                System.out.println(snapshot.getValue());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        */
    }

    public void playCard(View view)
    {
        Resources res = getResources();
        TypedArray imgs = res.obtainTypedArray(R.array.imgs);
        if(view.getBackground() == null){return;}

        // find Card by comparing the background to a stored array of drawables and transfer points to the user
        for(int i=0;i<11;i++) {
            if (view.getBackground().getConstantState() == getDrawable(imgs.getResourceId(i, 0)).getConstantState()) {
                System.out.println("The drawable was found");
                deck[i].owner = "ME";
                useablePoints[0] += deck[i].trade;
                useablePoints[1] += deck[i].combat;
                useablePoints[2] += deck[i].authority;
                syncCloud(view);
            }
        }
        // Remove Background so the card cannot be played again
        view.setBackgroundResource(0);
        System.out.print(useablePoints[0] + useablePoints[1] + useablePoints[2]);

        // Update the stats show to the User
        TextView combat = (TextView)findViewById(R.id.textView13);
        combat.setText("Combat: " + String.valueOf(useablePoints[1]));

        TextView trade =  (TextView)findViewById(R.id.textView11);
        trade.setText("Trade: " + String.valueOf(useablePoints[0]));

        TextView authority = (TextView) findViewById(R.id.textView12);
        authority.setText("Authority: " + String.valueOf(useablePoints[2]));
    }

    // not currently a neccesary function
    public void syncCloud(View view)
    {
        Firebase.setAndroidContext(this);
        final Firebase myFirebaseRef = new Firebase("https://popping-torch-1994.firebaseio.com/");
        for(int i=0;i<11;i++)
        {
            myFirebaseRef.child("Cards").child(deck[i].name).child("Authority").setValue(deck[i].authority);
            myFirebaseRef.child("Cards").child(deck[i].name).child("Trade").setValue(deck[i].trade);
            myFirebaseRef.child("Cards").child(deck[i].name).child("Combat").setValue(deck[i].combat);
            myFirebaseRef.child("Cards").child(deck[i].name).child("Owner").setValue(deck[i].owner);
        }
    }

    public void showStats(View view)
    {
        if(view.getBackground() == null){return;}

        Resources res = getResources();
        TypedArray imgs = res.obtainTypedArray(R.array.imgs);
        TextView showStats1 = (TextView) findViewById(R.id.textView17);
        TextView showStats2 = (TextView) findViewById(R.id.textView18);
        TextView showStats3 = (TextView) findViewById(R.id.textView23);

        // Search for a card with a matching background
        for(int i=0;i<11;i++) {
            if(view.getBackground().getConstantState() == getDrawable(imgs.getResourceId(i, 0)).getConstantState()) {

                // Output the stats of the card with the same background
                showStats1.setText(deck[i].name + ":");
                showStats2.setText("Cost = " + String.valueOf(deck[i].cost) + ",          Trade = " + String.valueOf(deck[i].trade));
                showStats3.setText("Combat = " + String.valueOf(deck[i].combat) + ",     Authority = " + String.valueOf(deck[i].authority));

            }
        }
        // This boolean makes sure that the card is not bought with a long press
        canBuyCard = false;
    }

    public void attack(View view)
    {
        TextView authority = (TextView) view;

        // Lower Opponent's Authority
        opponentAuthority -= useablePoints[1];
        String v = "Opponent Authority: " + String.valueOf(opponentAuthority);
        String gameOver = "Opponent Authority: 0";
        authority.setText(v);
        if(opponentAuthority <1){authority.setText(gameOver);}

        // Reset Combat
        useablePoints[1]=0;
        TextView combat = (TextView) findViewById(R.id.textView13);
        combat.setText("Combat: 0");
    }

    public void buyCard(View view)
    {
        if(canBuyCard == false){canBuyCard = true; return;}
        if(view.getBackground() == null){return;}

        Resources res = getResources();
        TypedArray imgs = res.obtainTypedArray(R.array.imgs);

        TextView showStats = (TextView) findViewById(R.id.textView17);
        TextView showStats2 = (TextView) findViewById(R.id.textView18);
        TextView trade = (TextView) findViewById(R.id.textView11);
        showStats.setText(null);

        boolean buy = false;

        // Search for a card with a matching background
        for(int i=0;i<11;i++) {
            if(view.getBackground().getConstantState() == getDrawable(imgs.getResourceId(i, 0)).getConstantState()) {
                if(useablePoints[0] >= deck[i].cost)
                {
                // Add Card to hand and decrement trade
                    myCards.addElement(deck[i]);
                    useablePoints[0] -= deck[i].cost;
                    String s = "Trade: " + String.valueOf(useablePoints[0]);
                    trade.setText(s);
                    buy = true;
                }
                else {showStats.setText("You cannot afford this card!"); showStats2.setText(null);}
            }
        }
        if(buy){view.setBackgroundResource(0);}

        /*
        if(view.getBackground().getConstantState() == null){return;}
        Resources res = getResources();
        TypedArray imgs = res.obtainTypedArray(R.array.imgs);

        for(int i =0;i<11;i++)
        {
            while(view.getBackground().getConstantState() == null || i<12){i++;}
            if(view.getBackground().getConstantState() == getDrawable(imgs.getResourceId(i, 0)).getConstantState())
            {
                if(useablePoints[0] >= deck[i].cost)
                {
                    useablePoints[0] -= deck[i].cost;
                    myCards.addElement(deck[i]);
                    view.setBackgroundResource(0);

                    System.out.println("The card was added to the deck " + deck[i].name );
                }
            }
        }
        */
    }

    public void nextRound(View view)
    {
        if(lost || won ){return;}
        Random rand = new Random();
        Resources res = getResources();
        TypedArray imgs = res.obtainTypedArray(R.array.imgs);

        // Randomly take a card out of the trade row to simulate the Opposing Player
        findViewById(trade[rand.nextInt(6)]).setBackgroundResource(0);

        // Generate new Cards to fill the trade row
        for(int i=0;i<6;i++)
        {
            if (findViewById(trade[i]).getBackground() == null) {
                findViewById(trade[i]).setBackgroundResource(imgs.getResourceId(rand.nextInt(10), 0));
            }
        }

        // Decrease User Authority by a random amount then output to view
        TextView uA = (TextView) findViewById(R.id.textView12);
        int newAuthority = useablePoints[2] -= (rand.nextInt(10)+10);
        String userAuthority = String.valueOf(newAuthority);
        userAuthority = "Authority: " + userAuthority;
        uA.setText(userAuthority);

        // Generate a new Hand
        changeCard(view);

        // Check for winner
        if(opponentAuthority <= 0 && won == false){win(); won = true;}
        else if(useablePoints[2] <= 0 && lost == false){lose(); lost = true;}
    }

    public void playAll(View view)
    {
        // Plays all the Cards in the user's hand
        for(int i =0;i<5;i++)
        {
            playCard(findViewById(hand[i]));
        }
    }
}


