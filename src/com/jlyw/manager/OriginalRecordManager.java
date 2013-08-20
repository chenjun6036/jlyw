package com.jlyw.manager;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Certificate;
import com.jlyw.hibernate.CertificateDAO;
import com.jlyw.hibernate.CertificateFeeAssignDAO;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.OriginalRecord;
import com.jlyw.hibernate.OriginalRecordDAO;
import com.jlyw.hibernate.OriginalRecordExcel;
import com.jlyw.hibernate.OriginalRecordExcelDAO;
import com.jlyw.hibernate.OriginalRecordStandards;
import com.jlyw.hibernate.OriginalRecordStandardsDAO;
import com.jlyw.hibernate.OriginalRecordStdAppliances;
import com.jlyw.hibernate.OriginalRecordStdAppliancesDAO;
import com.jlyw.hibernate.OriginalRecordTechnicalDocs;
import com.jlyw.hibernate.OriginalRecordTechnicalDocsDAO;
import com.jlyw.hibernate.Specification;
import com.jlyw.hibernate.Standard;
import com.jlyw.hibernate.StandardAppliance;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.VerifyAndAuthorize;
import com.jlyw.hibernate.VerifyAndAuthorizeDAO;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;

/**
 * 原始记录
 * @author Administrator
 *
 */
public class OriginalRecordManager {
private OriginalRecordDAO m_dao = new OriginalRecordDAO();
	
	/**
	 * 根据OriginalRecord Id 查找 OriginalRecord对象
	 * @param id: OriginalRecord Id
	 * @return OriginalRecord对象
	 */
	public OriginalRecord findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条OriginalRecord记录
	 * @param appSpecies OriginalRecord对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(OriginalRecord appSpecies) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(appSpecies);
			tran.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tran.rollback();
			throw e;
		} finally {
			m_dao.closeSession();
		}
	}
	
	/**
	 * 更新一条OriginalRecord记录
	 * @param appSpecies OriginalRecord对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(OriginalRecord appSpecies){
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
	 * 根据OriginalRecord Id,删除OriginalRecord对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			OriginalRecord u = m_dao.findById(id);
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
	 * @return 分页后的OriginalRecord列表
	 */
	public List<OriginalRecord> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("OriginalRecord", currentPage,pageSize, arr);
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
	 * 得到所有OriginalRecord记录数
	 * @param arr 条件键值对
	 * @return OriginalRecord记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("OriginalRecord", arr);
	}
	/**
	 * 得到所有OriginalRecord记录数
	 * @param arr 查询条件列表
	 * @return OriginalRecord记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("OriginalRecord", arr);
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
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,List<Object> arr) {
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
	public List<OriginalRecord> findByExample(OriginalRecord instance) {
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
	* @return 分页后的数据列表- List<OriginalRecord>
	*/
	public List<OriginalRecord> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("OriginalRecord", currentPage, pageSize, orderby, asc, arr);
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
	* @return 分页后的数据列表- List<OriginalRecord>
	*/
	public List<OriginalRecord> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("OriginalRecord", currentPage, pageSize, orderby, asc, arr);
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
	public List findByHQL(String queryString, Object...arr) throws Exception{
		return m_dao.findByHQL(queryString, arr);
		
	}
	
	/**
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findByHQL(String queryString, List<Object> arr) throws Exception{
		return m_dao.findByHQL(queryString, arr);
		
	}
	
	/**
	 * 按任意条件对查找，可指定排序字段
	 * @param TableName:从哪个表里查询
	 * @param orderby：按照哪个字段排序
	 * @param asc：true 增序 false 减序
	 * @param arr ：条件与值对
	 */
	public List<OriginalRecord> findByPropertyBySort(String orderby, boolean asc, KeyValueWithOperator...arr){
		try{
			return m_dao.findByPropertyBySort("OriginalRecord", orderby, asc, arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 插入一个OriginalRecord记录
	 * @param oRecordList OriginalRecord 对象列表
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean saveByBatch(OriginalRecord oRecord, List<Specification>speList, List<Standard>stdList, List<StandardAppliance>stdAppList, SysUser loginUser){
		if(oRecord == null || speList == null || stdList == null || stdAppList == null){
			return false;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		OriginalRecordTechnicalDocsDAO oSpeDao = new OriginalRecordTechnicalDocsDAO();
		OriginalRecordStandardsDAO oStdDao = new OriginalRecordStandardsDAO();
		OriginalRecordStdAppliancesDAO oStdAppDao = new OriginalRecordStdAppliancesDAO();
		Timestamp nowTime = new Timestamp(System.currentTimeMillis());
		try {
			m_dao.save(oRecord);
			
			//存技术规范
			Iterator<Specification> speIter = speList.iterator();
			while (speIter.hasNext()) {
				OriginalRecordTechnicalDocs oSpe = new OriginalRecordTechnicalDocs();
				oSpe.setOriginalRecord(oRecord);
				oSpe.setSpecification(speIter.next());
				oSpe.setLastEditTime(nowTime);
				oSpe.setSysUser(loginUser);
				
				oSpeDao.save(oSpe);
			}
			
			//存计量标准
			Iterator<Standard> stdIter = stdList.iterator();
			while (stdIter.hasNext()) {
				OriginalRecordStandards oStd = new OriginalRecordStandards();
				oStd.setOriginalRecord(oRecord);
				oStd.setStandard(stdIter.next());
				oStd.setLastEditTime(nowTime);
				oStd.setSysUser(loginUser);
				
				oStdDao.save(oStd);
			}
			
			//存标准器具
			Iterator<StandardAppliance> stdAppIter = stdAppList.iterator();
			while (stdAppIter.hasNext()) {
				OriginalRecordStdAppliances oStd = new OriginalRecordStdAppliances();
				oStd.setOriginalRecord(oRecord);
				oStd.setStandardAppliance(stdAppIter.next());
				oStd.setLastEditTime(nowTime);
				oStd.setSysUser(loginUser);
				
				oStdAppDao.save(oStd);
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
	 * 上传原始记录Excel时更新Excel、Certificate
	 * @param oRecord
	 * @param excel
	 * @param isNewExcel true：新增Excel；false：更新Excel
	 * @param certificate :id为null则新增Certificate，否则更新Certificate
	 * @param v:id为null则新增VerifyAndAuthorize，否则，更新VerifyAndAuthorize
	 * @return 
	 */
	public boolean uploadExcelUpdateDB(OriginalRecord oRecord, OriginalRecordExcel excel, boolean isNewExcel, Certificate certificate, VerifyAndAuthorize v) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			excel.setCertificateCode(certificate.getCertificateCode());	//设置Excel的证书编号（用于将Excel与证书关联起来，该字段仅用于查询所用）
			
			OriginalRecordExcelDAO xlsDao = new OriginalRecordExcelDAO();
			if(isNewExcel){
				xlsDao.save(excel);
			}else{
				xlsDao.update(excel);
			}
			
			CertificateDAO certificateDao = new CertificateDAO();
			if(certificate.getId() == null){
				certificateDao.save(certificate);
			}else{
				certificateDao.update(certificate);
			}
			
			VerifyAndAuthorizeDAO vDao = new VerifyAndAuthorizeDAO();
			v.setOriginalRecordExcel(excel);
			v.setCertificate(certificate);
			if(v.getId() == null){
				vDao.save(v);
			}else{
				vDao.update(v);
			}
			
			Certificate oldCertificate = oRecord.getCertificate();	//旧的证书
			
			oRecord.setOriginalRecordExcel(excel);
			oRecord.setCertificate(certificate);
			oRecord.setVerifyAndAuthorize(v);
			m_dao.update(oRecord);
			
			//判断委托单是否已完工，如果已完工，则需要将该证书以前的费用分配转移至该新的证书
			if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinished(oRecord.getCommissionSheet().getStatus()) && oldCertificate != null){
				String updateString = "update CertificateFeeAssign set certificate = ? where originalRecord.id = ? and certificate.id = ? ";
				CertificateFeeAssignDAO cDao = new CertificateFeeAssignDAO();
				cDao.updateByHQL(updateString, certificate, oRecord.getId(), oldCertificate.getId());
			}
			
			tran.commit();
			return true;
		}catch(Exception e){
			tran.rollback();
			throw new Exception(String.format("更新数据库失败！原因：%s", e.getMessage()==null?"无":e.getMessage()));
		}finally{
			m_dao.closeSession();
		}
	}
	
	/**
	 * 上传证书（生成证书）时更新数据库
	 * @param oRecord
	 * @param excel：id为null则新增Certificate，否则更新Certificate
	 * @param updateExcel true则更新Excel，否则不变
	 * @param certificate：id为null则新增Certificate，否则更新Certificate
	 * @return
	 * @throws Exception
	 */
	public boolean uploadCertificateUpdateDB(OriginalRecord oRecord, OriginalRecordExcel excel, Certificate certificate, VerifyAndAuthorize v) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			OriginalRecordExcelDAO xlsDao = new OriginalRecordExcelDAO();
			excel.setCertificateCode(certificate.getCertificateCode()); //设置Excel的证书编号（用于将Excel与证书关联起来，该字段仅用于查询所用）
			if(excel.getId() == null){
				xlsDao.save(excel);
			}else{
				xlsDao.update(excel);
			}
			
			CertificateDAO certificateDao = new CertificateDAO();
			if(certificate.getId() == null){
				certificateDao.save(certificate);
			}else{
				certificateDao.update(certificate);
			}
			
			VerifyAndAuthorizeDAO vDao = new VerifyAndAuthorizeDAO();
			v.setOriginalRecordExcel(excel);
			v.setCertificate(certificate);
			if(v.getId() == null){
				vDao.save(v);
			}else{
				vDao.update(v);
			}
			
			Certificate oldCertificate = oRecord.getCertificate();	//旧的证书
			
			oRecord.setOriginalRecordExcel(excel);
			oRecord.setCertificate(certificate);
			oRecord.setVerifyAndAuthorize(v);
			m_dao.update(oRecord);
			
			//判断委托单是否已完工，如果已完工，则需要将该证书以前的费用分配转移至该新的证书
			if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinished(oRecord.getCommissionSheet().getStatus()) && oldCertificate != null){
				String updateString = "update CertificateFeeAssign set certificate = ? where originalRecord.id = ? and certificate.id = ? ";
				CertificateFeeAssignDAO cDao = new CertificateFeeAssignDAO();
				cDao.updateByHQL(updateString, certificate, oRecord.getId(), oldCertificate.getId());
			}
			
			tran.commit();
			return true;
		}catch(Exception e){
			tran.rollback();
			throw new Exception(String.format("更新数据库失败！原因：%s", e.getMessage()==null?"无":e.getMessage()));
		}finally{
			m_dao.closeSession();
		}
		
	}
	/**
	 * 查询一个委托单下的最大记录编号（序列号）:用于证书编号（委托单号+序列号+版本号）
	 * @param cSheet
	 * @return 最大的序列号
	 * @throws Exception
	 */
	public Integer getMaxSequence(CommissionSheet cSheet) throws Exception{
		 Integer sequence = 1;	//初始从1开始
		try{
			String queryString = "select max(model.certificate.sequece) " +
				" from OriginalRecord as model " +
				" where model.certificate is not null and model.status <> 1 and model.taskAssign.status<>1 " +
				" and model.commissionSheet.id = ? ";
			List<Object> retList = m_dao.findByHQL(queryString, cSheet.getId());
			if(retList.size() > 0 && retList.get(0) != null){
				sequence = (Integer)retList.get(0) + 1;
			}
			return sequence;
		}catch(Exception e){
			throw new Exception(String.format("分配证书编号失败，请稍后再试！原因：%s", e.getMessage()==null?"无":e.getMessage()));
		}
		
	}
	/**
	 * 查询一个委托单下的可用的一个记录编号（序列号）:用于证书编号（委托单号+序列号+版本号）
	 * 2012-07-26 添加（替换getMaxSequence）：获取一个可用的序列号（不一定为最大的序列号），
	 * 例如：已有原始记录001,002，再把001注销后，再次调用该方法后，获取的是001（不是003）
	 * @param cSheet
	 * @return 可用的序列号
	 * @throws Exception
	 */
	 public Integer getAvailableSequence(CommissionSheet cSheet) throws Exception{
		 Integer sequence = 1;	//初始从1开始
		 try{
			 String queryString = "select max(model.certificate.sequece) " +
				" from OriginalRecord as model " +
				" where model.certificate is not null and model.status <> 1 and model.taskAssign.status<>1 " +
				" and model.commissionSheet.id = ? ";
			List<Object> retList = m_dao.findByHQL(queryString, cSheet.getId());	//先查询最大的序列号
			if(retList.size() > 0){
				Integer maxSequence = (retList.get(0)==null?0:(Integer)retList.get(0));	//最大的记录号
				String queryString2 = "select count(*) from OriginalRecord as model " +
						" where model.certificate is not null and model.certificate.sequece = ? and model.status <> 1 and model.taskAssign.status<>1 and model.commissionSheet.id = ? ";
				
				while(sequence < maxSequence){
					if(m_dao.getTotalCountByHQL(queryString2, sequence, cSheet.getId()) > 0){	//已存在该序号
						sequence ++;
					}else{
						return sequence;
					}
				}
				return maxSequence + 1;
			}
			return sequence;
		 }catch(Exception e){
			 throw new Exception(String.format("分配证书编号失败，请稍后再试！原因：%s", e.getMessage()==null?"无":e.getMessage()));
		 }
			
	 }
	
	/**
	 * 按任意条件对查找，可指定排序字段
	 * @param TableName:从哪个表里查询

	 * @param arr ：条件与值对
	 */
	public List<OriginalRecord> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("OriginalRecord", arr);
		}
		catch(Exception e){
			return null;
		}
	}
}
