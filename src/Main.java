import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.nio.charset.Charset;
import java.util.*;

public class Main{

	public static void main(String[] args)throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
		String s = null;
		
		
		
		
		List<String> list = new ArrayList<String>();
		list.add("GGG");
    	list.add("HHH");
    	Man man = new Man("Sasha",22,list);
    	Man m = deepCopy(man);
   	    List<Man> listman = new ArrayList<Man>();
   	    listman.add(man);
   	    listman.add(m);
   	// System.out.println("djn nen");
    	System.out.println(deepCopy(listman));
       
			}


	
	public static <T> T deepCopy(T o){
		T copyObj = null;
		
		Class cl = o.getClass();
	
 /* ���� ���������� ������ ��������� ��� ������� ������� ������� ��������� ��� �������*/
		if((cl.isPrimitive()
				||(o instanceof Number)
				||(o instanceof String)
				||(o instanceof Character)
				||(o instanceof Boolean)))
// � ���� ������ �� �������� ��� ����������� � ������� ��� ��� ����� ��� ��������:
		{try {
/*� ���� ������� ������� ���������� ����, ����� ��������, ���� ����������� � ���������� String*/			
			copyObj = (T) cl.getDeclaredConstructor(String.class).newInstance(o.toString());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
/*�������� ��������� ����������� ��������*/		
		else if(o instanceof Collection) {
		try {
			copyObj = (T) o.getClass().getDeclaredConstructor().newInstance();
			List list = ArrayList.class.getDeclaredConstructor(Collection.class).newInstance(o);
			Method getSizeF = o.getClass().getDeclaredMethod("size");
			Method addCol = copyObj.getClass().getDeclaredMethod("add", Object.class);
			int size = (int) getSizeF.invoke(o);		
			for(int i=0;i<list.size();i++) {
			addCol.invoke(copyObj, deepCopy(list.get(i)));}
			
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {	
			e.printStackTrace();
		} catch (IllegalAccessException e) {	
			e.printStackTrace();
		} catch (InstantiationException e) {	
			e.printStackTrace();
		}
		}
/*����� ���� ������ �� ��������� � ������ �� ����������������� ������� � ����������
 � ����� ������ �� �������� ����������� ������ � ������� ������, ���� ���� ����������� �� ��������� �� ����� ��� ���� ��� �� �����
 ��������� �� ����������� ������� ������������� � � ������� ������ returnVar() ��������� ���� ������� ��������� */		
		
		else {
			Constructor[] arrCons = cl.getDeclaredConstructors();// �������� ������ ������������� ������
			ArrayList<Class[]> listAr = new ArrayList<Class[]>();// ������� ������ �������� ���� Class, ��� ���� ��� ��
																	// ������� ���� ������� ���������� �������
																	// ������������
			for (Constructor f : arrCons) {// ���������� �� ������� �������������
				listAr.add(f.getParameterTypes());
			} // �������� � ������ � ������ listAr ������� ����� ����������
			Class[] parConst = listAr.get(listAr.size() - 1);
			try {
				for (int i = 0; i < listAr.size(); i++) {
					if (listAr.get(i).length == 0) {
						parConst = listAr.get(i);
					}
				} // ����� ���
				if (parConst.length == 0)
					copyObj = (T) cl.newInstance();
				else {
					Constructor cons = cl.getDeclaredConstructor(parConst);
					Object[] valueForC = new Object[parConst.length];
					for (int i = 0; i < parConst.length; i++) {
						valueForC[i] = returnVar(parConst[i]);// ��������� � ������ �������� �� ���� ����������
																// ������������
					}

					copyObj = (T) cons.newInstance(valueForC);
					
				} // ����� �� ����
/*�������� ������ ����� ������ ����������� � ������� ��� ������� ���� ����� ��������� ��� ������ */				
		Field[] fields = cl.getDeclaredFields();
		for(Field f:fields) {
			f.setAccessible(true);
			f.set(copyObj,deepCopy(f.get(o)));}
		
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} // ����� ���� ��� �������� ����� �������
		return copyObj;
	}// ����� ������ deepcopy

	@SuppressWarnings("rawtypes")
	public static Object returnVar(Class o)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, NullPointerException {
		Object b = null;
		if (o.isPrimitive()) {
			if (o.equals(boolean.class))
				b = false;
			else
				b = 0;
		} // ��������� ���� ��� �������� �� ������������� �����, ���� ������ ��� ��
			// ������, � ���� ������ ����� �������������� ���������
			// false(T)o.getClass().getDeclaredConstructor(o.getClass()).newInstance(0);
		else
			b = null;// new Object();
		return b;}
	// for(int i = 0;i<parCons.length;i++)returnVar(parCons[i]);
	
	
	
	
}
