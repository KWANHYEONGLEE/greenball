package com.google.ar.core.examples.java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.ar.core.examples.java.augmentedimage.R;

public class DescriptionActivity extends AppCompatActivity {

    private IntroduceItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        item = (IntroduceItem) getIntent().getSerializableExtra("item");
    }
}