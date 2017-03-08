package sy.dao.impl;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import sy.dao.UserDaoI;
import sy.model.Tuser;

@Repository
public class UserDaoImpl extends BaseDaoImpl<Tuser> implements UserDaoI {

	@Override
	public void e(String a) {
		System.out.println("d0000000000000000000000000000000000000000");
		SQLQuery q = this.getCurrentSession().createSQLQuery(a);
		
	}

}
