package it.mariomastrandrea.testrubrica.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import it.mariomastrandrea.testrubrica.models.Persona;

public class ContactsTableInteractionToolbar {	
	private static final String newContactLabel = "Nuovo";
	private static final String editContactLabel = "Modifica";
	private static final String deleteContactLabel = "Elimina";
	
	private static final String addContactIconPath = "icons/add_icon.png";
	private static final String editContactIconPath = "icons/edit_icon.png";
	private static final String deleteContactIconPath = "icons/delete_icon.png";


	private static final String editContactError = "Seleziona prima un contatto per effettuare una modifica!";
	private static final String deleteContactError = "Seleziona prima un contatto per eliminarlo!";

	
	private RubricaTable rubricaTable;
	private ContactsNavigator navigator;
	
	public ContactsTableInteractionToolbar(RubricaTable rubricaTable, ContactsNavigator navigator) {
		this.rubricaTable = rubricaTable;
		this.navigator = navigator;
	}
	
	public JToolBar create() {
		JToolBar buttonsToolbar = new JToolBar();
		
		// create action buttons
		
        ImageIcon addContactIcon = new ImageIcon(addContactIconPath); 
        ImageIcon editContactIcon = new ImageIcon(editContactIconPath); 
        ImageIcon deleteContactIcon = new ImageIcon(deleteContactIconPath); 

		JButton newContactButton = new JButton(newContactLabel, addContactIcon);
		JButton editContactButton = new JButton(editContactLabel, editContactIcon);
		JButton deleteContactButton = new JButton(deleteContactLabel, deleteContactIcon);

		// add click listeners
		
		newContactButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        navigator.navigateToAddContact();
		    }
		});
		
		editContactButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	Persona selectedContact = rubricaTable.getSelectedContact();
		    	
		    	if (selectedContact == null) {
		    		// show dialog error
		    		JOptionPane.showMessageDialog(null, editContactError);
		    		return;
		    	}
		    	
		    	navigator.navigateToEditContact(selectedContact);		    	
		    }
		});
		
		deleteContactButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	Persona selectedContact = rubricaTable.getSelectedContact();
		    	if (selectedContact == null) {
		    		// show dialog error
		    		JOptionPane.showMessageDialog(null, deleteContactError);
		    		return;
		    	}
		    	
		    	int userSelection = JOptionPane.showConfirmDialog(
		    		null,
		    		String.format(
		    			"Sei sicuro di voler eliminare %s %s?",
		    			selectedContact.getName(),
		    			selectedContact.getSurname()
		    		)
		    	);
		    	
		    	if (userSelection == JOptionPane.YES_OPTION) {
		    		// delete person
		    		rubricaTable.deleteContact(selectedContact);
		    	}
		    }
		});

		// add buttons to the toolBar
		buttonsToolbar.add(newContactButton);
		buttonsToolbar.add(editContactButton);
		buttonsToolbar.add(deleteContactButton);
				
		return buttonsToolbar;
	}
}
