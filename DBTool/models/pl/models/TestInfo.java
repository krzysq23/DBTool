package pl.models;

import pl.dbtool.annotations.Column;
import pl.dbtool.annotations.ColumnId;
import pl.dbtool.annotations.DBConnection;
import pl.dbtool.annotations.Table;
import pl.dbtool.annotations.ColumnId.GenerationType;

@DBConnection(connection = "office")
@Table(name = "TEST_INFO")
public class TestInfo {

	@ColumnId( strategy = GenerationType.OWN , name = "ID")
	private String Id;
	
	@Column(name = "INFO")
	private String info;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "TEST_ID")
	private int testId;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTestId() {
		return testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}
	
}
