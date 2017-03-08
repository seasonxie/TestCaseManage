package sy.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;







import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;






import sy.model.Tbuscase;
import sy.pageModel.ExCase;
import sy.pageModel.DataGrid;
import sy.pageModel.Json;
import sy.pageModel.PageHelper;
import sy.pageModel.BusCase;
import sy.pageModel.Resource;
import sy.pageModel.SessionInfo;
import sy.pageModel.Tree;
import sy.service.BusCaseServiceI;
import sy.service.ExCaseServiceI;
import sy.service.ResourceServiceI;
import sy.service.RoleServiceI;
import sy.service.UserServiceI;
import sy.util.ConfigUtil;
import sy.util.IpUtil;



/**
 * 用户控制器
 * 
 * @author 孙宇
 * 
 */
@Controller
@RequestMapping("/excaseController")
public class ExcaseController extends BaseController {

	@Autowired
	private ExCaseServiceI excaseService;

	@Autowired
	private ResourceServiceI resourceService;
	
	@Autowired
	private BusCaseServiceI buscaseService;

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
		return excaseService.tree(sessionInfo);
	}
	
	@RequestMapping("/project")
	@ResponseBody
	public List<Map<String,String>> project(HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		return excaseService.project();
	}
	
	@RequestMapping("/projectchild")
	@ResponseBody
	public List<Map<String,String>> projectchild(HttpSession session,HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
	
		return excaseService.projectchild(request.getParameter("child"));
	}
	
	
	@RequestMapping("/redminePage")
	public String redminePage(HttpSession session,HttpServletRequest request) {
		//SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		request.setAttribute("project", request.getParameter("project"));
		request.setAttribute("id", request.getParameter("id"));
		request.setAttribute("cproject", request.getParameter("cproject"));
		return "/case/exRedmine";
	}
	
	/*@RequestMapping("/redmineDo")
	@ResponseBody
	public String redmineDo(HttpSession session,HttpServletRequest request) {
		String version=request.getParameter("fversion");
		String subject=request.getParameter("subject");
		String comment=request.getParameter("comment");
		String caseid=request.getParameter("caseid");
		String cproject=request.getParameter("cproject");
		String commentt=comment.replace("\n", "<br>");
		System.out.println(commentt);
		String result=redmineHandle(version,subject,commentt);
		System.out.println(result);
		System.out.println(caseid);
		if(!result.contains("Failed")){
		
			excaseService.edit(caseid,"#"+result,cproject);
		}
	
		return result;
	}*/
	
	/*public String redmineHandle(String verion,String subject,String comment){
		String result="";
		try{
		RedmineManager mgr = new RedmineManager("http://redminetest.meizu.com/", "zhaotang","Qq0245663");
		Issue issue = new Issue();
		Tracker tracker = new Tracker();
		User user = new User();
		List<CustomField> customFields = new ArrayList<CustomField>();
		CustomField customField = new CustomField();
		customField.setId(41);
		customField.setName("Flyme版本");
		customField.setValue(verion);
		customFields.add(customField);
		user.setId(1025);
		user.setLogin("zhaotang");
		tracker.setId(4);
		tracker.setName("Flyme Buy管理");
		issue.setAuthor(user);
		User user1 = new User();
		user1.setId(2355);
		user1.setLogin("kongli");
		issue.setAssignee(user1);
		issue.setStatusId(2);
		issue.setPriorityId(3);
		issue.setSubject(subject);
		
		issue.setDescription(comment);
		
		
		issue.setCustomFields(customFields);
		
		Issue issue1=mgr.createIssue("944", issue);
		result=String.valueOf(issue1.getId());
		}catch(Exception e){
			result="Failed: "+e.getMessage();
		}
		
		return result;
		
	}
*/
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
	public List<Tree> allTree(HttpSession session,HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		
		return excaseService.allTree(sessionInfo, request.getParameter("project"));
	}
	
	@RequestMapping("/total")
	@ResponseBody
	public Long getTotal(HttpSession session,HttpServletRequest request,ExCase bug, PageHelper ph) {
		return excaseService.getTotal(request.getParameter("data"), bug,  ph);
		
	}

	/**
	 * 跳转到资源管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/manager")
	public String manager(HttpServletRequest request) {
		request.setAttribute("excasenode", request.getParameter("node"));
		return "/case/excase";
	}
	
	@RequestMapping("/uploadPage")
	public String uploadPage(HttpServletRequest request) {
		return "/case/excaseUpload";
	}
	

	
	@RequestMapping(value="/generateReport",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String,String>> generateReport(HttpSession session,HttpServletRequest request, PageHelper ph) throws IOException {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		List<Map<String,String>> rt=new ArrayList<Map<String,String>>();
	String parentproject=request.getParameter("parentproject");
	String treeTemplate=request.getParameter("treetemplate");
	String caseproject=request.getParameter("caseproject");
	String failed=request.getParameter("failed");
	String failedv=request.getParameter("failedv");
	String casesnum=request.getParameter("casesnum");
	String undo=request.getParameter("undo");
	String undov=request.getParameter("undov");
	String faileddetail=request.getParameter("faileddetail");
	
	Map<String,Integer> onum=new HashMap<String,Integer>();
	List<String> dnum=new ArrayList<String>();

	List<String> todo1=new ArrayList<String>();
	List<String> todo2=new ArrayList<String>();
	if(!faileddetail.isEmpty()){
	String[] faileddate=faileddetail.split("\\*");
	
	for(String d:faileddate){
		onum.put(d.substring(0, 10).replace("-", ""), 0);
		dnum.add(d.substring(0, 10).replace("-", ""));
	}
	
	for(String d:dnum){
		if(onum.containsKey(d)){
			int tt=onum.get(d);
			tt++;
			onum.put(d, tt);
		}
	}
	
	List<Entry<String, Integer>> mappingList = null; 
	  //通过ArrayList构造函数把map.entrySet()转换成list 
	  mappingList = new ArrayList<Map.Entry<String,Integer>>(onum.entrySet()); 
	  //通过比较器实现比较排序 
	  Collections.sort(mappingList, new Comparator<Map.Entry<String,Integer>>(){ 
	   public int compare(Map.Entry<String,Integer> mapping1,Map.Entry<String,Integer> mapping2){ 
	    return mapping1.getKey().compareTo(mapping2.getKey()); 
	   } 
	  }); 
	  
	  for(Map.Entry<String,Integer> mapping:mappingList){ 
		  todo1.add(mapping.getKey());
		     todo2.add(String.valueOf(String.valueOf(mapping.getValue())));
		  } 
	}
	/*
	Iterator it=onum.keySet().iterator();    
	while(it.hasNext()){    
	     String key;    
	     Object value;    
	     key=it.next().toString();    
	     value= onum.get(key);  
	     todo1.add(key);
	     todo2.add(String.valueOf(value));
	}  
	}*/
	
	VelocityEngine ve = new VelocityEngine();
	 Properties prop = new Properties();
       prop.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
       prop.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
       prop.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");  
       

//      prop.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, "/");
      // prop.put("file.resource.loader.path", "/WEB-INF");

       ve.init(prop);
		
		VelocityContext context = new VelocityContext();

		context.put("date", (new Date()).toString());
		context.put("parentproject", parentproject);
		context.put("caseproject", caseproject);
		context.put("failed", failed);
		context.put("failedv", failedv);
		context.put("casesnum", casesnum);
		context.put("undo", undo);
		context.put("undov", undov);
		
		context.put("alist", todo1);
		context.put("blist", todo2);


		List<ExCase> aa=excaseService.treeGridc(caseproject,ph);		
		if(aa.size()==0){			
			aa=excaseService.treeGrid(sessionInfo, caseproject, ph, null);
		}
		
		File savefile;
		
		Template t;
		if (treeTemplate != null) {
			if (treeTemplate.equals("2")) {
				t = Velocity.getTemplate("./hellovelocity1.vm", "UTF-8");
				// savefile = new File("templates/hellovelocity1.vm");
			} else {
				t = Velocity.getTemplate("./hellovelocity.vm", "UTF-8");
				// savefile = new File("hellovelocity.vm");
			}
		} else {
			t = Velocity.getTemplate("./hellovelocity.vm", "UTF-8");
			// savefile = new File("hellovelocity.vm");
		}
      
	   
		context.put("salerList", aa);

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sdf.format(date);

		//输出流
		//System.out.println(System.getProperty(""));
		
		File savefile1 = new File("src/main/webapp/report/report"+str+".html");   
		//File savefile1 = new File("/opt/lampp/tomcat/webapps/sypro/report/report"+str+".html");   
		FileOutputStream outstream = new FileOutputStream(savefile1);   

		OutputStreamWriter writer = new OutputStreamWriter(outstream,"UTF-8");   
		  
		BufferedWriter bufferWriter = new BufferedWriter(writer);   
		 
		//t.merge(context, bufferWriter);   
		
		 t.merge(context, bufferWriter);
	
		//Velocity.mergeTemplate(savefile.toString(), "UTF-8", context, bufferWriter);
		  
		 bufferWriter.flush();   
		  
		outstream.close();   
		  
		 bufferWriter.close(); 
		 Map<String,String> aaa=new HashMap<String,String>();
		 aaa.put("url", "/report/report"+str+"");
		 aaa.put("uri", "你的静态地址:  http://172.16.152.109:8077/sypro/report/report"+str+".html");
		 
		 rt.add(aaa);
		 
		/* 
		 Map<String,String> aaaa=new HashMap<String,String>();
		 aaaa.put("sdd", "rrrr");
		 rt.add(aaaa);*/
		
		return rt;
	}
	
	
	@RequestMapping("/reportPage")
	public String reportPage(HttpServletRequest request) {
		request.setAttribute("caseproject", request.getParameter("project"));
		request.setAttribute("parentproject", request.getParameter("parentproject"));
		
		request.setAttribute("casesnum", request.getParameter("casesnum"));
		request.setAttribute("failed", request.getParameter("failed"));
		request.setAttribute("failedv", request.getParameter("failedv")+"%");
		request.setAttribute("undo", request.getParameter("undo"));
		request.setAttribute("undov", request.getParameter("undov")+"%");
		request.setAttribute("faileddetail", request.getParameter("faileddetail"));
		return "/case/exGenerateReport";
	}
	
	
	/**
	 * 跳转到资源添加页面
	 * 
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request) {
		//request.setAttribute("ComCaseTypeList", ComCaseTypeService.getComCaseTypeList());

	
		request.setAttribute("caseproject", request.getParameter("project"));
		return "/case/excaseAdd";
	}

	/**
	 * 添加资源
	 * 
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public List<String> add(HttpSession session,HttpServletRequest request) {
		boolean result=true;
		String message="";
		int num=0;
		String pid = "";
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		Json j = new Json();
		String treeId=UUID.randomUUID().toString();
		try{
		if(resourceService.isExisted(request.getParameter("name"))){	
			//List<String> exid=excaseService.isExisted();
			String json=request.getParameter("rowstr");
			String[] datas=json.split("\\*");
		
			Resource r=new Resource();
			
			r.setId(treeId);
			r.setName(request.getParameter("name"));
			pid=resourceService.getId(request.getParameter("oname"), request.getParameter("oid"));
			if(pid!=null)
			r.setPid(pid);
		    r.setTemplate(request.getParameter("template"));
			r.setSeq(100);
			r.setTypeId("2");
			r.setUrl("/excaseController/manager");	
			resourceService.addEx(r, sessionInfo);
			
			for(String data:datas){
				BusCase b=buscaseService.get(data);
				ExCase c=new ExCase();
				BeanUtils.copyProperties(b, c);
				//if(!exid.contains(b.getId())){
			
				c.setId(UUID.randomUUID().toString());
				c.setOrgid(b.getId());
				c.setExpire(b.getExpire());
				c.setTreeid(treeId);
				/*c.setModule(b.getModule());
				c.setModulechild(b.getModulechild());
				c.setName(b.getName());
				c.setOp(b.getOp());
				c.setPid(b.getPid());
				c.setPre(b.getPre());*/
				c.setProject(request.getParameter("name"));
			/*	c.setRemark(b.getRemark());
				c.setSeq(b.getSeq());*/
				c.setStep(b.getStep());
				if(b.getStep()!=null){
				if(b.getStep().equals("")||b.getStep().isEmpty()){
					c.setIscase("0");
				}else{
					c.setIscase("1");
				}
				}else{
					c.setIscase("0");
				}
				excaseService.add(c, sessionInfo);
				num++;
			}
	
		}else{
			message="已存在执行用例名称:  "+request.getParameter("name");
			result=false;
		}
		}catch (Exception e){
			e.printStackTrace();
			message="已添加用例: "+num+"  继续的时候添加出现系统异常,请联系管理员";
			result=false;			
		}
		
	
		if(result){
			message=("成功添加用例数:  "+num);
		}
		
		List<String> m=new ArrayList<String>();
		m.add(message);
		m.add(treeId);
		m.add(request.getParameter("name"));
		m.add(pid);
		return m;
	}
	
	
	
	@RequestMapping("/addEx")
	@ResponseBody
	public String addEx(HttpSession session,HttpServletRequest request, PageHelper ph) {
		boolean result=true;
		String message="";
		int num=0;
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		try{
			String json=request.getParameter("rowstr");
	         String[] datas=json.split("\\*");
			
			System.out.println(datas.length);
			for(String data:datas){
				BusCase b=buscaseService.get(data);
				ExCase c=new ExCase();
				BeanUtils.copyProperties(b, c);
				c.setId(UUID.randomUUID().toString());
				c.setOrgid(b.getId());
				c.setExpire(b.getExpire());
				c.setTreeid(ph.getTreeid());
				/*c.setModule(b.getModule());
				c.setModulechild(b.getModulechild());
				c.setName(b.getName());
				c.setOp(b.getOp());
				c.setPid(b.getPid());
				c.setPre(b.getPre());*/
				c.setProject(request.getParameter("name"));
				/*c.setRemark(b.getRemark());
				c.setSeq(b.getSeq());*/
				c.setStep(b.getStep());
				if(b.getStep().equals("")||b.getStep().isEmpty()){
					c.setIscase("0");
				}else{
					c.setIscase("1");
				}
				excaseService.add(c, sessionInfo);
				num++;
		
		}
		}catch (Exception e){
			System.out.println(e.toString());
			message="已添加用例: "+num+"  继续的时候添加出现系统异常,请联系管理员";
			result=false;
		}
		
	
		if(result){
			message=("成功添加用例数:  "+num);
		}
		
		return message;
	}

	/**
	 * 跳转到资源编辑页面
	 * 
	 * @return
	 */
	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request) {
	//	request.setAttribute("ComCaseTypeList", ComCaseTypeService.getComCaseTypeList());
		ExCase r = excaseService.get(request.getParameter("id"),request.getParameter("name"));
		request.setAttribute("excase", r);
		return "/case/excaseEdit";
	}

	/**
	 * 编辑资源
	 * 
	 * @param ComCase
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(HttpSession session,ExCase excase,HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		String id=request.getParameter("idd");
		String status=request.getParameter("statuss");
		String namee=request.getParameter("namee");
		System.out.println(excase.getId());
		if(excase.getId()==null){
			excaseService.edit(id,status,sessionInfo,namee);
		}else{
		
		excaseService.edit(excase);
		}
		
		Json j = new Json();

		j.setSuccess(true);
		j.setMsg("编辑成功！");
		return j;
	}
	
	
	@RequestMapping("/doPOnce")
	@ResponseBody
	public Json doPOnce(HttpSession session,ExCase excase,HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		String project=request.getParameter("project");		
		String id=request.getParameter("ps");
		String[] ids=id.split("\\*");
		System.out.println(ids.length);
		for(String i:ids){
			excaseService.edit(i,"P",sessionInfo,project);			
		}	
	
		Json j = new Json();

		j.setSuccess(true);
		j.setMsg("一键P完成！");
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
	public List<ExCase> treeGrid(HttpSession session,HttpServletRequest request, PageHelper ph,ExCase c) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());

		return excaseService.treeGrid(sessionInfo,request.getParameter("data"),ph,c);
	}
	
	
	
	@RequestMapping("/collecttreeGrid")
	@ResponseBody
	public List<ExCase> collecttreeGrid(HttpSession session,HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());

		return excaseService.collecttreeGrid(sessionInfo,request.getParameter("pid"));
	}
	/**
	 * 删除资源
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(HttpServletRequest request) {
		Json j = new Json();
		String id=request.getParameter("ps");
		String[] ids=id.split("\\*");
		System.out.println(ids.length);
		for(String i:ids){
			excaseService.delete(i,request.getParameter("project"));
		}	
		
		j.setMsg("删除成功！");
		j.setSuccess(true);
		return j;
	}
	

}
