package cn.edu.cdtu.test;

import java.util.Date;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.edu.cdtu.dao.BaseDao;

import cn.edu.cdtu.service.BaseService;

public class TestSessionFactory {
	/**
	 * ����hibernate��spring�Ƿ����ϳɹ�
	 */
@Test
	public void testSessionFactory() {
		ApplicationContext contxt = new ClassPathXmlApplicationContext(
				"beans.xml");
		BaseDao dao=(BaseDao) contxt.getBean("baseDaoImpl");
		//Employee user=new Employee();
		/*user.setName("����");
		user.setEmail("714925749@qq.com");
		user.setDate(new Date());
		user.setCteateDate(new Date(1992, 1, 1));
		dao.save(user);*/
	}
}
