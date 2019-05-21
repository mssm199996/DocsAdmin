package DomainModel.PatientAdministration;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "ANALYSIS")
@XmlRootElement(name = "analysis")
public class Analysis implements Serializable {
    private LocalDate analysisDate;
    private String analysisResult;
    private AnalysisType analysisType;
    private Patient patient;

    @Id
    @Column(name = "ANALYSIS_DATE")
    public LocalDate getAnalysisDate() {
        return analysisDate;
    }

    public void setAnalysisDate(LocalDate analysisDate) {
        this.analysisDate = analysisDate;
    }

    @Basic
    @Column(name = "ANALYSIS_RESULT")
    public String getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(String analysisResult) {
        this.analysisResult = analysisResult;
    }

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ANALYSIS_TYPE_ID")
    public AnalysisType getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(AnalysisType analysisType) {
        this.analysisType = analysisType;
    }

    @Id
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
        if(o instanceof Analysis)
            return this.getAnalysisDate().equals(((Analysis) o).getAnalysisDate()) &&
                   this.getAnalysisType().equals(((Analysis) o).getAnalysisType()) &&
                   this.getPatient().equals(((Analysis) o).getPatient());
        return false;
    }
}
