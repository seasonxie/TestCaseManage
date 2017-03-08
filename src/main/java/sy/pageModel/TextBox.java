package sy.pageModel;

import java.util.Date;

public class TextBox implements java.io.Serializable {



	private String id;
	private String type;
	private String boundary_up;
	private String boundary_down;
	private String blank_q;
	private String blank_z;
	private String blank_h;
	private String jh;
	private String sign;
	private String letter_d;
	private String letter_x;
	private String letter_m;
	private String number_i;
	private String number_d;
	private String htmlencode;
	private String urlencode;
	private String updater;
	private String mix;
	private Date updatetime;
	private String name;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBoundary_up() {
		return boundary_up;
	}
	public void setBoundary_up(String boundary_up) {
		this.boundary_up = boundary_up;
	}
	public String getBoundary_down() {
		return boundary_down;
	}
	public void setBoundary_down(String boundary_down) {
		this.boundary_down = boundary_down;
	}
	public String getBlank_q() {
		return blank_q;
	}
	public void setBlank_q(String blank_q) {
		this.blank_q = blank_q;
	}
	public String getBlank_z() {
		return blank_z;
	}
	public void setBlank_z(String blank_z) {
		this.blank_z = blank_z;
	}
	public String getBlank_h() {
		return blank_h;
	}
	public void setBlank_h(String blank_h) {
		this.blank_h = blank_h;
	}
	public String getJh() {
		return jh;
	}
	public void setJh(String jh) {
		this.jh = jh;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getLetter_d() {
		return letter_d;
	}
	public void setLetter_d(String letter_d) {
		this.letter_d = letter_d;
	}
	public String getLetter_x() {
		return letter_x;
	}
	public void setLetter_x(String letter_x) {
		this.letter_x = letter_x;
	}
	public String getLetter_m() {
		return letter_m;
	}
	public void setLetter_m(String letter_m) {
		this.letter_m = letter_m;
	}
	public String getNumber_i() {
		return number_i;
	}
	public void setNumber_i(String number_i) {
		this.number_i = number_i;
	}
	public String getNumber_d() {
		return number_d;
	}
	public void setNumber_d(String number_d) {
		this.number_d = number_d;
	}
	public String getHtmlencode() {
		return htmlencode;
	}
	public void setHtmlencode(String htmlencode) {
		this.htmlencode = htmlencode;
	}
	public String getUrlencode() {
		return urlencode;
	}
	public void setUrlencode(String urlencode) {
		this.urlencode = urlencode;
	}
	public String getUpdater() {
		return updater;
	}
	public void setUpdater(String updater) {
		this.updater = updater;
	}
	public String getMix() {
		return mix;
	}
	public void setMix(String mix) {
		this.mix = mix;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

}
