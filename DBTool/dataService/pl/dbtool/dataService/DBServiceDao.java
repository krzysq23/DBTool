package pl.dbtool.dataService;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pl.dbtool.annotations.Column;
import pl.dbtool.annotations.ColumnId;
import pl.dbtool.annotations.ColumnId.AutoIncrement;
import pl.dbtool.models.DBModel;
import pl.dbtool.models.DBParametr;

public class DBServiceDao<T> {

	public DBServiceDao(Class< T > tClass) {
		super();
		this.myClass = tClass;
	}
	
	private Class< T > myClass;
	
	protected List<T> getAll(DBModel dbModel) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, SQLException {

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
	
	@SuppressWarnings("unchecked")
	protected T getById(DBModel dbModel) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, SQLException {

		Connection connection = getConnection(dbModel.getConnection());
		Statement statement = null;
		Object element = new Object();
		String query = "SELECT * from " + dbModel.getTable() + " WHERE " + dbModel.getColumnIDName() + " = ? ";
		statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(query);
		while (rs.next()) {
			element = createNewObject(rs);
		}
		return (T) element;
	}
	
	protected List<T> getByParametr(DBModel dbModel, DBParametr parametr) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, SQLException {

		Connection connection = getConnection(dbModel.getConnection());
		List<T> list = new ArrayList<>();
		String query = "SELECT * from " + dbModel.getTable() + " WHERE ";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		preparedStatement.setInt(1, 1001);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			list.add(createNewObject(rs));
		}
		preparedStatement.close();
		connection.close();
		return list;
	}
	
	protected List<T> getByParameters(DBModel dbModel, List<DBParametr> parameters) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, SQLException {

		Connection connection = getConnection(dbModel.getConnection());
		List<T> list = new ArrayList<>();
		String query = "SELECT * from " + dbModel.getTable() + " WHERE ";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		preparedStatement.setInt(1, 1001);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			list.add(createNewObject(rs));
		}
		preparedStatement.close();
		connection.close();
		return list;
	}

	protected void save(DBModel dbModel) throws SQLException {

		Connection connection = getConnection(dbModel.getConnection());
		String query = "INSERT INTO  " + dbModel.getTable() + " ( " ;
		if(dbModel.getAutoIncrement().equals(AutoIncrement.FALSE)) {
			query += dbModel.getColumnIDName() + ", ";
		}
		int i = 0;
		for (Map.Entry<String, Object> entry : dbModel.getFields().entrySet()) {
			if(i++ == dbModel.getFields().size() - 1) {
				query += entry.getKey() + " )";
			} else {
				query += entry.getKey() + ", ";
			}
		}
		query += " VALUES ( ";
		
		if(dbModel.getAutoIncrement().equals(AutoIncrement.FALSE)) {
			query += "?, ";
		}
		i = 0;
		for (Iterator<Entry<String, Object>> iterator = dbModel.getFields().entrySet().iterator(); iterator.hasNext();) {
			if(i++ == dbModel.getFields().size() - 1) {
				query += " ? )";
			} else {
				query += "?, ";
			}
		}
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		i = 0;
		if(dbModel.getAutoIncrement().equals(AutoIncrement.FALSE)) {
			setPreparedStatement(i, preparedStatement, dbModel.getColumnIDValue());
		}
		for (Map.Entry<String, Object> entry : dbModel.getFields().entrySet()) {
			setPreparedStatement(i, preparedStatement, entry.getValue());
		}
		preparedStatement.executeQuery();
		connection.commit();
		preparedStatement.close();
		connection.close();
	}
	
	protected void update(DBModel dbModel) throws SQLException {

		Connection connection = getConnection(dbModel.getConnection());
		String query = "UPDATE " + dbModel.getTable() + " SET " ;
		int i = 0;
		for (Map.Entry<String, Object> entry : dbModel.getFields().entrySet()) {
			if(i++ == dbModel.getFields().size() - 1) {
				query += entry.getKey() + " = ? ";
			} else {
				query += entry.getKey() + " = ?, ";
			}
		}
		query += "WHERE " + dbModel.getColumnIDName() + " = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		i = 0;
		for (Map.Entry<String, Object> entry : dbModel.getFields().entrySet()) {
			setPreparedStatement(i, preparedStatement, entry.getValue());
		}
		setPreparedStatement(i, preparedStatement, dbModel.getColumnIDValue());
		preparedStatement.executeQuery();
		connection.commit();
		preparedStatement.close();
		connection.close();
	}
	
	protected void remove(DBModel dbModel) throws SQLException {

		Connection connection = getConnection(dbModel.getConnection());
		String query = "DELETE FROM " + dbModel.getTable() + " WHERE " + dbModel.getColumnIDName() + " = ? ";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		if (dbModel.getColumnIDValue() instanceof Integer) {
			preparedStatement.setInt(1, (int) dbModel.getColumnIDValue());
		} 
		else if (dbModel.getColumnIDValue() instanceof String) {
			preparedStatement.setString(1, (String) dbModel.getColumnIDValue());
		}
		preparedStatement.executeQuery();
		connection.commit();
		preparedStatement.close();
		connection.close();
	}
	
	private Connection getConnection(String connection) {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private T createNewObject(ResultSet rs) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
		Class<?> clazz = Class.forName(myClass.getName());
		Constructor<?> ctor = clazz.getConstructor();
		Object object = ctor.newInstance();
		for (Field field : object.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			if(field.isAnnotationPresent(ColumnId.class)) {
				field.setAccessible(true);
				ColumnId columnId = (ColumnId) field.getDeclaredAnnotation(ColumnId.class);
				field.set(object, getValueFromResultSet(rs, field, columnId.name()));
			}
			if(field.isAnnotationPresent(Column.class)) {
				Column column = (Column) field.getDeclaredAnnotation(Column.class);
				field.set(object, getValueFromResultSet(rs, field, column.name()));
			}
		}
		return (T) object;
	}
	
	private Object getValueFromResultSet(ResultSet rs, Field field, String columnName) throws SQLException {
		
		if (field.getType() == Integer.class) {
			return  rs.getInt(columnName);
	    } else if(field.getType() == String.class) {
	    	return rs.getString(columnName);
	    } else if(field.getType() == Float.class) {
	    	return rs.getFloat(columnName);
	    } else if(field.getType() == Boolean.class) {
	    	return rs.getBoolean(columnName);
	    } else if(field.getType() == Date.class) {
	    	return rs.getDate(columnName);
	    } else if(field.getType() == Blob.class) {
	    	return rs.getBlob(columnName);
	    } else if(field.getType() == Long.class) {
	    	return rs.getLong(columnName);
	    } else if(field.getType() == Double.class) {
	    	return rs.getDouble(columnName);
	    } else if(field.getType() == Byte.class) {
	    	return rs.getBytes(columnName);
	    } else {
	    	return null;
	    }
	}
	
	private void setPreparedStatement(int i, PreparedStatement preparedStatement, Object value) throws SQLException {
		
		if (value instanceof Integer) {
			preparedStatement.setInt(i++, (int) value);
	    } else if(value instanceof String) {
	    	preparedStatement.setString(i++, (String) value);
	    } else if(value instanceof Float) {
			preparedStatement.setFloat(i++, (float) value);
	    } else if(value instanceof Boolean) {
			preparedStatement.setBoolean(i++, (boolean) value);
	    } else if(value instanceof Date) {
			preparedStatement.setDate(i++, (Date) value);
	    } else if(value instanceof Blob) {
			preparedStatement.setBlob(i++, (Blob) value);
	    } else if(value instanceof Long) {
			preparedStatement.setLong(i++, (long) value);
	    } else if(value instanceof Double) {
			preparedStatement.setDouble(i++, (double) value);
	    } else if(value instanceof Byte) {
			preparedStatement.setByte(i++, (byte) value);
	    }
	}
}
