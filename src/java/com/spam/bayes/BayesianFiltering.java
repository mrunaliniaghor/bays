/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spam.bayes;

import com.spam.entity.Word;
import com.spam.training.BadTrainingSet;
import com.spam.training.GoodTrainingSet;
import com.spam.training.HamSetReader;
import com.spam.training.SpamSetReader;
import com.spam.training.interfaces.ISetReader;
import com.spam.training.interfaces.ITrainingSet;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Girase
 */
public class BayesianFiltering {

    ResourceBundle customize = ResourceBundle.getBundle("customize", Locale.getDefault());
    HashMap<String, Double> goodWordMap;
    HashMap<String, Double> badWordMap;
    HashMap<String, Word> wordMap;
    String splitregex;
    Pattern wordregex;

    public BayesianFiltering() {
        wordMap = new LinkedHashMap<String, Word>();

        // Initialize Spam Training Set. . .
        
        ISetReader trainingSet = new SpamSetReader();
         // ITrainingSet trainingSet = new BadTrainingSet();
        trainingSet.train(wordMap);
        System.out.println("WordMap Size after BadTraningSet :" + wordMap.size());

        // Initialize Good Training Set. . .
         trainingSet = new HamSetReader();
        // trainingSet = new GoodTrainingSet();
        trainingSet.train(wordMap);
        System.out.println("WordMap Size after GoodTraningSet :" + wordMap.size());

        finalizeTraining();

        splitregex = "\\W";
        wordregex = Pattern.compile("\\w+");
    }

    public float analyze(String stuff) {
        // Create an arraylist of 15 most "interesting" words are most interesting based on how different their Spam probability is from 0.5
        ArrayList interesting = new ArrayList();

        // For every word in the String to be analyzed
        String[] tokens = stuff.split(splitregex);

        for (int i = 0; i < tokens.length; i++) {
            String s = tokens[i].toLowerCase();
            Matcher m = wordregex.matcher(s);

            if (m.matches()) {
                Word w;

                if (wordMap.containsKey(s)) {                   // If the String is in our HashMap get the word out
                    w = (Word) wordMap.get(s);
                } else {                        // Otherwise, make a new word with a Spam probability of 0.4;
                    w = new Word(s);
                    w.setPSpam(0.4f);
                }

                // We will limit ourselves to the 15 most interesting word
                int limit = 15;
                if (interesting.isEmpty()) {    // If this list is empty, then add this word in!
                    interesting.add(w);

                } else {                        // Otherwise, add it in sorted order by interesting level
                    for (int j = 0; j < interesting.size(); j++) {
                        Word nw = (Word) interesting.get(j);    // For every word in the list already

                        if (w.getWord().equals(nw.getWord())) {     // If it's the same word, don't bother
                            break;
                        } else if (w.interesting() > nw.interesting()) {    // If it's more interesting stick it in the list
                            interesting.add(j, w);
                            break;
                        } else if (j == interesting.size() - 1) {              // If we get to the end, just tack it on there
                            interesting.add(w);
                        }
                    }
                }

                // If the list is bigger than the limit, delete entries at the end (the more "interesting" ones are at the start of the list
                while (interesting.size() > limit) {
                    interesting.remove(interesting.size() - 1);
                }
            }
        }

        System.out.println("Stuff :"+stuff);
        System.out.println("Interesting Set :"+interesting);
        // Apply Bayes' rule (via Graham)
        float pposproduct = 1.0f;
        float pnegproduct = 1.0f;

        // For every word, multiply Spam probabilities ("Pspam") together (As well as 1 - Pspam)
        for (int i = 0; i < interesting.size(); i++) {
            Word w = (Word) interesting.get(i);
            System.out.println(w.getWord() + " " + w.getPSpam());
            pposproduct *= w.getPSpam();
            pnegproduct *= (1.0f - w.getPSpam());
        }

        // Apply formula
        float pspam = pposproduct / (pposproduct + pnegproduct);
        // System.out.println("\nSpam rating: " + pspam);

        // If the computed value is greater than 0.9 we have a Spam!!
//        if (pspam > 0.9f) {
//            return true;
//        } else {
//            return false;
//        }

        return pspam;
    }

    // For every word, calculate the Spam probability
    public void finalizeTraining() {
        Iterator iterator = wordMap.values().iterator();
        while (iterator.hasNext()) {
            Word word = (Word) iterator.next();
            word.finalizeProb();
        }
    }
}
