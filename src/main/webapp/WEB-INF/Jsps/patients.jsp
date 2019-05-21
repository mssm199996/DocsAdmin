<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="platformskin.jsp" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
      	<link href="css/patients.css" rel="stylesheet" type="text/css" />
        <title>Docs Admin</title>
	</head>
	<body>
		<div class="fixed-action-btn horizontal">
		    <a class="btn-floating btn-large cyan pulse"><i class="large material-icons">menu</i></a>
		    <ul>
		      	<li><a id="removePatientBtn" class="waves-effect waves-light btn-floating red"><i class="material-icons">remove</i></a></li>
		      	<li><a href="#newPatientModal" class="waves-effect waves-light btn-floating green modal-trigger"><i class="material-icons">add</i></a></li>
		    </ul>
	    </div>

	    <div class="row col s12">
	    	<div class="row searchContainer">
	    		<div class="col s12">
    				<div class="input-field col s12">
    					<i class="material-icons prefix">search</i>
			          	<input id="searchedPatient" type="text" class="validate"/>
			          	<label for="searchedPatient">Nom|Prenom du patient</label>
			        </div>
	    		</div>
	    	</div>

	    	<div class="col s9 center-align centerContainer">
	    		<div class="row">
	    			<table id="patientsTable" class="responsive-table centered bordered z-depth-2">
				        <thead>
				          	<tr>
				            	<th><img src="img/patients/numero.png" class="theaders"/>N°</th>
				              	<th><img src="img/patients/Name.png" class="theaders"/>om</th>
				              	<th><img src="img/patients/Prenom.png" class="theaders"/>renom</th>
				              	<th><img src="img/patients/ddn.png" class="theaders"/>Date de naissance</th>
				              	<th><img src="img/patients/sexe.png" class="theaders"/>Genre</th>
				          	</tr>
				        </thead>

				        <tbody class="pattbody">
				        </tbody>
				    </table>
	    		</div>
	    	</div>

	    	<div class="col s3 right-align">
		    	<div class="row">
		      		<ul class="collapsible z-depth-2" data-collapsible="accordion">
		      			<li>
		      				<div class="collapsible-header">
		      					<img src="img/patients/help.png">
		      					<span class="accheaders">Informations supplementaires</span>
		      				</div>

	      					<div class="collapsible-body row">
	      						<div class="col s12 left-align listContainer">
	      							<ul class="collection">
	      								<div class="row infosdivs">
	      									<img src="img/patients/Addresse.png" class="col s2">
	      									<div class="input-field col s10">
									          	<input id="patientAddress" type="text" class="validate col s12"/>
									          	<label for="patientAddress">Addresse</label>
									        </div>
	      								</div>
	      								
	      								<div class="row infosdivs">
	      									<img src="img/patients/Metier.png" class="col s2">
	      									<div class="input-field col s10">
									          	<input id="patientProfession" type="text" class="validate"/>
									          	<label for="patientProfession">Metier</label>
									        </div>
	      								</div>
								        
								        <div class="row infosdivs">
								        	<img src="img/patients/NDT.png" class="col s2" ndt="">
								        	<div class="input-field col s10">
									          	<input id="patientPhoneNumber" type="text" class="validate"/>
									          	<label for="patientPhoneNumber">N° de telephone</label>
									        </div>
								        </div>
								        
								        <div class="row infosdivs">
								        	<img src="img/patients/gmail.png" class="col s2">
								        	<div class="input-field col s10">
									          	<input id="patientEmail" type="text" class="validate"/>
									          	<label for="patientEmail">Email</label>
									        </div> 
								        </div>
								        
								        <div class="row infosdivs">
								        	<img src="img/patients/Assurance.png" class="col s2">
								        	<div class="input-field col s10">
									          	<input id="patientInsuranceCard" type="text" class="validate"/>
									          	<label for="patientInsuranceCard">N° de la carte d'assurance</label>
									        </div> 
								        </div>
								    </ul>
	      						</div>
	      					</div>
		      			</li>
		      			
		      			<li>
		      				<div class="collapsible-header">
		      					<img src="img/patients/Maladies.png">
		      					<span class="accheaders">Maladies</span>
		      				</div>

	      					<div class="collapsible-body row">
	      						<div class="col s12 left-align listContainer">
	      							<ul id="diseasesList" class="collection">
								    </ul>
	      						</div>
	      						<a class="waves-effect waves-light btn green col s12 center modal-trigger" href="#newDiseaseModel"><i class="material-icons">add</i></a>
	      					</div>
		      			</li>

		      			<li>
		      				<div class="collapsible-header">
		      					<img src="img/patients/Bilan.png">
		      					<span class="accheaders">Analyses</span>
		      				</div>

	      					<div class="collapsible-body row">
	      						<div class="col s12 left-align listContainer">
	      							<ul id="analysisList" class="collection">
								    </ul>
	      						</div>
	      						<a class="waves-effect waves-light btn green col s12 center modal-trigger" href="#newAnalysisModal"><i class="material-icons">add</i></a>
	      					</div>
		      			</li>

		      			<li>
		      				<div class="collapsible-header">
		      					<img src="img/patients/Antecedants.png">
		      					<span class="accheaders">Antecedants</span>
		      				</div>

	      					<div class="collapsible-body row">
	      						<div class="col s12 left-align listContainer">
	      							<ul id="antecedantsList" class="collection">
								    </ul>
	      						</div>
	      						<a class="waves-effect waves-light btn green col s12 center modal-trigger" href="#newAntecedantModal"><i class="material-icons">add</i></a>
	      					</div>
		      			</li>

		      			<li>
		      				<div class="collapsible-header">
		      					<img src="img/patients/Radio.png">
		      					<span class="accheaders">Radios</span>
		      				</div>

	      					<div class="collapsible-body row">
	      						<div class="col s12 left-align listContainer">
	      							<ul id="radiosList" class="collection">
								    </ul>
	      						</div>
	      						<a class="waves-effect waves-light btn green col s12 center modal-trigger" href="#newRadioModal"><i class="material-icons">add</i></a>
	      					</div>
		      			</li>

		    	  	</ul>		    	  		
		    	</div>
	    	</div>

	    	<div id="newPatientModal" class="modal">
			    <div class="modal-content">
			      	<div class="row">
			      		<h4>Nouveau Patient</h4>
			    	</div>

			    	<div class="model-content-table-container col s12">
			    		<div class="row">
					        <div class="input-field col s12">
					          	<i class="material-icons prefix">account_circle</i>
						        <input id="patientFirstName" type="text" class="validate">
						        <label for="patientFirstName">Nom</label>
					        </div>

					        <div class="input-field col s12">
					          	<i class="material-icons prefix">account_circle</i>
						        <input id="patientFamillyName" type="text" class="validate">
						        <label for="patientFamillyName">Prenom</label>
					        </div>

					        <div class="row">
					        	<div class="input-field col s4">
					        		<i class="material-icons prefix">account_circle</i>
									<input id="patientBirthday" type="date" class="datepicker" value="today">
									<label for="patientBirthday">Date de naissance</label>
					        	</div>
					        	
					        	<div class="input-field col s4">
								    <select id="patientGender">
								      	<option value="1">Masculin</option>
								      	<option value="2">Feminin</option>
								    </select>
								    <label>Genre</label>
								</div>

					        	<div class="col s4">
					        		<a id="confirmAddNewPatientBtn" class="waves-effect waves-light btn green col s12 center modal-trigger">Confirmer</a>
					        	</div>
							</div>

      					</div>
			    	</div>
			    </div>
			</div>

			<div id="newDiseaseModel" class="modal">
			    <div class="modal-content">
			    	<h4>Nouvelle maladie</h4>
			    </div>
			    
			    <div class="model-content-table-container col s12 center-align">
			    	<div class="model-content-table-container col s9 center-align">
				    	<div class="input-field col s12">
						    <select id="diseaseChoiceList">
						      	<c:forEach items="${requestScope.DISEASES_LIST}" var="disease" varStatus="status">
						    		<option value="${disease.diseaseId}" selected >${disease.diseaseName}</option>
				        		</c:forEach>
						    </select>
						    <label>Liste des maladies</label>
						</div>
			      	</div>
			      	<div class="col s3">
			      		<a id="confirmerAddNewDiseaseBtn" class="modal-close waves-effect waves-green btn">Confirmer</a>
			      	</div>
			    </div>
			</div>

			<div id="newAnalysisModal" class="modal">
			    <div class="modal-content">
			    	<h4>Nouveau bilan</h4>
			    </div>
			    
			    <div class="model-content-table-container col s12 center-align">
			    	<div class="model-content-table-container col s9 center-align row">
				    	<div class="input-field col s4">
						    <select id="analysisChoiceList">
						      	<c:forEach items="${requestScope.ANALYSIS_TYPES}" var="anaType" varStatus="status">
						    		<option value="${anaType.analysisTypeName}" selected >${anaType.analysisTypeName}</option>
				        		</c:forEach>
						    </select>
						    <label>Liste des analyses</label>
						</div>
						<div class="input-field col s2">
							<input id="analysisDate" type="date" class="datepicker">
							<label>Date de l'analyse</label>
						</div>
						<div class="input-field col s6">
						    <input id="analysisResult" type="text" class="validate">
          					<label for="analysisResult">Resultat de l'analyse</label>
						</div>
			      	</div>
			      	<div class="col s3">
			      		<a id="confirmerAddNewBilanBtn" class="modal-close waves-effect waves-green btn">Confirmer</a>
			      	</div>
			    </div>
			</div>

			<div id="newAntecedantModal" class="modal">
			    <div class="modal-content">
			    	<h4>Nouveau antecedant</h4>
			    </div>
			    
			    <div class="model-content-table-container col s12 center-align row">
			    	<div class="model-content-table-container col s12 center-align row no-bottom-margin">
			    		<div class="model-content-table-container col s9 center-align row no-bottom-margin">
					    	<div class="input-field col s4">
							    <select id="antecedantTypeChoiceList">
							      	<option value="0" selected>Familial</option>
							      	<option value="1">Personel</option>
							      	<option value="2">Medical</option>
							      	<option value="3">Chirurgical</option>
							    </select>
							    <label>Type d'antecedant</label>
							</div>
							<div class="input-field col s4">
								<input id="antecedantStartDate" type="date" class="datepicker">
								<label>Date de debut</label>
							</div>
							<div class="input-field col s4">
								<input id="antecedantRecoveryDate" type="date" class="datepicker">
								<label>Date de retablissement</label>
							</div>
				      	</div>
				      	<div class="col s3">
				      		<a id="confirmAddNewAntecedantBtn" class="modal-close waves-effect waves-green btn">Confirmer</a>
				      	</div>
			    	</div>
			    	<div class="input-field col s12">
			         	<textarea id="antecedantDescription" class="materialize-textarea"></textarea>
			          	<label for="antecedantDescription">Description de l'antécédant</label>
			        </div>
			    </div>
			</div>

			<div id="updateAntecedantModel" class="modal">
				<div class="modal-content">
					<h4 class="center">
						<a class="waves-effect waves-light btn-floating green headerIcon"><i class="material-icons">add_circle</i></a>
							Mise à jour d'un antecedant
						<a class="waves-effect waves-light btn-floating green headerIcon"><i class="material-icons">add_circle</i></a>
					</h4>

					<div class="row">
						<div class="input-field col s12">
						    <textarea class="materialize-textarea" name="antecedantDescriptionUpdater" id="antecedantDescriptionUpdater"></textarea>
						    <label for="antecedantDescriptionUpdater">Description de l'antecedant</label>
						</div>
					</div>
				</div>				
			</div>

			<div id="newRadioModal" class="modal">
				<div class="modal-content">
					<h4 class="center">
						<a class="waves-effect waves-light btn-floating green headerIcon"><i class="material-icons">add_circle</i></a>
							Uploader une radio
						<a class="waves-effect waves-light btn-floating green headerIcon"><i class="material-icons">add_circle</i></a>
					</h4>
				    <div class="row"> 
						<form name="formulaire" id="formulaire" method="POST" action="patients" enctype="multipart/form-data">
							<div class="row formRow ichiraku">
								<div class="input-field col s12 ichiraku">
									<input type= "hidden" name="REQUEST" id="REQUEST" value="UPLOAD_RADIO" /> 
								</div>
							</div>
							
							<div class="row formRow ichiraku">
								<div class="input-field col s12 ichiraku">
									<input type= "hidden" name="PATIENT_ID" id="PATIENT_ID" value="" /> 
								</div>
							</div>
							
							<div class="row formRow">
								<div class="input-field col s12">
									<input id="RADIO_DATE" name = "RADIO_DATE" type="date" class="datepicker" value="today">
									<label for="RADIO_DATE">Date de la radio</label>
								</div>
							</div>
							
							<div class="row formRow">
								<div class="input-field col s12">
						          	<textarea class="materialize-textarea" name="RADIO_DESCRIPTION" id="radiodescription"></textarea>
						          	<label for="radiodescription">Description de la radio</label>
						        </div>
							</div>
							
					        <div class="row formRow">
					        	<div class="file-field input-field">
								    <div class="btn">
								        <span>Fichier</span>
								        <input type="file" name="RADIO_CONTENT" id="radioToUpload" />
								    </div>
								    <div class="file-path-wrapper">
								        <input class="file-path validate" type="text" id="radioToUploadWrapper">
									</div>
								</div>
					        </div>
					        
					        <div class="row formRow col s12 valign-wrapper">
					        	<a id="upload" name = "upload" class=" col s2 waves-effect waves-light modal-action btn cyan"><i class="material-icons">cloud_upload</i></a>
					        	<div class="progress">
									<div class="determinate" style="width: 0%"> </div>
								</div>
					        </div>
				        </form>
				    </div>
				</div>
			</div>
	    </div>
        <script src="js/patients.js"></script>
	</body>
</html>