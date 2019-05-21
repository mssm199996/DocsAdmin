<%-- 
    Document   : index
    Created on : 5 mars 2018, 17:29:30
    Author     : MSSM
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
		<link href="css/materialize.min.css" rel="stylesheet" type="text/css"/>
		<link rel="stylesheet" type="text/css" href="css/index.css"/>
		<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>
        <title>Docs Admin</title>
    </head>
    <body>
        <nav class="slidedNav">
            <div class="nav-wrapper blue lighten-1">
              <a href="#" class="brand-logo"><img src="img/Logo1.png"/></a>
              <ul id="nav-mobile" class="right hide-on-med-and-down">
                <li><a href="secretarywelcomepage"><i class="material-icons left">create</i>Secretaire</a></li>
                <li><a href="doctorwelcomepage"><i class="material-icons left">present_to_all</i>Medecin</a></li>
                <li><a href="/help"><i class="material-icons left">help</i>A propos</a></li>
              </ul>
            </div>
        </nav>

        <div class="parallax-container valign-wrapper">
            <div class="parallax">
                <img src="img/doctor_office-wallpaper.jpg">
            </div>
            <div class="container">
                <div>
                    <h3 class="center-align white-text">Le premier portail pour la gestion médical en ligne en Algerie</h3>
                    <div>
                        <h5 class="center-align white-text">Grâce à DocsAdmin, centralisez la gestion de votre activité médicale.</h5>
						<h5 class="center-align white-text">Un outil simple & indispensable qui facilite votre quotidien.</h5>
                    </div>
                    
                </div>
                <div class="container center-align buttonsDiv">
                    <a href="secretarywelcomepage" class="waves-effect waves-light btn-large cyan signInButtin"><i class="material-icons left">create</i>Je suis secretaire</a>
                    <a href="doctorwelcomepage" class="waves-effect waves-light btn-large cyan "><i class="material-icons left">present_to_all</i>Je suis medecin</a>
                </div>
            </div>
        </div>

        <div class="section white">
            <h2 class="header center-align">Services</h2>
            <div>
                <div class="row container">
                    <div class="row center-align valign-wrapper">

                        <div class="col s5 z-depth-2">
                            <div>
                                <div class="center-align">
                                    <i class="material-icons medium">accessibility</i>
                                </div>
                                <div class="card-content">
                                    <h5 class="card-title black-text">Gestion des patients</h5>
                                    <p>
                                        Créez et accédez aux dossiers complets de vos patients pour un suivi optimal: administratif, antécédents médicaux, maladies chroniques, analyses médicaux...
                                    </p>
                                </div>
                            </div>
                        </div>

                        <div class="col s5 z-depth-2 middleCard">
                            <div>
                                <div class="center-align">
                                    <i class="material-icons medium">airline_seat_flat</i>
                                </div>
                                <div class="card-content">
                                    <h5 class="card-title black-text">Gestion des consultations</h5>
                                    <p>
                                        Créez et lister toutes vos dernières consultations pour re-consulter toutes vos observations, vos diagnostiques et plus encore !
                                    </p>
                                </div>
                            </div>
                        </div>

                        <div class="col s5 z-depth-2">
                            <div>
                                <div class="center-align">
                                    <i class="material-icons medium">add_circle</i>
                                </div>
                                <div class="card-content">
                                    <h5 class="card-title black-text">Nomenclature des médicaments</h5>
                                    <p>
                                        Consulter toute la nomenclature agré par l'état pour rester au courant de tout les médicaments disponibles et ceux remboursables.
                                    </p>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="row container">
                    <div class="row center-align valign-wrapper">

                        <div class="col s5 z-depth-2">
                            <div>
                                <div class="center-align">
                                    <i class="material-icons medium">perm_contact_calendar</i>
                                </div>
                                <div class="card-content">
                                    <h5 class="card-title black-text">Gestion des rendez-vous</h5>
                                    <p>
                                        Programmer tout vos rendez-vous en ligne, et consulter vos prochaines consultations pour une gestion facile et éfficace !
                                    </p>
                                </div>
                            </div>
                        </div>

                        <div class="col s5 z-depth-2 middleCard">
                            <div>
                                <div class="center-align">
                                    <i class="material-icons large">wallpaper</i>
                                </div>
                                <div class="card-content">
                                    <h5 class="card-title black-text">Impression des documents</h5>
                                    <p>
                                        Concervez soigneusement vos documents: fiche d'analyses, ordonnances... en ligne et imprimez les en un clin d'oeil !
                                    </p>
                                </div>
                            </div>
                        </div>

                        <div class="col s5 z-depth-2">
                            <div>
                                <div class="center-align">
                                    <i class="material-icons large">people</i>
                                </div>
                                <div class="card-content">
                                    <h5 class="card-title black-text">Secrétairiat</h5>
                                    <p>
                                        Facilitez l'enregistrement des dossiers médicaux et des rendez-vous en chargeant vos secrétaires de le faire à votre place.
                                    </p>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>

        <div class="parallax-container">
            <div class="parallax"><img src="img/doctor_office-wallpaper2.jpg"></div>
        </div>

        <footer class="page-footer blue lighten-1">
          <div class="container">
            <div class="row ">
              <div class="col l6 s12 center-align">
                <h5 class="white-text">Follow us</h5>
                <a class="waves-effect waves-light mediaButton white"><img src="img/facebook.png"></a>
                <a class="waves-effect waves-light mediaButton white"><img src="img/twitter.png"></a>
                <a class="waves-effect waves-light mediaButton white"><img src="img/youtube.png"></a>
              </div>
              <div class="col l4 offset-l2 s12">
                <h5 class="white-text">Contact us</h5>
                <ul>
                    <li>
                        <div class="valign-wrapper contactDivs">
                            <i class="material-icons">email</i>
                            <a class="grey-text text-lighten-3">docs.admin@gmail.com</a>
                        </div>
                    </li>
                    <li>
                        <div class="valign-wrapper contactDivs">
                            <i class="material-icons">phone_android</i>
                            <a class="grey-text text-lighten-3">+213559608173</a>
                        </div>
                    </li>
                    <li>
                        <div class="valign-wrapper contactDivs">
                            <i class="material-icons">local_phone</i>
                            <a class="grey-text text-lighten-3">043878960</a>
                        </div>
                    </li>
                </ul>
              </div>
            </div>
          </div>
          <div class="footer-copyright">
            <div class="container">
            © 2018 Copyright DocsAdmin Corporation
            </div>
          </div>
        </footer>
        <script src="js/jquery-3.2.1.min.js"></script>
        <script src="js/materialize.min.js"></script>
        <script src="js/index.js"></script>
    </body>
</html>
