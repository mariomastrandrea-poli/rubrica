package it.mariomastrandrea.testrubrica;

import javax.swing.SwingUtilities;

import it.mariomastrandrea.personal.jsonparser.JSONparser;
import it.mariomastrandrea.personal.jsonparser.datatypes.JSONcomponent;
import it.mariomastrandrea.personal.jsonparser.datatypes.JSONobject;
import it.mariomastrandrea.testrubrica.models.DbConfig;
import it.mariomastrandrea.testrubrica.repository.RubricaFileRepository;
import it.mariomastrandrea.testrubrica.repository.RubricaLocalRelationalDBRepository;
import it.mariomastrandrea.testrubrica.repository.RubricaRepository;


public class TestRubrica {
	static final String applicationName = "Rubrica";
	static final String contactsFilename = "informazioni.txt";
	static final String usersFilename = "credentials.txt";
	static final String configFilePath = "config.json";
	
	
	public static void main(String[] args) {
		// read config file
		JSONobject configObject = readConfig();
		if (configObject == null) { return; }
		
		JSONobject mysqlConfig = (JSONobject)configObject.getValue("mysql");
		JSONobject appConfig = (JSONobject)configObject.getValue("app");
		
		if (mysqlConfig == null || appConfig == null) {
			System.err.println("An unexpected error occurred reading config file properties");
			return;
		}
		
		boolean dbRepositoryFlag = appConfig.getBool("db_repository");		
		boolean fileRepositoryFlag = appConfig.getBool("file_repository");

		// create dependencies
		RubricaRepository repository;
		
		if (dbRepositoryFlag) {
			DbConfig dbConfig = new DbConfig(
				mysqlConfig.getString("username"),
				mysqlConfig.getString("password"),
				mysqlConfig.getString("ip_server"),
				mysqlConfig.getString("port"),
				mysqlConfig.getString("db_name")
			);
			
			// set a local (relational) DB repository
			repository = new RubricaLocalRelationalDBRepository(dbConfig);
			
		}
		else if (fileRepositoryFlag) {
			// set a file repository
			repository = new RubricaFileRepository(contactsFilename, usersFilename);
		}
		else {
			// no repository selected -> terminate execution
			System.err.println("Error: you must specify one type of repository in the config.json file");
			return;
		}
		
		// create and initialize app manager 
		RubricaManager manager = new RubricaManager(applicationName, repository);
		manager.init();
		
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	manager.showLoginWindow();
            }
        });
    }
	
	private static JSONobject readConfig() {
		JSONparser parser = new JSONparser();
		
		JSONcomponent configObject = parser.parseFile(configFilePath);
		if (configObject == null) {
			System.err.println("An unexpected error occurred reading config file");
			return null;
		}
		
		return (JSONobject)configObject;
	}
}

