package gallegux.db.consultas;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;


public class RowToHashmap 
{
	
	private int columns = 0;
	private String[] columnNames;
	private String[] columnLabels;
	private ResultSet resultSet = null;
	
	


	public RowToHashmap(ResultSet rs) 
	throws SQLException
	{
		ResultSetMetaData rsmd = rs.getMetaData();
		
		columns = rsmd.getColumnCount();
		columnNames = new String[columns];
		columnLabels = new String[columns];
		
		for (int i = 0; i < columns; i++) {
			columnNames[i] = rsmd.getColumnName(i);
			columnLabels[i] = rsmd.getColumnLabel(i);
		}

	}
	


	
	
	public HashMap<String, Object> getRow()
	throws SQLException
	{
		HashMap<String, Object> hmRow = new HashMap<String, Object>();
		String columnName;
		Object value;
		
		for (int i = 0; i < columns; i++) {
			columnName = columnNames[i];
			value = this.resultSet.getObject(columnName);
			hmRow.put(columnName, value);
		}
		
		return hmRow;
	}
	
	
}
