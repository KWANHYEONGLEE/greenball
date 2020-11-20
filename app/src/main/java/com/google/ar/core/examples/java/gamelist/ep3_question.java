package com.google.ar.core.examples.java.gamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.ar.core.examples.java.augmentedimage.R;

public class ep3_question extends AppCompatActivity {

    private Button ep3_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ep3_question);

        ep3_scan = (Button)findViewById(R.id.ep3_scan);
        ep3_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
    }
}