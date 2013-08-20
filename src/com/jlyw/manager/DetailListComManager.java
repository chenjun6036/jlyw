package com.jlyw.manager;

import java.text.DecimalFormat;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.DetailList;
import com.jlyw.hibernate.DetailListCom;
import com.jlyw.hibernate.DetailListComDAO;
import com.jlyw.hibernate.DetailListDAO;
import com.jlyw.hibernate.Discount;
import com.jlyw.hibernate.DiscountDAO;
import com.jlyw.util.KeyValueWithOperator;

/**
 * 原始记录
 * @author Administrator
 *
 */
public class DetailListComManager {
private DetailListComDAO m_dao = new DetailListComDAO();
	
	/**
	 * 根据DetailListCom Id 查找 DetailListCom对象
	 * @param id: DetailListCom Id
	 * @return DetailListCom对象
	 */
	public DetailListCom findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条DetailListCom记录
	 * @param appSpecies DetailListCom对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(DetailListCom appSpecies){
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
	 * 更新一条DetailListCom记录
	 * @param appSpecies DetailListCom对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(DetailListCom appSpecies){
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
	 * 根据DetailListCom Id,删除DetailListCom对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			DetailListCom u = m_dao.findById(id);
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
	 * @return 分页后的DetailListCom列表
	 */
	public List<DetailListCom> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("DetailListCom", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, Object...arr){
		try{
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 得到所有DetailListCom记录数
	 * @param arr 条件键值对
	 * @return DetailListCom记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("DetailListCom", arr);
	}
	/**
	 * 得到所有DetailListCom记录数
	 * @param arr 查询条件列表
	 * @return DetailListCom记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("DetailListCom", arr);
	}
	
	/**
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,Object...arr) {
		try{
			return m_dao.getTotalCountByHQL(queryString, arr);
		}catch(Exception ex){
			return 0;
		}
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<DetailListCom> findByExample(DetailListCom instance) {
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
	* @return 分页后的数据列表- List<DetailListCom>
	*/
	public List<DetailListCom> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("DetailListCom", currentPage, pageSize, orderby, asc, arr);
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
	* @return 分页后的数据列表- List<DetailListCom>
	*/
	public List<DetailListCom> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("DetailListCom", currentPage, pageSize, orderby, asc, arr);
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
	 * 按任意条件对查找，可指定排序字段
	 * @param TableName:从哪个表里查询
	 * @param orderby：按照哪个字段排序
	 * @param asc：true 增序 false 减序
	 * @param arr ：条件与值对
	 */
	public List<DetailListCom> findByPropertyBySort(String orderby, boolean asc, KeyValueWithOperator...arr){
		try{
			return m_dao.findByPropertyBySort("DetailListCom", orderby, asc, arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<DetailListCom> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("DetailListCom", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	/**
	 * 插入一批DetailListCom记录
	 * @param oRecordList DetailListCom 对象列表
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean saveByBatch(List<DetailListCom> oRecordList){
		if(oRecordList == null){
			return false;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			for(DetailListCom oRecord : oRecordList){
				m_dao.save(oRecord);
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
	 * 折扣审批：更新一批清单-委托单费用信息，更新清单，更新折扣办理信息
	 * dscfee[]折扣率
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean updateFeeByBatch(List<DetailListCom> detailListComList,DetailList deList,double dscfee[],Discount o) {
		if(detailListComList == null || dscfee.length == 0||dscfee==null||deList==null){
			return false;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		DecimalFormat   df   =new   DecimalFormat("#.00");	
		try {
			DetailListDAO dDAO = new DetailListDAO();	//清单表的DAO 
			DiscountDAO discountDAO = new DiscountDAO();	//折扣表的DAO 
			discountDAO.update(o);
			double totalfee=0;
			for(int i = 0; i < detailListComList.size(); i++){
				DetailListCom detailListCom = detailListComList.get(i);
				detailListCom.setTestFee(new Double(Math.round(detailListCom.getOldTestFee()*dscfee[0])));
				detailListCom.setRepairFee(new Double(Math.round(detailListCom.getOldRepairFee()*dscfee[1])));
				detailListCom.setMaterialFee(new Double(Math.round(detailListCom.getOldMaterialFee()*dscfee[2])));
				detailListCom.setCarFee(new Double(Math.round(detailListCom.getOldCarFee()*dscfee[3])));
				detailListCom.setDebugFee(new Double(Math.round(detailListCom.getOldDebugFee()*dscfee[4])));
				detailListCom.setOtherFee(new Double(Math.round(detailListCom.getOldOtherFee()*dscfee[5])));
				detailListCom.setTotalFee(new Double(Math.round(detailListCom.getOldTotalFee()*dscfee[6])));
				
				m_dao.save(detailListCom);
				totalfee+=detailListCom.getTotalFee();
			}
			deList.setVersion(deList.getVersion()+1);
			deList.setTotalFee(totalfee);
			dDAO.save(deList);
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

	
}
