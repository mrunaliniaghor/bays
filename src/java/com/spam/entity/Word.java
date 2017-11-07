/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spam.entity;

/**
 *
 * @author Girase
 */
public class Word {

    private String word;    // The String itself
    private int countBad;   // The total times it appears in "bad" messages 
    private int countGood;  // The total times it appears in "good" messages
    private float rBad;     // bad count / total bad words
    private float rGood;    // good count / total good words
    private float pSpam;    // probability this word is Spam

    // Create a word, initialize all vars to 0
    public Word(String s) {
        word = s;
        countBad = 0;
        countGood = 0;
        rBad = 0.0f;
        rGood = 0.0f;
        pSpam = 0.0f;
    }

    // Increment bad counter
    public void countBad() {
        countBad++;
    }

    // Increment good counter
    public void countGood() {
        countGood++;
    }

    // Compute how often this word is bad
    public float calcBadProb(int total) {
        // System.out.println("BadWord :"+word +" : rBad :"+rBad +" :countBad :"+countBad + " :Total :"+total);
        if (total > 0) {
            rBad = countBad / (float) total;
        }
        return rBad;
    }

    // Compute how often this word is good
    public float calcGoodProb(int total) {
        //System.out.println("GoodWord :"+word +" : rGood :"+rGood +" :countGood :"+countGood + " :Total :"+total);
        if (total > 0) {
            rGood = 2 * countGood / (float) total; // multiply 2 to help fight against false positives (via Graham)
        }
        return rGood;
    }

    // Implement bayes rules to computer how likely this word is "spam"
    public void finalizeProb() {
        if (rGood + rBad > 0) {
            pSpam = rBad / (rBad + rGood);
        }
        
        // System.out.println("Word :"+word +"RGood :"+rGood + " :rBad :"+rBad +" pSpam :"+pSpam);
        if (pSpam < 0.01f) {
            pSpam = 0.01f;
        } else if (pSpam > 0.99f) {
            pSpam = 0.99f;
        }
    }

    // The "interesting" rating for a word is
    // How different from 0.5 it is
    public float interesting() {
        return Math.abs(0.5f - pSpam);
    }

    // Some getters and setters	
    public float getPGood() {
        return rGood;
    }

    public float getPBad() {
        return rBad;
    }

    public float getPSpam() {
        return pSpam;
    }

    public void setPSpam(float f) {
        pSpam = f;
    }

    public String getWord() {
        return word;
    }

    public void setCountBad(int countBad) {
        this.countBad = countBad;
    }

    public void setCountGood(int countGood) {
        this.countGood = countGood;
    }

    public int getCountBad() {
        return countBad;
    }

    public int getCountGood() {
        return countGood;
    }

    @Override
    public String toString() {
        return "Word{" + "word=" + word + ", countBad=" + countBad + ", countGood=" + countGood + ", pSpam=" + pSpam + '}';
    }
}
