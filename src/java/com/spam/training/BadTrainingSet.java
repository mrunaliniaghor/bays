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
public class BadTrainingSet implements ITrainingSet {

    ResourceBundle customize = ResourceBundle.getBundle("customize", Locale.getDefault());
    String splitregex;
    Pattern wordregex;

    public BadTrainingSet() {
        splitregex = "\\W";
        wordregex = Pattern.compile("\\w+");
    }

    public HashMap<String, Word> train(HashMap<String, Word> wordMap) {
        try {
            String badTrainingSetFile = customize.getString("trainingSetHomeDir") + customize.getString("spamTrainingSetFile");
            String content = FileUtils.readFileToString(new File(badTrainingSetFile));

            String[] tokens = content.split(splitregex);
            int totalSpamWords = 0;

            // For every word token
            for (int i = 0; i < tokens.length; i++) {
                String word = tokens[i].toLowerCase();
                Matcher m = wordregex.matcher(word);
                if (m.matches()) {
                    totalSpamWords++;

                    if (wordMap.containsKey(word)) // If it exists in the HashMap already Increment the count
                    {
                        Word w = (Word) wordMap.get(word);
                        w.countBad();

                    } else // Otherwise it's a new word so add it
                    {
                        Word w = new Word(word);
                        w.countBad();
                        wordMap.put(word, w);
                    }
                }
            }
            
            File file = new File(customize.getString("trainingSetHomeDir") + customize.getString("badTrainSet"));
            file.delete();
            
            for (Map.Entry<String, Word> entry : wordMap.entrySet()) {
                String key = entry.getKey();
                Word word = entry.getValue();
                // float value = word.calcBadProb(totalSpamWords);
                
                // System.out.println(entry.getKey() +" : "+word.calcBadProb(totalSpamWords));
                writeContent(customize.getString("trainingSetHomeDir") + customize.getString("badTrainSet"), key + " " + word.calcBadProb(totalSpamWords) + " "+word.getCountBad());
                // badWordMap.put(key, word.calcBadProb(totalSpamWords));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return wordMap;
    }

    public void writeContent(String filename, String content) throws IOException {
        try {
            FileWriter fw = new FileWriter(filename, true); //the true will append the new data
            fw.write(content);//appends the string to the file
            fw.write(System.getProperty( "line.separator" ));
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
