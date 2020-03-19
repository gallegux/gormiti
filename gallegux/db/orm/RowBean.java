package gallegux.db.orm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;


public class RowBean<T>
{

	private Clase clase = null;
	private ResultSet rs = null;
	private int columns = 0;
	private String[] columnNames;
	private String[] columnLabels;
	// asociamos el nombre del la columna con el metodo setter
	private HashMap<String, Method> mapColumnaSetter = null;
	
	
	public RowBean(ResultSet rs, Class<T> beanClass)
	throws ClassNotFoundException, NoSuchMethodException, SQLException
	{
		this.clase = Mapeos.getClase(beanClass);
		this.rs = rs;
		
		ResultSetMetaData rsmd = rs.getMetaData();
		
		columns = rsmd.getColumnCount();
		columnNames = new String[columns];
		columnLabels = new String[columns];
		
		for (int i = 0; i < columns; i++) {
			columnNames[i] = rsmd.getColumnName(i);
			columnLabels[i] = rsmd.getColumnLabel(i);
		}
		
		mapColumnaSetter = new HashMap<>();
		for (Atributo a: this.clase.getAtributos()) {
			mapColumnaSetter.put( a.getNombreColumna(), a.getSetter());
		}
	}
	
	
	
	public T next() 
			throws InstantiationException, IllegalAccessException, SQLException,
			IllegalArgumentException, InvocationTargetException
	{
		String columnName;
		Object value;
		T instancia = (T) this.clase.getBeanClass().newInstance();
		Method setter = null;
		
		for (int i = 0; i < columns; i++) {
			columnName = columnNames[i];
			value = this.rs.getObject(columnName);
			setter = mapColumnaSetter.get(columnName);
			setter.invoke(instancia, value);
		}

		return instancia;
	}
	
	
	public RowBean(ResultSet rs)
	throws ClassNotFoundException, NoSuchMethodException, SQLException
	{
		this.rs = rs;
		
		ResultSetMetaData rsmd = rs.getMetaData();
		
		columns = rsmd.getColumnCount();
		columnNames = new String[columns];
		columnLabels = new String[columns];
		
		for (int i = 0; i < columns; i++) {
			columnNames[i] = rsmd.getColumnName(i);
			columnLabels[i] = rsmd.getColumnLabel(i);
		}
		
		mapColumnaSetter = new HashMap<>();
		for (Atributo a: this.clase.getAtributos()) {
			mapColumnaSetter.put( a.getNombreColumna(), a.getSetter());
		}
	}
	
	
	
	public T next(T instancia) 
			throws InstantiationException, IllegalAccessException, SQLException,
			IllegalArgumentException, InvocationTargetException
	{
		String columnName;
		Object value;
		Method setter = null;
		
		for (int i = 0; i < columns; i++) {
			columnName = columnNames[i];
			value = this.rs.getObject(columnName);
			setter = mapColumnaSetter.get(columnName);
			setter.invoke(instancia, value);
		}
		
		return instancia;
	}
	
	
}
