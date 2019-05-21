var SELECTED_PATIENT = null;
var SELECTED_PATIENT_ID = "sp";
var SELECTED_ANTECEDANT = null;

var SELECT_TABLE_ROW_COLOR = "#cfd8dc";
var HOVERED_TABLE_ROW_COLOR = "#e0e0e0";

var PATIENTS_SERVLET = "patients";

var ERROR_MESSAGE = "ERROR_MESSAGE";

(function(){
	this.initModels();
	this.initSelectors();
	this.initDatePickers();
	this.initTextFields();
	this.initButtons();
	this.initTables();
	this.initForms();
	this.refreshPatients("");
})();

function initModels(){
	$(document).ready(function(){
	    $('.modal').modal();
	});
}

function initSelectors(){
	$(document).ready(function() {
	    $('select').material_select();
	});
}

function initDatePickers(){
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

function initTextFields(){
	$("#searchedPatient").keyup(function(){
		refreshPatients($(this).val());
	});

	$("#antecedantDescriptionUpdater").keyup(function(){
		if(SELECTED_ANTECEDANT != null){
			updateAntecedant(
				SELECTED_ANTECEDANT.attr("antecedantType"), 
				SELECTED_ANTECEDANT.attr("antecedantStartDate"), 
				SELECTED_ANTECEDANT.attr("antecedantRecoveryDate"), 
				$(this).val()
			)
		}		
	});
	
	$("#patientProfession").keyup(function(){
		updatePersonnalInformations();
	});
	
    $("#patientPhoneNumber").keyup(function(){
    	updatePersonnalInformations();
    });
    
    $("#patientEmail").keyup(function(){
    	updatePersonnalInformations();
    });
    
    $("#patientAddress").keyup(function(){
    	updatePersonnalInformations();
    });
    
    $("#patientInsuranceCard").keyup(function(){
    	updatePersonnalInformations();
    });
}

function initButtons(){
	$("#removePatientBtn").click(function(){
		removeSpecifiedPatient();
	});

	$("#confirmAddNewPatientBtn").click(function(){
		patientFirstName = $("#patientFirstName").val();
		patientLastName = $("#patientFamillyName").val();
		patientBirthday = $("#patientBirthday").val();
		patientGender = $("#patientGender").val();

		if(patientFirstName != '' && patientFirstName != '' && patientBirthday != '')
			addNewPatient(patientLastName, patientFirstName, patientBirthday, patientGender);
	});

	$("#confirmerAddNewDiseaseBtn").click(function(){
		addNewDisease();
	});

	$("#confirmerAddNewBilanBtn").click(function(){
		addNewAnalysis();
	});

	$("#confirmAddNewAntecedantBtn").click(function(){
		addNewAntecedant();
	});
	
	$("#upload").click(function(){
		$("#formulaire").submit();
	});
}

function initForms(){	
	$("#formulaire").submit(function(e) {
		if(SELECTED_PATIENT == null){
			e.preventDefault();
			alert("Veuillez d'abords selectionner un patient");
			return;
		}
		
		$("#PATIENT_ID").val(SELECTED_PATIENT.attr("patientId"));
		
	    var formObj = $(this);
	    var formURL = formObj.attr("action");
	    var formData = new FormData(this);
	    	    
	    $(".determinate").removeClass("determinate").addClass("indeterminate");
	    	    
	    $.ajax({
	        url: formURL, 
	        type: 'POST',
	        data: formData,
	        mimeType:"multipart/form-data",
	        contentType: false,
	        cache: false,
	        processData: false,
	        success: function(data, textStatus, jqXHR) {	        	
	        	if(data == "INSERT_SUCCESS"){
	        		getRadios(SELECTED_PATIENT.attr("patientId"));
	        		
	        		$("#filename").val("");
		        	$("#filedescription").val("");
		        	$("#fileToUpload").val("");
		        	$("#fileToUploadWrapper").val("");
	        	}
	        	else if(data == "ERROR_MESSAGE")
	        		alert("Impossible d'uploader le document !");
	        	
	        	$(".indeterminate").removeClass("indeterminate").addClass("determinate");
	        },
	        error: function(jqXHR, textStatus, errorThrown) {
	        	alert("Impossible de joindre le serveur, veuillez réessayer ultérieurement !");
	        	$(".indeterminate").removeClass("indeterminate").addClass("determinate");
	        }          
	    });
	    e.preventDefault();
	});
}

function initTables(){
	this.initPatientTable();
}

// ------------------- Patients -------------------

function removeAllPatients(){
	$(".pattbody tr").remove();
	SELECTED_PATIENT = null;	
}

function removeSpecifiedPatient(){
	if(SELECTED_PATIENT == null)
		alert("Veuillez d'abords selectionner un patient !");
	
	else if(SELECTED_PATIENT != null){
		var patientId = SELECTED_PATIENT.attr("patientId");
		
		$.post(
			PATIENTS_SERVLET,
			{
				REQUEST: "DELETE_PATIENT",
				PATIENT_ID: patientId,
			},
			function(result){
				if(result == "REMOVE_SUCCESS")
					$("#" + SELECTED_PATIENT_ID).remove();
					
				else if(result == "ERROR_MESSAGE")
					alert("Impossible de supprimer l'element selectionné");
			}
		).fail(function() {
	        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
	    });
	}
}

function refreshPatients(searchedPatient){
	$.post(
		PATIENTS_SERVLET,
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
			    var genrePatient = $(this).find("patientGender").text();
			                
			    addPatientRow(id, nomPatient, prenomPatient, datePatient, genrePatient);
			});
			
			initPatientTable();
		}
	).fail(function() {
        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
    });
}

function addNewPatient(patientFirstName, patientLastName, patientBirthday, patientGender){
	$.post(
		PATIENTS_SERVLET,
		{
			REQUEST: "INSERT_NEW_PATIENT",
			PATIENT_FIRST_NAME: patientFirstName,
			PATIENT_LAST_NAME: patientLastName,
			PATIENT_BIRTHDAY: patientBirthday,
			PATIENT_GENDER: patientGender
		},
		function(result){
			if(result == "INSERT_SUCCESS")
				refreshPatients($("#searchedPatient").val());
			else alert("Impossible d'inserer le patient");
		}
	).fail(function(){
		alert("Impossible de contacter le serveur. Veuillez réessayer plus tard")
	});
}

function addPatientRow(id, nom, prenom, ddn, genre){
	$('#patientsTable')
        .append($('<tr>').attr("patientId", id)
        .append($('<td>').append($("#patientsTable tr").length))
        .append($('<td>').append(nom))
        .append($('<td>').append(prenom))
        .append($('<td>').append(ddn))
        .append($('<td>').append(genre == "FEMALE" ? "Femme" : "Homme"))
    );
}

// --------------------- Maladies -------------------------
function removeAllDiseases(){
	$("#diseasesList li").remove();
}

function removeSpecifiedDisease(diseaseId){
	if(SELECTED_PATIENT == null)
		alert("Veuillez d'abords selectionner un patient !");

	else{
		var patientId = SELECTED_PATIENT.attr("patientId");

		$.post(
			PATIENTS_SERVLET,
			{
				REQUEST: "DELETE_DISEASE",
				PATIENT_ID: patientId,
				DISEASE_ID: diseaseId
			},
			function(result){
				if(result == "REMOVE_SUCCESS")
					$("li[diseaseId = '" + diseaseId + "']").remove();
					
				else if(result == "ERROR_MESSAGE")
					alert("Impossible de supprimer l'element selectionne");
			}
		);
	}
}

function addNewDisease(){
	if(SELECTED_PATIENT == null)
		alert("Veuillez d'abords selectionner un patient !");
	else {
		var patientId = SELECTED_PATIENT.attr("patientId");
		var diseaseId = $("#diseaseChoiceList").val();
		
		$.post(
			PATIENTS_SERVLET,
			{
				REQUEST: "INSERT_NEW_DISEASE",
				PATIENT_ID: patientId,
				DISEASE_ID: diseaseId
			},
			function(result){
				if(result == "INSERT_SUCCESS")
					addDiseaseRow(diseaseId, $("#diseaseChoiceList option[value = '" + diseaseId + "']").text());
				else if(result == "ERROR_MESSAGE")
					alert("L'ajout est impossible !");
			}
		);
	}	
}

function addDiseaseRow(diseaseId, diseaseDescription){
	$("#diseasesList")
		.append($("<li>").attr("diseaseId", diseaseId).addClass("collection-item valign-wrapper")
		.append($("<div>").addClass("col s11 left-align")
			.append(diseaseDescription))
		.append($("<div>").addClass("col s1 right-align")
			.append($("<a>").addClass("waves-effect btn-small secondary-content red-text removeDiseaseBtn").click(function(){
				removeSpecifiedDisease($(this).parent().parent().attr("diseaseId"));
			}).append($("<i>").addClass("material-icons")
				.append("remove")))
	));
}

function getDiseases(patientId){	
	$.post(
			PATIENTS_SERVLET,
			{
				REQUEST: "GET_DISEASES",
				PATIENT_ID: patientId,
			},
			function(result){
				if(result == ERROR_MESSAGE)
					alert("Erreur... le patient specifiée n'éxiste pas");
				else {					
					removeAllDiseases();
					
					var xmlDoc = $.parseXML(result);
		            var xml = $(xmlDoc);
		            
					$(xml).find("disease").each( function(){
		                addDiseaseRow($(this).attr("diseaseId"), $(this).find("diseaseName").text());
		            });
				}
			}
	).fail(function() {
        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
    });
}

// ------------------------ Bilans -----------------------

function removeAllAnalysis(){
	$("#analysisList li").remove();
}

function removeSpecifiedAnalysis(analysisId){
	if(SELECTED_PATIENT == null)
		alert("Veuillez d'abords selectionner un patient !");

	else{
		var patientId = SELECTED_PATIENT.attr("patientId");
		
		$.post(
			PATIENTS_SERVLET,
			{
				REQUEST: "DELETE_ANALYSIS",
				PATIENT_ID: patientId,
				ANALYSIS_ID: analysisId
			},
			function(result){
				if(result == "REMOVE_SUCCESS")
					$("li[analysisId = '" + analysisId + "']").remove();
					
				else if(result == "ERROR_MESSAGE")
					alert("Impossible de supprimer l'element selectionne");
			}
		);
	}
}

function addNewAnalysis(){
	if(SELECTED_PATIENT == null)
		alert("Veuillez d'abords selectionner un patient !");
	else {
		var patientId = SELECTED_PATIENT.attr("patientId");
		var analysisId = $("#analysisChoiceList").val();
		var analysisDate = $("#analysisDate").val();
		var analysisResult = $("#analysisResult").val();

		$.post(
			PATIENTS_SERVLET,
			{
				REQUEST: "INSERT_NEW_ANALYSIS",
				PATIENT_ID: patientId,
				ANALYSIS_ID: analysisId,
				ANALYSIS_DATE: analysisDate,
				ANALYSIS_RESULT: analysisResult
			},
			function(result){
				if(result == "INSERT_SUCCESS")
					addAnalysisRow(
						analysisId, 
						analysisDate, 
						$("#analysisChoiceList option[value = '" + analysisId + "']").text(),
						analysisResult
					);
				else if(result == "ERROR_MESSAGE")
					alert("L'ajout est impossible !");
			}
		);
	}	
}

function addAnalysisRow(analysisId, analysisDate, analysisName, analysisResult){
	$("#analysisList")
		.append($("<li>").attr("analysisId", analysisId).addClass("collection-item valign-wrapper")
		.append($("<div>").addClass("col s11 left-align")
			.append(analysisName + " <br/>" + analysisDate + " <br/>" + analysisResult))
		.append($("<div>").addClass("col s1 right-align")
			.append($("<a>").addClass("waves-effect btn-small secondary-content red-text removeAnalysisBtn").click(function(){
				removeSpecifiedAnalysis($(this).parent().parent().attr("analysisId"));
			}).append($("<i>").addClass("material-icons")
				.append("remove")))
	));
}

function getAnalysis(patientId){	
	$.post(
			PATIENTS_SERVLET,
			{
				REQUEST: "GET_ANALYSIS",
				PATIENT_ID: patientId,
			},
			function(result){
				if(result == ERROR_MESSAGE)
					alert("Erreur... le patient specifiée n'éxiste pas");
				else {
					removeAllAnalysis();
					
					var xmlDoc = $.parseXML(result);
		            var xml = $(xmlDoc);
		            
					$(xml).find("analysis").each( function(){						
		                addAnalysisRow(
		                	$(this).find("analysisType").find("analysisTypeName").text(), 
		                	$(this).find("analysisDate").text(),
		                	$(this).find("analysisType").find("analysisTypeName").text(), 
		                	$(this).find("analysisResult").text()
		                );
		            });
				}
			}
	).fail(function() {
        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
    });
}

// --------------------- Antecedants ---------------------

function removeAllAntecedants(){
	$("#antecedantsList li").remove();
}

function removeSpecifiedAntecedant(antecedantType, antecedantStartDate, antecedantRecoveryDate){
	if(SELECTED_PATIENT == null)
		alert("Veuillez d'abords selectionner un patient !");

	else{
		var patientId = SELECTED_PATIENT.attr("patientId");
		
		$.post(
			PATIENTS_SERVLET,
			{
				REQUEST: "DELETE_ANTECEDANT",
				PATIENT_ID: patientId,
				ANTECEDANT_START_DATE: antecedantStartDate,
				ANTECEDANT_RECOVERY_DATE: antecedantRecoveryDate,
				ANTECEDANT_TYPE: antecedantType
			},
			function(result){
				if(result == "REMOVE_SUCCESS")
					$(("li[antecedantStartDate = " + antecedantStartDate + "][antecedantRecoveryDate = " 
							+ antecedantRecoveryDate + "][antecedantType = " + antecedantType + "]")).remove();
				else if(result == "ERROR_MESSAGE")
					alert("Impossible de supprimer l'element selectionne");
			}
		).fail(function() {
	        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
	    });
	}
}

function addNewAntecedant(){
	if(SELECTED_PATIENT == null)
		alert("Veuillez d'abords selectionner un patient !");
	else {
		var patientId = SELECTED_PATIENT.attr("patientId");
		var antecedantType = $("#antecedantTypeChoiceList").val();
		var antecedantStartDate = $("#antecedantStartDate").val();
		var antecedantRecoveryDate = $("#antecedantRecoveryDate").val();
		var antecedantDescription = $("#antecedantDescription").val();

		$.post(
			PATIENTS_SERVLET,
			{
				REQUEST: "INSERT_NEW_ANTECEDANT",
				PATIENT_ID: patientId,
				ANTECEDANT_TYPE: antecedantType,
				ANTECEDANT_START_DATE: antecedantStartDate,
				ANTECEDANT_RECOVERY_DATE: antecedantRecoveryDate,
				ANTECEDANT_DESCRIPTION: antecedantDescription
			},
			function(result){
				if(result == "INSERT_SUCCESS")
					getAntecedants(patientId);
				else if(result == "ERROR_MESSAGE")
					alert("L'ajout est impossible !");
			}
		);
	}	
}

function addAntecedantRow(antecedantType, antecedantStartDate, antecedantRecoveryDate, antecedantDescription){
	var antecedantTypeIndex = -1;
	
	if(antecedantType == "FAMILIAL")
		antecedantTypeIndex = 0;
	else if(antecedantType == "PERSONEL")
		antecedantTypeIndex = 1;
	else if(antecedantType == "MEDICAL")
		antecedantTypeIndex = 2;
	else if(antecedantType == "CHIRURGICAL")
		antecedantTypeIndex = 3;
	
	$("#antecedantsList")
		.append($("<li>").addClass("collection-item valign-wrapper").
			attr("antecedantType", antecedantTypeIndex).attr("antecedantStartDate", antecedantStartDate).attr("antecedantRecoveryDate", antecedantRecoveryDate)
		.append($("<div>").addClass("col s11 left-align")
			.append("Antecedant " + antecedantType + "<br/>Debut: " + antecedantStartDate + "<br/>Fin: " + antecedantRecoveryDate))
		.append($("<div>").addClass("col s1 right-align")
			.append($("<a>").addClass("waves-effect btn-small secondary-content red-text removeAnalysisBtn").click(function(){
				removeSpecifiedAntecedant(antecedantTypeIndex, antecedantStartDate, antecedantRecoveryDate);
			}).append($("<i>").addClass("material-icons")
				.append("remove")))
			.append($("<a>").addClass("waves-effect btn-small secondary-content blue-text modal-trigger displayAntecedantBtn").attr("href","#updateAntecedantModel").click(function(){
				$("#antecedantDescriptionUpdater").val(antecedantDescription);
				$("#antecedantDescriptionUpdater").parent().find("label").addClass("active");
				
				SELECTED_ANTECEDANT = $(this).parent().parent();
			}).append($("<i>").addClass("material-icons")
				.append("help")))
	));
}

function updateAntecedant(antecedantType, antecedantStartDate, antecedentRecoveryDate, antecedantNewDescription){
	if(SELECTED_PATIENT == null)
		alert("Veuillez d'abords selectionner un patient !");

	else{
		var patientId = SELECTED_PATIENT.attr("patientId");

		$.post(
			PATIENTS_SERVLET,
			{
				REQUEST: "UPDATE_ANTECEDANT",
				PATIENT_ID: patientId,
				ANTECEDANT_TYPE: antecedantType,
				ANTECEDANT_START_DATE: antecedantStartDate,
				ANTECEDANT_RECOVERY_DATE: antecedentRecoveryDate,
				ANTECEDANT_NEW_DESCRIPTION: antecedantNewDescription
			},
			function(result){
				 if(result == "ERROR_MESSAGE")
					alert("Impossible de supprimer l'element selectionne");
			}
		).fail(function() {
	        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
	    });
	}
}

function getAntecedants(patientId){	
	$.post(
			PATIENTS_SERVLET,
			{
				REQUEST: "GET_ANTECEDANTS",
				PATIENT_ID: patientId,
			},
			function(result){
				if(result == ERROR_MESSAGE)
					alert("Erreur... le patient specifiée n'éxiste pas");
				else {
					removeAllAntecedants();
					
					var xmlDoc = $.parseXML(result);
		            var xml = $(xmlDoc);
		            
					$(xml).find("antecedant").each(function(){
		                addAntecedantRow(
		                	$(this).find("antecedentType").text(),
		                	$(this).find("antecedentStartDate").text(),
		                	$(this).find("antecedentRecoveryDate").text(), 
		                	$(this).find("antecedentDescription").text()
		                );
		            });
				}
			}
	).fail(function() {
        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
    });
}

// --------------------- Personnel informations ---------------------

function getPersonnalInformations(patientId){
	$.post(
			PATIENTS_SERVLET,
			{
				REQUEST: "GET_PERSONNAL_INFORMATIONS",
				PATIENT_ID: patientId,
			},
			function(result){
				if(result == ERROR_MESSAGE)
					alert("Erreur... le patient specifiée n'éxiste pas");
				else {					
					var xmlDoc = $.parseXML(result);
		            var xml = $(xmlDoc);
		            
					$(xml).find("patient").each(function(){
						$("#patientProfession").val($(this).find("patientProfession").text());
			            $("#patientPhoneNumber").val($(this).find("patientPhoneNumber").text());
			            $("#patientEmail").val($(this).find("patientEmail").text());
			            $("#patientAddress").val($(this).find("patientAddress").text());
			            $("#patientInsuranceCard").val($(this).find("patientInsuranceCard").text());
			            
			            $("label[for=patientProfession]").addClass("active");
			            $("label[for=patientPhoneNumber]").addClass("active");
			            $("label[for=patientEmail]").addClass("active");
			            $("label[for=patientAddress]").addClass("active");
			            $("label[for=patientInsuranceCard]").addClass("active");
		            });
				}
			}
	).fail(function() {
        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
    });
}

function updatePersonnalInformations(){
	if(SELECTED_PATIENT == null)
		alert("Veuillez d'abords selectionner un patient !");

	else{
		var patientId = SELECTED_PATIENT.attr("patientId");
		var patientAddress = $("#patientAddress").val();
		var patientProfession = $("#patientProfession").val();
		var patientPhoneNumber = $("#patientPhoneNumber").val();
		var patientEmail = $("#patientEmail").val();
		var patientInsuranceCard = $("#patientInsuranceCard").val();

		$.post(
			PATIENTS_SERVLET,
			{
				REQUEST: "UPDATE_PERSONNAL_INFORMATIONS",
				PATIENT_ID: patientId,
				PATIENT_ADDRESS: patientAddress,
				PATIENT_PROFESSION: patientProfession,
				PATIENT_PHONE_NUMBER: patientPhoneNumber,
				PATIENT_EMAIL: patientEmail,
				PATIENT_INSURANCE_CARD: patientInsuranceCard
			},
			function(result){
				if(result == "ERROR_MESSAGE")
					alert("Impossible de supprimer l'element selectionne");
			}
		).fail(function() {
	        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
	    });
	}
}

// ------------------ Radios -------------------------

function removeAllRadios(){
	$("#radiosList li").remove();
}

function getRadios(patientId){	
	$.post(
			PATIENTS_SERVLET,
			{
				REQUEST: "GET_RADIOS",
				PATIENT_ID: patientId,
			},
			function(result){
				if(result == ERROR_MESSAGE)
					alert("Erreur... le patient specifiée n'éxiste pas");
				else {					
					removeAllRadios();
					
					var xmlDoc = $.parseXML(result);
		            var xml = $(xmlDoc);
		            
					$(xml).find("media").each(function(){
		                addRadioRow(
		                	$(this).attr("mediaFilePath"),
		                	$(this).attr("mediaFileExtension"),
		                	$(this).find("mediaDate").text(),
		                	$(this).find("mediaDescription").text()
		                );
		            });
				}
			}
	).fail(function() {
        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
    });
}

function addRadioRow(mediaId, mediaExtension, mediaDate, mediaDescription){
	$("#radiosList")
		.append($("<li>").attr("radioId", mediaId).attr("radioExtension", mediaExtension).addClass("collection-item valign-wrapper")
			.append($("<div>").addClass("col s11 left-align")
				.append(mediaDate + ": " + mediaDescription)
			)
			.append($("<div>").addClass("col s1 right-align")
				.append($("<a>").addClass("waves-effect btn-small secondary-content blue-text")
						.click(function(){
							viewRadio(mediaId, mediaExtension);
						})
					.append($("<i>").addClass("material-icons").append("help"))
				)
				.append($("<a>").addClass("waves-effect btn-small secondary-content red-text removeRadioBtn")
						.click(function(){
							removeSpecifiedRadio(mediaId);
						})
					.append($("<i>").addClass("material-icons").append("remove"))
				)
			)
		);
	
}

function removeSpecifiedRadio(radioId){
	if(SELECTED_PATIENT == null)
		alert("Veuillez d'abords selectionner un patient !");

	else{		
		$.post(
			PATIENTS_SERVLET,
			{
				REQUEST: "DELETE_RADIO",
				RADIO_ID: radioId
			},
			function(result){
				if(result == "REMOVE_SUCCESS")
					getRadios(SELECTED_PATIENT.attr("patientId"));
				else if(result == "ERROR_MESSAGE")
					alert("Impossible de supprimer l'element selectionne");
			}
		).fail(function() {
	        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
	    });
	}
}

function viewRadio(radioFilePath, radioFileExtension){
	var mywindow = window.open('', 'popup', 'height=480,width=640');
		mywindow.document.write('<html><head><title>DocsAdmin</title>');
		mywindow.document.write('</head><body>');
		mywindow.document.write('<img src="UploadedFiles/' + radioFilePath + radioFileExtension + '" id="printArea"></img>');
		mywindow.document.write('</body></html>');
	$(mywindow).load('UploadedFiles/' + radioFilePath + radioFileExtension);
}

// --------------------- Autre chose ---------------------

function initPatientTable(){
	$(".pattbody tr").click(function () {
		$(".pattbody tr").css("background-color", "white");
		$(this).css("background-color", SELECT_TABLE_ROW_COLOR);

		if(SELECTED_PATIENT != null)
			SELECTED_PATIENT.removeAttr("id");

		getDiseases($(this).attr("patientId"));
		getAnalysis($(this).attr("patientId"));
		getAntecedants($(this).attr("patientId"));
		getPersonnalInformations($(this).attr("patientId"));
		getRadios($(this).attr("patientId"));

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

