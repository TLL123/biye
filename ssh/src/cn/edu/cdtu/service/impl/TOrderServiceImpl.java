package cn.edu.cdtu.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.cdtu.dao.BaseDao;
import cn.edu.cdtu.entity.TOrder;
import cn.edu.cdtu.entity.TOrderitem;
import cn.edu.cdtu.page.Expression;
import cn.edu.cdtu.page.PageBean;
import cn.edu.cdtu.page.PageConstants;
import cn.edu.cdtu.service.TOrderService;
import cn.edu.cdtu.service.TOrderitemService;

@Service("tOrderServiceImpl")
public class TOrderServiceImpl extends BaseServiceImpl<TOrder> implements
		TOrderService {
	@Resource(name = "tOrderitemDao")
	public void setBaseDao(BaseDao<TOrder> baseDao) {
		super.setBaseDao(baseDao);
	}

	/*
	 * @Resource private TOrderitemService service;
	 */

	/**
	 * ͨ�õĲ�ѯ����
	 * 
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<TOrder> findByCriteria(List<Expression> exprList, int pc) {
		/*
		 * 1. �õ�ps 2. �õ�tr 3. �õ�beanList 4. ����PageBean������
		 */
		/*
		 * 1. �õ�ps
		 */
		int ps = PageConstants.ORDER_PAGE_SIZE;// ����ÿҳ��¼��
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
			if (!expr.getOperator().equals("is null")) {
				whereSql.append("?");
				params.add(expr.getValue());
			}

		}
		/*
		 * 3. �ܼ�¼��
		 */
		String hql = "select * from t_order" + whereSql;
		System.out.println(hql);
		List<TOrder> number = this.executeSQLQuery(null, hql, params.toArray());
		int tr = number.size();// �õ����ܼ�¼��

		// * 4. �õ�beanList������ǰҳ��¼

		hql = "select * from t_order" + whereSql
				+ " order by order_time desc limit ?,?";
		params.add((pc - 1) * ps);// ��ǰҳ���м�¼���±�
		params.add(ps);// һ����ѯ���У�����ÿҳ��¼��
		List<TOrder> beanList = this.executeSQLQuery(TOrder.class, hql,
				params.toArray());
		// * 5. ����PageBean�����ò���
		// ��Ȼ�Ѿ���ȡ���еĶ�������ÿ�������в�û�ж�����Ŀ��
		// ����ÿ��������Ϊ������������ж�����Ŀ
		for (TOrder order : beanList) {
			loadOrderItem(order);
		}
		PageBean<TOrder> pb = new PageBean<TOrder>();
		// * ����PageBeanû��url�����������Servlet���
		pb.setBeanList(beanList);
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		return pb;
	}

	/**
	 * ���û���ѯ����
	 * 
	 * @param uid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<TOrder> findByUser(String uid, int pc) {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("user_id", "=", uid));
		return findByCriteria(exprList, pc);
	}

	/*
	 * Ϊָ����order����������OrderItem
	 */
	private void loadOrderItem(TOrder order) {
		/*
		 * 1. ��sql���select * from t_orderitem where order_id=? 2.
		 * ִ��֮���õ�List<OrderItem> 3. ���ø�Order����
		 */
		String sql = "select * from t_orderitem where order_id=?";
		List<TOrderitem> mapList = this.executeSQLQuery(TOrderitem.class, sql,
				order.getOrderId());
		order.setOrderItemList(mapList);
	}

	/**
	 * ���ض���,��ʵ���ǲ鿴������ϸ��Ϣ
	 * 
	 * @param oid
	 * @return
	 */
	public TOrder load(String oid) {
		TOrder order = this.get(TOrder.class, oid);
		loadOrderItem(order);// Ϊ��ǰ���������������ж�����Ŀ
		return order;
	}

	/**
	 * ��ѯ����״̬
	 * 
	 * @param oid
	 * @return
	 */
	public int findStatus(String oid) {
		TOrder order = this.get(TOrder.class, oid);
		return order.getOrderStatus();
	}

	/**
	 * �޸Ķ���״̬
	 * 
	 * @param oid
	 * @param status
	 */
	public void updateStatus(String oid, int status) {
		String sql = "update t_order set order_status=? where order_id=?";
		this.executeSQL(sql, status, oid);
	}

	/**
	 * ���ɶ���
	 */
	public void add(TOrder order) {
		/*
		 * 1. ���붩��
		 */
		this.save(order);

	}
}
