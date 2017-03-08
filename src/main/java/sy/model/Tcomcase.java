package sy.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @author Administrator
 *
 */
@Entity
@Table(name = "tcomcase")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Tcomcase implements java.io.Serializable {

	private String id;
	private Tcomcase tcomcase;
	private String name;
	private String project;
	
	private String op;
	private String pre;
	private String step;
	private String expire;
	private String remark;
	private Integer seq;
	private String icon;
	
	

	private String appstatusc;
	private String apperc;
	private Date apptimec;


	public Tcomcase() {
	}

	public Tcomcase(String id,  String name,  String project) {
		this.id = id;
		this.name = name;
		this.project=project;
	}

	public Tcomcase(String id,String appstatusc,String apperc,Date apptimec, Tcomcase tcomcase,  String project,String name, String op,String pre,String step,String expire,String remark, Integer seq, String icon) {
		this.id = id;
		this.tcomcase = tcomcase;
		this.name = name;
		this.remark = remark;
		this.seq = seq;
		this.icon = icon;
		this.op=op;
		this.expire=expire;
		this.pre=pre;
		this.step=step;
		this.project=project;
		this.apperc=apperc;
		this.appstatusc=appstatusc;
		this.apptimec=apptimec;

	}

	@Id
	@Column(name = "ID", nullable = false, length = 36)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}



	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PID")
	public Tcomcase getTcomcase() {
		return this.tcomcase;
	}

	public void setTcomcase(Tcomcase tcomcase) {
		this.tcomcase = tcomcase;
	}

	@Column(name = "NAME", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "OP", length = 500)
	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	@Column(name = "PRE", length = 500)
	public String getPre() {
		return pre;
	}

	public void setPre(String pre) {
		this.pre = pre;
	}

	@Column(name = "STEP", length = 500)
	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}
	@Column(name = "PROJECT", length = 50)
	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}
    
	@Column(name = "EXPIRE", length = 500)
	public String getExpire() {
		return expire;
	}

	public void setExpire(String expire) {
		this.expire = expire;
	}

	
	@Column(name = "REMARK", length = 300)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "SEQ")
	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	

	@Column(name = "ICON", length = 100)
	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	@Column(name = "APPSTATUSC", length = 200)
	public String getAppstatusc() {
		return appstatusc;
	}

	public void setAppstatusc(String appstatusc) {
		this.appstatusc = appstatusc;
	}

	@Column(name = "APPERC", length = 200)
	public String getApperc() {
		return apperc;
	}

	public void setApperc(String apperc) {
		this.apperc = apperc;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPTIMEC", length = 19)
	public Date getApptimec() {
		return apptimec;
	}

	public void setApptimec(Date apptimec) {
		this.apptimec = apptimec;
	}



}
