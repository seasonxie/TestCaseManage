package sy.dao;

import java.util.List;

import sy.model.Tresource;

/**
 * 资源数据库操作类
 * 
 * @author 孙宇
 * 
 */
public interface ResourceDaoI extends BaseDaoI<Tresource> {
	public List<String> findString(String hql);
}
