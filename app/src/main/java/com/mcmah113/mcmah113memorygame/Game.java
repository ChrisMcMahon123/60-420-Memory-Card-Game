package com.mcmah113.mcmah113memorygame;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class Game extends AppCompatActivity implements EndGameDialog.OnCompleteListener {
    private AssetManager assetManager;
    private MediaPlayer mediaPlayerWin;
    private MediaPlayer mediaPlayerLose;
    private MediaPlayer mediaPlayerMatch;
    private MediaPlayer mediaPlayerMiss;
    private TextView textViewScoreValue;
    private ImageButton[] imageButtonArray;
    private CardItem[] cardItemArray;
    private Chronometer chronometer;

    private final int imageButtonIdArray[] = {
            R.id.imageButton1,  R.id.imageButton2,
            R.id.imageButton3,  R.id.imageButton4,
            R.id.imageButton5,  R.id.imageButton6,
            R.id.imageButton7,  R.id.imageButton8,
            R.id.imageButton9,  R.id.imageButton10,
            R.id.imageButton11, R.id.imageButton12,
            R.id.imageButton13, R.id.imageButton14,
            R.id.imageButton15, R.id.imageButton16,
            R.id.imageButton17, R.id.imageButton18,
            R.id.imageButton19, R.id.imageButton20
    };

    ArrayList<String> directoryImageList;

    ArrayList<String> selectedCategories;
    private int selectedNumCards;
    private int totalNumCards;

    //game variables
    private int numFlippedCards;
    private int remainingCards;
    private int selectedCards[] = new int[2];
    private int scoreValue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle bundleExtras = getIntent().getExtras();
        int numCardsList[] = bundleExtras.getIntArray("numCardsList");
        selectedNumCards = bundleExtras.getInt("selectedNumCards");
        selectedCategories = bundleExtras.getStringArrayList("selectedCategories");
        totalNumCards = selectedNumCards * 2;

        imageButtonArray = new ImageButton[totalNumCards];
        cardItemArray = new CardItem[totalNumCards];

        assetManager = getAssets();
        textViewScoreValue = findViewById(R.id.textViewScoreValue);

        chronometer = findViewById(R.id.chronometer);

        mediaPlayerWin = MediaPlayer.create(this, R.raw.win);
        mediaPlayerLose = MediaPlayer.create(this, R.raw.lose);
        mediaPlayerMatch = MediaPlayer.create(this, R.raw.match);
        mediaPlayerMiss = MediaPlayer.create(this, R.raw.miss);

        final LinearLayout linearLayout3 = findViewById(R.id.linearLayout3);
        final LinearLayout linearLayout4 = findViewById(R.id.linearLayout4);
        final LinearLayout linearLayout5 = findViewById(R.id.linearLayout5);

        if (selectedNumCards == numCardsList[0]) {
            //total of 8 cards
            linearLayout3.setVisibility(View.INVISIBLE);
            linearLayout4.setVisibility(View.INVISIBLE);
            linearLayout5.setVisibility(View.INVISIBLE);
        }
        else if (selectedNumCards == numCardsList[1]) {
            //total of 12 cards
            linearLayout4.setVisibility(View.INVISIBLE);
            linearLayout5.setVisibility(View.INVISIBLE);
        }
        else if(selectedNumCards == numCardsList[2]){
            //total of 16 cards
            linearLayout5.setVisibility(View.INVISIBLE);
        }

        //set the array lists that will store the image strings
        directoryImageList = new ArrayList<>();
        String[] directoryList;

        for(int i = 0; i < selectedCategories.size(); i ++){
            try {
                directoryList = assetManager.list(selectedCategories.get(i));

                for(String directoryEntry : directoryList){
                    //get all of the images in the directory
                    directoryImageList.add(selectedCategories.get(i) + "/" + directoryEntry);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        newGame();
    }

    public void newGame() {
        selectedCards[0] = -1;
        selectedCards[1] = -1;
        scoreValue = 1000;

        numFlippedCards = 0;
        remainingCards = totalNumCards;

        textViewScoreValue.setText(String.format(Locale.CANADA, "%d", scoreValue));

        Collections.shuffle(directoryImageList);

        //narrowed down list used for the game buttons images
        ArrayList<String> gameImagesList = new ArrayList<>();

        for(int i = 0; i < selectedNumCards; i ++) {
            gameImagesList.add(directoryImageList.get(i));
            gameImagesList.add(directoryImageList.get(i));
        }

        Collections.shuffle(gameImagesList);

        for(int i = 0; i < totalNumCards; i ++) {
            final int anonIndex = i;

            imageButtonArray[i] = findViewById(imageButtonIdArray[i]);

            try {
                //load the images for the card front and back
                InputStream inputStreamFront = assetManager.open(gameImagesList.get(i));
                InputStream inputStreamBack = assetManager.open("cardback.png");

                //set the images to the corresponding CardItem object
                Bitmap imageFront = BitmapFactory.decodeStream(inputStreamFront);
                Bitmap imageBack = BitmapFactory.decodeStream(inputStreamBack);
                cardItemArray[i] = new CardItem(imageFront,imageBack,gameImagesList.get(i));

                imageButtonArray[i].setImageBitmap(cardItemArray[i].getBackCard());
                inputStreamFront.close();
                inputStreamBack.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            imageButtonArray[i].setEnabled(true);
            imageButtonArray[i].setVisibility(View.VISIBLE);
            imageButtonArray[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final Handler delayHandler = new Handler();

                    if(!cardItemArray[anonIndex].getCardFlipped()){
                        //card is facing down
                        flipCardUp(anonIndex);
                        cardItemArray[anonIndex].setCardFlipped(true);
                        imageButtonArray[anonIndex].setImageBitmap(cardItemArray[anonIndex].getFrontCard());
                    }

                    if(getNumFlippedCards() == 2){
                        final int cardsFlipped[] = getSelectedCards();
                        //two cards are facing up now, do they equal each other?
                        if(doCardsMatch(cardItemArray[cardsFlipped[0]].getCardName(),cardItemArray[cardsFlipped[1]].getCardName())) {
                            //cards match
                            Toast.makeText(Game.this, "Found a match!", Toast.LENGTH_SHORT).show();

                            //remove cards and prevent input during the delay
                            for(ImageButton imageButton : imageButtonArray){
                                imageButton.setEnabled(false);
                            }

                            delayHandler.postDelayed(new Runnable() {
                                public void run() {
                                    imageButtonArray[cardsFlipped[0]].setVisibility(View.INVISIBLE);
                                    imageButtonArray[cardsFlipped[1]].setVisibility(View.INVISIBLE);

                                    flipCardDown(cardsFlipped[0]);
                                    flipCardDown(cardsFlipped[1]);

                                    for(ImageButton imageButton : imageButtonArray){
                                        imageButton.setEnabled(true);
                                    }

                                    //check to see if the player has won
                                    gameOver();
                                }
                            }, 1000);
                        }
                        else {
                            //cards don't match
                            Toast.makeText(Game.this, "Cards don't match!", Toast.LENGTH_SHORT).show();

                            //flip the cards back and prevent input during the delay
                            for(ImageButton imageButton : imageButtonArray){
                                imageButton.setEnabled(false);
                            }

                            delayHandler.postDelayed(new Runnable() {
                                public void run() {
                                    cardItemArray[cardsFlipped[0]].setCardFlipped(false);
                                    cardItemArray[cardsFlipped[1]].setCardFlipped(false);
                                    imageButtonArray[cardsFlipped[0]].setImageBitmap(cardItemArray[cardsFlipped[0]].getBackCard());
                                    imageButtonArray[cardsFlipped[1]].setImageBitmap(cardItemArray[cardsFlipped[1]].getBackCard());

                                    flipCardDown(cardsFlipped[0]);
                                    flipCardDown(cardsFlipped[1]);

                                    for(ImageButton imageButton : imageButtonArray){
                                        imageButton.setEnabled(true);
                                    }

                                    //reduce the players score
                                    reduceScore();
                                }
                            }, 1000);
                        }
                    }
                }
            });
        }

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    public void flipCardUp(int card) {
        if(numFlippedCards <= 2){
            if(selectedCards[0] == -1) {
                selectedCards[0] = card;
            }
            else if(selectedCards[1] == -1) {
                selectedCards[1] = card;
            }

            numFlippedCards ++;
        }
    }

    public void flipCardDown(int card) {
        if(numFlippedCards >= 1){
            if(selectedCards[0] == card) {
                selectedCards[0] = -1;
            }
            else if(selectedCards[1] == card) {
                selectedCards[1] = -1;
            }

            numFlippedCards --;
        }
    }

    public int getNumFlippedCards() {
        return numFlippedCards;
    }

    public int[] getSelectedCards() {
        return selectedCards;
    }

    public boolean doCardsMatch(String card1, String card2) {
        if(card1.equals(card2)){
            remainingCards -= 2;
            mediaPlayerMatch.start();

            return true;
        }
        else {
            mediaPlayerMiss.start();
            return false;
        }
    }

    public void reduceScore() {
        scoreValue -= 40;
        textViewScoreValue.setText(String.format(Locale.CANADA, "%d", scoreValue));

        if(scoreValue <= 0) {
            endGameDialog("Lose");
        }
    }

    public void gameOver() {
        if(remainingCards == 0) {
            endGameDialog("Win");
        }
    }

    public void endGameDialog(String type) {
        chronometer.stop();

        if(type.equals("Win")) {
            mediaPlayerWin.start();
        }
        else if(type.equals("Lose")){
            mediaPlayerLose.start();
        }


        //end game fragment
        Bundle args = new Bundle();
        args.putInt("score", scoreValue);
        args.putString("time", chronometer.getText().toString());
        args.putString("type", type);
        DialogFragment winDialog = new EndGameDialog();
        winDialog.setArguments(args);
        winDialog.setCancelable(false);
        winDialog.show(getFragmentManager(), "Settings Dialog");
    }

    public void onComplete(Bundle callbackData) {
        String dialogName = callbackData.getString("dialog");

        if(dialogName.equals("newGame")) {
            newGame();
        }
        else if(dialogName.equals("mainMenu")) {
            returnToMain();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            //android UI back button in the tab bar
            returnToMain();
        }

        return true;
    }

    public void onBackPressed() {
        //android back button has been pressed
        returnToMain();
    }

    public void returnToMain() {
        chronometer.stop();
        mediaPlayerWin.release();
        mediaPlayerLose.release();
        mediaPlayerMatch.release();
        mediaPlayerMiss.release();

        Intent intent = new Intent();
        intent.putExtra("selectedNumCards",selectedNumCards);
        intent.putExtra("selectedCategories",selectedCategories);
        setResult(RESULT_OK, intent);
        finish();
    }
}
