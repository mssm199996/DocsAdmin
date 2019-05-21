var SELECTED_DOCUMENT = null;
var SELECTED_DOCUMENT_ID = "sd";

var SELECT_TABLE_ROW_COLOR = "#cfd8dc";
var HOVERED_TABLE_ROW_COLOR = "#e0e0e0";

var DOCUMENTS_SERVLET = "documents";

var ERROR_MESSAGE = "ERROR";

(function(){
	this.initModals();
	this.initForms();
	this.initButtons();
	this.initTable();
	this.initTextFields();
})();

function initModals(){
	$(document).ready(function(){
	    $('#newUploadModal').modal();
	});
}

function initForms(){
	$("#formulaire").submit(function(e) {
	    var formObj = $(this);
	    var formURL = formObj.attr("action");
	    var formData = new FormData(this);
	    	    
	    $(".determinate").removeClass("determinate").addClass("indeterminate");
	    
	    $.ajax({
	        url: formURL,
	        type: 'POST',
	        data:  formData,
	        mimeType:"multipart/form-data",
	        contentType: false,
	        cache: false,
	        processData: false,
	        success: function(data, textStatus, jqXHR) {
	        	if(data == "INSERT_SUCCESS"){
	        		$.post(
	        				DOCUMENTS_SERVLET,
	        				{
	        					REQUEST: "GET_DOCUMENTS",
	        					DOCUMENT_NAME: $("#searchedDocument").val()
	        				},
	        				function(result){
	        					
	        					// Le resultat est un fichier xml
	        					removeAllDocuments();
	        		            
	        		            var xmlDoc = $.parseXML(result);
	        		            var xml = $(xmlDoc);
	        		            
	        		            $(xml).find("document").each( function(){
	        						
	        		                var idDocument = $(this).attr("documentId");
	        		                var extension = $(this).attr("documentExtension");
	        		                var nomDocument = $(this).find("documentName").text();
	        		                var dateDocument = $(this).find("documentUploadDate").text();
	        		                var descriptionDocument = $(this).find("documentDescription").text();						
	        		                		                
	        		                addDocument(idDocument, nomDocument, dateDocument, descriptionDocument, extension);
	        		                initTable();
	        		            });
	        				}
	        		).fail(function() {
	        	        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
	        	    });
	        		
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

function initButtons(){
	$("#deleteButton").click(function(){
		removeDocument();
	});
	
	$("#upload").click(function(){
		$("#formulaire").submit(); //Submit the form
	});
	
	$("#printButton").click(function(){
		printDocument();
	});
}

function initTextFields(){
	$("#searchedDocument").keyup(function(){
		$.post(
				DOCUMENTS_SERVLET,
				{
					REQUEST: "GET_DOCUMENTS",
					DOCUMENT_NAME: $("#searchedDocument").val()
				},
				function(result){										
					removeAllDocuments();
		            
		            var xmlDoc = $.parseXML(result);
		            var xml = $(xmlDoc);
		            
		            $(xml).find("document").each( function(){
		            	
		                var idDocument = $(this).attr("documentId");
		                var nomDocument = $(this).find("documentName").text();
		                var dateDocument = $(this).find("documentUploadDate").text();
		                var descriptionDocument = $(this).find("documentDescription").text();		
		                var extensionDocument = $(this).attr("documentExtension");
		                
		                addDocument(idDocument, nomDocument, dateDocument, descriptionDocument, extensionDocument);
		                initTable();
		            });
				}
		).fail(function() {
	        alert("Impossible de contacter le serveur... Veuillez réessayer plus tard.");
	    });
	});
}

function removeAllDocuments(){
	$(".docstbody tr").remove();
	SELECTED_DOCUMENT = null;
}

function removeDocument(){
	if(SELECTED_DOCUMENT == null)
		alert("Veuillez d'abords selectionner un document !");
	
	else {
		var documentId = SELECTED_DOCUMENT.attr("documentId");
		
		$.post(
			DOCUMENTS_SERVLET,
			{
				REQUEST: "DELETE_DOCUMENT",
				DOCUMENT_ID: documentId,
			},
			function(result){
				if(result == "REMOVE_SUCCESS")
					$("#" + SELECTED_DOCUMENT_ID).remove();
					
				else if(result == "ERROR_MESSAGE")
					alert("Impossible de supprimer l'element selectionne");
			}
		);		
	}
}

function addDocument(idDocument, nomDocument, dateDocument, descriptionDocument, extension){
	$('#documentsTable')
        .append($('<tr>').attr("documentId", idDocument).attr("documentExtension", extension)
        .append($('<td>').append($("#documentsTable tr").length))
        .append($('<td>').append(nomDocument))
        .append($('<td>').append(dateDocument))
        .append($('<td>').append(descriptionDocument))
    );
}

function printDocument(){
	if(SELECTED_DOCUMENT == null)
		alert("Veuillez d'abords selectionner un document !");
	else{
		var documentId = SELECTED_DOCUMENT.attr("documentId");
		var documentExtension = SELECTED_DOCUMENT.attr("documentExtension");
		
			var mywindow = window.open('', 'popup', 'height=480,width=640');
				mywindow.document.write('<html><head><title>DocsAdmin</title>');
			    mywindow.document.write('</head><body>');
			    mywindow.document.write('<img src="UploadedFiles/' + documentId + documentExtension + '" id="printArea"></img>');
			    mywindow.document.write('</body></html>');
			    $(mywindow).load('UploadedFiles/' + documentId + documentExtension);
		    
		var printArea = mywindow.document.getElementById("printArea");
			printArea.onload = function () {
				mywindow.print(); 
		    	mywindow.close();       
			};
	}
}

function initTable(){
	$(".docstbody tr").click(function () {
		$(".docstbody tr").css("background-color", "white");
		$(this).css("background-color", SELECT_TABLE_ROW_COLOR);

		if(SELECTED_DOCUMENT != null)
			SELECTED_DOCUMENT.removeAttr("id");

		SELECTED_DOCUMENT = $(this);
		SELECTED_DOCUMENT.attr("id", SELECTED_DOCUMENT_ID);
		
    });

    $(".docstbody tr").hover(function(){
    	$(".docstbody tr").each(function(){
    		if(SELECTED_DOCUMENT == null || $(this).get(0) != SELECTED_DOCUMENT.get(0))
    			$(this).css("background-color", "white");
    	});

    	if(SELECTED_DOCUMENT != null && $(this).get(0) != SELECTED_DOCUMENT.get(0))
    		$(this).css("background-color", HOVERED_TABLE_ROW_COLOR);
    	else if(SELECTED_DOCUMENT == null)
    		$(this).css("background-color", HOVERED_TABLE_ROW_COLOR);
    });
}