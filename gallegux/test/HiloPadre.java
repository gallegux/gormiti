package gallegux.test;


public class HiloPadre 
{
	
	public HiloPadre()
	{
		
	}
	
	
	public void run() throws InterruptedException
	{
		String hp = Thread.currentThread().getName();
		
		System.out.println("Hilo padre " + hp);
		
		HiloHijo hh = new HiloHijo();
		hh.start();
		
		System.out.println("Esperando...");
		wait(10000);
		System.out.println("notificado");
		
	}
	
	
	public static void main(String...arg)
	{
		try {
			HiloPadre hp = new HiloPadre();
			hp.run();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
