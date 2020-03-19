package gallegux.db.orm;





public class BeanUtil 
{

	private static final char SEP = '|';
	

	
	public static String toString(Object o)
	{
		try {
			StringBuilder sb = new StringBuilder();
			Clase clase = Mapeos.getClase(o.getClass());
			int n = 0;
			
			sb.append(clase.getBeanClass().getSimpleName());
			sb.append("={");
			
			for (Atributo a: clase.getAtributos()) {
				if (n++ > 0) sb.append(SEP);
				sb.append(a.getNombreAtributo());
				sb.append('=');
				sb.append(a.getGetter().invoke(o));
			}
			
			sb.append("}");
			
			return sb.toString();
		}
		catch (Exception e) {
			return null;
		}
	}
	

	
	
}
