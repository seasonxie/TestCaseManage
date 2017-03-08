package sy.util;

import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import sy.dao.BugDaoI;

/** 
 * @author  作者 
 * @date 创建时间：2016年10月18日 下午6:46:03 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
public class MockUtil {
	
	public static boolean MockStart=false;
	
	public static void mock(BugDaoI bugDao){		
		List<Object[]> mockdata=new LinkedList<Object[]>();
		mockdata.add(new Object[]{"8645","test1"});
		when(bugDao.findBySql("select distinct name from tbug")).thenReturn(mockdata);
	}
	
	public static BugDaoI set(){
		ApplicationContext ac = new ClassPathXmlApplicationContext(new String[]{"classpath:spring.xml", "classpath:spring-hibernate.xml", "classpath:spring-druid.xml" });
		BugDaoI bugDao=(BugDaoI) ac.getBean("bugDaoImpl");
		return bugDao;
	}

}


