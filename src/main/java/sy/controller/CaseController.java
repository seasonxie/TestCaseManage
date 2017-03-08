package sy.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import sy.pageModel.ComCase;
import sy.pageModel.DataGrid;
import sy.pageModel.Json;
import sy.pageModel.PageHelper;
import sy.pageModel.ComCase;
import sy.pageModel.SessionInfo;
import sy.pageModel.Tree;
import sy.pageModel.User;
import sy.service.BugServiceI;
import sy.service.CaseServiceI;
import sy.service.ResourceServiceI;
import sy.service.RoleServiceI;
import sy.service.UserServiceI;
import sy.util.ConfigUtil;
import sy.util.IpUtil;

import com.alibaba.fastjson.JSON;

/**
 * 用户控制器
 * 
 * @author 孙宇
 * 
 */
@Controller
@RequestMapping("/caseController")
public class CaseController extends BaseController {

	@Autowired
	private CaseServiceI caseService;

	@Autowired
	private ResourceServiceI resourceService;
	
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
	public List<Tree> tree(HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		return caseService.tree(sessionInfo);
	}
	
	@RequestMapping("/project")
	@ResponseBody
	public List<Map<String,String>> project(HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		return caseService.project();
	}
	
	@RequestMapping("/projectchild")
	@ResponseBody
	public List<Map<String,String>> projectchild(HttpSession session,HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
	
		return caseService.projectchild(request.getParameter("child"));
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
		return caseService.allTree(sessionInfo);
	}

	/**
	 * 跳转到资源管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/manager")
	public String manager(HttpServletRequest request) {
		request.setAttribute("casenode", request.getParameter("node"));
		return "/case/comcase";
	}
	
	@RequestMapping("/uploadPage")
	public String uploadPage(HttpServletRequest request) {
		return "/case/comcaseUpload";
	}
	
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	@ResponseBody
	public String uploadhandle(@RequestParam("uploadExcel") CommonsMultipartFile uploadExcel,  
            HttpServletRequest request, HttpServletResponse response, HttpSession session)  
            throws IOException, BiffException{
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		String result = "";
		String error = "";
		String celldata="";
		try{
		Workbook rwb = null;
		Cell cell = null;	
		InputStream in = uploadExcel.getInputStream();  
		rwb = Workbook.getWorkbook(in);	
		Sheet sheet = rwb.getSheet(0);	
		int rows=sheet.getRows();
		String str = null;
		
		//List<String> error=new ArrayList<String>();
		//List<ComCase> aa=new ArrayList<ComCase>();
		ComCase datacase = null;
		int row=0;
		int order=100;
		
		if(sheet.getCell(1,0).getContents().equals("项目用例")){
		for(int i=1;i<=rows;i++){
			
			if(i==1){
				if(resourceService.isExisted(sheet.getCell(1,1).getContents())){
					
					error="<br>false：不存在项目用例名称-- "+sheet.getCell(1,1).getContents();
					break;
				}
			}
			
			if(datacase!=null){
				
				caseService.add(datacase, sessionInfo);
				order++;
				row++;
			
				//aa.add(datacase);
				
			}
			if(rows-i==0){
				
				break;
			}
			try{
			datacase=new ComCase();
			for(int j=1;j<=8;j++){	
			
				cell = sheet.getCell(j,i); //ArrayIndexOutOfBoundsException
				str = cell.getContents().trim();
				
				String resultc=addcomcase(j,str,datacase,order,celldata);
				if(resultc.contains("false")){
					if(sheet.getCell(1,i).getContents().isEmpty()){
						datacase=null;
						break;
					}else{
						error+="<br>  Excel序号 - 第"+(i+1)+"条记录导入失败,"+resultc;
						datacase=null;
						break;
					}
				
				}
				
			}
		
		}catch(Exception e){
			datacase=null;
			e.printStackTrace();
		}
			
		}
		result="成功 导入"+row+"条数据";
		}else{
			result="导入失败：请用模板导入用例，第二列标题非【项目用例】";
		}
		}catch(Exception e){
			result="系统异常，请联系管理员  "+e;
			e.printStackTrace();
		}
	     
		
		if(error.length()!=0){
			result=result+"<br><br>下面为用例导入失败信息:"+error;
		}

		return result;
	}
     
	public String addcomcase(int j,String data,ComCase datacase,int order,String celldata){
		if(j==1){
			
			if(data.isEmpty()){
				return "false:控件类型不能为空";
			}
			if(data.length()>50){
				return "false:控件类型最大值为50";
			}
			datacase.setId(UUID.randomUUID().toString());
			datacase.setSeq(order);
			datacase.setProject(data);	
			
		}else if(j==2){
			if(data.length()>36){
				return "false:上级ID最大值为36";
			}
			datacase.setPid(data);
		}else if(j==3){
			if(data.length()>50){
				return "false:测试项目最大值为50";
			}
			if(data.isEmpty()){
				return "false:测试项目不能为空";
			}
			datacase.setName(data);
		}else if(j==4){
			if(data.length()>5){
				return "false:优先级最大值为5";
			}
			datacase.setOp(data);
		}else if(j==5){
			if(data.length()>200){
				return "false:前置条件最大值为200";
			}
			datacase.setPre(data);
		}else if(j==6){
			if(data.isEmpty()){
				return "false:操作步骤不能为空";
			}
			if(data.length()>400){
				return "false:操作步骤最大值为400";
			}
			datacase.setStep(data.replace("\n", "<br>"));;
		}else if(j==7){
			if(data.length()>400){
				return "false:预期结果最大值为400";
			}
			datacase.setExpire(data.replace("\n", "<br>"));
		}else if(j==8){	
			if(data.length()>200){
				return "false:备注最大值为200";
			}
			datacase.setRemark(data);
			
		}
		return "true";
		
	}
	/**
	 * 跳转到资源添加页面
	 * 
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request) {
		//request.setAttribute("ComCaseTypeList", ComCaseTypeService.getComCaseTypeList());
		ComCase r = new ComCase();
		r.setId(UUID.randomUUID().toString());
		r.setProject(request.getParameter("project"));
		request.setAttribute("case", r);
		return "/case/comcaseAdd";
	}

	/**
	 * 添加资源
	 * 
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Json add(ComCase ComCase, HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		Json j = new Json();
		System.out.println(ComCase.getPid());
		caseService.add(ComCase, sessionInfo);
		bugService.add("新增公共用例-- "+ComCase.getProject(), sessionInfo);
		j.setSuccess(true);
		j.setMsg("添加成功！");
		return j;
	}

	/**
	 * 跳转到资源编辑页面
	 * 
	 * @return
	 */
	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request, String id, HttpSession session) {
		
	//	request.setAttribute("ComCaseTypeList", ComCaseTypeService.getComCaseTypeList());
		ComCase r = caseService.get(id);
		request.setAttribute("case", r);
		return "/case/comcaseEdit";
	}
	
	
	@RequestMapping("/bupdatePage")
	public String bupdatePage(HttpServletRequest request,@RequestParam("id") List<String> ids) {
		request.setAttribute("case", request.getParameter("name"));
		request.setAttribute("caseid", ids);
		return "/case/comcaseBupdate";
	}

	/**
	 * 编辑资源
	 * 
	 * @param ComCase
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(ComCase ComCase, HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		Json j = new Json();
		caseService.edit(ComCase);
		bugService.add("修改公共用例-- "+ComCase.getProject(), sessionInfo);
		j.setSuccess(true);
		j.setMsg("编辑成功！");
		return j;
	}
	
	
	
	@RequestMapping("/bedit")
	@ResponseBody
	public Json bedit(ComCase ComCase,HttpServletRequest request) {
		String id=request.getParameter("id");
		System.out.println(id);
		String test=id.substring(1, id.length()-1);
		String aa[]=test.split(",");
		for(String a:aa){
		caseService.bedit(ComCase,a.trim());
		}
		Json j = new Json();
		
		j.setSuccess(true);
		j.setMsg("编辑成功！");
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
	public List<ComCase> treeGrid(HttpSession session,HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		/*Map<String, Object> aa=new HashMap<String, Object>();
		List<ComCase> bb=new ArrayList<ComCase>();
		ComCase cc=new ComCase();
		cc.setId("1");
		cc.setName("dd");
		bb.add(cc);
		aa.put("total", "1");
		aa.put("rows", bb);*/
		bugService.add("进入公共用例导航-- "+request.getParameter("data"), sessionInfo);
		return caseService.treeGrid(sessionInfo,request.getParameter("data"));
	}
	
	@RequestMapping("/ctreeGrid")
	@ResponseBody
	public List<ComCase> ctreeGrid(HttpSession session,HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());

		return caseService.ctreeGrid(sessionInfo,request.getParameter("data"));
	}

	/**
	 * 删除资源
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(@RequestParam("id") List<String> ids,HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		Json j = new Json();
		for(String id:ids){
			caseService.delete(id);
			bugService.add("删除公共用例-- "+id, sessionInfo);
		}	
		j.setMsg("删除成功！");
		j.setSuccess(true);
		return j;
	}
	

}
