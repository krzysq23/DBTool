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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.dbtool.annotations.Column;
import pl.dbtool.annotations.ColumnId;
import pl.dbtool.annotations.SequenceGenerator;
import pl.dbtool.annotations.ColumnId.GenerationType;
import pl.dbtool.models.DBJoinTable;
import pl.dbtool.models.DBJoinTable.Relationship;
import pl.dbtool.models.DBJoinedColumn;
import pl.dbtool.models.DBModel;
import pl.dbtool.models.DBParametr;

public class DBServiceDao<T> {

	public DBServiceDao(Class< T > tClass) {
		super();
		this.myClass = tClass;
	}
	
	private Class< T > myClass;
	
	protected List<T> getAll(DBModel dbModel) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, SQLException, NoSuchFieldException {

		Connection connection = getConnection(dbModel.getConnection());
		boolean isJoined = false;
		getJoinedObjectList(dbModel);
		if(dbModel.getJoinTables() != null && dbModel.getJoinTables().size() > 0) {
			isJoined = true;
		}
		String query = "SELECT * FROM " + dbModel.getTable();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(query);
		List<T> list = new ArrayList<>();
		while (rs.next()) {
			T element = createNewObject(rs);
			if(isJoined) {
				for (DBJoinTable joinTab : dbModel.getJoinTables()) {
					Field fieldJoinedColumn = element.getClass().getDeclaredField(joinTab.getJoinColumnsFieldName());
					Field fieldJoinedList = element.getClass().getDeclaredField(joinTab.getFieldName());
					fieldJoinedColumn.setAccessible(true);
					fieldJoinedList.setAccessible(true);
					Object value = fieldJoinedColumn.get(element);
					if(value != null && joinTab.getRelationship().equals(Relationship.ManyToMany) && joinTab.getFieldsList() != null) {
						fieldJoinedList.set(element, joinTab.getFieldsList().get(value));
					}
					if(value != null && joinTab.getRelationship().equals(Relationship.ManyToOne) && joinTab.getField() != null) {
						fieldJoinedList.set(element, joinTab.getField().get(value));
					}
				}
			}
			list.add(element);
		}
		connection.close();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	protected T getById(DBModel dbModel) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, SQLException, NoSuchFieldException {

		Connection connection = getConnection(dbModel.getConnection());
		boolean isJoined = false;
		getJoinedObjectList(dbModel);
		if(dbModel.getJoinTables() != null && dbModel.getJoinTables().size() > 0) {
			isJoined = true;
		}
		Object element = new Object();
		String query = "SELECT * from " + dbModel.getTable() + " WHERE " + dbModel.getColumnIDName() + " = ? ";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		setPreparedStatement(1, preparedStatement, dbModel.getColumnIDValue());
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			element = createNewObject(rs);
			if(isJoined) {
				for (DBJoinTable joinTab : dbModel.getJoinTables()) {
					Field fieldJoinedColumn = element.getClass().getDeclaredField(joinTab.getJoinColumnsFieldName());
					Field fieldJoinedList = element.getClass().getDeclaredField(joinTab.getFieldName());
					fieldJoinedColumn.setAccessible(true);
					fieldJoinedList.setAccessible(true);
					Object value = fieldJoinedColumn.get(element);
					if(value != null && joinTab.getRelationship().equals(Relationship.ManyToMany) && joinTab.getFieldsList() != null) {
						fieldJoinedList.set(element, joinTab.getFieldsList().get(value));
					}
					if(value != null && joinTab.getRelationship().equals(Relationship.ManyToOne) && joinTab.getField() != null) {
						fieldJoinedList.set(element, joinTab.getField().get(value));
					}
				}
			}
		}
		preparedStatement.close();
		connection.close();
		return (T) element;
	}
	
	protected List<T> getByParametr(DBModel dbModel, DBParametr parametr) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, SQLException, NoSuchFieldException {

		Connection connection = getConnection(dbModel.getConnection());
		boolean isJoined = false;
		getJoinedObjectList(dbModel);
		if(dbModel.getJoinTables() != null && dbModel.getJoinTables().size() > 0) {
			isJoined = true;
		}
		List<T> list = new ArrayList<>();
		String query = "SELECT * from " + dbModel.getTable() + " WHERE " + parametr.getFieldName() + " " + parametr.getOperator() + " ?";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		setPreparedStatement(1, preparedStatement, parametr.getValue());
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			T element = createNewObject(rs);
			if(isJoined) {
				for (DBJoinTable joinTab : dbModel.getJoinTables()) {
					Field fieldJoinedColumn = element.getClass().getDeclaredField(joinTab.getJoinColumnsFieldName());
					Field fieldJoinedList = element.getClass().getDeclaredField(joinTab.getFieldName());
					fieldJoinedColumn.setAccessible(true);
					fieldJoinedList.setAccessible(true);
					Object value = fieldJoinedColumn.get(element);
					if(value != null && joinTab.getRelationship().equals(Relationship.ManyToMany) && joinTab.getFieldsList() != null) {
						fieldJoinedList.set(element, joinTab.getFieldsList().get(value));
					}
					if(value != null && joinTab.getRelationship().equals(Relationship.ManyToOne) && joinTab.getField() != null) {
						fieldJoinedList.set(element, joinTab.getField().get(value));
					}
				}
			}
			list.add(element);
		}
		preparedStatement.close();
		connection.close();
		return list;
	}
	
	protected List<T> getByParameters(DBModel dbModel, List<DBParametr> parameters, String conjunction) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, SQLException, NoSuchFieldException {

		Connection connection = getConnection(dbModel.getConnection());
		boolean isJoined = false;
		getJoinedObjectList(dbModel);
		if(dbModel.getJoinTables() != null && dbModel.getJoinTables().size() > 0) {
			isJoined = true;
		}
		List<T> list = new ArrayList<>();
		String query = "SELECT * from " + dbModel.getTable() + " WHERE ";
		int i = 0;
		for(DBParametr param : parameters) {
			if(i == 0) {
				query += param.getFieldName() + " " + param.getOperator() + " ?";
			} else {
				query += conjunction + param.getFieldName() + " " + param.getOperator() + " ?";
			}
			i++;
		}
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		i = 1;
		for(DBParametr param : parameters) {
			setPreparedStatement(i, preparedStatement, param.getValue());
			i++;
		}
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			T element = createNewObject(rs);
			if(isJoined) {
				for (DBJoinTable joinTab : dbModel.getJoinTables()) {
					Field fieldJoinedColumn = element.getClass().getDeclaredField(joinTab.getJoinColumnsFieldName());
					Field fieldJoinedList = element.getClass().getDeclaredField(joinTab.getFieldName());
					fieldJoinedColumn.setAccessible(true);
					fieldJoinedList.setAccessible(true);
					Object value = fieldJoinedColumn.get(element);
					if(value != null && joinTab.getRelationship().equals(Relationship.ManyToMany)) {
						fieldJoinedList.set(element, joinTab.getFieldsList().get(value));
					}
				}
			}
			list.add(element);
		}
		preparedStatement.close();
		connection.close();
		return list;
	}

	@SuppressWarnings("unused")
	protected void save(DBModel dbModel) throws SQLException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException {

		Connection connection = getConnection(dbModel.getConnection());
		boolean isAutoIncrement = false;
		Object idValue = dbModel.getColumnIDValue();
		for (Field field : myClass.getDeclaredFields()) {
			if(field.isAnnotationPresent(ColumnId.class)) {
				ColumnId columnId = (ColumnId) field.getDeclaredAnnotation(ColumnId.class);
				if(columnId.strategy().equals(GenerationType.SEQUENCE)) {
					SequenceGenerator sequenceGenerator = (SequenceGenerator) field.getDeclaredAnnotation(SequenceGenerator.class);
					idValue = getNextValueFromSequence(sequenceGenerator.sequenceName(), connection);
					isAutoIncrement = true;
				} else if (columnId.strategy().equals(GenerationType.OWN)) {
					isAutoIncrement = true;
				}
			}
		}
		String query = "INSERT INTO  " + dbModel.getTable() + " ( " ;
		if(isAutoIncrement) {
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
		
		if(isAutoIncrement) {
			query += "?, ";
		}
		i = 0;
		for (Map.Entry<String, Object> entry : dbModel.getFields().entrySet()) {
			if(i++ == dbModel.getFields().size() - 1) {
				query += "? )";
			} else {
				query += "?, ";
			}
		}
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		i = 1;
		if(isAutoIncrement) {
			setPreparedStatement(i, preparedStatement, idValue);
			i++;
		}
		for (Map.Entry<String, Object> entry : dbModel.getFields().entrySet()) {
			setPreparedStatement(i, preparedStatement, entry.getValue());
			i++;
		}
		preparedStatement.executeQuery();
		connection.commit();
		preparedStatement.close();
		if(dbModel.getJoinTables() != null) {
			for (DBJoinTable joinTable : dbModel.getJoinTables()) {
				if(joinTable.getObjectList() != null) {
					for ( Object element : joinTable.getObjectList()) {
						saveJoinColumn(joinTable, connection, element, idValue);
					}
				}
				if(joinTable.getObject() != null) {
					saveJoinColumn(joinTable, connection, joinTable.getObject(), idValue);
				}
			}
		}
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
		i = 1;
		for (Map.Entry<String, Object> entry : dbModel.getFields().entrySet()) {
			setPreparedStatement(i, preparedStatement, entry.getValue());
			i++;
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
		setPreparedStatement(1, preparedStatement, dbModel.getColumnIDValue());
		preparedStatement.executeQuery();
		connection.commit();
		preparedStatement.close();
		if(dbModel.getJoinTables() != null) {
			for (DBJoinTable joinTable : dbModel.getJoinTables()) {
				if(joinTable.getObjectList() != null) {
					removeJoinColumn(joinTable.getTableName(), joinTable.getReferencedColumnName(), dbModel.getColumnIDValue(), connection);
				}
				if(joinTable.getObject() != null) {
					removeJoinColumn(joinTable.getTableName(), joinTable.getReferencedColumnName(), dbModel.getColumnIDValue(), connection);
				}
			}
		}
		connection.close();
	}
	
	private Connection getConnection(String connection) {
		Connection manager = null ;
	    return manager;
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
	
	private DBJoinedColumn createNewJoinedObject(ResultSet rs, Class<?> clazz, String columnName) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
		Constructor<?> ctor = clazz.getConstructor();
		Object object = ctor.newInstance();
		DBJoinedColumn element = new DBJoinedColumn();
		for (Field field : object.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			if(field.isAnnotationPresent(ColumnId.class)) {
				field.setAccessible(true);
				ColumnId columnId = (ColumnId) field.getDeclaredAnnotation(ColumnId.class);
				Object value = getValueFromResultSet(rs, field, columnId.name());
				if(columnId.name().equals(columnName)) element.setJoinedColumnValue(value);
				field.set(object, value);
			}
			if(field.isAnnotationPresent(Column.class)) {
				Column column = (Column) field.getDeclaredAnnotation(Column.class);
				Object value = getValueFromResultSet(rs, field, column.name());
				if(column.name().equals(columnName)) element.setJoinedColumnValue(value);
				field.set(object, value);
			}
		}
		element.setElement(object);
		return element ;
	}
	
	private Object getValueFromResultSet(ResultSet rs, Field field, String columnName) throws SQLException {
		
		if (field.getType().equals(Integer.TYPE)) {
			return  rs.getInt(columnName);
	    } else if (field.getType().equals(Integer.class)) {
			return  rs.getInt(columnName);
	    } else if(field.getType().equals(String.class)) {
	    	return rs.getString(columnName);
	    } else if(field.getType().equals(Float.TYPE)) {
	    	return rs.getFloat(columnName);
	    } else if(field.getType().equals(Boolean.TYPE)) {
	    	return rs.getBoolean(columnName);
	    } else if(field.getType().equals(Date.class)) {
	    	return rs.getDate(columnName);
	    } else if(field.getType().equals(Blob.class)) {
	    	return rs.getBlob(columnName);
	    } else if(field.getType().equals(Long.TYPE)) {
	    	return rs.getLong(columnName);
	    } else if(field.getType().equals(Double.TYPE)) {
	    	return rs.getDouble(columnName);
	    } else if(field.getType().equals(Byte.TYPE)) {
	    	return rs.getBytes(columnName);
	    } else {
	    	return null;
	    }
	}
	
	private void setPreparedStatement(int i, PreparedStatement preparedStatement, Object value) throws SQLException {
		
		if (value instanceof Integer) {
			preparedStatement.setInt(i, (int) value);
	    } else if(value instanceof String) {
	    	preparedStatement.setString(i, (String) value);
	    } else if(value instanceof Float) {
			preparedStatement.setFloat(i, (float) value);
	    } else if(value instanceof Boolean) {
			preparedStatement.setBoolean(i, (boolean) value);
	    } else if(value instanceof Date) {
			preparedStatement.setDate(i, (Date) value);
	    } else if(value instanceof Blob) {
			preparedStatement.setBlob(i, (Blob) value);
	    } else if(value instanceof Long) {
			preparedStatement.setLong(i, (long) value);
	    } else if(value instanceof Double) {
			preparedStatement.setDouble(i, (double) value);
	    } else if(value instanceof Byte) {
			preparedStatement.setByte(i, (byte) value);
	    }
	}
	
	private void getJoinedObjectList(DBModel dbModel) throws SQLException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		if(dbModel.getJoinTables() != null && dbModel.getJoinTables().size() > 0) {
			for (DBJoinTable record : dbModel.getJoinTables()) {
				Connection connection = getConnection(record.getConnection());
				if(record.getRelationship().equals(Relationship.ManyToMany))
				{
					Map<Object, Set<Object>> listElement = new HashMap<Object, Set<Object>>();
					Class<?> clazz = record.getClassName();
					String queryJoin = "SELECT " + record.getTableName() + ".* FROM " + dbModel.getTable() + " JOIN "
							+  record.getTableName() + " ON " + record.getTableName() + "." + record.getReferencedColumnName() 
									+ " = " + dbModel.getTable() + "." + record.getJoinColumnsName();
					Statement statementJoin = connection.createStatement();
					ResultSet rsJoin = statementJoin.executeQuery(queryJoin);
					while (rsJoin.next()) {
						DBJoinedColumn element = createNewJoinedObject(rsJoin, clazz, record.getReferencedColumnName());
						if (!listElement.containsKey(element.getJoinedColumnValue())) {
							Set<Object> newList = new HashSet<Object>();
							newList.add(element.getElement());
							listElement.put(element.getJoinedColumnValue(), newList);
						} else {
							Set<Object> oldList = listElement.get(element.getJoinedColumnValue());
							oldList.add(element.getElement());
						}
					}
					record.setFieldsList(listElement);
				}
				if(record.getRelationship().equals(Relationship.ManyToOne))
				{
					Map<Object, Object> listElement = new HashMap<Object, Object>();
					Class<?> clazz = record.getClassName();
					String queryJoin = "SELECT DISTINCT " + record.getTableName() + ".* FROM " + dbModel.getTable() + " JOIN "
							+  record.getTableName() + " ON " + record.getTableName() + "." + record.getReferencedColumnName() 
									+ " = " + dbModel.getTable() + "." + record.getJoinColumnsName();
					Statement statementJoin = connection.createStatement();
					ResultSet rsJoin = statementJoin.executeQuery(queryJoin);
					while (rsJoin.next()) {
						DBJoinedColumn element = createNewJoinedObject(rsJoin, clazz, record.getReferencedColumnName());
						listElement.put(element.getJoinedColumnValue(), element.getElement());
					}
					record.setField(listElement);
				}
				connection.close();
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void saveJoinColumn(DBJoinTable joinTable, Connection connection, Object element, Object id) throws SQLException, IllegalArgumentException, IllegalAccessException {
		
		boolean isAutoIncrement = false;
		Class<?> obj = element.getClass();
		Object idValue = null;
		String idColumnName = "";
		Map<String, Object> fields = new HashMap<>();
		for (Field field : obj.getDeclaredFields()) {
			field.setAccessible(true);
			Object value = field.get(element);
			if(field.isAnnotationPresent(ColumnId.class)) {
				idValue = value;
				ColumnId columnId = (ColumnId) field.getDeclaredAnnotation(ColumnId.class);
				idColumnName = columnId.name();
				if(columnId.strategy().equals(GenerationType.SEQUENCE)) {
					SequenceGenerator sequenceGenerator = (SequenceGenerator) field.getDeclaredAnnotation(SequenceGenerator.class);
					idValue = getNextValueFromSequence(sequenceGenerator.sequenceName(), connection);
					isAutoIncrement = true;
				} else if (columnId.strategy().equals(GenerationType.OWN)) {
					isAutoIncrement = true;
				}
			}
			if(field.isAnnotationPresent(Column.class)) {
				Column column = (Column) field.getDeclaredAnnotation(Column.class);
				if(column.name().equals(joinTable.getReferencedColumnName())) {
					fields.put(column.name(), id);
				} else if(value != null) {
					fields.put(column.name(), value);
				}
			}
		}
		String query = "INSERT INTO  " + joinTable.getTableName() + " ( " ;
		if(isAutoIncrement) {
			query += idColumnName + ", ";
		}
		int i = 0;
		for (Map.Entry<String, Object> entry : fields.entrySet()) {
			if(i++ == fields.size() - 1) {
				query += entry.getKey() + " )";
			} else {
				query += entry.getKey() + ", ";
			}
		}
		query += " VALUES ( ";
		
		if(isAutoIncrement) {
			query += "?, ";
		}
		i = 0;
		for (Map.Entry<String, Object> entry : fields.entrySet()) {
			if(i++ == fields.size() - 1) {
				query += "? )";
			} else {
				query += "?, ";
			}
		}
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		i = 1;
		if(isAutoIncrement) {
			setPreparedStatement(i, preparedStatement, idValue);
			i++;
		}
		for (Map.Entry<String, Object> entry : fields.entrySet()) {
			setPreparedStatement(i, preparedStatement, entry.getValue());
			i++;
		}
		preparedStatement.executeQuery();
		connection.commit();
		preparedStatement.close();
	}
	
	private void removeJoinColumn(String tableName, String columnName, Object value, Connection connection) throws SQLException {
		
		String query = "DELETE FROM " + tableName + " WHERE " + columnName + " = ? ";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		setPreparedStatement(1, preparedStatement, value);
		preparedStatement.executeQuery();
		connection.commit();
		preparedStatement.close();
	}
	
	private int getNextValueFromSequence(String sequence, Connection conn) throws SQLException {
		
		String query = "SELECT " + sequence + ".nextval FROM dual";
		int nextValue = 1 ;
		PreparedStatement pst = conn.prepareStatement(query);
		synchronized( this ) {
			ResultSet rs = pst.executeQuery();
			if(rs.next())
			{
				nextValue = rs.getInt(1);
			}
		}
		return nextValue;
	}
}
