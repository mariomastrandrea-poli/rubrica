package it.mariomastrandrea.testrubrica.ui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import it.mariomastrandrea.testrubrica.models.Persona;
import it.mariomastrandrea.testrubrica.models.PersonaOrError;

public class SaveContactPanel {
	public enum Action {
		save, edit
	}
	
	private static final String saveContactLabel = "Salva";
	private static final String cancelLabel = "Annulla";
	
	private Window window;
	private RubricaTable rubricaTable;
	private ContactForm form;
	private Action action;
	private Persona showedContact;
	
	public SaveContactPanel(Window window, RubricaTable rubricaTable, 
			ContactForm form, Action action, Persona showedContact) {
		this.window = window;
		this.rubricaTable = rubricaTable;
		this.form = form;
		this.action = action;
		this.showedContact = showedContact;
	}
	
	public JPanel create() {
		JPanel buttonsPanel = new JPanel();
		
		JButton saveContactButton = new JButton(saveContactLabel);
		JButton cancelButton = new JButton(cancelLabel);
		
		saveContactButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	PersonaOrError personaOrError = form.getPersonDetailsToSave();
		    	
		    	if (personaOrError.getErrorMessage() != null) {
		    		// input error
		    		JOptionPane.showMessageDialog(null, personaOrError.getErrorMessage());
		    		return;
		    	}
		    	
		    	// we can proceed
		    	
		    	switch (action) {
			    	case save:
			    		rubricaTable.createNewContact(personaOrError.getContact());
			    		break;
			    		
			    	case edit:
			    		if (!personaOrError.getContact().isIdenticalTo(showedContact)) {
			    			System.out.println("In");
			    			
			    			// edit just if the new details are not identical to the old ones
			    			rubricaTable.editContact(personaOrError.getContact());
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
		
		buttonsPanel.add(saveContactButton);
		buttonsPanel.add(cancelButton);
		
		return buttonsPanel;
	}
}
