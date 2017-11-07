/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spam.training.interfaces;

import com.spam.entity.Word;
import java.util.HashMap;

/**
 *
 * @author Girase
 */
public interface ISetReader {
    
    public HashMap<String,Word> train(HashMap<String,Word> wordMap);
}
