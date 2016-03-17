package cn.edu.cdtu.service.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.edu.cdtu.dao.BaseDao;
import cn.edu.cdtu.entity.TCategory;
import cn.edu.cdtu.service.BaseService;

public abstract class BaseServiceImpl<T> implements BaseService<T> {
	private BaseDao<T> baseDao;

	@Resource
	public void setBaseDao(BaseDao<T> baseDao) {
		this.baseDao = baseDao;
	}

	private Class<T> clazz;

	@SuppressWarnings("unchecked")
	public BaseServiceImpl() {
		ParameterizedType type = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		clazz = (Class<T>) type.getActualTypeArguments()[0];
	}

	@Override
	public T get(Class<T> entity, String id) {
		return (T) baseDao.get(entity, id);
	}

	@Override
	public void save(T t) {
		baseDao.save(t);
	}

	@Override
	public void update(T t) {
		baseDao.update(t);
	}

	@Override
	public void delete(T t) {

	}

	@Override
	public List<T> findEntityHql(String hql, Object... objects) {
		return baseDao.findEntityHql(hql, objects);
	}

	// �õ�ʵ��
	public T getEntity(String id) {
		return (T) baseDao.getEntity(id);
	}

	// ִ��sqlԭ����ѯ
	public List executeSQLQuery(Class clazz, String sql, Object... objects) {
		return baseDao.executeSQLQuery(clazz, sql, objects);
	}
	// ִ��ԭ����sql���
		public void executeSQL(String sql, Object... objects) {
			baseDao.executeSQL(sql, objects);
		}
		/**
		 * ����HQL��������������
		 */
		public void batchEntityByHQL(String hql, Object... objects) {
			baseDao.batchEntityByHQL(hql, objects);
		}
}
