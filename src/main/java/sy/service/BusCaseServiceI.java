package sy.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import sy.model.Tbuscase;
import sy.pageModel.BusCase;
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
public interface BusCaseServiceI {

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
	public List<Tree> allTree(SessionInfo sessionInfo,String project, PageHelper ph);

	/**
	 * 获得资源列表
	 * 
	 * @param sessionInfo
	 * 
	 * @return
	 */
	public List<BusCase> collecttreeGrid(SessionInfo sessionInfo,String data);
	public Map<String,Object> treeGrid(SessionInfo sessionInfo,String data,BusCase bug, PageHelper ph);
	public Long getTotal(String project,BusCase BusCase, PageHelper ph);
	public List<Map<String,String>> project();
	public List<Map<String,String>> projectchild(String project);
	public void setOrder(String project);
	/**
	 * 添加资源
	 * 
	 * @param ComCase
	 * @param sessionInfo
	 */
	public void add(BusCase BusCase, SessionInfo sessionInfo);
	
	public void addorUpdate(BusCase BusCase, SessionInfo sessionInfo);
	
	public void copy(String id, String pid,SessionInfo sessionInfo);
	
	public void insert(String id, String pid,SessionInfo sessionInfo);
	
	public void copyInsert(String id, String targetId,SessionInfo sessionInfo);

	/**
	 * 删除资源
	 * 
	 * @param id
	 */
	public String delete(String id);

	/**
	 * 修改资源
	 * 
	 * @param ComCaseComCase
	 */
	public void edit(BusCase BusCase, SessionInfo sessionInfo);

	public void edit(String oid,String pid, SessionInfo sessionInfo);
	/**
	 * 获得一个资源
	 * 
	 * @param id
	 * @return
	 */
	public BusCase get(String id);
	public Tbuscase getT(String id);
}
