package pl.dbtool.dataService;

import java.util.List;

public interface IDBService {

	/**
	 * Metoda pobiera wszystkie elementy
	 * @param Object
	 * @return	Zwracana jest liczna element�w
	 */
	public List<Object> getAll(Object object);
	public List<Object> getById(Object object, Object id);
}
