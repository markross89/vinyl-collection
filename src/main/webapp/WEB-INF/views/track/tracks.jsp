<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="g" uri="http://java.sun.com/jsp/jstl/functions" %>


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

    <title>Vinyl Collection - Songs</title>

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
            Songs collection:
            <div class="search-container input">
              <form action="<c:url value="/track/searchCollection"/>">
                <input class="input" type="text" placeholder="Search collection" name="query" style="border-radius:4px; border-style: solid;">
                <button type="submit" style="border-radius: 3px"><i class="fa fa-search" ></i></button>
              </form>
            </div>
          </div>

          <!-- Begin Page Content -->
          <div class="markus-markus" style="display: flex"  style="flex-wrap: wrap" style="justify-content: space-evenly">
            <!-- Page Heading -->


            <div class="card-body">
              <div class="table-responsive">
                <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                  <thead>
                  <tr>
                    <th>No</th>
                    <th><a href="<c:url value="/track/sortBy/title"/>" style="color: grey">Title</a></th>
                    <th><a href="<c:url value="/track/sortBy/artist"/>" style="color: grey">Artist</a></th>
                    <th><a href="<c:url value="/track/sortBy/album"/>" style="color: grey">Album</a></th>
                    <th><a href="<c:url value="/track/sortBy/label"/>" style="color: grey">Label</a></th>
                    <th><a href="<c:url value="/track/sortBy/date"/>" style="color: grey">Date</a></th>
                    <th><a href="<c:url value="/track/sortBy/duration"/>" style="color: grey">Duration</a></th>
                    <th>Track</th>
                  </tr>
                  </thead>
                   <tbody>



           <c:forEach items="${tracks}" var="t"  >
             <c:set var="counter" value="${counter + 1}"  scope="request" />
                  <tr>
                    <td>${counter}</td>
                    <td>${t.title}</td>
                    <td>${t.artist}</td>
                    <td><a href="<c:url value="/album/details/${t.discogsId}"/>" style="color: grey">${t.album}</a></td>
                    <td>${t.label}</td>
                    <td>${t.date}</td>
                    <td>${t.duration}</td>
                    <th><a href="<c:url value="/track/addForm/${t.id}"/>" style="color: darkgreen">Add</a></th>
                  </tr>
           </c:forEach>

                   </tbody>
                </table>
              </div>
            </div>
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
    <div
      class="modal fade"
      id="logoutModal"
      tabindex="-1"
      role="dialog"
      aria-labelledby="exampleModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel">Ready to Leave?</h5>
            <button
              class="close"
              type="button"
              data-dismiss="modal"
              aria-label="Close"
            >
              <span aria-hidden="true">×</span>
            </button>
          </div>
          <div class="modal-body">
            Select "Logout" below if you are ready to end your current session.
          </div>
          <div class="modal-footer">
            <button
              class="btn btn-secondary"
              type="button"
              data-dismiss="modal"
            >
              Cancel
            </button>
            <a class="btn btn-primary" href="login.html">Logout</a>
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
