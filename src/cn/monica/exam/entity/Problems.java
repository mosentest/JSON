package cn.monica.exam.entity;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name="problems")
public class Problems {
	@Id(column="id")
	private int id;
	private String title;
	private String answer;
	private String a;
	private String b;
	private String c;
	private String d;
	private int type; //1-填写  2-判断  3-选择  4-问题
	private boolean isFalse;
	
	public boolean isFalse() {
		return isFalse;
	}
	public void setFalse(boolean isFalse) {
		this.isFalse = isFalse;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public String getB() {
		return b;
	}
	public void setB(String b) {
		this.b = b;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
	public String getD() {
		return d;
	}
	public void setD(String d) {
		this.d = d;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
