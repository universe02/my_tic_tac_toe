package universe02.example.zhouyu.tictactoe;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import universe02.example.zhouyu.samecolorsudoku.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] btns = new Button[3][3];
    private Button newGame;
    private Button select_O, select_X;
    private boolean userClick = true;
    private String user_Icon, pc_Icon;
    private int count = 0;
    private int bestMove;
    private List<Button> btnList = new ArrayList<>();//store 9 buttons
    private String board[][] = new String[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Mat buttons
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String btnID = "btn_" + i + j;
                int resID = getResources().getIdentifier(btnID, "id", getPackageName());
                btns[i][j] = findViewById(resID);
                btns[i][j].setOnClickListener(this);
                btns[i][j].setBackgroundResource(R.color.myYellow);
                btnList.add(btns[i][j]);
            }
        }
        disable_btns();



        select_O = findViewById(R.id.select_O);
        select_X = findViewById(R.id.select_X);
        //User select O
        select_O.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_O.setText("You select\n O");
                select_X.setVisibility(View.INVISIBLE);
                user_Icon = "O";
                pc_Icon = "X";
                enable_btns();
            }
        });
        //User select X
        select_X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_X.setText("You select\n X");
                select_O.setVisibility(View.INVISIBLE);
                user_Icon = "X";
                pc_Icon = "O";
                enable_btns();
            }
        });

        //Start a new game
        newGame = findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

    }

    //Check if user or PC win on every click
    private boolean checkWin(String[][] mat, boolean max) {
        mat= new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mat[i][j] = btns[i][j].getText().toString();
            }
        }
        //compare the row icon
        for (int i = 0; i < 3; i++) {
            if (mat[i][0].equals(mat[i][1]) && mat[i][0].equals(mat[i][2]) && !mat[i][0].equals("")&&mat[i][0]==pc_Icon) {
                return true;
            }
        }
        //compare the column icon
        for (int j = 0; j < 3; j++) {
            if (mat[0][j].equals(mat[1][j]) && mat[0][j].equals(mat[2][j]) && !mat[0][j].equals("")&&mat[0][j]==pc_Icon) {
                return true;
            }
        }
        //compare the diagonal
        if (mat[0][0].equals(mat[1][1]) && mat[0][0].equals(mat[2][2]) && !mat[0][0].equals("")&&mat[0][0]==pc_Icon) {
            return true;
        }
        if (mat[0][2].equals(mat[1][1]) && mat[0][2].equals(mat[2][0]) && !mat[0][2].equals("")&&mat[0][2]==pc_Icon) {
            return true;
        }
        return false;

    }

    public void setBoard(){
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                board[i][j] = btns[i][j].getText().toString();
            }
        }
    }

    @Override
    public void onClick(View v) {
        //copy btns[][] to board[][]
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = btns[i][j].getText().toString();
            }
        }
        count++;
        /*if (!((Button) v).getText().toString().equals("")) {
            return;
        }*/
        //user play the game
        ((Button) v).setTextColor(Color.RED);
        ((Button) v).setTextSize(20);
        ((Button) v).setText(user_Icon);
        //setBoard();
        //PC play the game
        int move[] = new int[2];
        //move[0] = bestMove();
        btns[miniMax(2, true, 100, -100)[1]][miniMax(2, true, 100, -100)[2]].setText(pc_Icon);
       /* btnList.get(bestMove).setText(pc_Icon);
        btnList.get(bestMove).setEnabled(false);*/
        //setBoard();
        //show game result
       /* if (checkWin(board, true)) {
            if (userClick) {
                user_win();
            } else {
                pc_win();
            }
        } else if (count == 9) {
            draw();
        }*/
    }



    public int[] miniMax(int depth,boolean isPC, int max, int min) {
        int value = 0;
        List<int[]> nextMove = getMove();
        int nextRow = -1;
        int nextCol = -1;
        //PC win return 10
        if (checkWin(board, true))
            return new int[]{value, nextRow, nextCol};
        else if (!checkWin(board, false))
            return new int[]{value, nextRow, nextCol};
        else if (count == 9)
            return new int[]{0, nextRow, nextCol};
        int temp = 0;

        for (int[] move : nextMove) {

            if (board[move[0]][move[1]].equals(pc_Icon)) {
                value = miniMax(depth - 1, isPC, max, min)[0];
                if (value > max) {
                    max = value;
                    nextRow = move[0];
                    nextCol = move[1];
                }
            } else { // oppSeed is minimizing player
                value = miniMax(depth - 1, isPC, max, min)[0];
                if (value < min) {
                    min = value;
                    nextRow = move[0];
                    nextCol = move[1];
                }
            }
            // undo move
            board[move[0]][move[1]] = "";
            // cut-off
            if (max >= min)
                break;
        }
        return new int[] { isPC? max : min, nextRow, nextCol };
    }


    private List<int[]> getMove(){
        List<int[]> nextMove = new ArrayList<int[]>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (TextUtils.isEmpty(board[i][j])){
                    nextMove.add(new int[] {i, j});
                }
            }
        }
        return nextMove;
    }

   /* public int[] bestMove(String cell, boolean isPC){
        int best[] = new int[2];
        int temp=0;
        int value=0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (TextUtils.isEmpty(board[i][j])) {
                    {
                        temp = miniMax(board[i][j], isPC);
                    }
                    if (isPC && value < temp || !isPC && value > temp) {
                        value = temp;
                        best[0] = i;
                        best[1] = j;
                    }
                }
            }
        }
        return best;
    }*/




      /*  if (isPC) {
            int bestValue = -10000;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (TextUtils.isEmpty(values[i][j])) {
                        values[i][j] = pc_Icon;
                        count++;
                        bestValue = Math.max(bestValue, miniMax(values, !isPC));
                        values[i][j] = "";
                        count--;
                    }
                }
            }
            return bestValue;
        } else {
            int bestValue = 10000;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (TextUtils.isEmpty(values[i][j])) {
                        values[i][j] = user_Icon;
                        count++;
                        bestValue = Math.max(bestValue, miniMax(values, !isPC));
                        count--;
                        values[i][j] = "";
                    }
                }
            }
            return bestValue;
        }*/


    //Three results
    private void user_win () {
        Toast.makeText(this, "Congradulations!!! You win!!!Please click New Game", Toast.LENGTH_SHORT).show();
        disable_btns();
    }

    private void pc_win () {
        Toast.makeText(this, "Sorry, you lose.Please click New Game", Toast.LENGTH_SHORT).show();
        disable_btns();
    }

    private void draw(){
        Toast.makeText(this, "It is Draw.Please click New Game", Toast.LENGTH_SHORT).show();
        disable_btns();
    }

    //Prevent user to click the button before user select the XO or the game is finished
    private void disable_btns(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                btns[i][j].setEnabled(false);
            }
        }
    }

    //Enable Mat buttons
    private void enable_btns(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                btns[i][j].setEnabled(true);
            }
        }
    }


}