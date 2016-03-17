package cn.edu.cdtu.action;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.cdtu.entity.TBook;
import cn.edu.cdtu.entity.TCategory;
import cn.edu.cdtu.page.PageBean;
import cn.edu.cdtu.service.TBookService;
import cn.edu.cdtu.service.TCategoryService;

@Controller
@Scope("prototype")
public class AdminBookAction {
	HttpServletRequest req = ServletActionContext.getRequest();
	HttpServletResponse rep=ServletActionContext.getResponse();
	@Resource
	private TCategoryService serviceTc;
	@Resource
	private TBookService service;
	///////////////////////////////////
	private String bookName;
	private String categoryId;
	private String bookId;
	private String bookAuthor;
	private String bookPress;
	private String pid;
	public String getPid() {
		return pid;
	}


	public void setPid(String pid) {
		this.pid = pid;
	}


	///////////////////////////////////
	public String getBookAuthor() {
		return bookAuthor;
	}


	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}


	public String getBookPress() {
		return bookPress;
	}


	public void setBookPress(String bookPress) {
		this.bookPress = bookPress;
	}


	public String getBookName() {
		return bookName;
	}


	public void setBookName(String bookName) {
		this.bookName = bookName;
	}


	public String getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}


	public String getBookId() {
		return bookId;
	}


	public void setBookId(String bookId) {
		this.bookId = bookId;
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
	public String findAlls() {
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
		return "left";
	}


	/**
	 * ��ȡ��ǰҳ��
	 * 
	 * @param req
	 * @return
	 */
	private int getPc() {
		int pc = 1;
		String param = req.getParameter("pc");
		if (param != null && !param.trim().isEmpty()) {
			try {
				pc = Integer.parseInt(param);
			} catch (RuntimeException e) {
			}
		}
		return pc;
	}

	/**
	 * ��ȡurl��ҳ���еķ�ҳ��������Ҫʹ������Ϊ�����ӵ�Ŀ�꣡
	 * 
	 * @param req
	 * @return
	 */
	/*
	 * http://localhost:8080/goods/BookServlet?methed=findByCategory&cid=xxx&pc=3
	 * /goods/BookServlet + methed=findByCategory&cid=xxx&pc=3
	 */
	private String getUrl() {
		String url = req.getRequestURI() + "?" + req.getQueryString();
		/*
		 * ���url�д���pc��������ȡ��������������ǾͲ��ý�ȡ��
		 */
		int index = url.lastIndexOf("&pc=");
		if (index != -1) {
			url = url.substring(0, index);
		}
		return url;
	}

	/**
	 * �������,���ҵ�����ʱ�÷���id��ѯ��
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCategory() {
		/*
		 * 1. �õ�pc�����ҳ�洫�ݣ�ʹ��ҳ��ģ����û����pc=1
		 */
		int pc = getPc();
		/*
		 * 2. �õ�url��...
		 */
		String url = getUrl();
		/*
		 * 4. ʹ��pc��cid����service#findByCategory�õ�PageBean
		 */

		PageBean<TBook> pb = service.findByCategory(categoryId, pc);
		/*
		 * 5. ��PageBean����url������PageBean��ת����/jsps/book/list.jsp
		 */
		// System.out.println(pb.get);
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "list";
	}

	/**
	 * ���������ѯ
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByPress() {
		/*
		 * 1. �õ�pc�����ҳ�洫�ݣ�ʹ��ҳ��ģ����û����pc=1
		 */
		int pc = getPc();
		/*
		 * 2. �õ�url��...
		 */
		String url = getUrl();
		/*
		 * 3. ��ȡ��ѯ����������������cid���������id
		 */
		/*
		 * 4. ʹ��pc��cid����service#findByCategory�õ�PageBean
		 */

		PageBean<TBook> pb = service.findByPress(bookPress, pc);
		/*
		 * 5. ��PageBean����url������PageBean��ת����/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "list";
	}

	/**
	 * ��ͼ���飬���listҳ���ϵ�����ʱ������ѯ
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByBname() {
		/*
		 * 1. �õ�pc�����ҳ�洫�ݣ�ʹ��ҳ��ģ����û����pc=1
		 */
		int pc = getPc();
		/*
		 * 2. �õ�url��...
		 */
		String url = getUrl();
		/*
		 * 3. ��ȡ��ѯ����������������cid���������id
		 */
		// String bname = req.getParameter("bname");
		/*
		 * 4. ʹ��pc��cid����service#findByCategory�õ�PageBean
		 */
		PageBean<TBook> pb = service.findByBname(bookName, pc);
		/*
		 * 5. ��PageBean����url������PageBean��ת����/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "list";
	}

	/**
	 * ��bid��ѯ
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByBid() {
		TBook book = service.findByBid(bookId);// ͨ��bid�õ�book����
		req.setAttribute("book", book);// ���浽req��
		return "desc";// ת����desc.jsp
	}

	/**
	 * �����߲�
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByAuthor() {
		/*
		 * 1. �õ�pc�����ҳ�洫�ݣ�ʹ��ҳ��ģ����û����pc=1
		 */
		int pc = getPc();
		/*
		 * 2. �õ�url��...
		 */
		String url = getUrl();
		/*
		 * 3. ��ȡ��ѯ����������������cid���������id
		 */
		/*
		 * 4. ʹ��pc��cid����service#findByCategory�õ�PageBean
		 */
		System.out.println(bookAuthor);
		PageBean<TBook> pb = service.findByAuthor(bookAuthor, pc);
		/*
		 * 5. ��PageBean����url������PageBean��ת����/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "list";
	}
	
	

	/**
	 * ��������ϲ�ѯ,��ν�߼���ѯ
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCombination(){
		/*
		 * 1. �õ�pc�����ҳ�洫�ݣ�ʹ��ҳ��ģ����û����pc=1
		 */
		int pc = getPc();
		/*
		 * 2. �õ�url��...
		 */
		String url = getUrl();
		PageBean<TBook> pb = service.findByCombination(bookName, bookAuthor, bookPress, pc);
		/*
		 * 5. ��PageBean����url������PageBean��ת����/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "list";
	}
	
	

	/**
	 * ���ͼ�飺��һ��,��һ��������ʾ��add.jsp��
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addPre() {
		/*
		 * 1. ��ȡ����һ�����࣬����֮
		 * 2. ת����add.jsp����ҳ����������б�����ʾ����һ������
		 */
		/*
		 * 1. ͨ��service�õ����еķ��� 2. ���浽request�У�ת����left.jsp
		 */
		List<TCategory> parents = serviceTc.findAll();
	
		/*
		 * 2. ѭ���������е�һ�����࣬Ϊÿ��һ������������Ķ�������
		 */
		for (TCategory parent : parents) {
			// ��ѯ����ǰ������������ӷ���
			List<TCategory> childrens = serviceTc.findByParent(parent.getCategoryId());
			System.out.println(childrens.size());
				parent.setCategory(childrens);
		}
		req.setAttribute("parents", parents);
		return "add";
	}
	/**
	 * ͨ��Ajax��ʾһ�����������ж�������
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxFindChildren() throws IOException {
		/*
		 * 1. ��ȡpid
		 * 2. ͨ��pid��ѯ������2������
		 * 3. ��List<Category>ת����json��������ͻ���
		 */
		List<TCategory> children = serviceTc.findByParent(pid);
		System.out.println(children.get(0).getCategoryName()+"----------------------------------");
		String json = toJson(children);
		rep.getWriter().print(json);
		return null;
	}
	/**
	 * ת����json��ʽ
	 * @param categoryList
	 * @return
	 */
	private String toJson(List<TCategory> categoryList) {
		StringBuilder sb = new StringBuilder("[");
		for(int i = 0; i < categoryList.size(); i++) {
			sb.append(toJson(categoryList.get(i)));
			if(i < categoryList.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	/**
	 * ת��Ϊjson��ʽ2
	 * @param category
	 * @return
	 */
	private String toJson(TCategory category) {
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"categoryId\"").append(":").append("\"").append(category.getCategoryId()).append("\"");
		sb.append(",");
		sb.append("\"categoryName\"").append(":").append("\"").append(category.getCategoryName()).append("\"");
		sb.append("}");
		return sb.toString();
	}
}
