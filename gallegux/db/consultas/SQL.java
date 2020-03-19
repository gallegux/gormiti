package gallegux.db.consultas;


import gallegux.db.orm.DatabasePool;
import gallegux.db.orm.Transaccion;

import java.io.FileInputStream;
import java.sql.*;
import java.util.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;



public class SQL
{
	
    private static final char MARCA = '#';
    
    

    private String sqlOriginal = null;
    private String sqlBind = null;
    private Connection connection = null;
    private PreparedStatement ps = null;
    private HashMap<String, List<Integer>> mapParametrosPosiciones = new HashMap<>();
    private HashMap<String, Object> mapParametrosValores = new HashMap<String, Object>();
    private Logger log = Logger.getLogger(this.getClass().getName());
    

    
    protected SQL() {}
    
    
    
    public SQL(String sql, Transaccion tr)
    throws SQLException
    {
    	this.connection = tr.getConnection();     
    	this.sqlOriginal = sql;
    }
    
    
    
    public SQL(String sql, Connection connection)
    throws SQLException
    {
    	this.connection = connection;
    	this.sqlOriginal = sql;
    }
    
    
    
    protected void set(String sql, Connection connection)
    {
    	this.sqlOriginal = sql;
    	this.connection = connection;     
    }

    
    
    protected void set(String sql, Transaccion tr)
    {
    	this.sqlOriginal = sql;
    	this.connection = tr.getConnection();     
    }

    
    
    @Override
    public void finalize()
    {
    	try {
    		this.ps.close();
    	}
    	catch (SQLException e) {
    		log.warning(e.toString());
    	}
    }
    
    
    
    public void close()
    throws SQLException
    {
    	if (ps != null) ps.close();
    }
    
    
    
    private void prepare()
    throws SQLException
    {
    	if (this.ps == null) prepareSentence();
    }
    
    
    
    private void prepareSentence()
    		throws SQLException
    {
    	int pos1 = 0, pos2 = -1, numBind = 0;
    	boolean fin = false;
    	String param;
    	
		while ( (pos1 = this.sqlOriginal.indexOf(MARCA, pos2 + 1)) != -1) {
    			pos2 = this.sqlOriginal.indexOf(MARCA, pos1 + 1);
    			param = this.sqlOriginal.substring(pos1 + 1, pos2);
    			getPositions(param).add(++numBind);
    			log.finest(numBind + " " + param);
    			//log.finest("posiciones: " + getPositions(param).size());
    	}
    	
    	this.sqlBind = this.sqlOriginal;
    	log.finest(this.sqlBind);
    	for (String paramName: this.mapParametrosPosiciones.keySet()) {
    		this.sqlBind = this.sqlBind.replace('#' + paramName + '#', "?");
    	}
    	
    	this.ps = this.connection.prepareStatement(this.sqlBind);
    }
    


    public ResultSet executeQuery(HashMap<String, Object> parametrosValores)
    throws SQLException
    {
    	prepare();
    	bind(parametrosValores);
        return this.ps.executeQuery();   
    }
    
    
    
    /**
     * 
     * @param parametrosValores Los parametrosPosiciones impares son los nombres de los parametrosPosiciones y
     * los parametrosPosiciones pares son los valores de esos columnaAtributos
     * @return
     * @throws SQLException 
     * @throws IllegalArgumentException 
     */
    public ResultSet executeQuery(Object...parametrosValores) 
    throws IllegalArgumentException, SQLException
    {
    	prepare();
    	bind(parametrosValores);
    	return this.ps.executeQuery();
    }
    
    
    
    public int executeUpdate(HashMap<String, Object> parametrosValores) 
    throws SQLException
    {
    	prepare();
    	bind(parametrosValores);
        return this.ps.executeUpdate();   
    }
    
    
    
    public int executeUpdate(Object...parametrosValores) 
    throws SQLException
    {
    	bind(parametrosValores);
        return this.ps.executeUpdate();   
    }
    
    
    
    public void clearParameters()
    {
    	this.mapParametrosValores.clear();
    }
    
    
    
    public void setParameterValue(String nombreParametro, Object valor) 
    {
    	this.mapParametrosValores.put(nombreParametro, valor);
    }
    
    
    
	public List<Integer> getPositions(String param) 
	{
		List<Integer> l = this.mapParametrosPosiciones.get(param);
		
		if (l == null) {
			l = new ArrayList<Integer>(1);
			mapParametrosPosiciones.put(param, l);
		}
		
		return l;
	}


    
    
    private void bind(HashMap<String, Object> parametrosValores) 
    throws SQLException
    {
    	String parametro;
		Object valor;
       	Set<Map.Entry<String, Object>> setParValor = parametrosValores.entrySet();
    	
    	this.ps.clearParameters();
    	
    	for (Map.Entry<String, Object> par: setParValor) {
			parametro = par.getKey();
			valor = par.getValue();
			
			for (int pos: getPositions(parametro)) {
				log.finer("bind: " + pos + " -> " + valor);
				ps.setObject(pos, valor);
			}
    	}
    }
    

    
    /**
     * 
     * @param parametrosValores
     * Las posiciones impares son los nombres de los argumentos.
     * Las posiciones pares son los valores de los argumentos de la posición anterior
     * @throws SQLException
     * @throws IllegalArgumentException
     * Se lanza si:
     * - el número de elementos en el array es impoar
     * - alguno de los nombres de los argumentos no es un String o es nulo.
     */
    private void bind(Object...parametrosValores) 
    throws SQLException, IllegalArgumentException
    {
    	if (parametrosValores.length % 2 == 1) {
    		// si el num de argumentos es impar
    		throw new IllegalArgumentException();
    	}
    	
    	String nombre;
    	Object valor;
    	
    	try {
	    	for (int i = 0; i < parametrosValores.length; ) {
	    		nombre = parametrosValores[i++].toString();
	    		valor = parametrosValores[i++];

				for (int pos: getPositions(nombre)) {
					log.finer("bind: " + pos + " -> " + valor);
					ps.setObject(pos, valor);
				}
	    	}
    	}
    	catch (Exception e) {
    		throw new IllegalArgumentException();
    	}
    }
    	

    //--------------------------------------------------------------------------------------
    
    
    public static int executeUpdate(String sql, Connection connection) 
    throws SQLException
    {
    	Statement st = connection.createStatement();
    	
    	return st.executeUpdate(sql);
    }
    
    
    
    public static ResultSet executeQuery(String sql, Connection connection)
    throws SQLException
    {
    	Statement st = connection.createStatement();
    	
    	return st.executeQuery(sql);
    }
    

    
    //------------------------------------------------------------------------------------
    

    public static void main(String...arg)
    {
        try {
        	FileInputStream fis = new FileInputStream("logging.config");
			LogManager.getLogManager().readConfiguration(fis);
			
            HashMap<String, Class> t = new HashMap<String, Class>();
            t.put("pepe", String.class);
            Connection c = DatabasePool.getConnection();
       
            SQL pq = new SQL("select nombre, apellidos from personas where nombre=#pepe# or nombre=#juan# or nombre=#pepe#", c);

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("pepe", "PEPE");
			params.put("juan", "JUAN");

			Logger.getAnonymousLogger().info("map");
			pq.executeQuery(params);
			
			Logger.getAnonymousLogger().info("array");
			pq.executeQuery("pepe", "PEPE", "juan", "JUAN");
			
			pq = new SQL("select nombre, apellidos from personas", c);
			ResultSet rs = pq.executeQuery();
			Logger.getAnonymousLogger().info("RS " + rs);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    


}
