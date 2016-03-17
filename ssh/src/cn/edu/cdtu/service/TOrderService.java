package cn.edu.cdtu.service;

import cn.edu.cdtu.entity.TOrder;
import cn.edu.cdtu.page.PageBean;

public interface TOrderService extends BaseService<TOrder>{
	/**
	 * ���û���ѯ����
	 * @param uid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<TOrder> findByUser(String uid, int pc) ;
	/**
	 * ���ض���,��ʵ���ǲ鿴������ϸ��Ϣ
	 * @param oid
	 * @return
	 */
	public TOrder load(String oid);
	
	/**
	 * �޸Ķ���״̬
	 * @param oid
	 * @param status
	 */
	public void updateStatus(String oid, int status);
	/**
	 * ��ѯ����״̬
	 * @param oid
	 * @return
	 */
	public int findStatus(String oid);
	/**
	 * ���ɶ���
	 */
	public void add(TOrder order);
}
