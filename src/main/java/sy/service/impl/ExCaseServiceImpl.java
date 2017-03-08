package sy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.CaseDaoI;
import sy.dao.ExCaseDaoI;	
import sy.dao.ResourceTypeDaoI;
import sy.dao.RoleDaoI;
import sy.dao.UserDaoI;
import sy.model.Texcase;
import sy.model.Tcomcase;
import sy.model.Texcase;
import sy.model.Trole;
import sy.model.Tuser;
import sy.pageModel.BusCase;
import sy.pageModel.ComCase;
import sy.pageModel.DataGrid;
import sy.pageModel.ExCase;
import sy.pageModel.PageHelper;
import sy.pageModel.Resource;
import sy.pageModel.SessionInfo;
import sy.pageModel.Tree;
import sy.pageModel.User;
import sy.service.CaseServiceI;
import sy.service.ExCaseServiceI;
import sy.service.UserServiceI;
import sy.util.MD5Util;

@Service
public class ExCaseServiceImpl implements ExCaseServiceI {

	@Autowired
	private ExCaseDaoI excaseDao;


	@Autowired
	private UserDaoI userDao;

	@Override
	public List<Tree> tree(SessionInfo sessionInfo) {
		List<Texcase> l = null;
		List<Tree> lt = new ArrayList<Tree>();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resourceTypeId", "0");// 菜单类型的资源

		if (sessionInfo != null) {
			params.put("userId", sessionInfo.getId());// 自查自己有权限的资源
			l = excaseDao.find("select distinct t from Texcase t join fetch t.Texcasetype type join fetch t.troles role join role.tusers user where type.id = :resourceTypeId and user.id = :userId order by t.seq", params);
		} else {
			l = excaseDao.find("select distinct t from Texcase t join fetch t.Texcasetype type where type.id = :resourceTypeId order by t.seq", params);
		}

		if (l != null && l.size() > 0) {
			for (Texcase r : l) {
				Tree tree = new Tree();
				BeanUtils.copyProperties(r, tree);
				/*if (r.getTexcase() != null) {
					tree.setPid(r.getTexcase().getId());
				}*/
				tree.setText(r.getName());
				tree.setIconCls(r.getIcon());
				Map<String, Object> attr = new HashMap<String, Object>();
				//attr.put("url", r.getUrl());
				tree.setAttributes(attr);
				lt.add(tree);
			}
		}
		return lt;
	}

	@Override
	public List<Tree> allTree(SessionInfo sessionInfo,String project) {
		List<Texcase> l = null;
		List<Tree> lt = new ArrayList<Tree>();

		if (sessionInfo != null) {
			
			l = excaseDao.find("select distinct t from Texcase t  where t.project='"+project+"' order by t.seq");
		} else {
			//l = excaseDao.find("select distinct t from Texcase t join fetch t.Texcasetype type order by t.seq", params);
		}

		if (l != null && l.size() > 0) {
			for (Texcase r : l) {
				Tree tree = new Tree();
				BeanUtils.copyProperties(r, tree);
			/*	if (r.getTexcase() != null) {
					tree.setPid(r.getTexcase().getId());
				}*/
				if(r.getModulechild()==null){
					tree.setText(r.getModule());
				}else{
					if(r.getModulechild().isEmpty()){
						tree.setText(r.getModule());
					}else{
						tree.setText(r.getModule()+"--"+r.getModulechild());
					}
					
				}
				
				tree.setIconCls(r.getIcon());
				Map<String, Object> attr = new HashMap<String, Object>();
				//attr.put("url", r.getUrl());
				tree.setAttributes(attr);
				lt.add(tree);
			}
		}
		return lt;
	}
	
	@Override
	public Long getTotal(String project, ExCase bug, PageHelper ph) {
		Map<String, Object> params = new HashMap<String, Object>();
		return excaseDao.count("select count(*) from Texcase t "+whereHql(bug,params,project,ph.getTreeid())+"", params);
	}

	@Override
	public List<ExCase> treeGrid(SessionInfo sessionInfo,String data, PageHelper ph,ExCase c) {
		List<Texcase> l = null;
		List<ExCase> lr = new ArrayList<ExCase>();

		Map<String, Object> params = new HashMap<String, Object>();
		
		if (!data.isEmpty()) {	
			 String where=whereHql(c,params,data,ph.getTreeid());
			 // t.project='"+data+"' or t.treeid='"+ph.getTreeid()+"' 
			l = excaseDao.find("select distinct t from Texcase t "+where+" order by t.project,t.seq",params, ph.getPage(), ph.getRows());
		}

		if (l != null && l.size() > 0) {
			for (Texcase t : l) {
				ExCase r = new ExCase();
				BeanUtils.copyProperties(t, r);
			/*	if (t.getTexcase() != null) {
					r.setPid(t.getTexcase().getId());
					//r.setPname(t.getTcomcase().getName());
				}*/
				r.setId(r.getOrgid());
				r.setPid(t.getPid());
				//r.setTypeId(t.getTcomcasetype().getId());
			//	r.setTypeName(t.getTcomcasetype().getName());
				if (t.getIcon() != null && !t.getIcon().equalsIgnoreCase("")) {
					r.setIconCls(t.getIcon());
				}
				lr.add(r);
			}
		}

		return lr;
	}
	
	private String whereHql(ExCase bug, Map<String, Object> params,String data,String treeid) {
		String whereHql = "where 1=1 and (t.project='"+data+"' or t.treeid='"+treeid+"') ";
	
		if (bug != null) {
			if (bug.getName() != null&&!bug.getName().isEmpty()) {
				whereHql += " and t.name like :name";
				params.put("name", "%%" + bug.getName() + "%%");
			}
			if (bug.getStep() != null&&!bug.getStep().isEmpty()) {
				whereHql += " and t.step like :step";
				params.put("step","%%" + bug.getStep() + "%%");
			}
			if (bug.getOp() != null&&!bug.getOp().isEmpty()) {
				whereHql += " and t.op like :op";
				params.put("op","%%" + bug.getOp() + "%%");
			}
			if (bug.getTester() != null&&!bug.getTester().isEmpty()) {
				whereHql += " and t.tester like :tester";
				params.put("tester","%%" + bug.getTester() + "%%");
			}
			if (bug.getStatus()!= null&&!bug.getStatus().isEmpty()) {
				if(bug.getStatus().equals("未执行")){
					whereHql += " and t.status is null";
				
				}else{
					whereHql += " and t.status like :status";
					params.put("status","%%" + bug.getStatus() + "%%");	
				}
			
			}
		}
		System.out.println(whereHql);
		return whereHql;
	}
	
	List<ExCase> lr = new ArrayList<ExCase>();
	@Override
	public List<ExCase> treeGridc(String data, PageHelper ph) {
		lr.clear();
		System.out.println(data);
		List<Texcase> l = null;	
	
		if (!data.isEmpty()) {	
		
			l = excaseDao.find("select distinct t from Texcase t where (t.project='"+data+"' or t.treeid='"+ph.getTreeid()+"') and (t.pid is null or t.pid='') order by t.seq");
	
		}
		
		if (l != null && l.size() > 0) {
			for (Texcase t : l) {
			
				ExCase r = new ExCase();
				BeanUtils.copyProperties(t, r);	
				lr.add(r);
			
				List<Texcase> ll =excaseDao.find("select distinct t from Texcase t where (t.project='"+data+"' or t.treeid='"+ph.getTreeid()+"') and t.pid='"+t.getOrgid()+"' order by t.seq");
			
				if(ll.size()>0){
					dogetchild(ll,data,ph);
				}
			}
		}

		return lr;
	}
	
	public void dogetchild(List<Texcase> ll,String data, PageHelper ph) {
		for (Texcase t : ll) {
			ExCase r = new ExCase();
			BeanUtils.copyProperties(t, r);	
			lr.add(r);
			List<Texcase> lll =excaseDao.find("select distinct t from Texcase t where (t.project='"+data+"' or t.treeid='"+ph.getTreeid()+"') and t.pid='"+t.getOrgid()+"' order by t.seq");
			if(lll.size()>0){
				dogetchild(lll,data,ph);
			}
		}
		
	}


	@Override
	public void add(ExCase ExCase, SessionInfo sessionInfo) {
		Texcase t = new Texcase();
		BeanUtils.copyProperties(ExCase, t);
		//System.out.println(ExCase.getPid());
		if (ExCase.getPid() != null && !ExCase.getPid().equalsIgnoreCase("")) {
			/*t.setTexcase(excaseDao.get(Texcase.class, ExCase.getPid()));*/
			t.setPid(ExCase.getPid() );
			
		}
		if(ExCase.getExpire()!=null)
		t.setExpire(ExCase.getExpire().replace("&lt;br&gt;", "<br>").replace("script", "sc**pt").replace("\n", "<br>").replace("&amp;quot;", "\""));
		if(ExCase.getStep()!=null)
		t.setStep(ExCase.getStep().replace("&lt;br&gt;", "<br>").replace("script", "sc**pt").replace("\n", "<br>").replace("&amp;quot;", "\""));
		if(ExCase.getPre()!=null)
		t.setPre(ExCase.getPre().replace("&lt;br&gt;", "<br>").replace("script", "sc**pt").replace("\n", "<br>").replace("&amp;quot;", "\""));
		/*if (ComCase.getTypeId() != null && !ComCase.getTypeId().equalsIgnoreCase("")) {
			t.setTcomcasetype(resourceTypeDao.getById(ComCase.getTypeId()));
		}*/
		if (ExCase.getIconCls() != null && !ExCase.getIconCls().equalsIgnoreCase("")) {
			t.setIcon(ExCase.getIconCls());
		}
		excaseDao.save(t);

	/*	// 由于当前用户所属的角色，没有访问新添加的资源权限，所以在新添加资源的时候，将当前资源授权给当前用户的所有角色，以便添加资源后在资源列表中能够找到
		Tuser user = userDao.get(Tuser.class, sessionInfo.getId());
		Set<Trole> roles = user.getTroles();
		for (Trole r : roles) {
			r.getTcomcases().add(t);
		}*/
	}

	@Override
	public void delete(String id,String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		System.out.println(name);
		System.out.println(id);
		params.put("id",id);
		Texcase t = excaseDao.get("select distinct t from Texcase t where t.project='"+name+"' and t.orgid = :id", params);
		System.out.println(t.getId());
		del(t);
	}

	private void del(Texcase t) {
		/*if (t.getTcomcase() != null && t.getTcomcase().size() > 0) {
			for (Tcomcase r : t.getTcomcase()) {
				del(r);
			}
		}*/
		excaseDao.delete(t);
	}

	@Override
	public void edit(ExCase ExCase) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", ExCase.getId());
		Texcase t = excaseDao.get("select distinct t from Texcase t where t.id = :id", params);
		if (t != null) {
			BeanUtils.copyProperties(ExCase, t);
			t.setExdate(ExCase.getExdate());
			t.setExpire(ExCase.getExpire().replace("&lt;br&gt;", "<br>").replace("script", "sc**pt").replace("\n", "<br>").replace("&amp;quot;", "\""));
			t.setStep(ExCase.getStep().replace("&lt;br&gt;", "<br>").replace("script", "sc**pt").replace("\n", "<br>").replace("&amp;quot;", "\""));
			t.setPre(ExCase.getPre().replace("&lt;br&gt;", "<br>").replace("script", "sc**pt").replace("\n", "<br>").replace("&amp;quot;", "\""));
			/*if (ComCase.getTypeId() != null && !ComCase.getTypeId().equalsIgnoreCase("")) {
				t.setTcomcasetype(resourceTypeDao.getById(resource.getTypeId()));// 赋值资源类型
			}*/
			if (ExCase.getIconCls() != null && !ExCase.getIconCls().equalsIgnoreCase("")) {
				t.setIcon(ExCase.getIconCls());
			}
			if (ExCase.getPid() != null && !ExCase.getPid().equalsIgnoreCase("")) {// 说明前台选中了上级资源
				Texcase pt = excaseDao.get(Texcase.class, ExCase.getPid());
				//isChildren(t, pt);// 说明要将当前资源修改到当前资源的子/孙子资源下
				//t.setTexcase(pt);
				t.setPid(ExCase.getPid());
			} else {
				//t.setTexcase(null);// 前台没有选中上级资源，所以就置空
			}
		}
	}
	
	@Override//redmine
	public void edit(String id,String remark,String cproject) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Texcase t = excaseDao.get("select distinct t from Texcase t where t.project='"+cproject+"' and t.orgid = :id", params);
		//System.out.println(t);
		if (t != null) {
			t.setRemark(remark);
			excaseDao.update(t);
		}
	}

	@Override//status
	public void edit(String id, String status, SessionInfo sessionInfo,String namee) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id",id);
		Texcase pt = excaseDao.get("select distinct t from Texcase t where t.project='"+namee+"' and t.orgid = :id", params);
		if (pt != null) {
		if(pt.getIscase().equals("1")){
		pt.setTester(sessionInfo.getName());
		//System.out.println(pt);
		pt.setStatus(status);
		pt.setExdate(new Date());
		excaseDao.update(pt);
		}
		}
	}

	/**
	 * 判断是否是将当前节点修改到当前节点的子节点
	 * 
	 * @param t
	 *            当前节点
	 * @param pt
	 *            要修改到的节点
	 * @return
	 */
	/*private boolean isChildren(Texcase t, Texcase pt) {
		if (pt != null && pt.getTexcase() != null) {
			if (pt.getTexcase().getId().equalsIgnoreCase(t.getId())) {
				pt.setTexcase(null);
				return true;
			} else {
				return isChildren(t, pt.getTexcase());
			}
		}
		return false;
	}*/

	@Override
	public ExCase get(String id,String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Texcase t = excaseDao.get("from Texcase t where t.project='"+name+"' and t.orgid = :id", params);
		ExCase r = new ExCase();
		BeanUtils.copyProperties(t, r);
		r.setPid(t.getPid());
	/*	if (t.getTexcase() != null) {
			r.setPid(t.getTexcase().getId());
			r.setPname(t.getTexcase().getName());
		}
		r.setTypeId(t.getTcomcasetype().getId());
		r.setTypeName(t.getTcomcasetype().getName());*/
		if (t.getIcon() != null && !t.getIcon().equalsIgnoreCase("")) {
			r.setIconCls(t.getIcon());
		}
		return r;
	}

	@Override
	public List<Map<String, String>> project() {
		// TODO Auto-generated method stub
		List<Map<String,String>> aa=new ArrayList<Map<String,String>>();
		
		List<String> n=excaseDao.findString("select r.name from Tresource r where url='/excaseController/manager' and tresourcetype_id=2");
		
		 System.out.println(n.size());
	   for(String a:n){		
		   Map<String, String> re=new HashMap<String,String>();
		  re.put("project", a);	  
		  aa.add(re);
	   }
	   
        return aa;
	}
	
	
	public List<Map<String, String>> projectchild(String project) {
		// TODO Auto-generated method stub
		List<Map<String,String>> aa=new ArrayList<Map<String,String>>();		
		List<String> n=excaseDao.findString("select name from Texcase where pid is null and project='"+project+"'");
		List<String> id=excaseDao.findString("select id from Texcase where pid is null and project='"+project+"'");
		
		for(int i=0;i<n.size();i++){
			  Map<String, String> re=new HashMap<String,String>();
			  re.put("id", id.get(i));	  
			  re.put("name", n.get(i));	 
			  aa.add(re);
		}
  
        return aa;
	}

	
	
	@Override
	public List<ExCase> collecttreeGrid(SessionInfo sessionInfo,String data) {
		List<Texcase> l = null;
		List<ExCase> lr = new ArrayList<ExCase>();

		
		if (!data.isEmpty()) {	
	
			l = excaseDao.find("select distinct t from Texcase t where PID='"+data+"' order by t.project,t.seq");
		}

		if (l != null && l.size() > 0) {
			for (Texcase t : l) {
				ExCase r = new ExCase();
				BeanUtils.copyProperties(t, r);
				r.setPid(t.getPid());
				/*if (t.getTexcase() != null) {
					r.setPid(t.getTexcase().getId());
					//r.setPname(t.getTcomcase().getName());
				}
				//r.setTypeId(t.getTcomcasetype().getId());
*/			//	r.setTypeName(t.getTcomcasetype().getName());
				if (t.getIcon() != null && !t.getIcon().equalsIgnoreCase("")) {
					r.setIconCls(t.getIcon());
				}
				lr.add(r);
			}
		}

		return lr;
	}

	@Override
	public List<String> isExisted() {
		List<String> n=excaseDao.findString("select id from Texcase");
		return n;
	}


	

}
