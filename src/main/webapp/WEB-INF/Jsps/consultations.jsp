<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="platformskin.jsp" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
      	<link href="css/consultations.css" rel="stylesheet" type="text/css" />
        <title>Docs Admin</title>
	</head>
	<body>
	    <div class="fixed-action-btn horizontal">
		    <a class="btn-floating btn-large cyan pulse"><i class="large material-icons">menu</i></a>
		    <ul>
		      	<li><a id="removeConsuBtn" class="waves-effect waves-light btn-floating red"><i class="material-icons">remove</i></a></li>
		      	<li><a href="#newConsuModal" class="waves-effect waves-light btn-floating green modal-trigger"><i class="material-icons">add</i></a></li>
		    </ul>
	    </div>

	    <div class="row col s12">
	    	<div class="row searchContainer">
	    		<div class="col s9">
    				<div class="input-field col s12">
    					<i class="material-icons prefix">search</i>
			          	<input id="searchedPatient" type="text" class="validate"/>
			          	<label for="patient">Nom du patient</label>
			        </div>
	    		</div>

	    		<div class="row col s3">
    				<div class="input-field col s12">
    					<i class="material-icons prefix">date_range</i>
    					<label for="consultationDate">Date de la consultation</label>
			          	<input id="consultationDate" type="text" class="datepicker">
			        </div>
	    		</div>
	    	</div>

	    	<div class="col s9 center-align centerContainer">
	    		<div class="row">
	    			<table id="consultationsTable" class="responsive-table centered bordered z-depth-2">
				        <thead>
				          	<tr>
				            	<th><img src="img/consultations/numero.png" class="theaders"/>N°</th>
				              	<th><img src="img/consultations/Name.png" class="theaders"/>om</th>
				              	<th><img src="img/consultations/Prenom.png" class="theaders"/>renom</th>
				              	<th><img src="img/consultations/Date.png" class="theaders"/>Date</th>
				              	<th><img src="img/consultations/income.png" class="theaders"/>Prix</th>
				          	</tr>
				        </thead>

				        <tbody class="contbody"></tbody>
				    </table>
	    		</div>
	    	</div>

	    	<div class="col s3 right-align">
		    	<div class="row">
		      		<ul class="collapsible z-depth-2" data-collapsible="accordion">
		      			<li>
		      				<div class="collapsible-header">
		      					<img src="img/consultations/Observation.png">
		      					<span class="accheaders">Observations</span>
		      				</div>

	      					<div class="collapsible-body row">
	      						<div class="col s12 left-align listContainer">
	      							<ul id="observationList" class="collection">
								    </ul>
	      						</div>
	      						<a class="waves-effect waves-light btn green col s12 center modal-trigger" href="#newObsModal"><i class="material-icons">add</i></a>
	      					</div>
		      			</li>

		      			<li>
		      				<div class="collapsible-header">
		      					<img src="img/consultations/HM.png">
		      					<span class="accheaders">Histoire de la maladie</span>
		      				</div>
	      					<div class="collapsible-body row">
		      					<div class="input-field col s12">
						          	<textarea id="hm" class="materialize-textarea"></textarea>
						          	<label for="ec">Histoire de la maladie</label>
						        </div>
	      					</div>
		      			</li>
		      			
		      			<li>
		      				<div class="collapsible-header">
		      					<img src="img/consultations/Trai.png">
		      					<span class="accheaders">Traitements</span>
		      				</div>

	      					<div class="collapsible-body row">
	      						<div class="col s12 left-align listContainer">
	      							<ul id="prescriptionList" class="collection"></ul>
	      						</div>
	      						<a class="waves-effect waves-light btn green col s12 center modal-trigger" href="#newPrescriptionModal"><i class="material-icons">add</i></a>
	      					</div>
		      			</li>	

		      			<li>
		      				<div class="collapsible-header">
		      					<img src="img/consultations/EC.png">
		      					<span class="accheaders">Examen clinique</span>
		      				</div>
	      					<div class="collapsible-body row">
	      						<div class="input-field col s12">
					          		<textarea id="ec" class="materialize-textarea"></textarea>
					          		<label for="ec">Examen clinique</label>
					        	</div>
	      					</div>
		      			</li>

		      			<li>
		      				<div class="collapsible-header">
		      					<img src="img/consultations/Dia.png">
		      					<span class="accheaders">Diagnostique</span>
		      				</div>
	      					<div class="collapsible-body row">
	      						<div class="input-field col s12">
					          		<textarea id="dia" class="materialize-textarea"></textarea>
					          		<label for="dia">Diagnostique</label>
					        	</div>
	      					</div>
		      			</li>
		    	  	</ul>		    	  		
		    	</div>
	    	</div>

	    	<div id="newConsuModal" class="modal">
			    <div class="modal-content">
			      	<div class="row">
			      		<h4>Nouvelle consultation</h4>
					   	<div class="col s7">
				    		<div class="input-field col s12">
				   				<i class="material-icons prefix">search</i>
					          	<input id="newConsultationSearchedPatient" type="text"/>
					          	<label for="newConsultationSearchedPatient">Nom du patient</label>
					        </div>
			    		</div>

				    	<div class="row col s3">
				    		<div class="input-field col s12">
				    			<i class="material-icons prefix">search</i>
					          	<input id="price" type="text" class="validate" value="0.0"/>
					          	<label for="price">Prix</label>
					        </div>
			    		</div>

			    		<div class="row col s2 center">
			    			<a id="addConsultationBtn" class="waves-effect waves-light btn">Confirmer</a>
			    		</div>
			    	</div>

				    <div class="model-content-table-container col s12 center-align">
				   		<table id="patientsTable" class="responsive-table centered bordered z-depth-2">
						    <thead>
						        <tr>
					            	<th><img src="img/consultations/numero.png" class="theaders"/>N°</th>
					              	<th><img src="img/consultations/Name.png" class="theaders"/>om</th>
						            <th><img src="img/consultations/Prenom.png" class="theaders"/>renom</th>
						            <th><img src="img/consultations/ddn.png" class="theaders"/>Date de naissance</th>
						        </tr>
						    </thead>

						    <tbody class="pattbdoy">
						       	<c:forEach items="${requestScope.PATIENTS_LIST}" var="patient" varStatus="status">
					        		<tr patientId="${patient.patientId}">
					        			<td>${status.index + 1}</td>
					                    <td>${patient.patientLastName}</td>
					                    <td>${patient.patientFirstName}</td>
					                    <td>${patient.patientBirthday}</td>
					        		</tr>
					        	</c:forEach>
					        </tbody>
					    </table>
			      	</div>

			    </div>
			</div>

			<div id="newObsModal" class="modal">
			    <div class="modal-content">
			    	<h4>Nouvelle observation</h4>
			    	<div class="row">
			    		<div class="input-field col s9">
					      	<input value="Tension: 17" id="newObsText" type="text">
					      	<label class="active" for="newObsText">Description de l'observation</label>
					    </div>
					    <div class="row col s2 center">
					   		<a id="addNewObsBtn" class="modal-action modal-close waves-effect waves-light btn">Confirmer</a>
			    		</div>
			    	</div>
			    </div>
			</div>
			
			<div id="newPrescriptionModal" class="modal">
			    <div class="modal-content">
			      	<div class="row">
			      		<h4>Nouvelle prescription</h4>
					   	<div class="col s12">
				    		<div class="input-field col s12">
				   				<i class="material-icons prefix">search</i>
					          	<input id="newPrescriptionSearchedMedecine" type="text"/>
					          	<label for="newPrescriptionSearchedMedecine">Nom / DCI du medicament</label>
					        </div>
			    		</div>
			    	</div>

				    <div class="model-content-table-container col s12 center-align">
				   		<table id="medecinesTable" class="responsive-table centered bordered z-depth-2">
						    <thead>
						        <tr>
									<th><img src="img/type.png" class="theaders"/>DCI</th>
									<th><img src="img/marque.png" class="theaders">Marque</th>
									<th><img src="img/forme.png" class="theaders"/>Forme</th>
									<th><img src="img/dose.png" class="theaders"/>Dosage</th>
					                <th><img src="img/remboursable.png" class="theaders"/>Remboursable ?</th>
						        </tr>
						    </thead>

						    <tbody class="medtbdoy">
						       	<c:forEach items="${requestScope.MEDECINES_LIST}" var="medecine" varStatus="status">
					        		<tr medecineId="${medecine.medecineId}">
					                    <td class="medecineDataCell">${medecine.medecineDci}</td>
					                    <td class="medecineDataCell">${medecine.medecineMark}</td>
					                    <td class="medecineDataCell">${medecine.medecineForm}</td>
					                    <td class="medecineDataCell">${medecine.medecineDosage}</td>
					                    <td class="medecineCheckCell">
					                    	<c:choose>
					                        	<c:when test="${medecine.medecineRedeemability}">
					                            	<input type="checkbox" class="filled-in" checked="checked" />
					                               	<label for="nothing"></label>
					                            </c:when>
					                           	<c:otherwise >
					                            	<input type="checkbox" class="filled-in"/>
					                                <label for="nothing"></label>
					                            </c:otherwise>
					                       	</c:choose>
					                     </td>
					                     <td class="medecineDataCell"><a class="waves-effect waves-light btn">Prescrire</a></td>
					        		</tr>
					        	</c:forEach>
					        </tbody>
					    </table>
			      	</div>

			    </div>
			</div>

	    </div>
        <script src="js/consultations.js"></script>
    </body>
</html>