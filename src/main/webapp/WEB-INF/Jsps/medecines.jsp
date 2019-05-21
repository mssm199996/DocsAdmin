<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="platformskin.jsp" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/medecines.css"/>
        <title>Medecines</title>
    </head>
    <body>
	<div class="row">
            <form class="col s12">
		<div class="input-field col s12">
		    <i class="material-icons prefix formIcon">search</i>
		    <input id="medecinesSearchInput" type="text">
		    <label for="icon_prefix">DCI/Marque</label>
		</div>
            </form>
            <table id="medecinesTable" class="responsive-table highlight centered bordered">
				<thead>
		            <tr>
						<th>
		                	<img src="img/numero.png" class="theaders"/>
		                    <span>N°</span>
						</th>
						<th>
		                	<img src="img/type.png" class="theaders"/>
		                	<span>DCI</span>
						</th>
						<th>
		                    <img src="img/marque.png" class="theaders">
		                    <span>Marque</span>
		                </th>
						<th>
		                    <img src="img/forme.png" class="theaders"/>
		                    <span>Forme</span>
						</th>
						<th>
		                    <img src="img/dose.png" class="theaders"/>
		                    <span>Dosage</span>
						</th>
		                <th>
		                    <img src="img/remboursable.png" class="theaders"/>
		                    <span>Remboursable ?</span>
						</th>
		            </tr>
		        </thead>
				<tbody>
		        	<c:forEach items="${requestScope.MEDECINES_LISTE}" var="medecine" varStatus="status">
		            	<tr>
		                	<td class="dataCell">${status.index + 1}</td>
		                    <td class="dataCell">${medecine.medecineDci}</td>
		                    <td class="dataCell">${medecine.medecineMark}</td>
		                    <td class="dataCell">${medecine.medecineForm}</td>
		                    <td class="dataCell">${medecine.medecineDosage}</td>
		                    <td class="dataCell">
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
		                </tr>
		            </c:forEach>
				</tbody>
            </table>
        </div>
        <script src="js/medecines.js" ></script>
    </body>
</html>
