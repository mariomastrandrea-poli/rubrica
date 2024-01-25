package it.mariomastrandrea.testrubrica.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import it.mariomastrandrea.testrubrica.models.Persona;

public class ContactsTableInteractionPanel {
	private static final String newContactLabel = "Nuovo";
	private static final String editContactLabel = "Modifica";
	private static final String deleteContactLabel = "Elimina";

	private static final String editContactError = "Seleziona prima un contatto per effettuare una modifica!";
	private static final String deleteContactError = "Seleziona prima un contatto per eliminarlo!";

	
	private RubricaTable rubricaTable;
	private ContactsNavigator navigator;
	
	public ContactsTableInteractionPanel(RubricaTable rubricaTable, ContactsNavigator navigator) {
		this.rubricaTable = rubricaTable;
		this.navigator = navigator;
	}
	
	public JPanel create() {
		JPanel buttonsPanel = new JPanel();
		
		JButton newContactButton = new JButton(newContactLabel);
		JButton editContactButton = new JButton(editContactLabel);
		JButton deleteContactButton = new JButton(deleteContactLabel);
	
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

		buttonsPanel.add(newContactButton);
		buttonsPanel.add(editContactButton);
		buttonsPanel.add(deleteContactButton);
		
		return buttonsPanel;
	}
}
