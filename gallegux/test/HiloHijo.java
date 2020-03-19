package gallegux.test;

import gallegux.db.orm.DatabasePool;

import java.sql.Connection;


public class HiloHijo extends Thread 
{
	
	Connection conexion = null;
	
	
	public HiloHijo()
	{
		
	}
	
	
	
	public void run()
	{
		inicioHilo();
		operacionesHilo();
		finalHilo();
	}
	
	
	
	public void inicioHilo()
	{
		try {
			System.out.println("HiloHijo " + getName() + " inicio");
			this.conexion = DatabasePool.getConnection();
			this.conexion.setAutoCommit(false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public void operacionesHilo()
	{
		try {
			System.out.println("HiloHijo " + getName() + " operaciones");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public void finalHilo()
	{
		try {
			System.out.println("HiloHijo " + getName() + " final");
			conexion.commit();
			DatabasePool.release(this.conexion);
			//notify();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
