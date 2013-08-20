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
 * ԭʼ��¼
 * @author Administrator
 *
 */
public class OriginalRecordManager {
private OriginalRecordDAO m_dao = new OriginalRecordDAO();
	
	/**
	 * ����OriginalRecord Id ���� OriginalRecord����
	 * @param id: OriginalRecord Id
	 * @return OriginalRecord����
	 */
	public OriginalRecord findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��OriginalRecord��¼
	 * @param appSpecies OriginalRecord����
	 * @return ����ɹ�������true�����򷵻�false
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
	 * ����һ��OriginalRecord��¼
	 * @param appSpecies OriginalRecord����
	 * @return ���³ɹ�������true�����򷵻�false
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
	 * ����OriginalRecord Id,ɾ��OriginalRecord����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
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
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param arr ������ֵ��
	 * @return ��ҳ���OriginalRecord�б�
	 */
	public List<OriginalRecord> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("OriginalRecord", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
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
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
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
	 * �õ�����OriginalRecord��¼��
	 * @param arr ������ֵ��
	 * @return OriginalRecord��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("OriginalRecord", arr);
	}
	/**
	 * �õ�����OriginalRecord��¼��
	 * @param arr ��ѯ�����б�
	 * @return OriginalRecord��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("OriginalRecord", arr);
	}
	
	/**
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
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
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
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
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<OriginalRecord> findByExample(OriginalRecord instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	* ��ҳ��ʾ����
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param orderby�������ĸ��ֶ�����
	* @param asc��true ���� false ����
	* @param arr:Ϊ��ѯ������(��-ֵ)������
	* @return ��ҳ��������б�- List<OriginalRecord>
	*/
	public List<OriginalRecord> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("OriginalRecord", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	* ��ҳ��ʾ����
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param orderby�������ĸ��ֶ�����
	* @param asc��true ���� false ����
	* @param arr:Ϊ��ѯ������(��-ֵ)���б�
	* @return ��ҳ��������б�- List<OriginalRecord>
	*/
	public List<OriginalRecord> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("OriginalRecord", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findByHQL(String queryString, Object...arr) throws Exception{
		return m_dao.findByHQL(queryString, arr);
		
	}
	
	/**
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findByHQL(String queryString, List<Object> arr) throws Exception{
		return m_dao.findByHQL(queryString, arr);
		
	}
	
	/**
	 * �����������Բ��ң���ָ�������ֶ�
	 * @param TableName:���ĸ������ѯ
	 * @param orderby�������ĸ��ֶ�����
	 * @param asc��true ���� false ����
	 * @param arr ��������ֵ��
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
	 * ����һ��OriginalRecord��¼
	 * @param oRecordList OriginalRecord �����б�
	 * @return ����ɹ�������true�����򷵻�false
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
			
			//�漼���淶
			Iterator<Specification> speIter = speList.iterator();
			while (speIter.hasNext()) {
				OriginalRecordTechnicalDocs oSpe = new OriginalRecordTechnicalDocs();
				oSpe.setOriginalRecord(oRecord);
				oSpe.setSpecification(speIter.next());
				oSpe.setLastEditTime(nowTime);
				oSpe.setSysUser(loginUser);
				
				oSpeDao.save(oSpe);
			}
			
			//�������׼
			Iterator<Standard> stdIter = stdList.iterator();
			while (stdIter.hasNext()) {
				OriginalRecordStandards oStd = new OriginalRecordStandards();
				oStd.setOriginalRecord(oRecord);
				oStd.setStandard(stdIter.next());
				oStd.setLastEditTime(nowTime);
				oStd.setSysUser(loginUser);
				
				oStdDao.save(oStd);
			}
			
			//���׼����
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
	 * �ϴ�ԭʼ��¼Excelʱ����Excel��Certificate
	 * @param oRecord
	 * @param excel
	 * @param isNewExcel true������Excel��false������Excel
	 * @param certificate :idΪnull������Certificate���������Certificate
	 * @param v:idΪnull������VerifyAndAuthorize�����򣬸���VerifyAndAuthorize
	 * @return 
	 */
	public boolean uploadExcelUpdateDB(OriginalRecord oRecord, OriginalRecordExcel excel, boolean isNewExcel, Certificate certificate, VerifyAndAuthorize v) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			excel.setCertificateCode(certificate.getCertificateCode());	//����Excel��֤���ţ����ڽ�Excel��֤��������������ֶν����ڲ�ѯ���ã�
			
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
			
			Certificate oldCertificate = oRecord.getCertificate();	//�ɵ�֤��
			
			oRecord.setOriginalRecordExcel(excel);
			oRecord.setCertificate(certificate);
			oRecord.setVerifyAndAuthorize(v);
			m_dao.update(oRecord);
			
			//�ж�ί�е��Ƿ����깤��������깤������Ҫ����֤����ǰ�ķ��÷���ת�������µ�֤��
			if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinished(oRecord.getCommissionSheet().getStatus()) && oldCertificate != null){
				String updateString = "update CertificateFeeAssign set certificate = ? where originalRecord.id = ? and certificate.id = ? ";
				CertificateFeeAssignDAO cDao = new CertificateFeeAssignDAO();
				cDao.updateByHQL(updateString, certificate, oRecord.getId(), oldCertificate.getId());
			}
			
			tran.commit();
			return true;
		}catch(Exception e){
			tran.rollback();
			throw new Exception(String.format("�������ݿ�ʧ�ܣ�ԭ��%s", e.getMessage()==null?"��":e.getMessage()));
		}finally{
			m_dao.closeSession();
		}
	}
	
	/**
	 * �ϴ�֤�飨����֤�飩ʱ�������ݿ�
	 * @param oRecord
	 * @param excel��idΪnull������Certificate���������Certificate
	 * @param updateExcel true�����Excel�����򲻱�
	 * @param certificate��idΪnull������Certificate���������Certificate
	 * @return
	 * @throws Exception
	 */
	public boolean uploadCertificateUpdateDB(OriginalRecord oRecord, OriginalRecordExcel excel, Certificate certificate, VerifyAndAuthorize v) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			OriginalRecordExcelDAO xlsDao = new OriginalRecordExcelDAO();
			excel.setCertificateCode(certificate.getCertificateCode()); //����Excel��֤���ţ����ڽ�Excel��֤��������������ֶν����ڲ�ѯ���ã�
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
			
			Certificate oldCertificate = oRecord.getCertificate();	//�ɵ�֤��
			
			oRecord.setOriginalRecordExcel(excel);
			oRecord.setCertificate(certificate);
			oRecord.setVerifyAndAuthorize(v);
			m_dao.update(oRecord);
			
			//�ж�ί�е��Ƿ����깤��������깤������Ҫ����֤����ǰ�ķ��÷���ת�������µ�֤��
			if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinished(oRecord.getCommissionSheet().getStatus()) && oldCertificate != null){
				String updateString = "update CertificateFeeAssign set certificate = ? where originalRecord.id = ? and certificate.id = ? ";
				CertificateFeeAssignDAO cDao = new CertificateFeeAssignDAO();
				cDao.updateByHQL(updateString, certificate, oRecord.getId(), oldCertificate.getId());
			}
			
			tran.commit();
			return true;
		}catch(Exception e){
			tran.rollback();
			throw new Exception(String.format("�������ݿ�ʧ�ܣ�ԭ��%s", e.getMessage()==null?"��":e.getMessage()));
		}finally{
			m_dao.closeSession();
		}
		
	}
	/**
	 * ��ѯһ��ί�е��µ�����¼��ţ����кţ�:����֤���ţ�ί�е���+���к�+�汾�ţ�
	 * @param cSheet
	 * @return �������к�
	 * @throws Exception
	 */
	public Integer getMaxSequence(CommissionSheet cSheet) throws Exception{
		 Integer sequence = 1;	//��ʼ��1��ʼ
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
			throw new Exception(String.format("����֤����ʧ�ܣ����Ժ����ԣ�ԭ��%s", e.getMessage()==null?"��":e.getMessage()));
		}
		
	}
	/**
	 * ��ѯһ��ί�е��µĿ��õ�һ����¼��ţ����кţ�:����֤���ţ�ί�е���+���к�+�汾�ţ�
	 * 2012-07-26 ��ӣ��滻getMaxSequence������ȡһ�����õ����кţ���һ��Ϊ�������кţ���
	 * ���磺����ԭʼ��¼001,002���ٰ�001ע�����ٴε��ø÷����󣬻�ȡ����001������003��
	 * @param cSheet
	 * @return ���õ����к�
	 * @throws Exception
	 */
	 public Integer getAvailableSequence(CommissionSheet cSheet) throws Exception{
		 Integer sequence = 1;	//��ʼ��1��ʼ
		 try{
			 String queryString = "select max(model.certificate.sequece) " +
				" from OriginalRecord as model " +
				" where model.certificate is not null and model.status <> 1 and model.taskAssign.status<>1 " +
				" and model.commissionSheet.id = ? ";
			List<Object> retList = m_dao.findByHQL(queryString, cSheet.getId());	//�Ȳ�ѯ�������к�
			if(retList.size() > 0){
				Integer maxSequence = (retList.get(0)==null?0:(Integer)retList.get(0));	//���ļ�¼��
				String queryString2 = "select count(*) from OriginalRecord as model " +
						" where model.certificate is not null and model.certificate.sequece = ? and model.status <> 1 and model.taskAssign.status<>1 and model.commissionSheet.id = ? ";
				
				while(sequence < maxSequence){
					if(m_dao.getTotalCountByHQL(queryString2, sequence, cSheet.getId()) > 0){	//�Ѵ��ڸ����
						sequence ++;
					}else{
						return sequence;
					}
				}
				return maxSequence + 1;
			}
			return sequence;
		 }catch(Exception e){
			 throw new Exception(String.format("����֤����ʧ�ܣ����Ժ����ԣ�ԭ��%s", e.getMessage()==null?"��":e.getMessage()));
		 }
			
	 }
	
	/**
	 * �����������Բ��ң���ָ�������ֶ�
	 * @param TableName:���ĸ������ѯ

	 * @param arr ��������ֵ��
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
