package it.mariomastrandrea.testrubrica;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import it.mariomastrandrea.testrubrica.models.Persona;
import it.mariomastrandrea.testrubrica.repository.RubricaRepository;
import it.mariomastrandrea.testrubrica.ui.ContactForm;
import it.mariomastrandrea.testrubrica.ui.ContactsNavigator;
import it.mariomastrandrea.testrubrica.ui.ContactsTableInteractionPanel;
import it.mariomastrandrea.testrubrica.ui.RubricaTable;
import it.mariomastrandrea.testrubrica.ui.SaveContactPanel;
import it.mariomastrandrea.testrubrica.ui.SaveContactPanel.Action;

public class RubricaManager implements ContactsNavigator {
	private String appName;
	private RubricaRepository repository;
	private RubricaTable rubricaTable;
	private JFrame mainFrame;
	
	private static final String newContactWindowLabel = "Aggiungi nuovo contatto";
	private static final String editContactWindowLabel = "Modifica contatto";

	
	public RubricaManager(String appName, RubricaRepository repository) {
		this.appName = appName;
		this.repository = repository;
	}
	
	public boolean init() {
		boolean repositorySuccessfullyInitialized = this.repository.initAndLoadContacts();
				
		return repositorySuccessfullyInitialized;
	}
	
	public void showContactsWindow() {    	
        JFrame frame = new JFrame(this.appName);
        this.mainFrame = frame;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // create contacts table
        RubricaTable rubricaTable = new RubricaTable(this.repository);
        this.rubricaTable = rubricaTable;
        JScrollPane scrollPane = new JScrollPane(rubricaTable.create());
        
        // create buttons
        ContactsTableInteractionPanel interactionButtonsPanel = 
        		new ContactsTableInteractionPanel(rubricaTable, this);
        
        JPanel buttonsPanel = interactionButtonsPanel.create();
        
        // add components
        frame.add(scrollPane);
        frame.add(buttonsPanel, BorderLayout.SOUTH);
        
        // resize and show
        frame.pack();
        frame.setVisible(true);
    }
	
	@Override
	public void navigateToEditContact(Persona contact) {
		// create the new window in overlay
		String windowLabel = contact == null ? newContactWindowLabel : editContactWindowLabel;
		JDialog secondaryWindow = new JDialog(this.mainFrame, windowLabel);
        
        // add form
		ContactForm contactForm = new ContactForm(contact);
		secondaryWindow.add(contactForm.create());
		
		// add form buttons
		SaveContactPanel editContactButtons = new SaveContactPanel(
			secondaryWindow,
			this.rubricaTable,
			contactForm,
			contact == null ? Action.save : Action.edit,
			contact
		);
		
		secondaryWindow.add(editContactButtons.create(), BorderLayout.SOUTH);

		secondaryWindow.pack();
        
        // put it on top
		secondaryWindow.setSize(this.mainFrame.getSize());
		secondaryWindow.setLocation(this.mainFrame.getLocation());

        // show the new window
		secondaryWindow.setVisible(true);
	}
	
	@Override
	public void navigateToAddContact() {
		this.navigateToEditContact(null);
	}
}
