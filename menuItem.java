

public class menuItem{
	
	String name;
	int id;
	boolean isRefillable;
	String SRefillable;
	String type;
	
	public menuItem(String name, int id, String type, String SRefillable){
		this.name = name;
		this.id = id;
		if(SRefillable == "TRUE"){
			this.isRefillable = true;
		}
		else{
			this.isRefillable = false;
		}
		
	}

}
