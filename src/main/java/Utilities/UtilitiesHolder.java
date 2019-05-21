package Utilities;

import Utilities.BackgroundProcesses.DataTranslator;
import Utilities.DataHandlers.SessionFactoryHandler;
import Utilities.DataHandlers.ConversionDataHandler.BeanToXmlParser;
import Utilities.DataHandlers.InputDataHandlers.FilesUploader;
import Utilities.DataHandlers.InputDataHandlers.TextFileParser;
import Utilities.DataHandlers.InputDataHandlers.UpdatesHandler;
import Utilities.DataHandlers.InputDataHandlers.XlsxFileParser;
import Utilities.DataHandlers.OutputDataHandlers.SearchesHandler;
import Utilities.SecurityManagers.SecurityManager;

public abstract class UtilitiesHolder {
    public static SessionFactoryHandler SESSION_FACTORY_HANDLER = null;
    public static XlsxFileParser MEDECINES_FILE_TRANSLATER = null;
    public static BeanToXmlParser BEAN_TO_XML_PARSER = null;
    public static TextFileParser SPECIALITIES_PARSER = null;
    public static SearchesHandler SEARCHES_HANDLER = null;
    public static UpdatesHandler UPDATES_HANDLER = null;
    public static FilesUploader FILES_UPLOADER = null;
    public static SecurityManager AUTHENTICATION_SECURITY_MANAGER = null;
    public static DataTranslator DATA_TRANSLATOR = null;
    
    public static String JSPS_FOLDER = "/WEB-INF/Jsps/";
    public static String NOT_AUTHENTICATED_USER_JSP = "index.jsp";
    public static String APPLICATION_PATH = null;
    public static String ENCODING_POST_RESPONSE = "UTF-8";
}
