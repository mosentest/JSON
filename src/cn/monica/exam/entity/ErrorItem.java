package cn.monica.exam.entity;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name="error")
public class ErrorItem {
	@Id(column="id")
	private int id;
	private int tid;
	private int type;
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getTid(){
		return tid;
	}
	
	public void setTid(int tid){
		this.tid = tid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}	
}
