package sy.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import sy.dao.ResourceDaoI;
import sy.model.Tresource;

@Repository
public class ResourceDaoImpl extends BaseDaoImpl<Tresource> implements ResourceDaoI {
	
	@Override
	public List<String> findString(String hql) {
		Query q = this.getCurrentSession().createQuery(hql);
		return q.list();
	}
	

}
