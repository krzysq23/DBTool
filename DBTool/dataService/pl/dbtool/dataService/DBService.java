package pl.dbtool.dataService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.dbtool.annotations.Column;
import pl.dbtool.annotations.ColumnId;
import pl.dbtool.annotations.DBConnection;
import pl.dbtool.annotations.Table;
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
				| NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException | SQLException e) {
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
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<T> getByParametr(DBParametr parametr) {
		
		try {
			DBModel dbModel = getDBModelByClass(myClass);
			List<T> list = getByParametr(dbModel, parametr);
			return list;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException 
				| NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<T> getByParameters(List<DBParametr> parameters) {
		
		try {
			DBModel dbModel = getDBModelByClass(myClass);
			List<T> list = getByParameters(dbModel, parameters);
			return list;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException 
				| NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void save(T entity) {
		
		DBModel dbModel = getDBModelByObject(entity);
		save(dbModel);
	}

	@Override
	public void update(T entity) {
		
		DBModel dbModel = getDBModelByObject(entity);
		update(dbModel);
	}

	@Override
	public void remove(T entity) {
		DBModel dbModel = getDBModelByObject(entity);
		remove(dbModel);
	}
	
	private DBModel getDBModelByClass(Class< T > obj) {
		DBModel dbModel = new DBModel();
		
		try {
			
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
				if(field.isAnnotationPresent(ColumnId.class)) {
					field.setAccessible(true);
					ColumnId columnId = (ColumnId) field.getDeclaredAnnotation(ColumnId.class);
					Object value = field.get(object);
					dbModel.setColumnIDName(columnId.name());
					dbModel.setColumnIDValue(value);
				}
				if(field.isAnnotationPresent(Column.class)) {
					field.setAccessible(true);
					Object value = field.get(object);
					Column column = (Column) field.getDeclaredAnnotation(Column.class);
					fields.put(column.name(), value);
				}
			}
			
			dbModel.setFields(fields);
				
		} catch (IllegalArgumentException | SecurityException | IllegalAccessException  e) {
			e.printStackTrace();
		}
		return dbModel;
	}

}
