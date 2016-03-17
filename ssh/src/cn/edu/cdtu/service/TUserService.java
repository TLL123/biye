package cn.edu.cdtu.service;

import cn.edu.cdtu.entity.TUser;

public interface TUserService extends BaseService<TUser> {
	/**
	 * ��¼ʱ�ж��Ƿ������ݿ���
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	public TUser validateLoginInfo(String name, String password);

	/**
	 * �ж��Ƿ�ע��
	 */
	public TUser isRegisted(String name);

}
