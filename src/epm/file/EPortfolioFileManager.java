/**
 * @coauthor Jia Li
 **/
package epm.file;

import epm.EPortfolioMaker;
import static epm.StartupConstants.PATH_SLIDE_SHOWS;
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
import epm.model.Page;
import epm.model.EPortfolioModel;
import epm.model.HyperlinkComponent;
import epm.model.ImageComponent;
import epm.model.SlideShowComponent;
import epm.model.TextComponent;
import epm.model.VideoComponent;
import epm.view.EPortfolioMakerView;
import java.io.File;
import java.math.BigDecimal;
import javafx.stage.FileChooser;

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
    private String dataPath = PATH_SLIDE_SHOWS;
    
    /**
     * This method saves all the data associated with a slide show to
     * a JSON file.
     * 
     * @param slideShowToSave The course whose data we are saving.
     * 
     * @throws IOException Thrown when there are issues writing
     * to the JSON file.
     */
    public void saveEPortfolio(EPortfolioModel ePortfolioToSave) throws IOException {
       
        String title = "" + ePortfolioToSave.getTitle();
        if (ePortfolioToSave.getTitle() == null || title.equals(""))
            title = "Untitled";
            
        // BUILD THE FILE PATH
        String slideShowTitle = "" + title;
        String jsonFilePath = dataPath + SLASH + slideShowTitle + JSON_EXT;
        
        // INIT THE WRITER
        OutputStream os = new FileOutputStream(jsonFilePath);
        JsonWriter jsonWriter = Json.createWriter(os);  
       
        // BUILD THE SLIDES ARRAY
        JsonArray pagesJsonArray = makePagesJsonArray(ePortfolioToSave.getPages());
        
        // NOW BUILD THE COURSE USING EVERYTHING WE'VE ALREADY MADE
        JsonObject courseJsonObject = Json.createObjectBuilder()
                                    .add(JSON_TITLE, title)
                                    .add("pages", pagesJsonArray)
                .build();
        
        // AND SAVE EVERYTHING AT ONCE
        jsonWriter.writeObject(courseJsonObject);
    }
    
    public void saveAsEPortfolio(EPortfolioModel ePortfolioToSave, EPortfolioMakerView ui) throws IOException {
        FileChooser fileSaver = new FileChooser();
        fileSaver.setInitialDirectory(new File(dataPath));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileSaver.getExtensionFilters().add(extFilter);
        File file = fileSaver.showSaveDialog(ui.getWindow());
        
        String title = "" + ePortfolioToSave.getTitle();
        if (ePortfolioToSave.getTitle() == null || title.equals(""))
            title = "Untitled";
        
        fileSaver.setInitialFileName(title);
        if (file == null)
            return;
        dataPath = file.getAbsolutePath();
        title = file.getName();
        int pos = dataPath.indexOf("\\" + title);
        dataPath = dataPath.substring(0, pos);

        
        // BUILD THE FILE PATH
        String jsonFilePath = dataPath + SLASH + title;
        
        // INIT THE WRITER
        OutputStream os = new FileOutputStream(jsonFilePath);
        JsonWriter jsonWriter = Json.createWriter(os);  
       
        // BUILD THE SLIDES ARRAY
        JsonArray pagesJsonArray = makePagesJsonArray(ePortfolioToSave.getPages());
        
        // NOW BUILD THE COURSE USING EVERYTHING WE'VE ALREADY MADE
        JsonObject courseJsonObject = Json.createObjectBuilder()
                                    .add(JSON_TITLE, title)
                                    .add("pages", pagesJsonArray)
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
    
    private JsonArray makePagesJsonArray(List<Page> pages) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (Page page : pages) {
	    JsonObject jso = makePageJsonObject(page);
	    jsb.add(jso);
        }
        JsonArray jA = jsb.build();
        return jA;        
    }
    
    private JsonObject makePageJsonObject(Page page) {
        JsonObject jso = Json.createObjectBuilder()
		.add("title", page.getTitle())
		.add("name", page.getStudentName())
                .add("layout", page.getLayout())
                .add("banner_image_path", page.getBannerImgPath())
                .add("banner_image_name", page.getBannerImg())
                .add("color", page.getColor())
                .add("font", page.getFont())
                .add("footer", page.getFooter())
                .add("text_components", makeTextComponentJsonArray(page))
                .add("image_paths", makeImageComponentJsonArray(page))
                .add("video_paths", makeVideoComponentJsonArray(page))
                .add("slideshow_component", makeSlideShowComponentJsonArray(page))
		.build();
	return jso;
    }
    
    private JsonArray makeTextComponentJsonArray(Page page) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (TextComponent component : page.getTextComponents()) {
	    JsonObject jso = makeTextComponentJsonObject(component);
	    jsb.add(jso);
        }
        JsonArray jA = jsb.build();
        return jA;        
    }
    
    private JsonObject makeTextComponentJsonObject(TextComponent component) {
        if (component.getTextType().equalsIgnoreCase("paragraph") || component.getTextType().equalsIgnoreCase("header")) {
            JsonObject jso = Json.createObjectBuilder()
                    .add("type", component.getTextType())
                    .add("text", component.getData())
                    .add("font", component.getFont())
                    .add("style", component.getStyle())
                    .add("size", component.getSize())
                    .add("hyperlink", makeParagraphHyperlinkJsonArray(component))
                    .build();
            return jso;
        }
        else {
            JsonObject jso = Json.createObjectBuilder()
                    .add("type", component.getTextType())
                    .add("text", makeListComponentJsonArray(component))
                    .add("font", component.getFont())
                    .add("style", component.getStyle())
                    .add("size", component.getSize())
                    .add("hyperlink", makeListHyperlinkJsonArray(component))
                    .build();
            return jso;
        }
    }
    
    private JsonArray makeListComponentJsonArray(TextComponent component) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (String s : component.getList()) {
	    JsonObject jso = makeListComponentJsonObject(s);
	    jsb.add(jso);
        }
        JsonArray jA = jsb.build();
        return jA;        
    }
    
    private JsonObject makeListComponentJsonObject(String str) {
        JsonObject jso = Json.createObjectBuilder()
		.add("data", str)
		.build();
	return jso;
    }
    
    private JsonArray makeImageComponentJsonArray(Page page) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (ImageComponent component : page.getImageComponents()) {
	    JsonObject jso = makeImageComponentJsonObject(component);
	    jsb.add(jso);
        }
        JsonArray jA = jsb.build();
        return jA;        
    }
    
    private JsonObject makeImageComponentJsonObject(ImageComponent component) {
        JsonObject jso = Json.createObjectBuilder()
		.add("path", component.getUrl())
                .add("name", component.getImageName())
                .add("caption", component.getCaption())
                .add("position", component.getPosition())
                .add("width", "" + component.getWidth())
                .add("height", "" + component.getHeight())
		.build();
	return jso;
    }
    
    private JsonArray makeVideoComponentJsonArray(Page page) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (VideoComponent component : page.getVideoComponents()) {
	    JsonObject jso = makeVideoComponentJsonObject(component);
	    jsb.add(jso);
        }
        JsonArray jA = jsb.build();
        return jA;        
    }
    
    private JsonObject makeVideoComponentJsonObject(VideoComponent component) {
        JsonObject jso = Json.createObjectBuilder()
		.add("path", component.getUrl())
                .add("name", component.getVideoName())
                .add("caption", component.getCaption())
                .add("width", "" + component.getWidth())
                .add("height", "" + component.getHeight())
		.build();
	return jso;
    }
    
    private JsonArray makeSlideShowComponentJsonArray(Page page) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (SlideShowComponent component : page.getSlideShowComponents()) {
	    JsonObject jso = makeSlideShowComponentJsonObject(component);
	    jsb.add(jso);
        }
        JsonArray jA = jsb.build();
        return jA;        
    }
    
    private JsonObject makeSlideShowComponentJsonObject(SlideShowComponent component) {
        JsonObject jso = Json.createObjectBuilder()
		.add("paths", makeImagePathJsonArray(component))
                .add("names", makeImageNameJsonArray(component))
                .add("captions", makeCaptionJsonArray(component))
		.build();
	return jso;
    }
    
    private JsonArray makeImagePathJsonArray(SlideShowComponent component) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (String s : component.getImagePaths()) {
	    JsonObject jso = makeImagePathJsonObject(s);
	    jsb.add(jso);
        }
        JsonArray jA = jsb.build();
        return jA;        
    }
    
    private JsonObject makeImagePathJsonObject(String str) {
        JsonObject jso = Json.createObjectBuilder()
		.add("image_path", str)
		.build();
	return jso;
    }
    
    private JsonArray makeCaptionJsonArray(SlideShowComponent component) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (String s : component.getCaptions()) {
	    JsonObject jso = makeCaptionJsonObject(s);
	    jsb.add(jso);
        }
        JsonArray jA = jsb.build();
        return jA;        
    }
    
    private JsonObject makeCaptionJsonObject(String str) {
        JsonObject jso = Json.createObjectBuilder()
		.add("image_caption", str)
		.build();
	return jso;
    }
    
    private JsonArray makeImageNameJsonArray(SlideShowComponent component) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (String s : component.getImageNames()) {
	    JsonObject jso = makeImageNameJsonObject(s);
	    jsb.add(jso);
        }
        JsonArray jA = jsb.build();
        return jA;        
    }
    
    private JsonObject makeImageNameJsonObject(String str) {
        JsonObject jso = Json.createObjectBuilder()
		.add("image_caption", str)
		.build();
	return jso;
    }
    
    private JsonArray makeParagraphHyperlinkJsonArray(TextComponent component) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (HyperlinkComponent link : component.getHyperlinks()) {
	    JsonObject jso = makeParagraphHyperlinkJsonObject(link);
	    jsb.add(jso);
        }
        JsonArray jA = jsb.build();
        return jA;        
    }
    
    private JsonObject makeParagraphHyperlinkJsonObject(HyperlinkComponent link) {
        JsonObject jso = Json.createObjectBuilder()
		.add("url", link.getUrl())
                .add("selected_text", link.getSelectedText())
		.build();
	return jso;
    }
    
    private JsonArray makeListHyperlinkJsonArray(TextComponent component) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (HyperlinkComponent link : component.getHyperlinks()) {
	    JsonObject jso = makeListHyperlinkJsonObject(link);
	    jsb.add(jso);
        }
        JsonArray jA = jsb.build();
        return jA;        
    }
    
    private JsonObject makeListHyperlinkJsonObject(HyperlinkComponent link) {
        JsonObject jso = Json.createObjectBuilder()
		.add("url", link.getUrl())
                .add("selected_text", link.getIndexItem())
		.build();
	return jso;
    }
}
