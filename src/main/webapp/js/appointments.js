
var RDV_SERVLET ="";
var SELECTED_RDV = null;
var SELECTED_RDV_ID = "sa";

var SELECTED_PATIENT = null;
var SELECTED_PATIENT_ID = "sp";

var SELECT_TABLE_ROW_COLOR = "#cfd8dc";
var HOVERED_TABLE_ROW_COLOR = "#e0e0e0";

var ERROR_MESSAGE = "ERROR_MESSAGE";



(function(){
	this.initModels();
	this.initDatePicker();
	this.initTimePicker();
	this.initTextFields();
	this.initTable();
	this.initButtons();
	this.Recherche();
})();

function initModels(){
	$(document).ready(function(){
	    $('.modal').modal();
	});
}

function initDatePicker(){
	$('.datepicker').pickadate({
	    selectMonths: true, // Creates a dropdown to control month
	    selectYears: 15, // Creates a dropdown of 15 years to control year,
	    today: 'Aujourd\'hui',
	    clear: 'Effacer',
	    close: 'Confirmer',
	    closeOnSelect: true, // Close upon selecting a date,
	    format: "dd/mm/yyyy"
	});
}

function initTimePicker(){

  $('.timepicker').pickatime({
    default: 'now', // Set default time: 'now', '1:30AM', '16:30'
    fromnow: 0,       // set default time to * milliseconds from now (using with default = 'now')
    twelvehour: false, // Use AM/PM or 24-hour format
    donetext: 'OK', // text for done-button
    cleartext: 'Clear', // text for clear-button
    canceltext: 'Cancel', // Text for cancel-button,
    container: undefined, // ex. 'body' will append picker to body
    autoclose: false, // automatic close timepicker
    ampmclickable: true, // make AM PM clickable
    aftershow: function(){} //Function for after opening timepicker
  });
}


function initTable(){
	this.initRDVRows();
	this.initPatientRows();
}

function initButtons(){
	$("#removeRDVBtn").click(function(){
    	removeRDV();
    });

    $("#addRDVBtn").click(function(){
    	addnewRDV();
    });

    $(".removeParaBtn").click(function(){
    	var buttonIndex = $(this).attr("paraRob");

    	removeParaclinique(buttonIndex);
    });
    
     $(".removeMotifBtn").click(function(){
        var buttonIndex = $(this).attr("motifRob");

        removeMotif(buttonIndex);
    });

    $("#addNewParaBtn").click(function(){
    	addNewParaclinique();
    });
    
    $("#addNewMotifBtn").click(function(){
        addNewMotif();
    });

}

// ------------------ RDVs Handlings -----------------------

function removeAllRDV(){
	$(".contbody tr").remove();
    SELECTED_RDV = null;
}

function removeRDV(){
    if (SELECTED_RDV == null) 
    	alert("Veuillez d'abords selectionner un rendez_vous !");
	if(SELECTED_RDV != null){
        var appointmentId = SELECTED_RDV.attr("appointmentId");
                
        $.post(
            RDV_SERVLET,
            {
                REQUEST: "DELETE_APPOINTMENT",
                APPOINTMENT_ID: appointmentId,
            },
            function(result){
                if(result == "REMOVE_SUCCESS")
                    $("#" + SELECTED_RDV_ID).remove();
                    
                else if(result == "ERROR_MESSAGE")
                    alert("Impossible de supprimer l'element selectionne");
                else alert("ERROR");
            }
        );
	}
}

function addnewRDV(){
	if (SELECTED_PATIENT != null ) {
		var id = SELECTED_PATIENT.attr("patientId");
        var date = $("#addRDV_Date").val();                          
        var heure = $("#addTime").val();                                         

        $.post(
        	RDV_SERVLET,
        	{
        		REQUEST: "INSERT_NEW_APPOINTMENT",
        		PATIENT_ID: id,
        		APPOINTMENT_TIME: heure,
        		APPOINTMENT_DATE: date
        	},
            function(result){
                     if(result == "INSERT_SUCCESS"){                         
                         Recherche();
                         $('.modal').modal('close');
                     }
                     else 
                         alert("Impossible d'inserer une nouveau rendez-vous, réessayez ultérieurement");
                 }
            ).fail(function(){
                alert("Impossible de contacter le serveur, veuillez réessayer ultérieurement");
            });
    }
	else alert("vous devez selectionner un patient")
  
}

function addRDV(id, nom, prenom, date, heure){    
	$('#RDVTable')
        .append($('<tr>').attr("appointmentId", id)
        .append($('<td>').append($("#RDVTable tr").length))
        .append($('<td>').append(nom))
        .append($('<td>').append(prenom))
        .append($('<td>').append(date))
        .append($('<td>').append(heure))
    );
}

function addNewParaclinique(){
    if(SELECTED_RDV == null) 
    	alert("Veuillez d'abords selectionner un rendez_vous !");
    else {
    	var appointmentId = SELECTED_RDV.attr("appointmentId");
        var paraInfo = $("#newParaText").val();
        
         $.post(
            RDV_SERVLET,
            {
                REQUEST: "INSERT_NEW_PARACLINIC",
                APPOINTMENT_ID: appointmentId,
                TEXT_VALUE: paraInfo
            },
           function(result){
                if(result == "INSERT_SUCCESS")
                    addParaclinique(paraInfo);
                else if(result == "ERROR_MESSAGE")
                    alert("L'ajout est impossible, veuillez réessayer ultérierement !");
            }
        );
    }
}

function addParaclinique(paraInfo){
    var index = $("#ParacliniqueListe li").length + 1;

    $("#ParacliniqueListe")
        .append(
            $("<li>").addClass("collection-item valign-wrapper").attr("paraRob", index)
                .append(
                    $("<div>").addClass("col s11 left-align updPara").append(paraInfo)
                )
                .append(
                    $("<div>").addClass("col s1 right-align")
                        .append(
                            $("<a>").addClass("waves-effect btn-small secondary-content red-text removeObsBtn").attr("paraRob", index)
                                .click(
                                    function(){ 
                                        removeParaclinique($(this).attr("paraRob"));
                                    }
                                )
                                .append(
                                    $("<i>").addClass("material-icons").append("remove")
                                )
                        )
                )
                     
        );     
}

function removeAllParacliniques(){
	$("#ParacliniqueListe li").remove();
}

function removeParaclinique(index){
    if (SELECTED_RDV == null) 
    	alert("Veuillez d'abords selectionner un rendez vous !");
    else {
        var appointmentId = SELECTED_RDV.attr("appointmentId");
        var paraclinic = $('li[paraRob = ' + index + ']');
                
        $.post(
            RDV_SERVLET,
            {
                REQUEST: "DELETE_PARACLINIC",
                APPOINTMENT_ID: appointmentId,
                TEXT_VALUE: paraclinic.find("div:nth-child(1)").text()
            },
            function(result){
                if(result == "REMOVE_SUCCESS")
                	paraclinic.remove();
                    
                else if(result == "ERROR_MESSAGE")
                    alert("Impossible de supprimer l'element selectionne");                
            }
        );  
    }
	
}

function getParaclinique(appointmentId){
    $.post(
            RDV_SERVLET,
            {
                REQUEST: "GET_PARACLINICS",
                APPOINTMENT_ID: appointmentId,
            },
            function(result){
                if(result == ERROR_MESSAGE)
                    alert("Erreur... la RDV specifié n'éxiste pas");
                else {
                    removeAllParacliniques();
                    
                    var xmlDoc = $.parseXML(result);
                    var xml = $(xmlDoc);
                    
                    $(xml).find("paraclinic").each( function(){
                        addParaclinique($(this).find("description").text());
                    });
                }
            }
    ).fail(function() {
        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
    });
}

function addNewMotif(){
    if(SELECTED_RDV == null) 
    	alert("Veuillez d'abords selectionner un rendez_vous !");
    else{
        var appointmentId = SELECTED_RDV.attr("appointmentId");
        var reason = $("#newMotifText").val();
                
        $.post(
            RDV_SERVLET,
            {
                REQUEST: "INSERT_NEW_REASON",
                APPOINTMENT_ID: appointmentId,
                TEXT_VALUE: reason
            },
           function(result){
                if(result == "INSERT_SUCCESS")
                    addMotif(reason);
                else if(result == "ERROR_MESSAGE")
                    alert("L'ajout est impossible !");
            }
        );
  }
}

function addMotif(reason){
    var reasonIndex = $("#MotifListe li").length + 1;

    $("#MotifListe")
        .append(
            $("<li>").addClass("collection-item valign-wrapper").attr("reasonRob", reasonIndex)
                .append(
                    $("<div>").addClass("col s11 left-align updMotif").append(reason)
                )
                .append(
                    $("<div>").addClass("col s1 right-align")
                        .append(
                            $("<a>").addClass("waves-effect btn-small secondary-content red-text removeObsBtn").attr("reasonRob", reasonIndex)
                                .click(
                                    function(){ 
                                        removeMotif($(this).attr("reasonRob"));
                                    }
                                )
                                .append(
                                    $("<i>").addClass("material-icons").append("remove")
                                )
                        )
                )
              
        );     
}

function removeAllMotif(){
    $("#MotifListe li").remove();
}

function removeMotif(index){
    if (SELECTED_RDV == null) 
    	alert("Veuillez d'abords selectionner un rendez vous !");
    else{
        var appointmentId = SELECTED_RDV.attr("appointmentId");
        var motif = $('li[reasonRob = ' + index + ']');
        
        $.post(
            RDV_SERVLET,
            {
                REQUEST: "DELETE_REASON",
                APPOINTMENT_ID: appointmentId,
                TEXT_VALUE: motif.find("div:nth-child(1)").text()
            },
            function(result){
                if(result == "REMOVE_SUCCESS")
                    motif.remove();
                    
                else if(result == "ERROR_MESSAGE")
                    alert("Impossible de supprimer l'element selectionne");
            }
        );  
    }
    
}


function getMotif(appointmentId){
    $.post(
            RDV_SERVLET,
            {
                REQUEST: "GET_REASONS",
                APPOINTMENT_ID: appointmentId,
            },
            function(result){
                if(result == ERROR_MESSAGE)
                    alert("Erreur... le rendez-vous specifiée n'éxiste pas");
                else {
                    removeAllMotif();
                    
                    var xmlDoc = $.parseXML(result);
                    var xml = $(xmlDoc);
                    
                    $(xml).find("reason").each(function(){
                        addMotif($(this).find("description").text());
                    });
                }
            }
    ).fail(function() {
        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
    });
}

function initTextFields(){
    $("#searchedPatient").keyup(function(){
       Recherche();
    });
    
    $("#RDV_Date").change(function() {
      Recherche();
    });
    
    $("#newRDVSearchedPatient").keyup(function(){
    	searchPatients($(this).val());
    });
}


function Recherche(){
     $.post(
    		 RDV_SERVLET,
                {
                    REQUEST: "GET_APPOINTMENTS",
                    APPOINTMENT_DATE: $("#RDV_Date").val(),
                    APPOINTMENT_PATIENT: $("#searchedPatient").val()
                },
                function(result){
                	removeAllRDV();
                    removeAllParacliniques();
                    removeAllMotif();
                    
                    var xmlDoc = $.parseXML(result);
                    var xml = $(xmlDoc);
                    
                    $(xml).find("appointment").each( function(){
                        var id = $(this).attr("appointmentId");
                        var nomPatient = $(this).find("patient").find("patientLastName").text();
                        var prenomPatient = $(this).find("patient").find("patientFirstName").text();
                        var date = $(this).find("appointmentDate").text();
                        var heure = $(this).find("appointmentTime").text();
                        
                        addRDV(id, nomPatient, prenomPatient, date, heure);
                    });
                    
                    initRDVRows();
                }
        ).fail(function() {
            alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
        });
}

// --------------------------- Patients -----------------------------
function removeAllPatients(){
	$(".pattbody tr").remove();
	SELECTED_PATIENT = null;	
}

function searchPatients(searchedPatient){
	$.post(
			RDV_SERVLET,
			{
				REQUEST: "GET_PATIENTS",
				TEXT_VALUE: searchedPatient
			},
			function(result){
				removeAllPatients();
				
	            var xmlDoc = $.parseXML(result);
	            var xml = $(xmlDoc);
	            
	            $(xml).find("patient").each( function(){
	                var id = $(this).attr("patientId");
	                var nomPatient = $(this).find("patientLastName").text();
	                var prenomPatient = $(this).find("patientFirstName").text();
	                var datePatient = $(this).find("patientBirthday").text();
	                
	                addPatient(id, nomPatient, prenomPatient, datePatient);
	                initPatientRows();
	            });
			}
	).fail(function() {
        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
    });
}

function addPatient(id, nom, prenom, date){
	$('#patientsTable')
        .append($('<tr>').attr("patientId", id)
        .append($('<td>').append($("#patientsTable tr").length))
        .append($('<td>').append(nom))
        .append($('<td>').append(prenom))
        .append($('<td>').append(date))
    );
}

// -------------------------- Tables ---------------------------- 

function initPatientRows(){
    $(".pattbody tr").click(function () {
        $(".pattbody tr").css("background-color", "white");
        $(this).css("background-color", SELECT_TABLE_ROW_COLOR);

        if(SELECTED_PATIENT != null)
            SELECTED_PATIENT.removeAttr("id");

        SELECTED_PATIENT = $(this);
        SELECTED_PATIENT.attr("id", SELECTED_PATIENT_ID);
    });

    $(".pattbody tr").hover(function(){
        $(".pattbody tr").each(function(){
            if(SELECTED_PATIENT == null || $(this).get(0) != SELECTED_PATIENT.get(0))
                $(this).css("background-color", "white");
        });

        if(SELECTED_PATIENT != null && $(this).get(0) != SELECTED_PATIENT.get(0))
            $(this).css("background-color", HOVERED_TABLE_ROW_COLOR);
        else if(SELECTED_PATIENT == null)
            $(this).css("background-color", HOVERED_TABLE_ROW_COLOR);
    });
}


function initRDVRows(){
    $(".contbody tr").click(function () {
        $(".contbody tr").css("background-color", "white");
        $(this).css("background-color", SELECT_TABLE_ROW_COLOR);

        if(SELECTED_RDV != null)
            SELECTED_RDV.removeAttr("id");

        SELECTED_RDV = $(this);
        SELECTED_RDV.attr("id", SELECTED_RDV_ID);
        
        getParaclinique($(this).attr("appointmentId"));
        getMotif($(this).attr("appointmentId"));
     
    });

    $(".contbody tr").hover(function(){
        $(".contbody tr").each(function(){
            if(SELECTED_RDV == null || $(this).get(0) != SELECTED_RDV.get(0))
                $(this).css("background-color", "white");
        });

        if(SELECTED_RDV != null && $(this).get(0) != SELECTED_RDV.get(0))
            $(this).css("background-color", HOVERED_TABLE_ROW_COLOR);
        else if(SELECTED_RDV == null)
            $(this).css("background-color", HOVERED_TABLE_ROW_COLOR);
    });
}

/*
 * Imposible de rechercher des patient (la gestion de la recherche des patients n'existe pas)
 * Impossible d'ajouter un rdv (aucun parametre n'est envoyé au serveur)
 * addRdv contient Recherche() ???
 * la date n'est pas au bon format pour pouvoir ajouter
 * 
 * */
