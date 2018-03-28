package pl.dbtool.models;

import java.util.Map;
import java.util.Set;

public class DBJoinTable {

	private String tableName;
	private String fieldName;
	private String joinColumnsName;
	private Object joinColumnsValue;
	private String joinColumnsFieldName;
	private String referencedColumnName;
	private String referencedColumnFieldName;
	private Relationship relationship;
	private Class<?> className;
	private Map<Object, Set<Object>> fieldsList;
	private Map<Object, Object> field;

	public enum Relationship {
		ManyToOne, ManyToMany
	}
	
	public String getJoinColumnsName() {
		return joinColumnsName;
	}
	public void setJoinColumnsName(String joinColumnsName) {
		this.joinColumnsName = joinColumnsName;
	}
	public String getReferencedColumnName() {
		return referencedColumnName;
	}
	public void setReferencedColumnName(String referencedColumnName) {
		this.referencedColumnName = referencedColumnName;
	}
	public Object getJoinColumnsValue() {
		return joinColumnsValue;
	}
	public void setJoinColumnsValue(Object joinColumnsValue) {
		this.joinColumnsValue = joinColumnsValue;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Class<?> getClassName() {
		return className;
	}
	public void setClassName(Class<?> className) {
		this.className = className;
	}
	public String getJoinColumnsFieldName() {
		return joinColumnsFieldName;
	}
	public void setJoinColumnsFieldName(String joinColumnsFieldName) {
		this.joinColumnsFieldName = joinColumnsFieldName;
	}
	public String getReferencedColumnFieldName() {
		return referencedColumnFieldName;
	}
	public void setReferencedColumnFieldName(String referencedColumnFieldName) {
		this.referencedColumnFieldName = referencedColumnFieldName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public Relationship getRelationship() {
		return relationship;
	}
	public void setRelationship(Relationship relationship) {
		this.relationship = relationship;
	}
	public Map<Object, Set<Object>> getFieldsList() {
		return fieldsList;
	}
	public void setFieldsList(Map<Object, Set<Object>> fieldsList) {
		this.fieldsList = fieldsList;
	}
	public Map<Object, Object> getField() {
		return field;
	}
	public void setField(Map<Object, Object> field) {
		this.field = field;
	}
}
