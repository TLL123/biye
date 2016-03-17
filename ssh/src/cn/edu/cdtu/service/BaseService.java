package cn.edu.cdtu.service;

import java.util.List;

import cn.edu.cdtu.entity.TCategory;


public interface BaseService<T> {
	/**
	 * �����д����
	 * 
	 * @param t
	 */
	
	public T get(Class<T> entity, String id);
	public void save(T t);
	
	public void update(T t);

	public void delete(T t);
	/**
	 * ������
	 * 
	 */
	public List<T> findEntityHql(String hql, Object... objects);
	public T getEntity(String id);
	//ִ��ԭ����sql��ѯ
	public void executeSQL(String sql, Object... objects);
	public List executeSQLQuery(Class clazz,String sql,Object...objects);
	//��������
	public void batchEntityByHQL(String hql, Object... objects);

}
