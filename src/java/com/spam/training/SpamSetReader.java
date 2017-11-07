/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spam.training;

import com.spam.entity.Word;
import com.spam.training.interfaces.ISetReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 *
 * @author Girase
 */
public class SpamSetReader implements ISetReader {

    ResourceBundle customize = ResourceBundle.getBundle("customize", Locale.getDefault());
    
    @Override
    public HashMap<String, Word> train(HashMap<String, Word> wordMap) {
        try {
            File file = new File(customize.getString("trainingSetHomeDir") + customize.getString("badTrainSet"));
            BufferedReader br = new BufferedReader(new FileReader(file));
            
            int totalBadWords=0;
            String line = null;
            while((line = br.readLine())!=null) {
                if(line.contains(" ")) {
                    // totalBadWords++;
                    String badWord = line.split(" ")[0];
                    String rBad = line.split(" ")[2];
                    if (wordMap.containsKey(badWord)) // If it exists in the HashMap already, Increment the count
                    {
                        Word w = (Word) wordMap.get(badWord);
                        w.setCountBad(Integer.parseInt(rBad));
                        totalBadWords = totalBadWords + Integer.parseInt(rBad);
                        // w.countBad();
                    } else // Otherwise it's a new word so add it
                    {
                        Word w = new Word(badWord);
                        w.setCountBad(Integer.parseInt(rBad));
                        // w.countBad();
                        totalBadWords = totalBadWords + Integer.parseInt(rBad);
                        wordMap.put(badWord, w);
                    }
                }
            }
            
            // System.out.println("TotalBadWords :"+totalBadWords);
            // System.out.println("BadwordMap size :"+wordMap.size());

            for (Map.Entry<String, Word> entry : wordMap.entrySet()) {
                Word word = entry.getValue();
                word.calcBadProb(totalBadWords);
                
                wordMap.put(entry.getKey(), word);
                // System.out.println(entry.getKey() +" : "+value);
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return wordMap;
    }
    
    public static void main(String[] args) {
        HashMap<String,Word> wordMap = new HashMap<String, Word>();
        SpamSetReader spamSetReader = new SpamSetReader();
        spamSetReader.train(wordMap);
    }
    
    
}
