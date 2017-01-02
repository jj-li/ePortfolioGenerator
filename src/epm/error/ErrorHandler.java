package epm.error;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import epm.LanguagePropertyType;
import static epm.StartupConstants.PATH_ICONS;
import static epm.StartupConstants.STYLE_SHEET_UI;
import static epm.StartupConstants.WINDOWS_ICON;
import epm.view.EPortfolioMakerView;

/**
 * This class provides error messages to the user when the occur. Note
 * that error messages should be retrieved from language-dependent XML files
 * and should have custom messages that are different depending on
 * the type of error so as to be informative concerning what went wrong.
 * 
 * @author Jia Li
 */
public class ErrorHandler {
    // APP UI
    private EPortfolioMakerView ui;
    
    // KEEP THE APP UI FOR LATER
    public ErrorHandler(EPortfolioMakerView initUI) {
	ui = initUI;
    }
    
    /**
     * This method provides all error and success feedback. It gets the feedback text,
     * which changes depending on the type of error, and presents it to
     * the user in a dialog box.
     */
    public void processError(LanguagePropertyType error, String errorDialogTitle)
    {
        // GET THE FEEDBACK TEXT
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String errorFeedbackText = props.getProperty(error);
        if (errorFeedbackText == null)
            errorFeedbackText = errorDialogTitle;
        // POP OPEN A DIALOG TO DISPLAY TO THE USER
        Button btOK = new Button("OK");
        Text text = new Text(errorFeedbackText);
        GridPane pane = new GridPane();
        
        pane.getStyleClass().add("error_box");
        pane.add(text, 1, 1);
        pane.add(btOK, 1, 2);
        Scene scene = new Scene(pane, 450, 125);
        scene.getStylesheets().add(STYLE_SHEET_UI);
        Stage stage = new Stage();
        btOK.setOnAction(e -> {
            stage.close();
        });
        stage.setScene(scene);
        stage.getIcons().add(new Image("file:" + PATH_ICONS + WINDOWS_ICON));
        stage.setTitle(errorDialogTitle);
        stage.showAndWait();
    }    
}
