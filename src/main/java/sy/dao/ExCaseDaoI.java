package sy.dao;

import java.util.List;

import sy.model.Tbuscase;
import sy.model.Tcomcase;
import sy.model.Texcase;
import sy.model.Tuser;

/**
 * 用户数据库操作类
 * 
 * @author 孙宇
 * 
 */
public interface ExCaseDaoI extends BaseDaoI<Texcase> {
	
	public List<String> findString(String hql);

}
