package Servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DomainModel.AppointmentAdministration.Appointment;
import DomainModel.AppointmentAdministration.Paraclinic;
import DomainModel.AppointmentAdministration.Reason;
import DomainModel.PatientAdministration.Patient;
import DomainModel.UserAdministration.Account;
import DomainModel.UserAdministration.Doctor;
import DomainModel.UserAdministration.Secretary;
import Utilities.UtilitiesHolder;
import Utilities.SecurityManagers.SecurityManager;


@WebServlet(name = "AppointmentsServlet", urlPatterns = {"/appointments"})
public class AppointmentsServlet extends HttpServlet {

	public static int RESULT_LIMIT = 100;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(UtilitiesHolder.AUTHENTICATION_SECURITY_MANAGER.isAuthenticated(request)){
			Account account = (Account) request.getSession().getAttribute(SecurityManager.SESSION_SCOPE_ACCOUNT_KEY);
			
			if(account.getPerson() instanceof Doctor) {
				request.setAttribute("APPOINTMENTS_LIST", UtilitiesHolder.SEARCHES_HANDLER.getAllAppointments((Doctor) account.getPerson()));
				request.setAttribute("PATIENTS_LIST", UtilitiesHolder.SEARCHES_HANDLER.getAllPatients((Doctor) account.getPerson()));
			}
			else if(account.getPerson() instanceof Secretary) {
				request.setAttribute("APPOINTMENTS_LIST", UtilitiesHolder.SEARCHES_HANDLER.getAllAppointments(((Secretary) account.getPerson()).getDoctor()));
				request.setAttribute("PATIENTS_LIST", UtilitiesHolder.SEARCHES_HANDLER.getAllPatients(((Secretary) account.getPerson()).getDoctor()));
			}

			this.getServletContext().getRequestDispatcher(UtilitiesHolder.JSPS_FOLDER + "appointments.jsp").forward(request, response);
        }
		else response.sendRedirect("index");	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(UtilitiesHolder.AUTHENTICATION_SECURITY_MANAGER.isAuthenticated(request)) {
			String holdenRequest = request.getParameter(Parameeters.REQUEST.toString());
			
			if(holdenRequest != null) {
				Account account = (Account) request.getSession().getAttribute(SecurityManager.SESSION_SCOPE_ACCOUNT_KEY);
				
				if(holdenRequest.equals(Requests.GET_APPOINTMENTS.toString())) {
					String appointmentDate = request.getParameter(Parameeters.APPOINTMENT_DATE.toString());
					String appointmentPatient = request.getParameter(Parameeters.APPOINTMENT_PATIENT.toString());
					
					Doctor doc = null;
					
					if(account.getPerson() instanceof Doctor) 
						doc = (Doctor) account.getPerson();
					else doc = ((Secretary) account.getPerson()).getDoctor();
					
					response.getOutputStream().print(
						this.getAppointmentXmlResponse(doc, appointmentDate, appointmentPatient)
					);
				}
				else if(holdenRequest.equals(Requests.GET_PARACLINICS.toString())) {
					String appointmentId = request.getParameter(Parameeters.APPOINTMENT_ID.toString());
					
					response.getOutputStream().print(
						this.getParaclinicsXmlResponse(appointmentId)
					);
				}
				else if(holdenRequest.equals(Requests.GET_REASONS.toString())) {
					String appointmentId = request.getParameter(Parameeters.APPOINTMENT_ID.toString());
					
					response.getOutputStream().print(
						this.getReasonsXmlResponse(appointmentId)
					);
				}
				else if(holdenRequest.equals(Requests.INSERT_NEW_REASON.toString())) {
					String appointmentId = request.getParameter(Parameeters.APPOINTMENT_ID.toString());
					String reasonInfo = request.getParameter(Parameeters.TEXT_VALUE.toString());
					
					response.getOutputStream().print(
							this.insertNewReason(appointmentId, reasonInfo)
					);
				}
				else if(holdenRequest.equals(Requests.INSERT_NEW_PARACLINIC.toString())) {
					String appointmentId = request.getParameter(Parameeters.APPOINTMENT_ID.toString());
					String reasonInfo = request.getParameter(Parameeters.TEXT_VALUE.toString());
					
					response.getOutputStream().print(
							this.insertNewParaclinic(appointmentId, reasonInfo)
					);
				}
				else if(holdenRequest.equals(Requests.GET_PATIENTS.toString())) {
					String patientName = request.getParameter(Parameeters.TEXT_VALUE.toString());
					
					if(account.getPerson() instanceof Doctor)
						response.getOutputStream().print(this.getPatientXmlResponse((Doctor) account.getPerson(), patientName));
					else if(account.getPerson() instanceof Secretary)
						response.getOutputStream().print(this.getPatientXmlResponse(((Secretary)account.getPerson()).getDoctor(), patientName));
				}
				else if(holdenRequest.equals(Requests.INSERT_NEW_APPOINTMENT.toString())) {
					String patientId = request.getParameter(Parameeters.PATIENT_ID.toString());
					String appointmentDate = request.getParameter(Parameeters.APPOINTMENT_DATE.toString());
					String appointmentTime = request.getParameter(Parameeters.APPOINTMENT_TIME.toString());
					
					response.getOutputStream().print(this.insertNewAppointment(patientId, appointmentDate, appointmentTime));
				}
				else if(holdenRequest.equals(Requests.DELETE_APPOINTMENT.toString())) {
					String appointmentId = request.getParameter(Parameeters.APPOINTMENT_ID.toString());
					
					response.getOutputStream().print(this.deleteAppointment(appointmentId));
				}
				else if(holdenRequest.equals(Requests.DELETE_PARACLINIC.toString())) {
					String appointmentId = request.getParameter(Parameeters.APPOINTMENT_ID.toString());
					String paraclinicInformation = request.getParameter(Parameeters.TEXT_VALUE.toString());
					
					response.getOutputStream().print(this.deleteParaclinic(appointmentId, paraclinicInformation));
				}
				else if(holdenRequest.equals(Requests.DELETE_REASON.toString())) {
					String appointmentId = request.getParameter(Parameeters.APPOINTMENT_ID.toString());
					String reasonInformation = request.getParameter(Parameeters.TEXT_VALUE.toString());
					
					response.getOutputStream().print(this.deleteReason(appointmentId, reasonInformation));
				}
			}
		}
	}

	private String getAppointmentXmlResponse(Doctor doctor, String appointmentDate, String appointmentPatient) {
		String appointmeltXml = "";
        		
        List<Appointment> appointments = UtilitiesHolder.SEARCHES_HANDLER
        		.getSpecifiedAppointments(
        				doctor, 
        				appointmentDate.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}") ?
        					LocalDate.parse(appointmentDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null
        				,
        				appointmentPatient
        		);
        
        appointmeltXml = UtilitiesHolder.BEAN_TO_XML_PARSER.fromBeanToXml(new String[]{"appointmentId", "appointmentDate", "appointmentTime", "patient.patientFirstName", "patient.patientLastName"},
        		appointments.toArray(new Appointment[]{})
        );
                
        return appointmeltXml;
	}
	
	private String getReasonsXmlResponse(String appointmentId) {
		String reasonsXml = Responses.ERROR_MESSAGE.toString();
		
		try {
			UUID uuid = UUID.fromString(appointmentId);
			
			List<Reason> reasons = UtilitiesHolder.SEARCHES_HANDLER.getSpecifiedReasons(uuid);
			
			reasonsXml = UtilitiesHolder.BEAN_TO_XML_PARSER.fromBeanToXml(
					new String[] {"description"}, reasons.toArray(new Reason[]{}));
		}
		
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }
		
		return reasonsXml;
	}

	private String getParaclinicsXmlResponse(String appointmentId) {
		String paraclinicsXml = Responses.ERROR_MESSAGE.toString();
		
		try {
			UUID uuid = UUID.fromString(appointmentId);
			
			List<Paraclinic> paraclinics = UtilitiesHolder.SEARCHES_HANDLER.getSpecifiedParaclinics(uuid);
			
			paraclinicsXml = UtilitiesHolder.BEAN_TO_XML_PARSER.fromBeanToXml(
					new String[] {"description"}, paraclinics.toArray(new Paraclinic[]{}));
		}
		
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }
		
		return paraclinicsXml;
	}
	
	private String insertNewReason(String appointmentId, String reasonInfo) {
		try {
			UUID uuid = UUID.fromString(appointmentId);
			
			return UtilitiesHolder.UPDATES_HANDLER.insertNewReason(uuid, reasonInfo) ? 
					Responses.INSERT_SUCCESS.toString() : Responses.ERROR_MESSAGE.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String insertNewParaclinic(String appointmentId, String paraclinicInfo) {
		try {
			UUID uuid = UUID.fromString(appointmentId);
			
			return UtilitiesHolder.UPDATES_HANDLER.insertNewParaclinic(uuid, paraclinicInfo) ? 
					Responses.INSERT_SUCCESS.toString() : Responses.ERROR_MESSAGE.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}

	
	
	public String getPatientXmlResponse(Doctor doctor, String patientName){
        String patientXml = "";
                
        List<Patient> patients = UtilitiesHolder.SEARCHES_HANDLER
        		.getSpecifiedPatientsByNameOnly(
        				doctor,  
        				patientName
        		);
        
        patientXml = UtilitiesHolder.BEAN_TO_XML_PARSER.fromBeanToXml(new String[]{"patientId", "patientFirstName", "patientLastName", "patientBirthday"},
        		patients.toArray(new Patient[]{})
        );
                
        return patientXml;
    }
	
	private String insertNewAppointment(String patientId, String appointmentDate, String appointmentTime) {
		try {
			UUID uuid = UUID.fromString(patientId);
			
			return UtilitiesHolder.UPDATES_HANDLER.insertNewAppointment(
					uuid, 
					LocalDate.parse(appointmentDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")),
					LocalTime.parse(appointmentTime)
			) ? Responses.INSERT_SUCCESS.toString() : Responses.ERROR_MESSAGE.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String deleteAppointment(String appointmentId) {
		try {
			UUID uuid = UUID.fromString(appointmentId);
			
			if(UtilitiesHolder.UPDATES_HANDLER.deleteAppointment(uuid))
				return Responses.REMOVE_SUCCESS.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String deleteReason(String appointmentId, String reasonInformation) {
		try {
			UUID uuid = UUID.fromString(appointmentId);
			
			if(UtilitiesHolder.UPDATES_HANDLER.deleteReason(uuid, reasonInformation))
				return Responses.REMOVE_SUCCESS.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String deleteParaclinic(String appointmentId, String paraclinicInformation) {
		try {
			UUID uuid = UUID.fromString(appointmentId);
			
			if(UtilitiesHolder.UPDATES_HANDLER.deleteParaclinic(uuid, paraclinicInformation))
				return Responses.REMOVE_SUCCESS.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}

	public static enum Parameeters {
		REQUEST,
		APPOINTMENT_DATE,
		APPOINTMENT_TIME,
		APPOINTMENT_PATIENT,
		APPOINTMENT_ID,
		PATIENT_ID,
		TEXT_VALUE
	}
	
	public static enum Requests {
		GET_APPOINTMENTS,
		GET_PARACLINICS,
		GET_REASONS,
		INSERT_NEW_PARACLINIC,
		INSERT_NEW_APPOINTMENT,
		INSERT_NEW_REASON,
		DELETE_PARACLINIC,
		DELETE_APPOINTMENT,
		DELETE_REASON,
		GET_PATIENTS
	}
	
	public static enum Responses {
		ERROR_MESSAGE,
		INSERT_SUCCESS,
		REMOVE_SUCCESS
	}
}
