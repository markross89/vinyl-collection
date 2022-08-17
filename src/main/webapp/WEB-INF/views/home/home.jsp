<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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

    <title>Vinyl Collection - Home page</title>

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

  <body id="page-top">
    <!-- Page Wrapper -->
    <div id="wrapper">
      <ul
        class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion"
        id="accordionSidebar"
      >
        <!-- Sidebar - Brand -->
        <a
          class="sidebar-brand d-flex align-items-center justify-content-center"
          href="<c:url value="/home/start"/>"
        >
          <div class="sidebar-brand-text mx-3">Vinyl collection</div>
        </a>

        <!-- Divider -->
        <hr class="sidebar-divider my-0" />

        <!-- Divider -->
        <hr class="sidebar-divider" />

        <!-- Heading -->
        <sec:authorize access="isAuthenticated()">
        <div class="sidebar-heading">Menu</div>

        <!-- Nav Item - Pages Collapse Menu -->

        <li class="nav-item">
          <a class="nav-link" href="<c:url value="/album/albums"/>">
            <i class="fas fa-fw fa-folder"></i>
            <span>Albums</span></a
          >
        </li>

        <!-- Nav Item - Charts -->
        <li class="nav-item">
          <a class="nav-link" href="<c:url value="/track/tracks"/>">
            <i class="fas fa-fw fa-table"></i>
            <span>Songs</span></a
          >
        </li>

        <!-- Nav Item - Tables -->
        <li class="nav-item">
          <a class="nav-link" href="<c:url value="/tracklist/tracklists"/>">
            <i class="fas fa-fw fa-folder"></i>
            <span>Track lists</span></a
          >
        </li>

        <!-- Nav Item - Tables -->
        <li class="nav-item">
          <a class="nav-link" href="<c:url value="/box/boxes"/>">
            <i class="fas fa-fw fa-folder"></i>
            <span>Boxes</span></a
          >
        </li>


        <!-- Divider -->
        <hr class="sidebar-divider d-none d-md-block" />
        </sec:authorize>

        <li class="nav-item">
          <a class="nav-link" href="<c:url value="/about"/>"> <span>About us</span></a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="<c:url value="/contact"/>"> <span>Contact</span></a>
        </li>
        <sec:authorize access="isAnonymous()">
        <li class="nav-item">
          <a class="nav-link" href="<c:url value="/login"/>"> <span>Login</span></a>

        </li>
        <li class="nav-item">
          <a class="nav-link" href="<c:url value="/user/register"/>"> <span>Register</span></a>
        </li>
        </sec:authorize>
        <sec:authorize access="hasRole('ADMIN')">
          </li>
          <li class="nav-item">
            <a class="nav-link" href="<c:url value="/user/userList"/>"> <span>Users</span></a>
          </li>
        </sec:authorize>
      </ul>
      <!-- Content Wrapper -->
      <div id="content-wrapper" class="d-flex flex-column">
        <!-- Main Content -->
        <div id="content">
          <!-- Topbar -->
          <nav
            class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow"
          >
            <!-- Topbar Search -->

            <form
                    class="d-none d-sm-inline-block form-inline mr-auto ml-md-3 my-2 my-md-0 mw-100 navbar-search" action="<c:url value="/home/search"/>"
            >
              <div class="input-group">
                <input
                  type="text"
                  class="form-control bg-light border-0 small"
                  placeholder="Search Album"
                  aria-label="Search"
                  aria-describedby="basic-addon2"
                  name="value"
                />
                <div class="input-group-append">
                  <button class="btn btn-primary" type="submit">
                    <i class="fas fa-search fa-sm"></i>
                  </button>
                </div>
              </div>
            </form>





             <sec:authorize access="isAuthenticated()">
               <div style="margin:7px;">
                  <a class="markus-logout"  href="<c:url value="/user/details"/>">
                  <span class="submitLink"><sec:authentication property="principal.username"/></span> </a>
               </div>


               <div class="markus-logout" style="margin:7px;">
                    <form action="<c:url value="/logout"/>" method="post">
                      <input class="submitLink" type="submit" value="Logout">
                      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    </form>
               </div>
               </sec:authorize>





                <!-- Dropdown - User Information -->


          </nav>
          <!-- End of Topbar -->
          <div class="card-body">
            Fresh Albums From Discogs:
          </div>
          <!-- Begin Page Content -->
          <div class="markus-markus" >
            <!-- Page Heading -->



           <c:forEach items="${thumbs}" var="e">
            <div class="card shadow mb-4" style="width: 250px;"
                >
              <div class="card-header py-3">
                <h6 class="m-0 font-weight-bold text-primary">
                    ${e.title}
                </h6>
              </div>

              <div class="card-body markus-add" ><a href="<c:url value="/home/details/${e.id}"/>"><img src="${e.image}" alt="${e.title}" width="200" height="200"></a><br>
              <div class="markus-add"><a href="<c:url value="/home/add/${e.id}"/>" class="add-class">Add</a></div>
              </div>


            </div>
           </c:forEach>
          </div>
          <!-- /.container-fluid -->
        </div>
        <!-- End of Main Content -->

        <!-- Footer -->
        <footer class="sticky-footer bg-white">
          <div class="container my-auto">
            <div class="copyright text-center my-auto">
              <span>Copyright &copy; Vinyl Collection 2022</span>
            </div>
          </div>
        </footer>
        <!-- End of Footer -->
      </div>
      <!-- End of Content Wrapper -->
    </div>
    <!-- End of Page Wrapper -->

    <!-- Scroll to Top Button-->
    <a class="scroll-to-top rounded" href="#page-top">
      <i class="fas fa-angle-up"></i>
    </a>

    <!-- Logout Modal-->


    <!-- Bootstrap core JavaScript-->
    <script src="vendor/jquery/jquery.min.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

    <!-- Core plugin JavaScript-->
    <script src="vendor/jquery-easing/jquery.easing.min.js"></script>

    <!-- Custom scripts for all pages-->
    <script src="js/sb-admin-2.min.js"></script>
  </body>
</html>
