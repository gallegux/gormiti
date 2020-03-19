package gallegux.db.consultas;


import gallegux.db.orm.DatabasePool;
import gallegux.db.orm.Transaccion;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class AliasSQL extends SQL
{
	
	private static HashMap<String, String> mapAliasSql = null;
	
	
	static {
		try {
			Properties p = new Properties();
			p.load( new FileInputStream( "sql_aliases.config" ));
			p.list(System.out);
			
			mapAliasSql = new HashMap<>(p.size());
			
			for (String alias: p.stringPropertyNames()) {
				mapAliasSql.put(alias, p.getProperty(alias));
			}
			
			p.clear();
			p = null;
		}
		catch (IOException ex) {
			Logger.getAnonymousLogger().warning(ex.toString());
		}
	}

	
	
	public AliasSQL(String name, Connection con)
	{
		super();
		
		super.set( mapAliasSql.get(name), con);
	}
	
	
	
	public AliasSQL(String name, Transaccion tr)
	{
		super();
		
		super.set( mapAliasSql.get(name), tr);
	}
	
	
	
	//_------------------------------------------------------
	
	
	public static void main(String...arg)
	{
		try {
        	FileInputStream fis = new FileInputStream("logging.config");
			LogManager.getLogManager().readConfiguration(fis);
			
            Connection c = DatabasePool.getConnection();
       
            AliasSQL pq = new AliasSQL("prueba1", c);

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("anyo", 2015);
			params.put("mes", 5);

			Logger.getAnonymousLogger().info("map");
			pq.executeQuery(params);
			
			Logger.getAnonymousLogger().info("array");
			pq.executeQuery("anyo", 2015, "mes", 5);
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	
}
