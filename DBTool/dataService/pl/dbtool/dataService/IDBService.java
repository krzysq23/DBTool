package pl.dbtool.dataService;

import java.util.List;

import pl.dbtool.models.DBParametr;

public interface IDBService {

	public List<Object> getAll(Object object);
	public Object getById(Object object, Object id);
	public List<Object> getByParametr(Object object, DBParametr parametr);
	public List<Object> getByParameters(Object object, List<DBParametr> parameters);
	public void save(Object object);
	public void update(Object object);
	public void remove(Object object);
}
