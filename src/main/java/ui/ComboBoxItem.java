package ui;

import java.io.Serializable;

public class ComboBoxItem implements Serializable {
	
	private static final long serialVersionUID = -6497993382058846123L;
	
	private int id;
    private String label;

    public ComboBoxItem(String stringUnica) {
    	this.label = stringUnica;
    	int id  = Integer.parseInt(stringUnica.substring(0, stringUnica.indexOf("-")));
		this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getLabel() {
        return this.label;
    }
    
    @Override
    public String toString() {
    	return this.getLabel();
    }
}
