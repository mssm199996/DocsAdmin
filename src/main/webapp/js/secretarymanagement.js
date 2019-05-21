var SELECTED_SECRETAIRE = null;
var SECRETARY_SERVELET = "SecretaryManagement"; // secretaires
var SELECTED_SECRETAIRE_ID = "ss"; // ss
var ERROR_MESSAGE = "ERROR_MESSAGE";
var SELECTED_SECRETAIRE_ROW;
var SELECTED_SECRETAIRE_ROW_CONFIRM;
var SELECTED_ROW;
var IdButtton1;

// IIFF (instantly invoked function)
function initSelectors() {
	$('select').material_select();
}

function getIdRow(object) {
	SELECTED_SECRETAIRE_ROW = $(object).parent().parent().attr("idSecretary");
}

function deleteRow() {
	removeSecretaire();
	
	$('#secretaireTable tr[ idSecretary = ' + SELECTED_SECRETAIRE_ROW + ']').remove();
}

function getIdButton(object) {
	IdButtton1 = $(object).attr("id");
	SELECTED_SECRETAIRE_ROW_CONFIRM = $(object).parent().parent().attr("idSecretary");
	SELECTED_SECRETAIRE_ROW = $(object).attr("NotConfirm");
}

// ______________________________________________________________________________________

function removeSecretaire() {	
	if (SELECTED_SECRETAIRE_ROW != null) {
		$.post(
			SECRETARY_SERVELET, 
			{
				REQUEST: "DELETE_SECRETARY",
				SECRETARY_ID : SELECTED_SECRETAIRE_ROW,
			}, 
			function(result) {
				if (result == "REMOVE_SUCCESS")
					$('#secretaireTable tr[ idSecretary = ' + SELECTED_SECRETAIRE_ROW + ']').remove();
				else if (result == "ERROR_MESSAGE")
					alert("Impossible de supprimer votre secretaire");
			}
		).fail(function(){
			alert("La suppression a échouée, veuillez réessayer ultérieusement");
		});
	}
}

// ---------------confirmer-------------------
function confirmSecretary() {	
	if (SELECTED_SECRETAIRE_ROW_CONFIRM != null) {
		$.post(
			SECRETARY_SERVELET, 
			{
				REQUEST : "CONFIRM_SECRETARY",
				SECRETARY_ID : SELECTED_SECRETAIRE_ROW_CONFIRM,
			}, 
			function(result) {
				if (result == "ERROR_MESSAGE")
					alert("Impossible de confirmer votre secretaire");
			}
		).fail(function(){
			alert("La confirmation a échouée, veuillez réessayer ultérieusement");
		});
	}
}

// --------------------Clean Secretary--------------------------------
function cleanSecretary() {
	$.post(
		SECRETARY_SERVELET, 
		{
			REQUEST : "CLEAN_NON_CONFIRMED_SECRETARIES",
		}, 
		function(result) {
			if (result == "CLEAN_SUCCESS")
				Nettoyer();

			else if (result == "ERROR_MESSAGE")
				alert("Pas de secretaire à nettoyer");
		}
	);
}

// ------------------------------------------------------------------------
function Nettoyer() {
	$('#secretaireTable .secrbody tr').each(
		function() {
			var NotConfirmAtt = $(this).find("#btnConfirm").attr('NotConfirm');
			
			if (NotConfirmAtt == "true") 				
				$(this).remove();
		}
	);
}

// -------------------------confirmation-of-confirm-secretary-----------------

function confirmConfirmation() {
	this.confirmSecretary();
	disableButton();
}
// -------------------------confirmation-of-clean------------------

function confirmClean() {
	this.cleanSecretary();
}

// ------------------------------------disable button
// confirm----------------------------------
function disableButton() {
	$('#' + IdButtton1).attr("disabled", true);
	$('#' + IdButtton1).attr("NotConfirm", "true");
}
function getAttdRow(object) {
	SELECTED_SECRETAIRE_ROW = $(object).parent().parent().attr("NotConfirm");
}

$(document).ready(function() {

	$('.modal').modal();

	$('#clean').on('click', function() {
		$('#modal3').modal('open');
	})

	$('.confirm').on('click', function() {
		$('#modal2').modal('open');
	})

	$('.delete').on('click', function() {
		$('#modal1').modal('open');
	})

});
