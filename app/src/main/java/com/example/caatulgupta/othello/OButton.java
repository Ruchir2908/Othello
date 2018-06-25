package com.example.caatulgupta.othello;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;

public class OButton extends AppCompatButton{
    private int color = MainActivity.NO_COLOR;
    public int i;
    public int j;

    public int getColor(){
        return color;
    }

    public void setColor(int color){
        this.color = color;
        if(color!=MainActivity.NO_COLOR) {
            setText(color + "");
        }
    }

    public OButton(Context context){
        super(context);
    }

    public boolean isEmpty(){
        return this.color == MainActivity.NO_COLOR;
    }
}
