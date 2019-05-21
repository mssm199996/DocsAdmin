package Servlets;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import DomainModel.UserAdministration.Account;
import DomainModel.UserAdministration.Doctor;
import DomainModel.UserAdministration.Secretary;
import Utilities.UtilitiesHolder;
import Utilities.SecurityManagers.SecurityManager;

@WebServlet(name = "SecretaryManagementServelet", urlPatterns = {"/SecretaryManagement"})
public class SecretaryManagementServelet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(UtilitiesHolder.AUTHENTICATION_SECURITY_MANAGER.isAuthenticated(request)){
			Account account = (Account) request.getSession().getAttribute(SecurityManager.SESSION_SCOPE_ACCOUNT_KEY);
			
			if(account.getPerson() instanceof Doctor) {
				List<Secretary> secretaries = UtilitiesHolder.SEARCHES_HANDLER.getAllSecretaries((Doctor) account.getPerson());
				
				request.setAttribute("SECRETARIES_LIST", secretaries);
				
				this.getServletContext().getRequestDispatcher(UtilitiesHolder.JSPS_FOLDER + "secretarymanagement.jsp").forward(request, response);
			}
			else response.sendRedirect("patients");
        }
		
        else this.getServletContext().getRequestDispatcher(UtilitiesHolder.JSPS_FOLDER + UtilitiesHolder.NOT_AUTHENTICATED_USER_JSP).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Account account = (Account) request.getSession().getAttribute(SecurityManager.SESSION_SCOPE_ACCOUNT_KEY);
		
		if(account.getPerson() instanceof Doctor) {
			String holdenRequest = request.getParameter(Parameeters.REQUEST.toString());
			
			if(holdenRequest != null) {
				if(holdenRequest.equals(Requests.CONFIRM_SECRETARY.toString())) {
					String secretaryId = request.getParameter(Parameeters.SECRETARY_ID.toString());
					
					response.getOutputStream().print(this.confirmSecretary(secretaryId));
				}
				else 
					if(holdenRequest.equals(Requests.DELETE_SECRETARY.toString())) {
						String secretaryId = request.getParameter(Parameeters.SECRETARY_ID.toString());
						
						response.getOutputStream().print(this.deleteSecretary(secretaryId));
				}
				else if(holdenRequest.equals(Requests.CLEAN_NON_CONFIRMED_SECRETARIES.toString())) {
					response.getOutputStream().print(this.cleanSecretaries(account.getPerson().getPersonId().toString()));
				}
			}
		}
	}
	private String deleteSecretary(String secretaryId) {
		try {
			UUID uuid = UUID.fromString(secretaryId);
			
			if(UtilitiesHolder.UPDATES_HANDLER.deleteSecretary(uuid))
				return Responses.REMOVE_SUCCESS.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String cleanSecretaries(String doctorUUID) {
		try {
			UUID doctorID = UUID.fromString(doctorUUID);
			
			if(UtilitiesHolder.UPDATES_HANDLER.cleanSecretaries(doctorID))
				return Responses.CLEAN_SUCCESS.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String confirmSecretary(String secretaryId) {
		try {
			UUID uuid = UUID.fromString(secretaryId);
			
			if(UtilitiesHolder.UPDATES_HANDLER.confirmSecretary(uuid))
				return Responses.CONFIRM_SUCCESS.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	public static enum Requests {
		CONFIRM_SECRETARY,
		DELETE_SECRETARY,
		CLEAN_NON_CONFIRMED_SECRETARIES
	}
	public static enum Parameeters {
		SECRETARY_ID,
		REQUEST,
		DOCTOR_ID
	}
	public static enum Responses {
		ERROR_MESSAGE,
		REMOVE_SUCCESS,
		CONFIRM_SUCCESS,
		CLEAN_SUCCESS
	}
}
