<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="platformskin.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link href="css/appointments.css" rel="stylesheet" type="text/css" />
<title>Docs Admin</title>
</head>
<body>
	<div class="fixed-action-btn horizontal">
		<a class="btn-floating btn-large cyan pulse"><i
			class="large material-icons">menu</i></a>
		<ul>
			<li><a id="removeRDVBtn"
				class="waves-effect waves-light btn-floating red"><i
					class="material-icons">remove</i></a></li>
			<li><a href="#newRDVModal"
				class="waves-effect waves-light btn-floating green modal-trigger"><i
					class="material-icons">add</i> </a></li>

		</ul>
	</div>

	<div class="row col s12">

		<div class="row searchContainer">
			<div class="col s9">
				<div class="input-field col s12">
					<i class="material-icons prefix">search</i> <input
						id="searchedPatient" type="text" class="validate" /> <label
						for="patient">Nom du patient</label>
				</div>
			</div>

			<div class="row col s3">
				<div class="input-field col s12">
					<i class="material-icons prefix">date_range</i> <label
						for="RDV_Date">Date de rendez-vous</label> <input id="RDV_Date"
						type="text" class="datepicker">
				</div>
			</div>
		</div>

		<div class="col s9 center-align centerContainer">
			<div class="row">
				<table id="RDVTable"
					class="responsive-table centered bordered z-depth-2">
					<thead>
						<tr>
							<th><img src="img/consultations/numero.png" class="theaders" />N°</th>
							<th><img src="img/consultations/Name.png" class="theaders" />om</th>
							<th><img src="img/consultations/Prenom.png" class="theaders" />renom</th>
							<th><img src="img/consultations/Date.png" class="theaders" />Date</th>
							<th><img src="img/img18.png" class="theaders"
								style="width: 36px; height: 41px;" />Heure</th>
						</tr>
					</thead>

					<tbody class="contbody">
					</tbody>
				</table>
			</div>
		</div>

		<div class="col s3 right-align">
			<div class="row">
				<ul class="collapsible z-depth-2" data-collapsible="accordion">

					<li>
						<div class="collapsible-header">
							<img src="img/consultations/paraclinique.jpg"
								style="width: 42px; height: 42px;"> <span
								class="accheaders">Paraclinique</span>
						</div>

						<div class="collapsible-body row">
							<div class="col s12 left-align listContainer">

								<ul id="ParacliniqueListe" class="collection">
								</ul>
								<ul>
									<li class="collection-item valign-wrapper"><a
										class="waves-effect waves-light btn green col s12 center modal-trigger"
										href="#newParaclinique"> <i class="material-icons">add</i></a>
									</li>
								</ul>
							</div>



						</div>
					</li>


					<li>
						<div class="collapsible-header">
							<img src="img/consultations/motif.png"
								style="width: 42px; height: 42px;"> <span
								class="accheaders">Motif</span>
						</div>

						<div class="collapsible-body row">
							<div class="col s12 left-align listContainer">
								<ul id="MotifListe" class="collection">
								</ul>
								<ul>
									<li class="collection-item valign-wrapper" APPOINTMENT_ID="0"
										MotifIndex="0"><a
										class="waves-effect waves-light btn green col s12 center modal-trigger"
										href="#newMotif"> <i class="material-icons">add</i></a></li>
								</ul>
							</div>



						</div>
					</li>



				</ul>
			</div>

		</div>




		<div id="newRDVModal" class="modal modalRDV">
			<div class="modal-content">
				<div class="row">
					<h4>Nouveau rendez-vous</h4>
					<div class="col s4">
						<div class="input-field col s12">
							<i class="material-icons prefix">search</i> <input
								id="newRDVSearchedPatient" type="text" /> <label
								for="newRDVSearchedPatient">Nom du patient</label>
						</div>
					</div>

					<div class="input-field col s3">
						<i class="material-icons prefix">date_range</i> <label
							for="addRDV_Date">Date de rendez-vous</label> <input
							id="addRDV_Date" type="text" class="datepicker">
					</div>


					<div class="input-field col s3">
						<i class="material-icons prefix">schedule</i> <label for="addTime">Heure
							de rendez-vous</label> <input id="addTime" type="text" class="timepicker">
					</div>

					<div class="row col s2 center">
						<a id="addRDVBtn" class="waves-effect waves-light btn">Confirmer</a>
					</div>
				</div>

				<div class="model-content-table-container col s12 center-align">
					<table id="patientsTable"
						class="responsive-table centered bordered z-depth-2">
						<thead>
							<tr>
								<th><img src="img/consultations/numero.png"
									class="theaders" />N°</th>
								<th><img src="img/consultations/Name.png" class="theaders" />om</th>
								<th><img src="img/consultations/Prenom.png"
									class="theaders" />renom</th>
								<th><img src="img/consultations/ddn.png" class="theaders" />Date
									de naissance</th>
							</tr>
						</thead>
						<tbody class="pattbody">
							<c:forEach items="${requestScope.PATIENTS_LIST}" var="patient"
								varStatus="status">
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



		<div id="newParaclinique" class="modal">
			<div class="modal-content">
				<h4>Paraclinique</h4>
				<div class="row">
					<div class="input-field col s12">
						<input value="Examen cardio vasculaire" id="newParaText"
							type="text"> <label class="active" for="newParaText">Description</label>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<a id="addNewParaBtn"
					class="modal-action modal-close waves-effect waves-green btn-flat">Confirmer</a>
			</div>
		</div>


		<div id="newMotif" class="modal">
			<div class="modal-content">
				<h4>Nouveau motif</h4>
				<div class="row">
					<div class="input-field col s12">
						<input value="Consultation simple" id="newMotifText" type="text">
						<label class="active" for="newMotifText">Description</label>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<a id="addNewMotifBtn"
					class="modal-action modal-close waves-effect waves-green btn-flat">Confirmer</a>
			</div>
		</div>



	</div>
	<script src="js/appointments.js"></script>
</body>
</html>