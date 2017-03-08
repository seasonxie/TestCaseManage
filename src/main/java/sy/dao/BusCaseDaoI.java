package sy.dao;

import java.util.List;

import sy.model.Tbuscase;
import sy.model.Tcomcase;
import sy.model.Tuser;

/**
 * 用户数据库操作类
 * 
 * @author 孙宇
 * 
 */
public interface BusCaseDaoI extends BaseDaoI<Tbuscase> {
	
	public List<String> findString(String hql);

}
