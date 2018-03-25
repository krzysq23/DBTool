package pl.models;

import java.util.Map;

public class DBModel {

	private String connection;
	private String table;
	private String columnIDName;
	private Object columnIDValue;
	
	public String getColumnIDName() {
		return columnIDName;
	}
	public void setColumnIDName(String columnIDName) {
		this.columnIDName = columnIDName;
	}
	public Object getColumnIDValue() {
		return columnIDValue;
	}
	public void setColumnIDValue(Object columnIDValue) {
		this.columnIDValue = columnIDValue;
	}
	private Map<String, Object> fields;
	
	public String getConnection() {
		return connection;
	}
	public void setConnection(String connection) {
		this.connection = connection;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public Map<String, Object> getFields() {
		return fields;
	}
	public void setFields(Map<String, Object> fields) {
		this.fields = fields;
	}
}
