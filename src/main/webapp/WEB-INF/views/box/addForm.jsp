<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

    <title>Vinyl Collection - Add to Box</title>

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
            <div class="col-lg-5 d-none d-lg-block bg-addBox-image"></div>
            <div class="col-lg-7">
              <div class="p-5">

                <form action="/box/addAlbum/${album.discogsId}">
                <div style="margin-bottom: 10px">
                  <select name="boxId" class="selectBox" id="tracklist" >
                    <option value="0" hidden  selected >Pick a Box</option>
                    <c:forEach items="${boxes}" var="e">
                      <option value="${e.id}">${e.name}</option>
                    </c:forEach>
                  </select>
                </div>
                <button  class="btn btn-primary btn-user btn-block" style="border-radius: 25px; font-size: small; width: 530px;
                           height: 45px" type="submit">
                  Add
                </button>
                <div class="text-center" style="margin-bottom: 50px; margin-top: 50px">
                 <hr />
                </div>
               </form>

                <form class="user" action="<c:url value="/box/addCreate/${album.discogsId}" />">
                  <div class="form-group row">
                    <div class="" style="width: 530px; height: 45px; margin-left: 8px">

                      <input
                        type="text"
                        class="form-control form-control-user"
                        id="exampleFirstName"
                        placeholder="Box Name"
                        name="name"
                      />
                    </div>


                  </div>
                  <button  class="btn btn-primary btn-user btn-block" type="submit">
                   Create & Add
                  </button>

                  <hr />
                </form>



                  <div class="text-center">
                    <a class="small" href="<c:url value="/box/boxes" />"
                    >Back</a
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
