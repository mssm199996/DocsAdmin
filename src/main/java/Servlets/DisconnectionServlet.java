package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Utilities.UtilitiesHolder;

@WebServlet(name = "/DisconnectionServlet", urlPatterns = {"/disconnect"})
public class DisconnectionServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(UtilitiesHolder.AUTHENTICATION_SECURITY_MANAGER.isAuthenticated(request))			
			UtilitiesHolder.AUTHENTICATION_SECURITY_MANAGER.disconnectUser(request);
		
		response.sendRedirect("index");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
