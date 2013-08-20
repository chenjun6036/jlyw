package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.CertificateFeeAssign;
import com.jlyw.hibernate.CertificateFeeAssignDAO;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.Discount;
import com.jlyw.hibernate.DiscountComSheet;
import com.jlyw.hibernate.DiscountComSheetDAO;
import com.jlyw.hibernate.DiscountDAO;
import com.jlyw.util.KeyValueWithOperator;

public class DiscountManager {
	//HQL��䣺����ί�е�Id��ѯ��ί�е��µ�����ԭʼ����֮��
	public static final String queryStringAllOldFeeByCommissionSheetId = CertificateFeeAssignManager.queryStringAllOldFeeByCommissionSheetId;
	//HQL��䣺����ί�е�Id��ѯ��ί�е��µ����з��ü�¼
	public static final String queryString_CertificateFeeAssignByCommissionSheetId = CertificateFeeAssignManager.queryString_CertificateFeeAssignByCommissionSheetId;
	private DiscountDAO m_dao = new DiscountDAO();
	
	/**
	 * ����Discount Id ���� Discount����
	 * @param id: Discount Id
	 * @return Discount����
	 */
	public Discount findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��Discount��¼
	 * @param appSpecies Discount����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(Discount appSpecies) throws Exception{
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
	 * ����һ��Discount��¼
	 * @param appSpecies Discount����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(Discount appSpecies) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(appSpecies);
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
	 * ����Discount Id,ɾ��Discount����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Discount u = m_dao.findById(id);
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
			throw e;
		} finally {
			m_dao.closeSession();
		}
	}	
	
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param arr ������ֵ��
	 * @return ��ҳ���Discount�б�
	 */
	public List<Discount> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		return m_dao.findPagedAll("Discount", currentPage,pageSize, arr);
	}
	
	/**
	 * �õ�����Discount��¼��
	 * @param arr ������ֵ��
	 * @return Discount��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Discount", arr);
	}
	/**
	 * �õ�����Discount��¼��
	 * @param arr ��ѯ�����б�
	 * @return Discount��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("Discount", arr);
	}
	
	/**
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,Object...arr) {
		return m_dao.getTotalCountByHQL(queryString, arr);
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<Discount> findByExample(Discount instance) {
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
	* @return ��ҳ��������б�- List<Discount>
	*/
	public List<Discount> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		return m_dao.findPagedAllBySort("Discount", currentPage, pageSize, orderby, asc, arr);
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
	* @return ��ҳ��������б�- List<Discount>
	*/
	public List<Discount> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		return m_dao.findPagedAllBySort("Discount", currentPage, pageSize, orderby, asc, arr);
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
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<Discount> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("Discount", arr);
	}
	
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<Discount> findByVarProperty(List<KeyValueWithOperator> arr){
		return m_dao.findByVarProperty("Discount", arr);
	}
	/**
	 * �����������Բ��ң���ָ�������ֶ�
	 * @param TableName:���ĸ������ѯ
	 * @param orderby�������ĸ��ֶ�����
	 * @param asc��true ���� false ����
	 * @param arr ��������ֵ��
	 */
	public List<Discount> findByPropertyBySort(String orderby, boolean asc, KeyValueWithOperator...arr){
		return m_dao.findByPropertyBySort("Discount", orderby, asc, arr);
	}
	
	/**
	 * ִ�д��ۣ�����ͨ�����ۿ����뵥���ί�е����д���
	 * @param discount:�ۿ۵�
	 * @return
	 */
	public boolean discountExecute(Discount discount) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			DiscountComSheetDAO dCSheetDAO = new DiscountComSheetDAO();
			CertificateFeeAssignDAO feeAssignDAO = new CertificateFeeAssignDAO();
			List<DiscountComSheet> dCSheetList = dCSheetDAO.findByVarProperty("DiscountComSheet", 
					new KeyValueWithOperator("discount.id", discount.getId(), "="));	//��������ί�е����ۿ���Ϣ
			for(DiscountComSheet dCSheet : dCSheetList){
				CommissionSheet cSheet = dCSheet.getCommissionSheet();
				if(cSheet.getCheckOutDate() != null && cSheet.getDetailListCode() != null){	//ί�е��ѽ���
					throw new Exception(String.format("ί�е�%s�Ѿ����ˣ������嵥�ţ�%s��������ִ���ۿ۸ķѲ�����", cSheet.getCode(), cSheet.getDetailListCode()));
				}
				
				List<Object[]> feeList = feeAssignDAO.findByHQL(queryStringAllOldFeeByCommissionSheetId, cSheet.getId());	//ί�е��ĸ����ܷ��õ�ԭ��
				if(feeList.isEmpty()){
			    	throw new Exception(String.format("ί�е�%sû���κη��ü�¼������ִ���ۿ۲�����", cSheet.getCode()));
			    }else{	//ƽ̯��ÿ��֤����
				    Object []tempObjArray = feeList.get(0);
				    Double TestFeeOld = tempObjArray[0]==null?0:(Double)tempObjArray[0];	//��ί�е�ԭʼ�ĸ����ܷ���
				    Double RepairFeeOld = tempObjArray[1]==null?0:(Double)tempObjArray[1];
				    Double MaterialFeeOld = tempObjArray[2]==null?0:(Double)tempObjArray[2];
				    Double CarFeeOld = tempObjArray[3]==null?0:(Double)tempObjArray[3];
				    Double DebugFeeOld = tempObjArray[4]==null?0:(Double)tempObjArray[4];
				    Double OtherFeeOld = tempObjArray[5]==null?0:(Double)tempObjArray[5];
				    Double TotalFeeOld = tempObjArray[6]==null?0:(Double)tempObjArray[6];
				   
				    List<CertificateFeeAssign> feeAssignList = feeAssignDAO.findByHQL(queryString_CertificateFeeAssignByCommissionSheetId, cSheet.getId());
				    if(feeAssignList.size() == 0){
				    	throw new Exception(String.format("ί�е�%sû���κη��ü�¼������ִ���ۿ۲�����", cSheet.getCode()));
				    }
				    int indexMaxTestFee = -1, indexMaxRepairFee = -1, indexMaxMaterialFee = -1, indexMaxCarFee = -1, indexMaxDebugFee = -1, indexMaxOtherFee = -1;	//���ַ������ֵ��������
				    Double maxTestFee = null, maxRepairFee = null, maxMaterialFee = null, maxCarFee = null, maxDebugFee = null, maxOtherFee = null;	//���ַ��õ����ֵ
				    Double countTestFee = 0.0, countRepairFee = 0.0, countMaterialFee = 0.0, countCarFee = 0.0, countDebugFee = 0.0, countOtherFee = 0.0;	//���ַ��õ��ܼ�
				    Double dRateTestFee = (TestFeeOld == null || TestFeeOld <= 0 || dCSheet.getTestFee() == null)?0:dCSheet.getTestFee()/TestFeeOld;	//�ۿ���
				    Double dRateRepairFee = (RepairFeeOld == null || RepairFeeOld <= 0 || dCSheet.getRepairFee() == null)?0:dCSheet.getRepairFee()/RepairFeeOld;
				    Double dRateMaterialFee = (MaterialFeeOld == null || MaterialFeeOld <= 0 || dCSheet.getMaterialFee() == null)?0:dCSheet.getMaterialFee()/MaterialFeeOld;
				    Double dRateCarFee = (CarFeeOld == null || CarFeeOld == 0 || dCSheet.getCarFee() == null)?0:dCSheet.getCarFee()/CarFeeOld;
				    Double dRateDebugFee = (DebugFeeOld == null || DebugFeeOld == 0 || dCSheet.getDebugFee() == null)?0:dCSheet.getDebugFee()/DebugFeeOld;
				    Double dRateOtherFee = (OtherFeeOld == null || OtherFeeOld == 0 || dCSheet.getOtherFee() == null)?0:dCSheet.getOtherFee()/OtherFeeOld;
				    
				    for(int i = 0; i < feeAssignList.size(); i++){
				    	CertificateFeeAssign feeAssign = feeAssignList.get(i);
				    	feeAssign.setTestFee(new Double(Math.round((feeAssign.getTestFeeOld()==null?0:feeAssign.getTestFeeOld())*dRateTestFee)));	//���·���
				    	feeAssign.setRepairFee(new Double(Math.round((feeAssign.getRepairFeeOld()==null?0:feeAssign.getRepairFeeOld())*dRateRepairFee)));
				    	feeAssign.setMaterialFee(new Double(Math.round((feeAssign.getMaterialFeeOld()==null?0:feeAssign.getMaterialFeeOld())*dRateMaterialFee)));
				    	feeAssign.setCarFee(new Double(Math.round((feeAssign.getCarFeeOld()==null?0:feeAssign.getCarFeeOld())*dRateCarFee)));
				    	feeAssign.setDebugFee(new Double(Math.round((feeAssign.getDebugFeeOld()==null?0:feeAssign.getDebugFeeOld())*dRateDebugFee)));
				    	feeAssign.setOtherFee(new Double(Math.round((feeAssign.getOtherFeeOld()==null?0:feeAssign.getOtherFeeOld())*dRateOtherFee)));
				    	feeAssign.setTotalFee(new Double(feeAssign.getTestFee().intValue() + feeAssign.getRepairFee().intValue() + feeAssign.getMaterialFee().intValue() + feeAssign.getCarFee().intValue() + feeAssign.getDebugFee().intValue() + feeAssign.getOtherFee().intValue()));
				    	
				    	//�ж���������
				    	if(maxTestFee == null || maxTestFee <= feeAssign.getTestFee()){
				    		maxTestFee = feeAssign.getTestFee();
				    		indexMaxTestFee = i;
				    	}
				    	if(maxRepairFee == null || maxRepairFee <= feeAssign.getRepairFee()){
				    		maxRepairFee = feeAssign.getRepairFee();
				    		indexMaxRepairFee = i;
				    	}
				    	if(maxMaterialFee == null || maxMaterialFee <= feeAssign.getMaterialFee()){
				    		maxMaterialFee = feeAssign.getMaterialFee();
				    		indexMaxMaterialFee = i;
				    	}
				    	if(maxCarFee == null || maxCarFee <= feeAssign.getCarFee()){
				    		maxCarFee = feeAssign.getCarFee();
				    		indexMaxCarFee = i;
				    	}
				    	if(maxDebugFee == null || maxDebugFee <= feeAssign.getDebugFee()){
				    		maxDebugFee = feeAssign.getDebugFee();
				    		indexMaxDebugFee = i;
				    	}
				    	if(maxOtherFee == null || maxOtherFee <= feeAssign.getOtherFee()){
				    		maxOtherFee = feeAssign.getOtherFee();
				    		indexMaxOtherFee = i;
				    	}
				    	//������ַ���֮��
				    	countTestFee += feeAssign.getTestFee().intValue();
				    	countRepairFee += feeAssign.getRepairFee().intValue();
				    	countMaterialFee += feeAssign.getMaterialFee().intValue();
				    	countCarFee += feeAssign.getCarFee().intValue();
				    	countDebugFee += feeAssign.getDebugFee().intValue();
				    	countOtherFee += feeAssign.getOtherFee().intValue();
				    	
				    }//end for
				    
				    //�������̯�������
				    if(indexMaxTestFee >= 0){
				    	CertificateFeeAssign f = feeAssignList.get(indexMaxTestFee);
				    	Double balance = (dCSheet.getTestFee()==null?0:dCSheet.getTestFee()) - countTestFee;
				    	f.setTestFee(new Double(Math.round(f.getTestFee() + balance)));
				    	f.setTotalFee(new Double(f.getTestFee().intValue() + f.getRepairFee().intValue() + f.getMaterialFee().intValue() + f.getCarFee().intValue() + f.getDebugFee().intValue() + f.getOtherFee().intValue()));
				    }
				    if(indexMaxRepairFee >= 0){
				    	CertificateFeeAssign f = feeAssignList.get(indexMaxRepairFee);
				    	Double balance = (dCSheet.getRepairFee()==null?0:dCSheet.getRepairFee()) - countRepairFee;
				    	f.setRepairFee(new Double(Math.round(f.getRepairFee() + balance)));
				    	f.setTotalFee(new Double(f.getTestFee().intValue() + f.getRepairFee().intValue() + f.getMaterialFee().intValue() + f.getCarFee().intValue() + f.getDebugFee().intValue() + f.getOtherFee().intValue()));
				    }
				    if(indexMaxMaterialFee >= 0){
				    	CertificateFeeAssign f = feeAssignList.get(indexMaxMaterialFee);
				    	Double balance = (dCSheet.getMaterialFee()==null?0:dCSheet.getMaterialFee()) - countMaterialFee;
				    	f.setMaterialFee(new Double(Math.round(f.getMaterialFee() + balance)));
				    	f.setTotalFee(new Double(f.getTestFee().intValue() + f.getRepairFee().intValue() + f.getMaterialFee().intValue() + f.getCarFee().intValue() + f.getDebugFee().intValue() + f.getOtherFee().intValue()));
				    }
				    if(indexMaxCarFee >= 0){
				    	CertificateFeeAssign f = feeAssignList.get(indexMaxCarFee);
				    	Double balance = (dCSheet.getCarFee()==null?0:dCSheet.getCarFee()) - countCarFee;
				    	f.setCarFee(new Double(Math.round(f.getCarFee() + balance)));
				    	f.setTotalFee(new Double(f.getTestFee().intValue() + f.getRepairFee().intValue() + f.getMaterialFee().intValue() + f.getCarFee().intValue() + f.getDebugFee().intValue() + f.getOtherFee().intValue()));
				    }
				    if(indexMaxDebugFee >= 0){
				    	CertificateFeeAssign f = feeAssignList.get(indexMaxDebugFee);
				    	Double balance = (dCSheet.getDebugFee()==null?0:dCSheet.getDebugFee()) - countDebugFee;
				    	f.setDebugFee(new Double(Math.round(f.getDebugFee() + balance)));
				    	f.setTotalFee(new Double(f.getTestFee().intValue() + f.getRepairFee().intValue() + f.getMaterialFee().intValue() + f.getCarFee().intValue() + f.getDebugFee().intValue() + f.getOtherFee().intValue()));
				    }
				    if(indexMaxOtherFee >= 0){
				    	CertificateFeeAssign f = feeAssignList.get(indexMaxOtherFee);
				    	Double balance = (dCSheet.getOtherFee()==null?0:dCSheet.getOtherFee()) - countOtherFee;
				    	f.setOtherFee(new Double(Math.round(f.getOtherFee() + balance)));
				    	f.setTotalFee(new Double(f.getTestFee().intValue() + f.getRepairFee().intValue() + f.getMaterialFee().intValue() + f.getCarFee().intValue() + f.getDebugFee().intValue() + f.getOtherFee().intValue()));
				    }
				    
				    //���·��ü�¼
				    for(CertificateFeeAssign feeAssign : feeAssignList){
				    	feeAssignDAO.update(feeAssign);
				    } //end ���·��ü�¼
			    }//end else
			}//end for������һ��ί�е�
			m_dao.update(discount);			
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
}
