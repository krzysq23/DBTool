package pl.dbtool.dataService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.dbtool.annotations.Column;
import pl.dbtool.annotations.ColumnId;
import pl.dbtool.annotations.DBConnection;
import pl.dbtool.annotations.Table;
import pl.models.DBModel;

public class DBService implements IDBService{

	@Override
	public List<Object> getAll(Object object) {
		
		DBModel dbModel = getDBModel(object);
		List<Object> list = new ArrayList<>();
		
		return list;
	}

	@Override
	public List<Object> getById(Object object, Object id) {
		System.out.println("po³¹czenie " + id);
		return null;
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
//					Object name = field.getName();
//					Object type = field.getType();
					Object value = field.get(object);
					fields.put("ID", value);
				}
				if(field.isAnnotationPresent(Column.class)) {
					field.setAccessible(true);
					Object value = field.get(object);
					Column column = (Column) field.getDeclaredAnnotation(Column.class);
					fields.put(column.name(), value);
				}
			}
			dbModel.setFields(fields);
			System.out.println("po³¹czenie " + dbModel.getConnection());
			System.out.println("tabela " + dbModel.getConnection());
			
			for(Map.Entry<String, Object> entry : dbModel.getFields().entrySet()) {
			    String columnName = entry.getKey();
			    Object value = entry.getValue();
			    System.out.println("Kolumna " + columnName);
			    System.out.println("Wartoœæ " + value);
			}
				
		} catch (IllegalArgumentException | SecurityException | IllegalAccessException  e) {
			e.printStackTrace();
		}
		return dbModel;
	}
}
