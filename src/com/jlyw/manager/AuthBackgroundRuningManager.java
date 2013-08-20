package com.jlyw.manager;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.AuthBackgroundRuning;
import com.jlyw.hibernate.AuthBackgroundRuningDAO;
import com.jlyw.hibernate.VerifyAndAuthorize;
import com.jlyw.util.KeyValueWithOperator;

public class AuthBackgroundRuningManager {
	private static AuthBackgroundRuningManager instance = null;
	/**
	 * 增加一条后台签字任务（同时更新核验和签字记录的标志字段：IsAuthBgRuning）
	 * @param verifyAndAuthorize
	 * @return
	 */
	public static boolean AddAnAuthBackgroundRuning(VerifyAndAuthorize verifyAndAuthorize) throws Exception{
		if(instance == null){
			instance = new AuthBackgroundRuningManager();
		}
		AuthBackgroundRuningDAO dao = instance.getDAO();
		Transaction tran = dao.getSession().beginTransaction();
		try{
			AuthBackgroundRuning s = new AuthBackgroundRuning();
			s.setVerifyAndAuthorize(verifyAndAuthorize);
			s.setCreateTime(new Timestamp(System.currentTimeMillis()));
			dao.save(s);
			verifyAndAuthorize.setIsAuthBgRuning(true);
			dao.update(verifyAndAuthorize);
			tran.commit();
			return true;
		}catch(Exception e){
			tran.rollback();
			throw e;
		}finally{
			dao.closeSession();
		}
	}
	
	
	public static List<AuthBackgroundRuning> findPageAllAuthBackgroundRuningBySort(int currentPage, int pageSize, String orderby, boolean asc, KeyValueWithOperator...arr) throws Exception{
		if(instance == null){
			instance = new AuthBackgroundRuningManager();
		}
		return instance.findPagedAllBySort(currentPage, pageSize, orderby, asc, arr);
	}
	
	
	/**
	 * 移除一条后台签字的任务
	 * @param info
	 * @param doneSuccess:是否后台签字成功完成（true：后台签字成功完成；false：后台签字失败）
	 * @return
	 */
	public static boolean removeAnAuthBackgroundRuning(AuthBackgroundRuning info, boolean doneSuccess) throws Exception{
		if(instance == null){
			instance = new AuthBackgroundRuningManager();
		}
		AuthBackgroundRuningDAO dao = instance.getDAO();
		Transaction tran = dao.getSession().beginTransaction();
		try{
			VerifyAndAuthorize v = info.getVerifyAndAuthorize();
			dao.delete(info);
			if(doneSuccess){
				v.setIsAuthBgRuning(null);
			}else{
				v.setAuthorizeResult(null);
				v.setAuthorizeTime(null);
				v.setIsAuthBgRuning(null);
				v.setAuthorizeRemark(null);
				v.setSysUserByAuthorizeExecutorId(null);
			}
			dao.update(v);
			tran.commit();
			return true;
		}catch(Exception e){
			tran.rollback();
			throw e;
		}finally{
			dao.closeSession();
		}
	}
	
	private AuthBackgroundRuningDAO m_dao = new AuthBackgroundRuningDAO();
	/**
	 * 根据AuthBackgroundRuning Id 查找 AuthBackgroundRuning对象
	 * @param id: AuthBackgroundRuning Id
	 * @return AuthBackgroundRuning对象
	 */
	public AuthBackgroundRuning findById(int id) {
		return m_dao.findById(id);
	}
	
	public AuthBackgroundRuningDAO getDAO(){
		return m_dao;
	}
	
			
	public int delete(List<KeyValueWithOperator> condList){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			int iRet = m_dao.delete("AuthBackgroundRuning", condList);	
			tran.commit();
			return iRet;
		} catch (Exception e) {
			tran.rollback();
			return -1;
		} finally {
			m_dao.closeSession();
		}
	}
	
	/**
	* 分页显示数据
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param orderby：按照哪个字段排序
	* @param asc：true 增序 false 减序
	* @param arr:为查询条件的(键-值)对数组
	* @return 分页后的数据列表- List<Student>
	*/
	public List<AuthBackgroundRuning> findPagedAllBySort(int currentPage, int pageSize, String orderby, boolean asc, KeyValueWithOperator...arr) throws Exception{
		return m_dao.findPagedAllBySort("AuthBackgroundRuning", currentPage, pageSize, orderby, asc, arr);
	}
	/**
	 * 获取符合条件的记录个数
	 * @param 变参arr：查询的条件（键-值-算符）
	 * @return 符合条件的记录个数
	 */
	public int getTotalCount(KeyValueWithOperator...arr) throws Exception{
		return m_dao.getTotalCount("AuthBackgroundRuning", arr);
	}
}
