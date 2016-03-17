package cn.edu.cdtu.action;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.cdtu.entity.TBook;
import cn.edu.cdtu.entity.TCategory;
import cn.edu.cdtu.service.TBookService;
import cn.edu.cdtu.service.TCategoryService;

@Controller
@Scope("prototype")
public class AdminAddBookAction {
	HttpServletRequest req = ServletActionContext.getRequest();
	@Resource
	private TCategoryService serviceTc;
	@Resource
	private TBookService service;
	private File bookImageS; // ��ȡ�ϴ��ļ�
	private File bookImageB; // ��ȡ�ϴ��ļ�
	private String bookImageSFileName;
	private String bookImageBFileName;
	private String cid;
	private String pid;
	TBook book;
	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}



	public TBook getBook() {
		return book;
	}

	public void setBook(TBook book) {
		this.book = book;
	}

	public String getBookImageSFileName() {
		return bookImageSFileName;
	}

	public void setBookImageSFileName(String bookImageSFileName) {
		this.bookImageSFileName = bookImageSFileName;
	}

	public String getBookImageBFileName() {
		return bookImageBFileName;
	}

	public void setBookImageBFileName(String bookImageBFileName) {
		this.bookImageBFileName = bookImageBFileName;
	}

	public File getBookImageS() {
		return bookImageS;
	}

	public void setBookImageS(File bookImageS) {
		this.bookImageS = bookImageS;
	}

	public File getBookImageB() {
		return bookImageB;
	}

	public void setBookImageB(File bookImageB) {
		this.bookImageB = bookImageB;
	}

	/**
	 * ���ͼ��
	 * 
	 * @return
	 */
	public String addBookTwo() {
		if (bookImageSFileName != null && bookImageBFileName != null) {
			String realpath = ServletActionContext.getServletContext()
					.getRealPath("/book_img");
			File savefile = new File(new File(realpath), bookImageBFileName);
			File savefile2 = new File(new File(realpath), bookImageSFileName);
			if (!savefile.getParentFile().exists())// �жϸ��ļ����Ƿ����
				savefile.getParentFile().mkdirs();

			try {
				FileUtils.copyFile(bookImageB, savefile);
				FileUtils.copyFile(bookImageS, savefile2);
				// ���ļ�copy�����Ŀ¼
				// ����ɹ���Ϣת����msg.jsp
			book.setBookImageB("book_img/"+bookImageBFileName);
			book.setBookImageS("book_img/"+bookImageSFileName);
			TCategory catetegory=new TCategory();
			catetegory.setCategoryId(pid);
			book.setTCategory(catetegory);
			service.save(book);
			req.setAttribute("msg", "���ͼ��ɹ���");
			return "msg";
			} catch (IOException e) {
				//��ʾ���е�һ������
				req.setAttribute("parents", serviceTc.findParents());//����һ������
				req.setAttribute("msg", "���ͼƬʧ�ܣ�");
				return "add";
			}
		} else {
			//��ʾ���е�һ������
			req.setAttribute("parents", serviceTc.findParents());//����һ������
			req.setAttribute("msg", "���ͼƬʧ�ܣ�");
			return "add";
		}

	}

}
