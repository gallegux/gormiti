package gallegux.db.orm;



import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;



/**
 * Este objeto vale para insertar, modificar y eliminar registros en la base de datos.
 * Ha de instanciarse para cada transaccion.
 *
 */
public class Transaccion
{

	private static int contadorTransacciones = 0;
	
	private static HashMap<Class, String> sqlSelectByPK = new HashMap<>();
	private static HashMap<Class, String> sqlInserts = new HashMap<>();
	private static HashMap<Class, String> sqlUpdates = new HashMap<>();
	private static HashMap<Class, String> sqlDeletes = new HashMap<>();

	/**
	 * Cache de prepared statements. 
	 * Se pierden para cada instancia ya que se crean para cada conexion.
	 * Si la conexion que se utiliza son conexiones obtenidas mediante un pool se pueden reutilizar.
	 */
	private HashMap<Class, PreparedStatement> psSelectByPK = new HashMap<Class, PreparedStatement>();
	private HashMap<Class, PreparedStatement> psInserts = new HashMap<Class, PreparedStatement>();
	private HashMap<Class, PreparedStatement> psUpdates = new HashMap<Class, PreparedStatement>();
	private HashMap<Class, PreparedStatement> psDeletes = new HashMap<Class, PreparedStatement>();
	private HashMap<String, PreparedStatement> psSelectByFK = new HashMap<String, PreparedStatement>();

	/**
	 * La conexion que se utilizara
	 */
	private Connection conexion = null;
	private boolean releaseConnection = false;
	private int idTransaccion;
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	

	
	
	public Transaccion() throws SQLException
	{
		
		this.conexion = DatabasePool.getConnection();
		this.conexion.setAutoCommit(false);
		this.idTransaccion = contadorTransacciones++;
		this.releaseConnection = true;
	}
	
	
	/**
	 * La conexion a utilizar
	 * @param connection
	 */
	public Transaccion(Connection connection) throws SQLException
	{
		this.conexion = connection;
		this.conexion.setAutoCommit(false);
		
		this.idTransaccion = contadorTransacciones++;
	}
	
	
	
	protected void finalize() throws Throwable
	{
		log.fine("finalize Transaccion " + idTransaccion);
		
		closePreparedStatements(psSelectByPK);
		closePreparedStatements(psInserts);
		closePreparedStatements(psUpdates);
		closePreparedStatements(psDeletes);
		
		psSelectByPK = null;
		psInserts = null;
		psUpdates = null;
		psDeletes = null;
		
		if (this.releaseConnection) {
			DatabasePool.release(this.conexion);
		}
		
		super.finalize();
	}
	
	
	
	private void closePreparedStatements(HashMap<Class, PreparedStatement> hm)
	{
		Iterator<PreparedStatement> i = hm.values().iterator();
		
		while (i.hasNext()) {
			try {
				i.next().close();
			}
			catch (SQLException e) {}
		}
		
		hm.clear();
	}
	
	
	public Connection getConnection()
	{
		return this.conexion;
	}
	
	
	public void commit() throws SQLException
	{
		this.conexion.commit();
	}
	
	
	public void rollback() throws SQLException
	{
		this.conexion.rollback();
	}
	

	
	/**
	 * Devuelve el PreparedStatement del select de un registro de la tabla vinculada a la clase
	 * @param clazz
	 * @return
	 * @throws SQLException
	 * @throws NoSuchMethodException
	 */
	private PreparedStatement getPSselectByPK(Class clazz) 
	throws SQLException, NoSuchMethodException, ClassNotFoundException
	{
		PreparedStatement ps = psSelectByPK.get(clazz);
		
		if (ps == null) {
			String sql = sqlSelectByPK.get(clazz);
			if (sql == null) {
				sql = Mapeos.getClase(clazz).getSelectByPK();
				sqlSelectByPK.put(clazz, sql);
			}
			ps = this.conexion.prepareStatement(sql);
			psSelectByPK.put(clazz, ps);
		}
		
		return ps;
	}

	

	/**
	 * Devuelve el PreparedStatement del insert de un registro de la tabla vinculada a la clase
	 * @param clazz
	 * @return
	 * @throws SQLException
	 * @throws NoSuchMethodException
	 */
	private PreparedStatement getPSinsert(Class clazz) 
	throws SQLException, NoSuchMethodException, ClassNotFoundException
	{
		PreparedStatement ps = psInserts.get(clazz);
		
		if (ps == null) {
			String sql = sqlInserts.get(clazz);
			if (sql == null) {
				sql = Mapeos.getClase(clazz).getInsert();
				sqlInserts.put(clazz, sql);
			}
			ps = this.conexion.prepareStatement(sql);
			psInserts.put(clazz, ps);
		}
		
		return ps;
	}

	

	/**
	 * Devuelve el PreparedStatement del update de un registro de la tabla vinculada a la clase
	 * @param clazz
	 * @return
	 * @throws SQLException
	 * @throws NoSuchMethodException
	 */
	private PreparedStatement getPSupdate(Class clazz) 
	throws SQLException, NoSuchMethodException, ClassNotFoundException
	{
		PreparedStatement ps = psUpdates.get(clazz);
		
		if (ps == null) {
			String sql = sqlUpdates.get(clazz);
			if (sql == null) {
				sql = Mapeos.getClase(clazz).getUpdate();
				sqlUpdates.put(clazz, sql);
			}
			ps = this.conexion.prepareStatement(sql);
			psUpdates.put(clazz, ps);
		}
		
		return ps;
	}

	

	/**
	 * Devuelve el PreparedStatement del delete de un registro de la tabla vinculada a la clase
	 * @param clazz
	 * @return
	 * @throws SQLException
	 * @throws NoSuchMethodException
	 */
	private PreparedStatement getPSdelete(Class clazz) 
	throws SQLException, NoSuchMethodException, ClassNotFoundException
	{
		PreparedStatement ps = psDeletes.get(clazz);
		
		if (ps == null) {

			String sql = sqlDeletes.get(clazz);
			if (sql == null) {
				sql = Mapeos.getClase(clazz).getDelete();
				sqlDeletes.put(clazz, sql);
			}
			ps = this.conexion.prepareStatement(sql);
			psDeletes.put(clazz, ps);
		}
		
		return ps;
	}
	
	
	
	private PreparedStatement getPSselectByFK(String sql)
	throws SQLException
	{
		PreparedStatement ps = this.psSelectByFK.get(sql);
		
		if (ps == null) {
			ps = this.conexion.prepareStatement(sql);
		}
		
		return ps;
	}
	
	
	
	/**
	 * Selecciona un registro de la tabla por su Primary Key
	 * El objeto pasado como argumento debe tener los columnaAtributos correspondiendtes a las columnas
	 * Primary Key con valores.
	 * El metodo utiliza el objeto que se pasa como argumento rellenando los columnaAtributos.
	 * @param objeto
	 * @return Un objeto
	 */
	public Object selectByPK(Object objeto) 
	throws SQLException, IllegalAccessException, InvocationTargetException, InstantiationException, 
		NoSuchMethodException, ClassNotFoundException
	{
		Class claseObjeto = objeto.getClass();
		PreparedStatement ps = getPSselectByPK(claseObjeto);
		Clase dt = Mapeos.getClase(claseObjeto);
		int numBind = 1;
		
		for (Atributo dc: dt.getAtributosPK()) {
			bind(ps, numBind++, dc.getGetter().invoke(objeto));
		}
		
		ResultSet rs = ps.executeQuery();
		
		if (rs.next()) {
			Object instancia = createObject(rs, claseObjeto);
			rs.close();
			return instancia;
		}
		else {
			rs.close();
			return null;
		}
	}
	
	
	
	/**
	 * Carga en el objeto pasado como parámetro el registro que se busca.
	 * @param objeto Objeto con la primary Key
	 * @throws SQLException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void load(Object objeto) 
	throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException
	{
		Class claseObjeto = objeto.getClass();
		PreparedStatement ps = getPSselectByPK(claseObjeto);
		Clase dt = Mapeos.getClase(claseObjeto);
		int numBind = 1;
		
		for (Atributo dc: dt.getAtributosPK()) {
			bind(ps, numBind++, dc.getGetter().invoke(objeto));
		}
		
		ResultSet rs = ps.executeQuery();
		
		if (rs.next()) {
			for (Atributo c: Mapeos.getClase(claseObjeto).getAtributos()) {
				setValorAtributo(c, objeto, rs);
			}
		}
		rs.close();
	}
	
	
	
	/**
	 * Selecciona el registro referenciado por otro.
	 * Para relaciones 1:n (PK:FK)
	 * Las instancias de objeto hacen referencia a instancias de objetos de tipoObjeto.
	 * Ejemplo:
	 * FK coche.propietario = 12
	 * PK persona.id_persona = 12
	 * La Tabla/Clase coche hace referencia a la Tabla/Clase persona, por tanto
	 * selectByPK(coche, persona)
	 * obtiene el objeto persona propietario del coche
	 * @param objetoFK objeto que referencia al buscado
	 * @param claseObjetoPK clase del objeto referenciado
	 * @return Un objeto de la clase claseObjetoPK
	 * @throws NoSuchMethodException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	public Object selectOne(Object objetoFK, Class claseObjetoPK) 
	throws NoSuchMethodException, InstantiationException, IllegalAccessException, 
		IllegalArgumentException, InvocationTargetException, SQLException, ClassNotFoundException
	{
		List<Atributo> columnasFK = Mapeos.getRelacionAtributos(objetoFK.getClass(), claseObjetoPK);
		
		Object objetoPK = claseObjetoPK.newInstance();
		Method getter, setter;
		Object valor;
		
		for (Atributo a: columnasFK) {
			getter = a.getGetter();
			setter = a.getAtributoReferenciado().getSetter();
			valor = getter.invoke(objetoFK);
			setter.invoke(objetoPK, valor);
		}
		
		return selectByPK(objetoPK);
	}
	
	
	
	/**
	 * Selecciona los objetos de tipo claseObjetoFK que hacen referencia al objetoPK.
	 * Para relaciones 1:N (PK:FK), obtenemos N (FK) objetos 
	 * @param objetoPK
	 * @param claseObjetoFK
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws SQLException
	 */
	public ResultSet selectMany_asResultset(Object objetoPK, Class claseObjetoFK) 
	throws NoSuchMethodException, InstantiationException, IllegalAccessException, 
		IllegalArgumentException, InvocationTargetException, SQLException, ClassNotFoundException
	{
		List<Atributo> columnasFK = Mapeos.getRelacionAtributos(claseObjetoFK, objetoPK.getClass());
		HashMap<String, Object> mapColumnaValor = new HashMap<String, Object>();
		
		Object valor;
		
		for (Atributo a: columnasFK) {
			valor = a.getAtributoReferenciado().getGetter().invoke(objetoPK);
			mapColumnaValor.put(a.getNombreColumna(), valor);
		}

		return select_asResultset(claseObjetoFK, mapColumnaValor);
	}
	
	
	
	/**
	 * Selecciona los objetos de tipo claseObjetoFK que hacen referencia al objetoPK
	 * @param objetoPK
	 * @param claseObjetoFK
	 * @return
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws SQLException
	 */
	public List<? extends Object> selectMany_asList(Object objetoPK, Class claseObjetoFK) 
	throws IllegalArgumentException, NoSuchMethodException, InstantiationException, 
		IllegalAccessException, InvocationTargetException, SQLException, ClassNotFoundException
	{
		List<Object> objetos = new ArrayList<Object>();
		ResultSet rs = selectMany_asResultset(objetoPK, claseObjetoFK);
		
		while (rs.next()) {
			objetos.add( createObject(rs, claseObjetoFK) );
		}
		
		rs.close();

		return objetos;
	}
	
	
	
	
	/**
	 * Busca objetos del tipo indicado.
	 * En el array filtros, los nombres de los filtros son los nombres de los columnaAtributos
	 * @param claseObjetoBusqueda
	 * @param filtros
	 * @return
	 * @throws SQLException
	 */
	public ResultSet select_asResultset(Class claseObjetoBusqueda, HashMap<String, Object> filtros)
	throws SQLException, NoSuchMethodException, ClassNotFoundException
	{
		String columna;
		Object valor;
		Clase tabla = Mapeos.getClase(claseObjetoBusqueda);
		StringBuilder sql = new StringBuilder(tabla.getSelectFrom()).append(" where ");
		int condic = 0;
		int numBind = 1;
		
		for (Map.Entry<String, Object> filtro: filtros.entrySet()) {
			columna = filtro.getKey();
			valor = filtro.getValue();
			if (condic++ > 0) sql.append("and ");
			sql.append(columna);
			sql.append( (valor == null) ? " is null " : "=? ");
		}
		log.fine(sql.toString());
		PreparedStatement ps = getPSselectByFK(sql.toString());
		
		for (Map.Entry<String, Object> filtro: filtros.entrySet()) {
			if (filtro.getValue() != null) {
				bind(ps, numBind++, filtro.getValue());
			}
		}
		
		return ps.executeQuery();
	}

	
	
	/**
	 * Busca objetos del tipo indicado.
	 * En el array filtros, los nombres de los filtros son los nombres de los columnaAtributos
	 * @param tipoObjetoBusqueda
	 * @param filtros
	 * @return
	 * @throws SQLException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public List<? extends Object> select_asList(Class tipoObjetoBusqueda, HashMap<String, Object> filtros)
	throws SQLException, InvocationTargetException, NoSuchMethodException, 
	IllegalAccessException, InstantiationException, ClassNotFoundException
	{
		List<Object> objetos = new ArrayList<Object>();
		ResultSet rs = select_asResultset(tipoObjetoBusqueda, filtros);
		
		while (rs.next()) {
			objetos.add(createObject(rs, tipoObjetoBusqueda));
		}
		
		rs.close();

		return objetos;
	}

	
	
	
	/**
	 * Inserta un objeto en su tabla correspondiente
	 * @param objeto
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public int insert(Object objeto) 
	throws SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException
	{
		Class claseObjeto = objeto.getClass();
		PreparedStatement ps = getPSinsert(claseObjeto);
		Clase dt = Mapeos.getClase(claseObjeto);
		int numBind = 1;
		
		for (Atributo dc: dt.getAtributos()) {
			bind(ps, numBind++, dc.getGetter().invoke(objeto));
		}
		
		return ps.executeUpdate();
	}
	
	
	
	/**
	 * actualiza un registro de su tabla correspondiente. La tabla tiene que tener PK.
	 * @param objeto
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public int update(Object objeto) 
	throws SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException
	{
		Class claseObjeto = objeto.getClass();
		PreparedStatement ps = getPSupdate(claseObjeto);
		Clase dt = Mapeos.getClase(claseObjeto);
		int numBind = 1;
		
		for (Atributo dc: dt.getAtributos()) {
			bind(ps, numBind++, dc.getGetter().invoke(objeto));
		}
		for (Atributo dc: dt.getAtributosPK()) {
			bind(ps, numBind++, dc.getGetter().invoke(objeto));
		}
		
		return ps.executeUpdate();
	}

	
	
	/**
	 * Elimina un registro de su tabla correspondiente. La tabla tiene que tener PK.
	 * @param objeto
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public int delete(Object objeto) 
	throws SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException
	{
		Class claseObjeto = objeto.getClass();
		PreparedStatement ps = getPSdelete(claseObjeto);
		Clase dt = Mapeos.getClase(claseObjeto);
		int numBind = 1;
		
		for (Atributo dc: dt.getAtributosPK()) {
			bind(ps, numBind++, dc.getGetter().invoke(objeto));
		}
		
		return ps.executeUpdate();
	}
	
	
	
	/**
	 * Modifica o inserta un objeto.
	 * Si el resultado de la modificacion del objeto es 0, lo inserta.
	 * @param objeto
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public int updateOrInsert(Object objeto) 
	throws SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException
	{
		int result = update(objeto);
		
		if (result == 0) {
			result = insert(objeto);
		}
		
		return result;
	}
	
	
	
	/**
	 * Crea un objeto a partir de una fila de una consulta
	 * @param rs
	 * @param clazz
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SQLException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private Object createObject(ResultSet rs, Class clazz) 
	throws IllegalAccessException, InstantiationException, SQLException, InvocationTargetException, 
	NoSuchMethodException, ClassNotFoundException
	{
		Object objeto = clazz.newInstance();
		
		for (Atributo atr: Mapeos.getClase(clazz).getAtributos()) {
			setValorAtributo(atr, objeto, rs);
		}
		
		return objeto;
	}
	
	

	
	
	private void bind(PreparedStatement ps, int numCampo, Object valor)
	throws SQLException
	{
		if (valor == null) {
			ps.setObject(numCampo, null);
		} else {
			//bind(ps, numCampo, valor.getClass(), valor);
			ps.setObject(numCampo, valor);
		}
	}
	
	
	
	/**
	 * Establece un valor a un atributo de un bean
	 * @param atributo
	 * @param objeto
	 * @param rs
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws SQLException
	 */
	private void setValorAtributo(Atributo atributo, Object objeto, ResultSet rs)
	throws IllegalAccessException, InvocationTargetException, SQLException, NoSuchMethodException
	{
		atributo.getSetter().invoke(objeto, rs.getObject(atributo.getNombreColumna()));
	}


	
}
