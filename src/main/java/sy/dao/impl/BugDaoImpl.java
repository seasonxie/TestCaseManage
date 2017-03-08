package sy.dao.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import sy.dao.BaseDaoI;
import sy.dao.BugDaoI;
import sy.model.Tbug;
import sy.model.Tbuscase;

@Repository
public class BugDaoImpl extends BaseDaoImpl<Tbug> implements BugDaoI {
	
	public String getData(String name) {
		String hql = "select COUNT(*) from tbug where name='"+name+"'";
	    SQLQuery query = this.getCurrentSession().createSQLQuery(hql);    
	    String str = query.uniqueResult().toString();
		return str;    
	}
	
	
	public List<String> findBySql() {	
		String queryString = "select distinct name from Tbug";
		return  ((BaseDaoI<String>) getCurrentSession()).find(queryString);
}
}
