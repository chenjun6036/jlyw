package com.jlyw.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;

import com.jlyw.hibernate.Certificate;
import com.jlyw.hibernate.CertificateFeeAssign;
import com.jlyw.hibernate.CertificateFeeAssignDAO;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.OriginalRecord;
import com.jlyw.util.KeyValue;
import com.jlyw.util.KeyValueWithOperator;

public class CertificateFeeAssignManager {
	private static final Log log = LogFactory.getLog(CertificateFeeAssignManager.class);
	private CertificateFeeAssignDAO m_dao = new CertificateFeeAssignDAO();
	
	//HQL语句：根据委托单Id查询该委托单下的所有原始费用之和
	public static final String queryStringAllOldFeeByCommissionSheetId = " select SUM(a.testFeeOld),SUM(a.repairFeeOld),SUM(a.materialFeeOld),SUM(a.carFeeOld),SUM(a.debugFeeOld),SUM(a.otherFeeOld),SUM(a.totalFeeOld) " +
			" from CertificateFeeAssign as a " +
			" where a.commissionSheet.id = ? and ( " +
			" 	(a.originalRecord is null and a.certificate is null) or " +
			"   (a.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = a.certificate)) " +	//不可以直接使用a.originalRecord.certificate=a.certificate语句来判断，因为该语句会使得前一条语句(a.originalRecord is null and a.certificate is null)无效（HQL通过笛卡尔积集来查找）
			" ) ";
	//HQL语句：根据委托单Id查询该委托单下的所有费用记录
	public static final String queryString_CertificateFeeAssignByCommissionSheetId = "from CertificateFeeAssign as a " +
			" where a.commissionSheet.id = ? and ( " +
			" 	(a.originalRecord is null and a.certificate is null) or " +
			"	(a.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = a.certificate)) " +
			" ) ";
	
	//HQL语句：根据委托单Id查询该委托单下的所有费用之和（现价）
	public static final String queryStringAllFeeByCommissionSheetId = " select SUM(a.testFee),SUM(a.repairFee),SUM(a.materialFee),SUM(a.carFee),SUM(a.debugFee),SUM(a.otherFee),SUM(a.totalFee) " +
			" from CertificateFeeAssign as a " +
			" where a.commissionSheet.id = ? and ( " +
			" 	(a.originalRecord is null and a.certificate is null) or " +
			"	(a.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = a.certificate)) " +
			" ) ";

	//HQL语句：根据原始记录和证书查询该证书的所有费用(现价)
	public static final String queryStringAllFeeByOriginalRecordIdAndCertificateId = "select SUM(a.testFee),SUM(a.repairFee),SUM(a.materialFee),SUM(a.carFee),SUM(a.debugFee),SUM(a.otherFee),SUM(a.totalFee) " +
			" from CertificateFeeAssign as a " +
			" where a.originalRecord.id = ? and a.certificate.id = ? ";
	
	//HQL语句：根据原始记录和证书查询该证书的所有费用（现价+原价）
	public static final String queryStringAllAllFeeByOriginalRecordIdAndCertificateId = "select SUM(a.testFee),SUM(a.repairFee),SUM(a.materialFee),SUM(a.carFee),SUM(a.debugFee),SUM(a.otherFee),SUM(a.totalFee), SUM(a.testFeeOld),SUM(a.repairFeeOld),SUM(a.materialFeeOld),SUM(a.carFeeOld),SUM(a.debugFeeOld),SUM(a.otherFeeOld),SUM(a.totalFeeOld) " +
			" from CertificateFeeAssign as a " +
			" where a.originalRecord.id = ? and a.certificate.id = ? ";

	
	//HQL语句：根据委托单Id查询该委托单下的所有费用之和（现价+原价）
	public static final String queryStringAllAllFeeByCommissionSheetId = " select SUM(a.testFee),SUM(a.repairFee),SUM(a.materialFee),SUM(a.carFee),SUM(a.debugFee),SUM(a.otherFee),SUM(a.totalFee), SUM(a.testFeeOld),SUM(a.repairFeeOld),SUM(a.materialFeeOld),SUM(a.carFeeOld),SUM(a.debugFeeOld),SUM(a.otherFeeOld),SUM(a.totalFeeOld)  " +
			" from CertificateFeeAssign as a " +
			" where a.commissionSheet.id = ? and ( " +
			" 	(a.originalRecord is null and a.certificate is null) or " +
			"	(a.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = a.certificate)) " +
			" ) ";

	
	
	
	/**
	 * 根据CertificateFeeAssign Id 查找 CertificateFeeAssign对象
	 * @param id: CertificateFeeAssign Id
	 * @return CertificateFeeAssign对象
	 */
	public CertificateFeeAssign findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条CertificateFeeAssign记录
	 * @param appSpecies CertificateFeeAssign对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(CertificateFeeAssign appSpecies){
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
	 * 更新一条CertificateFeeAssign记录
	 * @param appSpecies CertificateFeeAssign对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(CertificateFeeAssign appSpecies){
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
	 * 根据CertificateFeeAssign Id,删除CertificateFeeAssign对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			CertificateFeeAssign u = m_dao.findById(id);
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
	 * @return 分页后的CertificateFeeAssign列表
	 */
	public List<CertificateFeeAssign> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) throws Exception {
		return m_dao.findPagedAll("CertificateFeeAssign", currentPage,pageSize, arr);
	}
	
	/**
	 * 得到所有CertificateFeeAssign记录数
	 * @param arr 条件键值对
	 * @return CertificateFeeAssign记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("CertificateFeeAssign", arr);
	}
	/**
	 * 得到所有CertificateFeeAssign记录数
	 * @param arr 查询条件列表
	 * @return CertificateFeeAssign记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("CertificateFeeAssign", arr);
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<CertificateFeeAssign> findByExample(CertificateFeeAssign instance) {
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
	* @return 分页后的数据列表- List<CertificateFeeAssign>
	*/
	public List<CertificateFeeAssign> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		return m_dao.findPagedAllBySort("CertificateFeeAssign", currentPage, pageSize, orderby, asc, arr);
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
	* @return 分页后的数据列表- List<CertificateFeeAssign>
	*/
	public List<CertificateFeeAssign> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		return m_dao.findPagedAllBySort("CertificateFeeAssign", currentPage, pageSize, orderby, asc, arr);
	}
	
	/**
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findByHQL(String queryString, Object...arr){
		return m_dao.findByHQL(queryString, arr);
	}
	/**
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findByHQL(String queryString, List<Object> arr){
		return m_dao.findByHQL(queryString, arr);
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
		return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
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
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, List<Object> arr){
		try{
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,Object...arr) throws Exception{
		return m_dao.getTotalCountByHQL(queryString, arr);
	}
	/**
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,List<Object> arr) throws Exception{
		return m_dao.getTotalCountByHQL(queryString, arr);
	}
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<CertificateFeeAssign> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("CertificateFeeAssign", arr);
	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<CertificateFeeAssign> findByVarProperty(List<KeyValueWithOperator> arr){
		return m_dao.findByVarProperty("CertificateFeeAssign", arr);
	}
	
	/**
	 * 为一个证书分配费用
	 * @param oRecord：证书所属的原始记录
	 * @param certificate：证书
	 * @param feeAssignList：费用分配列表
	 * @return
	 * @throws Exception
	 */
	public void executeFeeAssign(OriginalRecord oRecord, Certificate certificate, List<CertificateFeeAssign> feeAssignList) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try{
			//删除该原始记录下该证书以前的所有记录
			m_dao.delete("CertificateFeeAssign", 
					new KeyValue("originalRecord.id", oRecord.getId()), 
					new KeyValue("certificate.id", certificate.getId()));
			
			for(CertificateFeeAssign fAssign : feeAssignList){
				m_dao.save(fAssign);
			}
			tran.commit();
		}catch(Exception e){
			tran.rollback();
			throw e;
		}finally{
			m_dao.closeSession();
		}
	}
	/**
	 * 按委托单分配证书费用
	 * @param oRecord
	 * @param certificate
	 * @param feeAssignList
	 * @throws Exception
	 */
	public void executeFeeAssignBySheet(List<CertificateFeeAssign> feeAssignList) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try{
			for(CertificateFeeAssign fAssign : feeAssignList){
				m_dao.save(fAssign);
			}
			tran.commit();
		}catch(Exception e){
			tran.rollback();
			throw e;
		}finally{
			m_dao.closeSession();
		}
	}
	/**
	 * 为一个委托单分配费用（转包费、技术服务费等，不包括检定证书的费用）
	 * @param cSheet：委托单
	 * @param feeAssignList：费用分配列表
	 * @return
	 * @throws Exception
	 */
	public void executeFeeAssignWithoutCertificate(CommissionSheet cSheet, List<CertificateFeeAssign> feeAssignList) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try{
			//删除该委托单已分配的以前的所有记录（不包括检定证书的费用记录）
			List<KeyValueWithOperator> delCondList = new ArrayList<KeyValueWithOperator>();
			delCondList.add(new KeyValueWithOperator("commissionSheet.id", cSheet.getId(), "="));
			delCondList.add(new KeyValueWithOperator("originalRecord", null, "is null"));
			delCondList.add(new KeyValueWithOperator("certificate", null, "is null"));
			
			m_dao.delete("CertificateFeeAssign", delCondList);
			
			for(CertificateFeeAssign fAssign : feeAssignList){
				m_dao.save(fAssign);
			}
			tran.commit();
		}catch(Exception e){
			tran.rollback();
			throw e;
		}finally{
			m_dao.closeSession();
		}
	}
	/**
	 * 批量修改证书费用
	 *
	 * @param feeAssignList：费用分配列表
	 * @return
	 * @throws Exception
	 */
	public void updateFeeAssign(List<CertificateFeeAssign> feeAssignList) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try{
			//更新原始记录费用的HQL
			String updateString = "update OriginalRecord as o set " +
					" testFee=(select SUM(a.testFeeOld) from CertificateFeeAssign as a where a.originalRecord.id=o.id)," +
					" repairFee=(select SUM(a.repairFeeOld) from CertificateFeeAssign as a where a.originalRecord.id=o.id), " +
					" materialFee=(select SUM(a.materialFeeOld) from CertificateFeeAssign as a where a.originalRecord.id=o.id), " +
					" carFee=(select SUM(a.carFeeOld) from CertificateFeeAssign as a where a.originalRecord.id=o.id), " +
					" debugFee=(select SUM(a.debugFeeOld) from CertificateFeeAssign as a where a.originalRecord.id=o.id), " +
					" otherFee=(select SUM(a.otherFeeOld) from CertificateFeeAssign as a where a.originalRecord.id=o.id), " +
					" totalFee=(select SUM(a.totalFeeOld) from CertificateFeeAssign as a where a.originalRecord.id=o.id) " +
					" where o.id=? ";
			for(CertificateFeeAssign fAssign : feeAssignList){
				m_dao.update(fAssign);
				
				if(fAssign.getOriginalRecord() != null){
					m_dao.updateByHQL(updateString, fAssign.getOriginalRecord().getId());
				}
			}
			
			//将费用更新至原始记录费用中
			tran.commit();
		}catch(Exception e){
			tran.rollback();
			throw e;
		}finally{
			m_dao.closeSession();
		}
	}
}
