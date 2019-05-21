jQuery(document).ready(function($){

	var     $page_inscription='/DocsAdmin/doctorwelcomepage',
			$page_connexion='/DocsAdmin/doctorwelcomepage',
			$page_acceuil = '/DocsAdmin/patients';

	var     
	$utilisateur = $('#user'),
	$passe = $('#passe'), // password de connexion
	$connecter = $('#connecter'),
	$nom = $('#nom'),
	$prénom = $('#prénom'),
	$password = $('#password'), // password d'insecription
	$confirmation = $('#confirmation'),
	$spécialité = $('#spécialité'),
	$email = $('#email'),
	$inscription = $('#inscription'),
	$reset = $('#rafraichir'),
	$erreur = $('#erreur'),
	$champ = $('.row');



	/*  envoyer les données    */
	$inscription.click(function(e){
		e.preventDefault(); // on annule la fonction par défaut du bouton d'envoi

		verifier($nom);
		verifier($prénom);
		verifier($password);
		verifier($confirmation);
		verifier($email);

		function verifier(champ){
			if(champ.val() == ""){ // si le champ est vide
				champ.css({ // on rend le champ rouge
					borderColor : 'red',
					color : 'red'
				});
			}
		}

		jQuery.post(
				$page_inscription, // Un script PHP que l'on va créer juste après
				{
					REQUEST: "SUBSCRIPTION",
					LAST_NAME : $("#nom").val(),  // Nous récupérons la valeur de nos input que l'on fait passer à connexion.php
					FIRST_NAME : $('#prénom').val(),
					PASSWORD : $("#password").val(),
					SPECIALITY : $("#spécialité").val(),
					GENDER : $('#sex').val(),
					EMAIL : $('#email').val(),
					PHONE_NUMBER : $('#phone').val()

				},

				function(data){
					if(data == 'SUBSCRIPTION_SUCCEEDED'){
						window.location.replace($page_acceuil);
					}
					else if(data == "EMAIL_EXISTS"){
						alert("L'email que vous avez utilise existe deja !");
					}

				},
				'text'
		);
	});


	$connecter.click(function(e){
		e.preventDefault(); // on annule la fonction par défaut du bouton d'envoi

		verifier($utilisateur);
		verifier($passe);

		function verifier(champ){
			if(champ.val() == ""){ // si le champ est vide
				champ.css({ // on rend le champ rouge
					borderColor : 'red',
					color : 'red'
				});
			}
		}

		jQuery.post(
				$page_connexion, // Un script PHP que l'on va créer juste après
				{
					REQUEST: "AUTHENTIFICATION",
					EMAIL : $utilisateur.val(),
					PASSWORD : $passe.val() 
				},

				function(data){
					if(data == 'AUTHENTIFICATION_SUCCEEDED')
						window.location.replace($page_acceuil);
					else alert("Ce compte d'utilisateur n'éxiste pas.")
				},
				'text'
		);
	})

	$("#nom").keyup(function() {
		     var input = $(this).val();
		     var regex = new RegExp(/([a-zA-Z]|[ \t])*$/);
		     if (!regex.test(input)) {
			          alert("Nom invalide");
			     } 
	});


	$("#prénom").keyup(function() {
		     var input = $(this).val();

		     var regex = new RegExp(/([a-zA-Z]|[ \t])*$/);
		     if (!regex.test(input)) {
			          alert("Prénom invalide");
			     } 
	});


	$("#phone").keyup(function() {
		     var input = $(this).val();
		     var regex = new RegExp(/[^0-9]/i);
		     if (regex.test(input)) {
			          alert("Numéro de Téléphone invalide");
			     } 
	});

	$('#confirmation').keyup(function(){
		if($(this).val() != $('#password').val()){ // si la confirmation est différente du mot de passe
			$(this).css({ // on rend le champ rouge
				borderColor : 'red',
				color : 'red'
			});
		}
		else{
			$(this).css({ // si tout est bon, on le rend vert
				borderColor : 'green',
				color : 'green'
			});
		}
	});
	/* calendrier pour date de naissance */
	$('.datepicker').pickadate({
		selectMonths: true, // Creates a dropdown to control month
		selectYears: 15, // Creates a dropdown of 15 years to control year,
		today: 'Today',
		clear: 'Clear',
		close: 'Ok',
		closeOnSelect: false // Close upon selecting a date,
	});
	/* refrachaire */
	$('#reset').click(function(){
		$champ.css({ // on remet le style des champs comme on l'avait défini dans le style CSS
			borderColor : '#ccc',
			color : '#555'
		});
		$erreur.css('display', 'none'); // on prend soin de cacher le message d'erreur
	});




	/*
    Code By Ilyes CH 
	 ** Celyes **
    contact : http://fb.com/celyes17
	 */
	$(".dropdown-button").dropdown();
	$('.modal').modal();
	$(".signup-toggle").click(function(){
		$(this).hide();
		$(".signupForm").show(300);
		$(".policy").css("visibility","visible");
	});


	$('select').material_select();
});