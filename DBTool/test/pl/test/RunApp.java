package pl.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import pl.annotations.Column;
import pl.annotations.ColumnId;
import pl.annotations.DBConnection;
import pl.annotations.Table;
import pl.models.DBModel;
import pl.models.Test;

public class RunApp {

	public static void main(String[] args) {
		Test test = new Test();
		test.setId(1);
		test.setTitle("tutul");
		test.setName("nazwa");
		calssObj(test);

	}

	public static void calssObj(Object object) {

		try {
			
			Class<?> obj = object.getClass();
	
			DBModel dbModel = new DBModel();
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
	}
	
}
	