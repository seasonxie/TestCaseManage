package sy.test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import sy.dao.BugDaoI;
import sy.pageModel.Bug;
import sy.service.BugServiceI;
import static org.mockito.Mockito.*; 


public class test {
	 static List<String> list = mock(List.class); 
	 
	public static void main(String[] args) {
	
		Bug s=mock(Bug.class); 
		when(s.getId()).thenReturn("dddddddd");
		System.out.println(s.getId());   //print ddddddd
		
		
		
		ApplicationContext ac = new ClassPathXmlApplicationContext(new String[]{"classpath:spring.xml", "classpath:spring-hibernate.xml", "classpath:spring-druid.xml" });
		/*	ac.g
			BeanFactory bean = (BeanFactory)ac; */
			BugServiceI bugService=ac.getBean(BugServiceI.class);
			
			
			
			bugService.clo();
		
		 //创建mock对象，参数可以是类，也可以是接口  
        
          
        //设置方法的预期返回值  
       
        List<String> lista = as();  
          
        System.out.println(lista.get(0));
        //验证方法调用(是否调用了get(0))  
        System.out.println(verify(list).get(0)); 
        when(list.get(1)).thenThrow(new RuntimeException("test excpetion"));  
      
        //junit测试  
       // Assert.assertEquals("helloworld", result);  
	}
	
	public static  List<String> as(){
		 when(list.get(0)).thenReturn("helloworld");  
	      
		return list;
		
	}
	
	
/*	List<String> list = mock(List.class);  
	1.  when(list.get(0)).thenReturn("helloworld");  
	2.  verify(list).get(0);  验证是否有get-0
	3.  verify(list,times(2)).clear();  验证list被清除2次
	verify(mockedList).add("once");   验证add过once
	     verify(mockedList, times(2)).add("twice");  验证调用过2次add"twice"
	3. when(list.get(1)).thenThrow(new RuntimeException("test excpetion"));  抛异常
	4. when(list.get(anyInt())).thenReturn("hello","world");  */
	
	private void tt() {
		// TODO Auto-generated method stub
	System.out.println("aa");
		
		ApplicationContext ac = new ClassPathXmlApplicationContext(new String[]{"classpath:spring.xml", "classpath:spring-hibernate.xml", "classpath:spring-druid.xml" });
		BugServiceI bugService=ac.getBean(BugServiceI.class);
		bugService.clo();
		/*	ac.g
		BeanFactory bean = (BeanFactory)ac; */
		
	}

	
}
