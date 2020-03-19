package gallegux.db.orm;

import java.util.HashMap;


public class Par<T> 
{
	
	protected T o1, o2;
	
	
	public Par(String stringConPunto)
	{
		int punto = stringConPunto.indexOf('.');
		
		this.o1 = (T) stringConPunto.substring(0, punto);
		this.o2 = (T) stringConPunto.substring(punto+1);
		
	}
	
	
	public Par(T o1, T o2)
	{
		this.o1 = o1;
		this.o2 = o2;
		
	}
	
	
	public T get1() {
		return this.o1;
	}
	
	
	public T get2() {
		return this.o2;
	}
	
	
	@Override
	public boolean equals(Object otro)
	{
		try {
			Par<T> otroT = (Par<T>) otro;
			return (this.o1 == otroT.o1) && (this.o2 == otroT.o2);
		}
		catch (Exception e) {
			return false;
		}
	}
	
	
	public int hashCode()
	{
		return (this.o1.toString() + this.o2.toString()).hashCode();
	}
	
	
	
	public String toString()
	{
		return "(" + this.o1 + "." + this.o2 + ")";
	}
	
	
	public static void main(String ...arg)
	{
		Par<String> p1 = new Par<String>("en un lugar", "de la mancha");
		Par<String> p2 = new Par<String>("en un lugar", "de la mancha");
		Par<String> p3 = new Par<String>("hola.adios");
		
//		System.out.println(p1 + " " + p1.hashCode());
//		System.out.println(p2 + " " + p2.hashCode());
//		System.out.println(p1 == p2);
		
		
		HashMap<Par<String>, String> hm = new HashMap<>();
		hm.put(p1, "valor");
		System.out.println( hm.get(p2) );
		
	}

	
}
