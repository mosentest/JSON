package cn.hyf.notepad;

public class Notepad {
	private String name;
	private String hidden;
	
	public Notepad(String name,String hidden){
		this.name = name;
		this.hidden=hidden;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getHidden() {
		return hidden;
	}
	public void setHidden(String hidden) {
		this.hidden = hidden;
	}
	
}
