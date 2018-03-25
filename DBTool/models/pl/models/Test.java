package pl.models;

import pl.dbtool.annotations.Column;
import pl.dbtool.annotations.ColumnId;
import pl.dbtool.annotations.DBConnection;
import pl.dbtool.annotations.Table;

@DBConnection(connection = "oracle")
@Table(name = "TableTest")
public class Test {

	@ColumnId(name = "ID")
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
