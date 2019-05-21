package Utilities.BackgroundProcesses;

import java.util.Timer;
import java.util.TimerTask;
import Utilities.UtilitiesHolder;

public class DataTranslator {
	public static int PERIOD = 7 * 24 * 3600 * 1000;
	
	public DataTranslator() {
		Timer timer = new Timer();
			  timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					UtilitiesHolder.MEDECINES_FILE_TRANSLATER.convertMedecinesFromFileToDatabase();
					UtilitiesHolder.SPECIALITIES_PARSER.parseSpecialities();
					UtilitiesHolder.SPECIALITIES_PARSER.parseDiseases();
					UtilitiesHolder.SPECIALITIES_PARSER.parseAnalysis();
					
					System.out.println("Finished Refreshing");
				}
			}, 10_000 , PERIOD);
	}
}
