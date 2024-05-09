package edu.msu.zukebenj.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * The main activity for the Connect game
 */
public class ConnectActivity extends AppCompatActivity {

    /**
     * The current player
     */
    String currPlayer;

    /**
     * The first player
     */
    String player1;

    /**
     * The second player
     */
    String player2;


    /**
     * The text view for the current player
     */
    TextView textView;



    /**
     * The connect view object
     */
    private ConnectView getConnectView() {
        return (ConnectView) findViewById(R.id.connectView);
    }

    /**
     * Called when the activity is created
     * @param bundle The saved instance state
     */
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_connect);
        textView = (TextView)  findViewById(R.id.playerName);
        player1 = getIntent().getStringExtra("player1_key");
        currPlayer = player1;
        player2 = getIntent().getStringExtra("player2_key");
        textView.setText(currPlayer);

    }

    /**
     * On options item selected
     * @param item The item that was selected
     * @return true if the item was selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_new_game) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * On create options menu
     * @param menu The menu that was created
     * @return true if the menu was created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_connect, menu);
        return true;
    }

    /**
     * Called when the user clicks the Done button
     * @param view The view that was clicked
     */
    public void onDone(View view) {

        // needs a placed piece to be able to be done
        if (!getConnectView().getPlayable()) {
            if (getConnectView().checkHorizontal() || getConnectView().checkVertical() || getConnectView().checkDiagonalDesc() ||
                    getConnectView().checkDiagonalAsc()) {
                getConnectView().setPlayable(false);
            }
        }

        if (getConnectView().isWon()) {
            Intent intent = new Intent(this, FinishedActivity.class);
            if (Objects.equals(currPlayer, player1)) {
                intent.putExtra("winner_name", player1);
                intent.putExtra("loser_name", player2);
            } else {
                intent.putExtra("winner_name", player2);
                intent.putExtra("loser_name", player1);
            }
            startActivity(intent);
        }

        if (!getConnectView().getPlayable()) {
            if (Objects.equals(currPlayer, player1)) {
                getConnectView().setPlayerTurn(2);
                    currPlayer = player2;
                } else {
                    currPlayer = player1;
                    getConnectView().setPlayerTurn(1);
                }
                //textView.setText(currPlayer);
                getConnectView().setPlayable(true);

            }


        textView = (TextView)  findViewById(R.id.playerName);
        textView.setText(currPlayer);

    }

    /**
     * Called when the user clicks the Undo button
     * Sets the current slot to 0 and makes it playable again
     * @param view The view that was clicked
     */
    public void onUndo(View view) {

        getConnectView().undoMove();
        getConnectView().setPlayable(true);

    }


    /**
     * Called when the user clicks the Surrender button
     * @param view The view that was clicked
     */
    public void onSurrender(View view){
        Intent intent = new Intent(this, FinishedActivity.class);
        intent.putExtra("loser_name",currPlayer);
        if(Objects.equals(currPlayer, player1)){
            intent.putExtra("winner_name",player2);
        }
        else {
            intent.putExtra("winner_name", player1);
        }
        startActivity(intent);

    }

    /**
     * Called when saved instance state is called
     * @param bundle The saved instance state
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        getConnectView().saveInstanceState(bundle);
    }

    /**
     * Called when restore instance state is restored
     * @param bundle The saved instance state
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        getConnectView().restoreInstanceState(bundle);
    }



}
