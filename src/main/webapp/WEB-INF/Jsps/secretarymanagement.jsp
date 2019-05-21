<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="platformskin.jsp" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
  <html>
    <head>
      <link type="text/css" rel="stylesheet" href="css/materialize.min.css"  media="screen,projection"/>
      <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
      <link href="css/secretarymanagement.css" rel="stylesheet" type="text/css" />
    </head>

    <body>
    	<div class="fixed-action-btn horizontal">
		    <a class="btn-floating btn-large cyan pulse"><i class="large material-icons">menu</i></a>
		    <ul>
		    	<li><a id="clean" class="waves-effect waves-light btn-floating red" ><i class="material-icons">clear_all
		    	</i></a></li>
		    </ul>
	    </div>
      <div class="row col s12">
        <div class="row searchContainer">
        </div>
        <div class="col s12 center-align centerContainer">
          <div class="row">
            <table id="secretaireTable" class="responsive-table centered bordered z-depth-2 highlight">
                <thead>
                    <tr>
                        <th><img src="img/numero.png" class="theaders"/>N°</th>
                        <th><img src="img/Name.png" class="theaders"/>om</th>
                        <th><img src="img/Prenom.png" class="theaders"/>renom</th>
                        <th><img src="img/NDT.png" class="theaders"/>Telephone</th>
                        <th><img src="img/confirm.png" class="confirmedHeader" style="width: 40px; height: 40px;"/>Confirme ?</th>
                    </tr>
                </thead>

                <tbody class="secrbody">
                	<c:forEach items="${requestScope.SECRETARIES_LIST}" var="secretary" varStatus="status">
					    <tr idSecretary="${secretary.personId}">
					        <td>${status.index + 1}</td>
					        <td>${secretary.personLastName}</td>
					        <td>${secretary.personFirstName}</td>
					        <td>${secretary.personPhoneNumber}</td>
					        <td>
					        	<c:choose>
					        		<c:when test="${secretary.confirmed}">
					        			<button id="btnConfirm" disabled NotConfirm = "${!secretary.confirmed}" onclick="getIdButton(this)" class="confirm btn waves-effect waves-light " type="submit" name="action" >
											Confirm
										</button>
					        		</c:when>
					        		<c:otherwise>
						        		<button id="btnConfirm" NotConfirm = "${!secretary.confirmed}" onclick="getIdButton(this)" class="confirm btn waves-effect waves-light green" type="submit" name="action" >
											Confirm
										</button>
						        	</c:otherwise>
					        	</c:choose>
					        </td>
							<td>
								<a onclick = "getIdRow(this)" class="modal-trigger deleteRow waves-effect waves-light btn red" href="#modal1">
									<i class="material-icons delete" style="color:white" >delete</i>
								</a>
							</td>
						</tr>
					</c:forEach>
                </tbody>
            </table>
          </div>
        </div>
        <!--___________________________Modale de confirmation de supprission___________________________________________-->
        
        <div id="modal1" class="modal">
            <div class="modal-content">
                 <h4>Supprimer secrétaire</h4>
                 <p>Vous allez supprimer votre secrétaire</p>
            </div>
            <div class="modal-footer">
                 <a id="confirmDeleteBtn" onclick = "deleteRow()" class="modal-close waves-effect waves-light btn  col s2 center modal-trigger" style="float: right ;margin:50px;background-color: #26a69a;">Confirmer</a>
                 <a id="annulerDeleteBtn"  class="modal-close waves-effect waves-light btn  col s2 center modal-trigger" style="float: right ;margin:50px; background-color: #26a69a;" >Annuler</a>
            </div>
        </div>
        
        <!--___________________________Modale de confirmation de confirmation de secretaire_____________________________-->
         <div id="modal2" class="modal">
            <div class="modal-content">
                 <h4>Confirmation de secrétaire</h4>
                 <p>Voulez vous vraiment confirmer votre secrétaire ?</p>
            </div>
            <div class="modal-footer">
                 <a id="confirmConfirmBtn" onclick="confirmConfirmation()" class="modal-close waves-effect waves-light btn  col s2 center modal-trigger" style="float: right ;margin:50px;background-color: #26a69a;">Confirmer</a>
                 <a id="annulerConfirmBtn"  class="modal-close waves-effect waves-light btn  col s2 center modal-trigger" style="float: right ;margin:50px; background-color: #26a69a;">Annuler</a>
            </div>
        </div>
        
        <!--___________________________Modale de confirmation de nettoyage des secretaire_______________________________-->
        
        <div id="modal3" class="modal">
            <div class="modal-content">
                 <h4>Nettoyer les secretaires</h4>
                 <p>Vous allez supprimer tout les secrétaires non confirmer</p>
            </div>
            <div class="modal-footer">
                 <a id="confirmCleanBtn" onclick="cleanSecretary()" class="modal-close waves-effect waves-light btn  col s2 center modal-trigger" style="float: right ;margin:50px;background-color: #26a69a;">Confirmer</a>
                 <a id="annulerCleanBtn"  class="modal-close waves-effect waves-light btn  col s2 center modal-trigger" style="float: right ;margin:50px; background-color: #26a69a;">Annuler</a>
            </div>
        </div>
        
      <script  src="js/secretarymanagement.js"></script>
    </body>
  </html>