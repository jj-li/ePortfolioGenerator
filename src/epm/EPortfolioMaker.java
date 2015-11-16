/**
 * @author Jia Li
 **/
package epm;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import xml_utilities.InvalidXMLFileFormatException;
import properties_manager.PropertiesManager;
import static epm.LanguagePropertyType.TITLE_WINDOW;
import static epm.StartupConstants.CSS_CLASS_LANGUAGE_BOX;
import static epm.StartupConstants.PATH_DATA;
import static epm.StartupConstants.PATH_ICONS;
import static epm.StartupConstants.PROPERTIES_SCHEMA_FILE_NAME;
import static epm.StartupConstants.STYLE_SHEET_UI;
import static epm.StartupConstants.UI_PROPERTIES_FILE_NAME;
import static epm.StartupConstants.WINDOWS_ICON;
import epm.error.ErrorHandler;
import epm.file.EPortfolioFileManager;
import epm.model.Page;
import epm.view.EPortfolioMakerView;

/**
 * SlideShowMaker is a program for making custom image slideshows. It will allow
 * the user to name their slideshow, select images to use, select captions for
 * the images, and the order of appearance for slides.
 *
 * @author McKilla Gorilla & _____________
 */
public class EPortfolioMaker extends Application {
    // THIS WILL PERFORM SLIDESHOW READING AND WRITING
    EPortfolioFileManager fileManager = new EPortfolioFileManager();

    // THIS HAS THE FULL USER INTERFACE AND ONCE IN EVENT
    // HANDLING MODE, BASICALLY IT BECOMES THE FOCAL
    // POINT, RUNNING THE UI AND EVERYTHING ELSE
    EPortfolioMakerView ui = new EPortfolioMakerView(fileManager);
    
    public static Page CORRUPTED_SLIDE;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // LOAD APP SETTINGS INTO THE GUI AND START IT UP

        boolean success = loadProperties();
        if (success) {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String appTitle = props.getProperty(TITLE_WINDOW);

	    // NOW START THE UI IN EVENT HANDLING MODE
	    ui.startUI(primaryStage, appTitle);
	} // THERE WAS A PROBLEM LOADING THE PROPERTIES FILE
	else {
	    // LET THE ERROR HANDLER PROVIDE THE RESPONSE
	    ErrorHandler errorHandler = ui.getErrorHandler();
	    errorHandler.processError(LanguagePropertyType.ERROR_DATA_FILE_LOADING, "File Loading Problem");
	    System.exit(0);
	}
    }
    
    /**
     * Loads this application's properties file, which has a number of settings
     * for initializing the user interface.
     * 
     * @return true if the properties file was loaded successfully, false otherwise.
     */
    public boolean loadProperties() {
        try {
            // LOAD THE SETTINGS FOR STARTING THE APP
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, PATH_DATA);
	    props.loadProperties(UI_PROPERTIES_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME);
            return true;
       } catch (InvalidXMLFileFormatException ixmlffe) {
            // SOMETHING WENT WRONG INITIALIZING THE XML FILE
            ErrorHandler eH = ui.getErrorHandler();
            eH.processError(LanguagePropertyType.ERROR_PROPERTIES_FILE_LOADING, "XML File Problem");
            return false;
        }        
    }

    /**
     * This is where the application starts execution. We'll load the
     * application properties and then use them to build our user interface and
     * start the window in event handling mode. Once in that mode, all code
     * execution will happen in response to user requests.
     *
     * @param args This application does not use any command line arguments.
     */
    public static void main(String[] args) {
	launch(args);
    }
}
