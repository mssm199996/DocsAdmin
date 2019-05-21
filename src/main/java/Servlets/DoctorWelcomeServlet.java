package Servlets;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import DomainModel.UserAdministration.Account;
import DomainModel.UserAdministration.Doctor;
import DomainModel.UserAdministration.Doctor.Gender;
import DomainModel.UserAdministration.Speciality;
import Utilities.UtilitiesHolder;

@WebServlet(name = "SignInUpServlet", urlPatterns = {"/doctorwelcomepage"})
public class DoctorWelcomeServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("SPECIALITIES", (List<Speciality>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select("FROM Speciality"));
		this.getServletContext().getRequestDispatcher(UtilitiesHolder.JSPS_FOLDER + "doctorwelcomepage.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String holdenRequest = request.getParameter(Parameeters.REQUEST.toString());
		
		if(holdenRequest != null) {
			if(holdenRequest.equals(PostRequests.AUTHENTIFICATION.toString())) {
				String email = request.getParameter(Parameeters.EMAIL.toString());
				String password = request.getParameter(Parameeters.PASSWORD.toString());
				
				if(email != null && password != null) {
					Account account = UtilitiesHolder.SEARCHES_HANDLER.isAccountValid(email, password);
					
					if(account != null && account.getPerson() instanceof Doctor) {
						response.getOutputStream().print(Responses.AUTHENTIFICATION_SUCCEEDED.toString());
						UtilitiesHolder.AUTHENTICATION_SECURITY_MANAGER.authenticateUser(account, request);
					}
				}
			}
			else if(holdenRequest.equals(PostRequests.SUBSCRIPTION.toString())) {
				String email = request.getParameter(Parameeters.EMAIL.toString());
				
				if(!UtilitiesHolder.SEARCHES_HANDLER.doesEmailExist(email)) {
					Doctor doctor = new Doctor();
						   doctor.setPersonLastName(request.getParameter(Parameeters.LAST_NAME.toString()));
						   doctor.setPersonFirstName(request.getParameter(Parameeters.FIRST_NAME.toString()));
						   doctor.setDoctorGender(Gender.values()[Integer.parseInt(request.getParameter(Parameeters.GENDER.toString())) - 1]);
						   doctor.setPersonPhoneNumber(request.getParameter(Parameeters.PHONE_NUMBER.toString()));
						   
				    Account account = new Account();
				    		account.setAccountEmail(email);
				    		account.setAccountPassword(request.getParameter(Parameeters.PASSWORD.toString()));

				    doctor.setPersonAccount(account);
				    account.setPerson(doctor);
				    	
				    
				    int specialityIndex = Integer.parseInt(request.getParameter(Parameeters.SPECIALITY.toString()));
				    
				    doctor.setSpeciality(UtilitiesHolder.SEARCHES_HANDLER.getSpecifiedSpeciality(specialityIndex - 1));
				    
				    Utilities.UtilitiesHolder.SESSION_FACTORY_HANDLER.insert(doctor);
				    Utilities.UtilitiesHolder.AUTHENTICATION_SECURITY_MANAGER.authenticateUser(account, request);
				    
				    response.getOutputStream().print(Responses.SUBSCRIPTION_SUCCEEDED.toString());
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
        SPECIALITY,
        GENDER,
        PHONE_NUMBER
	}
	
	public static enum Responses{
		AUTHENTIFICATION_SUCCEEDED,
		SUBSCRIPTION_SUCCEEDED,
		EMAIL_EXISTS
	}
}
