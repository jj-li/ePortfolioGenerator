package epm.model;

import java.util.ArrayList;

/**
 * @author Jia Li
 */
public class TextComponent {
    private String textType;
    private ArrayList<String> list;
    private String data;
    private String font;
    private String style = "Normal";
    private ArrayList<HyperlinkComponent> links;
    private int fontSize = 12;
    
    public TextComponent(String type, String text, String font) {
        textType = type;
        data = text;
        this.font = font;
        links = new ArrayList<HyperlinkComponent>();
    }
    
    public TextComponent(String type, ArrayList<String> datas, String font) {
        textType = type;
        list = datas;
        this.font = font;
        links = new ArrayList<HyperlinkComponent>();
    }
    
    public void setTextType(String newType) {
        textType = newType;
    }
    
    public void setList(ArrayList<String> newData) {
        list = newData;
    }
    
    public void setData(String data) {
        this.data = data;
    }
    
    public String getTextType() {
        return textType;
    }
    
    public ArrayList<String> getList() {
        return list;
    }
    
    public String getData() {
        return data;
    }
    
    public String getFont() {
        return font;
    }
    
    public void setFont(String newFont) {
        font = newFont;
    }
    
    public ArrayList<HyperlinkComponent> getHyperlinks() {
        return links;
    }
    
    public void addHyperlink(HyperlinkComponent component) {
        links.add(component);
    }
    
    public void setHyperlinks(ArrayList<HyperlinkComponent> links) {
        this.links = links;
    }
    
    public void removeHyperlink(int pos) {
        links.remove(pos);
    }
    
    public String getStyle() {
        return style;
    }
    
    public void setStyle(String style) {
        this.style = style;
    }
    
    public void setSize(int num) {
        fontSize = num;
    }
    
    public int getSize() {
        return fontSize;
    }
    
}
