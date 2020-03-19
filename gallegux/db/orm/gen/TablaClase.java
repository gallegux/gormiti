package gallegux.db.orm.gen;

import java.util.ArrayList;
import java.util.List;

public class TablaClase 
{

	// bd
	public String tabla = null;
	// java
	public String paqueteClase = null;
	public String paquete = null;
	public String clase = null;
	
	public List<ColumnaAtributo> columnaAtributos = new ArrayList<>();
	
	
	public TablaClase(String...fila)
	{
		
		tabla = fila[0].trim();
		
		this.paqueteClase = fila[1].trim();
		
		int punto = paqueteClase.lastIndexOf('.');
		
		paquete = this.paqueteClase.substring(0,punto);
		clase = this.paqueteClase.substring(punto+1);
	}
	
}
