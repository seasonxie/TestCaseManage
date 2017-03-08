package sy.controller;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sy.pageModel.SessionInfo;
import sy.pageModel.TextBox;
import sy.pageModel.DataGrid;
import sy.pageModel.Json;
import sy.pageModel.PageHelper;
import sy.service.TextBoxServiceI;
import sy.util.ConfigUtil;

/**
 * TextBox管理控制器
 * 
 * @author 孙宇
 * 
 */
@Controller
@RequestMapping("/dataController")
public class dataController extends BaseController {

	@Autowired
	private TextBoxServiceI textboxService;


	/**
	 * 跳转到TextBox管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/manager")
	public String manager(HttpServletRequest request) {

		return "/testdata/testData";
	}

	/**
	 * 获取TextBox数据表格
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/dataGrid")
	@ResponseBody
	public DataGrid dataGrid(TextBox TextBox, PageHelper ph) {
		return textboxService.dataGrid(TextBox, ph);
	}

	/**
	 * 跳转到添加TextBox页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request) {
		TextBox b = new TextBox();
		b.setId(UUID.randomUUID().toString());
		request.setAttribute("TextBox", b);
		return "/testdata/testDataAdd";
	}

	/**
	 * 添加TextBox
	 * 
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Json add(HttpSession session,TextBox TextBox) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		Json j = new Json();
		try {
			textboxService.add(TextBox,sessionInfo);
			j.setSuccess(true);
			j.setMsg("添加成功！");
		} catch (Exception e) {
			// e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}

	public String newString(int num,String type){
		
		String data="";
		try{
		int i=0;
	
		while(true){
			if(i>=num){
				break;
			}
			
			if(type.equals("string")){
				data+="t";
			}else if(type.equals("chinese")){
				data+="测";
			}else if(type.equals("daxie")){
				data+="A";
			}else if(type.equals("xiaoxie")){
				data+="a";
			}else if(type.equals("daxiao")){
				if(i%2==0){
					data+="A";
				}else{
					data+="a";
				}			
			}else if(type.equals("int")){
				data+="1";			
			}else if(type.equals("mix")){
				if(i%2==0){
					data+="a";
				}else if(i%3==0){
					data+="&";
				}else if(i%4==0){
					data+="1";
				}else{
					data+="测";
				}				
			}
			
			
			i++;
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return data;		
	}

	@RequestMapping(value="/download")
	public void download(HttpServletRequest request,
            HttpServletResponse response, String id) throws Exception{
		TextBox b = textboxService.get(id);
		
		Map<String,String> tds=new HashMap<String,String>();
		
		
		
		textData(b,tds);
		numData(b,tds);
		commomData(b,tds);
		String filename ="测试数据"+System.currentTimeMillis()+".xls";
		response.setContentType("application/ms-excel;charset=UTF-8");  
	    response.setHeader("Content-Disposition", "attachment;filename="  
	            .concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));  
		OutputStream out = response.getOutputStream();
		
	    WritableWorkbook book = Workbook.createWorkbook(out);
        // 生成名为“第一页”的工作表，参数0表示这是第一页
      WritableSheet sheet = book.createSheet("第一页", 0);
     
  		sheet.setColumnView(0,10);  
  		sheet.setColumnView(1,35);  
  		sheet.setColumnView(2,100);  
  	
   
      WritableFont wfc1 = new WritableFont(WritableFont.ARIAL,18);       
      WritableCellFormat wcfFC1 = new WritableCellFormat(wfc1);      
      wcfFC1.setBackground(Colour.ICE_BLUE);
      wcfFC1.setAlignment(jxl.format.Alignment.CENTRE);
      wcfFC1.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN,jxl.format.Colour.DARK_BLUE);

      WritableFont wfc2 = new WritableFont(WritableFont.ARIAL,14);       
      WritableCellFormat wcfFC2 = new WritableCellFormat(wfc2);
      wcfFC2.setAlignment(jxl.format.Alignment.LEFT);

      
      String[] vv={"序号","检查项目","测试数据"};
      for(int i=0;i<vv.length;i++){
      sheet.addCell(new Label(i, 0, vv[i],wcfFC1));
      }  
      
      int rows=1;
      List<Entry<String, String>> mappingList = null; 
	  //通过ArrayList构造函数把map.entrySet()转换成list 
	  mappingList = new ArrayList<Map.Entry<String,String>>(tds.entrySet()); 
	  //通过比较器实现比较排序 
	  Collections.sort(mappingList, new Comparator<Map.Entry<String,String>>(){ 
	   public int compare(Map.Entry<String,String> mapping1,Map.Entry<String,String> mapping2){ 
	    return mapping1.getKey().compareTo(mapping2.getKey()); 
	   } 
	  }); 
	  
	  for(Map.Entry<String,String> mapping:mappingList){ 	     
		     sheet.addCell(new Label(0, rows,String.valueOf(rows),wcfFC2));
	    	  sheet.addCell(new Label(1, rows,mapping.getKey(),wcfFC2));
	    	  sheet.addCell(new Label(2, rows, mapping.getValue(),wcfFC2));
	    	   rows++;
		  } 
	

    	 
    
    book.write();
	book.close();
	out.close();
	}
	
	public void textData(TextBox b,Map<String,String> tds){

           if(b.getType().equals("文本输入框")||b.getType().equals("HTTP校验")||b.getType().equals("邮件校验")){
			
			if(b.getBoundary_down()!=null&&b.getBoundary_up()!=null)
			if(!b.getBoundary_down().isEmpty()&&!b.getBoundary_up().isEmpty()){
			tds.put("英文字符最小边界值-1", newString(Integer.valueOf(b.getBoundary_down())-1,"string"));
			tds.put("英文字符最小边界值", newString(Integer.valueOf(b.getBoundary_down()),"string"));
			tds.put("英文字符最小边界值+1", newString(Integer.valueOf(b.getBoundary_down())+1,"string"));
			
			tds.put("英文字符最大边界值-1", newString(Integer.valueOf(b.getBoundary_up())-1,"string"));
			tds.put("英文字符最大边界值", newString(Integer.valueOf(b.getBoundary_up()),"string"));
			tds.put("英文字符最大边界值+1", newString(Integer.valueOf(b.getBoundary_up())+1,"string"));
			
			tds.put("中文字符最小边界值-1", newString(Integer.valueOf(b.getBoundary_down())-1,"chinese"));
			tds.put("中文字符最小边界值", newString(Integer.valueOf(b.getBoundary_down()),"chinese"));
			tds.put("中文字符最小边界值+1", newString(Integer.valueOf(b.getBoundary_down())+1,"chinese"));
			
			tds.put("中文字符最大边界值-1", newString(Integer.valueOf(b.getBoundary_up())-1,"chinese"));
			tds.put("中文字符最大边界值", newString(Integer.valueOf(b.getBoundary_up()),"chinese"));
			tds.put("中文字符最大边界值+1", newString(Integer.valueOf(b.getBoundary_up())+1,"chinese"));
			}
			
			
			if(b.getBlank_q()!=null)
			if(b.getBlank_q().equals("on")){
			tds.put("正常字符前置空格", "  "+newString(Integer.valueOf(b.getBoundary_down())+1,"string"));
			tds.put("最大字符前置空格", "  "+newString(Integer.valueOf(b.getBoundary_up()),"chinese"));
			}
			
			
			if(b.getBlank_z()!=null)
			if(b.getBlank_z().equals("on")){
			String case1=newString(Integer.valueOf(b.getBoundary_down())+1,"string");
			String case2=newString(Integer.valueOf(b.getBoundary_up()),"chinese");
			if(case1.length()>0){
				StringBuffer stringBuffer = new StringBuffer(case1);
				case1=stringBuffer.insert(case1.length()/2, "  ").toString();
			}
             if(case2.length()>0){
            	 StringBuffer stringBuffer = new StringBuffer(case2);
 				case2=stringBuffer.insert(case1.length()/2, "  ").toString();
			}
			
			tds.put("正常字符中置空格", case1);
			tds.put("最大字符中置空格", case2);
			}
			
			
			if(b.getBlank_h()!=null)
			if(b.getBlank_h().equals("on")){
			tds.put("正常字符后置空格", newString(Integer.valueOf(b.getBoundary_down())+1,"string")+"  ");			
			tds.put("最大字符后置空格", newString(Integer.valueOf(b.getBoundary_up()),"chinese")+"  ");
			}
					
			
		}

           if(b.getType().equals("HTTP校验")){
        	   
        	   String http="http://";
        	   tds.put("HTTP最大字符+1", http+newString(Integer.valueOf(b.getBoundary_up())-6,"string"));
        	   tds.put("HTTP最大字符", http+newString(Integer.valueOf(b.getBoundary_up())-7,"string"));
        	   tds.put("HTTP最大字符-1", http+newString(Integer.valueOf(b.getBoundary_up())-8,"string"));
        	   
        	   String https="https://";
        	   tds.put("HTTPS最大字符+1", https+newString(Integer.valueOf(b.getBoundary_up())-7,"string"));
        	   tds.put("HTTPS最大字符", https+newString(Integer.valueOf(b.getBoundary_up())-8,"string"));
        	   tds.put("HTTPS最大字符-1", https+newString(Integer.valueOf(b.getBoundary_up())-9,"string"));
        	   
        	   tds.put("不正确的HTTP格式1", "http//www.meizu.com/"); 
        	   tds.put("不正确的HTTP格式2", "http:/www.meizu.com/");
        	   tds.put("不正确的HTTP格式3", "httpwww.meizu.com/");
        	   tds.put("不正确的HTTP格式4", "http://www.meizu.com.meizu.meizu.com");
        	   tds.put("不正确的HTTP格式5", "http://www.meizu.comhttp://meizu.meizu.com");
        	   tds.put("不正确的HTTPS格式1", "https//www.meizu.com/");
        	   tds.put("不正确的HTTPS格式2", "https:/www.meizu.com/");
        	   tds.put("不正确的HTTPS格式3", "httpswww.meizu.com/");
        	   tds.put("不正确的HTTPS格式4", "https://www.meizu.comhttps://www.meizu.com");
        	   tds.put("HTTP格式", "http://www.meizu.com/");        	  
        	   tds.put("HTTP带参数", "http://www.meizu.com/?refcode=baidu_pinzuan&utm_source=baidupz&utm_medium=cpc"); 
        	   tds.put("HTTP带中文参数", "http://www.baidu.com/s?wd=魅族&tn=87048150_dg&ie=utf8"); 
        	   tds.put("HTTP带空格参数", "http://www.baidu.com/s?wd=魅  族&tn=  87048150_dg & ie = utf8"); 
        	   tds.put("HTTP带符号参数", "http://www.baidu.com/?wq=#~!@#$%^&*(){}_+-="); 
        	   tds.put("HTTP带混合参数", "http://www.baidu.com/s?num=0.5456&wd=魅族&tn=utf_88150_dg&sign=@#$%^&*()-=_+"); 
        	   tds.put("HTTP带转义参数", "http://www.baidu.com/s?wd=%E9%AD%85%E6%97%8F&tn=!%40%23%24%25%5E%26*()_%2B-%3D");
        	   tds.put("HTTPS格式", "https://www.meizu.com/"); 
        	   tds.put("HTTPS带参数", "https://www.meizu.com/?refcode=baidu_pinzuan&utm_source=baidupz&utm_medium=cpc"); 
        	   tds.put("HTTPS带中文参数", "https://www.baidu.com/s?wd=魅族&tn=87048150_dg&ie=utf8"); 
        	   tds.put("HTTPS带空格参数", "https://www.baidu.com/s?wd=魅  族&tn=  87048150_dg & ie = utf8"); 
        	   tds.put("HTTPS带符号参数", "https://www.baidu.com/?wq=#~!@#$%^&*(){}_+-="); 
        	   tds.put("HTTPS带混合参数", "https://www.baidu.com/s?num=0.5456&wd=魅族&tn=utf_88150_dg&sign=@#$%^&*()-=_+"); 
        	   tds.put("HTTPS带转义参数", "https://www.baidu.com/s?wd=%E9%AD%85%E6%97%8F&tn=!%40%23%24%25%5E%26*()_%2B-%3D");
           }
           
            if(b.getType().equals("邮件校验")){
            	String case1=newString(Integer.valueOf(b.getBoundary_up())-1,"string");
    		
    			if(case1.length()>0){
    				StringBuffer stringBuffer = new StringBuffer(case1);
    				case1=stringBuffer.insert(case1.length()/2, "@").toString();
    			}
            	   tds.put("EMAIL最大字符+1", case1+"T");
            	   tds.put("EMAI最大字符", case1);
            	   tds.put("EMAI最大字符-1", case1.substring(0,case1.length()-1));
            	   
            	   tds.put("不正确的邮箱校验-@和。中间没有数据", "xxxx@.");   
            	   tds.put("不正确的邮箱校验-@前面没有数据","@sdsdds");   
            	   tds.put("不正确的邮箱校验-。前面没有数据",".dsdsds");  
            	   tds.put("不正确的邮箱校验-@后面没有数据","sdsdds@");   
            	   tds.put("不正确的邮箱校验-.后面没有数据","dsdsds."); 
            	   tds.put("不正确的邮箱校验-没有。", "xxx@meizucom");   
            	   tds.put("不正确的邮箱校验-含两个@", "xxx@@meiz.ucom"); 
            	   tds.put("不正确的邮箱校验-含两个。", "xxx@meizuc..om"); 
            	   tds.put("不正确的邮箱校验-多个特殊符号", "xxx@mei~·！@#￥%……&*（）-=-=【】、{|}：“；‘《》、，。、、zuc.om"); 
            	
        	   
           }
           
        
	}
	
	
	public void numData(TextBox b,Map<String,String> tds){
        if(b.getType().equals("数字输入框")){
			
			if(b.getBoundary_down()!=null&&b.getBoundary_up()!=null)
			if(!b.getBoundary_down().isEmpty()&&!b.getBoundary_up().isEmpty()){
				
			tds.put("整数边界值+1", newString(Integer.valueOf(b.getBoundary_up()),"int")+"1");	
			tds.put("整数边界值", newString(Integer.valueOf(b.getBoundary_up()),"int"));	
			tds.put("整数边界值-1", newString(Integer.valueOf(b.getBoundary_up()),"int").substring(0, newString(Integer.valueOf(b.getBoundary_up()),"int").length()-1));	
			
			tds.put("浮点边界值+1", "0."+newString(Integer.valueOf(b.getBoundary_up())-1,"int"));	
			tds.put("浮点边界值", "0."+newString(Integer.valueOf(b.getBoundary_up())-2,"int"));	
			tds.put("浮点边界值-1", "0."+newString(Integer.valueOf(b.getBoundary_up())-3,"int"));	
			}
			
			
			if(b.getBlank_q()!=null)
			if(b.getBlank_q().equals("on")){
			tds.put("最大整数前置空格", "  "+newString(Integer.valueOf(b.getBoundary_up()),"int"));
			tds.put("最大浮点前置空格", "  0."+newString(Integer.valueOf(b.getBoundary_up())-2,"int"));
			}
			
			
			if(b.getBlank_z()!=null)
			if(b.getBlank_z().equals("on")){
			String case1=newString(Integer.valueOf(b.getBoundary_up()),"int");
			String case2="0."+newString(Integer.valueOf(b.getBoundary_up())-2,"int");
			if(case1.length()>0){
				StringBuffer stringBuffer = new StringBuffer(case1);
				case1=stringBuffer.insert(case1.length()/2, "  ").toString();
			}
          if(case2.length()>0){
         	 StringBuffer stringBuffer = new StringBuffer(case2);
				case2=stringBuffer.insert(case1.length()/2, "  ").toString();
			}
			
			tds.put("最大整数中置空格", case1);
			tds.put("最大浮点中置空格", case2);
			}
			
			
			if(b.getBlank_h()!=null)
			if(b.getBlank_h().equals("on")){
				tds.put("最大整数后置空格", newString(Integer.valueOf(b.getBoundary_up()),"int")+"  ");
				tds.put("最大浮点后置空格", "0."+newString(Integer.valueOf(b.getBoundary_up())-2,"int")+"  ");
			}
					
			
		}
	}
	

	


	
	public void commomData(TextBox b,Map<String,String> tds){
		if(b.getJh()!=null)
			if(b.getJh().equals("on")){
				tds.put("JS语句1", "<scrpit>alert(123)</script>");	
				tds.put("JS语句2", "\" onclick=\"javascript:alert(123)");	
				tds.put("HTML语句","<h1>test</h1>");
				tds.put("SQL语句", "' or 1=1");
				}
			
			
			if(b.getSign()!=null)
			if(b.getSign().equals("on")){
				tds.put("全部特殊字符", "~·！@#￥%……&*（）——+-={}|【】、：“；‘《》,.<>？/、");	
				tds.put("URL特殊字符", "#&?+=");
				}
			
			if(b.getLetter_d()!=null)
			if(b.getLetter_d().equals("on")){
				tds.put("大写字母校验-正常", newString(Integer.valueOf(b.getBoundary_up())/2,"daxie"));	
				tds.put("大写字母校验-最大", newString(Integer.valueOf(b.getBoundary_up()),"daxie"));	
				}
			
			if(b.getLetter_x()!=null)
			if(b.getLetter_x().equals("on")){
				tds.put("小写字母校验-正常", newString(Integer.valueOf(b.getBoundary_up())/2,"xiaoxie"));	
				tds.put("小写字母校验-最大", newString(Integer.valueOf(b.getBoundary_up()),"xiaoxie"));	
				}
			
			
			if(b.getLetter_m()!=null)
			if(b.getMix()!=null)
			if(b.getLetter_m().equals("on")){
				tds.put("混合字母校验-正常", newString(Integer.valueOf(b.getBoundary_up())/2,"daxiao"));	
				tds.put("混合字母校验-最大", newString(Integer.valueOf(b.getBoundary_up()),"daxiao"));	
				}
			
			if(!b.getType().equals("数字输入框")){
			if(b.getNumber_i()!=null)
			if(b.getNumber_i().equals("on")){
				tds.put("数字校验", newString(Integer.valueOf(b.getBoundary_up()),"int"));	
				}
			
			if(b.getNumber_d()!=null)
			if(b.getNumber_d().equals("on")){
				tds.put("浮点校验", "0."+newString(Integer.valueOf(b.getBoundary_up())-2,"int"));	
				}
			}
			
			if(b.getMix()!=null)
			if(b.getMix().equals("on")){
				tds.put("混合字符校验", newString(Integer.valueOf(b.getBoundary_up()),"mix"));	
				}
			
			if(b.getHtmlencode()!=null)
			if(b.getHtmlencode().equals("on")){
				int l=Integer.valueOf(b.getBoundary_up());
				String case1="&lt;script&gt;alert(123)&lt;/script&gt;";
				String case2="&quot; onclick=&quot;alert(123)";
				if(case1.length()<l){
					l=case1.length();
					
				}
				tds.put("HTMLEncode1", case1.substring(0, l));	
				if(case2.length()<l){
					l=case2.length();
				}			
				tds.put("HTMLEncode2", case2.substring(0, l));	
				}
			
			if(b.getUrlencode()!=null)
			if(b.getUrlencode().equals("on")){
				String case3="%26%26lt%3bscript%26gt%3balert(123)%26lt%3b%2fscript%26gt%3b";
				String case4="%26quot%3b%26gt%3b%26lt%3bh1%26gt%3btest%26lt%3b%2fhi%26gt%3b";
				int ll=Integer.valueOf(b.getBoundary_up());
				if(case3.length()<ll){
					ll=case3.length();
					
				}
				tds.put("URLEncode1", case3.substring(0, ll));	
				if(case4.length()<ll){
					ll=case4.length();
					
				}
				tds.put("URLEncode2",  case4.substring(0, ll));	
				}
	}
	/**
	 * 跳转到TextBox修改页面
	 * 
	 * @return
	 */
	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request, String id) {
		TextBox b = textboxService.get(id);
		request.setAttribute("TextBox", b);
		return "/testdata/testDataEdit";
	}

	/**
	 * 修改TextBox
	 * 
	 * @param TextBox
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(HttpSession session,TextBox TextBox) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		Json j = new Json();
		try {
			textboxService.edit(TextBox,sessionInfo);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
		} catch (Exception e) {
			// e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 删除TextBox
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String id) {
		Json j = new Json();
		textboxService.delete(id);
		j.setMsg("删除成功！");
		j.setSuccess(true);
		return j;
	}

}
