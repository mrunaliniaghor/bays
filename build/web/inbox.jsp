<%-- 
    Document   : inbox
    Created on : Apr 13, 2014, 7:29:33 PM
    Author     : Tribbianis
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="com.spam.entity.Mail"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel='stylesheet' type='text/css' HREF="<%=request.getContextPath()%>/css/style.css"/>
        <link rel='stylesheet' type='text/css' HREF="<%=request.getContextPath()%>/css/bootstrap.css"/>
        <link rel='stylesheet' type='text/css' HREF="<%=request.getContextPath()%>/css/bootstrap-responsive.css"/>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7/jquery.js"></script>
        <script type="text/javascript" src=<%=request.getContextPath() + "/js/bootstrap.js"%>></script>

        <script type="text/javascript">
            function showMailContent(mailID) {

                if (navigator.appName.indexOf("Microsoft") > -1) {
                    var Ele = document.getElementById(mailID);
                    if (Ele.style.display == "block") {
                        Ele.style.display = "none";
                    }
                    else {
                        Ele.style.display = "block";
                    }
                } else {
                    var Ele = document.getElementById(mailID);
                    if (Ele.style.display == "table-row") {
                        Ele.style.display = "none";
                    }
                    else {
                        Ele.style.display = "table-row";
                    }
                }
                
                document.getElementById(mailID).click();
            }
        </script>

        <title>Inbox</title>
    </head>
    <body>

        <%
            List<Mail> spamMailList = (List<Mail>) session.getAttribute("spamMailList");
            List<Mail> goodMailList = (List<Mail>) session.getAttribute("goodMailList");

            System.out.println("GoodMailList :" + goodMailList.size());
            System.out.println("SpamMailList :" + spamMailList.size());
            DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
        %>

        <div class="top-nav">
            <div class="container-fluid">
                <div class="nav-collapse collapse">
                    <ul class="nav nav-tabs" style="background-color: black;font-family: verdana;">
                        <li><a href="LogoutServlet">Logout</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div style="padding-top: 20px"></div>
        <div class="container-fluid">
            <div class="row-fluid" style="font-family: calibri">
                <div class="span1"></div>
                <div class="span10" style="text-align: center;color: white">
                    <h3>WELCOME TO EMAIL SPAMMING FILTERING USING BAYESIAN ALGORITHM</h3>
                </div>
                <div class="span1"></div>
            </div>
        </div>
        <div class="container-fluid">
            <div class="row-fluid">
                <div class="span1"></div>
                <div class="span10">
                    <div id="content" style="font-family: verdana;color: black">
                        <ul id="tabs" class="nav nav-tabs" data-tabs="tabs">
                            <li class="active"><a href="#inbox" data-toggle="tab">Inbox</a></li>
                            <li><a href="#spam" data-toggle="tab">Spam Mails</a></li>
                        </ul>
                        <div id="my-tab-content" class="tab-content" style="font-family: verdana;"> 
                            <div class="tab-pane active fade in" id="inbox">
                                <% if (goodMailList != null && goodMailList.size() > 0) {%>
                                <table class="table" style="background-color: lightgray">
                                    <tr style="background-color: gray">
                                        <th>Sender</th>
                                        <th>Subject</th>
                                        <th>Sent</th>
                                        <th>Spam Probability</th>
                                        <th></th>
                                    </tr>
                                    <%
                                        int mailID = 1;
                                        for (Mail goodMail : goodMailList) {%>
                                    <tr>
                                        <td style="max-width: 180px"><%=goodMail.getFrom()%></td>
                                        <td><%=goodMail.getSubject()%></td>
                                        <td><%=df.parse(goodMail.getSent())%></td>
                                        <td><%=goodMail.getpSpam()%></td>
                                        <td><a href="#" onclick="showMailContent('Good<%=mailID%>');">Show</a></td>
                                    </tr>
                                    <tr id="Good<%=mailID%>" style="display: none;background-color: white"> 
                                        <td></td>
                                        <td colspan="4">
                                            <%=goodMail.getContent()%>
                                        </td>
                                    </tr>
                                    <%
                                            mailID++;
                                        }
                                    %>
                                </table>

                                <% } else {%>
                                <div><h5>No Good Mails found in Mailbox.</h5></div>
                                <% }%>
                            </div>
                            <div class="tab-pane fade in" id="spam">
                                <% if (spamMailList != null && spamMailList.size() > 0) {%>
                                <table class="table" style="background-color: lightgray">
                                    <tr style="background-color: gray">
                                        <th>Sender</th>
                                        <th>Subject</th>
                                        <th>Sent</th>
                                        <th>Spam Probability</th>
                                        <th></th>
                                    </tr>
                                    <% int mailID = 1;
                                        for (Mail badMail : spamMailList) {%>
                                    <tr>
                                        <td style="max-width: 180px"><%=badMail.getFrom()%></td>
                                        <td><%=badMail.getSubject()%></td>
                                        <td><%=df.parse(badMail.getSent())%></td>
                                        <td><%=badMail.getpSpam()%></td>
                                        <td><a href="#" onclick="showMailContent('Spam<%=mailID%>');">Show</a></td>
                                    </tr>
                                    <tr id="Spam<%=mailID%>" style="display: none;background-color: white"> 
                                        <td></td>
                                        <td colspan="4">
                                            <%=badMail.getContent()%>
                                        </td>
                                    </tr>
                                    <%
                                            mailID++;
                                        }
                                    %>
                                </table>

                                <% } else {%>
                                <div><h5>No Spam Mails found in Mailbox.</h5></div>
                                <% }%>
                            </div>
                        </div>
                    </div>
                    <div class="span1"></div>
                </div>
            </div>

            <script type="text/javascript">
                jQuery(document).ready(function ($) {
                    $('#tabs').tab();
                });
            </script>
    </body>
</html>
