package pl.dbtool.dataService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.dbtool.annotations.Column;
import pl.dbtool.annotations.ColumnId;
import pl.dbtool.annotations.DBConnection;
import pl.dbtool.annotations.JoinTable;
import pl.dbtool.annotations.ManyToMany;
import pl.dbtool.annotations.Table;
import pl.dbtool.models.DBJoinTable;
import pl.dbtool.models.DBJoinTable.Relationship;
import pl.dbtool.models.DBModel;
import pl.dbtool.models.DBParametr;

public class DBService<T> extends DBServiceDao<T> implements IDBService<T>{

	public DBService(Class< T > tClass) {
		super(tClass);
		this.myClass = tClass;
	}

	private Class< T > myClass;
	
	@Override
	public List<T> getAll() {
		
		try {
			DBModel dbModel = getDBModelByClass(myClass);
			List<T> list = getAll(dbModel);
			return list;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException 
				| NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException | SQLException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public T getById(Object id) {
		
		try {
			DBModel dbModel = getDBModelByClass(myClass);
			dbModel.setColumnIDValue(id);
			T element = getById(dbModel);
			return element;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException 
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | SQLException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<T> getByParametr(DBParametr parametr) {
		
		try {
			DBModel dbModel = getDBModelByClass(myClass);
			parametr.setFieldName(getFieldCollumn(parametr.getFieldName()));
			List<T> list = getByParametr(dbModel, parametr);
			return list;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException 
				| NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException | SQLException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<T> getByParameters(List<DBParametr> parameters, boolean isAnd) {
		
		try {
			DBModel dbModel = getDBModelByClass(myClass);
			for (DBParametr dbParametr : parameters) {
				dbParametr.setFieldName(getFieldCollumn(dbParametr.getFieldName()));
			}
			String conjunction = " AND ";
			if(!isAnd)  conjunction = " OR ";
			List<T> list = getByParameters(dbModel, parameters, conjunction);
			return list;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException 
				| NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException | SQLException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void save(T entity) {
		
		try {
			DBModel dbModel = getDBModelByObject(entity);
			save(dbModel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(T entity) {

		try {
			DBModel dbModel = getDBModelByObject(entity);
			update(dbModel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void remove(T entity) {
		
		try {
			DBModel dbModel = getDBModelByObject(entity);
			remove(dbModel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private DBModel getDBModelByClass(Class< T > obj) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException, InstantiationException, InvocationTargetException {
		DBModel dbModel = new DBModel();
		
		try {
			
			Annotation[] annotations = obj.getAnnotations();
			Class<?> clazz = Class.forName(myClass.getName());
			Constructor<?> ctor = clazz.getConstructor();
			Object object = ctor.newInstance();
			
			Map<String, Object> fields = new HashMap<String, Object>();
			Map<String, String> fieldNames = new HashMap<String, String>();
			List<DBJoinTable> joinTables = new ArrayList<DBJoinTable>();
			
			for(Annotation annotation : annotations){
			    if(annotation instanceof Table){
			    	Table myAnnotation = (Table) annotation;
			        dbModel.setTable(myAnnotation.name());
			    }
			    if(annotation instanceof DBConnection){
			    	DBConnection myAnnotation = (DBConnection) annotation;
			        dbModel.setConnection(myAnnotation.connection());
			    }
			}
			
			for (Field field : obj.getDeclaredFields()) {
				field.setAccessible(true);
				Object value = field.get(object);
				if(field.isAnnotationPresent(ColumnId.class)) {
					ColumnId columnId = (ColumnId) field.getDeclaredAnnotation(ColumnId.class);
					dbModel.setAutoIncrement(columnId.seq());
					dbModel.setColumnIDName(columnId.name());
					dbModel.setColumnIDValue(value);
					fieldNames.put(columnId.name(), field.getName());
				}
				if(field.isAnnotationPresent(Column.class)) {
					Column column = (Column) field.getDeclaredAnnotation(Column.class);
					fields.put(column.name(), value);
					fieldNames.put(column.name(), field.getName());
				}
			}
			
			for (Field field : obj.getDeclaredFields()) {
				if(field.isAnnotationPresent(JoinTable.class)) {
					JoinTable joinTable = (JoinTable) field.getDeclaredAnnotation(JoinTable.class);
					DBJoinTable table = new DBJoinTable();
					table.setTableName(joinTable.tableName());
					table.setFieldName(field.getName());
					table.setJoinColumnsName(joinTable.joinColumnsName());
					table.setReferencedColumnName(joinTable.referencedColumnName());
					table.setJoinColumnsFieldName(fieldNames.get(joinTable.joinColumnsName()));
					if(field.isAnnotationPresent(ManyToMany.class)) {
				        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
				        Class<?> stringListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
						table.setRelationship(Relationship.ManyToMany);
						table.setClassName(stringListClass);
					} else {
						table.setRelationship(Relationship.ManyToOne);
						table.setClassName(field.getType());
					}
					joinTables.add(table);
				}
			}
			
			dbModel.setJoinTables(joinTables);

		} catch (IllegalArgumentException | SecurityException  e) {
			e.printStackTrace();
		}
		return dbModel;
	}
	
	private DBModel getDBModelByObject(Object object) {
		DBModel dbModel = new DBModel();
		
		try {
			
			Class<?> obj = object.getClass();
	
			Map<String, Object> fields = new HashMap<String, Object>();
			Map<String, String> fieldNames = new HashMap<String, String>();
			List<DBJoinTable> joinTables = new ArrayList<DBJoinTable>();
			
			Annotation[] annotations = obj.getAnnotations();
	
			for(Annotation annotation : annotations){
			    if(annotation instanceof Table){
			    	Table myAnnotation = (Table) annotation;
			        dbModel.setTable(myAnnotation.name());
			    }
			    if(annotation instanceof DBConnection){
			    	DBConnection myAnnotation = (DBConnection) annotation;
			        dbModel.setConnection(myAnnotation.connection());
			    }
			}
			
			for (Field field : obj.getDeclaredFields()) {
				field.setAccessible(true);
				Object value = field.get(object);
				if(field.isAnnotationPresent(ColumnId.class)) {
					ColumnId columnId = (ColumnId) field.getDeclaredAnnotation(ColumnId.class);
					dbModel.setAutoIncrement(columnId.seq());
					dbModel.setColumnIDName(columnId.name());
					dbModel.setColumnIDValue(value);
					fieldNames.put(columnId.name(), field.getName());
				}
				if(field.isAnnotationPresent(Column.class)) {
					Column column = (Column) field.getDeclaredAnnotation(Column.class);
					fields.put(column.name(), value);
					fieldNames.put(column.name(), field.getName());
				}
			}
			for (Field field : obj.getDeclaredFields()) {
				if(field.isAnnotationPresent(JoinTable.class)) {
					JoinTable joinTable = (JoinTable) field.getDeclaredAnnotation(JoinTable.class);
					DBJoinTable table = new DBJoinTable();
					table.setTableName(joinTable.tableName());
					table.setFieldName(field.getName());
					table.setReferencedColumnName(joinTable.joinColumnsName());
					table.setReferencedColumnName(joinTable.referencedColumnName());
					table.setJoinColumnsFieldName(fieldNames.get(joinTable.joinColumnsName()));
					if(field.isAnnotationPresent(ManyToMany.class)) {
				        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
				        Class<?> stringListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
						table.setRelationship(Relationship.ManyToMany);
						table.setClassName(stringListClass);
					} else {
						table.setRelationship(Relationship.ManyToOne);
						table.setClassName(field.getType().getClass());
					}
					joinTables.add(table);
				}
			}
			
			dbModel.setFields(fields);
			if(joinTables != null && joinTables.size() > 0) {
				for (DBJoinTable table : joinTables) {
					table.setJoinColumnsValue(getValueFromFields(dbModel.getFields(), table.getJoinColumnsName()));
				}
			}
			dbModel.setJoinTables(joinTables);
			
		} catch (IllegalArgumentException | SecurityException | IllegalAccessException  e) {
			e.printStackTrace();
		}
		return dbModel;
	}
	
	private String getFieldCollumn(String fieldName) throws NoSuchFieldException, SecurityException {
		
		Field field = myClass.getDeclaredField(fieldName);
		Column column = (Column) field.getDeclaredAnnotation(Column.class);
		return column.name();
	
	}
	
	private Object getValueFromFields(Map<String, Object> dbModel, String columnName) {
		
		for(Map.Entry<String, Object> entry : dbModel.entrySet()) {
		    String column = entry.getKey();
		    if(column.equals(columnName)) {
		    	Object value = entry.getValue();
		    	return value;
		    }
		}
		return null;
	}
}
