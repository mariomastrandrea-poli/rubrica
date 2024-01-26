package it.mariomastrandrea.personal.jsonparser.datatypes;

/**
 * A concrete implementation of a generic JSON object. It represent a 'null' value
 * @author mariomastrandrea
 */
public class JSONnull extends JSONcomponent {	
	public JSONnull() {	}
	
	@Override
	public String print(int indentation) {
		return "null";
	}

	@Override
	public JSONcomponent removeProperties(String... properties) {
		return this;
	}
}

