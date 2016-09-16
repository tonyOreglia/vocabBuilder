package com.example.tonyoreglia.vocabbuilder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DisplayDefinitionActivity extends AppCompatActivity {

    TextView definitionView;
    TextView wordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("INFO", "Inside new activity creation 1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_definition);
        Log.i("INFO", "Inside new activity creation 2");

        Intent intent = getIntent();
        String word = intent.getStringExtra("com.example.tonyoreglia.vocabbuilder.word");
        String definition = intent.getStringExtra("com.example.tonyoreglia.vocabbuilder.definition");
        if (definition != null) {
            Log.i("INFO", "Setting definition view");
            definitionView = (TextView) findViewById(R.id.definitionView);
            definitionView.setText(definition);
            Log.i("INFO", "definition view set successfully");
        }
        else {
            definitionView.setText("Unable to retrieve definition, may need to connect to internet");
        }
        if (word != null) {
            Log.i("INFO", "Setting word view");
            wordView = (TextView) findViewById(R.id.wordView);
            wordView.setText(word + ":");
        }
        else {
            wordView.setText("Error. Unable to retrieve word from Database.");
            Log.i("INFO", "Unable to retrieve word from Database.");
        }
        //

    }
}
