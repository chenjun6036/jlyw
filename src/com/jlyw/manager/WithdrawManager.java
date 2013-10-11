package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.CommissionSheetDAO;
import com.jlyw.hibernate.Withdraw;
import com.jlyw.hibernate.WithdrawDAO;
import com.jlyw.util.KeyValueWithOperator;

/**
 * 退样
 * @author Administrator
 *
 */
public class WithdrawManager {
private WithdrawDAO m_dao = new WithdrawDAO();
	
	/**
	 * 根据Withdraw Id 查找 Withdraw对象
	 * @param id: Withdraw Id
	 * @return Withdraw对象
	 */
	public Withdraw findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条Withdraw记录
	 * @param appSpecies Withdraw对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(Withdraw appSpecies){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(appSpecies);
			tran.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tran.rollback();
			return false;
		} finally {
			m_dao.closeSession();
		}
	}
	
	/**
	 * 更新一条Withdraw记录
	 * @param appSpecies Withdraw对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(Withdraw appSpecies){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(appSpecies);
			tran.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tran.rollback();
			return false;
		} finally {
			m_dao.closeSession();
		}
	}
	
	/**
	 * 根据Withdraw Id,删除Withdraw对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Withdraw u = m_dao.findById(id);
			if(u == null){
				return true;
			}else{
				m_dao.delete(u);
			}			
			tran.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tran.rollback();
			return false;
		} finally {
			m_dao.closeSession();
		}
	}	
	
	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @param arr 条件键值对
	 * @return 分页后的Withdraw列表
	 */
	public List<Withdraw> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("Withdraw", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有Withdraw记录数
	 * @param arr 条件键值对
	 * @return Withdraw记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Withdraw", arr);
	}
	/**
	 * 得到所有Withdraw记录数
	 * @param arr 查询条件列表
	 * @return Withdraw记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("Withdraw", arr);
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<Withdraw> findByExample(Withdraw instance) {
		return m_dao.findByExample(instance);
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
	* @return 分页后的数据列表- List<Withdraw>
	*/
	public List<Withdraw> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("Withdraw", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			return null;
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
	* @param arr:为查询条件的(键-值)对列表
	* @return 分页后的数据列表- List<Withdraw>
	*/
	public List<Withdraw> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("Withdraw", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findByHQL(String queryString, Object...arr){
		try{
			return m_dao.findByHQL(queryString, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<Withdraw> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Withdraw", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	
	/**
	 * 退样
	 * 更新一条Withdraw记录
	 * @param appSpecies Withdraw对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean withdrawUpdate(Withdraw appSpecies){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(appSpecies);
			CommissionSheet comSheet= appSpecies.getCommissionSheet();
			String queryString = "from Withdraw as a where a.commissionSheet.id = ? and a.commissionSheet.status != ? and a.executeResult = true";
			List<Withdraw> withdrawList = m_dao.findByHQL(queryString, comSheet.getId(),10);//非注销
			int number=0;
			for(Withdraw w:withdrawList){
				number +=w.getNumber();
			}
			if(number==comSheet.getQuantity()){
				m_dao.updateByHQL("update CommissionSheet set status = 9,finishLocation = ? where id = ? ",appSpecies.getLocation(),comSheet.getId());	//置已结束状态
			}		
			tran.commit() ;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tran.rollback();
			return false;
		} finally {
			m_dao.closeSession();
		}
	}
}
