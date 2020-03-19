package gallegux.db.sqlbuilder.condiciones;

import java.util.ArrayList;
import java.util.List;


public class O extends Condicion
{
	
	
	private List<Condicion> condiciones = new ArrayList<Condicion>();
	
	
	
	public O() {}
	
	
	public O(Condicion...condiciones) {
		super();
		
		for (Condicion condicion: condiciones) {
			this.condiciones.add(condicion);
		}
	}
	
	
	public O(List<Condicion> condiciones) {
		super();
		this.condiciones.addAll(condiciones);
	}
	
	
	
	public void add(Condicion c) {
		this.condiciones.add(c);
	}
	

	@Override
	public void addTo(StringBuilder sb) 
	{
		add(sb, this.condiciones, " OR ");
	}
	

}
