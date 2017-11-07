/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spam.servlets;

import com.spam.entity.Word;
import com.spam.training.BadTrainingSet;
import com.spam.training.GoodTrainingSet;
import com.spam.training.interfaces.ITrainingSet;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Girase
 */
public class UpdateTraingSetServlet extends HttpServlet {

    HashMap<String, Word> wordMap;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            wordMap = new LinkedHashMap<String, Word>();
            
            System.out.println("UpdateTrainingSetServlet Post method called!!!");
            // Initialize Bad Training Set. . .
            ITrainingSet trainingSet = new BadTrainingSet();
            wordMap = trainingSet.train(wordMap);
            System.out.println("BadTrainngSet :"+wordMap.size());
            
            // Initialize Good Training Set. . .
            trainingSet = new GoodTrainingSet();
            wordMap = trainingSet.train(wordMap);
            System.out.println("GoodTrainngSet :"+wordMap.size());

            response.sendRedirect("login.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
