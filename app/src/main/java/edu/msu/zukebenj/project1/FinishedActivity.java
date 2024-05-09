package edu.msu.zukebenj.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Finished activity for the game constructor
 */
public class FinishedActivity extends AppCompatActivity {


    /**
     * On create method for the finished activity
     * @param bundle the bundle
     */
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_finished);

        TextView textView3 = (TextView) findViewById(R.id.textView3);
        TextView textView4 = (TextView) findViewById(R.id.textView4);


        String name = getIntent().getStringExtra("loser_name");
        String name2 = getIntent().getStringExtra("winner_name");
        textView3.setText(String.format("%s %s", name2, textView3.getText()));
        textView4.setText(String.format("Sorry %s you lost", name));


    }


    /**
     * On new game method
     * @param view the view
     */
    public void onNewGame(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }






}
