package epm.file;

import static epm.StartupConstants.PATH_EPORTFOLIOS;
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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javafx.stage.FileChooser;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

/**
 * This class uses the JSON standard to read and write ePortfolio data files.
 * 
 * @author Jia Li
 */
public class EPortfolioFileManager {
    // JSON FILE READING AND WRITING CONSTANTS
    public static String JSON_TITLE = "title";
    public static String JSON_IMAGE_FILE_NAME = "image_file_name";
    public static String JSON_IMAGE_PATH = "image_path";
    public static String JSON_EXT = ".json";
    public static String SLASH = "/";
    private String dataPath = PATH_EPORTFOLIOS;
    
    /**
     * This method saves all the data associated with an ePortfolio to
     * a JSON file.
     * 
     */
    public void saveEPortfolio(EPortfolioModel ePortfolioToSave) throws IOException {
       
        String title = "" + ePortfolioToSave.getTitle();
        if (ePortfolioToSave.getTitle() == null || title.equals(""))
            title = "Untitled";
            
        // BUILD THE FILE PATH
        String slideShowTitle = "" + title;
        String jsonFilePath = dataPath + SLASH + slideShowTitle + JSON_EXT;
       
        // BUILD THE SLIDES ARRAY
        JsonArray pagesJsonArray = makePagesJsonArray(ePortfolioToSave.getPages(), ePortfolioToSave, false);
        
        // NOW BUILD THE COURSE USING EVERYTHING WE'VE ALREADY MADE
        JsonObject courseJsonObject = Json.createObjectBuilder()
                                    .add(JSON_TITLE, title)
                                    .add("pages", pagesJsonArray)
                .build();
        
        // AND SAVE EVERYTHING AT ONCE
        StringWriter sw = new StringWriter();
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);

	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(courseJsonObject);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(jsonFilePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(courseJsonObject);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(jsonFilePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    public void saveExportEPortfolio(EPortfolioModel ePortfolioToSave) throws IOException {
       
        String title = "" + ePortfolioToSave.getTitle();
        if (ePortfolioToSave.getTitle() == null || title.equals(""))
            title = "Untitled";

        // BUILD THE FILE PATH
        String jsonFilePath = "sites" + SLASH + title + "/Testing" + JSON_EXT;
        File newJSONFile = new File("sites/" + title + "/Testing" + JSON_EXT);
        if (newJSONFile.exists())
            newJSONFile.delete();
        
       
        // BUILD THE SLIDES ARRAY
        JsonArray pagesJsonArray = makePagesJsonArray(ePortfolioToSave.getPages(), ePortfolioToSave, true);
        
        // NOW BUILD THE COURSE USING EVERYTHING WE'VE ALREADY MADE
        JsonObject courseJsonObject = Json.createObjectBuilder()
                                    .add(JSON_TITLE, title)
                                    .add("pages", pagesJsonArray)
                .build();
        
        // AND SAVE EVERYTHING AT ONCE
        StringWriter sw = new StringWriter();
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);

	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(courseJsonObject);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(jsonFilePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(courseJsonObject);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(jsonFilePath);
	pw.write(prettyPrinted);
	pw.close();
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
        int num = title.indexOf(".json");
        if (num != -1)
            title = title.substring(0, num);
        int pos = dataPath.indexOf("\\" + title);
        dataPath = dataPath.substring(0, pos);

        
        // BUILD THE FILE PATH
        String jsonFilePath = dataPath + SLASH + title + JSON_EXT;
       
        // BUILD THE SLIDES ARRAY
        JsonArray pagesJsonArray = makePagesJsonArray(ePortfolioToSave.getPages(), ePortfolioToSave, false);
        
        // NOW BUILD THE COURSE USING EVERYTHING WE'VE ALREADY MADE
        JsonObject courseJsonObject = Json.createObjectBuilder()
                                    .add(JSON_TITLE, title)
                                    .add("pages", pagesJsonArray)
                .build();
        
        // AND SAVE EVERYTHING AT ONCE
        StringWriter sw = new StringWriter();
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);

	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(courseJsonObject);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(jsonFilePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(courseJsonObject);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(jsonFilePath);
	pw.write(prettyPrinted);
	pw.close();
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
        dataPath = jsonFilePath;
        String title = json.getString("title");
        int pos = dataPath.indexOf("\\" + title);
        dataPath = dataPath.substring(0, pos);
        // NOW LOAD THE COURSE
	EPortfolioToLoad.reset();
        EPortfolioToLoad.setTitle(json.getString("title"));
        JsonArray jsonPagesArray = json.getJsonArray("pages");
        for (int i = 0; i < jsonPagesArray.size(); i++) {
	    JsonObject pageJso = jsonPagesArray.getJsonObject(i);
	    Page page = EPortfolioToLoad.addPage(   pageJso.getString("title"),
                                                    pageJso.getString("name"),
                                                    pageJso.getString("layout"),
                                                    pageJso.getString("color"),
                                                    pageJso.getString("font"),
                                                    pageJso.getString("footer"),
                                                    pageJso.getString("banner_image_path"),
                                                    pageJso.getString("banner_image_name"));
            
            JsonArray pageTextComponentsArray = pageJso.getJsonArray("text_components");
            for (int j = 0; j < pageTextComponentsArray.size(); j++) {
                JsonObject componentJso = pageTextComponentsArray.getJsonObject(j);
                String type = componentJso.getString("type");
                if (type.equalsIgnoreCase("paragraph")) {
                    TextComponent text = new TextComponent( componentJso.getString("type"),
                                                            componentJso.getString("text"),
                                                            componentJso.getString("font"));
                    text.setStyle(componentJso.getString("style"));
                    text.setSize(Integer.parseInt(componentJso.getString("size")));
                    JsonArray hyperlinkArray = componentJso.getJsonArray("hyperlink");
                    for (int k = 0; k < hyperlinkArray.size(); k++) {
                        JsonObject linkJso = hyperlinkArray.getJsonObject(k);
                        HyperlinkComponent link = new HyperlinkComponent(   componentJso.getString("text"),
                                                                            linkJso.getString("selected_text"),
                                                                            linkJso.getString("url"));
                        text.addHyperlink(link);
                    }
                    page.addTextComponent(text);
                }
                else if (type.equalsIgnoreCase("header")) {
                    TextComponent text = new TextComponent( componentJso.getString("type"),
                                                            componentJso.getString("text"),
                                                            componentJso.getString("font"));
                    text.setStyle(componentJso.getString("style"));
                    text.setSize(Integer.parseInt(componentJso.getString("size")));
                    page.addTextComponent(text);
                } 
                else {
                    JsonArray listArray = componentJso.getJsonArray("text");
                    ArrayList<String> list = new ArrayList<String>();
                    for (int k = 0; k < listArray.size(); k++) {
                        JsonObject listJso = listArray.getJsonObject(k);
                        list.add(listJso.getString("data"));
                    }
                    TextComponent text = new TextComponent( componentJso.getString("type"),
                                                            list,
                                                            componentJso.getString("font"));
                    text.setStyle(componentJso.getString("style"));
                    text.setSize(Integer.parseInt(componentJso.getString("size")));
                    JsonArray hyperlinkArray = componentJso.getJsonArray("hyperlink");
                    for (int k = 0; k < hyperlinkArray.size(); k++) {
                        JsonObject linkJso = hyperlinkArray.getJsonObject(k);
                        HyperlinkComponent link = new HyperlinkComponent(   Integer.parseInt(linkJso.getString("selected_text")),
                                                                            list,
                                                                            linkJso.getString("url"));
                        text.addHyperlink(link);
                    }
                    page.addTextComponent(text);
                }   
            }
            
            JsonArray pageImageComponentsArray = pageJso.getJsonArray("image_paths");  
            for (int k = 0; k < pageImageComponentsArray.size(); k++) {
                JsonObject componentJso = pageImageComponentsArray.getJsonObject(k);
                double width = Double.parseDouble(componentJso.getString("width"));
                double height = Double.parseDouble(componentJso.getString("height"));
                ImageComponent image = new ImageComponent(  componentJso.getString("path"),
                                                            componentJso.getString("position"),
                                                            width,
                                                            height,
                                                            componentJso.getString("caption"));
                page.addImageComponent(image);
            }
            
            JsonArray pageVideoComponentsArray = pageJso.getJsonArray("video_paths");  
            for (int k = 0; k < pageVideoComponentsArray.size(); k++) {
                JsonObject componentJso = pageVideoComponentsArray.getJsonObject(k);
                double width = Double.parseDouble(componentJso.getString("width"));
                double height = Double.parseDouble(componentJso.getString("height"));
                VideoComponent video = new VideoComponent(  componentJso.getString("path"),
                                                            componentJso.getString("caption"),
                                                            width,
                                                            height);
                page.addVideoComponent(video);
            }
            
            JsonArray pageSlideShowComponentsArray = pageJso.getJsonArray("slideshow_component");  
            for (int k = 0; k < pageSlideShowComponentsArray.size(); k++) {
                JsonObject componentJso = pageSlideShowComponentsArray.getJsonObject(k);
                ArrayList<String> imagePaths = new ArrayList<String>();
                ArrayList<String> imageNames = new ArrayList<String>();
                ArrayList<String> imageCaptions = new ArrayList<String>();
                
                JsonArray pathsArray = componentJso.getJsonArray("paths");  
                for (int l = 0; l < pathsArray.size(); l++) {
                    JsonObject pathJso = pathsArray.getJsonObject(l);
                    imagePaths.add(pathJso.getString("image_path"));
                }
                JsonArray namesArray = componentJso.getJsonArray("names");  
                for (int l = 0; l < namesArray.size(); l++) {
                    JsonObject nameJso = namesArray.getJsonObject(l);
                    imageNames.add(nameJso.getString("image_name"));
                }
                JsonArray captionsArray = componentJso.getJsonArray("captions");  
                for (int l = 0; l < captionsArray.size(); l++) {
                    JsonObject captionJso = captionsArray.getJsonObject(l);
                    imageCaptions.add(captionJso.getString("image_caption"));
                }
                
                SlideShowComponent slideShow = new SlideShowComponent(imagePaths, imageCaptions, imageNames);
                page.addSlideShowComponent(slideShow);
            }
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
    
    private JsonArray makePagesJsonArray(List<Page> pages, EPortfolioModel model, boolean export) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (Page page : pages) {
	    JsonObject jso = makePageJsonObject(page, model, export);
	    jsb.add(jso);
        }
        JsonArray jA = jsb.build();
        return jA;        
    }
    
    private JsonObject makePageJsonObject(Page page, EPortfolioModel model, boolean export) {
        JsonObject jso = Json.createObjectBuilder()
		.add("title", page.getTitle())
		.add("name", model.getStudentName())
                .add("layout", page.getLayout())
                .add("banner_image_path", page.getBannerImgPath())
                .add("banner_image_name", page.getBannerImg())
                .add("color", page.getColor())
                .add("font", page.getFont())
                .add("footer", page.getFooter())
                .add("text_components", makeTextComponentJsonArray(page))
                .add("image_paths", makeImageComponentJsonArray(page))
                .add("video_paths", makeVideoComponentJsonArray(page))
                .add("slideshow_component", makeSlideShowComponentJsonArray(page, export))
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
                    .add("size", "" + component.getSize())
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
                    .add("size", "" + component.getSize())
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
    
    private JsonArray makeSlideShowComponentJsonArray(Page page, boolean export) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        int i = 0;
        for (SlideShowComponent component : page.getSlideShowComponents()) {
	    JsonObject jso = makeSlideShowComponentJsonObject(component, export, i, page);
	    jsb.add(jso);
            i++;
        }
        JsonArray jA = jsb.build();
        return jA;        
    }
    
    private JsonObject makeSlideShowComponentJsonObject(SlideShowComponent component, boolean export, int counter, Page page) {
        if (!export) {
            JsonObject jso = Json.createObjectBuilder()
                    .add("paths", makeImagePathJsonArray(component))
                    .add("names", makeImageNameJsonArray(component))
                    .add("captions", makeCaptionJsonArray(component))
                    .build();
            return jso;
        }
        String name = "";
        String[] partials = page.getTitle().split(" ");
        for (String s : partials)
            name += s;
        JsonObject jso = Json.createObjectBuilder()
                    .add("slideshow_paths", "SlideShow/" + name + counter + "/index.html")
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
		.add("image_name", str)
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
                .add("selected_text", "" + link.getIndex())
		.build();
	return jso;
    }
}
