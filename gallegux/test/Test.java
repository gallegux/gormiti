package gallegux.test;

import gallegux.db.consultas.RowToList;
import gallegux.db.consultas.SQL;
import gallegux.db.orm.BeanUtil;
import gallegux.db.orm.DatabasePool;
import gallegux.db.orm.Transaccion;
import gallegux.test.beans.BalanceBean;
import gallegux.test.beans.CocheBean;
import gallegux.test.beans.DetalleBean;
import gallegux.test.beans.PersonaBean;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import basurilla.DBConnection;


public class Test {

	
	public static void main(String...arg)
	{
		
		try {
			Logger log = Logger.getAnonymousLogger();
			
			FileInputStream fis = new FileInputStream("logging.config");
			LogManager.getLogManager().readConfiguration(fis);
			
			Connection conex = DatabasePool.getConnection();
			int result = -1;
			
			Transaccion tr = new Transaccion(conex);
			
			ResultSet rs = SQL.executeQuery("select count(*) from personas", conex);
			RowToList rol = new RowToList(rs);
			List fila = rol.getRowAsList();
			log.info("filas=" + fila.get(0));
			
			PersonaBean p1 = new PersonaBean();
			p1.setDNI("123asd23");
			p1.setNombre("Alfreso");
			p1.setApellidos("Garcia");
			result = tr.insert(p1);
			log.info(result + "  insertado " + BeanUtil.toString(p1));
			
//			p1 = new PersonaBean();
//			p1.setDni("123asd");
//			p1.setNombre("Alfreso");
//			p1.setApellidos("Garcia");
//			result = tr.insert(p1);
//			System.out.println(result + "  insertado " + BeanUtil.toString(p1));
			
			
			
			PersonaBean p2 = (PersonaBean) tr.selectByPK(new PersonaBean("123asd23"));
			System.out.println("p2= " + BeanUtil.toString(p2));
			log.info("Alfredo");
			result = tr.update(p2);
			log.info(result + "  modificado " + BeanUtil.toString(p2));
			
			tr.delete(p2);
			log.info(result + "  eliminado " + BeanUtil.toString(p2));
			
			log.info("coches");
			PersonaBean p3 = new PersonaBean("12345S");
			p3 = (PersonaBean) tr.selectByPK(p3);
			List<CocheBean> coches = (List<CocheBean>) tr.selectMany_asList(p3, CocheBean.class);
			for (CocheBean coche: coches) {
				log.info("coche= " + BeanUtil.toString(coche));

			}
			
			
			log.info("detalles");
			BalanceBean bal = new BalanceBean(2011, 1);
			log.info("Select Many !");
			List<DetalleBean> lisdet = (List<DetalleBean>) tr.selectMany_asList(bal, DetalleBean.class);
			for (DetalleBean de: lisdet) {
				log.info("detalle= " + BeanUtil.toString(de));
			}
			
			log.info("balances");
			DetalleBean de = (DetalleBean) tr.selectByPK(new DetalleBean(1));
			log.info("detalle= " + BeanUtil.toString(de));
			
			log.info("Select One !");
			
			bal = (BalanceBean) tr.selectOne(de, BalanceBean.class);
			log.info("detalle= " + BeanUtil.toString(bal));
			
			log.info("commit");

			tr.commit();
			tr = null;
			log.info("FIN.");
			
			//
			
			conex = DBConnection.getConnection();
			tr = new Transaccion(conex);
			
			p1 = new PersonaBean();
			p1.setDNI(System.currentTimeMillis()+"");
			p1.setNombre("Juan");
			p1.setApellidos("Sin Miedo");
			result = tr.insert(p1);
			log.fine(result + "  insertado " + BeanUtil.toString(p1));
			
			//tr.commit();
			tr.rollback();
			//conex.close();
			tr = null;

			Runtime.getRuntime().gc();
			
			System.out.println("FIN 0");
			Thread.currentThread().sleep(100);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("FIN");
		
	}
	
	
}
