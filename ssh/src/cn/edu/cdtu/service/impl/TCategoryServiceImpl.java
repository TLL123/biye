package cn.edu.cdtu.service.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.cdtu.dao.BaseDao;
import cn.edu.cdtu.entity.TBook;
import cn.edu.cdtu.entity.TCategory;
import cn.edu.cdtu.service.TCategoryService;

@Service
public class TCategoryServiceImpl extends BaseServiceImpl<TCategory> implements
		TCategoryService {
	@Resource(name = "tCategoryDao")
	public void setBaseDao(BaseDao<TCategory> baseDao) {
		super.setBaseDao(baseDao);
	}

	/**
	 * �������з���
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<TCategory> findAll() {
		/*
		 * 1. ��ѯ������һ������
		 */
		String hql = " from TCategory as t where t.TCategory is null order by t.categoryOrderBy";
		List<TCategory> parents = this.findEntityHql(hql);
		
		return parents;
	}

	/**
	 * ͨ���������ѯ�ӷ���
	 * 
	 * @param pid
	 * @return
	 * @throws SQLException
	 */
	public List<TCategory> findByParent(String pid) {
		String hql = "from TCategory as t where t.TCategory.categoryId=?";
		List<TCategory> mapList = this.findEntityHql(hql, pid);
		return mapList;
	}
	
	
	
	/**
	 * ��ӷ���
	 * @param category
	 * @throws SQLException 
	 */
	public void add(TCategory category) {
		this.save(category);
	}
	
	/**
	 * ��ȡ���и����࣬�������ӷ���ģ�
	 * @return
	 * @throws SQLException
	 */
	public List<TCategory> findParents(){
		return findAll();
	}
	/**
	 * ��ѯָ�����������ӷ���ĸ���,����ɾ��
	 * @param pid
	 * @return
	 * @throws SQLException 
	 */
	public int findChildrenCountByParent(String pid){
		String sql = "select * from t_category where t_c_category_id=?";
	
		List<TCategory> number=this.executeSQLQuery(TCategory.class, sql, pid);
		int cnt =number.size();
		return  cnt;//���ظ���
	}
	
	
}
