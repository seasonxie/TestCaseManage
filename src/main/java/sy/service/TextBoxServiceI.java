package sy.service;

import sy.pageModel.SessionInfo;
import sy.pageModel.TextBox;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.pageModel.TextBox;

/**
 * 
 * @author 孙宇
 * 
 */
public interface TextBoxServiceI {

	/**
	 * 获取TextBox数据表格
	 * 
	 * @param TextBox
	 *            参数
	 * @param ph
	 *            分页帮助类
	 * @return
	 */
	public DataGrid dataGrid(TextBox TextBox, PageHelper ph);

	/**
	 * 添加TextBox
	 * 
	 * @param TextBox
	 */
	public void add(TextBox TextBox, SessionInfo sessionInfo);

	/**
	 * 获得TextBox对象
	 * 
	 * @param id
	 * @return
	 */
	public TextBox get(String id);

	/**
	 * 修改TextBox
	 * 
	 * @param TextBox
	 */
	public void edit(TextBox TextBox, SessionInfo sessionInfo);

	/**
	 * 删除TextBox
	 * 
	 * @param id
	 */
	public void delete(String id);



}
