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
	
	//HQL��䣺����ί�е�Id��ѯ��ί�е��µ�����ԭʼ����֮��
	public static final String queryStringAllOldFeeByCommissionSheetId = " select SUM(a.testFeeOld),SUM(a.repairFeeOld),SUM(a.materialFeeOld),SUM(a.carFeeOld),SUM(a.debugFeeOld),SUM(a.otherFeeOld),SUM(a.totalFeeOld) " +
			" from CertificateFeeAssign as a " +
			" where a.commissionSheet.id = ? and ( " +
			" 	(a.originalRecord is null and a.certificate is null) or " +
			"   (a.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = a.certificate)) " +	//������ֱ��ʹ��a.originalRecord.certificate=a.certificate������жϣ���Ϊ������ʹ��ǰһ�����(a.originalRecord is null and a.certificate is null)��Ч��HQLͨ���ѿ������������ң�
			" ) ";
	//HQL��䣺����ί�е�Id��ѯ��ί�е��µ����з��ü�¼
	public static final String queryString_CertificateFeeAssignByCommissionSheetId = "from CertificateFeeAssign as a " +
			" where a.commissionSheet.id = ? and ( " +
			" 	(a.originalRecord is null and a.certificate is null) or " +
			"	(a.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = a.certificate)) " +
			" ) ";
	
	//HQL��䣺����ί�е�Id��ѯ��ί�е��µ����з���֮�ͣ��ּۣ�
	public static final String queryStringAllFeeByCommissionSheetId = " select SUM(a.testFee),SUM(a.repairFee),SUM(a.materialFee),SUM(a.carFee),SUM(a.debugFee),SUM(a.otherFee),SUM(a.totalFee) " +
			" from CertificateFeeAssign as a " +
			" where a.commissionSheet.id = ? and ( " +
			" 	(a.originalRecord is null and a.certificate is null) or " +
			"	(a.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = a.certificate)) " +
			" ) ";

	//HQL��䣺����ԭʼ��¼��֤���ѯ��֤������з���(�ּ�)
	public static final String queryStringAllFeeByOriginalRecordIdAndCertificateId = "select SUM(a.testFee),SUM(a.repairFee),SUM(a.materialFee),SUM(a.carFee),SUM(a.debugFee),SUM(a.otherFee),SUM(a.totalFee) " +
			" from CertificateFeeAssign as a " +
			" where a.originalRecord.id = ? and a.certificate.id = ? ";
	
	//HQL��䣺����ԭʼ��¼��֤���ѯ��֤������з��ã��ּ�+ԭ�ۣ�
	public static final String queryStringAllAllFeeByOriginalRecordIdAndCertificateId = "select SUM(a.testFee),SUM(a.repairFee),SUM(a.materialFee),SUM(a.carFee),SUM(a.debugFee),SUM(a.otherFee),SUM(a.totalFee), SUM(a.testFeeOld),SUM(a.repairFeeOld),SUM(a.materialFeeOld),SUM(a.carFeeOld),SUM(a.debugFeeOld),SUM(a.otherFeeOld),SUM(a.totalFeeOld) " +
			" from CertificateFeeAssign as a " +
			" where a.originalRecord.id = ? and a.certificate.id = ? ";

	
	//HQL��䣺����ί�е�Id��ѯ��ί�е��µ����з���֮�ͣ��ּ�+ԭ�ۣ�
	public static final String queryStringAllAllFeeByCommissionSheetId = " select SUM(a.testFee),SUM(a.repairFee),SUM(a.materialFee),SUM(a.carFee),SUM(a.debugFee),SUM(a.otherFee),SUM(a.totalFee), SUM(a.testFeeOld),SUM(a.repairFeeOld),SUM(a.materialFeeOld),SUM(a.carFeeOld),SUM(a.debugFeeOld),SUM(a.otherFeeOld),SUM(a.totalFeeOld)  " +
			" from CertificateFeeAssign as a " +
			" where a.commissionSheet.id = ? and ( " +
			" 	(a.originalRecord is null and a.certificate is null) or " +
			"	(a.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = a.certificate)) " +
			" ) ";

	
	
	
	/**
	 * ����CertificateFeeAssign Id ���� CertificateFeeAssign����
	 * @param id: CertificateFeeAssign Id
	 * @return CertificateFeeAssign����
	 */
	public CertificateFeeAssign findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��CertificateFeeAssign��¼
	 * @param appSpecies CertificateFeeAssign����
	 * @return ����ɹ�������true�����򷵻�false
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
	 * ����һ��CertificateFeeAssign��¼
	 * @param appSpecies CertificateFeeAssign����
	 * @return ���³ɹ�������true�����򷵻�false
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
	 * ����CertificateFeeAssign Id,ɾ��CertificateFeeAssign����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
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
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param arr ������ֵ��
	 * @return ��ҳ���CertificateFeeAssign�б�
	 */
	public List<CertificateFeeAssign> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) throws Exception {
		return m_dao.findPagedAll("CertificateFeeAssign", currentPage,pageSize, arr);
	}
	
	/**
	 * �õ�����CertificateFeeAssign��¼��
	 * @param arr ������ֵ��
	 * @return CertificateFeeAssign��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("CertificateFeeAssign", arr);
	}
	/**
	 * �õ�����CertificateFeeAssign��¼��
	 * @param arr ��ѯ�����б�
	 * @return CertificateFeeAssign��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("CertificateFeeAssign", arr);
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<CertificateFeeAssign> findByExample(CertificateFeeAssign instance) {
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
	* @return ��ҳ��������б�- List<CertificateFeeAssign>
	*/
	public List<CertificateFeeAssign> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		return m_dao.findPagedAllBySort("CertificateFeeAssign", currentPage, pageSize, orderby, asc, arr);
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
	* @return ��ҳ��������б�- List<CertificateFeeAssign>
	*/
	public List<CertificateFeeAssign> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		return m_dao.findPagedAllBySort("CertificateFeeAssign", currentPage, pageSize, orderby, asc, arr);
	}
	
	/**
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findByHQL(String queryString, Object...arr){
		return m_dao.findByHQL(queryString, arr);
	}
	/**
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findByHQL(String queryString, List<Object> arr){
		return m_dao.findByHQL(queryString, arr);
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
		return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
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
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,Object...arr) throws Exception{
		return m_dao.getTotalCountByHQL(queryString, arr);
	}
	/**
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,List<Object> arr) throws Exception{
		return m_dao.getTotalCountByHQL(queryString, arr);
	}
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<CertificateFeeAssign> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("CertificateFeeAssign", arr);
	}
	
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<CertificateFeeAssign> findByVarProperty(List<KeyValueWithOperator> arr){
		return m_dao.findByVarProperty("CertificateFeeAssign", arr);
	}
	
	/**
	 * Ϊһ��֤��������
	 * @param oRecord��֤��������ԭʼ��¼
	 * @param certificate��֤��
	 * @param feeAssignList�����÷����б�
	 * @return
	 * @throws Exception
	 */
	public void executeFeeAssign(OriginalRecord oRecord, Certificate certificate, List<CertificateFeeAssign> feeAssignList) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try{
			//ɾ����ԭʼ��¼�¸�֤����ǰ�����м�¼
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
	 * ��ί�е�����֤�����
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
	 * Ϊһ��ί�е�������ã�ת���ѡ���������ѵȣ��������춨֤��ķ��ã�
	 * @param cSheet��ί�е�
	 * @param feeAssignList�����÷����б�
	 * @return
	 * @throws Exception
	 */
	public void executeFeeAssignWithoutCertificate(CommissionSheet cSheet, List<CertificateFeeAssign> feeAssignList) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try{
			//ɾ����ί�е��ѷ������ǰ�����м�¼���������춨֤��ķ��ü�¼��
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
	 * �����޸�֤�����
	 *
	 * @param feeAssignList�����÷����б�
	 * @return
	 * @throws Exception
	 */
	public void updateFeeAssign(List<CertificateFeeAssign> feeAssignList) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try{
			//����ԭʼ��¼���õ�HQL
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
			
			//�����ø�����ԭʼ��¼������
			tran.commit();
		}catch(Exception e){
			tran.rollback();
			throw e;
		}finally{
			m_dao.closeSession();
		}
	}
}
