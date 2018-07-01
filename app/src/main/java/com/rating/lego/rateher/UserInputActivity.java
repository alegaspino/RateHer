package com.rating.lego.rateher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class UserInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.g_array,
                android.R.layout.simple_spinner_item);
        Spinner spinner = findViewById(R.id.spinner_gchoice);
        spinner.setAdapter(arrayAdapter);
    }

    public void doRate(View view) {
        Intent newActivity = new Intent(this, GroupSyncActivity.class);
        startActivity(newActivity);
    }
}
