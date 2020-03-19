package gallegux.db.sqlbuilder.condiciones;



/**
 * expr BETWEEN exprLimInf AND exprLimSup
 *
 */
public class CondicionEntre extends Condicion
{
	
	
	String valor, limInf, limSup;


	
	public CondicionEntre(String valor, String limInf, String limSup) 
	{
		super();
		
		this.valor = valor;
		this.limInf = limInf;
		this.limSup = limSup;
	}



	@Override
	public void addTo(StringBuilder sb) {
		sb.append(this.valor).append(" BETWEEN ")
			.append(this.limInf).append(" AND ").append(this.limSup);
	}


}
