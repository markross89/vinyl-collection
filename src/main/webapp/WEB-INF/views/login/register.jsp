
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form method="post">
    <div class="container">
        <h1>Register</h1>
        <p>Please fill in this form to create an account.</p>
        <hr>

        <label for="f"><b>First Name</b></label>
        <input type="text" placeholder="Enter first name" name="firstName" id="f" required>

        <label for="s"><b>Second Name</b></label>
        <input type="text" placeholder="Enter second name" name="secondName" id="s" required>

        <label for="username"><b>Email</b></label>
        <input type="text" placeholder="Enter Email/Username" name="username" id="username" required>

        <label for="psw"><b>Password</b></label>
        <input type="password" placeholder="Enter Password" name="psw" id="psw" required>

        <label for="psw-repeat"><b>Repeat Password</b></label>
        <input type="password" placeholder="Repeat Password" name="psw-repeat" id="psw-repeat" required>
        <hr>

        <p>By creating an account you agree to our <a href="#">Terms & Privacy</a>.</p>
        <button type="submit" class="registerbtn">Register</button>
    </div>

    <div class="container signin">
        <p>Already have an account? <a href="/security/login">Sign in</a>.</p>
    </div>
</form>
</body>
</html>
