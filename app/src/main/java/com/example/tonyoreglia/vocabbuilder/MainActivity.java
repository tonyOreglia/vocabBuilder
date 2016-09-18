package com.example.tonyoreglia.vocabbuilder;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

//JSON parser libraryies
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class MainActivity extends AppCompatActivity {
    //API variables
    static final String API_KEY = "SmP6Z0l2s4msh4a3zK8A5AAfwELcp1RWStEjsnE1JXe3FeAP7W";

    ArrayList<String> words = new ArrayList<String>();

    MyDBHandler dbHandler;
    EditText wordInput;

    ListView tonysListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        wordInput = (EditText) findViewById(R.id.wordInput);

        dbHandler = new MyDBHandler(this, null, null, 1);
        //printDataBase();
        displayWordsInList();

        tonysListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
        words.clear();
        String[] result = dbHandler.databaseToString().split("\\n");
        for (int x=0; x<result.length; x++) {
            words.add(result[x]);
            //System.out.println(result[x]);
        }

        //display string arraylist of words from database
        ListAdapter tonysAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, words);
        tonysListView = (ListView) findViewById(R.id.tonysListView);
        tonysListView.setAdapter(tonysAdapter);
        wordInput.setText("");
    }

    //add word to database
    public void addWordButtonClicked(View view) {
        Word word = new Word(wordInput.getText().toString());
        //set definition of word object here by calling API
        //dbHandler.addWord(word);
        Log.i("INFO", "I'm here in addWordButtonClicked");
        new RetrieveFeedTask().execute(word.get_word());

    }

    //delete word from database
    public void removeWordButtonClicked(View view) {
        String inputText = wordInput.getText().toString();
        dbHandler.deleteWord(inputText);
        displayWordsInList();
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
                //Log.i("INFO", apiUrl);
                //URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) apiUrl.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("X-Mashape-Key", API_KEY);
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
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
                return null;
            }
        }

        protected void onPostExecute(String response) {

            if (response == null) {
                response = "THERE WAS AN ERROR IN POST EXECUTE";
            }
            Log.i("INFO", response);

            Word wordDefinitions = parseJson(response);

            //dbHandler.updateWord(wordObject);

            dbHandler.addWord(wordDefinitions);

            displayWordsInList();
        }

        public Word parseJson(String jsonDefinition) {
            try {
                JSONObject definitions = (JSONObject) new JSONTokener(jsonDefinition).nextValue();
                Log.i("INFO", "Inside JSON Parser");
                Log.i("INFO", definitions.toString());

                String word = definitions.optString("word").toString();
                Word wordDefinitions = new Word(word);
                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonDefinitionArray = definitions.optJSONArray("definitions");
                for(int i = 0; i < jsonDefinitionArray.length() && (i < 3); i++) {
                    JSONObject jsonDefinitionObject = jsonDefinitionArray.getJSONObject(i);
                    String definition = jsonDefinitionObject.optString("definition").toString();
                    String partOfSpeech = jsonDefinitionObject.optString("partOfSpeech").toString();
                    wordDefinitions.add_definition(definition);
                    wordDefinitions.add_partOfSpeech(partOfSpeech);

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
