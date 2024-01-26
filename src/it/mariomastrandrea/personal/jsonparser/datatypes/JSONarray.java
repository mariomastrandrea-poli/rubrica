package it.mariomastrandrea.personal.jsonparser.datatypes;

import java.util.ArrayList;
import java.util.List;

/**
 * A concrete implementation of a generic JSON object. It represent a JSON array, i.e. an indexed array
 * @author mariomastrandrea
 *
 */
public class JSONarray extends JSONcomponent {
	private List<JSONcomponent> array;
	
	
	public JSONarray() {
		this.array = new ArrayList<>();
	}
	
	@Override
	public String print(int indentation) {
		StringBuilder sb = new StringBuilder();
		
		for(var component : this.array) {
			String printedComponent = component.print(indentation+1);
			
			if(sb.length() > 0)
				sb.append(",\n");
			
			for(int i=0; i<=indentation; i++)
				sb.append("\t");
			
			sb.append(printedComponent);
		}
		
		sb.insert(0, "[\n").append("\n");
		
		for(int i=0; i<indentation; i++)
			sb.append("\t");
		
		return sb.append("]").toString();
	}
	
	public boolean add(JSONcomponent component) {
		return this.array.add(component);
	}

	@Override
	public JSONcomponent removeProperties(String... properties) {
		for(int i=0; i<this.array.size(); i++) {
			JSONcomponent element = this.array.remove(i);
			element = element.removeProperties(properties);
			this.array.add(i, element);
		}
		
		return this;
	}
}
