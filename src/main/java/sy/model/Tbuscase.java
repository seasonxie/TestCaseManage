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
@Table(name = "tbuscase")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Tbuscase implements java.io.Serializable {

	private String id;
/*	private Tbuscase tbuscase;*/
	private String name;
	private String project;
	
	private String op;
	private String pre;
	private String step;
	private String expire;
	private String remark;
	private double seq;
	private String icon;
	
	private String module;

	private String modulechild;
	

	private String appstatusb;
	private String apperb;
	private Date apptimeb;
	
	
	private String updater;

	private Date updatetime;
	private String version;
	private String casenum;
	private String casestep;
	private String fristpage;
	private String secpage;
	private String thrpage;
	private String fourpage;
	private String fivepage;
	private String pid;
	
	private String treeid;

	@Column(name = "TREEID",length = 100)
	public String getTreeid() {
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}

	@Column(name = "PID",length = 100)
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Tbuscase() {
	}

	public Tbuscase(String id,  String project) {
		this.id = id;
		this.project=project;
	}

	public Tbuscase(String treeid,String id,String pid,String fristpage,String secpage,String thrpage,String fourpage,String fivepage,String updater,Date updatetime,String casenum,String casestep,String version,String appstatusb,String apperb,Date apptimeb, String module,String modulechild, String project,String name, String op,String pre,String step,String expire,String remark, Integer seq, String icon) {
		this.id = id;
/*		this.tbuscase = tbuscase;*/
		this.name = name;
		this.remark = remark;
		this.seq = seq;
		this.icon = icon;
		this.op=op;
		this.expire=expire;
		this.pre=pre;
		this.step=step;
		this.project=project;
		this.module=module;
		this.modulechild=modulechild;
		this.apperb=apperb;
		this.appstatusb=appstatusb;
		this.apptimeb=apptimeb;
		this.updater=updater;
		this.updatetime=updatetime;
		this.version=version;
		this.casenum=casenum;
		this.casestep=casestep;
		this.fivepage=fristpage;
		this.secpage=secpage;
		this.thrpage=thrpage;
		this.fourpage=fourpage;
		this.fivepage=fivepage;
this.pid=pid;
this.treeid=treeid;
	}

	@Id
	@Column(name = "ID", nullable = false, length = 36)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


/*
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PID")
	public Tbuscase getTbuscase() {
		return this.tbuscase;
	}

	public void setTbuscase(Tbuscase tbuscase) {
		this.tbuscase = tbuscase;
	}*/

	@Column(name = "NAME", length = 100)
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
	public double getSeq() {
		return this.seq;
	}

	public void setSeq(double seq) {
		this.seq = seq;
	}

	

	@Column(name = "ICON", length = 100)
	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	@Column(name = "MODULE", length = 100)
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	@Column(name = "MODULECHILD", length = 100)
	public String getModulechild() {
		return modulechild;
	}

	public void setModulechild(String modulechild) {
		this.modulechild = modulechild;
	}
	
	@Column(name = "APPSTATUSB", length = 200)
	public String getAppstatusb() {
		return appstatusb;
	}

	public void setAppstatusb(String appstatusb) {
		this.appstatusb = appstatusb;
	}

	@Column(name = "APPERB", length = 200)
	public String getApperb() {
		return apperb;
	}

	public void setApperb(String apperb) {
		this.apperb = apperb;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPTIMEB", length = 19)
	public Date getApptimeb() {
		return apptimeb;
	}

	public void setApptimeb(Date apptimeb) {
		this.apptimeb = apptimeb;
	}

	@Column(name = "UPDATER", length = 100)
	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME", length = 19)
	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	@Column(name = "VERSION", length = 100)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name = "CASENUM", length = 200)
	public String getCasenum() {
		return casenum;
	}

	public void setCasenum(String casenum) {
		this.casenum = casenum;
	}
	@Column(name = "CASESTEP", length = 50)
	public String getCasestep() {
		return casestep;
	}

	public void setCasestep(String casestep) {
		this.casestep = casestep;
	}

	@Column(name = "FIRISTPAGE", length = 200)
	public String getFristpage() {
		return fristpage;
	}

	public void setFristpage(String fristpage) {
		this.fristpage = fristpage;
	}

	@Column(name = "SECPAGE", length = 200)
	public String getSecpage() {
		return secpage;
	}

	public void setSecpage(String secpage) {
		this.secpage = secpage;
	}

	@Column(name = "THRPAGE", length = 200)
	public String getThrpage() {
		return thrpage;
	}

	public void setThrpage(String thrpage) {
		this.thrpage = thrpage;
	}

	@Column(name = "FOURPAGE", length = 200)
	public String getFourpage() {
		return fourpage;
	}

	public void setFourpage(String fourpage) {
		this.fourpage = fourpage;
	}

	@Column(name = "FIVEPAGE", length = 200)
	public String getFivepage() {
		return fivepage;
	}

	public void setFivepage(String fivepage) {
		this.fivepage = fivepage;
	}





}
