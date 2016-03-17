package cn.edu.cdtu.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.edu.cdtu.dao.BaseDao;
import cn.edu.cdtu.entity.TCartitem;
import cn.edu.cdtu.service.TCartitemService;

@Service
// @Scope("prototype")
public class TCartitemServiceImpl extends BaseServiceImpl<TCartitem> implements
		TCartitemService {
	@Resource(name = "tCartitemDao")
	public void setBaseDao(BaseDao<TCartitem> baseDao) {
		super.setBaseDao(baseDao);
	}

	/**
	 * ��ѯĳ���û���ĳ��ͼ��Ĺ��ﳵ��Ŀ�Ƿ����
	 * 
	 * @throws SQLException
	 */
	public TCartitem findByUidAndBid(String uid, String bid) {
		String sql = "select * from t_cartitem where user_id=? and book_id=?";
		List<TCartitem> cartItem = this.executeSQLQuery(TCartitem.class, sql,
				uid, bid);
		if (cartItem.size() > 0)
			return cartItem.get(0);
		else
			return null;

	}

	/**
	 * �����Ŀ
	 * 
	 * @param cartItem
	 */
	public void add(TCartitem cartItem) {
		try {
			/*
			 * 1. ʹ��uid��bidȥ���ݿ��в�ѯ�����Ŀ�Ƿ����
			 */
			TCartitem _cartItem = findByUidAndBid(cartItem.getTUser()
					.getUserId(), cartItem.getTBook().getBookId());
			if (_cartItem == null) {// ���ԭ��û�������Ŀ����ô�����Ŀ
				this.save(cartItem);
			} else {// ���ԭ���������Ŀ���޸�����
				// ʹ��ԭ������������Ŀ����֮��������Ϊ�µ�����
				int quantity = cartItem.getCartitemCount()
						+ _cartItem.getCartitemCount();
				cartItem.setCartitemCount(quantity);
				cartItem.setCartitemId(_cartItem.getCartitemId());
				// �޸��������Ŀ������
				this.update(cartItem);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * �޸�ָ����Ŀ������
	 * 
	 * @param cartItemId
	 * @param quantity
	 * @throws SQLException
	 */
	public void updateQuantity(String cartItemId, int quantity) {
		String sql = "update t_cartitem set cartitem_count=? where cartitem_id=?";
		this.executeSQL(sql, quantity, cartItemId);
	}
	
	/**
	 * ���ض��CartItem
	 */
	public List<TCartitem> loadCartItems(Object[] cartItemIds) {
		String whereSql = toWhereSql(cartItemIds.length);
		String sql = "select * from t_cartitem, t_book where t_cartitem.book_id=t_book.book_id and "+whereSql;
		return this.executeSQLQuery(TCartitem.class, sql, cartItemIds);
	}
	/*
	 * ��������where�Ӿ�
	 */
	private String toWhereSql(int len) {
		StringBuilder sb = new StringBuilder("cartitem_id in(");
		for(int i = 0; i < len; i++) {
			sb.append("?");
			if(i < len - 1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
}
