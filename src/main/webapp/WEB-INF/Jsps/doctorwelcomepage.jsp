<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="css/medecin.css">
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="css/materialize.min.css" media="screen,projection" />

<meta name="viewport" content="width=device-width, initial-scale=1">

<script type="text/javascript" src="js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="js/materialize.min.js"></script>
<title>DocsAdmin</title>
</head>

<body class="login-body">
	<div class="crossfade">
		<figure />
		<figure />
		<figure />
		<figure />
		<figure />
	</div>
	<div class="row">
		<div class="input-cart col s12 m10 push-m1 z-depth-2 grey lighten-5">

			<div class="col s12 m5 login">
				<h4 class="center">Connexion</h4>
				<br>
				<form action="">

					<div class="row">
						<div class="input-field">
							<input type="text" id="user" name="user" class="validate"
								required="required"> <label for="user">Utilisateur</label>
						</div>
					</div>

					<div class="row">
						<div class="input-field">
							<input type="password" id="passe" name="passe" class="validate"
								required="required"> <label for="pass">Mot de
								passe</label>
						</div>
					</div>

					<div class="row">
						<div class="col s6">
							<button name="connecter"
								class="btn blue left waves-effect waves-light" id="connecter">Connexion</button>
						</div>
					</div>

				</form>
			</div>

			<!-- Signup form -->
			<div class="col s12 m7 signup">
				<div class="signupForm">
					<h4 class="center">Inscription</h4>
					<br>
					<form name="signup" action="">
						<div class="row">

							<div class="input-field col s12 m6">
								<input type="text" id="nom" name="nom" class="validate"
									required="required"> <label for="nom">* Nom</label>
							</div>

							<div class="input-field col s12 m6">
								<input type="text" id="prénom" name="prénom" class="validate"
									required="required"> <label for="prénom">*
									Prénom</label>
							</div>

							<div class="input-field col s12 m6">
								<input type="password" id="password" name="password"
									class="validate" required="required"> <label
									for="password">* Mot de passe</label>
							</div>

							<div class="input-field col s12 m6">
								<input type="password" id="confirmation" name="confirmation"
									class="validate" required="required"> <label
									for="confirmation">* Confirmation mot de passe</label>
							</div>

							<div class="input-field col s6">
								<i class="material-icons prefix">account_circle</i> <input
									id="email" type="email" class="validate" required="required">
								<label for="email" data-error="wrong" data-success="right">*
									Email</label>
							</div>

							<div class="input-field col s6">
								<i class="material-icons prefix">phone</i> <input id="phone"
									type="tel" class="validate"> <label
									for="icon_telephone">Telephone</label>
							</div>

							<!-- la selection de sex-->
							<div class="input-field col s6">
								<select id="sex">
									<option value="" disabled selected>Choisissez votre
										sexe</option>
									<option value="1">Homme</option>
									<option value="2">Femme</option>
								</select> <label>Sex</label>
							</div>

							<div class="input-field col s6">
								<select id="spécialité">
									<option value="" disabled selected>Votre specialité</option>
									<c:forEach items="${requestScope.SPECIALITIES}"
										var="speciality" varStatus="status">
										<option value="${speciality.specialityId}">${speciality.specialityName}</option>
									</c:forEach>
								</select> <label>Specialité</label>
							</div>

						</div>
						<div class="row">
							<button id="rafraichir"
								class="btn blue left waves-effect waves-light" type="reset"
								value="Rafraîchir">Refrachir</button>

							<button name="btn-signup"
								class="btn blue right waves-effect waves-light" id="inscription">
								inscrire <i class="material-icons right">send</i>
							</button>
						</div>
					</form>


				</div>



				<div class="signup-toggle center">
					<h4 class="center">
						Pour créer un compte médecin <a href="#!">cliquez ici</a>
					</h4>
				</div>
			</div>

			<div class="col s12">
				<br>
				<div class="legal center"></div>


			</div>
		</div>
	</div>
	<!-- script -->
	<script src="js/medecin.js" type="text/javascript"></script>
</body>
</html>