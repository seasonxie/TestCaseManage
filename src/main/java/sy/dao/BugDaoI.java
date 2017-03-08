package sy.dao;

import java.util.List;

import sy.model.Tbug;

/**
 * BUG数据库操作类
 * 
 * @author 孙宇
 * 
 */
public interface BugDaoI extends BaseDaoI<Tbug> {
	
	public String getData(String name);
	public List<String> findBySql();

}
