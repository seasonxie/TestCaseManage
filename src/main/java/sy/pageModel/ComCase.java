package sy.pageModel;

import java.util.Date;

public class ComCase implements java.io.Serializable {

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
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
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getPre() {
		return pre;
	}
	public void setPre(String pre) {
		this.pre = pre;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public String getExpire() {
		return expire;
	}
	public void setExpire(String expire) {
		this.expire = expire;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	private String id;
	private String pid;
	private String pname;
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	private String name;
	private String remark;
	private Integer seq;
	private String op;
	private String pre;
	private String step;
	private String expire;
	private String iconCls;
	

	private String appstatusc;
	private String apperc;
	private Date apptimec;

	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	private String project;


	public String getAppstatusc() {
		return appstatusc;
	}
	public void setAppstatusc(String appstatusc) {
		this.appstatusc = appstatusc;
	}
	public String getApperc() {
		return apperc;
	}
	public void setApperc(String apperc) {
		this.apperc = apperc;
	}
	public Date getApptimec() {
		return apptimec;
	}
	public void setApptimec(Date apptimec) {
		this.apptimec = apptimec;
	}


}
