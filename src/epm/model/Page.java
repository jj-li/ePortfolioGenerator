/**
 * @coauthor Jia Li
 **/
package epm.model;

/**
 * This class represents a single slide in a slide show.
 * 
 * @author McKilla Gorilla & _____________
 */
public class Page {
    String title;
    String studentName;
     
    /**
     * Constructor, it initializes all slide data.
     * @param initImageFileName File name of the image.
     * 
     * @param initImagePath File path for the image.
     * 
     */
    public Page() {
        title = "";
        studentName = "";
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
    
    
    
}
