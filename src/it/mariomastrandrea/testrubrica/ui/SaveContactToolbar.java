package it.mariomastrandrea.testrubrica.ui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import it.mariomastrandrea.testrubrica.models.Persona;

public class SaveContactToolbar {
	public enum Action {
		save, edit
	}
	
	private static final String saveContactLabel = "Salva";
	private static final String cancelLabel = "Annulla"; 
	
	private static final String saveContactIconPath = "icons/save_icon.png";
	private static final String cancelIconPath = "icons/cancel_icon.png";
	
	private Window window;
	private RubricaTable rubricaTable;
	private ContactForm form;
	private Action action;
	private Persona showedContact;
	
	
	public SaveContactToolbar(Window window, RubricaTable rubricaTable, 
			ContactForm form, Action action, Persona showedContact) {
		this.window = window;
		this.rubricaTable = rubricaTable;
		this.form = form;
		this.action = action;
		this.showedContact = showedContact;
	}
	
	public JToolBar create() {
		JToolBar buttonsToolbar = new JToolBar();
		
		// create buttons 
		
		ImageIcon saveContactIcon = new ImageIcon(saveContactIconPath);
		ImageIcon cancelIcon = new ImageIcon(cancelIconPath);
		
		JButton saveContactButton = new JButton(saveContactLabel, saveContactIcon);
		JButton cancelButton = new JButton(cancelLabel, cancelIcon);
		
		// add on click listeners
		
		saveContactButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	Persona contactToSave = form.getPersonDetailsToSave();
		    	
		    	if (contactToSave == null) {
		    		// input error
		    		return;
		    	}
		    	
		    	// we can proceed
		    	
		    	switch (action) {
			    	case save:
			    		rubricaTable.createNewContact(contactToSave);
			    		break;
			    		
			    	case edit:
			    		if (!contactToSave.isIdenticalTo(showedContact)) {			    			
			    			// edit just if the new details are not identical to the old ones
			    			rubricaTable.editContact(contactToSave);
			    		}
			    		break;
		    	}
		        
		        // close window
		        window.dispose();
		    }
		});
		
		cancelButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        window.dispose();
		    }
		});
		
		// add buttons to the toolBar
		
		buttonsToolbar.add(saveContactButton);
		buttonsToolbar.add(cancelButton);
		
		return buttonsToolbar;
	}
}
