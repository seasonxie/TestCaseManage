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
@Table(name = "texcase")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Texcase implements java.io.Serializable {

	private String id;
	private Texcase texcase;
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
	
	private String orgid;

	private String status;
	
	public Texcase getTexcase() {
		return texcase;
	}

	public void setTexcase(Texcase texcase) {
		this.texcase = texcase;
	}

	

	private String statuso;
	private String version;
	private String casenum;
	private String casestep;
	private String fristpage;
	private String secpage;
	private String thrpage;
	private String fourpage;
	private String fivepage;
	
	private String treeid;

	@Column(name = "TREEID",length = 100)
	public String getTreeid() {
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}
	
	
	
	@Column(name = "STATUS",length = 100)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private String tester;
	private String iscase;
	private Date exdate;
	private String pid;

	@Column(name = "PID",length = 100)
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Texcase() {
	}

	public Texcase(String id,  String name,  String project) {
		this.id = id;
		this.name = name;
		this.project=project;
	}

	public Texcase(String treeid,String pid,String fristpage,String secpage,String thrpage,String fourpage,String fivepage,String statuso,String casenum,String casestep,String version,String orgid,String tester,String iscase,Date exdate,String id, Texcase texcase, String module,String modulechild, String project,String name, String op,String pre,String step,String expire,String remark, Integer seq, String icon) {
		this.orgid=orgid;this.pid=pid;
		this.tester=tester;
		this.iscase=iscase;
		this.exdate=exdate;	
		this.id = id;
		this.texcase = texcase;
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
		this.statuso=statuso;
		this.version=version;
		this.casenum=casenum;
		this.casestep=casestep;
		this.fivepage=fristpage;
		this.secpage=secpage;
		this.thrpage=thrpage;
		this.fourpage=fourpage;
		this.fivepage=fivepage;
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



/*	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PID")
	public Texcase getTexcase() {
		return this.texcase;
	}

	public void setTexcase(Texcase texcase) {
		this.texcase = texcase;
	}*/

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
	
	@Column(name = "ORGID", length = 100)
	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	@Column(name = "TESTER", length = 100)
	public String getTester() {
		return tester;
	}

	public void setTester(String tester) {
		this.tester = tester;
	}
	@Column(name = "ISCASE", length = 100)
	public String getIscase() {
		return iscase;
	}

	
	public void setIscase(String iscase) {
		this.iscase = iscase;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXDATE", length = 19)
	public Date getExdate() {
		return exdate;
	}

	public void setExdate(Date exdate) {
		this.exdate = exdate;
	}
	
	@Column(name = "STATUSO", length = 100)
	public String getStatuso() {
		return statuso;
	}

	public void setStatuso(String statuso) {
		this.statuso = statuso;
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
	
	@Column(name = "CASESTEP", length = 100)
	public String getCasestep() {
		return casestep;
	}

	public void setCasestep(String casestep) {
		this.casestep = casestep;
	}

	@Column(name = "FRISTPAGE", length = 200)
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
