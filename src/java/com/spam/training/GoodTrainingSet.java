/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spam.training;

import com.spam.entity.Word;
import com.spam.training.interfaces.ITrainingSet;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Girase
 */
public class GoodTrainingSet implements ITrainingSet {

    ResourceBundle customize = ResourceBundle.getBundle("customize", Locale.getDefault());
    String splitregex;
    Pattern wordregex;

    public GoodTrainingSet() {
        splitregex = "\\W";
        wordregex = Pattern.compile("\\w+");
    }

    public HashMap<String, Word> train(HashMap<String, Word> wordMap) {
        try {
            String goodTrainingSetFile = customize.getString("trainingSetHomeDir") + customize.getString("goodTrainingSetFile");
            String content = FileUtils.readFileToString(new File(goodTrainingSetFile));
            String[] tokens = content.split(splitregex);
            int totalGoodWords = 0;

            // For every word token
            for (int i = 0; i < tokens.length; i++) {
                String word = tokens[i].toLowerCase();
                Matcher m = wordregex.matcher(word);
                if (m.matches()) {
                    totalGoodWords++;

                    if (wordMap.containsKey(word)) // If it exists in the HashMap already, Increment the count
                    {
                        Word w = (Word) wordMap.get(word);
                        w.countGood();
                    } else // Otherwise it's a new word so add it
                    {
                        Word w = new Word(word);
                        w.countGood();
                        wordMap.put(word, w);
                    }
                }
            }

            File file = new File(customize.getString("trainingSetHomeDir") + customize.getString("goodTrainSet"));
            file.delete();
            
            for (Map.Entry<String, Word> entry : wordMap.entrySet()) {
                String key = entry.getKey();
                Word word = entry.getValue();
                float value = word.calcGoodProb(totalGoodWords);

                writeContent(customize.getString("trainingSetHomeDir") + customize.getString("goodTrainSet"), key + " " + value +" "+word.getCountGood());
                // goodWordMap.put(key, word.calcGoodProb(totalGoodWords));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return wordMap;
    }

    public void writeContent(String filename, String content) throws IOException {
        FileWriter fw = new FileWriter(filename, true); //the true will append the new data
        fw.write(content);//appends the string to the file
        fw.write(System.getProperty( "line.separator" ));
        fw.close();
    }
}
