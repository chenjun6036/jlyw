package com.jlyw.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.TargetAppliance;
import com.jlyw.hibernate.TargetApplianceDAO;
import com.jlyw.util.KeyValueWithOperator;

public class TargetApplianceManager {
private TargetApplianceDAO m_dao = new TargetApplianceDAO();
	
	/**
	 * ����TargetAppliance Id ���� TargetAppliance����
	 * @param id: TargetAppliance Id
	 * @return TargetAppliance����
	 */
	public TargetAppliance findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��TargetAppliance��¼
	 * @param appSpecies TargetAppliance����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(TargetAppliance appSpecies){
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
	 * ����һ��TargetAppliance��¼
	 * @param appSpecies TargetAppliance����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(TargetAppliance appSpecies){
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
	 * ����TargetAppliance Id,ɾ��TargetAppliance����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			TargetAppliance u = m_dao.findById(id);
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
	 * @return ��ҳ���TargetAppliance�б�
	 */
	public List<TargetAppliance> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("TargetAppliance", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����TargetAppliance��¼��
	 * @param arr ������ֵ��
	 * @return TargetAppliance��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("TargetAppliance", arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<TargetAppliance> findByExample(TargetAppliance instance) {
		return m_dao.findByExample(instance);
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
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize,List<Object> arr){
		try{
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return �����б�- List
	*/
	public List findByHQL(String queryString,List<Object> arr){
		try{
			return m_dao.findByHQL(queryString,arr);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * ���ҷ����������ܼ�����
	 * @param speciesName����Ȩ����
	 * @param stanrdOrPopularName�����߱�׼���ƻ�������
	 * @param Model���ͺŹ��
	 * @return
	 */
//	public Object findTargetAppliance(String speciesName, String stanrdOrPopularName, String Model){
//		try{
//			m_dao.getSession().createQuery("select * " +
//					" from ApplianceSpecies as spec,ApplianceStandardName as sname,TargetAppliance as tapp,AppliancePopularName as pname " +
//					" where spec.name=? and sname.name=? and tapp.");
//		}catch(Exception e){
//			return null;
//		}
//	}
	
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
	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		Object[] temp = (Object[]) obj;
		result.add(((TargetAppliance)temp[0]).getApplianceStandardName().getName().toString());
		result.add(((TargetAppliance)temp[0]).getName().toString());
		result.add(((TargetAppliance)temp[0]).getBrief().toString());
		result.add(((TargetAppliance)temp[0]).getNameEn().toString());
		result.add(((TargetAppliance)temp[0]).getCode().toString());
		result.add(((TargetAppliance)temp[0]).getFee()==null?"":((TargetAppliance)temp[0]).getFee().toString());
		result.add(((TargetAppliance)temp[0]).getSrfee()==null?"":((TargetAppliance)temp[0]).getSrfee().toString());
		result.add(((TargetAppliance)temp[0]).getMrfee()==null?"":((TargetAppliance)temp[0]).getMrfee().toString());
		result.add(((TargetAppliance)temp[0]).getLrfee()==null?"":((TargetAppliance)temp[0]).getLrfee().toString());
		result.add(((TargetAppliance)temp[0]).getPromiseDuration()==null?"":((TargetAppliance)temp[0]).getPromiseDuration().toString());
		result.add(((TargetAppliance)temp[0]).getTestCycle()==null?"":((TargetAppliance)temp[0]).getTestCycle().toString());
		if(((Long)temp[2])!=null&&((Long)temp[2]).equals(new Long(1)))
			result.add((String)temp[1]);
		else
			result.add("");
		if(((Long)temp[4])!=null&&((Long)temp[4]).equals(new Long(1)))
			result.add((String)temp[3]);
		else
			result.add("");
		if(((Long)temp[6])!=null&&((Long)temp[6]).equals(new Long(1)))
			result.add((String)temp[5]);
		else
			result.add("");
		String Certification="";
		String cer = Integer.toBinaryString(((TargetAppliance)temp[0]).getCertification());
		if(cer.length()==1)
			cer = "00" + cer;
		if(cer.length()==2)
			cer = "0" + cer;
		if(cer.charAt(0)=='1')
			Certification = "����������Ȩ��";
		if(cer.charAt(1)=='1')
			Certification += "ʵ�����Ͽɡ�";
		if(cer.charAt(2)=='1')
			Certification += "������֤��";
		if(Certification.length()!=0)
			Certification = Certification.substring(0, Certification.length()-1);
		result.add(Certification);
		result.add(((TargetAppliance)temp[0]).getStatus().toString());
		result.add(((TargetAppliance)temp[0]).getRemark()==null?"":((TargetAppliance)temp[0]).getRemark().toString());		
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("���߱�׼����");
		result.add("�ܼ���������");
		result.add("ƴ������");
		result.add("�ܼ�����Ӣ������");
		result.add("���߱���");
		result.add("��׼����");
		result.add("С�޷���");
		result.add("���޷���");
		result.add("���޷���");
		result.add("��ŵ�����");
		result.add("�춨����");
		result.add("�ͺŹ��");
		result.add("������Χ");
		result.add("׼ȷ�ȵȼ�/��ȷ����");
		result.add("��֤");
		result.add("״̬");
		result.add("��ע");
		
		return result;
	}
	
}
