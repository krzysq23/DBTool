package pl.dbtool.dataService;

import java.util.List;

import pl.dbtool.models.DBParametr;

public interface IDBService<T>{

	public List<T> getAll();
	public T getById(final Object id);
	public List<T> getByParametr(DBParametr parametr);
	public List<T> getByParameters(List<DBParametr> parameters);
	public void save(final T entity);
	public void update(final T entity);
	public void remove(final T entity);
}
