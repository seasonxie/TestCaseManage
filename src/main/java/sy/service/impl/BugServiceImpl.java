package sy.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.BugDaoI;
import sy.dao.BugTypeDaoI;
import sy.model.Tbug;
import sy.pageModel.Bug;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.pageModel.SessionInfo;
import sy.service.BugServiceI;
import sy.util.ClobUtil;
import sy.util.MockUtil;

@Service
public class BugServiceImpl implements BugServiceI {

	@Mock
	@Autowired
	private BugDaoI bugDao;

	@Autowired
	private BugTypeDaoI bugTypeDao;

	@Override
	public DataGrid dataGrid(Bug bug, PageHelper ph) {
		DataGrid dg = new DataGrid();
		List<Bug> bl = new ArrayList<Bug>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from Tbug t ";
		String joinHql = " left join fetch t.tbugtype type ";
		List<Tbug> l = bugDao.find(hql + joinHql + whereHql(bug, params) + orderHql(ph), params, ph.getPage(), ph.getRows());
		if (l != null && l.size() > 0) {
			for (Tbug t : l) {
				Bug b = new Bug();
				BeanUtils.copyProperties(t, b);
				/*b.setTypeId(t.getTbugtype().getId());
				b.setTypeName(t.getTbugtype().getName());*/
				bl.add(b);
			}
		}
		
		dg.setTotal(bugDao.count("select count(*) " + hql + " left join t.tbugtype type " + whereHql(bug, params), params));
		dg.setRows(bl);
		
		return dg;
	}

	private String orderHql(PageHelper ph) {
		String orderString = "";
		if (ph.getSort() != null && ph.getOrder() != null) {
			orderString = " order by t." + ph.getSort() + " " + ph.getOrder();
		}
		return orderString;
	}

	private String whereHql(Bug bug, Map<String, Object> params) {
		String whereHql = "";
		if (bug != null) {
			whereHql += " where 1=1 ";
			if (bug.getName() != null) {
				whereHql += " and t.name like :name";
				params.put("name", "%%" + bug.getName() + "%%");
			}
			if (bug.getCreatedatetimeStart() != null) {
				whereHql += " and t.createdatetime >= :createdatetimeStart";
				params.put("createdatetimeStart", bug.getCreatedatetimeStart());
			}
			if (bug.getCreatedatetimeEnd() != null) {
				whereHql += " and t.createdatetime <= :createdatetimeEnd";
				params.put("createdatetimeEnd", bug.getCreatedatetimeEnd());
			}
			if (bug.getModifydatetimeStart() != null) {
				whereHql += " and t.modifydatetime >= :modifydatetimeStart";
				params.put("modifydatetimeStart", bug.getModifydatetimeStart());
			}
			if (bug.getModifydatetimeEnd() != null) {
				whereHql += " and t.modifydatetime <= :modifydatetimeEnd";
				params.put("modifydatetimeEnd", bug.getModifydatetimeEnd());
			}
			if (bug.getTypeId() != null && !bug.getTypeId().trim().equals("")) {
				whereHql += " and type.id = :type ";
				params.put("type", bug.getTypeId());
			}
		}
		return whereHql;
	}

	@Override
	public void add(String content,SessionInfo sessionInfo) {
		Tbug t = new Tbug();
		t.setId(UUID.randomUUID().toString());
		t.setCreatedatetime(new Date());
		t.setName(sessionInfo.getName());
		t.setNote(content);
		bugDao.save(t);
	}

	@Override
	public Bug get(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Tbug t = bugDao.get("from Tbug t join fetch t.tbugtype bugType where t.id = :id", params);
		Bug b = new Bug();
		BeanUtils.copyProperties(t, b, new String[] { "note" });
		b.setNote(t.getNote());
		b.setTypeId(t.getTbugtype().getId());
		b.setTypeName(t.getTbugtype().getName());
		return b;
	}

	@Override
	public void edit(Bug bug) {
		Tbug t = bugDao.get(Tbug.class, bug.getId());
		if (t != null) {
			BeanUtils.copyProperties(bug, t, new String[] { "id", "note", "createdatetime" });
			t.setTbugtype(bugTypeDao.getById(bug.getTypeId()));
			t.setModifydatetime(new Date());
			t.setNote(bug.getNote());
		}
	}

	@Override
	public void delete(String id) {
		bugDao.delete(bugDao.get(Tbug.class, id));
	}

	@Override
	public Map<String, List<Object>> clo() {
		
		MockitoAnnotations.initMocks(this); //运行到这里注解生效
		List<Object[]> mockdata=new LinkedList<Object[]>();
		mockdata.add(new Object[]{"8645","test1"});
		when(bugDao.findBySql("select distinct name from tbug")).thenReturn(mockdata);//开始mock
		
		
		System.out.println(bugDao.toString()+"   ---");
		System.out.println("ssssssssssssssssssssssssssssss");
		Map<String,List<Object>> c=new HashMap<String,List<Object>>();
	/*	List<String> aa=bugDao.findBySql();
		System.out.println(aa.toArray());
		System.out.println(aa.size());*/
		List<Object> users=new ArrayList<Object>();
		List<Object> nums=new ArrayList<Object>();
		
		List<Object[]> aaa1=bugDao.findBySql("select distinct name from tbug");
		System.out.println(aaa1.size());
		
		
		if(MockUtil.MockStart){
			System.out.println("mock");
			
			MockUtil.mock(bugDao);
		}else{
			MockUtil.set();
			System.out.println("nomock");
		}
		
		
		List<Object[]> aaa=bugDao.findBySql("select distinct name from tbug");
		System.out.println(aaa.size());
		
		for(int i=0;i<aaa.size();i++){
			users.add(String.valueOf(aaa.get(i)));
			//nums.add(Integer.valueOf(bugDao.getData(String.valueOf(aaa.get(i)))));		
		}
		
       c.put("users", users);
       c.put("nums", nums);
		
		
		return c;
	}

}
