package gallegux.db.sqlbuilder.condiciones;



/**
 * Para enviar la condicon entera
 *
 */
public class CondicionCompleta extends Condicion 
{
	
	private String exprexion;
	
	
	public CondicionCompleta(String expresion)
	{
		super();
		
		this.exprexion = expresion;
	}
	

	

	@Override
	public void addTo(StringBuilder sb) {
		sb.append(exprexion);
	}
	
	

}
