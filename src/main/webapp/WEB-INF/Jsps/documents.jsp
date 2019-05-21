<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="platformskin.jsp" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <link href="css/documents.css" rel="stylesheet" type="text/css"/>
		<title>DocsAdmin</title>
	</head>
	<body>
		<div class="fixed-action-btn horizontal">
		    <a class="btn-floating btn-large cyan pulse"><i class="large material-icons">menu</i></a>
		    <ul>
		      	<li><a id="deleteButton" class="waves-effect waves-light btn-floating red"><i class="material-icons">delete</i></a></li>
		      	<li><a id="uploadButton" href="#newUploadModal" class="waves-effect waves-light btn-floating green modal-trigger"><i class="material-icons">cloud_upload</i></a></li>
				<li><a id="printButton" class="waves-effect waves-light btn-floating blue"><i class="material-icons">local_printshop</i></a></li>
		    </ul>
	    </div>
		
		<div class="row col s12">
			<div class="row searchContainer">
				<div class="col s12">
					<div class="input-field col s12">
						<i class="material-icons prefix">search</i>
						<input id="searchedDocument" type="text" class="validate"/>
						<label for="searchedDocument">Nom du document</label>
					</div>
				</div>	    		
			</div>
			
			<div class="col s12 center-align centerContainer">
				<div class="row">
					<table id="documentsTable" class="responsive-table centered bordered z-depth-2 scroll">
						<thead>
							<tr>
								<th><img src="img/documents/numero.png" class="theaders"/>N°</th>
								<th><img src="img/documents/Name.png" class="theaders"/>Nom</th>
								<th><img src="img/documents/Date.png" class="theaders"/>Date</th>
								<th><img src="img/documents/information.png" class="theaders"/>Description</th>
							</tr>
						</thead>
						
						<tbody class="docstbody">
							<c:forEach items="${requestScope.DOCUMENTS_LIST}" var="document" varStatus="status">
				        		<tr documentId="${document.documentFilePath}" documentExtension="${document.documentExtension}">
				        			<td>${status.index + 1}</td>
				                    <td>${document.documentName}</td>
				                    <td>${document.documentUploadDate}</td>
				                    <td>${document.documentDescription}</td>
				        		</tr>
				        	</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		
		<div id="newUploadModal" class="modal">
			<div class="modal-content">
				<h4 class="center">
					<a class="waves-effect waves-light btn-floating green headerIcon"><i class="material-icons">add_circle</i></a>
						Uploader un document
					<a class="waves-effect waves-light btn-floating green headerIcon"><i class="material-icons">add_circle</i></a>
				</h4>
			    <div class="row"> 
					<form name="formulaire" id="formulaire" method="POST" action="documents" enctype="multipart/form-data">
						<div class="row formRow">
							<div class="input-field col s12">
								<input type= "hidden" name="REQUEST" id="REQUEST" value="UPLOAD_DOCUMENT" /> 
							</div>
						</div>
						
						<div class="row formRow">
							<div class="input-field col s12">
					          	<input type="text" name="DOCUMENT_NAME" id="filename" />
					          	<label for="filename">Nom du document</label>
					        </div>
						</div>
						
						<div class="row formRow">
							<div class="input-field col s12">
					          	<textarea class="materialize-textarea" name="DOCUMENT_DESCRIPTION" id="filedescription"></textarea>
					          	<label for="filedescription">Description du document</label>
					        </div>
						</div>
						
				        <div class="row formRow">
				        	<div class="file-field input-field">
							    <div class="btn">
							        <span>Fichier</span>
							        <input type="file" name="DOCUMENT_CONTENT" id="fileToUpload" />
							    </div>
							    <div class="file-path-wrapper">
							        <input class="file-path validate" type="text" id="fileToUploadWrapper">
								</div>
							</div>
				        </div>
				        
				        <div class="row formRow col s12 valign-wrapper">
				        	<a id="upload" name = "upload" class=" col s2 waves-effect waves-light modal-action btn cyan"><i class="material-icons">cloud_upload</i></a>
				        	<div class="progress">
								<div class="determinate" style="width: 0%"/>
							</div>
				        </div>
			        </form>
			    </div>
			</div>
		</div>
        <script src="js/documents.js"></script>
	</body>
</html>