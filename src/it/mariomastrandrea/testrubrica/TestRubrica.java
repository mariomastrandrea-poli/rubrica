package it.mariomastrandrea.testrubrica;

import javax.swing.SwingUtilities;

import it.mariomastrandrea.testrubrica.repository.RubricaFileRepository;
import it.mariomastrandrea.testrubrica.repository.RubricaRepository;


public class TestRubrica {
	static final String applicationName = "Rubrica";
	static final String repositoryFilename = "informazioni.txt";
	
	public static void main(String[] args) {		
		// create dependencies
		RubricaRepository repository = new RubricaFileRepository(repositoryFilename);
		
		// create and initialize app manager 
		RubricaManager manager = new RubricaManager(applicationName, repository);
		boolean appSuccessfullyInitialized = manager.init();
		
		if (!appSuccessfullyInitialized) {
			System.err.println("An error occurred during app initialization");
			return;
		}
		
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	manager.showContactsWindow();
            }
        });
    }
}

