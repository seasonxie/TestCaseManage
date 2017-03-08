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
import sy.dao.ResourceTypeDaoI;
import sy.dao.RoleDaoI;
import sy.dao.UserDaoI;
import sy.model.Tcomcase;
import sy.model.Trole;
import sy.model.Tuser;
import sy.pageModel.ComCase;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.pageModel.Resource;
import sy.pageModel.SessionInfo;
import sy.pageModel.Tree;
import sy.pageModel.User;
import sy.service.CaseServiceI;
import sy.service.UserServiceI;
import sy.util.MD5Util;

@Service
public class CaseServiceImpl implements CaseServiceI {

	@Autowired
	private CaseDaoI caseDao;


	@Autowired
	private UserDaoI userDao;

	@Override
	public List<Tree> tree(SessionInfo sessionInfo) {
		List<Tcomcase> l = null;
		List<Tree> lt = new ArrayList<Tree>();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resourceTypeId", "0");// 菜单类型的资源

		if (sessionInfo != null) {
			params.put("userId", sessionInfo.getId());// 自查自己有权限的资源
			l = caseDao.find("select distinct t from Tcomcase t join fetch t.Tcomcasetype type join fetch t.troles role join role.tusers user where type.id = :resourceTypeId and user.id = :userId order by t.seq", params);
		} else {
			l = caseDao.find("select distinct t from Tcomcase t join fetch t.Tcomcasetype type where type.id = :resourceTypeId order by t.seq", params);
		}

		if (l != null && l.size() > 0) {
			for (Tcomcase r : l) {
				Tree tree = new Tree();
				BeanUtils.copyProperties(r, tree);
				if (r.getTcomcase() != null) {
					tree.setPid(r.getTcomcase().getId());
				}
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
	public List<Tree> allTree(SessionInfo sessionInfo) {
		List<Tcomcase> l = null;
		List<Tree> lt = new ArrayList<Tree>();

		Map<String, Object> params = new HashMap<String, Object>();
		if (sessionInfo != null) {
			params.put("userId", sessionInfo.getId());// 查自己有权限的资源
			l = caseDao.find("select distinct t from Tcomcase t join fetch t.Tcomcasetype type join fetch t.troles role join role.tusers user where user.id = :userId order by t.seq", params);
		} else {
			l = caseDao.find("select distinct t from Tcomcase t join fetch t.Tcomcasetype type order by t.seq", params);
		}

		if (l != null && l.size() > 0) {
			for (Tcomcase r : l) {
				Tree tree = new Tree();
				BeanUtils.copyProperties(r, tree);
				if (r.getTcomcase() != null) {
					tree.setPid(r.getTcomcase().getId());
				}
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
	public List<ComCase> treeGrid(SessionInfo sessionInfo,String data) {
		List<Tcomcase> l = null;
		List<ComCase> lr = new ArrayList<ComCase>();

		if (sessionInfo != null&&data.isEmpty()) {	
		
			l = caseDao.find("select distinct t from Tcomcase t order by t.seq");
		} else {
			//l = caseDao.find("select distinct t from Tcomcase t join fetch t.Tcomcasetype type order by t.seq", params);
		}
		

		
		if (!data.isEmpty()) {	
	
			l = caseDao.find("select distinct t from Tcomcase t where t.project='"+data+"' order by t.project,t.seq");
		}

		if (l != null && l.size() > 0) {
			for (Tcomcase t : l) {
				ComCase r = new ComCase();
				BeanUtils.copyProperties(t, r);
				if (t.getTcomcase() != null) {
					r.setPid(t.getTcomcase().getId());
					//r.setPname(t.getTcomcase().getName());
				}
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
	/*
	@Override
	public List<ComCase> ctreeGrid(SessionInfo sessionInfo,String data) {
		List<Tcomcase> l = null;
		List<ComCase> lr = new ArrayList<ComCase>();
		
		if (sessionInfo != null&&data.isEmpty()) {	
		
			l = caseDao.find("select distinct t from Tcomcase t order by t.project,t.seq");
		} else {
					//l = caseDao.find("select distinct t from Tcomcase t join fetch t.Tcomcasetype type order by t.seq", params);
		}
	
		List<String> ppid=getName();
		
	//System.out.println("ppid"+ppid.size());
		
		List<Tcomcase> remove = new ArrayList<Tcomcase>();
		if (l != null && l.size() > 0) {
			int pidd=1;
			for(int i=0;i<ppid.size();i++){
				if(i!=0){
					pidd++;
				}
				ComCase pp = new ComCase();
				pp.setId(String.valueOf(pidd));
				pp.setProject(getName(ppid.get(i)));
				lr.add(pp);
				List<String> child=getChildProject(ppid.get(i));
				System.out.println(child.contains("排序控件"));
				System.out.println("child"+child.size());
				for (Tcomcase t : l) {
				
					if(child.contains(t.getProject())){
					
					ComCase r = new ComCase();
					BeanUtils.copyProperties(t, r);
				
					if (t.getTcomcase() != null) {
						r.setPid(t.getTcomcase().getId());
					}else{
						r.setPid(String.valueOf(pidd));
					}

					if (t.getIcon() != null && !t.getIcon().equalsIgnoreCase("")) {
						r.setIconCls(t.getIcon());
					}
					lr.add(r);
					remove.add(t);
					}
				}
				l.removeAll(remove);
				
			}
	
		}

		return lr;
	}
*/
	@Override
	public void add(ComCase ComCase, SessionInfo sessionInfo) {
		Tcomcase t = new Tcomcase();
		BeanUtils.copyProperties(ComCase, t);
		System.out.println(ComCase.getPid());
		if (ComCase.getPid() != null && !ComCase.getPid().equalsIgnoreCase("")) {
			t.setTcomcase(caseDao.get(Tcomcase.class, ComCase.getPid()));
			
		}
		t.setExpire(ComCase.getExpire().replace("\n", "<br>"));
		t.setStep(ComCase.getStep().replace("\n", "<br>"));
		/*if (ComCase.getTypeId() != null && !ComCase.getTypeId().equalsIgnoreCase("")) {
			t.setTcomcasetype(resourceTypeDao.getById(ComCase.getTypeId()));
		}*/
		if (ComCase.getIconCls() != null && !ComCase.getIconCls().equalsIgnoreCase("")) {
			t.setIcon(ComCase.getIconCls());
		}
		caseDao.save(t);

	/*	// 由于当前用户所属的角色，没有访问新添加的资源权限，所以在新添加资源的时候，将当前资源授权给当前用户的所有角色，以便添加资源后在资源列表中能够找到
		Tuser user = userDao.get(Tuser.class, sessionInfo.getId());
		Set<Trole> roles = user.getTroles();
		for (Trole r : roles) {
			r.getTcomcases().add(t);
		}*/
	}

	@Override
	public void delete(String id) {
		Tcomcase t = caseDao.get(Tcomcase.class, id);
		del(t);
	}

	private void del(Tcomcase t) {
		/*if (t.getTcomcase() != null && t.getTcomcase().size() > 0) {
			for (Tcomcase r : t.getTcomcase()) {
				del(r);
			}
		}*/
		caseDao.delete(t);
	}

	@Override
	public void edit(ComCase ComCase) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", ComCase.getId());
		Tcomcase t = caseDao.get("select distinct t from Tcomcase t where t.id = :id", params);
		if (t != null) {
			BeanUtils.copyProperties(ComCase, t);
			/*if (ComCase.getTypeId() != null && !ComCase.getTypeId().equalsIgnoreCase("")) {
				t.setTcomcasetype(resourceTypeDao.getById(resource.getTypeId()));// 赋值资源类型
			}*/
			if (ComCase.getIconCls() != null && !ComCase.getIconCls().equalsIgnoreCase("")) {
				t.setIcon(ComCase.getIconCls());
			}
			t.setExpire(ComCase.getExpire().replace("&lt;br&gt;", "<br>"));
			t.setStep(ComCase.getStep().replace("&lt;br&gt;", "<br>"));
			
			if (ComCase.getPid() != null && !ComCase.getPid().equalsIgnoreCase("")) {// 说明前台选中了上级资源
				Tcomcase pt = caseDao.get(Tcomcase.class, ComCase.getPid());
				isChildren(t, pt);// 说明要将当前资源修改到当前资源的子/孙子资源下
				t.setTcomcase(pt);
			} else {
				t.setTcomcase(null);// 前台没有选中上级资源，所以就置空
			}
		}
	}
	
	
	@Override//batch edit
	public void bedit(ComCase ComCase,String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Tcomcase t = caseDao.get("select distinct t from Tcomcase t where t.id = :id", params);
		if (ComCase.getOp() != null && !ComCase.getOp().equalsIgnoreCase("")) {	
		t.setOp(ComCase.getOp());
		}
		if (ComCase.getPid() != null && !ComCase.getPid().equalsIgnoreCase("")&&ComCase.getPid() !=t.getId()) {		
			Tcomcase pt = caseDao.get(Tcomcase.class, ComCase.getPid());
			isChildren(t, pt);
		    t.setTcomcase(pt);
		}
		caseDao.update(t);
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
	private boolean isChildren(Tcomcase t, Tcomcase pt) {
		if (pt != null && pt.getTcomcase() != null) {
			if (pt.getTcomcase().getId().equalsIgnoreCase(t.getId())) {
				pt.setTcomcase(null);
				return true;
			} else {
				return isChildren(t, pt.getTcomcase());
			}
		}
		return false;
	}

	@Override
	public ComCase get(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Tcomcase t = caseDao.get("from Tcomcase t where t.id = :id", params);
		ComCase r = new ComCase();
		BeanUtils.copyProperties(t, r);
		if (t.getTcomcase() != null) {
			r.setPid(t.getTcomcase().getId());
			r.setPname(t.getTcomcase().getName());
		}/*
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
		
		List<String> n=caseDao.findString("select r.name from Tresource r where url='/caseController/manager' and tresourcetype_id=2 order by seq asc");
		
		// System.out.println(n.size());
	   for(String a:n){		
		   Map<String, String> re=new HashMap<String,String>();
		  re.put("project", a);	  
		  aa.add(re);
	   }
	   
        return aa;
	}
	@Override
	public List< String> getComProject() {
		// TODO Auto-generated method stub
		
		List<String> n=caseDao.findString("select r.id from Tresource r where pid='41c3bccb-39d0-4491-88ca-5df1cdfd6155'");
		List<String> all=new ArrayList<String>();
		for(int i=0;i<n.size();i++){
			List<String> small=caseDao.findString("select r.name from Tresource r where pid='"+n.get(i)+"'");
			all.addAll(small);
		}
		
		// System.out.println(all.size());
   
        return all;
	}
	
	public List< String> getName() {
		// TODO Auto-generated method stub
		List<String> n=caseDao.findString("select r.id from Tresource r where pid='41c3bccb-39d0-4491-88ca-5df1cdfd6155' and tresourcetype_id=2");
		// System.out.println(all.size());  
        return n;
	}
	
	public String getName(String id) {
		// TODO Auto-generated method stub
		List<String> n=caseDao.findString("select r.name from Tresource r where id='"+id+"'");
		// System.out.println(all.size());  
        return n.get(0);
	}
	
	public List< String> getChildProject(String p) {
		// TODO Auto-generated method stub
			List<String> small=caseDao.findString("select r.name from Tresource r where pid='"+p+"'");	
		// System.out.println(all.size());  
        return small;
	}
	
	
	
	public List<Map<String, String>> projectchild(String project) {
		// TODO Auto-generated method stub
		List<Map<String,String>> aa=new ArrayList<Map<String,String>>();		
		List<String> n=caseDao.findString("select name from Tcomcase where pid is null and project='"+project+"'");
		List<String> id=caseDao.findString("select id from Tcomcase where pid is null and project='"+project+"'");
		
		for(int i=0;i<n.size();i++){
			  Map<String, String> re=new HashMap<String,String>();
			  re.put("id", id.get(i));	  
			  re.put("name", n.get(i));	 
			  aa.add(re);
		}
  
        return aa;
	}
	
	@Override
	public List<ComCase> ctreeGrid(SessionInfo sessionInfo,String data) {
		List<Tcomcase> l = null;
		List<ComCase> lr = new ArrayList<ComCase>();
		
		if (sessionInfo != null&&data.isEmpty()) {	
		
			l = caseDao.find("select distinct t from Tcomcase t order by t.project,t.seq");
		} else {
					//l = caseDao.find("select distinct t from Tcomcase t join fetch t.Tcomcasetype type order by t.seq", params);
		}
		
		List<String> ppid=getName();

		List<Tcomcase> remove = new ArrayList<Tcomcase>();
		if (l != null && l.size() > 0) {
			int pidd=1;
			int piddd=100;
			for(int i=0;i<ppid.size();i++){
				if(i!=0){
					pidd++;
				}
				ComCase pp = new ComCase();
				pp.setId(String.valueOf(pidd));
				pp.setProject(getName(ppid.get(i)));
				lr.add(pp);
				List<String> child=getChildProject(ppid.get(i));
				
				for(int j=0;j<child.size();j++){
				
						
					
					ComCase ppp = new ComCase();
					ppp.setId(String.valueOf(piddd));
					ppp.setProject(child.get(j));
					ppp.setPid(String.valueOf(pidd));
					lr.add(ppp);
				for (Tcomcase t : l) {
				
					if(child.get(j).equals(t.getProject())){
					
					ComCase r = new ComCase();
					BeanUtils.copyProperties(t, r);
				
					if (t.getTcomcase() != null) {
						r.setPid(t.getTcomcase().getId());
					}else{
						r.setPid(String.valueOf(piddd));
					}

					if (t.getIcon() != null && !t.getIcon().equalsIgnoreCase("")) {
						r.setIconCls(t.getIcon());
					}
					lr.add(r);
					remove.add(t);
					}
				}
				l.removeAll(remove);
				piddd++;
				}
			}
	
		}

		return lr;
	}
	

}
