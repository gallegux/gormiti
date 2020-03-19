package gallegux.db.sqlbuilder.condiciones;

import java.util.ArrayList;
import java.util.List;


public abstract class CondicionVariable extends Condicion
{
	
	protected List<String> expresiones = new ArrayList<String>();

	
	
	protected CondicionVariable(String...expresiones) 
	{
		int numExpr = expresiones.length;
		
		for (String expr: expresiones) this.expresiones.add(expr);
	}
	

	
	public String getExpresion(int i) {
		return this.expresiones.get(i);
	}


	
	
}
