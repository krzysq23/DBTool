package pl.test;

import java.util.List;

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
		
		Test item = dao.getById(1);
		System.out.println("getId " + item.getId());
		System.out.println("getName " + item.getName());
		System.out.println("getTitle " + item.getTitle());
		
		List<Test> list = dao.getAll();
		for (Test xxx : list) {
			System.out.println("xxx name " + xxx.getName());
			System.out.println("xxx title " + xxx.getTitle());
		}
	}
	
}
	