package sy.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sy.pageModel.Json;
import sy.pageModel.Resource;
import sy.pageModel.SessionInfo;
import sy.pageModel.Tree;
import sy.service.BugServiceI;
import sy.service.ResourceServiceI;
import sy.service.ResourceTypeServiceI;
import sy.util.ConfigUtil;

/**
 * 资源控制器
 * 
 * @author 孙宇
 * 
 */
@Controller
@RequestMapping("/resourceController")
public class ResourceController extends BaseController {

	@Autowired
	private ResourceServiceI resourceService;

	@Autowired
	private ResourceTypeServiceI resourceTypeService;
	
	@Autowired
	private BugServiceI bugService;

	/**
	 * 获得资源树(资源类型为菜单类型)
	 * 
	 * 通过用户ID判断，他能看到的资源
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("/tree")
	@ResponseBody
	public List<Tree> tree(HttpSession session,HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());

		return resourceService.tree(sessionInfo,request.getParameter("type"));
	}
	
	@RequestMapping("/tree2")
	@ResponseBody
	public List<Tree> tree2(HttpSession session,HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());

		return resourceService.tree2(sessionInfo,request.getParameter("type"));
	}

	/**
	 * 获得资源树(包括所有资源类型)
	 * 
	 * 通过用户ID判断，他能看到的资源
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("/allTree")
	@ResponseBody
	public List<Tree> allTree(HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		return resourceService.allTree(sessionInfo);
	}

	/**
	 * 跳转到资源管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/manager")
	public String manager() {
		return "/admin/resource";
	}

	/**
	 * 跳转到资源添加页面
	 * 
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request) {
		request.setAttribute("resourceTypeList", resourceTypeService.getResourceTypeList());
		Resource r = new Resource();
		r.setId(UUID.randomUUID().toString());
		request.setAttribute("resource", r);
		return "/admin/resourceAdd";
	}

	/**
	 * 添加资源
	 * 
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Json add(Resource resource, HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		Json j = new Json();
		if(resourceService.isExisted(resource.getName())){
		resourceService.add(resource, sessionInfo);
		j.setSuccess(true);
		j.setMsg("添加成功！");
		j.setId(resource.getId());
		j.setName(resource.getName());
		bugService.add("增加资源-- "+resource.getName(), sessionInfo);
		}else{
			j.setSuccess(false);
			j.setMsg("已存在相同的名字: "+resource.getName());	
		}
		return j;
	}

	/**
	 * 跳转到资源编辑页面
	 * 
	 * @return
	 */
	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request, String id) {
		request.setAttribute("resourceTypeList", resourceTypeService.getResourceTypeList());
		Resource r = resourceService.get(id);
		request.setAttribute("resource", r);
		return "/admin/resourceEdit";
	}

	/**
	 * 编辑资源
	 * 
	 * @param resource
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(Resource resource, HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		Json j = new Json();
		Resource r = resourceService.get(resource.getId());
		if(!r.getName().equals(resource.getName())){
			if(resourceService.isExisted(resource.getName())){
				resourceService.edit(resource);
				bugService.add("修改资源-- "+resource.getName(), sessionInfo);
				j.setSuccess(true);
				j.setMsg("编辑成功！");
			}else{
				j.setSuccess(false);
				j.setMsg("已存在相同的名字: "+resource.getName());	
			}
		}else{			
			resourceService.edit(resource);
			bugService.add("修改资源-- "+resource.getName(), sessionInfo);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
		}
		
		return j;
	}

	/**
	 * 获得资源列表
	 * 
	 * 通过用户ID判断，他能看到的资源
	 * 
	 * @return
	 */
	@RequestMapping("/treeGrid")
	@ResponseBody
	public List<Resource> treeGrid(HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		return resourceService.treeGrid(sessionInfo);
	}

	/**
	 * 删除资源
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String id,HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		Json j = new Json();
		resourceService.delete(id);
		bugService.add("删除资源-- "+id, sessionInfo);
		j.setMsg("删除成功！");
		j.setSuccess(true);
		return j;
	}

}
