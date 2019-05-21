package Servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import DomainModel.UserAdministration.Account;
import DomainModel.UserAdministration.Doctor;
import DomainModel.UserAdministration.Secretary;
import Utilities.UtilitiesHolder;


@WebServlet(name = "SecretaryWelcomeServlet", urlPatterns = {"/secretarywelcomepage"})
public class SecretaryWelcomeServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {				
		this.getServletContext().getRequestDispatcher(UtilitiesHolder.JSPS_FOLDER + "secretarywelcomepage.jsp").forward(request, response);;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String holdenRequest = request.getParameter(Parameeters.REQUEST.toString());
		
		if(holdenRequest != null) {
			if(holdenRequest.equals(PostRequests.AUTHENTIFICATION.toString())) {
				String email = request.getParameter(Parameeters.EMAIL.toString());
				String password = request.getParameter(Parameeters.PASSWORD.toString());
				
				if(email != null && password != null) {
					Account account = UtilitiesHolder.SEARCHES_HANDLER.isAccountValid(email, password);
					
					if(account != null && account.getPerson() instanceof Secretary) {
						UtilitiesHolder.AUTHENTICATION_SECURITY_MANAGER.authenticateUser(account, request);
						
						if(UtilitiesHolder.AUTHENTICATION_SECURITY_MANAGER.isAuthenticated(request))
							response.getOutputStream().print(Responses.AUTHENTIFICATION_SUCCEEDED.toString());
						else response.getOutputStream().print(Responses.NOT_CONFIRMED.toString());
					}
				}
			}
			else if(holdenRequest.equals(PostRequests.SUBSCRIPTION.toString())) {
				String email = request.getParameter(Parameeters.EMAIL.toString());
				
				if(!UtilitiesHolder.SEARCHES_HANDLER.doesEmailExist(email)) {
					String docUuid = request.getParameter(Parameeters.DOCTOR_ID.toString());
					Doctor doctor = UtilitiesHolder.SEARCHES_HANDLER.isDoctorValid(docUuid);
					
					if(doctor != null) {
						Secretary secretary = new Secretary();
					      		  secretary.setPersonLastName(request.getParameter(Parameeters.LAST_NAME.toString()));
					      		  secretary.setPersonFirstName(request.getParameter(Parameeters.FIRST_NAME.toString()));
					      		  secretary.setPersonPhoneNumber(request.getParameter(Parameeters.PHONE_NUMBER.toString()));
					   
					    Account account = new Account();
					    		account.setAccountEmail(email);
					    		account.setAccountPassword(request.getParameter(Parameeters.PASSWORD.toString()));
		
					    secretary.setPersonAccount(account);
					    secretary.setConfirmed(false);
					    account.setPerson(secretary);					    
					    				    
					    doctor.addSecretary(secretary);
					    
					    Utilities.UtilitiesHolder.SESSION_FACTORY_HANDLER.update(doctor);
					    Utilities.UtilitiesHolder.AUTHENTICATION_SECURITY_MANAGER.authenticateUser(account, request);
					    
					    response.getOutputStream().print(Responses.SUBSCRIPTION_SUCCEEDED.toString());
					}
					else response.getOutputStream().print(Responses.NO_DOCTOR.toString());
				}
				else response.getOutputStream().print(Responses.EMAIL_EXISTS.toString());
			}
		}
	}
	
	public static enum PostRequests{
		AUTHENTIFICATION,
		SUBSCRIPTION
	}
	
	public static enum Parameeters{
		REQUEST,
		EMAIL,
		PASSWORD,
		FIRST_NAME,
        LAST_NAME,
        DOCTOR_ID,
        PHONE_NUMBER
	}
	
	public static enum Responses{
		AUTHENTIFICATION_SUCCEEDED,
		NOT_CONFIRMED,
		SUBSCRIPTION_SUCCEEDED,
		EMAIL_EXISTS,
		NO_DOCTOR
	}
}
