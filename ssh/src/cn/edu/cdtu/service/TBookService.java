package cn.edu.cdtu.service;

import java.util.List;

import cn.edu.cdtu.entity.TBook;
import cn.edu.cdtu.page.Expression;
import cn.edu.cdtu.page.PageBean;


public interface TBookService extends BaseService<TBook> {
	/**
	 * ͨ�õĲ�ѯ����
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws SQLException 
	 */
	public  PageBean<TBook> findByCriteria(List<Expression> exprList, int pc);
	/**
	 * ������ģ����ѯ
	 * @param bname
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<TBook> findByBname(String bname, int pc);
	/**
	 * ���������
	 * @param press
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<TBook> findByPress(String press, int pc);

/**
 * �������ѯ,������ұ�ͼ��ʱ
 * @param cid
 * @param pc
 * @return
 * @throws SQLException 
 */
public PageBean<TBook> findByCategory(String cid, int pc);
/**
 * ��bookid��ѯ����ʾ��ϸ��Ϣ
 * @param bid
 * @return
 * @throws SQLException
 */
public TBook findByBid(String bid);

/**
 * �����߲�
 * @param bname
 * @param pc
 * @return
 * @throws SQLException
 */
public PageBean<TBook> findByAuthor(String author, int pc);
/**
 * ��������ϲ�ѯ
 * @param combination
 * @param pc
 * @return
 * @throws SQLException
 */
public PageBean<TBook> findByCombination(String name,String author,String press, int pc);
/**
 * ��ѯָ��������ͼ��ĸ���
 * @param cid
 * @return
 * @throws SQLException
 */
public int findBookCountByCategory(String cid);
}

