package com.pangugle.im.model;

public class GroupType {
	
	private long id;
	private String name;
	private String remark;
	private long maxCapacity;
	
	public static String getColumnPrefix(){
        return "type";
    }
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(long maxCapacity) {
		this.maxCapacity = maxCapacity;
	}
	
	public static enum DefaultConfig {
		LOW("初级", 30),
		MIDDLE("中级", 50),
		HIGTH("高级", 100);
		
		private String type;
		private long maxCapacity;
		
		private DefaultConfig(String type, long capacity)
		{
			this.type = type;
			this.maxCapacity = capacity;
		}
		
		public String getType()
		{
			return type;
		}
		
		public long getCapacity()
		{
			return maxCapacity;
		}
		
	}


}
