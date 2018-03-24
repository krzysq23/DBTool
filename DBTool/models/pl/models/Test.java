package pl.models;

import pl.annotations.Column;
import pl.annotations.ColumnId;
import pl.annotations.DBConnection;
import pl.annotations.Table;

@DBConnection(connection = "oracle")
@Table(name = "TableTest")
public class Test {

	@ColumnId
	private int Id;
	
	@Column(name = "TITLE")
	private String title;
	
	@Column(name = "NAME")
	private String name;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
