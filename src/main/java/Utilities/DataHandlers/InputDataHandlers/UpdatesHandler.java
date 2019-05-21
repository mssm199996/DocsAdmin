package Utilities.DataHandlers.InputDataHandlers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
import DomainModel.PatientAdministration.Antecedent.AntecedentType;
import DomainModel.PatientAdministration.Disease;
import DomainModel.PatientAdministration.Media;
import DomainModel.PatientAdministration.Media.MediaType;
import DomainModel.PatientAdministration.Patient;
import DomainModel.UserAdministration.Doctor;
import DomainModel.UserAdministration.Secretary;
import Utilities.UtilitiesHolder;

public class UpdatesHandler {
	
	// -------------------------- Patients ------------------------
	public boolean deletePatient(UUID patientId) {
		List<Patient> result = (List<Patient>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				"FROM Patient pat " +
				"WHERE (pat.patientId = ?)",
				patientId
		);
		if(result.size() == 0)
			return false;
		UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(result.get(0));
		return true;
	}
	
	public boolean insertNewPatient(Doctor doc,String fname,String lName,String ddn,String Gender) {
		Patient patient = new Patient();
				patient.setPatientFirstName(fname);
				patient.setPatientLastName(lName);
				patient.setPatientBirthday(LocalDate.parse(ddn,DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				patient.setPatientGender(Gender.equals("2")?DomainModel.UserAdministration.Doctor.Gender.FEMALE:DomainModel.UserAdministration.Doctor.Gender.MALE);
		
		List<Patient> result = (List<Patient>) (Object) 
				UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
						"FROM Patient pat " +
						"WHERE pat.patientFirstName = ? and pat.patientLastName = ?  and pat.patientBirthday = ? and pat.patientGender = ?",
						patient.getPatientFirstName(),patient.getPatientLastName(),patient.getPatientBirthday(),patient.getPatientGender()
						
					);
		if(!result.isEmpty()) return false;
		
		List<Doctor> result2 = (List<Doctor>) (Object) 
				UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
						"FROM Doctor doc LEFT OUTER JOIN FETCH doc.patients " +
						"WHERE doc.personId = ? ",
						doc.getPersonId()
						
					);
		if(result2.isEmpty()) return false;
		
	    result2.get(0).addPatient(patient);
	    UtilitiesHolder.SESSION_FACTORY_HANDLER.update(result2.get(0));
	  
	    return true;
	    
	}
	
	public boolean insertNewDisease(UUID patient , int maladieId){
		List<Patient> result = (List<Patient>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Patient pat  " +
    			"LEFT OUTER JOIN FETCH pat.diseases " +
    			"WHERE pat.patientId = ?", 
    			patient
    	);
		
		if(result.size() == 0)
			return false;
		
		List<Disease> result2 = (List<Disease>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Disease d  " +
    			"WHERE d.diseaseId = ?", 
    			maladieId
    	);
		
		if(result2.size() == 0) return false;
		
		Patient p  = result.get(0);
			    p.addDisease(result2.get(0));
					
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(p);
					    
		return true;
		
	}
	
	public boolean insertNewAnalysis(UUID patient,String analyseName ,String date,String resu) {
		List<Patient> result = (List<Patient>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Patient pat  " +
    			"LEFT OUTER JOIN FETCH pat.analysies " +
    			"WHERE pat.patientId = ?", 
    			patient
    	);
		
		if(result.size() == 0)
			return false;
		
		List<AnalysisType> result2 = (List<AnalysisType>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM AnalysisType analyse " +
    			"WHERE analyse.analysisTypeName = ?", 
    			analyseName
    	);
		if(result2.size() == 0) return false;
		
		Analysis analyse = new Analysis();
				 analyse.setAnalysisDate(LocalDate.parse(date,DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				 analyse.setAnalysisResult(resu);
				 analyse.setAnalysisType(result2.get(0));
		
		result.get(0).addAnalysis(analyse);
					
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(result.get(0));
					    
		return true;
	}

	public boolean insertNewAntecedent(UUID patientId,int antecedentType,String antecedentStartDate,String antecedentRecoveryDate,String antecedentDescription) {
		List<Patient> result = (List<Patient>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Patient pat  " +
    			"LEFT OUTER JOIN FETCH pat.antecedents " +
    			"WHERE pat.patientId = ?", 
    			patientId
    	);
		
		if(result.size() == 0)
			return false;
		
		Antecedent antecedent = new Antecedent();
				   antecedent.setAntecedentDescription(antecedentDescription);
				   antecedent.setAntecedentType(Antecedent.AntecedentType.values()[antecedentType]);
				   antecedent.setAntecedentStartDate(LocalDate.parse(antecedentStartDate,DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				   antecedent.setAntecedentRecoveryDate(LocalDate.parse(antecedentRecoveryDate,DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				 
		result.get(0).addAntecedent(antecedent);
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(result.get(0));
		return true;
		
	}
	
	
	public boolean deleteDisease(UUID patientId, String diseaseId) {
		List<Patient> result1 = (List<Patient>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				"FROM Patient  pat LEFT OUTER JOIN FETCH pat.diseases " +
				"WHERE pat.patientId = ?",
				patientId
		);
		
		if(result1.size() == 0)
			return false;
		
		List<Disease> result2 =(List<Disease>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				"FROM Disease dis "+
				"WHERE dis.diseaseId = ?",
				Integer.parseInt(diseaseId)
		);
		
		if(result2.size() == 0)
			return false;
		
		Patient p = result1.get(0);
				p.removeDisease(result2.get(0));
					
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(p);
		
		return true;
	}
	
	public boolean deleteAnalysis(UUID patientId,String analysisName) {
		List<Patient> result1 = (List<Patient>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				"FROM Patient  pat LEFT OUTER JOIN FETCH pat.analysies " +
				"WHERE pat.patientId = ?",
				patientId
		);
		
		if(result1.size() == 0)
			return false;
		
		List<Analysis> result2 = (List<Analysis>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				"FROM Analysis analyse " +
			    "WHERE analyse.patient.patientId = ? and " +
			    "analyse.analysisType.analysisTypeName = ?",
			    patientId,
			    analysisName);
		
		if(result2.size() == 0)
			return false;
		
		UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(result2.get(0));
		return true ;	    
		
	}
	public boolean deleteAntecedent(UUID patientId, String antecedentStartDate, String antecedentRecoveryDate, int antecedentType) {
		List<Antecedent> result = (List<Antecedent>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				"FROM Antecedent antecedent " +
			    "WHERE antecedent.patient.patientId = ? and "+
			    "antecedent.antecedentType = ? and "
			    + "antecedent.antecedentStartDate = ? and "
			    + "antecedent.antecedentRecoveryDate = ?",
			    patientId,
			    Antecedent.AntecedentType.values()[antecedentType],
			    LocalDate.parse(antecedentStartDate,DateTimeFormatter.ofPattern("yyyy-MM-dd")),
			    LocalDate.parse(antecedentRecoveryDate,DateTimeFormatter.ofPattern("yyyy-MM-dd")) 
		);
		
		if(result.size() == 0)
			return false;
		
		UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(result.get(0));
		return true;
	}
	
	public boolean updateAntecedent(UUID patientId, LocalDate antecedentStartDate, LocalDate antecedentRecoveryDate, AntecedentType antecedentType, String antecedentNewDescription) {
		List<Antecedent> result = (List<Antecedent>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				"FROM Antecedent antecedent " +
			    "WHERE antecedent.patient.patientId = ? and "+
			    "antecedent.antecedentType = ? and "
			    + "antecedent.antecedentStartDate = ? and "
			    + "antecedent.antecedentRecoveryDate = ?",
			    patientId,
			    antecedentType,
			    antecedentStartDate,
			    antecedentRecoveryDate
		);
		
		if(result.size() == 0)
			return false;
		
		Antecedent antecedent = result.get(0);
				   antecedent.setAntecedentDescription(antecedentNewDescription);
				   
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(antecedent);
		
		return true;
	}
	
	public boolean updatePatientPersonalInfo(UUID patientId,String patientAdr,String patientProfession,String patientPhoneNumber, String patientEmail,String patientInsuranceCode) {
		List<Patient> result1 = (List<Patient>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				"FROM Patient  pat LEFT OUTER JOIN FETCH pat.antecedents " +
				"WHERE pat.patientId = ?",
				patientId
		);
		
		if(result1.size() == 0)
			return false;
		result1.get(0).setPatientProfession(patientProfession);
		result1.get(0).setPatientPhoneNumber(patientPhoneNumber);
		result1.get(0).setPatientAddress(patientAdr);
		result1.get(0).setPatientInsuranceCard(patientInsuranceCode);
		result1.get(0).setPatientEmail(patientEmail);
		
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(result1.get(0));
		return true;
	}
	
	// ---------------------------- Consultations ------------------------------
	public boolean insertNewConsultation(UUID uuid, double price) {
		List<Patient> result = (List<Patient>) (Object) 
				UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
					"FROM Patient pat " +
					"LEFT OUTER JOIN FETCH pat.consultations " +
					"WHERE pat.patientId = ?",
					uuid
				);
		
		if(result.size() == 0)
			return false;
		
		Consultation consultation = new Consultation();
					 consultation.setConsultationAnamnesis("");
					 consultation.setConsultationDate(LocalDate.now());
					 consultation.setConsultationDiag("");
					 consultation.setConsultationPhex("");
					 consultation.setConsultationPrice(price);

		Patient patient = result.get(0);
				patient.addConsultation(consultation);
				
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(patient);
		
		return true;
	}
	
	public boolean deleteConsultation(UUID consultationId) {
		List<Consultation> result = (List<Consultation>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				"FROM Consultation con " +
				"WHERE (con.consultationId = ?)",
				consultationId
		);
		
		if(result.size() == 0)
			return false;
							
		UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(result.get(0));
		
		return true;
	}
	
	public boolean insertNewObservation(UUID consultationId, String obsInformation) {
		List<Consultation> result = (List<Consultation>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Consultation con " +
    			"LEFT OUTER JOIN FETCH con.observations " +
    			"WHERE con.consultationId = ?", 
    			consultationId
    	);
		
		if(result.size() == 0)
			return false;
		
		Observation observation = new Observation();
					observation.setDescription(obsInformation);
		
		Consultation consultation = result.get(0);
					 consultation.addObservation(observation);
					
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(consultation);
					    
		return true;
	}
	
	public boolean updateHm(UUID consultationId, String hmContent) {
		List<Consultation> result = (List<Consultation>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Consultation con " +
    			"WHERE con.consultationId = ?", 
    			consultationId
    	);
		
		if(result.size() == 0)
			return false;
		
		Consultation consultation = result.get(0);
					 consultation.setConsultationAnamnesis(hmContent);
		
	    UtilitiesHolder.SESSION_FACTORY_HANDLER.update(consultation);
		
		return true;
	}
	
	public boolean updateEc(UUID consultationId, String ecContent) {
		List<Consultation> result = (List<Consultation>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Consultation con " +
    			"WHERE con.consultationId = ?", 
    			consultationId
    	);
		
		if(result.size() == 0)
			return false;
		
		Consultation consultation = result.get(0);
					 consultation.setConsultationPhex(ecContent);
		
	    UtilitiesHolder.SESSION_FACTORY_HANDLER.update(consultation);
	    
		return true;
	}
	
	public boolean updateDia(UUID consultationId, String diaContent) {
		List<Consultation> result = (List<Consultation>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Consultation con " +
    			"WHERE con.consultationId = ?", 
    			consultationId
    	);
		
		if(result.size() == 0)
			return false;
		
		Consultation consultation = result.get(0);
					 consultation.setConsultationDiag(diaContent);
		
	    UtilitiesHolder.SESSION_FACTORY_HANDLER.update(consultation);
	    
		return true;
	}
	
	public boolean updatePrice(UUID consultationId, double newPrice) {
		List<Consultation> result = (List<Consultation>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Consultation con " +
    			"WHERE con.consultationId = ?", 
    			consultationId
    	);
		
		Consultation consultation = result.get(0);
		
		if(consultation != null)
			consultation.setConsultationPrice(newPrice);
		else return false;
		
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(consultation);
		
		return true;
	}
	
	public boolean deleteObservation(UUID consultationId, String obsInformation) {
		List<Observation> result = (List<Observation>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				"FROM Observation obs " +
				"WHERE (obs.consultation.consultationId = ? and obs.description = ?)",
				consultationId,
				obsInformation
		);
		
		if(result.size() == 0)
			return false;
		
		Observation observation = result.get(0);
					
		UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(observation);
		
		return true;
	}
	
	public boolean insertNewPrescription(UUID consultationId, int medecineId) {
		List<Consultation> result = (List<Consultation>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Consultation con " +
    			"LEFT OUTER JOIN FETCH con.prescriptions " +
    			"WHERE con.consultationId = ?", 
    			consultationId
    	);
		
		if(result.size() == 0)
			return false;
		
		Medecine medecine = (Medecine) (((List<Medecine>) 
				(Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select("FROM Medecine WHERE medecineId = ?", medecineId)).get(0));
		
		Consultation consultation = result.get(0);
					 consultation.addPrescription(medecine);;
					
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(consultation);
					    
		return true;
	}
	
	public boolean deletePrescription(UUID consultationId, int medecineId) {
		List<Consultation> consultation = (List<Consultation>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				"FROM Consultation con " +
				"LEFT OUTER JOIN FETCH con.prescriptions " +
				"WHERE (con.consultationId = ?)",
				consultationId
		);
		
		if(consultation.size() == 0)
			return false;

		List<Medecine> medecine = (List<Medecine>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				"FROM Medecine med " +
				"WHERE (med.medecineId = ?)",
				medecineId
		);
		
		if(medecine.size() == 0)
			return false;
		
		consultation.get(0).removePrescription(medecine.get(0));
		
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(consultation.get(0));
		
		return true;
	}
	
	// -------------------------- Appointments -------------------------
	
	public boolean insertNewReason(UUID appointmentId, String reasonInformation) {
		List<Appointment> result = (List<Appointment>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Appointment app " +
    			"LEFT OUTER JOIN FETCH app.reasons " +
    			"WHERE app.appointmentId = ?", 
    			appointmentId
    	);
		
		if(result.size() == 0)
			return false;
		
		Reason reason = new Reason();
			   reason.setDescription(reasonInformation);
		
		Appointment appointment = result.get(0);
					appointment.addReason(reason);
					
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(appointment);
					    
		return true;
	}
	
	public boolean insertNewParaclinic(UUID appointmentId, String paraclinicInformation) {
		List<Appointment> result = (List<Appointment>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
    			"FROM Appointment app " +
    			"LEFT OUTER JOIN FETCH app.paraclinics " +
    			"WHERE app.appointmentId = ?", 
    			appointmentId
    	);
		
		if(result.size() == 0)
			return false;
		
		Paraclinic paraclinic = new Paraclinic();
				   paraclinic.setDescription(paraclinicInformation);
		
		Appointment appointment = result.get(0);
					appointment.addParaclinic(paraclinic);
					
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(appointment);
					    
		return true;
	}
	
	public boolean insertNewAppointment(UUID uuid, LocalDate appointmentDate, LocalTime appointmentTime) {
		List<Patient> result = (List<Patient>) (Object) 
				UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
					"FROM Patient pat " +
					"LEFT OUTER JOIN FETCH pat.appointments " +
					"WHERE pat.patientId = ?",
					uuid
				);
		
		if(result.size() == 0)
			return false;
		
		Appointment appointment = new Appointment();
					appointment.setAppointmentTime(appointmentTime);
					appointment.setAppointmentDate(appointmentDate);					

		Patient patient = result.get(0);
				patient.addAppointment(appointment);
				
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(patient);
		
		return true;
	}
	
	public boolean deleteAppointment(UUID appointmentId) {
		List<Appointment> result = (List<Appointment>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				"FROM Appointment app " +
				"WHERE (app.appointmentId = ?)",
				appointmentId
		);
		
		if(result.size() == 0)
			return false;
							
		UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(result.get(0));
		
		return true;
	}
	
	public boolean deleteParaclinic(UUID appointmentId, String paraclinicInformation) {
		List<Paraclinic> result = (List<Paraclinic>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				"FROM Paraclinic par " +
				"WHERE (par.appointment.appointmentId = ? and par.description = ?)",
				appointmentId,
				paraclinicInformation
		);
		
		if(result.size() == 0)
			return false;
		
		Paraclinic paraclinic = result.get(0);
					
		UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(paraclinic);
		
		return true;
	}
	
	public boolean deleteReason(UUID appointmentId, String reasonInformation) {
		List<Reason> result = (List<Reason>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				"FROM Reason rea " +
				"WHERE (rea.appointment.appointmentId = ? and rea.description = ?)",
				appointmentId,
				reasonInformation
		);
		
		if(result.size() == 0)
			return false;
		
		Reason reason = result.get(0);
					
		UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(reason);
		
		return true;
	}
	
	// -------------------------- Documents -------------------------
	
	public Media uploadRadio(UUID patientId, LocalDate radioDate, String radioDescription, String radioExtension) {
		List<Patient> targetedPatient = (List<Patient>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
			"FROM Patient pat LEFT OUTER JOIN FETCH pat.medias " +
			"WHERE pat.patientId = ?", patientId
		);
		
		if(targetedPatient == null)
			return null;
		
		Media media = new Media();
			  media.setMediaDate(radioDate);
			  media.setMediaDescription(radioDescription);
			  media.setMediaFileExtension(radioExtension);
			  media.setMediaType(MediaType.RADIO);
			  
		targetedPatient.get(0).addMedia(media);
		
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(targetedPatient.get(0));
		
		return media;
	}
	
	public boolean deleteMedia(UUID mediaId) {
		List<Media> result = (List<Media>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				"FROM Media med " +
				"WHERE (med.mediaFilePath = ?)",
				mediaId.toString()
		);
		
		if(result.size() == 0)
			return false;
		
		Media media = result.get(0);
					
		if(UtilitiesHolder.FILES_UPLOADER.deleteMedia(media)) {
			UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(media);
			
			return true;
		}
		
		return false;
	}
	
	
	public Document uploadDocument(Doctor doctor, String documentName, String documentDescription, String documentExtension) {
    	Document document = new Document();
    			 document.setDocumentDescription(documentDescription);
    			 document.setDocumentName(documentName);
    			 document.setDocumentUploadDate(LocalDate.now());
    			 document.setDocumentExtension(documentExtension);
    			 document.setDoctor(doctor);
    	
    	UtilitiesHolder.SESSION_FACTORY_HANDLER.insert(document);
    	
    	return document;
    }

	public boolean deleteDocument(UUID documentId) {
		List<Document> result = (List<Document>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				"FROM Document doc " +
				"WHERE (doc.documentFilePath = ?)",
				documentId
		);
		
		if(result.size() == 0)
			return false;
		
		Document document = result.get(0);
					
		if(UtilitiesHolder.FILES_UPLOADER.deleteDocument(document)) {
			UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(document);
			
			return true;
		}
		
		return false;
	}
	
	// ------------------------------------- Gestion des secrétaires ---------------------------------------
	
	public boolean deleteSecretary(UUID secretaryId) {
		List<Secretary> result = (List<Secretary>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				"FROM Secretary sec " +
				"WHERE (sec.personId = ?)",
				secretaryId
		);
		if(result.size() == 0) return false;
		Secretary secretary = result.get(0);
		UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(secretary);
		return true;
	}
	public boolean cleanSecretaries(UUID doctorID) {
		List<Secretary> result = (List<Secretary>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				"FROM Secretary sec " +
				"WHERE (sec.confirmed = ? and sec.doctor.personId = ?)",
				false,
				doctorID
		);
		
		if(result.size() == 0) 
			return false;
		
		UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(result.toArray(new Secretary[result.size()]));
		
		return true;
	}
	
	public boolean confirmSecretary(UUID secretaryId) {
		List<Secretary> result = (List<Secretary>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				"FROM Secretary sec " +
				"WHERE (sec.personId = ?)",
				secretaryId
		);
		if(result.size() == 0) return false;
		Secretary secretary = result.get(0);
		secretary.setConfirmed(true);
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(secretary);
		return true;
	}
}
