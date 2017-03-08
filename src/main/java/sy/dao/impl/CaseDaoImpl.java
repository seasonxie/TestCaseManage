package sy.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import sy.dao.BaseDaoI;
import sy.dao.CaseDaoI;
import sy.dao.UserDaoI;
import sy.model.Tcomcase;
import sy.model.Tuser;

@Repository
public class CaseDaoImpl extends BaseDaoImpl<Tcomcase> implements CaseDaoI {

	@Override
	public List<String> findString(String hql) {
		Query q = this.getCurrentSession().createQuery(hql);
		return q.list();
	}
	
	public String getSDate() throws Exception {
		String hql = "select MIN(id) id from (select id from t_date order by id desc limit 2) t_date";
	    SQLQuery query = this.getCurrentSession().createSQLQuery(hql);    
	    String str = query.uniqueResult().toString();
		return str;    
	}

	
	public List<Tcomcase> findBySqll(String date) throws Exception {	
			String queryString = "from TTask as model where model.progress !='已完成' and model.tester='"+date+"'";
			return  ((BaseDaoI<Tcomcase>) getCurrentSession()).find(queryString);
	}
}
