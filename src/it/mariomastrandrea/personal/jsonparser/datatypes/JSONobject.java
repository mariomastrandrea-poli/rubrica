package it.mariomastrandrea.personal.jsonparser.datatypes;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A concrete implementation of a generic JSON object. It represent a concrete JSON
 * object, a.k.a. an associative array: a list of key-value pairs, where the value can
 * be any one of the possible JSON data types 
 * @author mariomastrandrea
 */
public class JSONobject extends JSONcomponent {
	private LinkedHashMap<String, JSONcomponent> keyValuePairProperties;
	
	
	public JSONobject() {
		this.keyValuePairProperties = new LinkedHashMap<>();
	}
	
	@Override
	public String print(int indentation) {	
		StringBuilder sb = new StringBuilder();
		
		for(Map.Entry<String, JSONcomponent> pair : keyValuePairProperties.entrySet()) {
			
			String key = pair.getKey();
			String value = pair.getValue().print(indentation+1);
			
			if(sb.length() > 0)
				sb.append(",\n");
			
			for(int i=0; i<=indentation; i++)
				sb.append("\t");
			
			sb.append(String.format("\"%s\": %s", key, value));
		}
		
		sb.insert(0, "{\n").append("\n");
		
		for(int i=0; i<indentation; i++)
			sb.append("\t");
		
		return sb.append("}").toString();
	}
	
	public void addProperty(String key, JSONcomponent value) {
		this.keyValuePairProperties.put(key, value);
	}
	
	public JSONcomponent getValue(String key) {
		if (!this.keyValuePairProperties.containsKey(key)) {
			return null;
		}
		
		return this.keyValuePairProperties.get(key);
	}

	public Boolean getBool(String key) {
		return ((JSONbool)this.keyValuePairProperties.get(key)).get();
	}
	
	public String getString(String key) {
		return ((JSONstring)this.keyValuePairProperties.get(key)).get();
	}
	
	@Override
	public JSONcomponent removeProperties(String... properties) {
		for(String propertyName : this.keyValuePairProperties.keySet()) {
			
			//retrieve object
			JSONcomponent propertyValue = this.keyValuePairProperties.get(propertyName);
			
			//removing properties
			propertyValue = propertyValue.removeProperties(properties);
			
			//update references
			this.keyValuePairProperties.put(propertyName, propertyValue);
		}
		
		if(this.keyValuePairProperties.size() != 1)
			return this;
		
		//here, there is only 1 property
		Entry<String, JSONcomponent> entry = 
				this.keyValuePairProperties.entrySet().iterator().next();
		String propertyName = entry.getKey();
		JSONcomponent propertyValue = entry.getValue();
		
		// if this property is among those that must be removed, its value object is returned instead of this object
		for(String propertyToRemove : properties) {
			if(propertyName.equals(propertyToRemove))
				return propertyValue;
		}
		
		return this;
	}
}
