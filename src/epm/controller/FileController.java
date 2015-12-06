/**
 * @coauthor Jia Li
 **/
package epm.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.Scanner;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import static epm.LanguagePropertyType.BUTTON_OKAY;
import static epm.LanguagePropertyType.FAILED_SLIDE_SHOW_CREATED;
import static epm.LanguagePropertyType.FAILED_SLIDE_SHOW_CREATED_TITLE;
import static epm.LanguagePropertyType.FAILED_SLIDE_SHOW_EXIT;
import static epm.LanguagePropertyType.FAILED_SLIDE_SHOW_EXIT_TITLE;
import static epm.LanguagePropertyType.FAILED_SLIDE_SHOW_LOAD;
import static epm.LanguagePropertyType.FAILED_SLIDE_SHOW_LOAD_TITLE;
import static epm.LanguagePropertyType.FAILED_SLIDE_SHOW_SAVE;
import static epm.LanguagePropertyType.FAILED_SLIDE_SHOW_SAVE_TITLE;
import static epm.LanguagePropertyType.SAVE_TEXT;
import static epm.LanguagePropertyType.SLIDE_SHOW_CREATED;
import static epm.LanguagePropertyType.SLIDE_SHOW_CREATED_TITLE;
import static epm.LanguagePropertyType.TITLE_WINDOW;
import static epm.StartupConstants.PATH_ICONS;
import static epm.StartupConstants.PATH_SLIDE_SHOWS;
import epm.model.EPortfolioModel;
import epm.error.ErrorHandler;
import epm.file.EPortfolioFileManager;
import epm.view.EPortfolioMakerView;
import static epm.StartupConstants.STYLE_SHEET_UI;
import static epm.StartupConstants.WINDOWS_ICON;
import static epm.file.EPortfolioFileManager.JSON_EXT;
import static epm.file.EPortfolioFileManager.SLASH;
import epm.model.ImageComponent;
import epm.model.Page;
import epm.model.VideoComponent;
import epm.model.SlideShowComponent;
import epm.ssm.model.Slide;
import epm.ssm.model.SlideShowModel;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonWriter;
/**
 * This class serves as the controller for all file toolbar operations,
 * driving the loading and saving of slide shows, among other things.
 * 
 * @author McKilla Gorilla & _____________
 */
public class FileController {

    // WE WANT TO KEEP TRACK OF WHEN SOMETHING HAS NOT BEEN SAVED
    private boolean saved;
    
    private boolean savedWork = false;

    // THE APP UI
    private EPortfolioMakerView ui;
    
    // THIS GUY KNOWS HOW TO READ AND WRITE SLIDE SHOW DATA
    private EPortfolioFileManager slideShowIO;
    
    
    private PropertiesManager prop;
    
    /**
     * This default constructor starts the program without a slide show file being
     * edited.
     *
     * @param initSlideShowIO The object that will be reading and writing slide show
     * data.
     */
    public FileController(EPortfolioMakerView initUI, EPortfolioFileManager initEPortfolioIO) {
        // NOTHING YET
        saved = true;
	ui = initUI;
        slideShowIO = initEPortfolioIO;
        prop = PropertiesManager.getPropertiesManager();
    }
    
    public void markAsEdited() {
        saved = false;
        ui.updateToolbarControls(saved);
    }

    /**
     * This method starts the process of editing a new slide show. If a pose is
     * already being edited, it will prompt the user to save it first.
     */
    public void handleNewSlideShowRequest() {
        try {
            // WE MAY HAVE TO SAVE CURRENT WORK
            boolean continueToMakeNew = true;
            if (!saved) {
                // THE USER CAN OPT OUT HERE WITH A CANCEL
                continueToMakeNew = promptToSave();
            }

            // IF THE USER REALLY WANTS TO MAKE A NEW COURSE
            if (continueToMakeNew) {
                // RESET THE DATA, WHICH SHOULD TRIGGER A RESET OF THE UI
                EPortfolioModel ePortfolio = ui.getEPortfolio();
		ePortfolio.reset();
                ui.reloadSlideShowPane(ePortfolio);
                saved = false;

                // REFRESH THE GUI, WHICH WILL ENABLE AND DISABLE
                // THE APPROPRIATE CONTROLS
                ui.updateToolbarControls(saved);
                // TELL THE USER THE SLIDE SHOW HAS BEEN CREATED
            }
        } catch (IOException ioe) {
            ErrorHandler eH = ui.getErrorHandler();
            eH.processError(FAILED_SLIDE_SHOW_CREATED, prop.getProperty(FAILED_SLIDE_SHOW_CREATED_TITLE));
        }
    }

    /**
     * This method lets the user open a slideshow saved to a file. It will also
     * make sure data for the current slideshow is not lost.
     */
    public void handleLoadEPortfolioRequest() {
        try {
            // WE MAY HAVE TO SAVE CURRENT WORK
            boolean continueToOpen = true;
            if (!saved) {
                // THE USER CAN OPT OUT HERE WITH A CANCEL
                continueToOpen = promptToSave();
                if (!continueToOpen) {
                // GO AHEAD AND PROCEED MAKING A NEW POSE
                promptToOpen();
            }
            }

            // IF THE USER REALLY WANTS TO OPEN A POSE
            if (continueToOpen) {
                // GO AHEAD AND PROCEED MAKING A NEW POSE
                promptToOpen();
            }
        } catch (IOException ioe) {
            ErrorHandler eH = ui.getErrorHandler();
            eH.processError(FAILED_SLIDE_SHOW_LOAD, prop.getProperty(FAILED_SLIDE_SHOW_LOAD_TITLE));
        }
    }

    /**
     * This method will save the current slideshow to a file. Note that we already
     * know the name of the file, so we won't need to prompt the user.
     */
    public boolean handleSaveEPortfolioRequest() {
        try {
	    // GET THE SLIDE SHOW TO SAVE
	    EPortfolioModel ePortfolioToSave = ui.getEPortfolio();
            // SAVE IT TO A FILE
            slideShowIO.saveEPortfolio(ePortfolioToSave);

            // MARK IT AS SAVED
            saved = true;

            // AND REFRESH THE GUI, WHICH WILL ENABLE AND DISABLE
            // THE APPROPRIATE CONTROLS
            ui.updateToolbarControls(saved);
	    return true;
        } catch (IOException ioe) {
            ErrorHandler eH = ui.getErrorHandler();
            eH.processError(FAILED_SLIDE_SHOW_SAVE, prop.getProperty(FAILED_SLIDE_SHOW_SAVE_TITLE));
	    return false;
        }
    }
    
     public boolean handleExportSaveEPortfolioRequest() {
        try {
	    // GET THE SLIDE SHOW TO SAVE
	    EPortfolioModel ePortfolioToSave = ui.getEPortfolio();
            // SAVE IT TO A FILE
            slideShowIO.saveExportEPortfolio(ePortfolioToSave);

            // MARK IT AS SAVED
            saved = true;

            // AND REFRESH THE GUI, WHICH WILL ENABLE AND DISABLE
            // THE APPROPRIATE CONTROLS
            ui.updateToolbarControls(saved);
	    return true;
        } catch (IOException ioe) {
            ErrorHandler eH = ui.getErrorHandler();
            eH.processError(FAILED_SLIDE_SHOW_SAVE, prop.getProperty(FAILED_SLIDE_SHOW_SAVE_TITLE));
	    return false;
        }
    }
    
     public boolean handleSaveAsEPortfolioRequest() {
        try {
	    // GET THE SLIDE SHOW TO SAVE
	    EPortfolioModel ePortfolioToSave = ui.getEPortfolio();
            // SAVE IT TO A FILE
            slideShowIO.saveAsEPortfolio(ePortfolioToSave, ui);

            // MARK IT AS SAVED
            saved = true;

            // AND REFRESH THE GUI, WHICH WILL ENABLE AND DISABLE
            // THE APPROPRIATE CONTROLS
	    return true;
        } catch (IOException ioe) {
            ErrorHandler eH = ui.getErrorHandler();
            eH.processError(FAILED_SLIDE_SHOW_SAVE, prop.getProperty(FAILED_SLIDE_SHOW_SAVE_TITLE));
            ioe.printStackTrace();
	    return false;
        }
    }

     /**
     * This method will exit the application, making sure the user doesn't lose
     * any data first.
     */
    public void handleExitRequest() {
        try {
            // WE MAY HAVE TO SAVE CURRENT WORK
            boolean continueToExit = true;
            if (!saved) {
                // THE USER CAN OPT OUT HERE
                continueToExit = promptToSave();
            }
            
            if (saved)
                System.exit(0);

            // IF THE USER REALLY WANTS TO EXIT THE APP
            if (!continueToExit) {
                // EXIT THE APPLICATION
                System.exit(0);
            }
        } catch (IOException ioe) {
            ErrorHandler eH = ui.getErrorHandler();
            eH.processError(FAILED_SLIDE_SHOW_EXIT, prop.getProperty(FAILED_SLIDE_SHOW_EXIT_TITLE));
        }
    }
    
    public void handleExportEPortfolioRequest()
    {
        EPortfolioModel ePortfolioToShow = ui.getEPortfolio();
        handleSaveEPortfolioRequest();
        Boolean siteDirectory = new File("sites/" + ePortfolioToShow.getTitle()).mkdirs();
        Boolean cssDirectory = new File("sites/" + ePortfolioToShow.getTitle() +"/css").mkdirs();
        Boolean jsDirectory = new File("sites/" + ePortfolioToShow.getTitle() +"/js").mkdirs();
        Boolean imgDirectory = new File("sites/" + ePortfolioToShow.getTitle() +"/img").mkdirs();
        Boolean videoDirectory = new File("sites/" + ePortfolioToShow.getTitle() +"/Videos").mkdirs();
        Boolean slideShowDirectory = new File("sites/" + ePortfolioToShow.getTitle() +"/SlideShow").mkdirs();
        File imgs = new File("sites/" + ePortfolioToShow.getTitle() +"/img");
        File videos = new File("sites/" + ePortfolioToShow.getTitle() +"/Videos");
        handleExportSaveEPortfolioRequest();
        
        if (imgs.exists()) {
            for (File files : imgs.listFiles()) {
                boolean fileExists = false;
                for (Page p : ePortfolioToShow.getPages()) {
                    for (ImageComponent image : p.getImageComponents()) {
                        if(files.toString().contains(image.getImageName()) == true)
                        fileExists = true;
                    }
                }
                if (!fileExists)
                    files.delete();
            }
        }  
        
        
        if (videos.exists()) {
            for (File files : imgs.listFiles()) {
                boolean fileExists = false;
                for (Page p : ePortfolioToShow.getPages()) {
                    for (VideoComponent video : p.getVideoComponents()) {
                        if(files.toString().contains(video.getVideoName()) == true)
                        fileExists = true;
                    }
                }
                if (!fileExists)
                    files.delete();
            }
        }
        
        String jsPath = "sites/Template/js/generateHTML.js";
        File jsFile = new File(jsPath);
        File newJSFile = new File("sites/" + ePortfolioToShow.getTitle() + "/js/generateHTML.js");
        
        try {
            Files.copy(jsFile.toPath(), newJSFile.toPath());
        }
        catch (FileAlreadyExistsException e6)
        {
                
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        for (Page page : ePortfolioToShow.getPages()) {
            int layoutNumber = 1;
            if (page.getLayout().equalsIgnoreCase("Top-Left Nagivation"))
                layoutNumber = 1;
            else if (page.getLayout().equalsIgnoreCase("Left Navgiation"))
                layoutNumber = 2;
            else if (page.getLayout().equalsIgnoreCase("Middle-Left Navigation"))
                layoutNumber = 3;
            else if (page.getLayout().equalsIgnoreCase("Middle-Right Navigation"))
                layoutNumber = 4;
            else
                layoutNumber = 5;
            

            File cssFile = new File("sites/Template/css/layout" + layoutNumber + "CSS.css");
            File newCSSFile = new File("sites/" + ePortfolioToShow.getTitle() + "/css/layout" + layoutNumber + "CSS.css");
            try {
                Files.copy(cssFile.toPath(), newCSSFile.toPath());
            }
            catch (FileAlreadyExistsException e3)
            {

            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
            
            int colorNumber = 1;
            if (page.getColor().equalsIgnoreCase("Blue/Yellow"))
                colorNumber = 1;
            else if (page.getColor().equalsIgnoreCase("Cyan/Red"))
                colorNumber = 2;
            else if (page.getColor().equalsIgnoreCase("Orange/Yellow"))
                colorNumber = 3;
            else if (page.getColor().equalsIgnoreCase("Red/Green"))
                colorNumber = 4;
            else
                colorNumber = 5;
            
            File colorFile = new File("sites/Template/css/colorFont" + colorNumber + ".css");
            File newColorFile = new File("sites/" + ePortfolioToShow.getTitle() + "/css/colorFont" + colorNumber + ".css");
            try {
                Files.copy(colorFile.toPath(), newColorFile.toPath());
            }
            catch (FileAlreadyExistsException e3)
            {

            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
            
            String pageTitle = page.getTitle();
            String[] noSpaces = pageTitle.split(" ");
            pageTitle = "";
            for (String s : noSpaces)
                pageTitle += s;
            
            String htmlString = "";
            String htmlPath = "sites/Template/layout" + layoutNumber + ".html";
            File temp = new File(htmlPath);
            try {
                Scanner myScan = new Scanner(temp);
                while (myScan.hasNextLine())
                    htmlString += myScan.nextLine() + "\n";
                myScan.close();
            }
            catch(Exception e)
            {

            }
            int pos = htmlString.indexOf("colorFont");
            String partOne = htmlString.substring(0, pos+9);
            String partTwo = htmlString.substring(pos+10);
            String partThree = "" + colorNumber;
            String completeJS = partOne + partThree + partTwo;
            byte[] bytes = completeJS.getBytes();
            try {
                OutputStream out = new BufferedOutputStream(new FileOutputStream("sites/" + ePortfolioToShow.getTitle() + "/" + pageTitle + ".html"),1024);
                out.write(bytes);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            String name = "";
            String[] partial = page.getTitle().split(" ");
            for (String s : partial)
                name += s;
            for (int i = 0; i < page.getSlideShowComponents().size(); i++) {
                SlideShowComponent component = page.getSlideShowComponents().get(i);
                handleExportSlideShowRequest(component, i, name, ePortfolioToShow);
            }
        }
        
        
        copyMedia(ePortfolioToShow);
        
        
        
    }
    
    private static void copyMedia(EPortfolioModel ePortfolioToShow)
    {
        for (Page p : ePortfolioToShow.getPages())
        {
            for (ImageComponent component : p.getImageComponents()) {
                String imagePath = component.getUrl();
                File image = new File(imagePath);
                File newImage = new File("sites/" + ePortfolioToShow.getTitle() + "/img/" + component.getImageName());
                try {
                    Files.copy(image.toPath(), newImage.toPath());
                }
                catch (FileAlreadyExistsException e1)
                {

                }
                catch (IOException e) {
                    e.printStackTrace();
                } 
            }
            
            for (VideoComponent component : p.getVideoComponents()) {
                String imagePath = component.getUrl();
                File image = new File(imagePath);
                File newImage = new File("sites/" + ePortfolioToShow.getTitle() + "/Videos/" + component.getVideoName());
                try {
                    Files.copy(image.toPath(), newImage.toPath());
                }
                catch (FileAlreadyExistsException e1)
                {

                }
                catch (IOException e) {
                    e.printStackTrace();
                } 
            }
        }
        
        
    }
    
   
    /**
     * This helper method verifies that the user really wants to save their
     * unsaved work, which they might not want to do. Note that it could be used
     * in multiple contexts before doing other actions, like creating a new
     * pose, or opening another pose, or exiting. Note that the user will be
     * presented with 3 options: YES, NO, and CANCEL. YES means the user wants
     * to save their work and continue the other action (we return true to
     * denote this), NO means don't save the work but continue with the other
     * action (true is returned), CANCEL means don't save the work and don't
     * continue with the other action (false is retuned).
     *
     * @return true if the user presses the YES option to save, true if the user
     * presses the NO option to not save, false if the user presses the CANCEL
     * option to not continue.
     */
    private boolean promptToSave() throws IOException {
        // PROMPT THE USER TO SAVE UNSAVED WORK
        
        Text saveText = new Text(prop.getProperty(SAVE_TEXT));
        Button btOK = new Button(prop.getProperty(BUTTON_OKAY));
        GridPane pane = new GridPane();
        Stage stage = new Stage();
        btOK.setOnAction(e -> {
            markFileAsSaved();
            stage.hide();
        });
        Button btCancel = new Button("NO");
        btCancel.setOnAction(e -> {
            markFileAsNotSaved();
            stage.hide();
        });
        pane.getStyleClass().add("error_box");
        pane.add(saveText, 1, 1);
        pane.add(btOK, 1, 2);
        pane.add(btCancel, 2, 2);
        Scene scene = new Scene(pane, 300, 75);
        scene.getStylesheets().add(STYLE_SHEET_UI);
        stage.getIcons().add(new Image("file:" + PATH_ICONS + WINDOWS_ICON));
        stage.setTitle(prop.getProperty(TITLE_WINDOW));
        stage.setScene(scene);
        stage.showAndWait();
         // @todo change this to prompt

        // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
        if (saved) {
            EPortfolioModel slideShow = ui.getEPortfolio();
            slideShowIO.saveEPortfolio(slideShow);
            saved = true;
        } // IF THE USER SAID CANCEL, THEN WE'LL TELL WHOEVER
        // CALLED THIS THAT THE USER IS NOT INTERESTED ANYMORE
        else if (!saved) {
            return false;
        }

        // IF THE USER SAID NO, WE JUST GO ON WITHOUT SAVING
        // BUT FOR BOTH YES AND NO WE DO WHATEVER THE USER
        // HAD IN MIND IN THE FIRST PLACE
        return true;
    }

    /**
     * This helper method asks the user for a file to open. The user-selected
     * file is then loaded and the GUI updated. Note that if the user cancels
     * the open process, nothing is done. If an error occurs loading the file, a
     * message is displayed, but nothing changes.
     */
    private void promptToOpen() {
        // AND NOW ASK THE USER FOR THE COURSE TO OPEN
        FileChooser slideShowFileChooser = new FileChooser();
        slideShowFileChooser.setInitialDirectory(new File(PATH_SLIDE_SHOWS));
        File selectedFile = slideShowFileChooser.showOpenDialog(ui.getWindow());

        // ONLY OPEN A NEW FILE IF THE USER SAYS OK
        if (selectedFile != null) {
            try {    
               // textFields = new ArrayList<TextField>();
		EPortfolioModel ePortfolioToLoad = ui.getEPortfolio();
                slideShowIO.loadSlideShow(ePortfolioToLoad, selectedFile.getAbsolutePath());
                
                ui.reloadSlideShowPane(ePortfolioToLoad);
                saved = true;
                ui.updateToolbarControls(true);
            } catch (Exception e) {
                e.printStackTrace();
                ErrorHandler eH = ui.getErrorHandler();
                eH.processError(FAILED_SLIDE_SHOW_LOAD, prop.getProperty(FAILED_SLIDE_SHOW_LOAD_TITLE));
            }
        }
    }
    

    /**
     * This mutator method marks the file as not saved, which means that when
     * the user wants to do a file-type operation, we should prompt the user to
     * save current work first. Note that this method should be called any time
     * the pose is changed in some way.
     */
    public void markFileAsNotSaved() {
        saved = false;
    }

    public void markFileAsSaved() {
        saved = true;
    }
    
    /**
     * Accessor method for checking to see if the current pose has been saved
     * since it was last editing. If the current file matches the pose data,
     * we'll return true, otherwise false.
     *
     * @return true if the current pose is saved to the file, false otherwise.
     */
    public boolean isSaved() {
        return saved;
    }
    
    public void handleEditEPortfolioRequest() {
        EPortfolioModel slideShowToShow = ui.getEPortfolio();
        ui.editWorkspace(slideShowToShow);
    }
    
    
    public void handleExportSlideShowRequest(SlideShowComponent component, int i, String title, EPortfolioModel model)
    {
        SlideShowModel slideShowToShow = component.getSlideShow().getUI().getSlideShow();
        String initPath = "sites/" + model.getTitle() + "/SlideShow/" +  title + i;
        
        Boolean siteDirectory = new File(initPath).mkdirs();
        Boolean cssDirectory = new File(initPath +"/css").mkdirs();
        Boolean jsDirectory = new File(initPath +"/js").mkdirs();
        Boolean imgDirectory = new File(initPath +"/img").mkdirs();
        File imgs = new File(initPath +"/img");
        if (imgs.exists()) {
            for (File files : imgs.listFiles()) {
                boolean fileExists = false;
                for (Slide s : slideShowToShow.getSlides()) {
                    if(files.toString().contains(s.getImageFileName()) == true)
                        fileExists = true;
                }
                if (!fileExists)
                    files.delete();
            }
        }    
        String jsString = "";
        String jsPath = "sites/Template/SlideShow/Template/js/TemplateJS.js";
        File jsFile = new File(jsPath);
        try {
            Scanner myScan = new Scanner(jsFile);
            while (myScan.hasNextLine())
                jsString += myScan.nextLine() + "\n";
            myScan.close();
        }
        catch(Exception e)
        {
            
        }
        int pos = jsString.indexOf("Missing Image");
        String partOne = jsString.substring(0, pos);
        String partTwo = jsString.substring(pos+13);
        String partThree = title+i;
        String completeJS = partOne + partThree + partTwo;
        byte[] bytes = completeJS.getBytes();
        try {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(initPath + "/js/TemplateJS.js"),1024);
            out.write(bytes);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        File htmlFile = new File("sites/Template/SlideShow/Template/index.html");
        File newHTMLFile = new File(initPath + "/index.html");
        try {
            Files.copy(htmlFile.toPath(), newHTMLFile.toPath());
        }
        catch (FileAlreadyExistsException e4)
        {
                
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        
        
        File cssFile = new File("sites/Template/SlideShow/Template/css/TemplateCSS.css");
        File newCSSFile = new File(initPath + "/css/TemplateCSS.css");
        try {
            Files.copy(cssFile.toPath(), newCSSFile.toPath());
        }
        catch (FileAlreadyExistsException e3)
        {
                
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        
        File newJSONFile = new File(initPath + "/" + title + i  + JSON_EXT);
        if (newJSONFile.exists())
            newJSONFile.delete();
        try {
            saveSlideShow(slideShowToShow, model.getTitle(), i, title);
        }
        catch (FileAlreadyExistsException e3)
        {
                
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        
        copyImages(slideShowToShow, initPath);
    }
    
    public void saveSlideShow(SlideShowModel slideShowToSave, String path, int i, String title) throws IOException {
            
        // BUILD THE FILE PATH
        String jsonFilePath = "sites/" + path + "/SlideShow/" +  title + i + "/" + title + i + JSON_EXT;
        
        // INIT THE WRITER
        OutputStream os = new FileOutputStream(jsonFilePath);
        JsonWriter jsonWriter = Json.createWriter(os);  
       
        // BUILD THE SLIDES ARRAY
        JsonArray slidesJsonArray = makeSlidesJsonArray(slideShowToSave.getSlides());
        
        // NOW BUILD THE COURSE USING EVERYTHING WE'VE ALREADY MADE
        JsonObject courseJsonObject = Json.createObjectBuilder()
                                    .add("titles", title)
                                    .add("slides", slidesJsonArray)
                .build();
        
        // AND SAVE EVERYTHING AT ONCE
        jsonWriter.writeObject(courseJsonObject);
    }
    
    private JsonArray makeSlidesJsonArray(List<Slide> slides) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (Slide slide : slides) {
	    JsonObject jso = makeSlideJsonObject(slide);
	    jsb.add(jso);
        }
        JsonArray jA = jsb.build();
        return jA;        
    }
    
    private JsonObject makeSlideJsonObject(Slide slide) {
        JsonObject jso = Json.createObjectBuilder()
		.add("image_file_name", slide.getImageFileName())
		.add("image_path", slide.getImagePath())
                .add("image_caption", slide.getCaption())
		.build();
	return jso;
    }
    
    private static void copyImages(SlideShowModel slideShowToShow, String path)
    {
        for (Slide s : slideShowToShow.getSlides())
        {
            String imagePath = s.getImagePath() + SLASH + s.getImageFileName();
            File image = new File(imagePath);
            File newImage = new File(path + "/img/" + s.getImageFileName());
            try {
                Files.copy(image.toPath(), newImage.toPath());
            }
            catch (FileAlreadyExistsException e1)
            {
                
            }
            catch (IOException e) {
                e.printStackTrace();
            } 
        }
        
        File next = new File("sites/Template/SlideShow/Template/img/next.png");
        File previous = new File("sites/Template/SlideShow/Template/img/previous.png");
        File play = new File("sites/Template/SlideShow/Template/img/play.png");
        File pause = new File("sites/Template/SlideShow/Template/img/pause.png");
        File newNext = new File(path + "/img/next.png");
        File newPrevious = new File(path+ "/img/previous.png");
        File newPlay = new File(path + "/img/play.png");
        File newPause = new File(path + "/img/pause.png");
        try {
            Files.copy(next.toPath(), newNext.toPath());
            Files.copy(previous.toPath(), newPrevious.toPath());
            Files.copy(play.toPath(), newPlay.toPath());
            Files.copy(pause.toPath(), newPause.toPath());
        }
        catch (FileAlreadyExistsException e1)
        {
                
        }
        catch (IOException e) {
            e.printStackTrace();
        } 
    }

}

