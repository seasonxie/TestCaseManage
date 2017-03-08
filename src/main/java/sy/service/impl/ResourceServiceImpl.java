package sy.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.ResourceDaoI;
import sy.dao.ResourceTypeDaoI;
import sy.dao.UserDaoI;
import sy.model.Tresource;
import sy.model.Trole;
import sy.model.Tuser;
import sy.pageModel.Resource;
import sy.pageModel.SessionInfo;
import sy.pageModel.Tree;
import sy.service.ResourceServiceI;

@Service
public class ResourceServiceImpl implements ResourceServiceI {

	@Autowired
	private ResourceDaoI resourceDao;

	@Autowired
	private ResourceTypeDaoI resourceTypeDao;

	@Autowired
	private UserDaoI userDao;

	@Override
	public List<Tree> tree(SessionInfo sessionInfo,String type) {
		List<Tresource> l = null;
		List<Tree> lt = new ArrayList<Tree>();

		Map<String, Object> params = new HashMap<String, Object>();
		
		if(!type.equals("all")){   // system tree  0  case tree  2 
		params.put("resourceTypeId", type);// 菜单类型的资源
		if (sessionInfo != null) {
			params.put("userId", sessionInfo.getId());// 自查自己有权限的资源
			l = resourceDao.find("select distinct t from Tresource t join fetch t.tresourcetype type join fetch t.troles role join role.tusers user where type.id = :resourceTypeId and user.id = :userId order by t.seq", params);
		} else {
			l = resourceDao.find("select distinct t from Tresource t join fetch t.tresourcetype type where type.id = :resourceTypeId order by t.seq", params);
		}
		}else{ // tree
			if (sessionInfo != null) {
				params.put("userId", sessionInfo.getId());// 自查自己有权限的资源
				l = resourceDao.find("select distinct t from Tresource t join fetch t.tresourcetype type join fetch t.troles role join role.tusers user where type.id in ('0','2') and user.id = :userId order by t.seq", params);
			} else {
				l = resourceDao.find("select distinct t from Tresource t join fetch t.tresourcetype type where type.id in ('0','2') order by t.seq", params);
			}	
		}

		if (l != null && l.size() > 0) {
			for (Tresource r : l) {
				Tree tree = new Tree();
				BeanUtils.copyProperties(r, tree);
				if (r.getTresource() != null) {
					tree.setPid(r.getTresource().getId());
				}
				if(r.getTresourcetype().getId().equals("2"))
				tree.setType(r.getTresourcetype().getId());
				tree.setText(r.getName());
				tree.setIconCls(r.getIcon());
				Map<String, Object> attr = new HashMap<String, Object>();
				
				attr.put("url", r.getUrl());
				tree.setAttributes(attr);
				
				if(sessionInfo.getGroupn().contains(tree.getText())){
					lt.add(0, tree);
				}else{
					lt.add(tree);	
				}
				
			}
		}
		return lt;
	}
	
	
	@Override
	public List<Tree> allTree(SessionInfo sessionInfo) {
		List<Tresource> l = null;
		List<Tree> lt = new ArrayList<Tree>();

		Map<String, Object> params = new HashMap<String, Object>();
		if (sessionInfo != null) {
			params.put("userId", sessionInfo.getId());// 查自己有权限的资源

			l = resourceDao.find("select distinct t from Tresource t join fetch t.tresourcetype type join fetch t.troles role join role.tusers user where user.id = :userId order by t.seq", params);
		} else {
			l = resourceDao.find("select distinct t from Tresource t join fetch t.tresourcetype type order by t.seq", params);
		}

		if (l != null && l.size() > 0) {
			for (Tresource r : l) {
				Tree tree = new Tree();
				BeanUtils.copyProperties(r, tree);
				if (r.getTresource() != null) {
					tree.setPid(r.getTresource().getId());
				}
				tree.setText(r.getName());
				tree.setIconCls(r.getIcon());
				Map<String, Object> attr = new HashMap<String, Object>();
				attr.put("url", r.getUrl());
				tree.setAttributes(attr);
				lt.add(tree);
			}
		}
		return lt;
	}

	@Override
	public List<Resource> treeGrid(SessionInfo sessionInfo) {
		List<Tresource> l = null;
		List<Resource> lr = new ArrayList<Resource>();

		Map<String, Object> params = new HashMap<String, Object>();
		if (sessionInfo != null&&sessionInfo.getName().contains("admin")) {
			params.put("userId", sessionInfo.getId());// 自查自己有权限的资源
			l = resourceDao.find("select distinct t from Tresource t join fetch t.tresourcetype type join fetch t.troles role join role.tusers user where user.id = :userId order by t.seq", params);
		} else if(sessionInfo != null) { //non admin---not LIKE 'zygl%'  and type.id in ('2')
			params.put("userId", sessionInfo.getId());// 自查自己有权限的资源
			l = resourceDao.find("select distinct t from Tresource t join fetch t.tresourcetype type join fetch t.troles role join role.tusers user where user.id = :userId and t.id not LIKE 'zygl%' and type.id in ('2') order by t.seq", params);
		}else{		
			l = resourceDao.find("select distinct t from Tresource t join fetch t.tresourcetype type order by t.seq", params);
		}

		if (l != null && l.size() > 0) {
			for (Tresource t : l) {
				Resource r = new Resource();
				BeanUtils.copyProperties(t, r);
				if (t.getTresource() != null) {
					r.setPid(t.getTresource().getId());
					r.setPname(t.getTresource().getName());
				}
				r.setTypeId(t.getTresourcetype().getId());
				r.setTypeName(t.getTresourcetype().getName());
				if (t.getIcon() != null && !t.getIcon().equalsIgnoreCase("")) {
					r.setIconCls(t.getIcon());
				}
				lr.add(r);
			}
		}

		return lr;
	}

	@Override
	public void add(Resource resource, SessionInfo sessionInfo) {
		Tresource t = new Tresource();
		Tresource tt = null;
		BeanUtils.copyProperties(resource, t);
		if (resource.getPid() != null && !resource.getPid().equalsIgnoreCase("")) {
			t.setTresource(resourceDao.get(Tresource.class, resource.getPid()));
			String pid=getId(resourceDao.get(Tresource.class, resource.getPid()).getName(),resource.getPid());//getID  different for pid  same for pname
			if(pid!=null&&resource.getTypeId().equals("2")){
				tt=newTresource(resource,pid);
			}
		}
		if (resource.getTypeId() != null && !resource.getTypeId().equalsIgnoreCase("")) {
			t.setTresourcetype(resourceTypeDao.getById(resource.getTypeId()));
		}
		if (resource.getIconCls() != null && !resource.getIconCls().equalsIgnoreCase("")) {
			t.setIcon(resource.getIconCls());
		}
		resourceDao.save(t);
		if(tt!=null)
		resourceDao.save(tt);
        //System.out.println(tt.getUrl());
       // System.out.println(tt.getName());
       /* System.out.println(tt.getTresource().getId());*/
		// 由于当前用户所属的角色，没有访问新添加的资源权限，所以在新添加资源的时候，将当前资源授权给当前用户的所有角色，以便添加资源后在资源列表中能够找到
		Tuser user = userDao.get(Tuser.class, sessionInfo.getId());
		Set<Trole> roles = user.getTroles();
		for (Trole r : roles) {
			r.getTresources().add(t);
			if(tt!=null)
			r.getTresources().add(tt);
		}
	}
	
	//执行集添加
	@Override
	public void addEx(Resource resource, SessionInfo sessionInfo) {
		Tresource t = new Tresource();
		BeanUtils.copyProperties(resource, t);
		if (resource.getPid() != null && !resource.getPid().equalsIgnoreCase("")) {
			t.setTresource(resourceDao.get(Tresource.class, resource.getPid()));
		}
		if (resource.getTypeId() != null && !resource.getTypeId().equalsIgnoreCase("")) {
			t.setTresourcetype(resourceTypeDao.getById(resource.getTypeId()));
		}
		if (resource.getIconCls() != null && !resource.getIconCls().equalsIgnoreCase("")) {
			t.setIcon(resource.getIconCls());
		}
		resourceDao.save(t);
	
		Tuser user = userDao.get(Tuser.class, sessionInfo.getId());
		Set<Trole> roles = user.getTroles();
		for (Trole r : roles) {
			r.getTresources().add(t);
		}
	}
	
	public Tresource newTresource(Resource resource,String pid){
		Tresource tt = new Tresource();
		BeanUtils.copyProperties(resource, tt);
		tt.setId(UUID.randomUUID().toString());
		tt.setTresource(resourceDao.get(Tresource.class, pid));
		tt.setUrl("");
		if (resource.getTypeId() != null && !resource.getTypeId().equalsIgnoreCase("")) {
			tt.setTresourcetype(resourceTypeDao.getById(resource.getTypeId()));
		}
		
		if (resource.getIconCls() != null && !resource.getIconCls().equalsIgnoreCase("")) {
			tt.setIcon(resource.getIconCls());
		}
		
		return tt;
	}
	@Override  //有相同的name才会增加
	public String getId(String name,String id) {
		//System.out.println(name);
		//System.out.println(id);
		// TODO Auto-generated method stub
		String rt=null;
		if(name.equals("业务用例")){
		rt="659273d2-0e92-4879-a8af-77d05800e7fd";	//执行用例id
		}else{  //name相同 id不同
		List<String> n=resourceDao.findString("select id from Tresource where name='"+name+"' and id!='"+id+"' and tresourcetype_id in ('2')");
		//System.out.println(n.size());
		if(n.size()>0){
			rt=n.get(0);
		}
		}
		//System.out.println(rt);
		// System.out.println(all.size());  
        return rt;
	}
	
	
 //edit for getid  
	public String getIdEdit(String name,String id) {
		//System.out.println(name);
		//System.out.println(id);
		// TODO Auto-generated method stub
		String rt=null;
		 //name相同 id不同
		List<String> n=resourceDao.findString("select id from Tresource where name='"+name+"' and id!='"+id+"' and tresourcetype_id in ('2')");
		//System.out.println(n.size());
		if(n.size()>0){
			rt=n.get(0);
		}
		
		//System.out.println(rt);
		// System.out.println(all.size());  
        return rt;
	}


	public String getName(String id) {
		
		String rt=null;
		
		List<String> n=resourceDao.findString("select name from Tresource where id='"+id+"' and tresourcetype_id in ('2')");
		//System.out.println(n.size());
		if(n.size()>0){
			rt=n.get(0);
		}
		
  
        return rt;
	}
	
	@Override
	public void delete(String id) {
		//System.out.println(id);
		Tresource t = resourceDao.get(Tresource.class, id);
		
		String pid=null;
		
		String name=getName(id);
		if(name!=null)
		pid=getId(name,id);
		//System.out.println(pid);
		if(pid!=null){
		Tresource tt = resourceDao.get(Tresource.class, pid);
		del(tt);
		}
		del(t);
		
	}

	private void del(Tresource t) {
		if (t.getTresources() != null && t.getTresources().size() > 0) {
			for (Tresource r : t.getTresources()) {
				del(r);
			}
		}
		resourceDao.delete(t);
	}

	@Override
	public void edit(Resource resource) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", resource.getId());
		Tresource t = resourceDao.get("select distinct t from Tresource t where t.id = :id", params);
		doEdit(resource,t.getName());//修改同名
		if (t != null) {
			BeanUtils.copyProperties(resource, t);
			if (resource.getTypeId() != null && !resource.getTypeId().equalsIgnoreCase("")) {
				t.setTresourcetype(resourceTypeDao.getById(resource.getTypeId()));// 赋值资源类型
			}
			if (resource.getIconCls() != null && !resource.getIconCls().equalsIgnoreCase("")) {
				t.setIcon(resource.getIconCls());
			}
			if (resource.getPid() != null && !resource.getPid().equalsIgnoreCase("")) {// 说明前台选中了上级资源
				Tresource pt = resourceDao.get(Tresource.class, resource.getPid());
				isChildren(t, pt);// 说明要将当前资源修改到当前资源的子/孙子资源下
				t.setTresource(pt);
			} else {
				t.setTresource(null);// 前台没有选中上级资源，所以就置空
			}
		}
	
		
		
	
	}
  
	
	public void doEdit(Resource resource,String name){
		
		//获取要修改的id
		String pid=getIdEdit(name,resource.getId());

		Map<String, Object> paramss = new HashMap<String, Object>();
		paramss.put("id", resource.getPid());
		Tresource te = resourceDao.get("select distinct t from Tresource t where t.id = :id", paramss);
		
		//获取要修改的PID
		String ppid=getIdEdit(te.getName(),resource.getPid());
		System.out.println(ppid);
		if(pid!=null){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", pid);
			Tresource t = resourceDao.get("select distinct t from Tresource t where t.id = :id", params);
			String oid=t.getId();
			if (t != null) {			
				t.setId(oid);
				t.setName(resource.getName());
				t.setSeq(resource.getSeq());
				t.setTemplate(resource.getTemplate());
				if (resource.getTypeId() != null && !resource.getTypeId().equalsIgnoreCase("")) {
					t.setTresourcetype(resourceTypeDao.getById(resource.getTypeId()));// 赋值资源类型
				}
				if (resource.getIconCls() != null && !resource.getIconCls().equalsIgnoreCase("")) {
					t.setIcon(resource.getIconCls());
				}
				//当修改顶级节点的时候
				if(resource.getPid().equals("c1237836-7868-4cd0-8295-2ce936b14e55")){
					ppid="659273d2-0e92-4879-a8af-77d05800e7fd";
				}else if(resource.getPid().equals("659273d2-0e92-4879-a8af-77d05800e7fd")){
					ppid="c1237836-7868-4cd0-8295-2ce936b14e55";
				}
			
				if (ppid!= null && !ppid.equalsIgnoreCase("")) {// 说明前台选中了上级资源				
					Tresource pt = resourceDao.get(Tresource.class, ppid);
					isChildren(t, pt);// 说明要将当前资源修改到当前资源的子/孙子资源下
					t.setTresource(pt);
				} else {
					t.setTresource(null);// 前台没有选中上级资源，所以就置空
				}
				} 
			resourceDao.update(t);
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
	private boolean isChildren(Tresource t, Tresource pt) {
		if (pt != null && pt.getTresource() != null) {
			if (pt.getTresource().getId().equalsIgnoreCase(t.getId())) {
				pt.setTresource(null);
				return true;
			} else {
				return isChildren(t, pt.getTresource());
			}
		}
		return false;
	}

	@Override
	public Resource get(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Tresource t = resourceDao.get("from Tresource t left join fetch t.tresource resource left join fetch t.tresourcetype resourceType where t.id = :id", params);
		Resource r = new Resource();
		BeanUtils.copyProperties(t, r);
		if (t.getTresource() != null) {
			r.setPid(t.getTresource().getId());
			r.setPname(t.getTresource().getName());
		}
		r.setTypeId(t.getTresourcetype().getId());
		r.setTypeName(t.getTresourcetype().getName());
		if (t.getIcon() != null && !t.getIcon().equalsIgnoreCase("")) {
			r.setIconCls(t.getIcon());
		}
		return r;
	}


	@Override
	public List<Tree> tree2(SessionInfo sessionInfo, String type) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isExisted(String name) {
		List<String> num=resourceDao.findString("select id from Tresource where name='"+name+"'");
		if(num.size()>0){
		return false;
		}else{
			return true;
		}
	}

}
