package wowData.business.support;

public enum RegionSelection {
	
	EU(-1), US(-2);
	
	private Integer id;
	
	RegionSelection(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
}
