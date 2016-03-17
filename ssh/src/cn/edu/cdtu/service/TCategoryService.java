package cn.edu.cdtu.service;

import java.util.List;

import cn.edu.cdtu.entity.TCategory;

public interface TCategoryService extends BaseService<TCategory> {
	public List<TCategory> findAll();
	public List<TCategory> findByParent(String pid);
	/**
	 * ��ӷ���
	 * @param category
	 * @throws SQLException 
	 */
	public void add(TCategory category);
	public List<TCategory> findParents();//�鿴һ�����
	/**
	 * ��ѯָ�����������ӷ���ĸ���,����ɾ��
	 * @param pid
	 * @return
	 * @throws SQLException 
	 */
	public int findChildrenCountByParent(String pid);
}
