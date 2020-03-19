package gallegux.db.orm.util;


import java.util.Iterator;
import java.util.List;



public class Util 
{
	
//	public static String join(List<String> lista, String union)
//	{
//		StringBuilder s = new StringBuilder();
//		
//		boolean usarUnion = false;
//		Iterator<String> i = lista.iterator();
//		
//		while (i.hasNext()) {
//			if (usarUnion) {
//				s.append(union);
//			}
//			s.append(i.next());
//			usarUnion = true;
//		}
//		
//		return s.toString();
//	}
	
	
	public static boolean esVacio(String s) {
		return s == null || "".equals(s.trim());
	}
	
	
//	public static Par split(String s)
//	{
//		s = s.trim();
//		int punto = s.indexOf('.');
//		
//		return new Par( s.substring(0, punto) , s.substring(punto+1) );
//	}
	
	
	public static String[] split(String s)
	{
		s = s.trim();
		int punto = s.indexOf('.');
		
		return new String[] { s.substring(0, punto) , s.substring(punto+1) };
	}
	
	
	public static String capitaliza(String s) {
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	} 
	
	
//	public static void print(String[] ss)
//	{
//		for (int i = 0; i < ss.length; i++) {
//			System.out.print(ss[i]+" ; ");
//		}
//		System.out.println();
//	}
	
	
	public static boolean estaEn(String s, String...ss)
	{
		for (int i = 0; i < ss.length; i++) if (s.equals(ss[i])) return true;
		
		return false;
	}
	
	

	

}
