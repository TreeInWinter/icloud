package org.lobobrowser.html.renderer;


import javax.swing.*;
import java.util.*;

import org.lobobrowser.html.domimpl.*;
import org.lobobrowser.util.gui.WrapperLayout;
import org.w3c.dom.Node;
import org.w3c.dom.html2.*;

class InputSelectControl extends BaseInputControl {
	private static final OptionFilter OPTION_FILTER = new OptionFilter();
	private final JComboBox comboBox;
	
	public InputSelectControl(HTMLBaseInputElement modelNode) {
		super(modelNode);
		this.setLayout(WrapperLayout.getInstance());
		JComboBox comboBox = new JComboBox();
		this.add(comboBox);
		this.comboBox = comboBox;
	}
	
	public void reset(int availWidth, int availHeight) {
		super.reset(availWidth, availHeight);
		HTMLElementImpl selectElement = this.controlElement;
		ArrayList list = selectElement.getDescendents(OPTION_FILTER);
		Iterator i = list.iterator();
		JComboBox comboBox = this.comboBox;
		comboBox.removeAllItems();
		while(i.hasNext()) {
			HTMLOptionElement option = (HTMLOptionElement) i.next();
			OptionItem item = new OptionItem(option); 
			comboBox.addItem(item);
			if(option.getDefaultSelected()) {
				comboBox.setSelectedItem(item);
			}
		}		
	}
	
	public String getValue() {
		OptionItem item = (OptionItem) this.comboBox.getSelectedItem();
		if(item == null) {
			return null;
		}
		else {
			return item.getValue();
		}
	}

	public boolean getMultiple() {
		return false;
	}
	
	public void setMultiple(boolean value) {
		if(value) {
			//TODO ?
			throw new UnsupportedOperationException();
		}
	}
	
	public int getSelectedIndex() {
		return this.comboBox.getSelectedIndex();
	}
	
	public void setSelectedIndex(int value) {
		this.comboBox.setSelectedIndex(value);
	}
	
	public int getVisibleSize() {
		return this.comboBox.getMaximumRowCount();
	}
	
	public void setVisibleSize(int value) {
		this.comboBox.setMaximumRowCount(value);
	}
	
	public void resetInput() {
		this.comboBox.setSelectedIndex(-1);
	}
	
	private static class OptionFilter implements NodeFilter {
		public boolean accept(Node node) {
			return node instanceof HTMLOptionElement;
		}
	}

	private static class OptionItem {
		private final HTMLOptionElement option;
		private final String caption;
		
		public OptionItem(HTMLOptionElement option) {
			this.option = option;
			String label = option.getLabel();
			if(label == null) {
				this.caption = option.getText();
			}
			else {
				this.caption = label;
			}
		}
		
		public String toString() {
			return this.caption;
		}
		
		public String getValue() {
			String value = this.option.getValue();
			if(value == null) {
				value = this.option.getText();
			}
			return value;
		}
	}
}
