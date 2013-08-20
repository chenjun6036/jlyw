package com.jlyw.manager.quotation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Quotation;
import com.jlyw.hibernate.QuotationDAO;
import com.jlyw.hibernate.QuotationItem;
import com.jlyw.hibernate.QuotationItemDAO;
import com.jlyw.util.KeyValueWithOperator;


public class QuotationItemManager {
private QuotationItemDAO m_dao = new QuotationItemDAO();
	
	/**
	 * ����QuotationItem Id ���� QuotationItem����
	 * @param id: QuotationItem Id
	 * @return QuotationItem����
	 */
	public QuotationItem findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��QuotationItem��¼
	 * @param appSpecies QuotationItem����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(QuotationItem appSpecies){
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
	 * ����һ��QuotationItem��¼
	 * @param appSpecies QuotationItem����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(QuotationItem appSpecies){
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
	 * ����QuotationItem Id,ɾ��QuotationItem����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			QuotationItem u = m_dao.findById(id);
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
	 * @return ��ҳ���QuotationItem�б�
	 */
	public List<QuotationItem> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("QuotationItem", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����QuotationItem��¼��
	 * @param arr ������ֵ��
	 * @return QuotationItem��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("QuotationItem", arr);
	}
	/**
	 * �õ�����QuotationItem��¼��
	 * @param arr ��ѯ�����б�
	 * @return QuotationItem��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("QuotationItem", arr);
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<QuotationItem> findByExample(QuotationItem instance) {
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
	* @return ��ҳ��������б�- List<QuotationItem>
	*/
	public List<QuotationItem> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("QuotationItem", currentPage, pageSize, orderby, asc, arr);
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
	* @return ��ҳ��������б�- List<QuotationItem>
	*/
	public List<QuotationItem> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("QuotationItem", currentPage, pageSize, orderby, asc, arr);
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
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<QuotationItem> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("QuotationItem", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<QuotationItem> findByPropertyBySort(String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findByPropertyBySort("QuotationItem",orderby,asc,arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<QuotationItem> findByVarProperty(List<KeyValueWithOperator> arr){
		try{
			return m_dao.findByVarProperty("QuotationItem", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * �½�Quotation������ӱ��۵���Ŀ��Ϣ
	 * 
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean saveByBatch(List<QuotationItem> quotationItemList,Quotation quotation) {
		if(quotationItemList == null ||quotation==null){
			return false;
		}
		QuotationDAO dDAO = new QuotationDAO();	//���۵���DAO 
		
		Transaction tran = m_dao.getSession().beginTransaction();		
		try {			
			dDAO.save(quotation);			
			for(int i = 0; i < quotationItemList.size(); i++){
				QuotationItem quotationItem = quotationItemList.get(i);
				quotationItem.setQuotation(quotation);
				m_dao.save(quotationItem);				
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

	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		
		//result.add(((QuotationItem)obj).getId().toString());
		result.add(((QuotationItem)obj).getQuotation().getNumber());
		result.add(((QuotationItem)obj).getCertificateName()==null?"":((QuotationItem)obj).getCertificateName().toString());
		result.add(((QuotationItem)obj).getStandardName()==null?"":((QuotationItem)obj).getStandardName().toString());
		result.add(((QuotationItem)obj).getModel()==null?"":((QuotationItem)obj).getModel().toString());
		result.add(((QuotationItem)obj).getRange()==null?"":((QuotationItem)obj).getRange().toString());
		result.add(((QuotationItem)obj).getAccuracy()==null?"":((QuotationItem)obj).getAccuracy().toString());
		String certType=((QuotationItem)obj).getCertType()==null?"":((QuotationItem)obj).getCertType().toString();
		if(certType.equals("1"))
			certType="�춨";
		else if(certType.equals("2"))
			certType="У׼";
		else if(certType.equals("3"))
			certType="���";
		else if(certType.equals("4"))
			certType="����";
		result.add(certType);
		String siteTest=((QuotationItem)obj).getSiteTest()==null?"":((QuotationItem)obj).getSiteTest().toString();
		if(siteTest.equals("true"))
			siteTest="��";
		else
			siteTest="��";
		result.add(siteTest);
		
		result.add(((QuotationItem)obj).getAppFactoryCode()==null?"":((QuotationItem)obj).getAppFactoryCode().toString());
		result.add(((QuotationItem)obj).getAppManageCode()==null?"":((QuotationItem)obj).getAppManageCode().toString());
		result.add(((QuotationItem)obj).getManufacturer()==null?"":((QuotationItem)obj).getManufacturer().toString());	
		result.add(((QuotationItem)obj).getQuantity()==null?"1":((QuotationItem)obj).getQuantity().toString());	
		String cost="";
		if(((QuotationItem)obj).getMinCost().equals(((QuotationItem)obj).getMaxCost()))
			cost=((QuotationItem)obj).getMinCost();
		else
			cost=String.format("%s~%s", ((QuotationItem)obj).getMinCost(),((QuotationItem)obj).getMaxCost());
		
		String totalcost="";
		if(((QuotationItem)obj).getMinCost().equals(((QuotationItem)obj).getMaxCost()))
			totalcost=String.format("%s",Double.parseDouble(((QuotationItem)obj).getMinCost())*((QuotationItem)obj).getQuantity());
		else
			totalcost=String.format("%s~%s", Double.parseDouble(((QuotationItem)obj).getMinCost())*((QuotationItem)obj).getQuantity(),Double.parseDouble(((QuotationItem)obj).getMaxCost())*((QuotationItem)obj).getQuantity());
		result.add(cost);
		result.add(totalcost);
		
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("���۵���");
		result.add("�ܼ�����֤������");
		result.add("�ܼ����߱�׼����");
		result.add("�ͺŹ��");
		result.add("������Χ");
		result.add("׼ȷ�ȵȼ�");
		result.add("֤������");
		result.add("�Ƿ��ֳ����");
		
		result.add("�������");
		result.add("������");
		result.add("���쳧");
		result.add("̨����");
		result.add("Ԫ/̨��");
		result.add("�ܼ���");
	
		return result;
	}
}
