package gallegux.db.orm;


import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Stack;
import java.util.logging.Logger;




public class DatabasePool 
{
	public final static String CONFIG_FILE = "config/database_pool.config";
	public final static int DEFAULT_MAX_CONNECTIONS = 10;
	
	private static String driver, url, user, password, urlConnection;
	private static int maxConnections = DEFAULT_MAX_CONNECTIONS;
	
	private static Stack<Connection> conexionesLibres = new Stack<>();
	private static Stack<Connection> conexionesOcupadas = new Stack<>();
	
	private static Logger log = Logger.getLogger("gallegux.db.orm.DatabasePool");
	
	
	
	static {
		init();
	}
	
	
	
	private static void init()
	{
		try {
			log.info("");
			Properties p = new Properties();
			// TODO: el config file puede (y debe) estar en el ClassLoader
			p.load( new FileInputStream(CONFIG_FILE) );
			
			driver = p.getProperty("driver");
			url = p.getProperty("url");
			user = p.getProperty("user");
			password = p.getProperty("password");
			
			try {
				maxConnections = Integer.parseInt( p.getProperty("max-connections") );
			}
			catch (NumberFormatException nfe) {
				log.warning(nfe.toString());
			}
			
			urlConnection = driver + url;
			log.info(driver +" "+ url +" "+ user +" "+ password +" "+ maxConnections);
		}
		catch (IOException e) {
			log.warning(e.toString());
		}
	}
	
	
	

	public synchronized static Connection getConnection()
			throws SQLException
	{
		Connection conexion = null;
		
		while (conexion == null) {
			conexion = _getConnection();
			if (conexion == null) esperar();
		}
		
		return conexion;
	}
	
	
	
	private static Connection _getConnection()
	throws SQLException
	{
		Connection conexion = null;
		Logger log = Logger.getLogger("gallegux.db.orm.DatabasePool");
		
		if (conexionesLibres.size() > 0) {
			conexion = conexionesLibres.pop();
			if (!conexion.isClosed()) {
				log.info("Conexion libre activa.");
				conexionesOcupadas.push(conexion);
			}
			else {
				log.info("Conexion libre cerrada. Crear conexion.");
				conexion = crearConnection();
				conexionesOcupadas.push(conexion);
			}
		}
		else if (conexionesOcupadas.size() < maxConnections) {
			log.info("Pocas conexiones. Crear conexion.");
			conexion = crearConnection();
			conexionesOcupadas.push(conexion);
		}
		
		return conexion;
	}
	
	
	
	private static Connection crearConnection()
	throws SQLException
	{
		if (user == null && password == null) {
			return DriverManager.getConnection(urlConnection);
		}
		else {
			return DriverManager.getConnection(urlConnection, user, password);
		}
	}

	
	
	
	public static synchronized void release(Connection c)
	{
		conexionesOcupadas.remove(c);
		conexionesLibres.push(c);
	}

	
	
	private static void esperar()
	{
		try {
			Thread.sleep(1000);
		}
		catch (Exception e) {}
	}

	

}
