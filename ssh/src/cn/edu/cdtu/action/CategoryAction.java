package cn.edu.cdtu.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.cdtu.entity.TCategory;
import cn.edu.cdtu.service.TCategoryService;

@Controller
@Scope("prototype")
public class CategoryAction {
	HttpServletResponse resp = ServletActionContext.getResponse();
	HttpServletRequest req = ServletActionContext.getRequest();
	@Resource
	private TCategoryService service;

	/**
	 * ��ѯ���з���
	 */
	public String findAll() {
		/*
		 * 1. ͨ��service�õ����еķ��� 2. ���浽request�У�ת����left.jsp
		 */
		List<TCategory> parents = service.findAll();
	
		/*
		 * 2. ѭ���������е�һ�����࣬Ϊÿ��һ������������Ķ�������
		 */
		for (TCategory parent : parents) {
			// ��ѯ����ǰ������������ӷ���
			List<TCategory> childrens = service.findByParent(parent.getCategoryId());
			System.out.println(childrens.size());
				parent.setCategory(childrens);
		}
	
		req.setAttribute("parents", parents);
		return "left";
	}
}
