package Servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import DomainModel.ConsultationAdministration.Consultation;
import DomainModel.ConsultationAdministration.Observation;
import DomainModel.MedecineAdministration.Medecine;
import DomainModel.PatientAdministration.Patient;
import DomainModel.UserAdministration.Account;
import DomainModel.UserAdministration.Doctor;
import DomainModel.UserAdministration.Secretary;
import Utilities.UtilitiesHolder;
import Utilities.SecurityManagers.SecurityManager;

@WebServlet(name = "ConsultationsServlet", urlPatterns = {"/consultations"})
public class ConsultationsServlet extends HttpServlet {
	
	public static int RESULT_LIMIT = 100;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(UtilitiesHolder.AUTHENTICATION_SECURITY_MANAGER.isAuthenticated(request)){
			Account account = (Account) request.getSession().getAttribute(SecurityManager.SESSION_SCOPE_ACCOUNT_KEY);
			
			if(account.getPerson() instanceof Doctor) {
				request.setAttribute("CONSULTATIONS_LIST", UtilitiesHolder.SEARCHES_HANDLER.getAllConsultations((Doctor) account.getPerson()));
				request.setAttribute("PATIENTS_LIST", UtilitiesHolder.SEARCHES_HANDLER.getAllPatients((Doctor) account.getPerson()));
	            request.setAttribute("MEDECINES_LIST", UtilitiesHolder.SEARCHES_HANDLER.getAllMedecines());

				this.getServletContext().getRequestDispatcher(UtilitiesHolder.JSPS_FOLDER + "consultations.jsp").forward(request, response);
			}
			else response.sendRedirect("patients");
        }
        else response.sendRedirect("index");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(UtilitiesHolder.AUTHENTICATION_SECURITY_MANAGER.isAuthenticated(request)) {
			String holdenRequest = request.getParameter(Parameeters.REQUEST.toString());
			
			if(holdenRequest != null) {
				Account account = (Account) request.getSession().getAttribute(SecurityManager.SESSION_SCOPE_ACCOUNT_KEY);
								
				if(holdenRequest.equals(Requests.GET_CONSULTATIONS.toString())) {
					String consultationDate = request.getParameter(Parameeters.CONSULTATION_DATE.toString());
					String consultationPatient = request.getParameter(Parameeters.CONSULTATION_PATIENT.toString());
					
					response.getOutputStream().print(
						this.getConsultationXmlResponse((Doctor) account.getPerson(), consultationDate, consultationPatient)
					);
				}
				else if(holdenRequest.equals(Requests.GET_OBSERVATIONS.toString())) {
					String consultationId = request.getParameter(Parameeters.CONSULTATION_ID.toString());
					
					response.getOutputStream().print(
						this.getObservationsXmlResponse(consultationId)
					);
				}
				else if(
					holdenRequest.equals(Requests.GET_HM.toString()) ||
					holdenRequest.equals(Requests.GET_DIA.toString()) ||
					holdenRequest.equals(Requests.GET_EC.toString())		
				) {
					String consultationId = request.getParameter(Parameeters.CONSULTATION_ID.toString());
					
					response.getOutputStream().print(
						this.getText(consultationId, holdenRequest)
					);
				}
				else if(
					holdenRequest.equals(Requests.UPDATE_DIA.toString()) || 
					holdenRequest.equals(Requests.UPDATE_HM.toString()) || 
					holdenRequest.equals(Requests.UPDATE_EC.toString())
				) {
					String consultationId = request.getParameter(Parameeters.CONSULTATION_ID.toString());
					String newValue = request.getParameter(Parameeters.TEXT_VALUE.toString());
					
					response.getOutputStream().print(
						this.updateText(consultationId, newValue, holdenRequest)
					);
				}
				else if(holdenRequest.equals(Requests.INSERT_NEW_OBSERVATION.toString())) {
					String consultationId = request.getParameter(Parameeters.CONSULTATION_ID.toString());
					String obsInfo = request.getParameter(Parameeters.TEXT_VALUE.toString());
					
					response.getOutputStream().print(
							this.insertNewObservation(consultationId, obsInfo)
					);
				}
				else if(holdenRequest.equals(Requests.DELETE_OBSERVATION.toString())) {
					String consultationId = request.getParameter(Parameeters.CONSULTATION_ID.toString());
					String obsInfo = request.getParameter(Parameeters.TEXT_VALUE.toString());
					
					response.getOutputStream().print(this.deleteObservation(consultationId, obsInfo));
				}
				else if(holdenRequest.equals(Requests.DELETE_CONSULTATION.toString())) {
					String consultationId = request.getParameter(Parameeters.CONSULTATION_ID.toString());
					
					response.getOutputStream().print(this.deleteConsultation(consultationId));
				}
				else if(holdenRequest.equals(Requests.GET_PATIENTS.toString())) {
					String patientName = request.getParameter(Parameeters.TEXT_VALUE.toString());
					
					if(account.getPerson() instanceof Doctor)
						response.getOutputStream().print(this.getPatientXmlResponse((Doctor) account.getPerson(), patientName));
					else if(account.getPerson() instanceof Secretary)
						response.getOutputStream().print(this.getPatientXmlResponse(((Secretary)account.getPerson()).getDoctor(), patientName));
				}
				else if(holdenRequest.equals(Requests.INSERT_NEW_CONSULTATION.toString())) {
					String patientId = request.getParameter(Parameeters.PATIENT_ID.toString());
					String price = request.getParameter(Parameeters.PRICE.toString());
					
					response.getOutputStream().print(this.insertNewConsultation(patientId, price));
				}
				else if(holdenRequest.equals(Requests.INSERT_NEW_PRESCRIPTION.toString())) {
					String consultationId = request.getParameter(Parameeters.CONSULTATION_ID.toString());
					String medecineId = request.getParameter(Parameeters.MEDECINE_ID.toString());
					
					response.getOutputStream().print(this.insertNewPrescription(consultationId, medecineId));
				}
				else if(holdenRequest.equals(Requests.GET_PRESCRIPTIONS.toString())) {
					String consultationId = request.getParameter(Parameeters.CONSULTATION_ID.toString());
					
					response.getOutputStream().print(this.getPrescriptionXmlResponse(consultationId));
				}
				else if(holdenRequest.equals(Requests.DELETE_PRESCRIPTION.toString())) {
					String consultationId = request.getParameter(Parameeters.CONSULTATION_ID.toString());
					String medecineId = request.getParameter(Parameeters.MEDECINE_ID.toString());
					
					response.getOutputStream().print(this.deletePrescription(consultationId, medecineId));
				}
				else if(holdenRequest.equals(Requests.UPDATE_PRICE.toString())) {
					String consultationId = request.getParameter(Parameeters.CONSULTATION_ID.toString());
					String newPrice = request.getParameter(Parameeters.PRICE.toString());
					
					response.getOutputStream().print(this.updatePrice(consultationId, newPrice));
				}
			}
		}
	}

	public String getConsultationXmlResponse(Doctor doctor, String consultationDate, String consultationPatient){
        String consultationXml = "";
                
        List<Consultation> consultations = UtilitiesHolder.SEARCHES_HANDLER
        		.getSpecifiedConsultations(
        				doctor, 
        				consultationDate.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}") ?
        					LocalDate.parse(consultationDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null
        				,
        				consultationPatient
        		);
        
        consultationXml = UtilitiesHolder.BEAN_TO_XML_PARSER.fromBeanToXml(new String[]{"consultationId", "consultationDate", "consultationPrice", "patient.patientFirstName", "patient.patientLastName"},
        		consultations.toArray(new Consultation[]{})
        );
                
        return consultationXml;
    }
	
	private String getObservationsXmlResponse(String consultationId) {
		String observationsXml = Responses.ERROR_MESSAGE.toString();
		
		try {
			UUID uuid = UUID.fromString(consultationId);
			
			List<Observation> observations = UtilitiesHolder.SEARCHES_HANDLER.getSpecifiedObservations(uuid);
			
			observationsXml = UtilitiesHolder.BEAN_TO_XML_PARSER.fromBeanToXml(
					new String[] {"description"}, observations.toArray(new Observation[]{}));
		}
		
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }
		
		return observationsXml;
	}
	
	private String insertNewObservation(String consultationId, String obsInfo) {
		try {
			UUID uuid = UUID.fromString(consultationId);
			
			return UtilitiesHolder.UPDATES_HANDLER.insertNewObservation(uuid, obsInfo) ? 
					Responses.INSERT_SUCCESS.toString() : Responses.ERROR_MESSAGE.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String getText(String consultationId, String queryType) {
		try {
			UUID uuid = UUID.fromString(consultationId);
			
			if(queryType.equals(Requests.GET_HM.toString()))
				return UtilitiesHolder.SEARCHES_HANDLER.getHm(uuid);
			
			else if(queryType.equals(Requests.GET_EC.toString()))
				return UtilitiesHolder.SEARCHES_HANDLER.getEc(uuid);
			
			else if(queryType.equals(Requests.GET_DIA.toString()))
				return UtilitiesHolder.SEARCHES_HANDLER.getDia(uuid);
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String updateText(String consultationId, String newValue, String queryType) {		
		try {
			UUID uuid = UUID.fromString(consultationId);
			
			if(
				(queryType.equals(Requests.UPDATE_HM.toString()) && UtilitiesHolder.UPDATES_HANDLER.updateHm(uuid, newValue)) ||
				(queryType.equals(Requests.UPDATE_EC.toString()) && UtilitiesHolder.UPDATES_HANDLER.updateEc(uuid, newValue)) ||
				(queryType.equals(Requests.UPDATE_DIA.toString()) && UtilitiesHolder.UPDATES_HANDLER.updateDia(uuid, newValue))
			) return Responses.UPDATE_SUCCESS.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String deleteObservation(String consultationId, String obsInfo) {
		try {
			UUID uuid = UUID.fromString(consultationId);
			
			if(UtilitiesHolder.UPDATES_HANDLER.deleteObservation(uuid, obsInfo))
				return Responses.REMOVE_SUCCESS.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	public String deleteConsultation(String consultationId) {
		try {
			UUID uuid = UUID.fromString(consultationId);
			
			if(UtilitiesHolder.UPDATES_HANDLER.deleteConsultation(uuid))
				return Responses.REMOVE_SUCCESS.toString();
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
	
	private String insertNewConsultation(String patientId, String price) {
		try {
			UUID uuid = UUID.fromString(patientId);
			
			return UtilitiesHolder.UPDATES_HANDLER.insertNewConsultation(uuid, Double.parseDouble(price)) ? 
					Responses.INSERT_SUCCESS.toString() : Responses.ERROR_MESSAGE.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String insertNewPrescription(String consultationId, String medecineId) {
		try {
			UUID uuid = UUID.fromString(consultationId);
			
			return UtilitiesHolder.UPDATES_HANDLER.insertNewPrescription(uuid, Integer.parseInt(medecineId)) ? 
					Responses.INSERT_SUCCESS.toString() : Responses.ERROR_MESSAGE.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }
		
		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String getPrescriptionXmlResponse(String consultationId) {
		String prescriptionsXml = Responses.ERROR_MESSAGE.toString();
		
		try {
			UUID uuid = UUID.fromString(consultationId);
			
			List<Medecine> prescriptions = UtilitiesHolder.SEARCHES_HANDLER.getSpecifiedPrescriptions(uuid);
			
			prescriptionsXml = UtilitiesHolder.BEAN_TO_XML_PARSER.fromBeanToXml(
					new String[] {"medecineId", "medecineMark", "medecineDosage", "medecineForm"}, prescriptions.toArray(new Medecine[]{}));
		}
		
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }
		
		return prescriptionsXml;
	}
	
	private String deletePrescription(String consultationId, String medecineId) {
		try {
			UUID uuid = UUID.fromString(consultationId);
			
			if(UtilitiesHolder.UPDATES_HANDLER.deletePrescription(uuid, Integer.parseInt(medecineId)))
				return Responses.REMOVE_SUCCESS.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String updatePrice(String consultationId, String newPrice) {
		try {
			UUID uuid = UUID.fromString(consultationId);
			
			if(UtilitiesHolder.UPDATES_HANDLER.updatePrice(uuid, Double.parseDouble(newPrice)))
				return Responses.UPDATE_SUCCESS.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	public static enum Parameeters {
		REQUEST,
		CONSULTATION_DATE,
		CONSULTATION_PATIENT,
		CONSULTATION_ID,
		PATIENT_ID,
		MEDECINE_ID,
		PRICE,
		TEXT_VALUE
	}
	
	public static enum Requests {
		GET_CONSULTATIONS,
		GET_OBSERVATIONS,
		GET_PRESCRIPTIONS,
		GET_HM,
		GET_EC,
		GET_DIA,
		UPDATE_HM,
		UPDATE_EC,
		UPDATE_DIA,
		INSERT_NEW_OBSERVATION,
		INSERT_NEW_CONSULTATION,
		INSERT_NEW_PRESCRIPTION,
		DELETE_OBSERVATION,
		DELETE_CONSULTATION,
		DELETE_PRESCRIPTION,
		GET_PATIENTS,
		UPDATE_PRICE
	}
	
	public static enum Responses {
		ERROR_MESSAGE,
		UPDATE_SUCCESS,
		INSERT_SUCCESS,
		REMOVE_SUCCESS
	}
}
