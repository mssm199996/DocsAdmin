package DomainModel.PatientAdministration;

import java.time.LocalDate;
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


@Entity
@Table(name = "MEDIAS")
@XmlRootElement(name = "media")
public class Media {
    private LocalDate mediaDate;
    private String mediaDescription,
                   mediaFilePath,
                   mediaFileExtension;
    private MediaType mediaType;
    private Patient patient;

    @Basic
    @Column(name = "MEDIA_DATE")
    public LocalDate getMediaDate() {
        return mediaDate;
    }

    public void setMediaDate(LocalDate mediaDate) {
        this.mediaDate = mediaDate;
    }

    @Basic
    @Column(name = "MEDIA_DESCRIPTION")
    public String getMediaDescription() {
        return mediaDescription;
    }

    public void setMediaDescription(String mediaDescription) {
        this.mediaDescription = mediaDescription;
    }

    @Id
    @Column(name = "MEDIA_FILE_PATH")
    @GeneratedValue(generator = "mediaIdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "mediaIdGenerator", strategy = "uuid2")
    @XmlAttribute
    public String getMediaFilePath() {
        return mediaFilePath;
    }

    public void setMediaFilePath(String mediaFilePath) {
        this.mediaFilePath = mediaFilePath;
    }
    
    @Basic
    @Column(name = "MEDIA_FILE_EXTENSION")
    @XmlAttribute
    public String getMediaFileExtension() {
		return mediaFileExtension;
	}

	public void setMediaFileExtension(String mediaFileExtension) {
		this.mediaFileExtension = mediaFileExtension;
	}

	@Basic
    @Column(name = "MEDIA_TYPE")
    public MediaType getMediaType(){
        return this.mediaType;
    }
    
    public void setMediaType(MediaType mediaType){
        this.mediaType = mediaType;
    }

    @Basic
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PATIENT_ID")
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Media)
            return this.getMediaFilePath().equals(((Media) o).getMediaFilePath());
        return false;
    }
    
    
    public static enum MediaType {
        RADIO,
        VIDEO
    }
}
