package Utilities.Listeners;

import Utilities.DataHandlers.ConversionDataHandler.BeanToXmlParser;
import Utilities.DataHandlers.SessionFactoryHandler;
import Utilities.DataHandlers.InputDataHandlers.FilesUploader;
import Utilities.DataHandlers.InputDataHandlers.TextFileParser;
import Utilities.DataHandlers.InputDataHandlers.UpdatesHandler;
import Utilities.DataHandlers.InputDataHandlers.XlsxFileParser;
import Utilities.DataHandlers.OutputDataHandlers.SearchesHandler;
import Utilities.SecurityManagers.SecurityManager;
import Utilities.UtilitiesHolder;
import Utilities.BackgroundProcesses.DataTranslator;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class OnStartApacheListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	UtilitiesHolder.APPLICATION_PATH = sce.getServletContext().getRealPath("/");
        UtilitiesHolder.SESSION_FACTORY_HANDLER = new SessionFactoryHandler("hibernate.cfg.xml");
        UtilitiesHolder.MEDECINES_FILE_TRANSLATER = new XlsxFileParser();
        UtilitiesHolder.SPECIALITIES_PARSER = new TextFileParser();
        UtilitiesHolder.BEAN_TO_XML_PARSER = new BeanToXmlParser();
        UtilitiesHolder.SEARCHES_HANDLER = new SearchesHandler();
        UtilitiesHolder.UPDATES_HANDLER = new UpdatesHandler();
        UtilitiesHolder.AUTHENTICATION_SECURITY_MANAGER = new SecurityManager();
        UtilitiesHolder.FILES_UPLOADER = new FilesUploader();
        UtilitiesHolder.DATA_TRANSLATOR = new DataTranslator();
        
        System.out.println("Apache is starting... !");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Apache Tomcat turned off");
    }    
}
