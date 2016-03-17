package cn.edu.cdtu.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.cdtu.entity.TAdmin;
import cn.edu.cdtu.entity.TCategory;
import cn.edu.cdtu.entity.TUser;
import cn.edu.cdtu.service.TAdminService;
import cn.edu.cdtu.service.TCategoryService;
import cn.edu.cdtu.utils.MD5Util;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
public class AdminAction extends ActionSupport {
	HttpServletRequest req = ServletActionContext.getRequest();
	@Resource
	private TAdminService service;
	@Resource
	private TCategoryService serviceTc;
	private TAdmin admin;

	public TAdmin getAdmin() {
		return admin;
	}

	public void setAdmin(TAdmin admin) {
		this.admin = admin;
	}
/**
 * ����Ա��½
 * @return
 */
	public String adminUI() {
		TAdmin u = service.validateLoginInfo(admin.getAdminName(),
				MD5Util.Md5(admin.getAdminPassword()));
		System.out.println(MD5Util.Md5(admin.getAdminPassword()));
		/*
		 * ��ʼ�ж�
		 */
		if (u == null) {
			req.setAttribute("msg", "�û������������");
			return "login";
		}
		// �����û���session
		req.getSession().setAttribute("sessionAdmin", u);
		// ��ȡ�û������浽cookie��
		return "index";// �ص���ҳ
	}
	
}
