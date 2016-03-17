package cn.edu.cdtu.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.cdtu.dao.BaseDao;

@Transactional
public abstract class BaseDaoImpl<T> implements BaseDao<T> {
	// ע��sessionFactory
	@Resource
	private SessionFactory sessionFactory;

	private Class<T> clazz;

	public BaseDaoImpl() {
		// �õ����ͻ�����
		ParameterizedType type = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		clazz = (Class<T>) type.getActualTypeArguments()[0];
	}
	public T get(Class<T> entity,String id)
	{
		return (T)sessionFactory.getCurrentSession().get(entity, id);
	}
	@Override
	public void save(T t) {
		sessionFactory.getCurrentSession().save(t);
	}

	@Override
	public void update(T t) {
		sessionFactory.getCurrentSession().update(t);
	}

	@Override
	public void delete(T t) {
		sessionFactory.getCurrentSession().delete(t);
	}
	/**
	 * ����ʵ�������ݿ����Ƿ����
	 * @param hql
	 * @param objects
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findEntityHql(String hql, Object... objects) {
		Query q = sessionFactory.getCurrentSession().createQuery(hql);
		for (int i = 0; i < objects.length; i++) {
			q.setParameter(i, objects[i]);
		}
		return q.list();
	}
	
	public T getEntity(String id) {
		return (T) sessionFactory.getCurrentSession().get(clazz, id);
	}
	
	/**
	 * ����HQL��������������
	 */
	public void batchEntityByHQL(String hql, Object... objects) {
		Query q = sessionFactory.getCurrentSession().createQuery(hql);
		for (int i = 0; i < objects.length; i++) {
			q.setParameter(i, objects[i]);
		}
		q.executeUpdate();
	}

	// ִ��ԭ����sql���
	public void executeSQL(String sql, Object... objects) {
		SQLQuery q = sessionFactory.getCurrentSession().createSQLQuery(sql);
		for (int i = 0; i < objects.length; i++) {
			q.setParameter(i, objects[i]);
		}
		q.executeUpdate();
	}
	// ִ��ԭ����sql��ѯ
		public List executeSQLQuery(Class clazz, String sql, Object... objects) {
			SQLQuery q = sessionFactory.getCurrentSession().createSQLQuery(sql);
			// ���ʵ����
			if (clazz != null) {
				q.addEntity(clazz);
			}
			for (int i = 0; i < objects.length; i++) {
				q.setParameter(i, objects[i]);
			}
			return q.list();
		}
}
