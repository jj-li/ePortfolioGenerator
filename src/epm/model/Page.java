/**
 * @coauthor Jia Li
 **/
package epm.model;

import epm.view.PageEditView;
import java.util.ArrayList;

/**
 * This class represents a single slide in a slide show.
 * 
 * @author McKilla Gorilla & _____________
 */
public class Page {
    PageEditView selectedPageEditView;
    String title;
    String studentName;
    ArrayList<TextComponent> textComponents;
    ArrayList<ImageComponent> imageComponents;
    ArrayList<VideoComponent> videoComponents;
    ArrayList<SlideShowComponent> slideShowComponents;
     
    /**
     * Constructor, it initializes all slide data.
     * @param initImageFileName File name of the image.
     * 
     * @param initImagePath File path for the image.
     * 
     */
    public Page() {
        title = "New Page";
        studentName = "";
        textComponents = new ArrayList<TextComponent>();
        imageComponents = new ArrayList<ImageComponent>();
        videoComponents = new ArrayList<VideoComponent>();
        slideShowComponents = new ArrayList<SlideShowComponent>();
       
    }
    
    public Page(String title, String studentName) {
        this.title = title;
        this.studentName = studentName;
    }
    
    // ACCESSOR METHODS
    public String getTitle() { return title; }
    public String getStudentName() { return studentName; }
    
    // MUTATOR METHODS
    public void setTitle(String newTitle) {
	title = newTitle;
    }
    
    public void setStudentName(String newStudentName) {
	studentName = newStudentName;
    }
    
    public void addTextComponent(TextComponent textComponent) {
        textComponents.add(textComponent);
    }
    
    public ArrayList<TextComponent> getTextComponents() {
        return textComponents;
    }
    
    public ArrayList<ImageComponent> getImageComponents() {
        return imageComponents;
    }
    
    public void addImageComponent(ImageComponent imageComponent) {
        imageComponents.add(imageComponent);
    }
    
    public ArrayList<VideoComponent> getVideoComponents() {
        return videoComponents;
    }
    
    public void addVideoComponent(VideoComponent videoComponent) {
        videoComponents.add(videoComponent);
    }
    
    public ArrayList<SlideShowComponent> getSlideShowComponents() {
        return slideShowComponents;
    }
    
    public void addSlideShowComponent(SlideShowComponent slideShowComponent) {
        slideShowComponents.add(slideShowComponent);
    }
    
    public void setPageEditView(PageEditView editView) {
        selectedPageEditView = editView;
    }
    
    public PageEditView getSelectedPageEditView() {
        return selectedPageEditView;
    }
    
    public void removeTextComponent(TextComponent textComponent) {
        for (int i = textComponents.size()-1; i >= 0; i--) {
            if (textComponents.get(i).equals(textComponent)) {
                textComponents.remove(i);
                break;
            }  
        }
    }
    
    public void removeImageComponent(ImageComponent imageComponent) {
        for (int i = imageComponents.size()-1; i >= 0; i--) {
            if (imageComponents.get(i).equals(imageComponent)) {
                imageComponents.remove(i);
                break;
            }  
        }
    }
    
    public void removeVideoComponent(VideoComponent videoComponent) {
        for (int i = videoComponents.size()-1; i >= 0; i--) {
            if (videoComponents.get(i).equals(videoComponent)) {
                videoComponent.getMediaView().getMediaPlayer().stop();
                videoComponents.remove(i);
                break;
            }  
        }
    }
    
    public void removeSlideShowComponent(SlideShowComponent slideShowComponent) {
        for (int i = slideShowComponents.size()-1; i >= 0; i--) {
            if (slideShowComponents.get(i).equals(slideShowComponent)) {
                slideShowComponents.remove(i);
                break;
            }  
        }
    }
}
