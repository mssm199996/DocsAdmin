package Servlets;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import DomainModel.DocumentAdministration.Document;
import DomainModel.UserAdministration.Account;
import DomainModel.UserAdministration.Doctor;
import Utilities.UtilitiesHolder;
import Utilities.SecurityManagers.SecurityManager;

@WebServlet(name = "DocumentsServlet", urlPatterns = {"/documents"})
@MultipartConfig
public class DocumentsServlet extends HttpServlet {

	public static int RESULT_LIMIT = 100;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(UtilitiesHolder.AUTHENTICATION_SECURITY_MANAGER.isAuthenticated(request)){
			Account account = (Account) request.getSession().getAttribute(SecurityManager.SESSION_SCOPE_ACCOUNT_KEY);
			
			if(account.getPerson() instanceof Doctor) {
				request.setAttribute("DOCUMENTS_LIST", UtilitiesHolder.SEARCHES_HANDLER.getAllDocuments((Doctor) account.getPerson()));
				this.getServletContext().getRequestDispatcher(UtilitiesHolder.JSPS_FOLDER + "documents.jsp").forward(request, response);
			}
			else response.sendRedirect("patients");
        }
        else response.sendRedirect("index");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String holdenRequest = request.getParameter(Parameeters.REQUEST.toString());

		if(holdenRequest != null) {
			Account account = (Account) request.getSession().getAttribute(SecurityManager.SESSION_SCOPE_ACCOUNT_KEY);
			
			if(holdenRequest.equals(Requests.GET_DOCUMENTS.toString())) {
				String documentName = request.getParameter(Parameeters.DOCUMENT_NAME.toString());
				
				response.getOutputStream().print(
					this.getDocumentsXmlResponse((Doctor) account.getPerson(), documentName)
				);
			}
			else if(holdenRequest.equals(Requests.UPLOAD_DOCUMENT.toString())) {
				String documentName = request.getParameter(Parameeters.DOCUMENT_NAME.toString());
				String documentDescription = request.getParameter(Parameeters.DOCUMENT_DESCRIPTION.toString());
				Part fileToUpload = request.getPart(Parameeters.DOCUMENT_CONTENT.toString());
				
				response.getOutputStream().print(
					this.uploadDocument(fileToUpload, (Doctor) account.getPerson(), documentName, documentDescription)
				);
			}
			else if(holdenRequest.equals(Requests.DELETE_DOCUMENT.toString())) {
				String documentId = request.getParameter(Parameeters.DOCUMENT_ID.toString());
				
				response.getOutputStream().print(
					this.deleteDocument(documentId)
				);
			}
			else if(holdenRequest.equals(Requests.DOWNLOAD_DOCUMENT.toString())) {
				String documentId = request.getParameter(Parameeters.DOCUMENT_ID.toString());
				
				response.getOutputStream().write(this.downloadDocument(documentId));
			}
		}
	}	

	public String getDocumentsXmlResponse(Doctor doctor, String documentName){
        String documentXml = "";
                
        List<Document> documents = UtilitiesHolder.SEARCHES_HANDLER.getSpecifiedDocuments(doctor, documentName);
        
        documentXml = UtilitiesHolder.BEAN_TO_XML_PARSER.fromBeanToXml(new String[]{"documentFilePath", "documentName", "documentUploadDate", "documentDescription", "documentExtension"},
        		documents.toArray(new Document[]{})
        );
                
        return documentXml;
    }
	
	private String uploadDocument(Part fileToUpload, Doctor doctor, String documentName, String documentDescription) {
		if(fileToUpload != null && fileToUpload.getSize() != 0 && documentName != null) {
			try {
				String fileExtension = fileToUpload.getSubmittedFileName().substring(fileToUpload.getSubmittedFileName().lastIndexOf('.'));
			
				if(UtilitiesHolder.FILES_UPLOADER.uploadDocument(
						fileToUpload, UtilitiesHolder.UPDATES_HANDLER.uploadDocument(doctor, documentName, documentDescription, fileExtension)))
					return Responses.INSERT_SUCCESS.toString();
			}
			catch(ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException exp) {
				exp.printStackTrace();
				
				return Responses.ERROR_MESSAGE.toString();
			}
		}
		
		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String deleteDocument(String documentId) {
		try {
			UUID uuid = UUID.fromString(documentId);
			
			if(UtilitiesHolder.UPDATES_HANDLER.deleteDocument(uuid))
				return Responses.REMOVE_SUCCESS.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private byte[] downloadDocument(String documentId) {
		try {
			UUID uuid = UUID.fromString(documentId);
			
			return UtilitiesHolder.FILES_UPLOADER.downloadDocument(uuid);
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }
		
		return null;
	}
	
	public static enum Parameeters {
		REQUEST,
		DOCUMENT_ID,
		DOCUMENT_NAME,
		DOCUMENT_DESCRIPTION,
		DOCUMENT_CONTENT
	}
	
	public static enum Requests {
		GET_DOCUMENTS,
		UPLOAD_DOCUMENT,
		DELETE_DOCUMENT,
		DOWNLOAD_DOCUMENT
	}
	
	public static enum Responses {
		ERROR_MESSAGE,
		INSERT_SUCCESS,
		REMOVE_SUCCESS
	}
}

