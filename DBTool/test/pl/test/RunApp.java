package pl.test;

import pl.dbtool.dataService.DBService;
import pl.models.Test;

public class RunApp {

	static DBService<Test> dao = new DBService<>(Test.class);
	
	public static void main(String[] args) {
		Test test = new Test();
		test.setId(1);
		test.setTitle("tutul");
		test.setName("nazwa");
		dao.save(test);
		dao.update(test);
		dao.remove(test);
		
		Test item = (Test) dao.getById(new Test());
		System.out.println("getId " + item.getId());
		System.out.println("getName " + item.getName());
		System.out.println("getTitle " + item.getTitle());
	}
	
}
	