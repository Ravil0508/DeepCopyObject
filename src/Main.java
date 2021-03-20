import java.lang.reflect.*;
import java.util.*;

public class Main {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
		List<String> list = new ArrayList<String>();
		list.add("GGG");
		list.add("HHH");
		Man man = new Man("Sasha", 22, list);
		Man m = deepCopy(man);
		List<Man> listman = new ArrayList<Man>();
		listman.add(man);
		listman.add(m);

		System.out.println(deepCopy(listman));

	}

	public static <T> T deepCopy(T o) {
		T copyObj = null;

		Class cl = o.getClass();

		/*
		 * Если копируемый объект примитиву или потомок нумбера стринга характера или
		 * булеана
		 */
		if ((cl.isPrimitive() || (o instanceof Number) || (o instanceof String) || (o instanceof Character)
				|| (o instanceof Boolean)))
// В этом случае мы получаем его конструктор и создаем его вот таким вот способом:
		{
			try {
				/*
				 * У всех классов оберток примитивов есть, кроме характер, есть конструктор с
				 * аргументом String
				 */
				copyObj = (T) cl.getDeclaredConstructor(String.class).newInstance(o.toString());
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/* Отдельно обработаю копирование коллекий */
		else if (o instanceof Collection) {
			try {
				copyObj = (T) o.getClass().getDeclaredConstructor().newInstance();
				List list = ArrayList.class.getDeclaredConstructor(Collection.class).newInstance(o);
				Method getSizeF = o.getClass().getDeclaredMethod("size");
				Method addCol = copyObj.getClass().getDeclaredMethod("add", Object.class);
				int size = (int) getSizeF.invoke(o);
				for (int i = 0; i < list.size(); i++) {
					addCol.invoke(copyObj, deepCopy(list.get(i)));
				}

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
		/*
		 * Иначе если объект не относится к одному из вышеперечисленных классов и
		 * примитивов в таком случае мы получаем конструктор класса и создаем объект,
		 * если есть конструктор по умолчанию то берем его если нет то берем последний
		 * из полученного массива конструкторов и с помощью метода returnVar() добавляем
		 * туда нулевые параметры
		 */

		else {
			Constructor[] arrCons = cl.getDeclaredConstructors();// получаем массив конструкторов класса
			ArrayList<Class[]> listAr = new ArrayList<Class[]>();// создаем список массивов типа Class, для того что бы
																	// вносить туда массивы параметров каждого
																	// конструктора
			for (Constructor f : arrCons) {// проходимся по массиву конструкторов
				listAr.add(f.getParameterTypes());
			} // получаем и вносим в список listAr массивы типов параметров
			Class[] parConst = listAr.get(listAr.size() - 1);
			try {
				for (int i = 0; i < listAr.size(); i++) {
					if (listAr.get(i).length == 0) {
						parConst = listAr.get(i);
					}
				} // конец фор
				if (parConst.length == 0)
					copyObj = (T) cl.newInstance();
				else {
					Constructor cons = cl.getDeclaredConstructor(parConst);
					Object[] valueForC = new Object[parConst.length];
					for (int i = 0; i < parConst.length; i++) {
						valueForC[i] = returnVar(parConst[i]);// возвращаю в массив значения по типу параметров
																// конструктора
					}

					copyObj = (T) cons.newInstance(valueForC);

				} // конец иф елзе
				/*
				 * Получаем список полей класса присваиваем и создаем для каждого поля новый
				 * экземпляр его класса
				 */
				Field[] fields = cl.getDeclaredFields();
				for (Field f : fields) {
					f.setAccessible(true);
					f.set(copyObj, deepCopy(f.get(o)));
				}

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
		} // КОНЕЦ ЕЛЗЕ ДЛЯ СОЗДАНИЯ КОПИИ ОБЪЕКТА
		return copyObj;
	}// конец метода deepcopy

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
		} // Проверяем если это примитив то инииализируем нулем, если только это не
			// булеан, а если булеан тогда инициализируем значанием

		else
			b = null;
		return b;
	}

}
