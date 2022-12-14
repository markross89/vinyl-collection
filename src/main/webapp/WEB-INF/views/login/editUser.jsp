<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta
      name="viewport"
      content="width=device-width, initial-scale=1, shrink-to-fit=no"
    />
    <meta name="description" content="" />
    <meta name="author" content="" />

    <title>Vinyl Collection - Register</title>

    <!-- Custom fonts for this template-->
    <link
            href="<c:url value="/resources/html-css/vendor/fontawesome-free/css/all.min.css" />"
      rel="stylesheet"
      type="text/css"
    />
    <link
      href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
      rel="stylesheet"
    />

    <!-- Custom styles for this template-->
    <link href="<c:url value="/resources/html-css/css/sb-admin-2.min.css" />" rel="stylesheet">
  </head>

  <body class="bg-gradient-primary">
    <div class="container">
      <div class="card o-hidden border-0 shadow-lg my-5">
        <div class="card-body p-0">
          <!-- Nested Row within Card Body -->
          <div class="row">
            <div class="col-lg-5 d-none d-lg-block bg-register-image"></div>
            <div class="col-lg-7">
              <div class="p-5">
                <div class="text-center">
                  <h1 class="h4 text-gray-900 mb-4">Edit Your Profile</h1>
                </div>
                <form:form  class="user" modelAttribute="user" method="post">

                  <div class="form-group row">
                      <div class="col-sm-6 mb-3 mb-sm-0">
                        <form:hidden path="id" value="${user.id}" />
                          <form:input path="firstName" class="form-control form-control-user" />
                        <form:errors path="firstName" Class="markus-error"  element="div"/>

                      </div>
                      <div class="col-sm-6">
                          <form:input path="lastName" class="form-control form-control-user" placeholder="${user.lastName}"/>
                        <form:errors path="lastName" Class="markus-error" />
                      </div>
                  </div>
                  <div class="form-group">
                          <form:input path="username" class="form-control form-control-user" placeholder="${user.username}"/>
                    <form:errors path="username" Class="markus-error" />
                  </div>
                  <div class="form-group ">

                          <form:password path="password" class="form-control form-control-user" placeholder="${user.password}"/>
                    <form:errors path="password" Class="markus-error" />


                  </div>
                    <button  class="btn btn-primary btn-user btn-block" type="submit" value="Save">Save Changes</button>

                </form:form>

                <hr/>

                  <div class="text-center">
                    <a class="small" href="<c:url value="/home/start" />"
                    >Back Home</a
                    >
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Bootstrap core JavaScript-->
    <script src="vendor/jquery/jquery.min.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

    <!-- Core plugin JavaScript-->
    <script src="vendor/jquery-easing/jquery.easing.min.js"></script>

    <!-- Custom scripts for all pages-->
    <script src="js/sb-admin-2.min.js"></script>
  </body>
</html>
