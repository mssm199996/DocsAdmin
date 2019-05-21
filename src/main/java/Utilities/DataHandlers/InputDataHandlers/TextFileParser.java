package Utilities.DataHandlers.InputDataHandlers;

import DomainModel.PatientAdministration.AnalysisType;
import DomainModel.PatientAdministration.Disease;
import DomainModel.UserAdministration.Speciality;
import Utilities.UtilitiesHolder;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TextFileParser {
    public static String SPECIALITIES_TXT_FILE_PATH = "C:\\DocsAdmin\\Specialities.txt";
    public static String ANALYSYS_TXT_FILE_PATH = "C:\\DocsAdmin\\Analyses.txt";
    public static String DISEASES_TXT_FILE_PATH = "C:\\DocsAdmin\\Maladies.txt";
    public static boolean ENABLE_UPDATING_SPECIALITIES = false;
    public static boolean ENABLE_UPDATING_ANALYSIS_TYPES = false;
    public static boolean ENABLE_UPDATING_DISEASES = false;
    private static String ENCODING_CHARSET = "UTF-8";
    
    public TextFileParser(){
        if(ENABLE_UPDATING_SPECIALITIES)
            this.parseSpecialities();
        if(ENABLE_UPDATING_ANALYSIS_TYPES)
            this.parseAnalysis();
        if(ENABLE_UPDATING_DISEASES)
        	this.parseDiseases();
    }
    
    public void parseSpecialities(){
        try{
            UtilitiesHolder.SESSION_FACTORY_HANDLER.executeHql("DELETE Speciality");
            
            Path path = Paths.get(SPECIALITIES_TXT_FILE_PATH);
            
            List<String> specialitiesLines = Files.readAllLines(path, Charset.forName(ENCODING_CHARSET));
            Speciality[] specialities = new Speciality[specialitiesLines.size()];
            
            int i = 0;
            
            for(String specialityLine: specialitiesLines){
                Speciality speciality = new Speciality();
                           speciality.setSpecialityName(specialityLine);
                           
                specialities[i++] = speciality;
            }
            
            UtilitiesHolder.SESSION_FACTORY_HANDLER.insert(specialities);
        }
        catch(IOException exp){
            exp.printStackTrace();
        }
    }
    
    public void parseAnalysis(){
        try{
            UtilitiesHolder.SESSION_FACTORY_HANDLER.executeHql("DELETE AnalysisType");
            
            Path path = Paths.get(ANALYSYS_TXT_FILE_PATH);
            
            List<String> analysisTypeLines = Files.readAllLines(path, Charset.forName(ENCODING_CHARSET));
            AnalysisType[] analysisTypes = new AnalysisType[analysisTypeLines.size()];
            
            int i = 0;
            
            for(String analysisTypeLine: analysisTypeLines){
                AnalysisType analysisType = new AnalysisType();
                             analysisType.setAnalysisTypeName(analysisTypeLine);
                           
                analysisTypes[i++] = analysisType;
            }
             
            UtilitiesHolder.SESSION_FACTORY_HANDLER.insert(analysisTypes);
        }
        catch(IOException exp){
            exp.printStackTrace();
        }
    }
    
    public void parseDiseases() {
    	try{
            UtilitiesHolder.SESSION_FACTORY_HANDLER.executeHql("DELETE Disease");
            
            Path path = Paths.get(DISEASES_TXT_FILE_PATH);
            
            List<String> diseasesLines = Files.readAllLines(path, Charset.forName(ENCODING_CHARSET));
            Disease[] diseases = new Disease[diseasesLines.size()];
            
            int i = 0;
            
            for(String diseaseName: diseasesLines){
                Disease disease = new Disease();
                        disease.setDiseaseName(diseaseName);
                           
                diseases[i++] = disease;
            }
             
            UtilitiesHolder.SESSION_FACTORY_HANDLER.insert(diseases);
        }
        catch(IOException exp){
            exp.printStackTrace();
        }
    }
}
