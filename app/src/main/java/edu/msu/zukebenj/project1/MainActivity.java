package edu.msu.zukebenj.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Main activity for the Connect Four game
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The name of player 1
     */
    public String player1 = "";

    /**
     * The name of player 2
     */
    public String player2 = "";

    /**
     * On create method for the main activity
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * On start method for the main activity
     */

    public void onStartConnect(View view) {
        TextView player1_text = findViewById(R.id.playerOneName);
        TextView player2_text = findViewById(R.id.playerTwoName);

        player1 = player1_text.getText().toString();
        player2 = player2_text.getText().toString();

        Intent intent = new Intent(this, ConnectActivity.class);
        if (player1.isEmpty())
        {
            player1 = "Player 1";
        }
        if (player2.isEmpty())
        {
            player2 = "Player 2";
        }
        intent.putExtra("player1_key", player1);
        intent.putExtra("player2_key", player2);
        startActivity(intent);


    }



}