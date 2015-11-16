/**
 * @coauthor Jia Li
 **/
package epm.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import static epm.StartupConstants.PATH_SLIDE_SHOWS;
import epm.model.Page;
import epm.model.EPortfolioModel;

/**
 * This class uses the JSON standard to read and write slideshow data files.
 * 
 * @author McKilla Gorilla & _____________
 */
public class EPortfolioFileManager {
    // JSON FILE READING AND WRITING CONSTANTS
    public static String JSON_TITLE = "title";
    public static String JSON_SLIDES = "slides";
    public static String JSON_IMAGE_FILE_NAME = "image_file_name";
    public static String JSON_IMAGE_PATH = "image_path";
    public static String JSON_CAPTION = "image_caption";
    public static String JSON_EXT = ".json";
    public static String SLASH = "/";

    /**
     * This method saves all the data associated with a slide show to
     * a JSON file.
     * 
     * @param slideShowToSave The course whose data we are saving.
     * 
     * @throws IOException Thrown when there are issues writing
     * to the JSON file.
     */
    public void saveSlideShow(EPortfolioModel ePortfolioToSave) throws IOException {
       
        String title = "" + ePortfolioToSave.getTitle();
        if (ePortfolioToSave.getTitle() == null || title.equals(""))
            throw new IOException();
            
        // BUILD THE FILE PATH
        String slideShowTitle = "" + title;
        String jsonFilePath = PATH_SLIDE_SHOWS + SLASH + slideShowTitle + JSON_EXT;
        
        // INIT THE WRITER
        OutputStream os = new FileOutputStream(jsonFilePath);
        JsonWriter jsonWriter = Json.createWriter(os);  
       
        // BUILD THE SLIDES ARRAY
        JsonArray slidesJsonArray = makeSlidesJsonArray(ePortfolioToSave.getPages());
        
        // NOW BUILD THE COURSE USING EVERYTHING WE'VE ALREADY MADE
        JsonObject courseJsonObject = Json.createObjectBuilder()
                                    .add(JSON_TITLE, title)
                                    .add(JSON_SLIDES, slidesJsonArray)
                .build();
        
        // AND SAVE EVERYTHING AT ONCE
        jsonWriter.writeObject(courseJsonObject);
    }
    
    /**
     * This method loads the contents of a JSON file representing a slide show
     * into a SlideSShowModel object.
     * 
     * @param slideShowToLoad The slide show to load
     * @param jsonFilePath The JSON file to load.
     * @throws IOException 
     */
    public void loadSlideShow(EPortfolioModel EPortfolioToLoad, String jsonFilePath) throws IOException {
        // LOAD THE JSON FILE WITH ALL THE DATA
        JsonObject json = loadJSONFile(jsonFilePath);
        
        // NOW LOAD THE COURSE
	EPortfolioToLoad.reset();
        EPortfolioToLoad.setTitle(json.getString(JSON_TITLE));
        JsonArray jsonSlidesArray = json.getJsonArray(JSON_SLIDES);
        for (int i = 0; i < jsonSlidesArray.size(); i++) {
	    JsonObject slideJso = jsonSlidesArray.getJsonObject(i);
	    EPortfolioToLoad.addPage(	slideJso.getString(JSON_IMAGE_FILE_NAME),
					slideJso.getString(JSON_IMAGE_PATH));
	}
    }

    // AND HERE ARE THE PRIVATE HELPER METHODS TO HELP THE PUBLIC ONES
    
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
        InputStream is = new FileInputStream(jsonFilePath);
        JsonReader jsonReader = Json.createReader(is);
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        is.close();
        return json;
    }    
    
    private ArrayList<String> loadArrayFromJSONFile(String jsonFilePath, String arrayName) throws IOException {
        JsonObject json = loadJSONFile(jsonFilePath);
        ArrayList<String> items = new ArrayList();
        JsonArray jsonArray = json.getJsonArray(arrayName);
        for (JsonValue jsV : jsonArray) {
            items.add(jsV.toString());
        }
        return items;
    }
    
    private JsonArray makeSlidesJsonArray(List<Page> pages) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (Page page : pages) {
	    JsonObject jso = makeSlideJsonObject(page);
	    jsb.add(jso);
        }
        JsonArray jA = jsb.build();
        return jA;        
    }
    
    private JsonObject makeSlideJsonObject(Page page) {
        JsonObject jso = Json.createObjectBuilder()
		.add(JSON_IMAGE_FILE_NAME, page.getImageFileName())
		.add(JSON_IMAGE_PATH, page.getImagePath())
                .add(JSON_CAPTION, page.getCaption())
		.build();
	return jso;
    }
}
