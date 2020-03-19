package gallegux.db.sqlbuilder.condiciones;



/**
 * Condicion en la que intervienen dos operandos y un elemento de comparacion,
 * en el formato
 * OPERANDO OPERADOR OPERANDO
 * Se considera un array como un operando. En caso de que la BBDD no soporte esa operacion
 * debería utilzarse la clase CondicionIN. Idem para "NOT INT"
 *
 */
public class CondicionExprOpExpr extends Condicion 
{
	
	public final static String IGUAL_QUE = "=";
	public final static String MENOR_QUE = "<";
	public final static String MAYOR_QUE = ">";
	public final static String LIKE = "LIKE";
	public final static String IN = "IN";
	public final static String NOT_IN = "NOT IN";
	
	
	private String expr1, operador, expr2;
	
	
	public CondicionExprOpExpr(String expr1, String operador, String expr2)
	{
		super();
		
		this.expr1 = expr1;
		this.operador = operador;
		this.expr2 = expr2;
	}
	

	public CondicionExprOpExpr(String expr1)
	{
		super();
		
		this.expr1 = expr1;
	}
	

	@Override
	public void addTo(StringBuilder sb) {
		sb.append(expr1).append(operador).append(expr2);
	}
	
	

}
