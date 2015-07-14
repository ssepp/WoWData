package wowData.business.db;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "Item" )
public class Item {

	@Id
	private Integer id;
	private String name;
	private Integer itemClass;
	private Integer itemSubClass;
	
	@Override
	public String toString() {
		return name;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getItemClass() {
		return itemClass;
	}
	public void setItemClass(Integer itemClass) {
		this.itemClass = itemClass;
	}
	public Integer getItemSubClass() {
		return itemSubClass;
	}
	public void setItemSubClass(Integer itemSubClass) {
		this.itemSubClass = itemSubClass;
	}
	
}
