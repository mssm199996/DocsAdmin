package Utilities.DataHandlers.InputDataHandlers;

import DomainModel.MedecineAdministration.Medecine;
import Utilities.UtilitiesHolder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class XlsxFileParser {
    public static String XLSX_FILE_PATH = "C:\\DocsAdmin\\NOMENCLATURE NATIONALE.xlsx";
    
    public static String IS_REDEEMABLE = "OUI";
    public static int DCI_INDEX = 3,
                      BRAND_INDEX = 4,
                      FORM_INDEX = 5,
                      DOSAGE_INDEX = 6,
                      REDEEMABILITY_INDEX = 19,
                      START_OFFSET = 8,
                      SHEET_INDEX = 0,
                      MAX_EFFECTIVE_CELLS = 20;
    
    private static final boolean ENABLE_UPDATING_MEDECINES = false; 
    
    public XlsxFileParser(){
        if(ENABLE_UPDATING_MEDECINES)
            this.convertMedecinesFromFileToDatabase();
    }
    
    public void convertMedecinesFromFileToDatabase(){
        try {
            long t0 = System.currentTimeMillis();
            
            UtilitiesHolder.SESSION_FACTORY_HANDLER.executeHql("DELETE Medecine");
             
            XSSFWorkbook book = new XSSFWorkbook(new FileInputStream(new File(XlsxFileParser.XLSX_FILE_PATH)));
            
            XSSFSheet sheet = book.getSheetAt(SHEET_INDEX);
            
            Iterator<Row> itr = sheet.iterator(); // Iterating over Excel file in Java
            
            for(int i = 0; i < START_OFFSET; i++)
                itr.next();
            
            LinkedList<Medecine> medecinesToSave = new LinkedList();
            
            while (itr.hasNext()) {
                Row row = itr.next(); // Iterating over each column of Excel file 
                
                if(row.getPhysicalNumberOfCells() < MAX_EFFECTIVE_CELLS) // Si on a dépassé le nombre de mediaments
                    break;
                     
                Medecine medecine = new Medecine();
                         medecine.setMedecineDci(row.getCell(DCI_INDEX).getStringCellValue());
                         medecine.setMedecineMark(row.getCell(BRAND_INDEX).getStringCellValue());
                         medecine.setMedecineForm(row.getCell(FORM_INDEX).getStringCellValue());
                         medecine.setMedecineRedeemability(row.getCell(REDEEMABILITY_INDEX).getStringCellValue().equals(IS_REDEEMABLE));
                
                if(row.getCell(DOSAGE_INDEX).getCellTypeEnum() == CellType.NUMERIC)
                    medecine.setMedecineDosage(row.getCell(DOSAGE_INDEX).getNumericCellValue() + "");
                else medecine.setMedecineDosage(row.getCell(DOSAGE_INDEX).getStringCellValue());
                
                medecinesToSave.add(medecine);
            }
            
            UtilitiesHolder.SESSION_FACTORY_HANDLER.insert(medecinesToSave.toArray(new Medecine[medecinesToSave.size()]));
        
            System.out.println("Needed time to save all the medecines: " + (System.currentTimeMillis() - t0));
            
            this.removeDuplicatedMedecines();
        }
        catch(IOException exp){
            exp.printStackTrace();
        }
    }
    
    private void removeDuplicatedMedecines(){
        UtilitiesHolder.SESSION_FACTORY_HANDLER.executeHql(
            "DELETE FROM Medecine med1 " +
            "WHERE EXISTS ("  +
               "FROM Medecine med2 " +
               "WHERE(" +
                    "med1.medecineId < med2.medecineId AND " +
                    "REPLACE(med1.medecineDci, ' ', '') = REPLACE(med2.medecineDci, ' ', '') AND " +
                    "REPLACE(med1.medecineMark, ' ', '') = REPLACE(med2.medecineMark, ' ', '') AND " +
                    "REPLACE(med1.medecineForm, ' ', '') = REPLACE(med2.medecineForm, ' ', '') AND " +
                    "REPLACE(med1.medecineDosage, ' ', '') = REPLACE(med2.medecineDosage, ' ', '') AND " +
                    "med1.medecineRedeemability = med2.medecineRedeemability" +
                ")" +
            ")"
        );
    }
}
