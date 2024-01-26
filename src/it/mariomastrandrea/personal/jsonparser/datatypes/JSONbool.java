package it.mariomastrandrea.personal.jsonparser.datatypes;

/**
 * A concrete implementation of a generic JSON object. 
 * It represent a boolean value: either 'true' or 'false'
 * @author mariomastrandrea
 *
 */
public class JSONbool extends JSONcomponent {
	private boolean bool;
	
	
	public JSONbool(boolean bool) {
		this.bool = bool;
	}
	
	public boolean get() { return this.bool; }

	@Override
	public String print(int indentation) {
		return this.bool ? "true" : "false";
	}

	@Override
	public JSONcomponent removeProperties(String... properties) {
		return this;
	}
}
