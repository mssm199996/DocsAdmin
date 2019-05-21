package DomainModel.PatientAdministration;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;


@Entity
@XmlRootElement
@Table(name = "ANALYSIS_TYPE")
public class AnalysisType implements Serializable{
    private int analysisTypeId;
    private String analysisTypeName;

    @Id
    @Column(name = "ANALYSIS_TYPE_ID")
    @GeneratedValue(generator = "analysisTypeGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "analysisTypeGenerator", strategy = "increment")
    @XmlAttribute
    public int getAnalysisTypeId() {
        return analysisTypeId;
    }

    public void setAnalysisTypeId(int analysisTypeId) {
        this.analysisTypeId = analysisTypeId;
    }

    @Basic
    @Column(name = "ANALYSIS_TYPE_NAME")
    public String getAnalysisTypeName() {
        return analysisTypeName;
    }

    public void setAnalysisTypeName(String analysisTypeName) {
        this.analysisTypeName = analysisTypeName;
    }
}
