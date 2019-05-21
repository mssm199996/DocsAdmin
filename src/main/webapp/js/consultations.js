var MEDECINES_SERVLET = "medecines";

var IS_REM_CHILD = '<input type="checkbox" class="filled-in" checked="checked" />' +
                   '<label for="nothing"></label>';
var IS_NOT_REM_CHILD = '<input type="checkbox" />' +
                       '<label for="nothing"></label>';

var SELECTED_CONSULTATION = null;
var SELECTED_CONSULTATION_ID = "sc";

var SELECTED_PATIENT = null;
var SELECTED_PATIENT_ID = "sp";

var SELECT_TABLE_ROW_COLOR = "#cfd8dc";
var HOVERED_TABLE_ROW_COLOR = "#e0e0e0";

var CONSULTATIONS_SERVLET = "consultations";

var ERROR_MESSAGE = "ERROR_MESSAGE";

(function(){
	this.initModels();
	this.initDatePicker();
	this.initTable();
	this.initButtons();
	this.initTextFields();
	this.searchConsultations();
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

function initTable(){
	this.initConsultationRows();
	this.initPatientRows();
	this.initMedecineRows();
}

function initButtons(){
	$("#removeConsuBtn").click(function(){
    	removeConsultation();
    });

    $("#addConsultationBtn").click(function(){
    	addNewConsultation();
    });

    $(".removeObsBtn").click(function(){
    	var buttonIndex = $(this).attr("rob");

    	removeObservation(buttonIndex);
    });

    $("#addNewObsBtn").click(function(){
    	addNewObservation();
    });

    $(".removeMedBtn").click(function(){
    	var buttonIndex = $(this).attr("rmed");

    	removeMedecine(buttonIndex);
    });

    $("#hm").keyup(function(){
    	updateHm();
    });

    $("#ec").keyup(function(){
    	updateEc();
    })

    $("#dia").keyup(function(){
    	updateDiag();
    });
}

function initTextFields(){
	$("#searchedPatient").keyup(function(){
		searchConsultations();
	});
	
	$("#consultationDate").change(function() {
		searchConsultations();
	});
	
	$("#newConsultationSearchedPatient").keyup(function(){
		$.post(
				CONSULTATIONS_SERVLET,
				{
					REQUEST: "GET_PATIENTS",
					TEXT_VALUE: $("#newConsultationSearchedPatient").val()
				},
				function(result){
					// Le resultat est un fichier xml
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
	});
	
    $("#newPrescriptionSearchedMedecine").keyup(function(){
        refreshMedecinesFromServer($(this).val());
    });
}

// ------------------ Consultations Handlings -----------------------

function removeAllConsultations(){
	$(".contbody tr").remove();
	SELECTED_CONSULTATION = null;
}

function removeConsultation(){
	if(SELECTED_CONSULTATION == null)
		alert("Veuillez d'abords selectionner une consultation !");
	
	else if(SELECTED_CONSULTATION != null){
		var consultationId = SELECTED_CONSULTATION.attr("consultationId");
		
		$.post(
			CONSULTATIONS_SERVLET,
			{
				REQUEST: "DELETE_CONSULTATION",
				CONSULTATION_ID: consultationId,
			},
			function(result){
				if(result == "REMOVE_SUCCESS")
					$("#" + SELECTED_CONSULTATION_ID).remove();
					
				else if(result == "ERROR_MESSAGE")
					alert("Impossible de supprimer l'element selectionne");
			}
		).fail(function() {
	        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
	    });	
	}
}

function searchConsultations(){
	$.post(
		CONSULTATIONS_SERVLET,
		{
			REQUEST: "GET_CONSULTATIONS",
			CONSULTATION_DATE: $("#consultationDate").val(),
			CONSULTATION_PATIENT: $("#searchedPatient").val()
		},
		function(result){
			// Le resultat est un fichier xml
			removeAllConsultations();
	        removeAllObservations();
	        clearTexts();
	            
	        var xmlDoc = $.parseXML(result);
	        var xml = $(xmlDoc);
	            
	        $(xml).find("consultation").each( function(){
	            var id = $(this).attr("consultationId");
	            var nomPatient = $(this).find("patient").find("patientLastName").text();
	            var prenomPatient = $(this).find("patient").find("patientFirstName").text();
	            var date = $(this).find("consultationDate").text();
	            var prix = $(this).find("consultationPrice").text();
	                
	            addConsultation(id, nomPatient, prenomPatient, date, prix);
	        });
	            
	        initConsultationRows();
		}
	).fail(function() {
        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
    });
}

function addNewConsultation(){
	if(SELECTED_PATIENT == null)
		alert("Veuillez selectionner un patient");
	else{
		var regex = new RegExp(/^\d+$|^\d+\.\d+$/);

		if(!regex.test($("#price").val()))
			alert("Prix invalide");
		else {

			var patientId = SELECTED_PATIENT.attr("patientId");
			var price = $("#price").val();
			
			$.post(
				 CONSULTATIONS_SERVLET,
				 {
					 REQUEST: "INSERT_NEW_CONSULTATION",
					 PATIENT_ID: patientId,
					 PRICE: price
				 },
				 function(result){
					 if(result == "INSERT_SUCCESS"){						 
						 $.post(
									CONSULTATIONS_SERVLET,
									{
										REQUEST: "GET_CONSULTATIONS",
										CONSULTATION_DATE: $("#consultationDate").val(),
										CONSULTATION_PATIENT: $("#searchedPatient").val()
									},
									function(result){
										removeAllConsultations();
							            removeAllObservations();
							            clearTexts();
							            
							            var xmlDoc = $.parseXML(result);
							            var xml = $(xmlDoc);
							            
							            $(xml).find("consultation").each( function(){
							                var id = $(this).attr("consultationId");
							                var nomPatient = $(this).find("patient").find("patientLastName").text();
							                var prenomPatient = $(this).find("patient").find("patientFirstName").text();
							                var date = $(this).find("consultationDate").text();
							                var prix = $(this).find("consultationPrice").text();
							                
							                addConsultation(id, nomPatient, prenomPatient, date, prix);
							            });
							            
							            initConsultationRows();
									}
							).fail(function() {
						        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
						    });
						 
						 $('.modal').modal('close');
					 }
					 else 
						 alert("Impossible d'inserer une nouvelle consultation, réessayez ultérieurement");
				 }
			).fail(function(){
				alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
			});
		}
	}
}

function addConsultation(id, nom, prenom, date, prix){
	$('#consultationsTable')
        .append($('<tr>').attr("consultationId", id)
        .append($('<td>').append($("#consultationsTable tr").length))
        .append($('<td>').append(nom))
        .append($('<td>').append(prenom))
        .append($('<td>').append(date))
        .append($('<td>').append($("<input>").addClass("validate").attr("type", "text").val(prix).keyup(function(event){
        	updateConsultationPrice($(this), id, prix);
        }).keydown(function(){
        	prix = $(this).val();
        })))
    );
}

function updateConsultationPrice(input, consultationId, ancientValue){
	var regex = new RegExp(/^\d+$|^\d+\.\d+$/);

	if(regex.test(input.val())){
		$.post(
			CONSULTATIONS_SERVLET,
			{
				REQUEST: "UPDATE_PRICE",
				CONSULTATION_ID: consultationId,
				PRICE: input.val()
			},
			function(result){
				if(result == "UPDATE_SUCCESS");			 
					 
				else {
					alert("Impossible de faire la mise à jour, réessayez ultérieurement");
					input.val(ancientValue);
				}
			}
		).fail(function() {
			alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
		    input.val(ancientValue);
		});
	}
	
	else {
		alert("Prix invalide");
		input.val(ancientValue);
	}
}

// -------------------- Observation Handlings -----------------------

function removeAllObservations(){
	$("#observationList li").remove();
}

function removeObservation(index){
	if(SELECTED_CONSULTATION == null)
		alert("Veuillez d'abords selectionner une consultation !");
	else{
		var consultationId = SELECTED_CONSULTATION.attr("consultationId");
		var observation = $('li[obIndex="' + index + '"]');
		
		$.post(
			CONSULTATIONS_SERVLET,
			{
				REQUEST: "DELETE_OBSERVATION",
				CONSULTATION_ID: consultationId,
				TEXT_VALUE: observation.find("div:nth-child(1)").text()
			},
			function(result){
				if(result == "REMOVE_SUCCESS")
					observation.remove();
					
				else if(result == "ERROR_MESSAGE")
					alert("Impossible de supprimer l'element selectionne");
			}
		);
	}	
}

function addNewObservation(){
	if(SELECTED_CONSULTATION == null)
		alert("Veuillez d'abords selectionner une consultation !");
	else {
		var consultationId = SELECTED_CONSULTATION.attr("consultationId");
		var obsInf = $("#newObsText").val();
		
		$.post(
			CONSULTATIONS_SERVLET,
			{
				REQUEST: "INSERT_NEW_OBSERVATION",
				CONSULTATION_ID: consultationId,
				TEXT_VALUE: obsInf
				
			},
			function(result){
				if(result == "INSERT_SUCCESS")
					addObservation(obsInf);
				else if(result == "ERROR_MESSAGE")
					alert("L'ajout est impossible !");
			}
		);
	}	
}

function addObservation(obsInf){
	var obsIndex = $("#observationList li").length + 1;

	$("#observationList")
		.append(
			$("<li>").addClass("collection-item valign-wrapper").attr("obIndex", obsIndex)
				.append(
					$("<div>").addClass("col s11 left-align").append(obsInf)
				)
				.append(
					$("<div>").addClass("col s1 right-align")
						.append(
							$("<a>").addClass("waves-effect btn-small secondary-content red-text removeObsBtn").attr("rob", obsIndex)
								.click(
									function(){ 
										removeObservation($(this).attr("rob"));
									}
								)
								.append(
									$("<i>").addClass("material-icons").append("remove")
								)
						)
				)
		);
}

function getObservation(consultationId){
	$.post(
			CONSULTATIONS_SERVLET,
			{
				REQUEST: "GET_OBSERVATIONS",
				CONSULTATION_ID: consultationId,
			},
			function(result){
				if(result == ERROR_MESSAGE)
					alert("Erreur... la consultation specifiée n'éxiste pas");
				else {
					removeAllObservations();
					
					var xmlDoc = $.parseXML(result);
		            var xml = $(xmlDoc);
		            
					$(xml).find("observation").each( function(){
		                addObservation($(this).find("description").text());
		            });
				}
			}
	).fail(function() {
        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
    });
}

// ---------------------- TXT Handlings --------------------
function clearTexts(){
	$("#hm").val("");
	$("#ec").val("");
	$("#dia").val("");
}

function getText(request){
	$.post(
		CONSULTATIONS_SERVLET,
		{
			REQUEST: request,
			CONSULTATION_ID: $("#" + SELECTED_CONSULTATION_ID).attr("consultationId"),
		},
		function(result){
			if(request == "GET_HM")
				$("#hm").val(result);
			
			else if(request == "GET_EC")
				$("#ec").val(result);
			
			else if(request == "GET_DIA")
				$("#dia").val(result);
				
		}
	).fail(function() {
        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
    })
}

function updateText(request, content){
	$.post(
		CONSULTATIONS_SERVLET,
		{
			REQUEST: request,
			CONSULTATION_ID: $("#" + SELECTED_CONSULTATION_ID).attr("consultationId"),
			TEXT_VALUE: content
		},
		function(result){
		}
	).fail(function(){
		alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
	});
}


function updateHm(){
	if(SELECTED_CONSULTATION == null){
		$("#hm").val("");
		alert("Veuillez selectionner une consultation");
	}

	else updateText("UPDATE_HM", $("#hm").val());
}

function updateEc(){
	if(SELECTED_CONSULTATION == null){
		$("#ec").val("");
		alert("Veuillez selectionner une consultation");
	}
	else updateText("UPDATE_EC", $("#ec").val());
}

function updateDiag(){
	if(SELECTED_CONSULTATION == null){
		$("#dia").val("");
		alert("Veuillez selectionner une consultation");
	}

	else updateText("UPDATE_DIA", $("#dia").val());
}

// ---------------------- Patient ------------------------

function removeAllPatients(){
	$(".pattbdoy tr").remove();
	SELECTED_PATIENT = null;	
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


// ---------------------- Medecine ----------------------------

function addMedecine(medId, dci, mark, form, dosage, isRemb){    
    $('#medecinesTable')
        .append($('<tr>').attr("medecineId", medId)
        .append($('<td>').append(dci).addClass("medecineDataCell"))
        .append($('<td>').append(mark).addClass("medecineDataCell"))
        .append($('<td>').append(form).addClass("medecineDataCell"))
        .append($('<td>').append(dosage).addClass("medecineDataCell"))
        .append($('<td>').append(isRemb === 'true' ? IS_REM_CHILD : IS_NOT_REM_CHILD).addClass("medecineCheckCell"))
        .append($('<td>').append($('<a>').addClass("waves-effect waves-light btn medecineDataCell").click(function(){
        	addNewPrescription($(this).parent().parent().attr("medecineId"));
        }).append('Prescrire')))
    );
}

function removeAllMedecines(){
    $('#medecinesTable tbody tr').remove();
}

function refreshMedecinesFromServer(requestContent){
    $.post(
        MEDECINES_SERVLET, 
        {
            TARGETED_MEDECINES: requestContent
        },
        function(result){
            // Le resultat est un fichier xml
        	removeAllMedecines();
            
            var xmlDoc = $.parseXML(result);
            var xml = $(xmlDoc);
            
            $(xml).find("medecine").each( function(){
            	var medId = $(this).attr("medecineId");
                var dci = $(this).find("medecineDci").text();
                var mark = $(this).find("medecineMark").text();
                var form = $(this).find("medecineForm").text();
                var dosage = $(this).find("medecineDosage").text();
                var redeem = $(this).find("medecineRedeemability").text();
                
                addMedecine(medId, dci, mark, form, dosage, redeem);
            });
        }
    ).fail(function() {
        alert("Impossible de rafraichir la liste des médicaments... Veuillez réessayer plus tard.");
    });
}

//-------------------- Prescriptions ---------------------

function addNewPrescription(medId){
	if(SELECTED_CONSULTATION == null)
		alert("Veuillez d'abords selectionner une consultation !");	
	else{
		$.post(
			CONSULTATIONS_SERVLET,
			{
				REQUEST: "INSERT_NEW_PRESCRIPTION",
				MEDECINE_ID: medId,
				CONSULTATION_ID: SELECTED_CONSULTATION.attr("consultationId")
			},
			function(result){
				if(result == "INSERT_SUCCESS"){
					getPrescriptions(SELECTED_CONSULTATION.attr("consultationId"));
				}
				else if(result == ERROR_MESSAGE){
					alert("Erreur de données");
				}	
		    }).fail(function() {
		        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
		});
	}
}

function addPrescription(medecineId, medDescription){
	var obsIndex = $("#prescriptionList li").length + 1;

	$("#prescriptionList")
		.append(
			$("<li>").addClass("collection-item valign-wrapper").attr("presIndex", obsIndex).attr("medecineId", medecineId)
				.append(
					$("<div>").addClass("col s11 left-align").append(medDescription)
				)
				.append(
					$("<div>").addClass("col s1 right-align")
						.append(
							$("<a>").addClass("waves-effect btn-small secondary-content red-text removeObsBtn").attr("presIndex", obsIndex)
								.click(
									function(){ 
										removePrescription($(this).attr("presIndex"));
									}
								)
								.append(
									$("<i>").addClass("material-icons").append("remove")
								)
						)
				)
		);
}

function getPrescriptions(consultationId){
	$.post(
			CONSULTATIONS_SERVLET,
			{
				REQUEST: "GET_PRESCRIPTIONS",
				CONSULTATION_ID: consultationId,
			},
			function(result){
				if(result == ERROR_MESSAGE)
					alert("Erreur... la prescription specifiée n'éxiste pas");
				else {
					removeAllPrescriptions();
					
					var xmlDoc = $.parseXML(result);
		            var xml = $(xmlDoc);
		            
					$(xml).find("medecine").each(function(){
						addPrescription(
							$(this).attr("medecineId"), $(this).find("medecineMark").text() + ' ' + $(this).find("medecineDosage").text() + ' ' + $(this).find("medecineForm").text()
						);
		            });
				}
			}
	).fail(function() {
        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
    });
}

function removeAllPrescriptions(){
	$("#prescriptionList li").remove();
}

function removePrescription(index){
	if(SELECTED_CONSULTATION == null)
		alert("Veuillez d'abords selectionner une consultation !");
	else{
		var consultationId = SELECTED_CONSULTATION.attr("consultationId");
		var prescription = $('li[presIndex="' + index + '"]');
		
		$.post(
			CONSULTATIONS_SERVLET,
			{
				REQUEST: "DELETE_PRESCRIPTION",
				CONSULTATION_ID: consultationId,
				MEDECINE_ID: prescription.attr("medecineId")
			},
			function(result){
				if(result == "REMOVE_SUCCESS")
					prescription.remove();
					
				else if(result == "ERROR_MESSAGE")
					alert("Impossible de supprimer l'element selectionne");
			}
		);
	}	
}

// ------------------ Some Craps --------------------

function initConsultationRows(){
	$(".contbody tr").click(function () {
		$(".contbody tr").css("background-color", "white");
		$(this).css("background-color", SELECT_TABLE_ROW_COLOR);

		if(SELECTED_CONSULTATION != null)
			SELECTED_CONSULTATION.removeAttr("id");

		SELECTED_CONSULTATION = $(this);
		SELECTED_CONSULTATION.attr("id", SELECTED_CONSULTATION_ID);
		
		getObservation($(this).attr("consultationId"));
		getPrescriptions($(this).attr("consultationId"));
		
		getText("GET_HM");
		getText("GET_EC");
		getText("GET_DIA");
    });

    $(".contbody tr").hover(function(){
    	$(".contbody tr").each(function(){
    		if(SELECTED_CONSULTATION == null || $(this).get(0) != SELECTED_CONSULTATION.get(0))
    			$(this).css("background-color", "white");
    	});

    	if(SELECTED_CONSULTATION != null && $(this).get(0) != SELECTED_CONSULTATION.get(0))
    		$(this).css("background-color", HOVERED_TABLE_ROW_COLOR);
    	else if(SELECTED_CONSULTATION == null)
    		$(this).css("background-color", HOVERED_TABLE_ROW_COLOR);
    });
}

function initPatientRows(){
	$(".pattbdoy tr").click(function () {
		$(".pattbdoy tr").css("background-color", "white");
		$(this).css("background-color", SELECT_TABLE_ROW_COLOR);

		if(SELECTED_PATIENT != null)
			SELECTED_PATIENT.removeAttr("id");

		SELECTED_PATIENT = $(this);
		SELECTED_PATIENT.attr("id", SELECTED_PATIENT_ID);
    });

    $(".pattbdoy tr").hover(function(){
    	$(".pattbdoy tr").each(function(){
    		if(SELECTED_PATIENT == null || $(this).get(0) != SELECTED_PATIENT.get(0))
    			$(this).css("background-color", "white");
    	});

    	if(SELECTED_PATIENT != null && $(this).get(0) != SELECTED_PATIENT.get(0))
    		$(this).css("background-color", HOVERED_TABLE_ROW_COLOR);
    	else if(SELECTED_PATIENT == null)
    		$(this).css("background-color", HOVERED_TABLE_ROW_COLOR);
    });
}

function initMedecineRows(){
	$(".medtbdoy tr a").click(function(){
		addNewPrescription($(this).parent().parent().attr("medecineId"));
	});
}