/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epm.model;

import java.util.ArrayList;

/**
 *
 * @author icysniper
 */
public class HyperlinkComponent {
    private String completeText;
    private String selectedText;
    private String url;
    private int index;
    private ArrayList<String> items;
    
    public HyperlinkComponent(String complete, String select, String url) {
        completeText = complete;
        selectedText = select;
        this.url = url;
    }
    
    public HyperlinkComponent(int index, ArrayList<String> items, String url) {
        this.index = index;
        this.items = items;
        this.url = url;
    }
    
    public int getIndex() {
        return index;
    }
    
    public String getIndexItem() {
        return items.get(index);
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public String getCompleteText() {
        return completeText;
    }
    
    public String getSelectedText() {
        return selectedText;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setCompleteText(String text) {
        completeText = text;
    }
    
    public void setSelectedText(String select) {
        selectedText = select;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
}
