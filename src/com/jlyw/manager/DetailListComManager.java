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
 * ԭʼ��¼
 * @author Administrator
 *
 */
public class DetailListComManager {
private DetailListComDAO m_dao = new DetailListComDAO();
	
	/**
	 * ����DetailListCom Id ���� DetailListCom����
	 * @param id: DetailListCom Id
	 * @return DetailListCom����
	 */
	public DetailListCom findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��DetailListCom��¼
	 * @param appSpecies DetailListCom����
	 * @return ����ɹ�������true�����򷵻�false
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
	 * ����һ��DetailListCom��¼
	 * @param appSpecies DetailListCom����
	 * @return ���³ɹ�������true�����򷵻�false
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
	 * ����DetailListCom Id,ɾ��DetailListCom����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
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
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param arr ������ֵ��
	 * @return ��ҳ���DetailListCom�б�
	 */
	public List<DetailListCom> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("DetailListCom", currentPage,pageSize, arr);
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
	 * �õ�����DetailListCom��¼��
	 * @param arr ������ֵ��
	 * @return DetailListCom��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("DetailListCom", arr);
	}
	/**
	 * �õ�����DetailListCom��¼��
	 * @param arr ��ѯ�����б�
	 * @return DetailListCom��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("DetailListCom", arr);
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
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<DetailListCom> findByExample(DetailListCom instance) {
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
	* @return ��ҳ��������б�- List<DetailListCom>
	*/
	public List<DetailListCom> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("DetailListCom", currentPage, pageSize, orderby, asc, arr);
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
	* @return ��ҳ��������б�- List<DetailListCom>
	*/
	public List<DetailListCom> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("DetailListCom", currentPage, pageSize, orderby, asc, arr);
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
	public List findByHQL(String queryString, Object...arr){
		try{
			return m_dao.findByHQL(queryString, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * �����������Բ��ң���ָ�������ֶ�
	 * @param TableName:���ĸ������ѯ
	 * @param orderby�������ĸ��ֶ�����
	 * @param asc��true ���� false ����
	 * @param arr ��������ֵ��
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
	 * ����һ��DetailListCom��¼
	 * @param oRecordList DetailListCom �����б�
	 * @return ����ɹ�������true�����򷵻�false
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
	 * �ۿ�����������һ���嵥-ί�е�������Ϣ�������嵥�������ۿ۰�����Ϣ
	 * dscfee[]�ۿ���
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean updateFeeByBatch(List<DetailListCom> detailListComList,DetailList deList,double dscfee[],Discount o) {
		if(detailListComList == null || dscfee.length == 0||dscfee==null||deList==null){
			return false;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		DecimalFormat   df   =new   DecimalFormat("#.00");	
		try {
			DetailListDAO dDAO = new DetailListDAO();	//�嵥���DAO 
			DiscountDAO discountDAO = new DiscountDAO();	//�ۿ۱��DAO 
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
