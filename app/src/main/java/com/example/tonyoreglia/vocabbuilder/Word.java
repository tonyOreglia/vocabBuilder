package com.example.tonyoreglia.vocabbuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by tonyoreglia on 9/09/16.
 */
public class Word {
    public Word(String _word) {
        this._word = _word.substring(0, 1).toUpperCase() + _word.substring(1).toLowerCase();
//        this._definition.add(null);
//        this._partOfSpeech.add(null);
    }
    public Word(String _word, String _definition) {
        this._word = _word.substring(0, 1).toUpperCase() + _word.substring(1).toLowerCase();
        this._definition.add(_definition);
        //this._partOfSpeech = null;
    }

    public Word(String _word, String _definition, String _partOfSpeech) {
        this._word = _word.substring(0, 1).toUpperCase() + _word.substring(1).toLowerCase();
        this._definition.add(_definition);
        this._partOfSpeech.add(_partOfSpeech);
    }

    public Word() {
    }

    int _id;
    String _word;
    ArrayList<String> _definition = new ArrayList<String>();
    ArrayList<String> _partOfSpeech = new ArrayList<String>();

//    public String get_definition() {
//        StringBuilder allDefinitions = new StringBuilder();
//        for(int i=0; i<_definition.size(); i++) {
//            allDefinitions.append(i+1 + ". ");
//            allDefinitions.append(_definition.get(i));
//            allDefinitions.append("; ");
//            allDefinitions.append(this._partOfSpeech.get(i));
//            allDefinitions.append("\n");
//        }
//        return allDefinitions.toString();
//    }

    public ArrayList<String> get_definition() {
        return _definition;
    }

    public ArrayList<String> get_partOfSpeech() {
        return _partOfSpeech;
    }


//    public String get_partOfSpeech() {
//        StringBuilder allPartsOfSpeech = new StringBuilder();
//        for(int i=0; i<_partOfSpeech.size(); i++) {
//            allPartsOfSpeech.append(i+1 + ". ");
//            allPartsOfSpeech.append(_partOfSpeech.get(i));
//            allPartsOfSpeech.append("\n");
//        }
//        return allPartsOfSpeech.toString();
//    }

    public int get_id() {
        return _id;
    }

    public String get_word() {
        return _word;
    }

    public void add_definition(String _definition) {
        this._definition.add(_definition);
    }

    public void add_partOfSpeech(String _partOfSpeech) {
        this._partOfSpeech.add(_partOfSpeech);
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_word(String _word) {
        this._word = _word;
    }
}


