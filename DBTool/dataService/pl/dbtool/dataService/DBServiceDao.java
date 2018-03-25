package pl.dbtool.dataService;

import java.util.ArrayList;
import java.util.List;

import pl.dbtool.models.DBModel;
import pl.dbtool.models.DBParametr;

public class DBServiceDao<T> {

	public DBServiceDao(Class< T > tClass) {
		super();
		this.myClass = tClass;
	}
	
	private Class< T > myClass;
	
	protected List<T> getAll(DBModel dbModel) throws InstantiationException, IllegalAccessException {
		System.out.println("getAll");
		getConnection(dbModel.getConnection());
		List<T> list = new ArrayList<>();
		return list;
	}
	
	protected T getById(DBModel dbModel) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		System.out.println("getById");
		getConnection(dbModel.getConnection());
		return null;
	}
	
	protected List<T> getByParametr(DBModel dbModel, DBParametr parametr) throws InstantiationException, IllegalAccessException {
		System.out.println("getByParametr");
		getConnection(dbModel.getConnection());
		List<T> list = new ArrayList<>();
		return list;
	}
	
	protected List<T> getByParameters(DBModel dbModel, List<DBParametr> parameters) throws InstantiationException, IllegalAccessException {
		System.out.println("getByParameters");
		getConnection(dbModel.getConnection());
		List<T> list = new ArrayList<>();
		return list;
	}

	protected void save(DBModel dbModel) {
		System.out.println("save");
		getConnection(dbModel.getConnection());
	}
	
	protected void update(DBModel dbModel) {
		System.out.println("update");
		getConnection(dbModel.getConnection());
	}
	
	protected void remove(DBModel dbModel) {
		System.out.println("remove");
		getConnection(dbModel.getConnection());
	}
	
	private void getConnection(String connection) {
		
	}
}
