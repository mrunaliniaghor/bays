<%-- 
    Document   : login
    Created on : Mar 29, 2014, 10:46:28 PM
    Author     : Tribbianis
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; UTF-8">
        <title>Login</title>
        <link rel="stylesheet" type="text/css" href="css/login.css" />
        <link rel="stylesheet" type="text/css" href="css/body.css" />
        <link rel='stylesheet' type='text/css' HREF="<%=request.getContextPath()%>/css/bootstrap.css"/>
        <link rel='stylesheet' type='text/css' HREF="<%=request.getContextPath()%>/css/bootstrap-responsive.css"/>
        <script language="javascript">
            function validate(objForm){

                if(objForm.username.value.length==0){
                    alert("Enter Username");
                    objForm.username.focus();
                    return false;
                }

                if(objForm.password.value.length==0){
                    alert("Enter Password");
                    objForm.password.focus();
                    return false;
                }

                return true;
            }
        </script>

        <script type="text/javascript">
            function back_block()
            {
                window.history.foward(1);
            }
        </script>

    </head>
    <body style="background-color:lightskyblue">
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
        <div id="wrapper">
            <form name="login-form" class="login-form" action="LoginServlet" method="post" onsubmit="return validate(this);">
                <div class="header">
                    <span></span>
                </div>
                <div class="content">
                    <input name="username" type="text" class="input username" style="font-family: verdana;font-size: 15px" placeholder="Username" value=""/>
                    <div class="user-icon"></div>
                    <input name="password" type="password" class="input password" style="font-family: verdana;font-size: 15px" placeholder="Password" value=""/>
                    <div class="pass-icon"></div>		
                </div>

                <div class="footer">
                    <input type="submit" name="submit" value="Login" style="font-family: verdana;font-size: 15px" class="button" />
                </div>
            </form>
            <form name="login-form" class="login-form" action="UpdateTraingSetServlet" method="post">
                <div class="footer">
                    <input type="submit" name="update" value="Update Training Set" style="font-family: verdana;font-size: 15px" class="button" />
                </div>
            </form>
            <div style="padding-top: 5px"></div>
        </div>
        <div class="gradient"></div>
    </body>
</html>