package com.example.tonyoreglia.vocabbuilder;

/**
 * Created by tonyoreglia on 9/09/16.
 */
public class Word {
    public Word(String _word) {
        this._word = _word.substring(0, 1).toUpperCase() + _word.substring(1).toLowerCase();
        this._definition = "";
        //this._partOfSpeech = "";
        //set definition here
    }
    public Word(String _word, String _definition) {
        this._word = _word.substring(0, 1).toUpperCase() + _word.substring(1).toLowerCase();
        this._definition = _definition;
        //this._partOfSpeech = "";
    }

    public Word() {
    }

    int _id;
    String _word;
    String _definition;
    String _partOfSpeech;
    String _example;
    //String[] _definition;
    //String[] _partOfSpeech;
    //String[] _example;


    public String get_definition() {
        return _definition;
    }

    public int get_id() {
        return _id;
    }

    public String get_word() {
        return _word;
    }

    public void set_definition(String _definition) {
        this._definition = _definition;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_word(String _word) {
        this._word = _word;
    }
}


