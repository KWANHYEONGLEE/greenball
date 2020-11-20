package com.google.ar.core.examples.java.gamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.ar.core.examples.java.augmentedimage.R;

public class ep7 extends AppCompatActivity {

    private ImageView back_game_card;
    private Button game_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ep7);


        back_game_card = (ImageView) findViewById(R.id.back_game_card);
        back_game_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        game_finish = (Button) findViewById(R.id.game_finish);
        game_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}