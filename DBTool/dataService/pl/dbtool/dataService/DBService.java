package pl.dbtool.dataService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.dbtool.annotations.Column;
import pl.dbtool.annotations.ColumnId;
import pl.dbtool.annotations.DBConnection;
import pl.dbtool.annotations.Table;
import pl.dbtool.models.DBModel;
import pl.dbtool.models.DBParametr;

public class DBService extends DBServiceDao implements IDBService{

	@Override
	public List<Object> getAll(Object object) {
		
		try {
			DBModel dbModel = getDBModel(object);
			List<Object> list = getAll(object, dbModel);
			return list;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object getById(Object object, Object id) {
		
		try {
			DBModel dbModel = getDBModel(object);
			dbModel.setColumnIDValue(id);
			Object element = getById(object, dbModel);
			return element;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Object> getByParametr(Object object, DBParametr parametr) {
		
		try {
			DBModel dbModel = getDBModel(object);
			List<Object> list = getByParametr(object, dbModel, parametr);
			return list;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Object> getByParameters(Object object, List<DBParametr> parameters) {
		
		try {
			DBModel dbModel = getDBModel(object);
			List<Object> list = getByParameters(object, dbModel, parameters);
			return list;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void save(Object object) {
		
		DBModel dbModel = getDBModel(object);
		save(dbModel);
	}

	@Override
	public void update(Object object) {
		
		DBModel dbModel = getDBModel(object);
		update(dbModel);
	}
	
	@Override
	public void remove(Object object) {
		
		DBModel dbModel = getDBModel(object);
		remove(dbModel);
	}
	
	private DBModel getDBModel(Object object) {
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
