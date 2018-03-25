package pl.dbtool.dataService;

import java.util.ArrayList;
import java.util.List;

import pl.dbtool.models.DBModel;
import pl.dbtool.models.DBParametr;

public class DBServiceDao {

	protected List<Object> getAll(Object object, DBModel dbModel) throws InstantiationException, IllegalAccessException {
		System.out.println("getAll");
		List<Object> list = new ArrayList<>();
		Object element = object.getClass().newInstance();
		list.add(element);
		return list;
	}
	
	protected Object getById(Object object, DBModel dbModel) throws InstantiationException, IllegalAccessException {
		System.out.println("getById");
		Object element = object.getClass().newInstance();
		return element;
	}
	
	protected List<Object> getByParametr(Object object, DBModel dbModel, DBParametr parametr) throws InstantiationException, IllegalAccessException {
		System.out.println("getByParametr");
		List<Object> list = new ArrayList<>();
		Object element = object.getClass().newInstance();
		list.add(element);
		return list;
	}
	
	protected List<Object> getByParameters(Object object, DBModel dbModel, List<DBParametr> parameters) throws InstantiationException, IllegalAccessException {
		System.out.println("getByParameters");
		List<Object> list = new ArrayList<>();
		Object element = object.getClass().newInstance();
		list.add(element);
		return list;
	}

	protected void save(DBModel dbModel) {
		System.out.println("save");
	}
	
	protected void update(DBModel dbModel) {
		System.out.println("update");
	}
	
	protected void remove(DBModel dbModel) {
		System.out.println("remove");
	}
}
