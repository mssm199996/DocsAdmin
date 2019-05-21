package DomainModel.DocumentAdministration;

import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;
import DomainModel.UserAdministration.Doctor;

@Entity
@Table(name = "DOCUMENTS")
@XmlRootElement
public class Document {
    private String documentName;
    private UUID documentFilePath;
    private String documentDescription;
    private String documentExtension;
    private LocalDate documentUploadDate;
    private Doctor doctor;

    
    @Id
    @Column(name = "DOCUMENT_FILE_PATH")
    @GeneratedValue(generator = "documentIdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "documentIdGenerator", strategy = "uuid2")
    @XmlAttribute(name = "documentId")
    public UUID getDocumentFilePath() {
        return documentFilePath;
    }

    public void setDocumentFilePath(UUID documentFilePath) {
        this.documentFilePath = documentFilePath;
    }
    
    @Basic
    @Column(name = "DOCUMENT_NAME")
    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    @Basic
    @Column(name = "DOCUMENT_DESCRIPTION")
    public String getDocumentDescription() {
    	return this.documentDescription;
    }
    
    public void setDocumentDescription(String documentDescription) {
    	this.documentDescription = documentDescription;
    }
    
    @Basic
    @Column(name = "DOCUMENT_DATE")
    public LocalDate getDocumentUploadDate() {
    	return this.documentUploadDate;
    }
    
    public void setDocumentUploadDate(LocalDate documentUploadDate) {
    	this.documentUploadDate = documentUploadDate;
    }
    
    @Basic
    @Column(name = "DOCUMENT_EXTENSION")
    @XmlAttribute
    public String getDocumentExtension() {
    	return this.documentExtension;
    }
    
    public void setDocumentExtension(String documentExtension) {
    	this.documentExtension = documentExtension;
    }
    

    @Basic
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DOCTOR_ID")
    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }    
}
