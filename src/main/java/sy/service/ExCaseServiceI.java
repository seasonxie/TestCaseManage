package sy.service;

import java.util.List;
import java.util.Map;

import sy.pageModel.ExCase;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.pageModel.ComCase;
import sy.pageModel.BusCase;
import sy.pageModel.SessionInfo;
import sy.pageModel.Tree;
import sy.pageModel.User;

/**
 * 用户Service
 * 
 * @author 孙宇
 * 
 */
public interface ExCaseServiceI {

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
	public List<Tree> allTree(SessionInfo sessionInfo,String project);
	public Long getTotal(String project,ExCase BusCase, PageHelper ph);
	/**
	 * 获得资源列表
	 * 
	 * @param sessionInfo
	 * 
	 * @return
	 */
	public List<ExCase> collecttreeGrid(SessionInfo sessionInfo,String data);
	public List<ExCase> treeGrid(SessionInfo sessionInfo,String data, PageHelper ph,ExCase c);
	public List<ExCase> treeGridc(String data, PageHelper ph);
	public List<Map<String,String>> project();
	public List<Map<String,String>> projectchild(String project);

	/**
	 * 添加资源
	 * 
	 * @param ComCase
	 * @param sessionInfo
	 */
	public void add(ExCase ExCase, SessionInfo sessionInfo);

	/**
	 * 删除资源
	 * 
	 * @param id
	 */
	public void delete(String id,String name);
	public List<String> isExisted();

	/**
	 * 修改资源
	 * 
	 * @param ComCaseComCase
	 */
	public void edit(ExCase ExCase);
	
	public void edit(String id,String remark,String cproject);

	public void edit(String id,String status,SessionInfo sessionInfo,String namee);
	/**
	 * 获得一个资源
	 * 
	 * @param id
	 * @return
	 */
	public ExCase get(String id,String name);
}
