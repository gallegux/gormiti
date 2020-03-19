package gallegux.db.sqlbuilder.condiciones;

import java.util.Iterator;
import java.util.List;



public abstract class Condicion 
{
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		addTo(sb);
		
		return sb.toString();
	}
	
	
	
	public abstract void addTo(StringBuilder sb);
	

	
	protected void add(StringBuilder sb, List<Condicion> condiciones, String separador)
	{
		int numCondiciones = condiciones.size();
		
		if (numCondiciones > 0) {
			sb.append('(');
		}
		
		if (numCondiciones == 1 ) {
			condiciones.get(0).addTo(sb);
		}
		else if (numCondiciones > 1) {
			Iterator<Condicion> i = condiciones.iterator();
			
			i.next().addTo(sb);
			
			while (i.hasNext()) {
				sb.append(separador);
				i.next().addTo(sb);
			}
		}
		
		if (numCondiciones > 0) {
			sb.append(')');
		}
	}
	
	
}
