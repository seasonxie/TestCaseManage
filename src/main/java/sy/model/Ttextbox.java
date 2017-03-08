package sy.model;

import java.sql.Clob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "ttestdata_textbox")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Ttextbox implements java.io.Serializable {

	private String id;


	private String type;
	private String boundary;
	private String blank;
	private String jh;
	private String sign;
	private String letter;
	private String number;
	private String htmlencode;
	private String urlencode;
	private String updater;
	private String mix;
	private Date updatetime;
	private String name;
	@Column(name = "NAME", length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Ttextbox() {
	}

	public Ttextbox(String id) {
		this.id = id;
	}

	public Ttextbox(String id,String name,String type,String boundary,String blank,String jh,String sign,String letter,String number,String htmlencode,String urlencode,String mix,String updater, Date updatetime) {
		this.id = id;
		this.type=type;
		this.blank=blank;
		this.boundary=boundary;
		this.jh=jh;
		this.sign=sign;
		this.letter=letter;
		this.number=number;
		this.htmlencode=htmlencode;
		this.urlencode=urlencode;
		this.updater=updater;
		this.mix=mix;
		this.updatetime=updatetime;
		this.name=name;
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 36)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "TYPE", length = 50)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "BOUNDARY", length = 50)
	public String getBoundary() {
		return boundary;
	}

	public void setBoundary(String boundary) {
		this.boundary = boundary;
	}

	@Column(name = "BLANK", length = 50)
	public String getBlank() {
		return blank;
	}

	public void setBlank(String blank) {
		this.blank = blank;
	}

	@Column(name = "JH", length = 50)
	public String getJh() {
		return jh;
	}

	public void setJh(String jh) {
		this.jh = jh;
	}

	@Column(name = "SIGN", length = 50)
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Column(name = "LETTER", length = 50)
	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	@Column(name = "NUMBER", length = 50)
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Column(name = "HTMLENCODE", length = 50)
	public String getHtmlencode() {
		return htmlencode;
	}

	public void setHtmlencode(String htmlencode) {
		this.htmlencode = htmlencode;
	}

	@Column(name = "URLENCODE", length = 50)
	public String getUrlencode() {
		return urlencode;
	}

	public void setUrlencode(String urlencode) {
		this.urlencode = urlencode;
	}

	@Column(name = "UPDATER", length = 50)
	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	@Column(name = "MIX", length = 50)
	public String getMix() {
		return mix;
	}

	public void setMix(String mix) {
		this.mix = mix;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME", length = 19)
	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	

	
}
