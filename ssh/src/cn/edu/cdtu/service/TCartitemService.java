package cn.edu.cdtu.service;

import java.util.List;

import cn.edu.cdtu.entity.TBook;
import cn.edu.cdtu.entity.TCartitem;

public interface TCartitemService extends BaseService<TCartitem> {
	/**
	 * �����Ŀ
	 * @param cartItem
	 */
	public void add(TCartitem cartItem);
	/**
	 * �޸�ָ����Ŀ������
	 * @param cartItemId
	 * @param quantity
	 * @throws SQLException 
	 */
	public void updateQuantity(String cartItemId, int quantity);
	/**
	 * ���ض��CartItem
	 * @param cartItemIds
	 * @return
	 * @throws SQLException 
	 */
	public List<TCartitem> loadCartItems(Object[] cartItemIds);
}
