package Utilities.DataHandlers.ConversionDataHandler;

import java.io.StringWriter;
import org.eclipse.persistence.jaxb.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.jaxb.JAXBHelper;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.ObjectGraph;
import DomainModel.AppointmentAdministration.Appointment;
import DomainModel.AppointmentAdministration.Paraclinic;
import DomainModel.AppointmentAdministration.Reason;
import DomainModel.ConsultationAdministration.Consultation;
import DomainModel.ConsultationAdministration.Observation;
import DomainModel.MedecineAdministration.Medecine;
import DomainModel.PatientAdministration.Patient;


public class BeanToXmlParser {
    public static JAXBContext JAXB_CONTEXT = null;
    
    public BeanToXmlParser() {
        try{
            BeanToXmlParser.JAXB_CONTEXT = (JAXBContext) JAXBContext.newInstance(
            		BeansWrapper.class,
                    Medecine.class,
                    Consultation.class,
                    Patient.class,
                    Observation.class,
                    Appointment.class,
                    Reason.class,
                    Paraclinic.class
            );
        }
        catch(JAXBException exp){
            exp.printStackTrace();
        }
    }
    
    public String fromBeanToXml(Object... beans) {
        StringWriter resultHolder = new StringWriter();
        
        try {            
            Marshaller jaxbMarshaller = BeanToXmlParser.JAXB_CONTEXT.createMarshaller();
                       jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);            
                       jaxbMarshaller.marshal(new BeansWrapper(beans), resultHolder);
        } 
        catch (JAXBException e) {
            e.printStackTrace();
        }
        
        return resultHolder.toString();
    } 
    
    public String fromBeanToXml(String[] properties, Object... beans) {
    	StringWriter resultHolder = new StringWriter();
        
    	ObjectGraph outputInfo = JAXBHelper.getJAXBContext(BeanToXmlParser.JAXB_CONTEXT).createObjectGraph(BeansWrapper.class);
    				outputInfo.addSubgraph("beans");
    	
    	for (String propertyName : properties) 
    		outputInfo.addSubgraph(propertyName);

    	// Output XML - Based on Object Graph
        try {
        	Marshaller jaxbMarshaller = BeanToXmlParser.JAXB_CONTEXT.createMarshaller();
			 		   jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			 		   jaxbMarshaller.setProperty(MarshallerProperties.OBJECT_GRAPH, outputInfo);
			 		   jaxbMarshaller.marshal(new BeansWrapper(beans), resultHolder);
        }
        catch(JAXBException e) {
        	e.printStackTrace();
        }
        		   
        return resultHolder.toString();
    }
    
    @XmlRootElement(name = "beanswrapper")
    public static class BeansWrapper{
    	
    	
        private Object[] beans;
    	
    	public BeansWrapper() {}
    	public BeansWrapper(Object ... beans) { this.beans = beans; }
     
    	@XmlAnyElement(lax = true)
        public Object[] getBeans(){
        	return this.beans;
    	}
     
        public void setBeans(Object[] beans) {
            this.beans = beans;
        }
    }
}
