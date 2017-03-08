package sy.pageModel;

import java.util.Date;

public class ExCase implements java.io.Serializable {
	
	private String treeid;

	public String getTreeid() {
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}

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
	public double getSeq() {
		return seq;
	}
	public void setSeq(double seq) {
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
	private String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private String name;
	private String remark;
	private double seq;
	private String op;
	private String pre;
	private String step;
	private String expire;
	private String iconCls;
	
	private String statuso;
	
	private String version;
	private String casenum;
	private String casestep;
	private String fristpage;
	private String secpage;
	private String thrpage;
	private String fourpage;
	private String fivepage;
	
	public String getOrgid() {
		return orgid;
	}
	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}
	public String getTester() {
		return tester;
	}
	public void setTester(String tester) {
		this.tester = tester;
	}
	public String getIscase() {
		return iscase;
	}
	public void setIscase(String iscase) {
		this.iscase = iscase;
	}
	public Date getExdate() {
		return exdate;
	}
	public void setExdate(Date exdate) {
		this.exdate = exdate;
	}
	private String orgid;


	private String tester;
	private String iscase;
	private Date exdate;
	
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getModulechild() {
		return modulechild;
	}
	public void setModulechild(String modulechild) {
		this.modulechild = modulechild;
	}
	private String module;

	private String modulechild;
	
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	private String project;

	
	public String getStatuso() {
		return statuso;
	}
	public void setStatuso(String statuso) {
		this.statuso = statuso;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCasenum() {
		return casenum;
	}
	public void setCasenum(String casenum) {
		this.casenum = casenum;
	}
	public String getCasestep() {
		return casestep;
	}
	public void setCasestep(String casestep) {
		this.casestep = casestep;
	}
	public String getFristpage() {
		return fristpage;
	}
	public void setFristpage(String fristpage) {
		this.fristpage = fristpage;
	}
	public String getSecpage() {
		return secpage;
	}
	public void setSecpage(String secpage) {
		this.secpage = secpage;
	}
	public String getThrpage() {
		return thrpage;
	}
	public void setThrpage(String thrpage) {
		this.thrpage = thrpage;
	}
	public String getFourpage() {
		return fourpage;
	}
	public void setFourpage(String fourpage) {
		this.fourpage = fourpage;
	}
	public String getFivepage() {
		return fivepage;
	}
	public void setFivepage(String fivepage) {
		this.fivepage = fivepage;
	}



}
