package com.jlyw.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Address;
import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.Standard;
import com.jlyw.hibernate.StandardDAO;
import com.jlyw.util.KeyValueWithOperator;

public class StandardManager {

	private StandardDAO m_dao = new StandardDAO();
	/**
	 * ����Standard Id ���� Standard����
	 * 
	 * @param id
	 *            Standard Id
	 * @return Standard����
	 */
	public Standard findById(int id) {
		return m_dao.findById(id);
	}

	/**
	 * ����һ��Standard��¼
	 * 
	 * @param Standard
	 *            Standard����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(Standard Standard) {
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			m_dao.save(Standard);
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
	 * ����һ��Standard��¼
	 * 
	 * @param Standard
	 *            Standard����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(Standard Standard) {
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			m_dao.update(Standard);
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
	 * ����Standard Id,ɾ��Standard����
	 * 
	 * @param id
	 *            Standard id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id) {
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			Standard u = m_dao.findById(id);
			if (u == null) {
				return true;
			} else {
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
	 * 
	 * @param currentPage
	 *            ��ǰҳ��
	 * @param pageSize
	 *            ÿҳ�ļ�¼��
	 * @return ��ҳ���Standard�б�
	 */
	public List<Standard> findPagedAll(int currentPage, int pageSize,
			KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("Standard", currentPage, pageSize, arr);
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
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,Object...arr) {
		return m_dao.getTotalCountByHQL(queryString, arr);
	}
	
	/**
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,List<Object> arr) {
		return m_dao.getTotalCountByHQL(queryString, arr);
	}

	/**
	 * �õ�����Standard��¼��
	 * 
	 * @return Standard��¼��
	 */
	public int getTotalCount() {
		return m_dao.getTotalCount("Standard");
	}

	/**
	 * �õ�����Standard��¼��
	 * @return Standard��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Standard",arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * 
	 * @param instance
	 *            ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(Standard instance) {
		return m_dao.findByExample(instance);

	}
	
	/**
	* ��ѯ����
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
	 * ����������
	 * @param arr
	 * @return
	 */
	public List<Standard> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Standard", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		
		result.add(((Standard)obj).getId().toString());
		result.add(((Standard)obj).getName().toString());
		result.add(((Standard)obj).getNameEn().toString());
		result.add(((Standard)obj).getBrief().toString());
		result.add(((Standard)obj).getCertificateCode().toString());
		result.add(((Standard)obj).getStandardCode().toString());
		result.add(((Standard)obj).getProjectCode()==null?"":((Standard)obj).getProjectCode().toString());
		result.add(((Standard)obj).getStatus().toString());		
		result.add(((Standard)obj).getCreatedBy().toString());		
		result.add(((Standard)obj).getIssuedBy().toString());
		result.add(((Standard)obj).getIssueDate().toString());
		result.add(((Standard)obj).getValidDate().toString());
		result.add(((Standard)obj).getRange().toString());
		result.add(((Standard)obj).getUncertain().toString());
		result.add(((Standard)obj).getSissuedBy().toString());
		result.add(((Standard)obj).getSissueDate().toString());
		result.add(((Standard)obj).getSvalidDate().toString());
		result.add(((Standard)obj).getScertificateCode().toString());
		
		result.add(((Standard)obj).getSregion().toString());
		result.add(((Standard)obj).getSauthorizationCode().toString());
		result.add(((Standard)obj).getSlocaleCode().toString());
		result.add(((Standard)obj).getCopy().toString());
		result.add(((Standard)obj).getScopy().toString());
		result.add(((Standard)obj).getRemark()==null?"":((Standard)obj).getRemark().toString());
		result.add(((Standard)obj).getWarnSlot()==null?"":((Standard)obj).getWarnSlot().toString());
		result.add(((Standard)obj).getSysUser().getName().toString());
		result.add(((Standard)obj).getProjectType().toString());
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("���");
		result.add("��׼����");
		result.add("��׼����Ӣ��");
		result.add("ƴ������");
		result.add("������׼֤���");
		result.add("��׼����");
		result.add("��Ŀ���");
		result.add("״̬");
		result.add("���굥λ");
		result.add("��֤��λ");
		result.add("��֤����");
		result.add("��Ч��");
		result.add("������Χ");
		result.add("��ȷ�������");
		result.add("���֤��֤����");
		result.add("���֤��֤����");
		result.add("���֤��Ч����");
		result.add("���֤֤���");
		
		result.add("���֤��Ч����");
		result.add("���֤��Ȩ֤���");
		result.add("���ڱ��");
		result.add("������׼ɨ���");
		result.add("���֤ɨ���");
		result.add("��ע");
		result.add("��������ʱ��");
		result.add("��׼������");
		result.add("��׼����");
		
		
		return result;
	}

}
