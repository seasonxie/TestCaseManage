package sy.service;

import java.util.List;
import java.util.Map;

import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.pageModel.ComCase;
import sy.pageModel.ComCase;
import sy.pageModel.SessionInfo;
import sy.pageModel.Tree;
import sy.pageModel.User;

/**
 * 用户Service
 * 
 * @author 孙宇
 * 
 */
public interface CaseServiceI {

	/**
	 * 获得资源树(资源类型为菜单类型)
	 * 
	 * 通过用户ID判断，他能看到的资源
	 * 
	 * @param sessionInfo
	 * @return
	 */
	public List<Tree> tree(SessionInfo sessionInfo);

	/**
	 * 获得资源树(包括所有资源类型)
	 * 
	 * 通过用户ID判断，他能看到的资源
	 * 
	 * @param sessionInfo
	 * @return
	 */
	public List<Tree> allTree(SessionInfo sessionInfo);

	/**
	 * 获得资源列表
	 * 
	 * @param sessionInfo
	 * 
	 * @return
	 */
	public List<ComCase> treeGrid(SessionInfo sessionInfo,String data);
	public List<ComCase> ctreeGrid(SessionInfo sessionInfo,String data);
	
	public List<Map<String,String>> project();
	public List<Map<String,String>> projectchild(String project);

	/**
	 * 添加资源
	 * 
	 * @param ComCase
	 * @param sessionInfo
	 */
	public void add(ComCase ComCase, SessionInfo sessionInfo);

	/**
	 * 删除资源
	 * 
	 * @param id
	 */
	public void delete(String id);

	/**
	 * 修改资源
	 * 
	 * @param ComCase
	 */
	public void edit(ComCase ComCase);
	public void bedit(ComCase ComCase,String id);

	/**
	 * 获得一个资源
	 * 
	 * @param id
	 * @return
	 */
	public ComCase get(String id);

	List<String> getComProject();
}
