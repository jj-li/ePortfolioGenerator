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
import epm.model.Page;
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
                ePortfolio.addPage();
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
    public void handleLoadSlideShowRequest() {
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
    public boolean handleSaveSlideShowRequest() {
        try {
	    // GET THE SLIDE SHOW TO SAVE
	    EPortfolioModel slideShowToSave = ui.getEPortfolio();
            // SAVE IT TO A FILE
            slideShowIO.saveSlideShow(slideShowToSave);

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
    
    public void handleViewSlideShowRequest()
    {
        handleSaveSlideShowRequest();
        EPortfolioModel slideShowToShow = ui.getEPortfolio();
        
        Boolean siteDirectory = new File("sites/" + slideShowToShow.getTitle()).mkdirs();
        Boolean cssDirectory = new File("sites/" + slideShowToShow.getTitle() +"/css").mkdirs();
        Boolean jsDirectory = new File("sites/" + slideShowToShow.getTitle() +"/js").mkdirs();
        Boolean imgDirectory = new File("sites/" + slideShowToShow.getTitle() +"/img").mkdirs();
        File imgs = new File("sites/" + slideShowToShow.getTitle() +"/img");
        //TO FIX
        /*
        if (imgs.exists()) {
            for (File files : imgs.listFiles()) {
                boolean fileExists = false;
                for (Page s : slideShowToShow.getPages()) {
                    if(files.toString().contains(s.getImageFileName()) == true)
                        fileExists = true;
                }
                if (!fileExists)
                    files.delete();
            }
        }  
        */
        String jsString = "";
        String jsPath = "sites/Template/js/TemplateJS.js";
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
        String partThree = slideShowToShow.getTitle();
        String completeJS = partOne + partThree + partTwo;
        byte[] bytes = completeJS.getBytes();
        try {
            OutputStream out = new BufferedOutputStream(new FileOutputStream("sites/" + slideShowToShow.getTitle() +"/js/TemplateJS.js"),1024);
            out.write(bytes);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        File htmlFile = new File("sites/Template/index.html");
        File newHTMLFile = new File("sites/" + slideShowToShow.getTitle() + "/index.html");
        try {
            Files.copy(htmlFile.toPath(), newHTMLFile.toPath());
        }
        catch (FileAlreadyExistsException e4)
        {
                
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        
        File cssFile = new File("sites/Template/css/TemplateCSS.css");
        File newCSSFile = new File("sites/" + slideShowToShow.getTitle() + "/css/TemplateCSS.css");
        try {
            Files.copy(cssFile.toPath(), newCSSFile.toPath());
        }
        catch (FileAlreadyExistsException e3)
        {
                
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        
        File jsonFile = new File(PATH_SLIDE_SHOWS + SLASH + slideShowToShow.getTitle() + JSON_EXT);
        File newJSONFile = new File("sites/" + slideShowToShow.getTitle() + "/" + slideShowToShow.getTitle() + JSON_EXT);
        if (newJSONFile.exists())
            newJSONFile.delete();
        try {
            Files.copy(jsonFile.toPath(), newJSONFile.toPath());
        }
        catch (FileAlreadyExistsException e3)
        {
                
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        
        copyImages(slideShowToShow);
        ui.viewSlideShow(slideShowToShow);
    }
    
    private static void copyImages(EPortfolioModel slideShowToShow)
    {
        for (Page s : slideShowToShow.getPages())
        {
            //TO FIX
            /*
            String imagePath = s.getImagePath() + SLASH + s.getImageFileName();
            File image = new File(imagePath);
            File newImage = new File("sites/" + slideShowToShow.getTitle() + "/img/" + s.getImageFileName());
            try {
                Files.copy(image.toPath(), newImage.toPath());
            }
            catch (FileAlreadyExistsException e1)
            {
                
            }
            catch (IOException e) {
                e.printStackTrace();
            } 
            */
        }
        
        File next = new File("sites/Template/img/next.png");
        File previous = new File("sites/Template/img/previous.png");
        File play = new File("sites/Template/img/play.png");
        File pause = new File("sites/Template/img/pause.png");
        File newNext = new File("sites/" + slideShowToShow.getTitle() + "/img/next.png");
        File newPrevious = new File("sites/" + slideShowToShow.getTitle() + "/img/previous.png");
        File newPlay = new File("sites/" + slideShowToShow.getTitle() + "/img/play.png");
        File newPause = new File("sites/" + slideShowToShow.getTitle() + "/img/pause.png");
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
            slideShowIO.saveSlideShow(slideShow);
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
		EPortfolioModel slideShowToLoad = ui.getEPortfolio();
                slideShowIO.loadSlideShow(slideShowToLoad, selectedFile.getAbsolutePath());
                
                ui.reloadSlideShowPane(slideShowToLoad);
                saved = true;
                ui.updateToolbarControls(true);
            } catch (Exception e) {
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
}

