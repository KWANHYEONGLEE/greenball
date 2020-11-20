package com.google.ar.core.examples.java.gamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.ar.core.examples.java.augmentedimage.R;

public class ep2_wrong extends AppCompatActivity {

    private ImageView back_game_card;
    private Button answer_wrong_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ep2_wrong);


        back_game_card = (ImageView)findViewById(R.id.back_game_card);
        back_game_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        answer_wrong_back = (Button)findViewById(R.id.answer_wrong_back);
        answer_wrong_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}