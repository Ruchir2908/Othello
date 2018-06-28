package com.example.caatulgupta.othello;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int BLACK = 0;
    public static final int WHITE = 1;
    public static final int NO_COLOR = -1;
    public int BCount = 2;
    public int WCount = 2;
    public int[] Count;

    public static final int INCOMPLETE = 1;
    public static final int WWon = 2;
    public static final int BWon = 3;
    public static final int DRAW = 4;

    public int SIZE = 8;

    public int currentPlayer;
    public int opponent;
    public ArrayList<LinearLayout> rows;
    public OButton[][] board;
    public int currentStatus = INCOMPLETE;

    public static final int[] x = {-1,-1,-1,0,0,1,1,1};
    public static final int[] y = {-1,0,1,1,-1,-1,0,1};

    LinearLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootLayout = findViewById(R.id.rootLayout);

        setUpBoard();
    }

    public void setUpBoard(){
        rows = new ArrayList<>();
        board = new OButton[SIZE][SIZE];
        currentPlayer = BLACK;
        opponent = WHITE;
        //rootLayout.removeAllViews();

        for(int i=0;i<SIZE;i++){
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1);
            linearLayout.setLayoutParams(layoutParams);
            rootLayout.addView(linearLayout);
            rows.add(linearLayout);
        }

        for(int i=0;i<SIZE;i++){
            for(int j=0;j<SIZE;j++){
                OButton button = new OButton(MainActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
                button.setLayoutParams(layoutParams);
                button.setOnClickListener(MainActivity.this);
                LinearLayout row = rows.get(i);
                row.addView(button);
                button.setColor(NO_COLOR);
                board[i][j] = button;
                button.i = i;
                button.j = j;
            }
        }

        board[3][3].setColor(WHITE);
        board[4][4].setColor(WHITE);
        board[3][4].setColor(BLACK);
        board[4][3].setColor(BLACK);

    }

    public void onClick(View view){
        if(currentStatus==INCOMPLETE){
            OButton button = (OButton) view;
//            if(currentPlayer==BLACK){
//                Count[currentPlayer]++;
//            }else{
//                Count[currentPlayer]++;
//            }
            if(validMoveRecursive(view)) {
                changeColor(view);
                button.setColor(currentPlayer);
                togglePlayer();
            }
        }
    }

    public void changeColor(View view) {
        OButton button = (OButton) view;
        int i = button.i;
        int j = button.j;
        int ni = 0;
        int nj = 0;
        for(int k=0;k<8;k++){
            ni = i + x[k];
            nj = j + y[k];
            if(ni<0 || nj<0 || ni>=SIZE || nj>=SIZE || board[ni][nj].getColor()==NO_COLOR || board[ni][nj].getColor()==currentPlayer){
                continue;
            }
            change(ni,nj,k,true);
        }
    }

    public void change(int ni, int nj, int k,boolean first) {
        if(ni<0 || nj<0 || ni>=SIZE || nj>=SIZE){
            return;
        }
//        if(first){
//            board[ni][nj].setColor(currentPlayer);
//        }
        if(board[ni][nj].getColor()==NO_COLOR){
            board[ni-x[k]][nj-y[k]].setColor(opponent);
            return;
        }
        if(board[ni][nj].getColor()==currentPlayer){
//            board[ni-x[k]][nj-y[k]].setColor(currentPlayer);
            return;
        }

        board[ni][nj].setColor(currentPlayer);
        change(ni+x[k],nj+y[k],k,false);
        return;
    }

    public boolean validMoveRecursive(View view){
        OButton button = (OButton) view;
        int i = button.i;
        int j = button.j;
        int ni = 0;
        int nj = 0;
        boolean ans = false;
        for(int k=0;k<8;k++){
            ans = false;
            ni = i + x[k];
            nj = j + y[k];
            if(ni<0 || nj<0 || ni>=SIZE || nj>=SIZE || board[ni][nj].getColor()==NO_COLOR || board[ni][nj].getColor()==currentPlayer){
                ans = false;
                continue;
            }
            if(validity(ni,nj,k)){
                ans = true;
                //change(ni,nj,k);
//                move = true;
                break;
            }
        }
        return ans;
    }

    public boolean validity(int ni, int nj, int k) {
        if(ni<0 || nj<0 || ni>=SIZE || nj>=SIZE){
            return false;
        }
        if(board[ni][nj].getColor()==currentPlayer){
            return true;
        }
        if(board[ni][nj].getColor()==NO_COLOR){
            return false;
        }
        return validity(ni+x[k],nj+y[k],k);
    }

    public boolean validMove(View view){
//        Log.i("Enter1","INIT");
        OButton button = (OButton) view;
        int i=button.i;
        int j=button.j;
        boolean valid = false;

//        if(currentPlayer==BLACK){
//            button.setColor(BLACK);
//        }else{
//            button.setColor(WHITE);
//        }
        //must contain one white neighbour
        //jis direction mein white neighbour h ussi dir mein last white disc kablack neighbour bhi hona chaiye

        if((i > 0) && (j > 0) && (board[i - 1][j - 1].getColor() != currentPlayer) && (board[i - 1][j - 1].getColor() != NO_COLOR)){
//            Log.i("Upper","I: "+i+" J: "+j+ "First");
            i = button.i-1;
            j = button.j-1;
//            Log.i("Upper","I: "+i+" J: "+j+ "SECOND");
            while(i>=0 && j>=0){
                if(board[i][j].getColor()==NO_COLOR){
                    break;
                }
                if(board[i][j].getColor()==currentPlayer){
//                    Log.i("Upper","I: "+i+" J: "+j+"THIRD"+" 1: "+button.i+" 2: "+button.j);
                    valid = true;
                    break;
                }
//                Log.i("Upper","I: "+i+" J: "+j);
                i--;
                j--;
            }
            i=button.i;
            j=button.j;
        }
        if((i > 0) && (board[i - 1][j].getColor() != currentPlayer) && (board[i - 1][j].getColor() != NO_COLOR)){
            i = button.i-1;
            while(i>=0){
                if(board[i][j].getColor()==NO_COLOR){
                    break;
                }
                if(board[i][j].getColor()==currentPlayer){
                    valid = true;
                    break;
                }
                i--;
            }
            i=button.i;
            j=button.j;
        }
        if((i > 0) && (j < SIZE-1) && (board[i - 1][j + 1].getColor() != currentPlayer) && (board[i - 1][j + 1].getColor() != NO_COLOR)){
            i = button.i-1;
            j = button.j+1;
            while(i>=0 && j<SIZE){
                if(board[i][j].getColor()==NO_COLOR){
                    break;
                }
                if(board[i][j].getColor()==currentPlayer){
                    valid = true;
                    break;
                }
                i--;
                j++;
            }
            i=button.i;
            j=button.j;
        }
        if((j > 0) && (board[i][j - 1].getColor() != currentPlayer) && (board[i][j - 1].getColor() != NO_COLOR)){
            j = button.j-1;
            while(j>=0){
                if(board[i][j].getColor()==NO_COLOR){
                    break;
                }
                if(board[i][j].getColor()==currentPlayer){
                    valid = true;
                    break;
                }
                j--;
            }
            i=button.i;
            j=button.j;
        }
        if((j < SIZE-1) && (board[i][j + 1].getColor() != currentPlayer) && (board[i][j + 1].getColor() != NO_COLOR)){
            j = button.j+1;
            while(j<SIZE){
                if(board[i][j].getColor()==NO_COLOR){
                    break;
                }
                if(board[i][j].getColor()==currentPlayer){
                    valid = true;
                    break;
                }
                j++;
            }
            i=button.i;
            j=button.j;
        }
        if((i < SIZE-1) && (j > 0) && (board[i + 1][j - 1].getColor() != currentPlayer) && (board[i + 1][j - 1].getColor() != NO_COLOR)){
            i = button.i+1;
            j = button.j-1;
            while(i<SIZE && j>=0){
                if(board[i][j].getColor()==NO_COLOR){
                    break;
                }
                if(board[i][j].getColor()==currentPlayer){
                    valid = true;
                    break;
                }
                i++;
                j--;
            }
            i=button.i;
            j=button.j;
        }
        if((i < SIZE-1) && (board[i + 1][j].getColor() != currentPlayer) && (board[i + 1][j].getColor() != NO_COLOR)){
//            Log.i("Lower","I: "+i+" J: "+j+" 11111");
            i = button.i+1;
//            Log.i("Lower","I: "+i+" J: "+j+" 22222");
            while(i<SIZE){
//                Log.i("Lower","I: "+i+" J: "+j+" 33333");
                if(board[i][j].getColor()==NO_COLOR){
                    break;
                }
                if(board[i][j].getColor()==currentPlayer){
//                    Log.i("Lower","I: "+i+" J: "+j+" 44444");
                    valid = true;
                    break;
                }
                i++;
            }
//            Log.i("Lower","I: "+i+" J: "+j+" 55555");
            i=button.i;
            j=button.j;
        }
        if((i < SIZE-1) && (j < SIZE-1) && (board[i + 1][j + 1].getColor() != currentPlayer) && (board[i + 1][j + 1].getColor() != NO_COLOR)){
            i = button.i+1;
            j = button.j+1;
            while(i<SIZE && j<SIZE) {
                if(board[i][j].getColor()==NO_COLOR){
                    break;
                }
                if (board[i][j].getColor() == currentPlayer) {
                    valid = true;
                    break;
                }
                i++;
                j++;
            }
            i=button.i;
            j=button.j;
        }

        return valid;
    }

    public void checkGame() {
        return;
    }

    public void togglePlayer(){
        if(currentPlayer==BLACK){
            currentPlayer = WHITE;
            opponent = BLACK;
            Toast.makeText(this,"WHITE 1",Toast.LENGTH_LONG).show();
        }else{
            currentPlayer = BLACK;
            opponent = WHITE;
            Toast.makeText(this,"BLACK 0",Toast.LENGTH_LONG).show();
        }
    }

}
