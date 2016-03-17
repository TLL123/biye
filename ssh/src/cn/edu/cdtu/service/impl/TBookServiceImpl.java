package cn.edu.cdtu.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.edu.cdtu.dao.BaseDao;
import cn.edu.cdtu.entity.TBook;
import cn.edu.cdtu.page.Expression;
import cn.edu.cdtu.page.PageBean;
import cn.edu.cdtu.page.PageConstants;
import cn.edu.cdtu.service.TBookService;

@Service
// @Scope("prototype")
public class TBookServiceImpl extends BaseServiceImpl<TBook> implements
		TBookService {
	@Resource(name = "tBookDao")
	public void setBaseDao(BaseDao<TBook> baseDao) {
		super.setBaseDao(baseDao);
	}

	/**
	 * ������ģ����ѯ
	 * 
	 * @param bname
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<TBook> findByBname(String bname, int pc) {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("book_name", "like", "%" + bname + "%"));
		return findByCriteria(exprList, pc);
	}

	/**
	 * ���������
	 * 
	 * @param press
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<TBook> findByPress(String press, int pc) {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("book_press", "like", "%" + press + "%"));
		return findByCriteria(exprList, pc);
	}

	/**
	 * ͨ�õĲ�ѯ����
	 * 
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<TBook> findByCriteria(List<Expression> exprList, int pc) {
		/*
		 * 1. �õ�ps 2. �õ�tr 3. �õ�beanList 4. ����PageBean������
		 */
		/*
		 * 1. �õ�ps
		 */
		int ps = PageConstants.BOOK_PAGE_SIZE;// ÿҳ��¼��
		/*
		 * 2. ͨ��exprList������where�Ӿ�
		 */
		StringBuilder whereSql = new StringBuilder(" where 1=1 ");
		List<Object> params = new ArrayList<Object>();// SQL�����ʺţ����Ƕ�Ӧ�ʺŵ�ֵ
		for (Expression expr : exprList) {
			/*
			 * ���һ�������ϣ� 1) ��and��ͷ 2) ���������� 3) �������������������=��!=��>��< ... is null��is
			 * nullû��ֵ 4) �����������is null����׷���ʺţ�Ȼ������params�����һ���ʺŶ�Ӧ��ֵ
			 */
			whereSql.append(" and ").append(expr.getName()).append(" ")
					.append(expr.getOperator()).append(" ");
			// where 1=1 and bid = ?
			if (!expr.getOperator().equals("is null")) {
				whereSql.append("?");
				params.add(expr.getValue());
			}

		}
		// System.out.println(params);
		// System.out.println(whereSql);

		/*
		 * 3. �ܼ�¼��
		 */
		String hql = "select * from t_book" + whereSql;
		List<TBook> number = this.executeSQLQuery(null, hql, params.toArray());
		int tr = number.size();// �õ����ܼ�¼��

		// * 4. �õ�beanList������ǰҳ��¼

		hql = "select * from t_book" + whereSql
				+ " order by book_orderBy limit ?,?";
		params.add((pc - 1) * ps);// ��ǰҳ���м�¼���±�
		params.add(ps);// һ����ѯ���У�����ÿҳ��¼��

		List<TBook> beanList = this.executeSQLQuery(TBook.class, hql,
				params.toArray());
		// * 5. ����PageBean�����ò���

		PageBean<TBook> pb = new PageBean<TBook>();

		// * ����PageBeanû��url�����������Servlet���

		pb.setBeanList(beanList);
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		return pb;
	}

	/**
	 * ����ר��
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		TBookServiceImpl ss = new TBookServiceImpl();
		List<Expression> list = new ArrayList<Expression>();
		list.add(new Expression("bid", "=", "1"));
		list.add(new Expression("bid", "is null", null));
		ss.findByCriteria(list, 10);
	}

	/**
	 * �������ѯ,������ұ�ͼ��ʱ
	 * 
	 * @param cid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<TBook> findByCategory(String cid, int pc) {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("category_id", "=", cid));
		return findByCriteria(exprList, pc);
	}

	/**
	 * ��bookid��ѯ����ʾ��ϸ��Ϣ
	 * 
	 * @param bid
	 * @return
	 * @throws SQLException
	 */
	public TBook findByBid(String bid) {
		System.out.println(bid);
		String sql = "SELECT * FROM t_book , t_category WHERE t_book.category_id=t_category.category_id AND t_book.book_id=?";
		// һ�м�¼�У������˺ܶ��book�����ԣ�����һ��cid����
		List<TBook> beanList = this.executeSQLQuery(TBook.class, sql, bid);
		System.out.println(beanList.size()
				+ "----------------------------------------");
		System.out.println(beanList.get(0).getBookName());
		if (beanList.size() > 0) {
			TBook book = beanList.get(0);
			return book;
		}
		return null;
	}

	/**
	 * �����߲�
	 * 
	 * @param bname
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<TBook> findByAuthor(String author, int pc) {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("book_author", "like", "%" + author + "%"));
		return findByCriteria(exprList, pc);
	}

	
	/**
	 * ��������ϲ�ѯ
	 * @param combination
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<TBook> findByCombination(String name,String author,String press, int pc){
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("book_name", "like", "%" + name + "%"));
		exprList.add(new Expression("book_author", "like", "%" + author + "%"));
		exprList.add(new Expression("book_press", "like", "%" + press + "%"));
		return findByCriteria(exprList, pc);
	}
	/**
	 * ��ѯָ��������ͼ��ĸ���
	 * @param cid
	 * @return
	 * @throws SQLException
	 */
	public int findBookCountByCategory(String cid){
		String sql = "select * from t_book where category_id=?";
		int cnt=this.executeSQLQuery(TBook.class, sql, cid).size();
		return cnt;
	}
}
