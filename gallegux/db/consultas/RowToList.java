package gallegux.db.consultas;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;


public class RowToList 
{
	
	private int columns = 0;
	private ResultSet resultSet = null;
	
	
	

	public RowToList(ResultSet rs) 
	throws SQLException
	{
		this.resultSet = rs;
		columns = this.resultSet.getMetaData().getColumnCount();
	}

	
	
	public int getColumnsCount()
	{
		return this.columns;
	}
	
	
	public ArrayList<Object> getRowAsList()
	throws SQLException
	{
		ArrayList<Object> row = new ArrayList<Object>(columns);
		
		for (int i = 0; i < columns; i++) {
			row.add(this.resultSet.getObject(i+1));
		}
		
		return row;
	}
	
	
	
	public Object[] getRowAsArray()
	throws SQLException
	{
		Object[] row = new Object[columns];
		
		for (int i = 0; i < columns; i++) {
			row[i] = this.resultSet.getObject(i+1);
		}
		
		return row;
	}
	
	
	

}
