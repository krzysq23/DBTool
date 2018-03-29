package pl.models;

import java.util.HashSet;
import java.util.Set;

import pl.dbtool.annotations.Column;
import pl.dbtool.annotations.ColumnId;
import pl.dbtool.annotations.ColumnId.GenerationType;
import pl.dbtool.annotations.ManyToMany.FetchType;
import pl.dbtool.annotations.DBConnection;
import pl.dbtool.annotations.JoinTable;
import pl.dbtool.annotations.ManyToMany;
import pl.dbtool.annotations.ManyToOne;
import pl.dbtool.annotations.SequenceGenerator;
import pl.dbtool.annotations.Table;

@DBConnection(connection = "oracle")
@Table(name = "TableTest")
public class Test {

	@ColumnId(strategy = GenerationType.SEQUENCE, name = "ID")
	@SequenceGenerator(sequenceName = "SEQ_TEST_ID")
	private int Id;
	
	@Column(name = "TITLE")
	private String title;
	
	@Column(name = "NAME")
	private String name;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(tableName = "TEST_INFO", joinColumnsName = "ID", referencedColumnName = "TEST_ID")
	private Set<TestInfo> testInfo = new HashSet<TestInfo>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinTable(tableName = "TEST_INFO", joinColumnsName = "ID", referencedColumnName = "TEST_ID")
	private TestInfo testInfoObj ;
	
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

	public Set<TestInfo> getTestInfo() {
		return testInfo;
	}

	public void setTestInfo(Set<TestInfo> testInfo) {
		this.testInfo = testInfo;
	}

	public TestInfo getTestInfoObj() {
		return testInfoObj;
	}

	public void setTestInfoObj(TestInfo testInfoObj) {
		this.testInfoObj = testInfoObj;
	}
	
}
