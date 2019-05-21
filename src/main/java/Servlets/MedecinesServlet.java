package Servlets;

import DomainModel.MedecineAdministration.Medecine;
import DomainModel.UserAdministration.Account;
import DomainModel.UserAdministration.Doctor;
import Utilities.UtilitiesHolder;
import Utilities.SecurityManagers.SecurityManager;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MedecinesServlet", urlPatterns = {"/medecines"})
public class MedecinesServlet extends HttpServlet{
    public static final int RESULT_LIMIT = 50;
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        // get all the medicines 
        if(UtilitiesHolder.AUTHENTICATION_SECURITY_MANAGER.isAuthenticated(request)){
        	Account account = (Account) request.getSession().getAttribute(SecurityManager.SESSION_SCOPE_ACCOUNT_KEY);
        	
        	if(account.getPerson() instanceof Doctor) {
        		request.setAttribute("MEDECINES_LISTE", UtilitiesHolder.SEARCHES_HANDLER.getAllMedecines());
                this.getServletContext().getRequestDispatcher(UtilitiesHolder.JSPS_FOLDER + "medecines.jsp").forward(request, response);
        	}
        	else response.sendRedirect("patients");
        }
        else response.sendRedirect("index");
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        if(UtilitiesHolder.AUTHENTICATION_SECURITY_MANAGER.isAuthenticated(request)){
            response.getOutputStream().print(
                    getMedecineXmlResponse(request.getParameter(PostRequests.TARGETED_MEDECINES.name())));
        }
    }
    
    public String getMedecineXmlResponse(String targetedMedecines){
        String medecinesXml = "";
                
        List<Medecine> medecines = UtilitiesHolder.SEARCHES_HANDLER.getSpecifiedMedecines(targetedMedecines);
        
        medecinesXml = UtilitiesHolder.BEAN_TO_XML_PARSER.fromBeanToXml(
                medecines.toArray(new Medecine[]{})
        );
                
        return medecinesXml;
    }
    
    public enum PostRequests{
        TARGETED_MEDECINES
    }
}
