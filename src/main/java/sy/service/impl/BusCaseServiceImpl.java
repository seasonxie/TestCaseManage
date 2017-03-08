package sy.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.BusCaseDaoI;
import sy.dao.CaseDaoI;
import sy.dao.ResourceTypeDaoI;
import sy.dao.RoleDaoI;
import sy.dao.UserDaoI;
import sy.model.Tbuscase;
import sy.model.Tcomcase;
import sy.model.Texcase;
import sy.model.Trole;
import sy.model.Tuser;
import sy.pageModel.Bug;
import sy.pageModel.BusCase;
import sy.pageModel.ComCase;
import sy.pageModel.DataGrid;
import sy.pageModel.ExCase;
import sy.pageModel.PageHelper;
import sy.pageModel.Resource;
import sy.pageModel.SessionInfo;
import sy.pageModel.Tree;
import sy.pageModel.User;
import sy.service.BusCaseServiceI;
import sy.service.CaseServiceI;
import sy.service.UserServiceI;
import sy.util.MD5Util;

@Service
public class BusCaseServiceImpl implements BusCaseServiceI {

	@Autowired
	private BusCaseDaoI buscaseDao;


	@Autowired
	private UserDaoI userDao;

	@Override
	public List<Tree> tree(SessionInfo sessionInfo) {
		List<Tbuscase> l = null;
		List<Tree> lt = new ArrayList<Tree>();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resourceTypeId", "0");// 菜单类型的资源

		if (sessionInfo != null) {
			params.put("userId", sessionInfo.getId());// 自查自己有权限的资源
			l = buscaseDao.find("select distinct t from Tbuscase t join fetch t.Tbuscasetype type join fetch t.troles role join role.tusers user where type.id = :resourceTypeId and user.id = :userId order by t.seq", params);
		} else {
			l = buscaseDao.find("select distinct t from Tbuscase t join fetch t.Tbuscasetype type where type.id = :resourceTypeId order by t.seq", params);
		}

		if (l != null && l.size() > 0) {
			for (Tbuscase r : l) {
				Tree tree = new Tree();
				BeanUtils.copyProperties(r, tree);
				if (r.getPid()!= null) {
					tree.setPid(r.getPid());
				}/*else{
					tree.setPid("0");
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
	public List<Tree> allTree(SessionInfo sessionInfo,String project, PageHelper ph) {
		List<Tbuscase> l = null;
		List<Tree> lt = new ArrayList<Tree>();
		System.out.println(project);
		String[] projects=project.split("\\*");

		for(String p:projects){
		if (sessionInfo != null) {			
			l = buscaseDao.find("select distinct t from Tbuscase t  where t.treeid='"+p+"' order by t.seq");
		} 
		if (l != null && l.size() > 0) {
			for (Tbuscase r : l) {
				Tree tree = new Tree();
				BeanUtils.copyProperties(r, tree);
				if (r.getPid() != null) {
					tree.setPid(r.getPid());
				}
				if(r.getModulechild()==null&&r.getModule()==null){ //模块和子模块等于null，---数据采集模式
					try{
						String text="";
						if(!r.getFivepage().isEmpty()){
							text=r.getFivepage();
							tree.setText(text+" -- "+r.getName()+" -- "+r.getCasestep());
							lt.add(tree);
							continue;
						}else if(!r.getFourpage().isEmpty()){
							text=r.getFourpage();
							tree.setText(text+" -- "+r.getName()+" -- "+r.getCasestep());
							lt.add(tree);
							continue;
						}else if(!r.getThrpage().isEmpty()){
							text=r.getThrpage();
							tree.setText(text+" -- "+r.getName()+" -- "+r.getCasestep());
							lt.add(tree);
							continue;
						}else if(!r.getSecpage().isEmpty()){
							text=r.getSecpage();
							tree.setText(text+" -- "+r.getName()+" -- "+r.getCasestep());
							lt.add(tree);
							continue;
						}else if(!r.getFristpage().isEmpty()){
							text=r.getFristpage();
							tree.setText(text+" -- "+r.getName()+" -- "+r.getCasestep());
							lt.add(tree);
							continue;
						}else{
							text=r.getFristpage();
							tree.setText(text+" -- "+r.getName()+" -- "+r.getCasestep());
							lt.add(tree);
							continue;
						}
						}catch(Exception e){
							e.printStackTrace();
						}
				}else{//再次判断
					
					if(r.getModulechild().isEmpty()&&r.getModule().isEmpty()){//模块盒子模块都为空
						try{
							
							String text="";
							if(!r.getFivepage().isEmpty()){
								text=r.getFivepage();
								tree.setText(text+" -- "+r.getName()+" -- "+r.getCasestep());
								lt.add(tree);
								continue;
							}else if(!r.getFourpage().isEmpty()){
								text=r.getFourpage();
								tree.setText(text+" -- "+r.getName()+" -- "+r.getCasestep());
								lt.add(tree);
								continue;
							}else if(!r.getThrpage().isEmpty()){
								text=r.getThrpage();
								tree.setText(text+" -- "+r.getName()+" -- "+r.getCasestep());
								lt.add(tree);
								continue;
							}else if(!r.getSecpage().isEmpty()){
								text=r.getSecpage();
								tree.setText(text+" -- "+r.getName()+" -- "+r.getCasestep());
								lt.add(tree);
								continue;
							}else if(!r.getFristpage().isEmpty()){
								text=r.getFristpage();
								tree.setText(text+" -- "+r.getName()+" -- "+r.getCasestep());
								lt.add(tree);
								continue;
							}else{
								text=r.getFristpage();
								tree.setText(text+" -- "+r.getName()+" -- "+r.getCasestep());
								lt.add(tree);
								continue;
							}
							}catch(Exception e){
								e.printStackTrace();
							}
					}else{ //都不为空，为其他模板  公共和多媒体
						String text="";
						if(!r.getModule().isEmpty()){
							text=r.getModule();
						}
						if(!r.getModulechild().isEmpty()){
							text=text+" -- "+r.getModulechild();
						}
						if(!r.getName().isEmpty()){
							text=text+" -- "+r.getName();
						}
						tree.setText(text);					
					lt.add(tree);
					}
					
				}
				
				
			}
		}
		}
		
		
		
		return lt;


		
	}
	
	@Override
	public Long getTotal(String project, BusCase bug, PageHelper ph) {
		Map<String, Object> params = new HashMap<String, Object>();
		return buscaseDao.count("select count(*) from  Tbuscase t "+whereHql(bug,params,project,ph.getTreeid())+"", params);
	}

	@Override
	public Map<String,Object> treeGrid(SessionInfo sessionInfo,String data,BusCase bug, PageHelper ph) {
		Map<String,Object> datagrid=new HashMap<String,Object>();
		
		List<Tbuscase> l = null;
		List<BusCase> lr = new ArrayList<BusCase>();
		Map<String, Object> params = new HashMap<String, Object>();

		if (!data.isEmpty()) {	
	        String where=whereHql(bug,params,data,ph.getTreeid());
			l = buscaseDao.find("select distinct t from Tbuscase t "+where+" order by t.project,t.seq",params, ph.getPage(), ph.getRows());
		}
		if (l != null && l.size() > 0) {
			for (Tbuscase t : l) {
				BusCase r = new BusCase();
				BeanUtils.copyProperties(t, r);
				if (t.getPid()!= null) {
					r.setPid(t.getPid());
					//r.setPname(t.getTcomcase().getName());
				}
				//r.setTypeId(t.getTcomcasetype().getId());
			//	r.setTypeName(t.getTcomcasetype().getName());
				if (t.getIcon() != null && !t.getIcon().equalsIgnoreCase("")) {
					r.setIconCls(t.getIcon());
				}
				if(t.getExpire()!=null)
				r.setExpire(t.getExpire().replace( "<br>","\n"));
				if(t.getStep()!=null)
				r.setStep(t.getStep().replace( "<br>","\n"));
				if(t.getPre()!=null)
				r.setPre(t.getPre().replace("<br>","\n"));
			//r.setChildren(aa);
				lr.add(r);
			}
		}
	 /*  }else{
		   System.out.println("-aaaaaaa-----------+++++++");
		   l = buscaseDao.find("select distinct t from Tbuscase t where t.project='"+data+"' and (t.pid is null or t.pid='') order by t.seq", ph.getPage(), ph.getRows());   
		   if (l != null && l.size() > 0) {
				for (Tbuscase t : l) {
				
					BusCase r = new BusCase();
					BeanUtils.copyProperties(t, r);	
					lr.add(r);
				
					List<Tbuscase> ll =buscaseDao.find("select distinct t from Tbuscase t where t.project='"+data+"' and t.pid='"+t.getId()+"' order by t.seq", ph.getPage(), ph.getRows());
				
					if(ll.size()>0){
						dogetchild(ll,data,lr,ph);
					}
				}
			}
	   }*/

/*	if (bug.getName() != null||bug.getStep() != null) {
		if (!bug.getName().isEmpty()||!bug.getStep().isEmpty()) {
		String pid="";
		List<BusCase> lrp = new ArrayList<BusCase>(lr);

		for(BusCase b:lrp){
			if(b.getPid()!=null){
				if(!b.getPid().isEmpty()){
			String pidd=pid;
			if(!pidd.contains(b.getPid())){
			
			Tbuscase t = buscaseDao.get(Tbuscase.class, b.getPid());
			BusCase r = new BusCase();
			BeanUtils.copyProperties(t, r);
			if (t.getTbuscase() != null) {
				r.setPid(t.getTbuscase().getId());
			}

			if (t.getIcon() != null && !t.getIcon().equalsIgnoreCase("")) {
				r.setIconCls(t.getIcon());
			}
			lr.add(r);
			pid+=b.getPid();
			}
			}
			}
			
		}
		}
		}*/
	datagrid.put("rows", lr);
	//datagrid.put("total",String.valueOf( buscaseDao.count("select count(*) from  Tbuscase t "+whereHql(bug,params,data)+"", params)));
	return datagrid;
	}
	
	private String whereHql(BusCase bug, Map<String, Object> params,String data,String treeid) {
		String[] datas=data.split(",");
		String search="";
		if(datas.length==1){
			search="'"+datas[0]+"'";
		}else if(datas.length>0&&datas.length!=1){
		for(int i=0;i<datas.length;i++){
			if(datas.length-i==1){
				search+="'"+datas[i]+"'";	
			}else{
			search+="'"+datas[i]+"'"+",";
			}
		}
		}
		String whereHql = "where 1=1 and (t.treeid in("+search+") or t.treeid='"+treeid+"') ";
		
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
			if (bug.getUpdater() != null&&!bug.getUpdater().isEmpty()) {
				whereHql += " and t.updater like :updater";
				params.put("updater","%%" + bug.getUpdater() + "%%");
			}
		}
		System.out.println(whereHql);
		return whereHql;
	}
	
    static  int order=0;
	@Override
	public void setOrder(String data) {
		order=1;
		List<Tbuscase> l = null;	
	
		if (!data.isEmpty()) {	
		
			l = buscaseDao.find("select distinct t from Tbuscase t where t.treeid='"+data+"' and (t.pid is null or t.pid='') order by t.seq");
	
		}
		
		if (l != null && l.size() > 0) {
			for (Tbuscase t : l) {
			
				updateCase(t);
			
				List<Tbuscase> ll =buscaseDao.find("select distinct t from Tbuscase t where t.treeid='"+data+"' and t.pid='"+t.getId()+"' order by t.seq");
			
				if(ll.size()>0){
					dogetchild(ll,data,order);
				}
			}
		}

	}
	
	public void dogetchild(List<Tbuscase> ll,String data,int order) {
		for (Tbuscase t : ll) {
		
			updateCase(t);
			
			List<Tbuscase> lll =buscaseDao.find("select distinct t from Tbuscase t where t.treeid='"+data+"' and t.pid='"+t.getId()+"' order by t.seq");
			if(lll.size()>0){
				dogetchild(lll,data,order);
			}
		}
		
	}
	
	public void updateCase(Tbuscase b){
		b.setSeq(order);
		buscaseDao.update(b);
		order++;
	}
	


	@Override
	public void add(BusCase BusCase, SessionInfo sessionInfo) {
		Tbuscase t = new Tbuscase();
		BeanUtils.copyProperties(BusCase, t);
		System.out.println(BusCase.getPid());
	/*	if (BusCase.getPid() != null && !BusCase.getPid().equalsIgnoreCase("")) {
			t.setTbuscase(buscaseDao.get(Tbuscase.class, BusCase.getPid()));
			
		}*/
	/*	t.setExpire(BusCase.getExpire().replace("\n", "<br>"));
		t.setStep(BusCase.getStep().replace("\n", "<br>"));*/
		
		/*if (ComCase.getTypeId() != null && !ComCase.getTypeId().equalsIgnoreCase("")) {
			t.setTcomcasetype(resourceTypeDao.getById(ComCase.getTypeId()));
		}*/
		t.setPre(BusCase.getPre().replace("&lt;br&gt;", "<br>").replace("script", "sc**pt"));
		t.setStep(BusCase.getStep().replace("&lt;br&gt;", "<br>").replace("script", "sc**pt"));
		t.setExpire(BusCase.getExpire().replace("&lt;br&gt;", "<br>").replace("script", "sc**pt"));
		t.setRemark(BusCase.getRemark().replace("&lt;br&gt;", "<br>").replace("script", "sc**pt"));
		t.setUpdater(sessionInfo.getName());
		t.setUpdatetime(new Date());
		if (BusCase.getIconCls() != null && !BusCase.getIconCls().equalsIgnoreCase("")) {
			t.setIcon(BusCase.getIconCls());
		}
		buscaseDao.save(t);

	/*	// 由于当前用户所属的角色，没有访问新添加的资源权限，所以在新添加资源的时候，将当前资源授权给当前用户的所有角色，以便添加资源后在资源列表中能够找到
		Tuser user = userDao.get(Tuser.class, sessionInfo.getId());
		Set<Trole> roles = user.getTroles();
		for (Trole r : roles) {
			r.getTcomcases().add(t);
		}*/
	}
	
	@Override
	public void insert(String id,String pid, SessionInfo sessionInfo) {
		List<Tbuscase> l = null;
		 Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", id);
			Tbuscase t = buscaseDao.get("from Tbuscase t where t.id = :id", params);
	        if(pid!=null){
			l = buscaseDao.find("select distinct t from Tbuscase t where t.pid='"+pid+"' order by t.seq,t.project");
	        }else{
	        	l = buscaseDao.find("select distinct t from Tbuscase t where (t.pid is "+pid+" or t.pid='') and project='"+t.getProject()+"' order by t.seq,t.project");
	        }
	

	       
			Tbuscase ta=new Tbuscase(); 
		    System.out.println("--"+l.indexOf(t));
		    Double num=t.getSeq();
		    num=num+1;
		    ta.setSeq(num);
		    int j=1;
			for(int i=l.indexOf(t)+1;i<l.size();i++){
				Tbuscase tb=new Tbuscase(); 
				//l.get(i);
				l.get(i).setSeq(num+j);
				buscaseDao.update(l.get(i));
				j++;
			}
			
			ta.setId(UUID.randomUUID().toString());
			System.out.println(pid);
			if(pid!=null){
			//ta.setTbuscase(buscaseDao.get(Tbuscase.class, pid));
			ta.setPid(pid);
			}else{
				ta.setPid(null);
				//ta.setTbuscase(null);
			}
			ta.setUpdater(sessionInfo.getName());
			ta.setUpdatetime(new Date());
			ta.setProject(t.getProject());
			ta.setModule(t.getModule());
			ta.setName(t.getName());
			ta.setModulechild(t.getModulechild());
			ta.setFristpage(t.getFristpage());
			ta.setFivepage(t.getFivepage());
			ta.setFourpage(t.getFourpage());
			ta.setSecpage(t.getSecpage());
			ta.setThrpage(t.getThrpage());
			ta.setTreeid(t.getTreeid());
		    buscaseDao.save(ta);

	}
	@Override
	public void copyInsert(String id,String targerId, SessionInfo sessionInfo) {
		List<Tbuscase> l = null;
		Tbuscase t=new Tbuscase();
		 String[] ids=id.split("\\*");
		    Map<String, Object> params = new HashMap<String, Object>();
		    params.put("id", targerId);
			t = buscaseDao.get("from Tbuscase t where t.id = :id", params);	
			
		    int k=1;
		    if(t.getPid()!=null){
				l = buscaseDao.find("select distinct t from Tbuscase t where t.pid='"+t.getPid()+"' order by t.seq,t.project");
		        }else{
		        	l = buscaseDao.find("select distinct t from Tbuscase t where (t.pid is "+t.getPid()+" or t.pid='') and project='"+t.getProject()+"' order by t.seq,t.project");
		        }
		    Double num=t.getSeq();
			for(String i:ids){	
				 Map<String, Object> paramss = new HashMap<String, Object>();
				Tbuscase tt=new Tbuscase();
				paramss.put("id", i);
				tt = buscaseDao.get("from Tbuscase t where t.id = :id", paramss);	
				
				Tbuscase ta=new Tbuscase(); 
				BeanUtils.copyProperties(tt, ta);	
				
			    num=num+k;
			    ta.setSeq(num);
			    System.out.println(num);
			    ta.setId(UUID.randomUUID().toString());
			
			   if(t.getPid()!=null){
				ta.setPid(t.getPid());
				}else{
					ta.setPid(null);
				}
				ta.setUpdater(sessionInfo.getName());
				ta.setUpdatetime(new Date());
			    buscaseDao.save(ta);
			 
			}	
	      
	        
                int j=1;
			for(int i=l.indexOf(t)+1;i<l.size();i++){
				l.get(i).setSeq(num+j);
				buscaseDao.update(l.get(i));
				j++;
			}
			
			

	}
	
	@Override
	public void addorUpdate(BusCase BusCase, SessionInfo sessionInfo) {
		Tbuscase t = new Tbuscase();
		BeanUtils.copyProperties(BusCase, t);
		System.out.println(BusCase.getPid());
	/*	if (BusCase.getPid() != null && !BusCase.getPid().equalsIgnoreCase("")) {
			t.setTbuscase(buscaseDao.get(Tbuscase.class, BusCase.getPid()));
			
		}*/
	/*	t.setExpire(BusCase.getExpire().replace("\n", "<br>"));
		t.setStep(BusCase.getStep().replace("\n", "<br>"));*/
		
		/*if (ComCase.getTypeId() != null && !ComCase.getTypeId().equalsIgnoreCase("")) {
			t.setTcomcasetype(resourceTypeDao.getById(ComCase.getTypeId()));
		}*/
		t.setPre(BusCase.getPre().replace("&lt;br&gt;", "<br>").replace("script", "sc**pt"));
		t.setStep(BusCase.getStep().replace("&lt;br&gt;", "<br>").replace("script", "sc**pt"));
		t.setExpire(BusCase.getExpire().replace("&lt;br&gt;", "<br>").replace("script", "sc**pt"));
		t.setRemark(BusCase.getRemark().replace("&lt;br&gt;", "<br>").replace("script", "sc**pt"));
		t.setUpdater(sessionInfo.getName());
		t.setUpdatetime(new Date());
		if (BusCase.getIconCls() != null && !BusCase.getIconCls().equalsIgnoreCase("")) {
			t.setIcon(BusCase.getIconCls());
		}
		buscaseDao.update(t);

		
	}

	
	@Override
	public void copy(String id,String pid,SessionInfo sessionInfo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Tbuscase t = buscaseDao.get("select distinct t from Tbuscase t where t.id = :id", params);	
		if(t!=null){
		Tbuscase tt =new Tbuscase();
		BeanUtils.copyProperties(t, tt);
		tt.setProject(t.getProject());
		tt.setUpdater(sessionInfo.getName());
		tt.setUpdatetime(new Date());
		tt.setId(UUID.randomUUID().toString());
		System.out.println(pid);
		if(pid!=null){
			tt.setPid(pid);
	//	tt.setTbuscase(buscaseDao.get(Tbuscase.class, pid));
		}else{
			tt.setPid(null);
		}
		//tt.setTbuscase(null);		
		buscaseDao.save(tt);
		}

	}

	@Override
	public String delete(String id) {
		Tbuscase t = buscaseDao.get(Tbuscase.class, id);
		del(t);
		return t.getProject()+" -- "+t.getId();
	}

	private void del(Tbuscase t) {
		/*if (t.getTcomcase() != null && t.getTcomcase().size() > 0) {
			for (Tcomcase r : t.getTcomcase()) {
				del(r);
			}
		}*/
		buscaseDao.delete(t);
	}

	@Override
	public void edit(BusCase BusCase,SessionInfo sessionInfo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", BusCase.getId());
		Tbuscase t = buscaseDao.get("select distinct t from Tbuscase t where t.id = :id", params);
		if (t != null) {
			BeanUtils.copyProperties(BusCase, t);
			/*if (ComCase.getTypeId() != null && !ComCase.getTypeId().equalsIgnoreCase("")) {
				t.setTcomcasetype(resourceTypeDao.getById(resource.getTypeId()));// 赋值资源类型
			}*/
			if (BusCase.getIconCls() != null && !BusCase.getIconCls().equalsIgnoreCase("")) {
				t.setIcon(BusCase.getIconCls());
			}
			t.setExpire(BusCase.getExpire().replace("&lt;br&gt;", "<br>").replace("script", "sc**pt"));
			t.setStep(BusCase.getStep().replace("&lt;br&gt;", "<br>").replace("script", "sc**pt"));
			t.setPre(BusCase.getPre().replace("&lt;br&gt;", "<br>").replace("script", "sc**pt"));
			t.setUpdater(sessionInfo.getName());
			t.setUpdatetime(new Date());
	/*		t.setExpire(BusCase.getExpire().replace("\n", "<br>"));
			t.setStep(BusCase.getStep().replace("\n", "<br>"));*/
			/*if (BusCase.getPid() != null && !BusCase.getPid().equalsIgnoreCase("")) {// 说明前台选中了上级资源
				Tbuscase pt = buscaseDao.get(Tbuscase.class, BusCase.getPid());
				//isChildren(t, pt);// 说明要将当前资源修改到当前资源的子/孙子资源下
				t.setTbuscase(pt);
			} else {
				t.setTbuscase(null);// 前台没有选中上级资源，所以就置空
			}*/
		}
	}

	@Override
	public void edit(String oid, String nid,SessionInfo sessionInfo) {
		// TODO Auto-generated method stub
		System.out.println(oid);
		String[] os=oid.split("\\*");
		System.out.println(os.length);
		for(String o:os){
			Tbuscase pt = buscaseDao.get(Tbuscase.class, o);
			if(pt!=null){
				pt.setPid(nid);
			//pt.setTbuscase(buscaseDao.get(Tbuscase.class, nid));
			pt.setUpdater(sessionInfo.getName());
			pt.setUpdatetime(new Date());
			buscaseDao.update(pt);
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
	/*private boolean isChildren(Tbuscase t, Tbuscase pt) {
		if (pt != null && pt.getTbuscase() != null) {
			if (pt.getTbuscase().getId().equalsIgnoreCase(t.getId())) {
				pt.setTbuscase(null);
				return true;
			} else {
				return isChildren(t, pt.getTbuscase());
			}
		}
		return false;
	}*/

	@Override
	public BusCase get(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Tbuscase t = buscaseDao.get("from Tbuscase t where t.id = :id", params);
		BusCase r = new BusCase();
		BeanUtils.copyProperties(t, r);
	/*	if (t.getTbuscase() != null) {
			r.setPid(t.getTbuscase().getId());
			r.setPname(t.getTbuscase().getName());
		}
		r.setTypeId(t.getTcomcasetype().getId());
		r.setTypeName(t.getTcomcasetype().getName());*/
		if(t.getExpire()!=null)
		r.setExpire(t.getExpire().replace( "<br>","\n"));
		if(t.getStep()!=null)
		r.setStep(t.getStep().replace( "<br>","\n"));
		if(t.getPre()!=null)
		r.setPre(t.getPre().replace("<br>","\n"));
		if (t.getIcon() != null && !t.getIcon().equalsIgnoreCase("")) {
			r.setIconCls(t.getIcon());
		}
		return r;
	}
	
	@Override
	public Tbuscase getT(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Tbuscase t = buscaseDao.get("from Tbuscase t where t.id = :id", params);
		return t;
	}


	@Override
	public List<Map<String, String>> project() {
		// TODO Auto-generated method stub
		List<Map<String,String>> aa=new ArrayList<Map<String,String>>();
		
		List<String> n=buscaseDao.findString("select r.name from Tresource r where url='/buscaseController/manager' and tresourcetype_id=2");
		
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
		List<String> n=buscaseDao.findString("select name from Tbuscase where pid is null and project='"+project+"'");
		List<String> id=buscaseDao.findString("select id from Tbuscase where pid is null and project='"+project+"'");
		
		for(int i=0;i<n.size();i++){
			  Map<String, String> re=new HashMap<String,String>();
			  re.put("id", id.get(i));	  
			  re.put("name", n.get(i));	 
			  aa.add(re);
		}
  
        return aa;
	}

	
	
	@Override
	public List<BusCase> collecttreeGrid(SessionInfo sessionInfo,String data) {
		List<Tbuscase> l = null;
		List<BusCase> lr = new ArrayList<BusCase>();

		
		if (!data.isEmpty()) {	
	
			l = buscaseDao.find("select distinct t from Tbuscase t where PID='"+data+"' order by t.project,t.seq");
		}

		if (l != null && l.size() > 0) {
			for (Tbuscase t : l) {
				BusCase r = new BusCase();
				BeanUtils.copyProperties(t, r);
				if (t.getPid() != null) {
					r.setPid(t.getPid());
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

	
	


	


	

}
