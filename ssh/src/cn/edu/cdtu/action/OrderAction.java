package cn.edu.cdtu.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.cdtu.entity.TBook;
import cn.edu.cdtu.entity.TCartitem;
import cn.edu.cdtu.entity.TOrder;
import cn.edu.cdtu.entity.TOrderitem;
import cn.edu.cdtu.entity.TUser;
import cn.edu.cdtu.page.PageBean;
import cn.edu.cdtu.service.TBookService;
import cn.edu.cdtu.service.TCartitemService;
import cn.edu.cdtu.service.TOrderService;
import cn.edu.cdtu.service.TOrderitemService;

@Controller("orderAction")
@Scope("prototype")
public class OrderAction {
	HttpServletResponse resp = ServletActionContext.getResponse();
	HttpServletRequest req = ServletActionContext.getRequest();
	@Resource
	private TOrderService service;
	@Resource
	private TCartitemService serviceCar;
	@Resource
	private TBookService serviceBo;
	@Resource
	private TOrderitemService serviceTo;
	
	private String orderId;// ��ȡorderId
	private String btn;// btn˵�����û�����ĸ������������ʱ�������;
	private String cartItemIds;

	private String address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCartItemIds() {
		return cartItemIds;
	}

	public void setCartItemIds(String cartItemIds) {
		this.cartItemIds = cartItemIds;
	}

	public String getBtn() {
		return btn;
	}

	public void setBtn(String btn) {
		this.btn = btn;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public String myOrders() {

		// 1. �õ�pc�����ҳ�洫�ݣ�ʹ��ҳ��ģ����û����pc=1

		int pc = getPc();

		// * 2. �õ�url��...

		String url = getUrl();

		// * 3. �ӵ�ǰsession�л�ȡUser

		TUser user = (TUser) req.getSession().getAttribute("sessionUser");
		// * 4. ʹ��pc��cid����service#findByCategory�õ�PageBean

		PageBean<TOrder> pb = service.findByUser(user.getUserId(), pc);

		// * 5. ��PageBean����url������PageBean��ת����/jsps/book/list.jsp

		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "list";
	}

	/**
	 * ���ض���,��ʵ���ǲ鿴������ϸ��Ϣ
	 */
	public String load() {
		TOrder order = service.load(orderId);
		req.setAttribute("order", order);
		req.setAttribute("btn", btn);// �õ������ť��״̬
		return "desc";
	}

	/**
	 * ȡ������
	 * 
	 * @return
	 */
	public String cancel() {
		/*
		 * У�鶩��״̬
		 */
		int status = service.findStatus(orderId);
		if (status != 1) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "״̬���ԣ�����ȡ����");
			return "msg";
		}
		service.updateStatus(orderId, 5);// ����״̬Ϊȡ����
		req.setAttribute("code", "success");
		req.setAttribute("msg", "���Ķ�����ȡ�������������");
		return "msg";
	}

	/**
	 * ȷ���ջ�
	 * 
	 * @return
	 */
	public String confirm() {
		/*
		 * У�鶩��״̬
		 */
		int status = service.findStatus(orderId);
		if (status != 3) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "״̬���ԣ�����ȷ���ջ���");
			return "msg";
		}
		service.updateStatus(orderId, 4);// ����״̬Ϊȡ����
		req.setAttribute("code", "success");
		req.setAttribute("msg", "��ϲ�����׳ɹ���");
		return "msg";
	}

	/**
	 * ֧��׼��
	 */
	public String paymentPre() {
		req.setAttribute("order", service.load(orderId));
		return "pay";
	}

	/**
	 * ���ɶ���
	 */
	public String createOrder() {
		/*
		 * 1. ��ȡ���й��ﳵ��Ŀ��id����ѯ֮
		 */
		Object[] cartItemIdArray = cartItemIds.split(",");
		List<TCartitem> cartItemList = serviceCar
				.loadCartItems(cartItemIdArray);
		if (cartItemList.size() == 0) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "��û��ѡ��Ҫ�����ͼ�飬�����µ���");
			return "msg";
		}
		/*
		 * 2. ����Order
		 */
		String time=String.format("%tF %<tT", new Date());
		TOrder order = new TOrder();
		order.setOrderAddress(address);
		order.setOrderTime(time);// �µ�ʱ��
		order.setOrderStatus(1);// ����״̬��1��ʾδ����
		TUser owner = (TUser) req.getSession().getAttribute("sessionUser");
		order.setTUser(owner);// ���ö���������
		BigDecimal total = new BigDecimal("0");
		for (TCartitem cartItem : cartItemList) {
			TBook tbook = serviceBo.get(TBook.class, cartItem.getTBook()
					.getBookId());
			BigDecimal b1 = new BigDecimal(tbook.getBookCurrPrice() + "");
			BigDecimal b2 = new BigDecimal(cartItem.getCartitemCount() + "");
			BigDecimal b3 = b1.multiply(b2);
			total = total.add(b3);
		}
		order.setOrderTotal(total);// �����ܼ�
		/*
		 * 3. ����List<OrderItem> һ��CartItem��Ӧһ��OrderItem
		 */
		List<TOrderitem> orderItemList = new ArrayList<TOrderitem>();
		for (TCartitem cartItem : cartItemList) {
			TBook tbook = serviceBo.get(TBook.class, cartItem.getTBook()
					.getBookId());
			BigDecimal b1 = new BigDecimal(tbook.getBookCurrPrice() + "");
			BigDecimal b2 = new BigDecimal(cartItem.getCartitemCount() + "");
			BigDecimal b3 = b1.multiply(b2);
			TOrderitem orderItem = new TOrderitem();
			orderItem.setOrderitemCount(cartItem.getCartitemCount());
			orderItem.setOrderitemSubtotal(b3);
			orderItem.setTBook(cartItem.getTBook());
			orderItem.setTOrder(order);
			orderItemList.add(orderItem);
		}
		order.setOrderItemList(orderItemList);
		/*
		 * 4. ����service������
		 */
		String sql="select * from t_order where order_time=?";
		service.add(order);
		List<TOrder>  orderid=service.executeSQLQuery(TOrder.class, sql, time);
		/**
		 * ���ɶ�����
		 */
		
		/*
		 * 2. ѭ������������������Ŀ,��ÿ����Ŀ����һ��Object[] �����Ŀ�Ͷ�ӦObject[][] ִ����������ɲ��붩����Ŀ
		 */
		int len = order.getOrderItemList().size();
		for (int i = 0; i < len; i++) {
			TOrderitem item = order.getOrderItemList().get(i);
			TBook tbook = serviceBo.get(TBook.class, item.getTBook()
					.getBookId());
			TOrderitem ord=new TOrderitem();
			ord.setTBook(tbook);
			System.out.println("-----");
			System.out.println(orderid.get(0).getOrderId()+"----------------");
			ord.setTOrder(orderid.get(0));
			ord.setOrderitemImage(tbook.getBookImageB());
			ord.setOrderitemName(tbook.getBookName());
			ord.setOrderitemCount(item.getOrderitemCount());
			ord.setOrderitemPrice(tbook.getBookCurrPrice());
			ord.setOrderitemSubtotal(tbook.getBookCurrPrice());
			serviceTo.save(ord);
		}

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*
		 * 5. ���涩����ת����ordersucc.jsp
		 */
		req.setAttribute("order", order);
		return "ordersucc";
	}

}
