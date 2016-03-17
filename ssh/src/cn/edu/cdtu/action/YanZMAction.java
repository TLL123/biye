package cn.edu.cdtu.action;

import java.io.ByteArrayInputStream;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.cdtu.utils.RandomNumUtil;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class YanZMAction extends ActionSupport{
	private ByteArrayInputStream inputStream;
	public void setInputStream(ByteArrayInputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public ByteArrayInputStream getInputStream() {
		return inputStream;
	}

	/**
	 * ������֤��
	 */
	public String randomIamge() throws Exception {
		RandomNumUtil rdnu = RandomNumUtil.Instance();
		this.setInputStream(rdnu.getImage());// ȡ�ô�������ַ�����ͼƬ
		ActionContext.getContext().getSession()
				.put("sessionCode", rdnu.getString());// ȡ������ַ�������HttpSession
	
		return SUCCESS;
	}
}
