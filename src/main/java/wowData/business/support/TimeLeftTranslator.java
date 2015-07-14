package wowData.business.support;

public enum TimeLeftTranslator {

	SHORT(-1), MEDIUM(-2), LONG(-3), VERY_LONG(-4);
	
	private Integer id;
	
	TimeLeftTranslator(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
}
