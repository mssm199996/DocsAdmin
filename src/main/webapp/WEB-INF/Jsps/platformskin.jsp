<%-- 
    Document   : platform
    Created on : 10 mars 2018, 16:27:27
    Author     : MSSM
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>
		<link href="css/materialize.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/platformskin.css" rel="stylesheet" type="text/css"/>
        <title>Docs Admin</title>
    </head>
    <body>
        <nav class="slidedNav">
            <div class="nav-wrapper blue lighten-1">
                <div class="container left">
                    <a id="sideNavButton" data-activates="slide-out" class="five-margin waves-effect waves-light btn-floating btn-large blue lighten-1 z-depth-0"><i class="material-icons">menu</i></a>
                    <a class="brand-logo"><img src="img/Logo1.png"/></a>
                </div>
                <ul id="nav-mobile" class="right hide-on-med-and-down">
                    <li><a href="disconnect"><i class="material-icons left">highlight_off</i>Deconnexion</a></li>
                </ul>
            </div>
        </nav>
        
        <ul id="slide-out" class="side-nav">
            <li>
                <div class="user-view">
                    <div class="background"><img src="img/office.png"></div>
                    <a><img class="circle" src="img/brain.png"></a>
                    <a class="white-text name">${sessionScope.MY_ACCOUNT.person.personFirstName} ${sessionScope.MY_ACCOUNT.person.personLastName}</a>
                    <a class="white-text email">${sessionScope.MY_ACCOUNT.person.personPhoneNumber}</a>
                    <a class="white-text email">${sessionScope.MY_ACCOUNT.accountEmail}</a>
                    <a class="white-text email">${sessionScope.MY_ACCOUNT.person.personId}</a>
                </div>
            </li>
            <li><a href="patients" class="waves-effect"><i class="material-icons">accessibility</i>Gestion des patients</a></li>
            <li><a href="consultations" class="waves-effect"><i class="material-icons">airline_seat_flat</i>Gestion des consultations</a></li>
            <li><a href="appointments" class="waves-effect"><i class="material-icons">perm_contact_calendar</i>Gestion des rendez-vous</a></li>
            <li><div class="divider"/></li>
            <li><a href="medecines" class="waves-effect"><i class="material-icons">add_circle</i>Gestion des medicaments</a></li>
            <li><div class="divider"/></li>
            <li><a href="documents" class="waves-effect" href="#!"><i class="material-icons">wallpaper</i>Gestion d'impression</a></li>
            <li><a href="SecretaryManagement" class="waves-effect" href="#!"><i class="material-icons">people</i>Gestion de la secretariat</a></li>
        </ul>
        <script src="js/jquery-3.2.1.min.js"></script>
        <script src="js/materialize.min.js"></script>
        <script src="js/platformskin.js"></script>
    </body>
</html>
