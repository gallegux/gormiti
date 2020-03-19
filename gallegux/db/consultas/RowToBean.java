package gallegux.db.consultas;


import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;


/**
 * convierte un registro de una consulta a bbdd en un bean.
 * los atributos del bean deben ser publicos
 *
 */
public class RowToBean 
{
	
	private final boolean MAPEO_NOMBRECOLUMNA_ATRIBUTOBEAN = false;
	private final boolean MAPEO_POSCOLUMNA_ATRIBUTOBEAN = true;
	
	/**
	 * Mapeo entre los nombres de los campos de la bbdd y los columnaAtributos
	 */
	private HashMap<String, Field> map_ColumnaAtributo = null;
	
	/**
	 * Mapeo posicion-columna con nombre-de-atributo
	 */
	private Field[] map_PosColumna_Atributo = null;
	
	/**
	 * Tipo de mapeo
	 */
	private boolean tipoMapeo = MAPEO_NOMBRECOLUMNA_ATRIBUTOBEAN;  // un valor inicial
	
	private Class claseObjeto = null;
	
	
	
	/**
	 * MAPEO_NOMBRECOLUMNA_ATRIBUTOBEAN
	 * @param clase clase a la que se convertiran las filas
	 * @param mapeo mapeo nombre_campo_bd <--> nombre_atributo_clase
	 */
	public RowToBean(Class clase, HashMap<String, String> mapeo)
	{
		this.claseObjeto = clase;
		this.map_ColumnaAtributo = new HashMap<String, Field>();
		this.tipoMapeo = MAPEO_NOMBRECOLUMNA_ATRIBUTOBEAN;
		
		mapearCamposMetodos(clase, mapeo);
	}
	
	
	
	/**
	 * MAPEO_POSCOLUMNA_ATRIBUTOBEAN
	 * @param clase
	 * @param nombreColumnas
	 */
	public RowToBean(Class clase, String[] nombreAtributos)
	{
		this.claseObjeto = clase;
		this.tipoMapeo = MAPEO_POSCOLUMNA_ATRIBUTOBEAN;
		
		mapearPosicionAtributo(clase, nombreAtributos);
	}
	
	
	
	/**
	 * 
	 * @param clase
	 * @param mapeo
	 */
	private void mapearCamposMetodos(Class clase, HashMap<String, String> mapeo)
	{
		Collection<String> nombreAtributos = mapeo.values();
		Field[] atributosClase = clase.getFields();
		
		for (String nombreAtributo: nombreAtributos) {
			map_ColumnaAtributo.put(nombreAtributo, getAtributo(nombreAtributo, atributosClase));
		}
	}
	
	
	
	/**
	 * Devuelve el atributo solicitado por nombre
	 * @param nombreAtributo
	 * @param atributosClase
	 * @return
	 */
	private Field getAtributo(String nombreAtributo, Field[] atributosClase) {
		for (Field atributo: atributosClase) {
			if (atributo.getName().equals(nombreAtributo)) {
				return atributo;
			}
		}
		return null;
	}
	
	
	
	private String capitaliza(String s) {
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}
	
	
	
	private void mapearPosicionAtributo(Class clase, String[] nombreAtributos)
	{
		Field[] atributosClase = clase.getFields();
		map_PosColumna_Atributo = new Field[nombreAtributos.length];
		
		for (int i = 0; i < nombreAtributos.length; i++) {
			map_PosColumna_Atributo[i] = getAtributo(nombreAtributos[i], atributosClase);
		}
	}
	
	
	
	public Object createObject(ResultSet rs)
	throws IllegalAccessException, InstantiationException, SQLException
	{
		if (tipoMapeo == MAPEO_NOMBRECOLUMNA_ATRIBUTOBEAN) {
			return createObject_MAPEO_NOMBRECOLUMNA_ATRIBUTO(rs);
		} else {
			return createObject_MAPEO_POSCOLUMNA_ATRIBUTO(rs);
		}
	}
	
	
	
	private Object createObject_MAPEO_NOMBRECOLUMNA_ATRIBUTO(ResultSet rs) 
	throws IllegalAccessException, InstantiationException, SQLException
	{
		Object objeto = this.claseObjeto.newInstance();
		
		Set<String> campos = map_ColumnaAtributo.keySet();
		Field atributo = null;
		Class claseAtributo = null;
		
		for (String nombreCampo: campos) {
			atributo = map_ColumnaAtributo.get(nombreCampo);
			claseAtributo = atributo.getType();
			
			if (claseAtributo.equals(boolean.class)) {
				atributo.setBoolean(objeto, rs.getBoolean(nombreCampo));
			} else if (claseAtributo.equals(byte.class)) {
				atributo.setByte(objeto, rs.getByte(nombreCampo));
			} else if (claseAtributo.equals(char.class)) {
				atributo.setChar(objeto, (char) rs.getByte(nombreCampo));
			} else if (claseAtributo.equals(double.class)) {
				atributo.setDouble(objeto, rs.getDouble(nombreCampo));
			} else if (claseAtributo.equals(float.class)) {
				atributo.setFloat(objeto, rs.getFloat(nombreCampo));
			} else if (claseAtributo.equals(int.class)) {
				atributo.setInt(objeto, rs.getInt(nombreCampo));
			} else if (claseAtributo.equals(long.class)) {
				atributo.setLong(objeto, rs.getLong(nombreCampo));
			} else if (claseAtributo.equals(short.class)) {
				atributo.setShort(objeto, rs.getShort(nombreCampo));
			} else {
				atributo.set(objeto, rs.getObject(nombreCampo));
			}
		}
		
		
		return objeto;
	}

	
	
	private Object createObject_MAPEO_POSCOLUMNA_ATRIBUTO(ResultSet rs) 
	throws IllegalAccessException, InstantiationException, SQLException
	{
		Object objeto = this.claseObjeto.newInstance();
		Field atributo = null;
		Class claseAtributo = null;
		
		for (int columna = 0; columna < map_PosColumna_Atributo.length; columna++) {
			atributo = map_PosColumna_Atributo[columna];
			claseAtributo = atributo.getType();
			
			if (claseAtributo.equals(boolean.class)) {
				atributo.setBoolean(objeto, rs.getBoolean(columna+1));
			} else if (claseAtributo.equals(byte.class)) {
				atributo.setByte(objeto, rs.getByte(columna+1));
			} else if (claseAtributo.equals(char.class)) {
				atributo.setChar(objeto, (char) rs.getByte(columna+1));
			} else if (claseAtributo.equals(double.class)) {
				atributo.setDouble(objeto, rs.getDouble(columna+1));
			} else if (claseAtributo.equals(float.class)) {
				atributo.setFloat(objeto, rs.getFloat(columna+1));
			} else if (claseAtributo.equals(int.class)) {
				atributo.setInt(objeto, rs.getInt(columna+1));
			} else if (claseAtributo.equals(long.class)) {
				atributo.setLong(objeto, rs.getLong(columna+1));
			} else if (claseAtributo.equals(short.class)) {
				atributo.setShort(objeto, rs.getShort(columna+1));
			} else {
				atributo.set(objeto, rs.getObject(columna+1));
			}
		}
		
		return objeto;
	}
	
	
	
	
}
