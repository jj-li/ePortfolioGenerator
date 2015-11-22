package epm.ssm;

import static epm.ssm.LanguagePropertyType.TITLE_WINDOW;
import static epm.ssm.StartupConstants.PATH_DATA;
import static epm.ssm.StartupConstants.PROPERTIES_SCHEMA_FILE_NAME;
import epm.ssm.view.SlideShowMakerView;
import javafx.stage.Stage;

import xml_utilities.InvalidXMLFileFormatException;
import properties_manager.PropertiesManager;



/**
 * SlideShowMaker is a program for making custom image slideshows. It will allow
 * the user to name their slideshow, select images to use, select captions for
 * the images, and the order of appearance for slides.
 *
 * @author McKilla Gorilla & _____________
 */
public class SlideShowMaker extends Stage{
    
    SlideShowMakerView ui = new SlideShowMakerView(this);
    
    public SlideShowMaker(){
	String languageCode = "EN";
	
        // LOAD APP SETTINGS INTO THE GUI AND START IT UP
        boolean success = loadProperties(languageCode);
        if (success) {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String appTitle = props.getProperty(TITLE_WINDOW);
            ui.startUI(this, appTitle);
	} // THERE WAS A PROBLEM LOADING THE PROPERTIES FILE
	else {
	}
    }
    
    /**
     * Loads this application's properties file, which has a number of settings
     * for initializing the user interface.
     * 
     * @return true if the properties file was loaded successfully, false otherwise.
     */
    public boolean loadProperties(String languageCode) {
        try {
	    // FIGURE OUT THE PROPER FILE NAME
	    String propertiesFileName = "properties_" + languageCode + ".xml";

	    // LOAD THE SETTINGS FOR STARTING THE APP
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, PATH_DATA);
	    props.loadProperties(propertiesFileName, PROPERTIES_SCHEMA_FILE_NAME);
            return true;
       } catch (InvalidXMLFileFormatException ixmlffe) {
            // SOMETHING WENT WRONG INITIALIZING THE XML FILE
            return false;
        }        
    }


}
