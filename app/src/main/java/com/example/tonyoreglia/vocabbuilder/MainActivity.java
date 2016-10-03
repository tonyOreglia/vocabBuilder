package com.example.tonyoreglia.vocabbuilder;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.AdapterView;
import android.util.Log;
import java.util.ArrayList;

//API integration libraries
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.AsyncTask;
import android.widget.Toast;

//JSON parser libraryies
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class MainActivity extends AppCompatActivity {
    //API variables
    static final String API_KEY = "SmP6Z0l2s4msh4a3zK8A5AAfwELcp1RWStEjsnE1JXe3FeAP7W";
    static final String API_ERROR = "UNABLE TO PULL DEFINITION";
    ArrayList<String> words = new ArrayList<String>();

    MyDBHandler dbHandler;
    EditText wordInput;

    //ListView tonysListView;
    ListView customListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        wordInput = (EditText) findViewById(R.id.wordInput);

        dbHandler = new MyDBHandler(this, null, null, 1);

//
//        ListView list = (ListView) findViewById(R.id.listview);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Object listItem = list.getItemAtPosition(position);
//            }
//        });
        customListView = (ListView) findViewById(R.id.customListView);

        //printDataBase();
        displayWordsInList();

        customListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.i("INFO", "Inside Click Listener");

                        Object listItem = customListView.getItemAtPosition(position);
                        String word = String.valueOf(parent.getItemAtPosition(position));
                        String definition = dbHandler.databaseDefinitionToString(word);

                        Log.i("INFO", "Just pulled definition from database");
                        Log.i("INFO", word);
                        if(definition.equals("")) {
                            Log.i("INFO","definition is empty");
                        }
                        Log.i("INFO", definition);
                        Intent intent;
                        intent = new Intent(MainActivity.this, DisplayDefinitionActivity.class);
                        intent.putExtra("com.example.tonyoreglia.vocabbuilder.word", word);
                        intent.putExtra("com.example.tonyoreglia.vocabbuilder.definition", definition);
                        startActivity(intent);
                    }
                }
        );

    }

    public void displayWordsInList() {
        String temp = "";
        words.clear();
        String[] result = dbHandler.databaseToString().split("\\n");
        for (int x=0; x<result.length; x++) {
            if(result[x].equals(temp)) {
                continue;
            }
            words.add(result[x]);
            temp = result[x];
            //System.out.println(result[x]);
        }

        //display string arraylist of words from database
        //ListAdapter tonysAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, words);
        ListAdapter customListAdapter = new customAdapter(this, words);// Pass the food arrary to the constructor.

        //tonysListView = (ListView) findViewById(R.id.tonysListView);
        customListView = (ListView) findViewById(R.id.customListView);

        //tonysListView.setAdapter(tonysAdapter);
        customListView.setAdapter(customListAdapter);

        wordInput.setText("");
    }

    //add word to database
    public void addWordButtonClicked(View view) {
        Log.i("INFO", "I'm here in addWordButtonClicked");
        String input = wordInput.getText().toString();
        if(input.equals("")) {
            displayToast("Type a word to add");
        }
        else {
            Word word = new Word(input.trim());
            if (!dbHandler.checkForWord(word.get_word())) {
                new RetrieveFeedTask().execute(word.get_word());
            }
            else {
                wordInput.setText("");
                Log.i("INFO", "Word already in list");
                displayToast(word.get_word() + " is already in your list.");
            }
        }
    }

    //delete word from database
//    public void removeWordButtonClicked(View view) {
//
//        String inputText = wordInput.getText().toString();
//        if (inputText.equals("")) {
//            displayToast("Type a word to remove");
//        }
//        else {
//            dbHandler.deleteWord(inputText);
//            displayWordsInList();
//        }
//    }

    public void displayToast(String message) {
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
        }

        protected String doInBackground(String... word) {
            Log.i("INFO", "I'm here in background");
            try {
                String url = "https://wordsapiv1.p.mashape.com/words/" + word[0] + "/definitions";

                //String url = "https://wordsapiv1.p.mashape.com/words/stoic/definitions";
                URL apiUrl = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) apiUrl.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("X-Mashape-Key", API_KEY);
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        //Log.i("INFO", "Appending input to line: " + line);
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                //Log.i("INFO", "error message: " + e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                //response = API_ERROR;
                Log.i("INFO", "Here in onPostExecute. Response is null");
                displayToast("Unable to find definition. Please connect to internet, check spelling, and try again.");
                wordInput.setText("");
//                Context context = getApplicationContext();
//                CharSequence text = "Unable to find definition. Please connect to internet and try again.";
//                int duration = Toast.LENGTH_SHORT;
//
//                Toast toast = Toast.makeText(context, text, duration);
//                toast.show();
            }
            else {
                Log.i("INFO", "Here in onPostExecute. There is some response");
                Log.i("INFO", response);
                Word wordDefinitions = parseJson(response);
                dbHandler.addWord(wordDefinitions);
                //dbHandler.addWord(wordDefinitions);
                displayWordsInList();
            }
        }

        public Word parseJson(String jsonDefinition) {
//            if(jsonDefinition.equals(API_ERROR)) {
//
//            }
            try {
                JSONObject definitions = (JSONObject) new JSONTokener(jsonDefinition).nextValue();
                Log.i("INFO", "Inside JSON Parser");
                Log.i("INFO", definitions.toString());

                String word = definitions.optString("word").toString();
                Word wordDefinitions = new Word(word);
                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonDefinitionArray = definitions.optJSONArray("definitions");
                for(int i = jsonDefinitionArray.length()-1; i >= 0 && i >= jsonDefinitionArray.length() - 3; i--) {
                    JSONObject jsonDefinitionObject = jsonDefinitionArray.getJSONObject(i);
                    String definition = jsonDefinitionObject.optString("definition").toString();
                    String partOfSpeech = jsonDefinitionObject.optString("partOfSpeech").toString();
                    wordDefinitions.add_definition(definition);
                    wordDefinitions.add_partOfSpeech(partOfSpeech);
                    Log.i("INFO", "definition " + i + ". " + wordDefinitions.get_definition());
                }
                //return new String[] {definition, word};
                return wordDefinitions;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
