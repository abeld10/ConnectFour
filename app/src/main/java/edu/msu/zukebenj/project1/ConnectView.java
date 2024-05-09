package edu.msu.zukebenj.project1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * Custom view class for our Connect Board.
 */
public class ConnectView extends View {


    /**
     * The number of rows in the board
     */
    private final int ROW_COUNT = 6;
    /**
     * The number of columns in the board
     */
    private final int COL_COUNT = 7;

    /**
     * The game board with the rows and columns
     */
    private final int[][] gameBoard = new int[ROW_COUNT][COL_COUNT];

    /**
     * The pieces for the first player
     */
    private final Drawable player1Piece;
    /**
     * The pieces for the second player
     */
    private final Drawable player2Piece;
    /**
     * The empty slot
     */
    private final Drawable emptySlot;

    /**
     * The margin of the board
     */
    private int margin = 0;

    /**
     * The size of a slot
     */
    private int slot_size = 0;

    /**
     * The current player's turn
     */
    private int playerTurn = 1;


    /**
     * if the game is playable
     */
    boolean playable = true;

    /**
     * current row
     */
    int currRow;
    /**
     * current column
     */
    int currCol;

    /**
     * if the game is won
     * @return if the game is won
     */
    public boolean isWon() {
        return won;
    }

    /**
     * if the game is won
     */
    boolean won = false;

    /**
     * Connect view constructor for attributes
     * @param context context
     * @param attrs attributes
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("UseCompatLoadingForDrawables")
    public ConnectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        player1Piece = getResources().getDrawable(R.drawable.green);
        player2Piece = getResources().getDrawable(R.drawable.white);
        emptySlot = getResources().getDrawable(R.drawable.empty);
    }

    /**
     * Initialize the view and set the attributes
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("UseCompatLoadingForDrawables")
    private void init() {

        // get the size of a piece
        slot_size = getResources().getDrawable(R.drawable.empty).getIntrinsicWidth();

        // get the margin of the board
        margin = (getResources().getDrawable(R.drawable.empty).getIntrinsicWidth()*7) / 2;


    }

    /**
     * Undo the move
     */
    public void undoMove() {
        if (!playable) {

            gameBoard[currRow][currCol] = 0;
            playable = true;
            invalidate();
        }
    }

    /**
     * On draw method
     * @param canvas canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);


        // Draw the game board
        int slotSize = Math.min(getWidth() / COL_COUNT, getHeight() / ROW_COUNT);
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COL_COUNT; j++) {
                int left = j * slotSize;
                int top = i * slotSize;
                int right = left + slotSize;
                int bottom = top + slotSize;

                // get screen width
                int screenWidth = getWidth();

                // get board width
                int boardWidth = COL_COUNT * slotSize;

                // get margin
                margin = (screenWidth - boardWidth) / 2;

                left += margin;
                right += margin;

                emptySlot.setBounds(left, top, right, bottom);
                emptySlot.draw(canvas);

                // get the translation of the piece
                int left2 = j * slotSize;
                left2 += margin;
                // invert the row so that the top of the screen is row 0
                //int top2 = (ROW_COUNT - i - 1) * slotSize;
                int right2 = left2 + slotSize;

                //int bottom2 = top2 + slotSize;

                if (gameBoard[i][j] == 1) {

                    player1Piece.setBounds(left2, top, right2, bottom);
                    player1Piece.draw(canvas);

                } else if (gameBoard[i][j] == 2) {
                    player2Piece.setBounds(left2, top, right2, bottom);
                    player2Piece.draw(canvas);
                }
            }
        }
    }


    /**
     * Click listener
     * @param x x
     * @param y y
     */
    // click listener
    @SuppressWarnings("deprecation")
    @SuppressLint("UseCompatLoadingForDrawables")
    public void onClick(int x, int y) {

        if (playable) {

            // get the size of a piece
            slot_size = getResources().getDrawable(R.drawable.empty).getIntrinsicWidth();


            //int col = (int) (x / (float) getWidth() * COL_COUNT);


            float float_width = getWidth();

            // scale the width by the margin
            int width = (int)(float_width - (margin * 2))/7;
            width = width * 7;

            float tileWidth = (float)(width / COL_COUNT);


            int col = (int)((x-margin)/tileWidth);

            // use the getRow function in Connect to get the non occupied row
            int row = ROW_COUNT;

            // attempts to index out of bounds
            if (col < 0 || col >= COL_COUNT) {
                return;
            }

            // attempts to do an x that is in margin
            if (x < margin || x > (margin + (COL_COUNT * slot_size))) {
                return;
            }


            // found a suitable row
            boolean found = false;


            // iterate through the rows until we find the first empty row
            for (int i = ROW_COUNT -1; i >= 0; i--) {
                if (gameBoard[i][col] == 0) {
                    row = i;
                    found = true;
                    break;
                }
            }

            // if we didn't find a row, return
            if (!found) {
                return;
            }

            // if the slot is empty, place a piece in it
            if (gameBoard[row][col] == 0) {
                gameBoard[row][col] = playerTurn;
                currCol = col;
                currRow = row;

                invalidate();

                playable = false;
            }

            // redraw the board
        }
    }

    /**
     * Checks the horizontal line for a win
     * only within where the last piece was placed
     * @return boolean
     */
    public boolean checkHorizontal() {
        int count = 0;
        int player = gameBoard[currRow][currCol];

        // check left
        for (int i = currCol; i >= 0; i--) {
            if (gameBoard[currRow][i] == player) {
                count++;
            } else {
                break;
            }
        }

        // check right
        for (int i = currCol + 1; i < COL_COUNT; i++) {
            if (gameBoard[currRow][i] == player) {
                count++;
            } else {
                break;
            }
        }

        if (count >= 4) {
            won = true;
            return true;
            // Call to win screen
        } else {
            return false;
        }
    }

    /**
     * Checks the vertical line for a win
     * only within where the last piece was placed
     * @return boolean
     */
    public boolean checkVertical() {
        int count = 0;
        int player = gameBoard[currRow][currCol];

        // check up
        for (int i = currRow; i >= 0; i--) {
            if (gameBoard[i][currCol] == player) {
                count++;
            } else {
                break;
            }
        }

        // check down
        for (int i = currRow + 1; i < ROW_COUNT; i++) {
            if (gameBoard[i][currCol] == player) {
                count++;
            } else {
                break;
            }
        }

        if (count >= 4) {
            won = true;
            return true;
            // Call to win screen
        } else {
            return false;
        }
    }

    /**
     * Checks the diagonal line for a win
     * ensure the indexes don't go out of bounds before checking
     * @return boolean won
     */
    public boolean checkDiagonalDesc() {
        int count = 0;
        int player = gameBoard[currRow][currCol];

        // check up
        for (int i = currRow, j = currCol; i >= 0 && j >= 0; i--, j--) {
            if (gameBoard[i][j] == player) {
                count++;
            } else {
                break;
            }
        }

        // check down
        for (int i = currRow + 1, j = currCol + 1; i < ROW_COUNT && j < COL_COUNT; i++, j++) {
            if (gameBoard[i][j] == player) {
                count++;
            } else {
                break;
            }
        }

        if (count >= 4) {
            won = true;
            return true;
            // Call to win screen
        } else {
            return false;
        }
    }

    /**
     * Checks the diagonal line for a win
     */
    public boolean checkDiagonalAsc() {
        int count = 0;
        int player = gameBoard[currRow][currCol];

        // check up
        for (int i = currRow, j = currCol; i >= 0 && j < COL_COUNT; i--, j++) {
            if (gameBoard[i][j] == player) {
                count++;
            } else {
                break;
            }
        }

        // check down
        for (int i = currRow + 1, j = currCol - 1; i < ROW_COUNT && j >= 0; i++, j--) {
            if (gameBoard[i][j] == player) {
                count++;
            } else {
                break;
            }
        }

        if (count >= 4) {
            won = true;
            return true;
            // Call to win screen
        } else {
            return false;
        }
    }

    /**
     * On touch event
     * @param event The motion event
     * @return boolean
     */
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            onClick((int) event.getX(), (int) event.getY());
        }
        return true;
    }

    /**
     * Set the player turn
     * @param playerTurn The player turn
     */
    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    /**
     * Set the playable
     * @param doneClicked The done clicked
     */
    public void setPlayable(boolean doneClicked) {
        playable = doneClicked;
    }

    /**
     * Get the playable
     * @return boolean playable
     */
    public boolean getPlayable() {
        return playable;
    }

    /**
     * get the margin
     * @return int margin
     */
    public int getMargin() {
        return margin;
    }

    /**
     * Save the puzzle to a bundle
     * @param bundle The bundle we save to
     */
    @SuppressWarnings("ManualArrayCopy")
    public void saveInstanceState(Bundle bundle) {
        int[] flattenBoard = new int[gameBoard.length * gameBoard[0].length];
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                flattenBoard[i * gameBoard[i].length + j] = gameBoard[i][j];
            }
        }
        bundle.putIntArray("flattenBoard", flattenBoard);
        bundle.putInt("playerTurn", playerTurn);
        bundle.putInt("currRow", currRow);
        bundle.putInt("currCol", currCol);
        bundle.putBoolean("won", won);
        bundle.putBoolean("playable", playable);
    }

    /**
     * Restore the puzzle from a bundle
     * @param bundle The bundle we restore from
     */
    public void restoreInstanceState(Bundle bundle) {
        int[] flattenBoard = bundle.getIntArray("flattenBoard");
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                gameBoard[i][j] = flattenBoard[i * gameBoard[i].length + j];
            }
        }
        playerTurn = bundle.getInt("playerTurn");
        currRow = bundle.getInt("currRow");
        currCol = bundle.getInt("currCol");
        won = bundle.getBoolean("won");
        playable = bundle.getBoolean("playable");

        invalidate();
    }

}