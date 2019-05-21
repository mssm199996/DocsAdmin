package Utilities.DataHandlers.OutputDataHandlers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import DomainModel.AppointmentAdministration.Appointment;
import DomainModel.AppointmentAdministration.Paraclinic;
import DomainModel.AppointmentAdministration.Reason;
import DomainModel.ConsultationAdministration.Consultation;
import DomainModel.ConsultationAdministration.Observation;
import DomainModel.DocumentAdministration.Document;
import DomainModel.MedecineAdministration.Medecine;
import DomainModel.PatientAdministration.Analysis;
import DomainModel.PatientAdministration.AnalysisType;
import DomainModel.PatientAdministration.Antecedent;
import DomainModel.PatientAdministration.Disease;
import DomainModel.PatientAdministration.Media;
import DomainModel.PatientAdministration.Patient;
import DomainModel.UserAdministration.Account;
import DomainModel.UserAdministration.Doctor;
import DomainModel.UserAdministration.Secretary;
import DomainModel.UserAdministration.Speciality;
import Servlets.AppointmentsServlet;
import Servlets.ConsultationsServlet;
import Servlets.DocumentsServlet;
import Servlets.MedecinesServlet;
import Utilities.UtilitiesHolder;

public class SearchesHandler {
	
	public Account isAccountValid(String email, String password) {
		List<Account> result = (List<Account>) (Object)
				UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
						"FROM Account as acc " + 
						"WHERE (" + 
							"acc.accountEmail = ? and acc.accountPassword = ?" +
						")", email, password);
		return (result.size() == 0 ? null : result.get(0));
	}
	
	public boolean doesEmailExist(String email) {
		return (((List<Account>) (Object)
				UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
						"FROM Account as acc " + 
						"WHERE (acc.accountEmail = ?)", email)).size() > 0);
	}
	
	public Speciality getSpecifiedSpeciality(int specialityIndex) {
		List<Speciality> result = (List<Speciality>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				"FROM Speciality");
		
		return (result.size() <= specialityIndex ? null : result.get(specialityIndex));
	}
	
	public Doctor isDoctorValid(String docUuid) {
		System.out.println(docUuid);
		try {
			UUID uuid = UUID.fromString(docUuid);
			
			List<Doctor> result = (List<Doctor>) (Object)
					UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
							"FROM Doctor as doc " +
							"LEFT JOIN FETCH doc.secretaries as secretary " +
							"WHERE doc.personId = ?", uuid
			);
			
			if(result.size() == 0)
				return null;
			
			return result.get(0);
		}
		catch(IllegalArgumentException exp) {
			return null;
		}
	}
	// ------------------------- Patients -----------------------------
	
	public List<Patient> getAllPatients(Doctor doctor){
		return (List<Patient>) (Object)
    			UtilitiesHolder.SESSION_FACTORY_HANDLER.selectWithLimit(
    					"FROM Patient pat " + 
    					"WHERE pat.doctor = ?",
    					ConsultationsServlet.RESULT_LIMIT, doctor
    			);
	}
	
	public List<Patient> getSpecifiedPatientsByNameOnly(Doctor doctor, String patientName) {
		if(patientName == null || patientName.equals(""))
			return this.getAllPatients(doctor);
		
		String hqlRequest = "FROM Patient pat " +
			    			"WHERE ((pat.doctor = ?) and ";
			    	    	
		String[] requestParams = this.tokenMePlease(patientName, " ");
        Object[] effectiveParams = new Object[2 * requestParams.length + 1];
        		 effectiveParams[0] = doctor;
        
        for(int i = 0; i < requestParams.length; i++){
            hqlRequest += "(pat.patientFirstName like ? or pat.patientLastName like ?) and ";
            
            effectiveParams[2 * i + 1] = "%" + requestParams[i] + "%";
            effectiveParams[2 * i + 2] = effectiveParams[2 * i + 1];
        }
        
        hqlRequest = hqlRequest.substring(0, hqlRequest.length() - 5) + ")";
                        
        return (List<Patient>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.selectWithLimit(
                hqlRequest,
                ConsultationsServlet.RESULT_LIMIT,
                effectiveParams
        );
	}
	
	public List<Patient> getSpecifiedPatients(Doctor doctor, String patientName) {
		if(patientName == null || patientName.equals(""))
			return this.getAllPatients(doctor);
		
		String hqlRequest = "FROM Patient pat LEFT JOIN FETCH pat.diseases ds " +
			    			"WHERE ((pat.doctor = ?) and ";
			    	    	
		String[] requestParams = this.tokenMePlease(patientName, " ");
        Object[] effectiveParams = new Object[3 * requestParams.length + 1];
        		 effectiveParams[0] = doctor;

        for(int i = 0; i < requestParams.length; i++){
            hqlRequest += "(pat.patientFirstName like ? or pat.patientLastName like ?  or ds.diseaseName like ? ) and ";
            
            effectiveParams[3 * i + 1] = "%" + requestParams[i] + "%";
            effectiveParams[3 * i + 2] = effectiveParams[3 * i + 1];
            effectiveParams[3 * i + 3] = effectiveParams[3 * i + 1];
        }
        
        
        hqlRequest = hqlRequest.substring(0, hqlRequest.length() - 5) + ")";
        
        return (List<Patient>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.selectWithLimit(
                hqlRequest,
                ConsultationsServlet.RESULT_LIMIT,
                effectiveParams
        );
	}
	
	public List<Antecedent> getSpecifiedAntecedent(UUID uuid) {
    	List<Patient> result = (List<Patient>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Patient pat " +
    			"LEFT OUTER JOIN FETCH pat.antecedents " +
    			"WHERE pat.patientId = ?", 
    			uuid
    	);
    	
    	return result.size() == 0 ? null : result.get(0).getAntecedents();  	
	}
	
	public List<Disease> getDiseases(UUID patientId) {
    	List<Patient> result = (List<Patient>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Patient pat LEFT OUTER JOIN FETCH pat.diseases " +
    			"WHERE pat.patientId = ?", 
    			patientId
    	);
    	
    	if(result.size() == 0)
    		return null;
    	
    	return result.get(0).getDiseases();
    }
	 
	public List<Antecedent> getAntecedents(UUID patientId){
		List<Patient> result = (List<Patient>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Patient pat LEFT OUTER JOIN FETCH pat.antecedents " +
    			"WHERE pat.patientId = ?", 
    			patientId
    	);
		if(result.size() == 0) return null;
		return result.get(0).getAntecedents();
	}
	
	public List<Analysis> getAnalysis(UUID patientId){
		List<Patient> result = (List<Patient>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Patient pat LEFT OUTER JOIN FETCH pat.analysies " +
    			"WHERE pat.patientId = ?", 
    			patientId
    	);
		if(result.size() == 0) return null;
		return result.get(0).getAnalysies();
	}
	
	public List<Media> getRadios(UUID patientId){
		List<Patient> result = (List<Patient>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Patient pat LEFT OUTER JOIN FETCH pat.medias " +
    			"WHERE pat.patientId = ?", 
    			patientId
    	);
		if(result.size() == 0) return null;
		return result.get(0).getMedias();
	}
	
	
	public List<Patient> getPatientInfo(UUID uuidPatient){
		return (List<Patient>) (Object)
    			UtilitiesHolder.SESSION_FACTORY_HANDLER.selectWithLimit(
    					"FROM Patient pat " + 
    					"WHERE pat.patientId = ?",
    					ConsultationsServlet.RESULT_LIMIT, uuidPatient
    			);
	}
	
	public List<Disease> getAllDiseases(){
		return (List<Disease>) (Object)
				UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
						"FROM Disease"
				);
	}
	
	//------------------------- Analysis -------------------------------
	public List< AnalysisType> getAllAnalysisTypes(){
		return (List<AnalysisType>) (Object)
				UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
						"FROM AnalysisType"
						);
	}
	
	// ------------------------ Medicines -----------------------------
	
    public List<Medecine> getAllMedecines(){
        return (List<Medecine>) (Object) 
                UtilitiesHolder.SESSION_FACTORY_HANDLER.selectWithLimit(
                    "FROM Medecine",
                    MedecinesServlet.RESULT_LIMIT
                );
    }
    
    
    public List<Medecine> getSpecifiedMedecines(String request){
        if(request == null || request.equals(""))
            return this.getAllMedecines();
        
        String[] requestParams = this.tokenMePlease(request, " ");
        String[] effectiveParams = new String[2 * requestParams.length];
        
        String hqlRequest = "FROM Medecine " +
                            "WHERE (";
        
        for(int i = 0; i < requestParams.length; i++){
            hqlRequest += "(medecineDci like ? or medecineMark like ?) and ";
            
            effectiveParams[2 * i] = "%" + requestParams[i] + "%";
            effectiveParams[2 * i + 1] = effectiveParams[2 * i];
        }
        
        hqlRequest = hqlRequest.substring(0, hqlRequest.length() - 5) + ")";
                
        return (List<Medecine>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.selectWithLimit(
                hqlRequest,
                MedecinesServlet.RESULT_LIMIT,
                effectiveParams
        );
    }
    
    //--------------------------------- Consultations -----------------------------------------
    
    public List<Consultation> getAllConsultations(Doctor doctor){
    	return (List<Consultation>) (Object)
    			UtilitiesHolder.SESSION_FACTORY_HANDLER.selectWithLimit(
    					"FROM Consultation con " + 
    					"WHERE con.patient.doctor = ?",
    					ConsultationsServlet.RESULT_LIMIT, doctor
    			);
    }
    
    public List<Consultation> getSpecifiedConsultations(Doctor doctor, LocalDate consultationDate, String consultationPatient){
    	if((consultationPatient == null && consultationDate == null) || (consultationPatient.replaceAll(" ", "").equals("") && consultationDate == null))
    		return getAllConsultations(doctor);
    	
    	String hqlRequest = "FROM Consultation con " +
    			"WHERE ((con.patient.doctor = ?) and ";
    	    	
    	String[] consultationPatientParams = this.tokenMePlease(consultationPatient, " ");    	
    	    	
    	Object[] effectiveParams = new Object[1 + 2 * consultationPatientParams.length + (consultationDate != null ? 1 : 0)];
		 		 effectiveParams[0] = doctor;
    			 		 
    	if(consultationDate != null) {
    		hqlRequest += "(con.consultationDate = ?) and ";
    		effectiveParams[1] = consultationDate;
    	}
    	
    	int offset = (effectiveParams[1] == null ? 1 : 2);
    	
    	for(int i = 0; i < consultationPatientParams.length; i++){
            hqlRequest += "(con.patient.patientFirstName like ? or con.patient.patientLastName like ?) and ";
            
            effectiveParams[2 * i + offset] = "%" + consultationPatientParams[i] + "%";
            effectiveParams[2 * i + offset + 1] = effectiveParams[2 * i + offset];
        }
    	
    	hqlRequest = hqlRequest.substring(0, hqlRequest.length() - 5) + ")";
    	
    	List<Consultation> result = (List<Consultation>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.selectWithLimit(
                hqlRequest,
                ConsultationsServlet.RESULT_LIMIT,
                effectiveParams
        );
    	
        return result;
    }
    
    public List<Observation> getSpecifiedObservations(UUID uuid){
    	List<Consultation> result = (List<Consultation>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Consultation con " +
    			"LEFT OUTER JOIN FETCH con.observations " +
    			"WHERE con.consultationId = ?", 
    			uuid
    	);
    	
    	return result.size() == 0 ? null : result.get(0).getObservations();
    }
    
    public String getHm(UUID consultationId) {
    	List<Consultation> result = (List<Consultation>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Consultation con " +
    			"WHERE con.consultationId = ?", 
    			consultationId
    	);
    	
    	if(result.size() == 0)
    		return "R.A.S";
    	
    	return result.get(0).getConsultationAnamnesis();
    }
    
    public String getEc(UUID consultationId) {
    	List<Consultation> result = (List<Consultation>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Consultation con " +
    			"WHERE con.consultationId = ?", 
    			consultationId
    	);
    	
    	if(result.size() == 0)
    		return "R.A.S";
    	
    	return result.get(0).getConsultationPhex();
    }
    
    public String getDia(UUID consultationId) {
    	List<Consultation> result = (List<Consultation>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Consultation con " +
    			"WHERE con.consultationId = ?", 
    			consultationId
    	);
    	
    	if(result.size() == 0)
    		return "R.A.S";
    	
    	return result.get(0).getConsultationDiag();
    }
    
    public List<Medecine> getSpecifiedPrescriptions(UUID uuid) {
    	List<Consultation> result = (List<Consultation>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Consultation con " +
    			"LEFT OUTER JOIN FETCH con.prescriptions " +
    			"WHERE con.consultationId = ?", 
    			uuid
    	);
    	
    	return result.size() == 0 ? null : result.get(0).getPrescriptions();    	
	}
    
    // ---------------------------- Appointments -------------------------------------
    
    public List<Appointment> getAllAppointments(Doctor doctor){
    	return (List<Appointment>) (Object)
    			UtilitiesHolder.SESSION_FACTORY_HANDLER.selectWithLimit(
    					"FROM Appointment app " + 
    					"WHERE app.patient.doctor = ?",
    					AppointmentsServlet.RESULT_LIMIT, doctor
    			);
    }
    
    public List<Appointment> getSpecifiedAppointments(Doctor doctor, LocalDate appointmentDate, String appointmentPatient) {
    	if((appointmentPatient == null && appointmentDate == null) || (appointmentPatient.replaceAll(" ", "").equals("") && appointmentDate == null))
    		return getAllAppointments(doctor);
    	
    	String hqlRequest = "FROM Appointment app " +
    			"WHERE ((app.patient.doctor = ?) and ";
    	    	
    	String[] appointmentPatientParams = this.tokenMePlease(appointmentPatient, " ");    	
    	    	
    	Object[] effectiveParams = new Object[1 + 2 * appointmentPatientParams.length + (appointmentDate != null ? 1 : 0)];
		 		 effectiveParams[0] = doctor;
    			 		 
    	if(appointmentDate != null) {
    		hqlRequest += "(app.appointmentDate = ?) and ";
    		effectiveParams[1] = appointmentDate;
    	}
    	
    	int offset = (effectiveParams[1] == null ? 1 : 2);
    	
    	for(int i = 0; i < appointmentPatientParams.length; i++){
            hqlRequest += "(app.patient.patientFirstName like ? or app.patient.patientLastName like ?) and ";
            
            effectiveParams[2 * i + offset] = "%" + appointmentPatientParams[i] + "%";
            effectiveParams[2 * i + offset + 1] = effectiveParams[2 * i + offset];
        }
    	
    	hqlRequest = hqlRequest.substring(0, hqlRequest.length() - 5) + ")";
    	
    	List<Appointment> result = (List<Appointment>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.selectWithLimit(
                hqlRequest,
                AppointmentsServlet.RESULT_LIMIT,
                effectiveParams
        );
    	
        return result;
	}
    
    public List<Reason> getSpecifiedReasons(UUID appointmentId) {
    	List<Appointment> result = (List<Appointment>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Appointment app " +
    			"LEFT OUTER JOIN FETCH app.reasons " +
    			"WHERE app.appointmentId = ?", 
    			appointmentId
    	);
    	
    	return result.size() == 0 ? null : result.get(0).getReasons();
	}

	public List<Paraclinic> getSpecifiedParaclinics(UUID appointmentId) {
		List<Appointment> result = (List<Appointment>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Appointment app " +
    			"LEFT OUTER JOIN FETCH app.paraclinics " +
    			"WHERE app.appointmentId = ?", 
    			appointmentId
    	);
    	
    	return result.size() == 0 ? null : result.get(0).getParaclinics();
	}
    
    // ----------------------------- Documents ---------------------------------------
    
    public List<Document> getAllDocuments(Doctor doctor){
    	return (List<Document>) (Object)
    			UtilitiesHolder.SESSION_FACTORY_HANDLER.selectWithLimit(
    					"FROM Document doc " + 
    					"WHERE doc.doctor = ?",
    					ConsultationsServlet.RESULT_LIMIT, doctor
    			);
    }
    
    public List<Document> getSpecifiedDocuments(Doctor doctor, String documentName){
    	if(documentName == null || documentName.replaceAll(" ", "").equals(""))
    		return this.getAllDocuments(doctor);
    	
    	String hqlRequest = "FROM Document doc " +
    			"WHERE ((doc.doctor = ?) and ";
    	    	
    	String[] documentNameParams = this.tokenMePlease(documentName, " ");    	
    	    	
    	Object[] effectiveParams = new Object[1 + 2 * documentNameParams.length];
		 		 effectiveParams[0] = doctor;
    	
    	for(int i = 0; i < documentNameParams.length; i++){
            hqlRequest += "(doc.documentName like ? or doc.documentDescription like ?) and ";
            
            effectiveParams[2 * i + 1] = "%" + documentNameParams[i] + "%";
            effectiveParams[2 * i + 2] = effectiveParams[2 * i + 1];
        }
    	
    	hqlRequest = hqlRequest.substring(0, hqlRequest.length() - 5) + ")";
    	
    	List<Document> result = (List<Document>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.selectWithLimit(
                hqlRequest,
                DocumentsServlet.RESULT_LIMIT,
                effectiveParams
        );
    	
        return result;
    }
    
    public Document getDocumentById(UUID documentId){
    	List<Document> result = (List<Document>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Document doc " + 
    			"WHERE doc.documentFilePath = ?",
    			documentId
    	);
    	
    	if(result.size() == 0)
    		return null;
    	
    	return result.get(0);
    }
    
    public Media getMediaById(UUID mediaId) {
    	List<Media> result = (List<Media>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Media med " + 
    			"WHERE med.mediaFilePath = ?",
    			mediaId
    	);
    	
    	if(result.size() == 0)
    		return null;
    	
    	return result.get(0);
	}
    
  //--------------------------------Secretary---------------------------------------
    public List<Secretary> getAllSecretaries(Doctor doctor){
    	return (List<Secretary>) (Object)
    			UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    					"FROM Secretary sec " + 
    					"WHERE sec.doctor = ?",
    					 doctor
    			);
    }
    
    // ----------------------------- Some craps --------------------------------------
    private String[] tokenMePlease(String something, String separators){
        List<String> results = new ArrayList<String>();
        
        StringTokenizer stringTokenizer = new StringTokenizer(something, separators);
        
        while(stringTokenizer.hasMoreElements())
            results.add((String) stringTokenizer.nextElement());
        
        return results.toArray(new String[results.size()]);
    }
}
