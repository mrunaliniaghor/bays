/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spam.servlets;

import com.spam.bayes.BayesianFiltering;
import com.spam.entity.Mail;
import com.spam.entity.User;
import com.spam.textextraction.EmailReaderService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Store;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Girase 
 */
public class LoginServlet extends HttpServlet {
//dopost serves the request
   
    @Override
   public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //ServletException: Defines a general exception a servlet can throw when it encounters difficulty
       
        List<Mail> spamMailList = new ArrayList<Mail>();
        List<Mail> goodMailList = new ArrayList<Mail>();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (authenticate(username, password)) {
            User user = new User(); //create an entity user
            user.setUsername(username);
            user.setPassword(password);

            List<Mail> allMailList = new ArrayList<Mail>();
            EmailReaderService emailReaderService = new EmailReaderService();
            allMailList = emailReaderService.readInboxMails(username, password);

            BayesianFiltering bayesianFiltering = new BayesianFiltering();

            int spamCount = 0;
            float pspam = 0;
            boolean spam = false;
            PrintWriter out = response.getWriter();
            for (Mail mail : allMailList) {
                // System.out.println("Mail From :" + mail.getFrom());
                pspam = bayesianFiltering.analyze(mail.getContent());

                // If the computed value is greater than 0.9 we have a Spam!!
                if (pspam >= 0.9f) {
                    spam = true;
                } else {
                    spam = false;
                }

                mail.setpSpam(pspam);
                if (spam) {
                    spamMailList.add(mail);
                    System.out.println("Mail is Spam");
                    spamCount++;
                } else {
                    goodMailList.add(mail);
                    System.out.println("Mail is not spam");
                }
            }

            System.out.println("SpamMessages Count :" + spamCount);


            HttpSession session = request.getSession();
            session.setAttribute("spamMailList", spamMailList);
            session.setAttribute("goodMailList", goodMailList);

            request.setAttribute("user", user);
            response.sendRedirect("inbox.jsp");
        }
        
        else {
            HttpSession session = request.getSession();
            session.setAttribute("msg", "Incorrect username or password");
          
            request.getRequestDispatcher("login.jsp").forward(request, response);
            PrintWriter out = response.getWriter();
            out.print("Login Failed!!");
        }
    }

    public boolean authenticate(String username, String password) {
        boolean authenticate = false;
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            // Authenticate the Email.
            Session session = Session.getInstance(props, null);
            Store store = session.getStore();
            store.connect("imap.gmail.com", username, password);
            authenticate = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return authenticate;
    }
}