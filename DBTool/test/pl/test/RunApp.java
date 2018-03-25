package pl.test;

import pl.dbtool.dataService.DBService;
import pl.dbtool.dataService.IDBService;
import pl.models.Test;

public class RunApp {

	public static IDBService dbService = new DBService();
	
	public static void main(String[] args) {
		Test test = new Test();
		test.setId(1);
		test.setTitle("tutul");
		test.setName("nazwa");
		dbService.getAll(test);
		dbService.getById(new Test(), 11);
		dbService.save(test);
		dbService.update(test);
		dbService.remove(test);
	}
	
}
	