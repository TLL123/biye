package cn.edu.cdtu.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.cdtu.entity.TBook;
import cn.edu.cdtu.entity.TCategory;
import cn.edu.cdtu.service.TAdminService;
import cn.edu.cdtu.service.TBookService;
import cn.edu.cdtu.service.TCategoryService;

@Controller
@Scope("prototype")
public class AdminCategoryAction {
	HttpServletRequest req = ServletActionContext.getRequest();
	TCategory category;
	private String cid;// ���id
	private String pid;// ��������e��һ�����id
	private String desc;// �������
	private String cname;// ������Q
	@Resource
	private TCategoryService serviceTc;
	@Resource
	private TBookService serviceBo;
	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public TCategory getCategory() {
		return category;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public void setCategory(TCategory category) {
		this.category = category;
	}

	/**
	 * ��ѯ���з���
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAll() {
		/*
		 * 1. ͨ��service�õ����еķ��� 2. ���浽request�У�ת����left.jsp
		 */
		List<TCategory> parents = serviceTc.findAll();

		/*
		 * 2. ѭ���������е�һ�����࣬Ϊÿ��һ������������Ķ�������
		 */
		for (TCategory parent : parents) {
			// ��ѯ����ǰ������������ӷ���
			List<TCategory> childrens = serviceTc.findByParent(parent
					.getCategoryId());
			System.out.println(childrens.size());
			parent.setCategory(childrens);
		}
		req.setAttribute("parents", parents);
		return "list";
	}

	/**
	 * ���һ������
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addParent() {
		/*
		 * 1. ��װ�����ݵ�Category�� 2. ����service��add()���������� 3.
		 * ����findAll()������list.jsp��ʾ���з���
		 */
		serviceTc.add(category);
		return findAll();
	}

	/**
	 * ��ӵڶ����ࣺ��һ��
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addChildPre() {
		String pid = req.getParameter("pid");// ��ǰ����ĸ�����id
		List<TCategory> parents = serviceTc.findParents();
		req.setAttribute("pid", pid);
		req.setAttribute("parents", parents);
		return "add2";
	}

	/**
	 * ��Ӷ�������ڶ���
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */

	public String addChild() {
		// ���
		TCategory child = new TCategory();
		child.setCategoryName(cname);
		child.setCategoryDesc(desc);
		// ���
		TCategory parent = new TCategory();
		parent.setCategoryId(pid);
		child.setTCategory(parent);

		serviceTc.add(child);
		return findAll();
	}

	/**
	 * �޸�һ�����ࣺ��һ��
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editParentPre() {
		TCategory parent = serviceTc.get(TCategory.class, cid);
		req.setAttribute("parent", parent);
		return "edit";
	}

	/**
	 * �޸�һ�����ࣺ�ڶ���
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editParent() {

		serviceTc.update(category);
		return findAll();
	}

	/**
	 * �޸Ķ������ࣺ��һ��
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editChildPre() {
		TCategory child = serviceTc.get(TCategory.class, cid);
		req.setAttribute("child", child);
		req.setAttribute("parents", serviceTc.findParents());
		return "edit2";
	}

	/**
	 * �޸Ķ������ࣺ�ڶ���
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editChild() {
		System.out.println(pid);
		System.out.println(cname);
		System.out.println(desc);
		// ���
		TCategory child = new TCategory();
		child.setCategoryName(cname);
		child.setCategoryDesc(desc);
		child.setCategoryId(cid);
		// ���
		TCategory parent = new TCategory();
		parent.setCategoryId(pid);
		child.setTCategory(parent);
		serviceTc.update(child);
		return findAll();
	}

	/**
	 * ɾ��һ������
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deleteParent() {
		/*
		 * 1. ��ȡ���Ӳ���cid������һ��1�������id 2. ͨ��cid���鿴�ø��������ӷ���ĸ��� 3.
		 * ��������㣬˵�������ӷ��࣬����ɾ�������������Ϣ��ת����msg.jsp 4. ��������㣬ɾ��֮�����ص�list.jsp
		 */
		int cnt = serviceTc.findChildrenCountByParent(pid);
		if (cnt > 0) {
			req.setAttribute("msg", "�÷����»����ӷ��࣬����ɾ����");
			return "msg";
		} else {
			String sql = "delete from t_category where category_id=?";
			serviceTc.executeSQL(sql, pid);
			return findAll();
		}
	}
	
	/**
	 * ɾ��2������
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deleteChild() {
		/*
		 * 1. ��ȡcid����2������id
		 * 2. ��ȡ�÷����µ�ͼ�����
		 * 3. ��������㣬���������Ϣ��ת����msg.jsp
		 * 4. ��������㣬ɾ��֮�����ص�list.jsp
		 */
		int cnt = serviceBo.findBookCountByCategory(cid);
		System.out.println(cnt);
		if(cnt > 0) {
			req.setAttribute("msg", "�÷����»�����ͼ�飬����ɾ����");
			return "msg";
		} else {
			String sql = "delete from t_category where category_id=?";
			serviceTc.executeSQL(sql, cid);
			return findAll();
		}
	}
	
	
}
