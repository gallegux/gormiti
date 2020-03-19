package gallegux.db.sqlbuilder.condiciones;

import java.util.ArrayList;
import java.util.List;




/**
 * valorEn IN ( valor1, valor2, ... )
 *
 */
public class CondicionEn extends Condicion {

	
	private String valorEn = null;
	private List<String> valores = new ArrayList<String>();
	private boolean usarComillas = true;
	
	
	/**
	 * 
	 * @param expr1
	 * @param usarComillas para poner el caracter ' entre los valores
	 */
	public CondicionEn(String valorEn, boolean usarComillas) {
		super();
		this.valorEn = valorEn;
		this.usarComillas = usarComillas;
	}
	
	
	public CondicionEn(String valorEn) {
		super();
		this.valorEn = valorEn;
	}
	
	
	
	public void setUsarComillas(boolean usarComillas) {
		this.usarComillas = usarComillas;
	}
	
	
	public void addValor(Object valor) {
		addValor(valor);
	}


	@Override
	public void addTo(StringBuilder sb) {
		sb.append(valorEn).append(" IN (");
		
		for (Object o: valores) {
			if (this.usarComillas) sb.append('\'');
			sb.append(o);
			if (this.usarComillas) sb.append('\'');
			sb.append(',');
		}
		// sustituimos la ultima coma por )
		sb.setCharAt(sb.length()-1, ')');
	}
	
	
	
}
