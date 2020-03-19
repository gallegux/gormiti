package gallegux.db.orm;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class Mapeos 
{
	

	
	private static HashMap<Class, Clase> map_claseBean_clase = null;
	

	/**
	 * Para la relacion claseA->claseB se guardan las columnasFK de la claseA
	 */
	private static HashMap<Par<Class>, List<Atributo>> rel_clases_atributos = null;
	
	
	
	static {
		map_claseBean_clase = new HashMap<>();
		rel_clases_atributos = new HashMap<>();
	}
	
	
	
	
	protected static List<Atributo> getRelacionAtributos(Class classFK, Class classPK)
	throws ClassNotFoundException, NoSuchMethodException
	{
		Par<Class> claveClassClass = new Par(classFK, classPK);
		List<Atributo> columnasFK = rel_clases_atributos.get(claveClassClass);
		
		if (columnasFK == null) {
			columnasFK = new ArrayList<>();
			rel_clases_atributos.put(claveClassClass, columnasFK);
			
			Clase tablaPK = getClase(classPK);
			Clase tablaFK = getClase(classFK);
			
			for (Atributo columna: tablaFK.getAtributos()) {
				// comprobamos que:
				// - la columna es FK
				// - y apunta a la tablaPK
				if (columna.isForeignKey() && columna.getAtributoReferenciado().getClase() == tablaPK) {
					columnasFK.add(columna);
				}
			}
		}
		
		return columnasFK;
	}
	

	
	
	/**
	 * Devuelve los datos de una tabla.
	 * @param claseBean La clase bean vinculada a la tabla
	 * @return
	 * @throws NoSuchMethodException
	 */
	protected static Clase getClase(Class claseBean)
	throws NoSuchMethodException, ClassNotFoundException
	{
		Clase clase = map_claseBean_clase.get(claseBean);
		
		if (clase == null) {
			clase = new Clase(claseBean);
			map_claseBean_clase.put(claseBean, clase);
		}
		
		return clase;
	}
	
	
	
	protected static Clase getClase(String nombreClaseBean)
	throws NoSuchMethodException, ClassNotFoundException
	{
		//System.out.println(nombreClaseBean);
		return getClase( Class.forName(nombreClaseBean) );
	}
	
	
	

}
