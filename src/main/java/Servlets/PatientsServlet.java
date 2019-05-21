package Servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import DomainModel.PatientAdministration.Analysis;
import DomainModel.PatientAdministration.Antecedent;
import DomainModel.PatientAdministration.Disease;
import DomainModel.PatientAdministration.Media;
import DomainModel.PatientAdministration.Patient;
import DomainModel.UserAdministration.Account;
import DomainModel.UserAdministration.Doctor;
import DomainModel.UserAdministration.Secretary;
import Utilities.UtilitiesHolder;
import Utilities.SecurityManagers.SecurityManager;


@WebServlet(name = "PatientsServlet", urlPatterns = {"/patients"})
@MultipartConfig
public class PatientsServlet extends HttpServlet {
	
	public static final int RESULT_LIMIT = 100;
	public static final String ATT_PATIENTS = "PATIENTS_LIST";
	public static final String ATT_ANALYSIS_TYPE = "ANALYSIS_TYPES";
	public static final String ATT_DISEASES = "DISEASES_LIST";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 if(UtilitiesHolder.AUTHENTICATION_SECURITY_MANAGER.isAuthenticated(request)) { 
			 Account compte = (Account) request.getSession().getAttribute(SecurityManager.SESSION_SCOPE_ACCOUNT_KEY);
			 
			 if(compte.getPerson() instanceof Doctor)
	            request.setAttribute(ATT_PATIENTS, UtilitiesHolder.SEARCHES_HANDLER.getAllPatients((Doctor)compte.getPerson()));
			 else 
				request.setAttribute(ATT_PATIENTS, UtilitiesHolder.SEARCHES_HANDLER.getAllPatients(((Secretary)compte.getPerson()).getDoctor()));
			 
			 request.setAttribute(ATT_ANALYSIS_TYPE, UtilitiesHolder.SEARCHES_HANDLER.getAllAnalysisTypes());
			 request.setAttribute(ATT_DISEASES, UtilitiesHolder.SEARCHES_HANDLER.getAllDiseases());
	         
			 this.getServletContext().getRequestDispatcher(UtilitiesHolder.JSPS_FOLDER + "patients.jsp").forward(request, response);
	     }
	     else this.getServletContext().getRequestDispatcher(UtilitiesHolder.JSPS_FOLDER + UtilitiesHolder.NOT_AUTHENTICATED_USER_JSP).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding(UtilitiesHolder.ENCODING_POST_RESPONSE);
		
		if(UtilitiesHolder.AUTHENTICATION_SECURITY_MANAGER.isAuthenticated(request)){
			String patientId;
			
			String requestContent = request.getParameter(Parameeters.REQUEST.toString());
						
			if(requestContent != null) {				
				Account compte = (Account) request.getSession().getAttribute(SecurityManager.SESSION_SCOPE_ACCOUNT_KEY);
				
				if(requestContent.equals(Requests.GET_PATIENTS.toString())) {
					String selection = request.getParameter(Parameeters.TEXT_VALUE.toString());
					if(compte.getPerson() instanceof Doctor)
						 response.getOutputStream().write(
							 this.getPatientXmlResponse((Doctor) compte.getPerson(), selection).getBytes()
						 );
					else response.getOutputStream().write(
							this.getPatientXmlResponse(((Secretary) compte.getPerson()).getDoctor() ,selection).getBytes()
						 );
				}
				else if(requestContent.equals(Requests.GET_PERSONNAL_INFORMATIONS.toString())) {
					patientId = request.getParameter(Parameeters.PATIENT_ID.toString());
					
					response.getOutputStream().write(
							this.getPatientPersonalInfo(patientId).getBytes(UtilitiesHolder.ENCODING_POST_RESPONSE)
					);
				}
				else if(requestContent.equals(Requests.GET_DISEASES.toString())) {
					patientId = request.getParameter(Parameeters.PATIENT_ID.toString());
					
					response.getOutputStream().write(this.getDiseaseXmlResponse(patientId).getBytes("UTF-8"));
				}
				else if(requestContent.equals(Requests.GET_ANTECEDANTS.toString())) {
					patientId = request.getParameter(Parameeters.PATIENT_ID.toString());
					
					response.getOutputStream().write(this.getAntecedentXmlResponse(patientId).getBytes(UtilitiesHolder.ENCODING_POST_RESPONSE));
				}
				else if(requestContent.equals(Requests.GET_ANALYSIS.toString())) {
					patientId = request.getParameter(Parameeters.PATIENT_ID.toString());
					
					response.getOutputStream().write(this.getAnalysisXmlResponse(patientId).getBytes(UtilitiesHolder.ENCODING_POST_RESPONSE));
				}
				else if(requestContent.equals(Requests.INSERT_NEW_ANALYSIS.toString())) {
					patientId = request.getParameter(Parameeters.PATIENT_ID.toString());
					String analysisName = request.getParameter(Parameeters.ANALYSIS_ID.toString()),
						   analysisdate = request.getParameter(Parameeters.ANALYSIS_DATE.toString()),
						   analysisResult = request.getParameter(Parameeters.ANALYSIS_RESULT.toString());
					
					response.getOutputStream().write(this.insertNewAnalysis(patientId,analysisName,analysisdate,analysisResult).getBytes(UtilitiesHolder.ENCODING_POST_RESPONSE));
				}
				else if(requestContent.equals(Requests.INSERT_NEW_ANTECEDANT.toString())) {
					patientId = request.getParameter(Parameeters.PATIENT_ID.toString());
					String antecedentType = request.getParameter(Parameeters.ANTECEDANT_TYPE.toString()),
							antecedentStartDate = request.getParameter(Parameeters.ANTECEDANT_START_DATE.toString()),
							antecedentRecoveryDate = request.getParameter(Parameeters.ANTECEDANT_RECOVERY_DATE.toString()),
							antecedentDescription = request.getParameter(Parameeters.ANTECEDANT_DESCRIPTION.toString());
					
					response.getOutputStream().write(this.insertNewAntecedent(patientId,antecedentType,antecedentStartDate,antecedentRecoveryDate,antecedentDescription).getBytes(UtilitiesHolder.ENCODING_POST_RESPONSE));
				}
				
				else if(requestContent.equals(Requests.DELETE_PATIENT.toString())) {
					patientId = request.getParameter(Parameeters.PATIENT_ID.toString());
					
					response.getOutputStream().write(this.deletePatient(patientId).getBytes(UtilitiesHolder.ENCODING_POST_RESPONSE));
				}
				
				else if(requestContent.equals(Requests.DELETE_DISEASE.toString())) {
					patientId = request.getParameter(Parameeters.PATIENT_ID.toString());
				    String diseaseId = request.getParameter(Parameeters.DISEASE_ID.toString());
				    
					response.getOutputStream().write(this.deleteDisease(patientId, diseaseId).getBytes(UtilitiesHolder.ENCODING_POST_RESPONSE));
				}
				
				else if(requestContent.equals(Requests.DELETE_ANALYSIS.toString())) {
					patientId = request.getParameter(Parameeters.PATIENT_ID.toString());
					String analysisId = request.getParameter(Parameeters.ANALYSIS_ID.toString());
					
					response.getOutputStream().write(this.deleteAnalysis(patientId,analysisId).getBytes(UtilitiesHolder.ENCODING_POST_RESPONSE));
				}
				
				else if(requestContent.equals(Requests.DELETE_ANTECEDANT.toString())) {
					patientId = request.getParameter(Parameeters.PATIENT_ID.toString());
					String antecedentStartDate = request.getParameter(Parameeters.ANTECEDANT_START_DATE.toString()),
						   antecedentRecoveryDate = request.getParameter(Parameeters.ANTECEDANT_RECOVERY_DATE.toString()),
						   antecedentType = request.getParameter(Parameeters.ANTECEDANT_TYPE.toString());
					
					response.getOutputStream().write(this.deleteAntecedent(patientId,antecedentStartDate,antecedentRecoveryDate,antecedentType).getBytes(UtilitiesHolder.ENCODING_POST_RESPONSE));
				}
				
				else if(requestContent.equals(Requests.INSERT_NEW_PATIENT.toString())) {
					String patientName = request.getParameter(Parameeters.PATIENT_LAST_NAME.toString()),
							patientFirstName = request.getParameter(Parameeters.PATIENT_FIRST_NAME.toString()),
							patientBirthDate = request.getParameter(Parameeters.PATIENT_BIRTHDAY.toString()),
							PatientGender = request.getParameter(Parameeters.PATIENT_GENDER.toString());
					
					Doctor doc = null;
					
					if(compte.getPerson() instanceof Doctor) 
						doc = (Doctor) compte.getPerson();
					else doc = ((Secretary) compte.getPerson()).getDoctor();
					
					response.getOutputStream().write(this.insertNewPatient(doc, patientFirstName,patientName,patientBirthDate,PatientGender).getBytes(UtilitiesHolder.ENCODING_POST_RESPONSE));	
				}
				else if(requestContent.equals(Requests.INSERT_NEW_DISEASE.toString())) {
					patientId =  request.getParameter(Parameeters.PATIENT_ID.toString());
					String maladieId = request.getParameter(Parameeters.DISEASE_ID.toString());
					
					response.getOutputStream().write(this.insertNewDisease(patientId, maladieId).getBytes(UtilitiesHolder.ENCODING_POST_RESPONSE));
				}
				else if(requestContent.equals(Requests.UPDATE_PERSONNAL_INFORMATIONS.toString())) {
					patientId =  request.getParameter(Parameeters.PATIENT_ID.toString());
					String patientAdr = request.getParameter(Parameeters.PATIENT_ADDRESS.toString()),
						 patientProfession = request.getParameter(Parameeters.PATIENT_PROFESSION.toString()),
						 patientPhoneNumber = request.getParameter(Parameeters.PATIENT_PHONE_NUMBER.toString()),
						 patientEmail = request.getParameter(Parameeters.PATIENT_EMAIL.toString()),
						 patientInsuranceCode = request.getParameter(Parameeters.PATIENT_INSURANCE_CARD.toString());
					
					response.getOutputStream().write(this.updatePersonalInformations(patientId,patientAdr,patientProfession,patientPhoneNumber, patientEmail,patientInsuranceCode).getBytes(UtilitiesHolder.ENCODING_POST_RESPONSE));
				}
				else if(requestContent.equals(Requests.UPDATE_ANTECEDANT.toString())){
					patientId = request.getParameter(Parameeters.PATIENT_ID.toString());
				
					String antecedentStartDate = request.getParameter(Parameeters.ANTECEDANT_START_DATE.toString()),
						   antecedentRecoveryDate = request.getParameter(Parameeters.ANTECEDANT_RECOVERY_DATE.toString()),
						   antecedentType = request.getParameter(Parameeters.ANTECEDANT_TYPE.toString()),
						   antecedentNewDescription = request.getParameter(Parameeters.ANTECEDANT_NEW_DESCRIPTION.toString());
					
					response.getOutputStream().write(this.updateAntecedant(patientId, antecedentStartDate, antecedentRecoveryDate, antecedentType, antecedentNewDescription).getBytes(UtilitiesHolder.ENCODING_POST_RESPONSE));
				}
				else if(requestContent.equals(Requests.UPLOAD_RADIO.toString())) {
					String radioDate = request.getParameter(Parameeters.RADIO_DATE.toString());
			        String radioDescription = request.getParameter(Parameeters.RADIO_DESCRIPTION.toString());
			        Part fileToUpload = request.getPart(Parameeters.RADIO_CONTENT.toString());
			        
			        patientId = request.getParameter(Parameeters.PATIENT_ID.toString());
			        
			        response.getOutputStream().print(
			        	this.uploadRadio(fileToUpload, patientId, radioDate, radioDescription)
					);
				}
				else if(requestContent.equals(Requests.GET_RADIOS.toString())) {
					patientId = request.getParameter(Parameeters.PATIENT_ID.toString());
					
					response.getOutputStream().write(
							this.getRadiosXmlResponse(patientId).getBytes(UtilitiesHolder.ENCODING_POST_RESPONSE));
				}
				else if(requestContent.equals(Requests.DELETE_RADIO.toString())) {
					String radioId = request.getParameter(Parameeters.RADIO_ID.toString());
					
					response.getOutputStream().write(
							this.deleteRadio(radioId).getBytes(UtilitiesHolder.ENCODING_POST_RESPONSE));
				}
			}	
		}
	}
	
	private String deleteRadio(String radioId) {
		try {
			UUID uuid = UUID.fromString(radioId);
			
			if(UtilitiesHolder.UPDATES_HANDLER.deleteMedia(uuid))
				return Responses.REMOVE_SUCCESS.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}

	private String getRadiosXmlResponse(String patientId) {
		try {
			UUID uuid = UUID.fromString(patientId);

	        String reponse = "";
	                
	        List<Media> radios = UtilitiesHolder.SEARCHES_HANDLER.getRadios(uuid);
	        
	        reponse = UtilitiesHolder.BEAN_TO_XML_PARSER.fromBeanToXml(new String[]{"mediaDate", "mediaDescription", "mediaFilePath", "mediaFileExtension"},
	        		radios.toArray(new Media[radios.size()])
	        );
	                
	        return reponse;
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}

	private String updateAntecedant(String patientId, String antecedentStartDate, String antecedentRecoveryDate,
			String antecedentType, String antecedentNewDescription) {
		try {
			UUID uuid = UUID.fromString(patientId);
			
			if(UtilitiesHolder.UPDATES_HANDLER.updateAntecedent(
					uuid, 
					LocalDate.parse(antecedentStartDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
				    LocalDate.parse(antecedentRecoveryDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")), 
				    Antecedent.AntecedentType.values()[Integer.parseInt(antecedentType)],
				    antecedentNewDescription)
			) return Responses.REMOVE_SUCCESS.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}

	private String getDiseaseXmlResponse(String patientId) {
		try {
			patientId = patientId.replaceAll("}", "");
			UUID uuid = UUID.fromString(patientId);

	        String reponse = "";
	                
	      List<Disease> diseases = UtilitiesHolder.SEARCHES_HANDLER.getDiseases(uuid);
	        
	        reponse = UtilitiesHolder.BEAN_TO_XML_PARSER.fromBeanToXml(new String[]{"diseaseName","diseaseId"},
	        		diseases.toArray(new Disease[]{})
	        );
	                
	        return reponse;
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String getAntecedentXmlResponse(String patientId) {
		try {
			patientId = patientId.replaceAll("}", "");
			UUID uuid = UUID.fromString(patientId);

	        String reponse = "";
	                
	      List<Antecedent> antecedents = UtilitiesHolder.SEARCHES_HANDLER.getAntecedents(uuid);
	        
	        reponse = UtilitiesHolder.BEAN_TO_XML_PARSER.fromBeanToXml(new String[]{"antecedentType","antecedentStartDate","antecedentRecoveryDate","antecedentDescription"},
	        		antecedents.toArray(new Antecedent[]{})
	        );         
	        return reponse;
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
		
	}
	
	private String getAnalysisXmlResponse(String patientId) {
		try {
			patientId = patientId.replaceAll("}", "");
			UUID uuid = UUID.fromString(patientId);

	        String reponse = "";
	                
	      List<Analysis> analyses = UtilitiesHolder.SEARCHES_HANDLER.getAnalysis(uuid);
	        
	        reponse = UtilitiesHolder.BEAN_TO_XML_PARSER.fromBeanToXml(new String[]{"analysisDate","analysisType.analysisTypeName","analysisResult"},
	        		analyses.toArray(new Analysis[]{})
	        );        
	        return reponse;
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String deleteDisease(String patientId,String diseaseId) {
		try {
			patientId = patientId.replaceAll("}","");
			diseaseId = diseaseId.replaceAll("}", "");
			UUID uuid = UUID.fromString(patientId);
			
			if(UtilitiesHolder.UPDATES_HANDLER.deleteDisease(uuid, diseaseId))
				return Responses.REMOVE_SUCCESS.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String insertNewDisease(String patientId,String maladieId) {
		try {
			patientId = patientId.replaceAll("}","");
			UUID uuid = UUID.fromString(patientId);
			
			return UtilitiesHolder.UPDATES_HANDLER.insertNewDisease(uuid, Integer.parseInt(maladieId)) ? 
					Responses.INSERT_SUCCESS.toString() : Responses.ERROR_MESSAGE.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String insertNewPatient(Doctor doc, String fName,String Lname,String BirthDate,String gender) {
		try {
			
			
			return UtilitiesHolder.UPDATES_HANDLER.insertNewPatient(doc, fName,Lname,BirthDate, gender) ? 
					Responses.INSERT_SUCCESS.toString() : Responses.ERROR_MESSAGE.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String insertNewAnalysis(String patientId,String analysisName,String analysisdate,String analysisResult){
		try {
			patientId = patientId.replaceAll("}","");
			UUID uuid = UUID.fromString(patientId);
			
			return UtilitiesHolder.UPDATES_HANDLER.insertNewAnalysis(uuid, analysisName, analysisdate, analysisResult) ? 
					Responses.INSERT_SUCCESS.toString() : Responses.ERROR_MESSAGE.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String insertNewAntecedent(String patientId,String antecedentType,String antecedentStartDate,String antecedentRecoveryDate,String antecedentDescription) {
		try {
			patientId = patientId.replaceAll("}","");
			UUID uuid = UUID.fromString(patientId);
			antecedentType = antecedentType.trim();
			int index = Integer.parseInt(antecedentType );
			
			return UtilitiesHolder.UPDATES_HANDLER.insertNewAntecedent(uuid, index, antecedentStartDate, antecedentRecoveryDate, antecedentDescription) ? 
					Responses.INSERT_SUCCESS.toString() : Responses.ERROR_MESSAGE.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String getPatientXmlResponse(Doctor doctor, String patientName){

        String patientXml = "";
                
        List<Patient> patients = UtilitiesHolder.SEARCHES_HANDLER
        		.getSpecifiedPatientsByNameOnly(
        				doctor,  
        				patientName
        		);
        
        patientXml = UtilitiesHolder.BEAN_TO_XML_PARSER.fromBeanToXml(new String[]{"patientId", "patientFirstName", "patientLastName", "patientBirthday","patientGender"},
        		patients.toArray(new Patient[]{})
        );
                
        return patientXml;
    }

	
	private String deletePatient(String patientId) {
		try {
			patientId = patientId.replaceAll("}", "");
			UUID uuid = UUID.fromString(patientId);
			
			if(UtilitiesHolder.UPDATES_HANDLER.deletePatient(uuid))
				return Responses.REMOVE_SUCCESS.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String deleteAnalysis(String patientId, String analysisId) {
		try {
			patientId = patientId.replaceAll("}", "");
			UUID uuid = UUID.fromString(patientId);
			
			if(UtilitiesHolder.UPDATES_HANDLER.deleteAnalysis(uuid, analysisId))
				return Responses.REMOVE_SUCCESS.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String deleteAntecedent(String patientId,String antecedentStartDate,String antecedentRecoveryDate,String antecedentType) {
		try {
			patientId = patientId.replaceAll("}", "");
			UUID uuid = UUID.fromString(patientId);
			antecedentType = antecedentType.trim();
			int antecedentTypeIndex = Integer.parseInt(antecedentType);
			
			if(UtilitiesHolder.UPDATES_HANDLER.deleteAntecedent(uuid,antecedentStartDate,antecedentRecoveryDate,antecedentTypeIndex))
				return Responses.REMOVE_SUCCESS.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String getPatientPersonalInfo(String uuidPatient) {
		try {
			uuidPatient = uuidPatient.replaceAll("}", "");
			UUID uuid = UUID.fromString(uuidPatient);

	        String patientXml = "";
	                
	      List<Patient> patients = UtilitiesHolder.SEARCHES_HANDLER.getPatientInfo(uuid);
	        
	        patientXml = UtilitiesHolder.BEAN_TO_XML_PARSER.fromBeanToXml(new String[]{"patientProfession", "patientPhoneNumber", "patientEmail", "patientAddress","patientInsuranceCard"},
	        		patients.toArray(new Patient[]{})
	        );
	                
	        return patientXml;
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();
	}
	
	private String updatePersonalInformations(String patientId,String patientAdr,String patientProfession,String patientPhoneNumber, String patientEmail,String patientInsuranceCode) {
		try {
			patientId = patientId.replaceAll("}", "");
			UUID uuid = UUID.fromString(patientId);
	                

			if(UtilitiesHolder.UPDATES_HANDLER.updatePatientPersonalInfo(uuid, patientAdr, patientProfession, patientPhoneNumber, patientEmail, patientInsuranceCode))
				return Responses.UPDATE_SUCCESS.toString();
		}
		catch(IllegalArgumentException exp) { exp.printStackTrace(); }

		return Responses.ERROR_MESSAGE.toString();	       
	}
	
	private String uploadRadio(Part fileToUpload, String patientId, String radioDate, String radioDescription) {
		if(fileToUpload != null && fileToUpload.getSize() != 0) {
			try {
				String fileExtension = fileToUpload.getSubmittedFileName().substring(fileToUpload.getSubmittedFileName().lastIndexOf('.'));
				
				UUID uuid = UUID.fromString(patientId);
				
				LocalDate radioLocalDate = LocalDate.parse(radioDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				
				if(UtilitiesHolder.FILES_UPLOADER.uploaderMedia(
						fileToUpload, UtilitiesHolder.UPDATES_HANDLER.uploadRadio(uuid, radioLocalDate, radioDescription, fileExtension)))
					return Responses.INSERT_SUCCESS.toString();
			}
			catch(ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException exp) {
				exp.printStackTrace();
				
				return Responses.ERROR_MESSAGE.toString();
			}
		}
		
		return Responses.ERROR_MESSAGE.toString();
	}
	
	public static enum Requests {
		GET_PATIENTS,
		GET_PERSONNAL_INFORMATIONS,
		GET_ANTECEDANTS,
		GET_DISEASES,
		GET_ANALYSIS,
		DELETE_PATIENT,
		DELETE_ANALYSIS,
		INSERT_NEW_PATIENT,
		INSERT_NEW_ANALYSIS,
		UPDATE_PERSONNAL_INFORMATIONS,
		UPDATE_ANTECEDANT,
		INSERT_NEW_DISEASE,
		DELETE_DISEASE,
		INSERT_NEW_ANTECEDANT,
		DELETE_ANTECEDANT,
		UPLOAD_RADIO, GET_RADIOS, DELETE_RADIO
	}
	
	public static enum Parameeters{
		REQUEST,
		TEXT_VALUE,
		PATIENT_ID,
		PATIENT_FIRST_NAME,
		PATIENT_LAST_NAME,
		PATIENT_BIRTHDAY,
		PATIENT_GENDER,
		PATIENT_ADDRESS,
		PATIENT_PROFESSION,
		PATIENT_PHONE_NUMBER,
		PATIENT_EMAIL,
		PATIENT_INSURANCE_CARD,
		DISEASE_ID,
		ANALYSIS_ID,
		ANALYSIS_DATE,
		ANALYSIS_RESULT,
		ANTECEDANT_TYPE,
		ANTECEDANT_START_DATE,
		ANTECEDANT_RECOVERY_DATE,
		ANTECEDANT_DESCRIPTION,
		ANTECEDANT_NEW_DESCRIPTION,
		RADIO_NAME,
		RADIO_DESCRIPTION,
		RADIO_CONTENT, 
		RADIO_DATE, 
		RADIO_ID
	}
	public static enum Responses {
		ERROR_MESSAGE,
		UPDATE_SUCCESS,
		INSERT_SUCCESS,
		REMOVE_SUCCESS
	}
}
