package com.mcmah113.mcmah113memorygame;

import android.graphics.Bitmap;

public class CardItem {
    private Bitmap frontCard;
    private Bitmap backCard;
    private String cardName;
    private boolean cardFlipped;

    public CardItem(Bitmap front, Bitmap back, String name){
        frontCard = front;
        backCard = back;
        cardName = name;
        cardFlipped = false;
    }

    public Bitmap getFrontCard(){
        return frontCard;
    }

    public Bitmap getBackCard(){
        return backCard;
    }

    public String getCardName(){
        return cardName;
    }

    public boolean getCardFlipped(){
        return cardFlipped;
    }

    public void setCardFlipped(boolean value){
        cardFlipped = value;
    }
}