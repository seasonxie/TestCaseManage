package sy.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.sf.json.JSONObject;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import sy.model.Tbuscase;
import sy.pageModel.BusCase;
import sy.pageModel.DataGrid;
import sy.pageModel.Json;
import sy.pageModel.PageHelper;
import sy.pageModel.BusCase;
import sy.pageModel.SessionInfo;
import sy.pageModel.Tree;
import sy.pageModel.User;
import sy.service.BugServiceI;
import sy.service.BusCaseServiceI;
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
@RequestMapping("/buscaseController")
public class BuscaseController extends BaseController {

	@Autowired
	private BusCaseServiceI buscaseService;
	
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
		return buscaseService.tree(sessionInfo);
	}
	
	@RequestMapping("/project")
	@ResponseBody
	public List<Map<String,String>> project(HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		return buscaseService.project();
	}
	
	@RequestMapping("/projectchild")
	@ResponseBody
	public List<Map<String,String>> projectchild(HttpSession session,HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
	
		return buscaseService.projectchild(request.getParameter("child"));
	}

	
	@RequestMapping("/collectcase")
	public String collectcase(HttpSession session,HttpServletRequest request) {
		request.setAttribute("collectpid", request.getParameter("pid"));
		request.setAttribute("collectmodule", request.getParameter("module"));
		request.setAttribute("collectmodulechild", request.getParameter("modulechild"));
		request.setAttribute("collectproject", request.getParameter("project"));
		request.setAttribute("collectprojectid", request.getParameter("treeid"));
		return "/case/collectcase";
	}
	
	
	@RequestMapping("/setOrder")
	public void setOrder(HttpSession session,HttpServletRequest request) {
		buscaseService.setOrder(request.getParameter("project"));		
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
	public List<Tree> allTree(HttpSession session,HttpServletRequest request, PageHelper ph) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		String p=request.getParameter("project");	
		String i=request.getParameter("projectid");	
		String type=request.getParameter("casetype");
		try{
		String a="";
		System.out.println(type);
		if(type!=null){
			if(type.equals("excase")){
		String[] projects=p.split("\\*");
		String[] projectids=i.split("\\*");
		System.out.println(projects.length);
			for(int j=0;j<projects.length;j++){
				System.out.println(projects[j]+projectids[j]);
				a+=resourceService.getId(projects[j], projectids[j])+"*";
			}
			}			
		}
		System.out.println(a);
		if(!a.equals("")){
			p=a;
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(p);
		return buscaseService.allTree(sessionInfo, p,ph);
	}

	/**
	 * 跳转到资源管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/manager")
	public String manager(HttpServletRequest request) {
		request.setAttribute("buscasenode", request.getParameter("node"));
		System.out.println(request.getParameter("node"));
		return "/case/buscase";
	}
	
	@RequestMapping("/uploadPage")
	public String uploadPage(HttpServletRequest request) {
		return "/case/buscaseUpload";
	}
	
	@RequestMapping("/generateCase")
	public String generateCase(HttpServletRequest request) {
		request.setAttribute("caseproject", request.getParameter("project"));
		request.setAttribute("num", request.getParameter("num"));
		return "/case/busGenerate";
	}
	
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	@ResponseBody
	public String uploadhandle(@RequestParam("uploadExcel") CommonsMultipartFile uploadExcel,  
            HttpServletRequest request, HttpServletResponse response, HttpSession session, PageHelper ph)  
            throws IOException, BiffException{
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		String result;
		String error = "";
		String cellless3="";
		String cellless4="";
		int col=11;
	
		try{
		Workbook rwb = null;
		Cell cell = null;	
		InputStream in = uploadExcel.getInputStream();  
		rwb = Workbook.getWorkbook(in);	
		Sheet sheet = rwb.getSheet(0);	
		int rows=sheet.getRows();
		String str = null;

		BusCase datacase = null;
		int row=0;
		int order=1;
		if(sheet.getCell(1,0).getContents().equals("项目用例")){
		String template=sheet.getCell(0,1).getContents();
		if(template.equals("2")){
			col=16;
		}else if(template.equals("3")){
			col=13;
		}
		for(int i=1;i<=rows;i++){
			
			if(i==1){
				if(resourceService.isExisted(sheet.getCell(1,1).getContents())){
					
					error="<br>false：不存在项目用例名称-- "+sheet.getCell(1,1).getContents();
					break;
				}
			}
			if(datacase!=null){		
				if(datacase.getId()==null||datacase.getId().isEmpty()){
				datacase.setId(UUID.randomUUID().toString());
				datacase.setTreeid(ph.getTreeid());
				buscaseService.add(datacase, sessionInfo);
				order++;
				row++;
				}else{
					try{
					datacase.setSeq(buscaseService.getT(datacase.getId()).getSeq());
					
					datacase.setTreeid(ph.getTreeid());
					buscaseService.addorUpdate(datacase, sessionInfo);
					order++;
					row++;
					}catch(Exception e){
						e.printStackTrace();
						error="<br>false：ID 列请不要输系统不存在的值 "+datacase.getId();
					}
				}
						
			}
			if(rows-i==0){
				break;
			}
			try{
			datacase=new BusCase();
		   
			
			
			for(int j=1;j<=col;j++){			
				
				cell = sheet.getCell(j,i);
				
				str = cell.getContents().trim();
				
				if(j==3){				
					if(!str.isEmpty())
					cellless3=str;

				}
				
				if(j==4){				
					if(!str.isEmpty())
					cellless4=str;

				}
			
				String resultc=addbuscase(j,str,datacase,order,cellless3,cellless4,template);
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
		result="成功导入 "+row+" 条数据";
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

	public String addbuscase(int j,String data,BusCase datacase,int order,String cellless3,String cellless4,String template){		
		if(j==1){			
			if(data.isEmpty()){
				return "false:项目用例不能为空";
			}		
			datacase.setSeq(order);
			datacase.setProject(data.trim());	
		}else if(j==2){

			if(data.length()>36){
				return "false:上级ID最大值为36位";
			}
			datacase.setPid(data);
		}else if(j==3){
			
			if(template.equals("2")){
				if(data.length()>30){
					return "false:测试编号最大值为30";
				}
				if(data.isEmpty())
					data=cellless3;
				
				datacase.setCasenum(data);;
			}else if(template.equals("3")){
				if(data.length()>30){
					return "false:模块最大值为30";
				}
				if(data.isEmpty())
					data=cellless3;
				
				datacase.setModule(data);
			}else{
				if(data.length()>30){
					return "false:模块最大值为30";
				}
				if(data.isEmpty())
					data=cellless3;
				
				datacase.setModule(data);
			}
		
		}else if(j==4){
			
			if(template.equals("2")){
				if(data.length()>50){
					return "false:测试项目最大值为50";
				}
				datacase.setCasestep(data);
				
			}else if(template.equals("3")){
				if(data.length()>100){
					return "false:子模块最大值为100";
				}
				if(data.isEmpty())
					data=cellless4;
		
				datacase.setModulechild(data);
			}else{
				if(data.length()>100){
					return "false:子模块最大值为100";
				}
			/*	if(data.isEmpty())
					data=cellless4;*/
		
				datacase.setModulechild(data);
			}
		
		}else if(j==5){
			
			if(template.equals("2")){
				if(data.length()>200){
					return "false:测试描述最大值为200";
				}
				datacase.setName(data);
				
			}else if(template.equals("3")){
				if(data.length()>30){
					return "false:测试编号最大值为30";
				}			
				datacase.setCasenum(data);;
			}else{
				if(data.length()>200){
					return "false:测试项目最大值为200";
				}		
				datacase.setName(data);
			}
			
		}else if(j==6){
			if(template.equals("2")){
				if(data.length()>5){
					return "false:优先级最大值为5";
				}
				datacase.setOp(data);
			
			}else if(template.equals("3")){
				if(data.length()>200){
					return "false:测试名称最大值为200";
				}
				datacase.setName(data);
			}else{
				if(data.length()>5){
					return "false:优先级最大值为5";
				}
				datacase.setOp(data);
			}
			
		}else if(j==7){
			if(template.equals("2")){
				if(data.length()>30){
					return "false:一级页面最大值为30";
				}
				datacase.setFristpage(data);;
				
			}else if(template.equals("3")){
				if(data.length()>500){
					return "false:前置条件最大值为500";
				}
				datacase.setPre(data);
			}else{
				if(data.length()>500){
					return "false:前置条件最大值为500";
				}
				datacase.setPre(data);
			}
	
		}else if(j==8){
			if(template.equals("2")){
				if(data.length()>30){
					return "false:二级页面最大值为30";
				}
				datacase.setSecpage(data);;
				
			}else if(template.equals("3")){
				if(data.length()>20){
					return "false:用例步号最大值为20";
				}
				datacase.setCasestep(data);;
				
			}else{
				if(data.length()>1000){
					return "false:操作步骤最大值为1000";
				}
				datacase.setStep(data);;
			}
		
		}else if(j==9){
			if(template.equals("2")){
				if(data.length()>30){
					return "false:三级页面最大值为30";
				}
				datacase.setThrpage(data);;
				
			}else if(template.equals("3")){
				if(data.length()>1000){
					return "false:操作步骤最大值1000";
				}
				datacase.setStep(data);;
			}else{
				if(data.length()>1100){
					return "false:期望结果最大值为1100";
				}
				datacase.setExpire(data);	
			}
			
		}else if(j==10){
			if(template.equals("2")){
				if(data.length()>30){
					return "false:四级页面最大值为30";
				}
				datacase.setFourpage(data);;
				
			}else if(template.equals("3")){
				if(data.length()>1100){
					return "false:期望结果最大值为1100";
				}
				datacase.setExpire(data);	
			}else{
				if(data.length()>200){
					return "false:备注最大值为200";
				}
				datacase.setRemark(data);
			}
		
			
		}else if(j==11){	
			if(template.equals("2")){
				if(data.length()>30){
					return "false:五级页面最大值为30";
				}
				datacase.setFivepage(data);;	
				
			}else if(template.equals("3")){
				if(data.length()>5){
					return "false:优先级最大值为5";
				}
				datacase.setOp(data);
			}else{
				if(data.length()>100){
					return "false:ID最大值为100";
				}
				datacase.setId(data);
			}
		
			
		}else if(j==12){
			if(template.equals("2")){
				if(data.length()>500){
					return "false:前置条件最大值为500";
				}
				datacase.setPre(data);
				
			}else if(template.equals("3")){
				if(data.length()>200){
					return "false:备注最大值为200";
				}
				datacase.setRemark(data);
			}
			
			
		}else if(j==13){	
			if(template.equals("2")){
				if(data.length()>1000){
					return "false:操作步骤最大值为1000";
				}
				datacase.setStep(data);;
				
			}else if(template.equals("3")){
				if(data.length()>100){
					return "false:ID最大值为100";
				}
				datacase.setId(data);
			}
			
			
			
		}else if(j==14){
			if(template.equals("2")){
				if(data.length()>1100){
					return "false:期望结果最大值为1100";
				}
				datacase.setExpire(data);	
				
			}
			
		}else if(j==15){
			if(template.equals("2")){
				if(data.length()>200){
					return "false:备注最大值为200";
				}
				datacase.setRemark(data);
			}
			
		}else if(j==16){
			if(template.equals("2")){
				if(data.length()>100){
					return "false:ID最大值为100";
				}
				datacase.setId(data);
			}
			
		}
		
		return "true";
		
	}
	/**
	 * 跳转到资源添加页面
	 * 
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request, PageHelper ph) {
		//request.setAttribute("ComCaseTypeList", ComCaseTypeService.getComCaseTypeList());
		BusCase r = new BusCase();
		r.setId(UUID.randomUUID().toString());
		r.setProject(request.getParameter("project"));
		r.setPid(request.getParameter("pid"));
		r.setSeq(Double.valueOf(request.getParameter("seq"))+1);
		r.setTreeid(ph.getTreeid());
		request.setAttribute("buscase", r);
		return "/case/buscaseAdd";
	}

	/**
	 * 添加资源
	 * 
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Json add(BusCase buscase, HttpSession session,HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		Json j = new Json();
		if(buscase.getId()!=null){
			buscaseService.add(buscase, sessionInfo);
			bugService.add("新增业务用例-- "+buscase.getProject(), sessionInfo);
		}else{//add coment
			String json=request.getParameter("data");
			
			BusCase cases=new BusCase();
			cases.setId(UUID.randomUUID().toString());
			cases.setExpire(JSONObject.fromObject(json).get("expire").toString());
		    

			cases.setModule(request.getParameter("module"));
			if(!request.getParameter("modulechild").equals("undefined"))
			cases.setModulechild(request.getParameter("modulechild"));
			
			
			
			cases.setName(JSONObject.fromObject(json).get("name").toString());
			cases.setOp(JSONObject.fromObject(json).get("op").toString());
			cases.setPid(request.getParameter("pid"));	
			cases.setPre(JSONObject.fromObject(json).get("pre").toString());
			cases.setProject(request.getParameter("project"));
			cases.setTreeid(request.getParameter("projectid"));
			cases.setRemark(JSONObject.fromObject(json).get("remark").toString());
			cases.setSeq(Integer.valueOf(JSONObject.fromObject(json).get("seq").toString()));
			cases.setStep(JSONObject.fromObject(json).get("step").toString());

			
			buscaseService.add(cases, sessionInfo);
			bugService.add("从公共用例新增业务用例-- "+cases.getProject(), sessionInfo);
		}
		j.setObj(UUID.randomUUID().toString());
		j.setSuccess(true);
		j.setMsg("添加成功！");
		
		return j;
	}

	
	
	/**
	 * 跳转到资源编辑页面
	 * 
	 * @return
	 */
	@RequestMapping("/download")
		 public void download(HttpServletRequest request,  
		            HttpServletResponse response) throws Exception {  
		        response.setContentType("text/html;charset=UTF-8");   
		        BufferedInputStream in = null;  
		        BufferedOutputStream out = null;  
		        request.setCharacterEncoding("UTF-8");  
		        String rootpath = request.getSession().getServletContext().getRealPath(  
		                "/");  
		        String fileName ="uploadTemplate1.xls";  
		        try {  
		            File f = new File(rootpath + "\\template\\" + fileName);  
		            response.setContentType("application/octet-stream;charset=UTF-8");  
		            response.setCharacterEncoding("UTF-8");  
		              response.setHeader("Content-Disposition", "attachment; filename="+new String(fileName.getBytes("gbk"),"iso-8859-1"));  
		            response.setHeader("Content-Length",String.valueOf(f.length()));  
		            in = new BufferedInputStream(new FileInputStream(f));  
		            out = new BufferedOutputStream(response.getOutputStream());  
		            byte[] data = new byte[1024];  
		            int len = 0;  
		            while (-1 != (len=in.read(data, 0, data.length))) {  
		                out.write(data, 0, len);  
		            }  
		        } catch (Exception e) {  
		            e.printStackTrace();  
		        } finally {  
		            if (in != null) {  
		                in.close();  
		            }  
		            if (out != null) {  
		                out.close();  
		            }  
		        }  
		  
		    }  
	
	@RequestMapping("/download2")
	 public void download2(HttpServletRequest request,  
	            HttpServletResponse response) throws Exception {  
	        response.setContentType("text/html;charset=UTF-8");   
	        BufferedInputStream in = null;  
	        BufferedOutputStream out = null;  
	        request.setCharacterEncoding("UTF-8");  
	        String rootpath = request.getSession().getServletContext().getRealPath(  
	                "/");  
	        String fileName ="uploadTemplate2.xls";  
	        try {  
	            File f = new File(rootpath + "\\template\\" + fileName);  
	            response.setContentType("application/octet-stream;charset=UTF-8");  
	            response.setCharacterEncoding("UTF-8");  
	              response.setHeader("Content-Disposition", "attachment; filename="+new String(fileName.getBytes("gbk"),"iso-8859-1"));  
	            response.setHeader("Content-Length",String.valueOf(f.length()));  
	            in = new BufferedInputStream(new FileInputStream(f));  
	            out = new BufferedOutputStream(response.getOutputStream());  
	            byte[] data = new byte[1024];  
	            int len = 0;  
	            while (-1 != (len=in.read(data, 0, data.length))) {  
	                out.write(data, 0, len);  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } finally {  
	            if (in != null) {  
	                in.close();  
	            }  
	            if (out != null) {  
	                out.close();  
	            }  
	        }  
	  
	    }  
	
	
	@RequestMapping("/download3")
	 public void download3(HttpServletRequest request,  
	            HttpServletResponse response) throws Exception {  
	        response.setContentType("text/html;charset=UTF-8");   
	        BufferedInputStream in = null;  
	        BufferedOutputStream out = null;  
	        request.setCharacterEncoding("UTF-8");  
	        String rootpath = request.getSession().getServletContext().getRealPath(  
	                "/");  
	        String fileName ="uploadTemplate3.xls";  
	        try {  
	            File f = new File(rootpath + "\\template\\" + fileName);  
	            response.setContentType("application/octet-stream;charset=UTF-8");  
	            response.setCharacterEncoding("UTF-8");  
	              response.setHeader("Content-Disposition", "attachment; filename="+new String(fileName.getBytes("gbk"),"iso-8859-1"));  
	            response.setHeader("Content-Length",String.valueOf(f.length()));  
	            in = new BufferedInputStream(new FileInputStream(f));  
	            out = new BufferedOutputStream(response.getOutputStream());  
	            byte[] data = new byte[1024];  
	            int len = 0;  
	            while (-1 != (len=in.read(data, 0, data.length))) {  
	                out.write(data, 0, len);  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } finally {  
	            if (in != null) {  
	                in.close();  
	            }  
	            if (out != null) {  
	                out.close();  
	            }  
	        }  
	  
	    }  
	 
	 
	/**
	 * 跳转到资源编辑页面
	 * 
	 * @return
	 */
	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request, String id) {
	//	request.setAttribute("ComCaseTypeList", ComCaseTypeService.getComCaseTypeList());
		BusCase r = buscaseService.get(id);
		request.setAttribute("buscase", r);
		return "/case/buscaseEdit";
	}
	
	public static String id=new String();
	@RequestMapping("/output")
	public void outPut(HttpServletRequest request,
            HttpServletResponse response) throws IOException, RowsExceededException, WriteException {
		
		if(request.getParameter("ps")!=null)
		id=request.getParameter("ps");

		
				
		if(request.getParameter("do")!=null){
		String t=request.getParameter("do");
		String[] ids=id.split("\\*");
		List<BusCase> datas=new ArrayList<BusCase>();
		for(String i:ids){
		datas.add(buscaseService.get(i));
			
		}	
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sdf.format(date);
		String filename =str+".xls";
		response.setContentType("application/ms-excel;charset=UTF-8");  
	    response.setHeader("Content-Disposition", "attachment;filename="  
	            .concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));  
		OutputStream out = response.getOutputStream();
		
	    WritableWorkbook book = Workbook.createWorkbook(out);
	  
	  
        // 生成名为“第一页”的工作表，参数0表示这是第一页
      WritableSheet sheet = book.createSheet("第一页", 0);
      for(int i=0;i<10;i++){
  		sheet.setColumnView(i,20);  
  		}
      
      WritableFont wfc1 = new WritableFont(WritableFont.ARIAL,18);       
      WritableCellFormat wcfFC1 = new WritableCellFormat(wfc1);      
      wcfFC1.setBackground(Colour.ICE_BLUE);
      wcfFC1.setAlignment(jxl.format.Alignment.CENTRE);
      wcfFC1.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN,jxl.format.Colour.DARK_BLUE);

      WritableFont wfc2 = new WritableFont(WritableFont.ARIAL,14);       
      WritableCellFormat wcfFC2 = new WritableCellFormat(wfc2);
      wcfFC2.setAlignment(jxl.format.Alignment.LEFT);
     
      String[] vv;
      if(t.equals("2")){
    	  vv=new String[]{"模板","项目用例","上级ID","测试编号","测试项目","测试描述","优先级","一级页面","二级页面","三级页面","四级页面","五级页面","前置条件","操作步骤","预期结果","备注","ID"};  
      }else if(t.equals("3")){
    	  vv=new String[]{"模板","项目用例","上级ID","测试模块","子模块","测试编号","测试名称","测试步号","前置条件","操作步骤","预期结果","优先级","备注","ID"};    
      }else{
    	  vv=new String[]{"模板","项目用例","上级ID","测试模块","子模块","测试项目","优先级","前置条件","操作步骤","预期结果","备注","ID"};  
      }
      
      for(int i=0;i<vv.length;i++){
      sheet.addCell(new Label(i, 0, vv[i],wcfFC1));
      }
      
      int cols=0;
      if(t.equals("2")){
    	  cols=16;
    	  sheet.addCell(new Label(0, 1, "2",wcfFC2)); 
      }else if(t.equals("3")){
    	  cols=13;
    	  sheet.addCell(new Label(0, 1, "3",wcfFC2));
      }else{
    	  cols=11;
    	  sheet.addCell(new Label(0, 1, "1",wcfFC2));
      }
      
      
      
      for(int i=1;i<=datas.size();i++){
    	  for(int j=1;j<=cols;j++){
    		  String data=changeData(datas.get(i-1),j,t);
    		 if(data!=null)
    			  data=data.replace("&amp;", "&").replace("<br>", "\n").replace("&quot;", "\"");
    	  sheet.addCell(new Label(j, i,data,wcfFC2));
    	  }
      }
      
      
      book.write();
		  book.close();
				out.close();
				id="";
	
		}
	}
	
	public String changeData(BusCase r,int j,String t){
		String data="";
		if(j==1){
			if(t.equals("2")){
				data=r.getProject();
			}else if(t.equals("3")){
				data=r.getProject();
			}else{
				data=r.getProject();
			}
			
		}else if(j==2){
			if(t.equals("2")){
				data=r.getPid();
			}else if(t.equals("3")){
				data=r.getPid();
			}else{
				data=r.getPid();
			}
			
		}else if(j==3){
			if(t.equals("2")){
				data=r.getCasenum();
			}else if(t.equals("3")){
				data=r.getModule();
			}else{
				data=r.getModule();
			}
			
		}else if(j==4){
			if(t.equals("2")){
				data=r.getCasestep();
			}else if(t.equals("3")){
				data=r.getModulechild();
			}else{
				data=r.getModulechild();
			}
			
		}else if(j==5){
			if(t.equals("2")){
				data=r.getName();
			}else if(t.equals("3")){
				data=r.getCasenum();
			}else{
				data=r.getName();
			}
			
		}else if(j==6){
			if(t.equals("2")){
				data=r.getOp();
			}else if(t.equals("3")){
				data=r.getName();
			}else{
				data=r.getOp();
			}
			
			
		}else if(j==7){
			if(t.equals("2")){
				data=r.getFristpage();
			}else if(t.equals("3")){
				data=r.getPre();
			}else{
				data=r.getPre();
			}
			
		}else if(j==8){
			if(t.equals("2")){
				data=r.getSecpage();
			}else if(t.equals("3")){
				data=r.getCasestep();
			}else{
				data=r.getStep();
			}
			
		}else if(j==9){
			if(t.equals("2")){
				data=r.getThrpage();
			}else if(t.equals("3")){
				data=r.getStep();
			}else{
				data=r.getExpire();
			}
			
		}else if(j==10){
			if(t.equals("2")){
				data=r.getFourpage();
			}else if(t.equals("3")){
				data=r.getExpire();
			}else{
				data=r.getRemark();
			}
			
		}else if(j==11){
			if(t.equals("2")){
				data=r.getFivepage();
			}else if(t.equals("3")){
				data=r.getOp();
			}else{
				data=r.getId();
			}
			
		
		}else if(j==12){
			if(t.equals("2")){
				data=r.getPre();
			}else if(t.equals("3")){
				data=r.getRemark();
			}
		}else if(j==13){
			if(t.equals("2")){
				data=r.getStep();
			}else if(t.equals("3")){
				data=r.getId();
			}
		}else if(j==14){
			if(t.equals("2")){
				data=r.getExpire();
			}
		}else if(j==15){
			if(t.equals("2")){			
				data=r.getRemark();
			}
		}else if(j==16){
			if(t.equals("2")){
				data=r.getId();
			}
		}
		return data;
		
	}

	/**
	 * 编辑资源
	 * 
	 * @param ComCase
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(BusCase buscase,HttpServletRequest request,HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());

		if(request.getParameter("oid")!=null){
			
			buscaseService.edit(request.getParameter("oid"), request.getParameter("nid"),sessionInfo);
			
			bugService.add("拖动模式操作业务用例-- "+request.getParameter("oid"), sessionInfo);
		}else{
			buscaseService.edit(buscase,sessionInfo);
			bugService.add("修改业务用例-- "+buscase.getProject()+" -- "+buscase.getId(), sessionInfo);
		}
		Json j = new Json();
		buscaseService.edit(buscase,sessionInfo);
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
	public List<BusCase> treeGrid(HttpSession session,HttpServletRequest request,BusCase bug, PageHelper ph) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		Map<String, Object> data=buscaseService.treeGrid(sessionInfo,request.getParameter("data"),bug,ph);	
		bugService.add("进入业务用例导航-- "+request.getParameter("data"), sessionInfo);
		//request.setAttribute("total", data.get("total"));
		return (List<BusCase>) data.get("rows");
	}
	
	
	@RequestMapping("/total")
	@ResponseBody
	public Long getTotal(HttpSession session,HttpServletRequest request,BusCase bug, PageHelper ph) {
		return buscaseService.getTotal(request.getParameter("data"), bug,  ph);
		
	}
	
	
	@RequestMapping("/collecttreeGrid")
	@ResponseBody
	public List<BusCase> collecttreeGrid(HttpSession session,HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());

		return buscaseService.collecttreeGrid(sessionInfo,request.getParameter("pid"));
	}
	/**
	 * 删除资源
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(HttpServletRequest request,HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		Json j = new Json();
		String id=request.getParameter("ps");
		String[] ids=id.split("\\*");
		
		for(String i:ids){
		
			bugService.add("删除业务用例-- "+buscaseService.delete(i), sessionInfo);;
		
		}	
		
		j.setMsg("删除成功！");
		j.setSuccess(true);
		return j;
	}
	
	
	@RequestMapping("/copy")
	@ResponseBody
	public Json copy(HttpServletRequest request, HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		Json j = new Json();
		String id=request.getParameter("ps");
		String pid=request.getParameter("pid");
		String[] ids=id.split("\\*");

		for(String i:ids){		
			buscaseService.copy(i,pid, sessionInfo);
		}	
		
		j.setMsg("复制成功！");
		j.setSuccess(true);
		return j;
	}
	
	@RequestMapping("/insert")
	@ResponseBody
	public Json insert(HttpServletRequest request, HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		Json j = new Json();
		String id=request.getParameter("id");
		String pid=request.getParameter("pid");	
		buscaseService.insert(id,pid, sessionInfo);
		j.setMsg("插入成功！");
		j.setSuccess(true);
		return j;
	}
	
	@RequestMapping("/copyInsert")
	@ResponseBody
	public Json copyInsert(HttpServletRequest request, HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		Json j = new Json();
		String id=request.getParameter("id");
		String targetId=request.getParameter("targetId");	
		buscaseService.copyInsert(id,targetId, sessionInfo);
		j.setMsg("插入成功！");
		j.setSuccess(true);
		return j;
	}
	

}
