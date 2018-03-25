package pl.dbtool.dataService;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pl.dbtool.annotations.Column;
import pl.dbtool.models.DBModel;
import pl.dbtool.models.DBParametr;

public class DBServiceDao<T> {

	public DBServiceDao(Class< T > tClass) {
		super();
		this.myClass = tClass;
	}
	
	private Class< T > myClass;
	
	protected List<T> getAll(DBModel dbModel) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, SQLException {
		System.out.println("getAll");
		Connection connection = getConnection(dbModel.getConnection());
		Statement statement = null;
		List<T> list = new ArrayList<>();
		String query = "SELECT * from " + dbModel.getTable();
		statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(query);
		while (rs.next()) {
			list.add(createNewObject(rs));
		}
		return list;
	}
	
	protected T getById(DBModel dbModel) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		System.out.println("getById");
		Connection connection = getConnection(dbModel.getConnection());
//		T object = createNewObject();
		return null;
	}
	
	protected List<T> getByParametr(DBModel dbModel, DBParametr parametr) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		System.out.println("getByParametr");
		Connection connection = getConnection(dbModel.getConnection());
		List<T> list = new ArrayList<>();
//		list.add(createNewObject());
		return null;
	}
	
	protected List<T> getByParameters(DBModel dbModel, List<DBParametr> parameters) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		System.out.println("getByParameters");
		Connection connection = getConnection(dbModel.getConnection());
		List<T> list = new ArrayList<>();
//		list.add(createNewObject(ResultSet));
		return null;
	}

	protected void save(DBModel dbModel) {
		System.out.println("save");
		Connection connection = getConnection(dbModel.getConnection());
	}
	
	protected void update(DBModel dbModel) {
		System.out.println("update");
		Connection connection = getConnection(dbModel.getConnection());
	}
	
	protected void remove(DBModel dbModel) {
		System.out.println("remove");
		Connection connection = getConnection(dbModel.getConnection());
	}
	
	private Connection getConnection(String connection) {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private T createNewObject(ResultSet rs) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> clazz = Class.forName(myClass.getName());
		Constructor<?> ctor = clazz.getConstructor();
		Object object = ctor.newInstance();
		for (Field field : object.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			if(field.isAnnotationPresent(Column.class)) {
				field.set(object, field.getName());
//				String userid = rs.getString("USER");
			}
		}
		return (T) object;
	}
	
}
