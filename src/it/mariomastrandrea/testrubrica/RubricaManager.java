package it.mariomastrandrea.testrubrica;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import it.mariomastrandrea.testrubrica.models.Persona;
import it.mariomastrandrea.testrubrica.models.User;
import it.mariomastrandrea.testrubrica.repository.RubricaRepository;
import it.mariomastrandrea.testrubrica.ui.ContactForm;
import it.mariomastrandrea.testrubrica.ui.ContactsNavigator;
import it.mariomastrandrea.testrubrica.ui.ContactsTableInteractionToolbar;
import it.mariomastrandrea.testrubrica.ui.LoginForm;
import it.mariomastrandrea.testrubrica.ui.LoginNavigator;
import it.mariomastrandrea.testrubrica.ui.LoginToolbar;
import it.mariomastrandrea.testrubrica.ui.RubricaTable;
import it.mariomastrandrea.testrubrica.ui.SaveContactToolbar;
import it.mariomastrandrea.testrubrica.ui.SaveContactToolbar.Action;

public class RubricaManager implements ContactsNavigator, LoginNavigator {
	private static final String newContactWindowLabel = "Aggiungi nuovo contatto";
	private static final String editContactWindowLabel = "Modifica contatto";
	
	private String appName;
	private RubricaRepository repository;
	private RubricaTable rubricaTable;
	private JFrame currentFrame;
	private User user;
	
	
	public RubricaManager(String appName, RubricaRepository repository) {
		this.appName = appName;
		this.repository = repository;
	}
	
	public boolean init() {
		boolean repositoryContactsSuccessfullyInitialized = this.repository.initAndLoadContacts();
		boolean repositoryUsersSuccessfullyInitialized = this.repository.initAndLoadUsers();
				
		return repositoryContactsSuccessfullyInitialized && repositoryUsersSuccessfullyInitialized;
	}
	
	public void showLoginWindow() {
		JFrame loginFrame = new JFrame(String.format("%s - Login", this.appName));
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.currentFrame = loginFrame;
		
		// add login form
		LoginForm form = new LoginForm(this.repository);
		loginFrame.add(form.create());
		
		// add login toolBar
		LoginToolbar toolbar = new LoginToolbar(form, this);
		loginFrame.add(toolbar.create(), BorderLayout.SOUTH);
		
		loginFrame.pack();
		loginFrame.setVisible(true);
	}
	
	@Override
	public void navigateToContactsTable(User user) {
		this.user = user;
		
		JFrame oldFrame = this.currentFrame;
		JFrame contactFrame = new JFrame(this.appName);
        this.currentFrame = contactFrame;
        contactFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // create and add contacts table
        RubricaTable rubricaTable = new RubricaTable(this.repository);
        this.rubricaTable = rubricaTable;
        
        contactFrame.add(new JScrollPane(rubricaTable.create()));
        
        // create and add buttons toolBar
        ContactsTableInteractionToolbar interactionButtonsToolbar = 
        		new ContactsTableInteractionToolbar(rubricaTable, this);
                
        contactFrame.add(interactionButtonsToolbar.create(), BorderLayout.SOUTH);
        
        // resize and show
        contactFrame.pack();
        contactFrame.setVisible(true);
        
        // close old window
        oldFrame.dispose();
		
		return;
	}
	
	@Override
	public void navigateToEditContact(Persona contact) {
		// create the new window in overlay
		String windowLabel = contact == null ? newContactWindowLabel : editContactWindowLabel;
		JDialog secondaryWindow = new JDialog(this.currentFrame, windowLabel);
        
        // add form
		ContactForm contactForm = new ContactForm(contact);
		secondaryWindow.add(contactForm.create(), BorderLayout.NORTH);
		
		// add form buttons
		SaveContactToolbar editContactButtons = new SaveContactToolbar(
			secondaryWindow,
			this.rubricaTable,
			contactForm,
			contact == null ? Action.save : Action.edit,
			contact
		);
		
		secondaryWindow.add(editContactButtons.create(), BorderLayout.SOUTH);

		secondaryWindow.pack();
        
        // put it on top
		secondaryWindow.setSize(this.currentFrame.getSize());
		secondaryWindow.setLocation(this.currentFrame.getLocation());

        // show the new window
		secondaryWindow.setVisible(true);
	}
	
	@Override
	public void navigateToAddContact() {
		this.navigateToEditContact(null);
	}
}
