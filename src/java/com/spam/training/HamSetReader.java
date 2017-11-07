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
public class HamSetReader implements ISetReader {
    
    ResourceBundle customize = ResourceBundle.getBundle("customize", Locale.getDefault());
    
    @Override
    public HashMap<String, Word> train(HashMap<String, Word> wordMap) {
        try {
            File file = new File(customize.getString("trainingSetHomeDir") + customize.getString("goodTrainSet"));
            BufferedReader br = new BufferedReader(new FileReader(file));
            
            int totalGoodWords=0;
            String line = null;
            while((line = br.readLine())!=null) {
                if(line.contains(" ")) {
                    // totalGoodWords++;
                    String gudWord = line.split(" ")[0];
                    String rGood = line.split(" ")[2];
                    if (wordMap.containsKey(gudWord)) // If it exists in the HashMap already, Increment the count
                    {
                        Word w = (Word) wordMap.get(gudWord);
                        w.setCountGood(Integer.parseInt(rGood));
                        totalGoodWords = totalGoodWords + Integer.parseInt(rGood);
                        // w.countGood();
                    } else // Otherwise it's a new word so add it
                    {
                        Word w = new Word(gudWord);
                        w.setCountGood(Integer.parseInt(rGood));
                        // w.countGood();
                        totalGoodWords = totalGoodWords + Integer.parseInt(rGood);
                        wordMap.put(gudWord, w);
                    }
                }
            }
            
            // System.out.println("TotalGoodWords :"+totalGoodWords);
            // System.out.println("GoodwordMap size :"+wordMap.size());

            for (Map.Entry<String, Word> entry : wordMap.entrySet()) {
                Word word = entry.getValue();
                word.calcGoodProb(totalGoodWords);
                
                wordMap.put(entry.getKey(), word);
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return wordMap;
    }
    
    public static void main(String[] args) {
        HashMap<String,Word> wordMap = new HashMap<String, Word>();
        HamSetReader hamSetReader = new HamSetReader();
        hamSetReader.train(wordMap);
    }
}
