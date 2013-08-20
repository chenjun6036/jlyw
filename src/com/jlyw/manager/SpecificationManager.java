package com.jlyw.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Specification;
import com.jlyw.hibernate.SpecificationDAO;
import com.jlyw.hibernate.TgtAppSpec;
import com.jlyw.hibernate.TgtAppSpecDAO;
import com.jlyw.util.KeyValue;
import com.jlyw.util.KeyValueWithOperator;

public class SpecificationManager {
private SpecificationDAO m_dao = new SpecificationDAO();
	
	/**
	 * ����User Id ���� User����
	 * @param id User Id
	 * @return Specification����
	 */
	public Specification findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��Specification��¼
	 * @param Specification Specification����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(Specification Specification){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(Specification);
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
	 * ����һ��Specification��¼
	 * @param Specification Specification����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(Specification Specification){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(Specification);
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
	 * ����Specification Id,ɾ��Specification����
	 * @param id Specification id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Specification u = m_dao.findById(id);
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
	 * @return ��ҳ���Specification�б�
	 */
	public List<Specification> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("Specification", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @return ��ҳ���Specification�б�
	 */
	public List<Specification> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("Specification", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����Specification��¼��
	 * @return Specification��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Specification",arr);		
	}
	
	/**
	 * �õ�����Specification��¼��
	 * @return Specification��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("Specification",arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(Specification instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<Specification> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Specification", arr);
		}
		catch(Exception e){
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
	 * �滻���
	 * @param specification
	 * @return
	 */
	public boolean replace(Specification specification){
		TgtAppSpecDAO tgtAppSpecDAO = new TgtAppSpecDAO();
		Transaction tran = m_dao.getSession().beginTransaction();
		try{
			String oldSpecificationCode = specification.getOldSpecification();
			Specification oldSpec = (Specification) m_dao.findByOldSpecification(oldSpecificationCode).get(0);
			oldSpec.setStatus(1);
			specification.setStatus(0);
			List<KeyValue> key = new ArrayList<KeyValue>();
			key.add(new KeyValue("specification.id", specification.getId()));
			tgtAppSpecDAO.update("TgtAppSpec", key, new KeyValue("specification.id", oldSpec.getId()));
			m_dao.update(oldSpec);
			m_dao.save(specification);
			tran.commit();
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			tran.rollback();
			return false;
		}finally{
			m_dao.closeSession();
		}
	}
	
	/**
	 * �����滻��̣��ں�̨��ʱ������ʹ�á�
	 * @param specList
	 * @return
	 */
	public boolean replaceByList(List<Specification> specList){
		TgtAppSpecDAO tgtAppSpecDAO = new TgtAppSpecDAO();
		Transaction tran = m_dao.getSession().beginTransaction();
		try{
			for(Specification temp : specList){
				String oldSpecificationCode = temp.getOldSpecification();
				Specification oldSpec = (Specification) m_dao.findByOldSpecification(oldSpecificationCode).get(0);
				oldSpec.setStatus(1);
				temp.setStatus(0);
				List<KeyValue> key = new ArrayList<KeyValue>();
				key.add(new KeyValue("specification.id", temp.getId()));
				tgtAppSpecDAO.update("TgtAppSpec", key, new KeyValue("specification.id", oldSpec.getId()));
				m_dao.update(oldSpec);
				m_dao.update(temp);
			}
			tran.commit();
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			tran.rollback();
			return false;
		}finally{
			m_dao.closeSession();
		}
	}
	
	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
	
		result.add(((Specification)obj).getId().toString());
		result.add(((Specification)obj).getSpecificationCode().toString());
		result.add(((Specification)obj).getNameCn().toString());
		result.add(((Specification)obj).getNameEn()==null?"":((Specification)obj).getNameEn().toString());
		result.add(((Specification)obj).getBrief().toString());
		result.add(((Specification)obj).getType().toString());
		result.add(((Specification)obj).getInCharge().toString());	
		result.add(((Specification)obj).getStatus().toString());
		
		result.add(((Specification)obj).getLocaleCode()==null?"":((Specification)obj).getLocaleCode().toString());
		result.add(((Specification)obj).getIssueDate().toString());
		result.add(((Specification)obj).getEffectiveDate().toString());

		result.add(((Specification)obj).getRepealDate()==null?"":((Specification)obj).getRepealDate().toString());
		result.add(((Specification)obj).getOldSpecification()==null?"":((Specification)obj).getOldSpecification().toString());
		result.add(((Specification)obj).getCopy()==null?"":((Specification)obj).getCopy().toString());
		result.add(((Specification)obj).getMethodConfirm()==null?"":((Specification)obj).getMethodConfirm().toString());
		result.add(((Specification)obj).getRemark()==null?"":((Specification)obj).getRemark().toString());
		result.add(((Specification)obj).getWarnSlot()==null?"":((Specification)obj).getWarnSlot().toString());
		
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("���");
		result.add("�����淶���");
		result.add("��λ����");
		result.add("��λ����Ӣ��");
		result.add("ƴ������");
		result.add("���");
		result.add("�Ƿ��ܿ�");		
		result.add("״̬");
		
		result.add("���ڱ��");
		result.add("��������");
		result.add("ʵʩ����");
		result.add("��ֹ����");
		result.add("����μ����淶");
		result.add("�����淶ɨ���");
		result.add("����ȷ�ϱ�ɨ���");
	
		result.add("��ע");
		result.add("��������ʱ��");
		
		return result;
	}
	
	
}
