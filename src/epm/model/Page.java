/**
 * @coauthor Jia Li
 **/
package epm.model;

import java.util.ArrayList;

/**
 * This class represents a single slide in a slide show.
 * 
 * @author McKilla Gorilla & _____________
 */
public class Page {
    String title;
    String studentName;
    ArrayList<TextComponent> textComponents;
    ArrayList<ImageComponent> imageComponents;
     
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
        
        //Hard coded data.
        TextComponent paragraph = new TextComponent("paragraph", "The oldest classical Greek and Latin writing had little or no space between words, and could be written in boustrophedon (alternating directions). Over time, text direction (left to right) became standardized, and word dividers and terminal punctuation became common. The first way to divide sentences into groups was the original paragraphos, similar to an underscore at the beginning of the new group.[3] The Greek paragraphos evolved into the pilcrow (¶), which in English manuscripts in the Middle Ages can be seen inserted inline between sentences. The hedera leaf has also been used in the same way.");
        ArrayList<String> data = new ArrayList<String>();
        data.add("item 1");
        data.add("item 2");
        data.add("item 3");
        TextComponent list = new TextComponent("list", data);
        TextComponent header = new TextComponent("header", "This is a dummy header.");
        textComponents.add(paragraph);
        textComponents.add(list);
        textComponents.add(header);
        
        String imagePath = "images/slide_show_images/ArchesUtah.jpg";
        ImageComponent image = new ImageComponent(imagePath, "neither", 200, 200);
        imageComponents.add(image);
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
    
}
