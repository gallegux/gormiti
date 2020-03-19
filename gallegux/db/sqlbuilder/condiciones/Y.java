package gallegux.db.sqlbuilder.condiciones;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Y extends Condicion
{
	
	
	private List<Condicion> condiciones = new ArrayList<Condicion>();
	
	
	
	public Y() {}
	
	
	public Y(Condicion...condiciones) {
		super();
		
		for (Condicion condicion: condiciones) {
			this.condiciones.add(condicion);
		}
	}
	
	
	public Y(List<Condicion> condiciones) {
		super();
		this.condiciones.addAll(condiciones);
	}
	
	
	
	public void add(Condicion c) {
		this.condiciones.add(c);
	}


	@Override
	public void addTo(StringBuilder sb) 
	{
		add(sb, this.condiciones, " AND ");

	}
	

}
