package cn.edu.cdtu.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.cdtu.entity.TBook;
import cn.edu.cdtu.entity.TBookAndTCartitem;
import cn.edu.cdtu.entity.TCartitem;
import cn.edu.cdtu.entity.TUser;
import cn.edu.cdtu.service.TBookService;
import cn.edu.cdtu.service.TCartitemService;

@Controller
@Scope("prototype")
public class CartItemAction {

	HttpServletResponse resp = ServletActionContext.getResponse();
	HttpServletRequest req = ServletActionContext.getRequest();
	@Resource
	private TCartitemService service;
	@Resource
	private TBookService serviceTb;
	private String bookId;
	private int quantity;
	private double total;

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	private String cartItemId;

	public String getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	/**
	 * �ҵĹ��ﳵ����ʾ�ҵĹ��ﳵ��������Ϣ�����ﳵ��Ŀ
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myCart() {
		/*
		 * 1. �õ�uid
		 */
		TUser user = (TUser) req.getSession().getAttribute("sessionUser");
		String uid = user.getUserId();
		/*
		 * 2. ͨ��service�õ���ǰ�û������й��ﳵ��Ŀ
		 */
		String sql = "select book_name,book_currPrice,cartitem_count,book_image_b,cartitem_id from t_cartitem , t_book  where t_cartitem.book_id=t_book.book_id and user_id=?";
		List cartItemLIst = service.executeSQLQuery(null, sql, uid);
		List<TBookAndTCartitem> lis = new ArrayList<TBookAndTCartitem>();
		Iterator it = cartItemLIst.iterator();
		TBookAndTCartitem s = null;
		while (it.hasNext()) {
			Object[] tuple = (Object[]) it.next();
			String bookName = (String) tuple[0];
			BigDecimal bookCurrPrice = (BigDecimal) tuple[1];
			Integer cartitemCount = (Integer) tuple[2];
			String bookImageB = (String) tuple[3];
			String cartitemId = (String) tuple[4];
			s = new TBookAndTCartitem(bookName, bookCurrPrice, cartitemCount,
					cartitemId, bookImageB);
			lis.add(s);
		}
		/*
		 * 3. ����������ת����/cart/list.jsp
		 */
		req.setAttribute("cartItemList", lis);
		return "list";
	}

	/**
	 * ��ӹ��ﳵ��Ŀ������鱾��
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add() {
		/*
		 * 1. ��װ�����ݵ�CartItem(bid, quantity)
		 */
		TCartitem cartItem = new TCartitem();
		TBook book = new TBook();
		book.setBookId(bookId);
		TUser user = (TUser) req.getSession().getAttribute("sessionUser");
		cartItem.setTBook(book);
		;
		cartItem.setTUser(user);
		cartItem.setCartitemCount(quantity);
		/*
		 * 2. ����service������
		 */
		service.add(cartItem);
		/*
		 * 3. ��ѯ����ǰ�û���������Ŀ��ת����list.jsp��ʾ
		 */
		return myCart();
	}

	/**
	 * ���첽ˢϴ�������ݿ��е��������
	 * 
	 * @return
	 */
	public String updateQuantity() {
		// �Ȳ�ѯ����bookid��userid�ŵ�cart��
		service.updateQuantity(cartItemId, quantity);
		TCartitem cartItem = service.get(TCartitem.class, cartItemId);// Ȼ���ڲ�ѯ
		TBook book = serviceTb
				.get(TBook.class, cartItem.getTBook().getBookId());
		BigDecimal b1 = new BigDecimal(book.getBookCurrPrice() + "");
		BigDecimal b2 = new BigDecimal(cartItem.getCartitemCount() + "");
		BigDecimal b3 = b1.multiply(b2);
		// ���ͻ��˷���һ��json����
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"quantitys\"").append(":")
				.append(cartItem.getCartitemCount());
		sb.append(",");
		sb.append("\"subtotals\"").append(":").append(b3);
		sb.append("}");
		try {
			resp.getWriter().print(sb);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ���ﳵ��Ŀ��ɾ������
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deleteCart() {
		/*
		 * 1. ��ȡcartItemIds���� 2. ����service������ɹ��� 3. ���ص�list.jsp
		 */
		String sql = "delete from t_cartitem where cartitem_id=?";
		service.executeSQL(sql, cartItemId);
		/*
		 * TCartitem cart=service.get(TCartitem.class, cartItemId);
		 * System.out.println(cart.getCartitemCount());
		 * System.out.println(cart.getCartitemId());
		 * System.out.println(cart.getCartitemOrderBy()); service.delete(cart);
		 */
		return myCart();
	}

	/**
	 * ���ض��CartItem
	 * 
	 */
	public String loadCartItems() {
		Object[] cartItemIdArray = cartItemId.split(",");
		List<TCartitem> cartItemList = service.loadCartItems(cartItemIdArray);// ��ͼ���ѯ����
		for (int i = 0; i < cartItemList.size(); i++) {
			TBook book = serviceTb.get(TBook.class, cartItemList.get(i)
					.getTBook().getBookId());
			cartItemList.get(i).setTBook(book);
		}
		/*
		 * 3. ���棬Ȼ��ת����/cart/showitem.jsp
		 */
		req.setAttribute("cartItemList", cartItemList);
		req.setAttribute("total", total);
		req.setAttribute("cartItemIds", cartItemId);
		return "showitem";
	}
}
