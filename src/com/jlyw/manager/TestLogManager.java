package com.jlyw.manager;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.StandardAppliance;
import com.jlyw.hibernate.StandardApplianceDAO;
import com.jlyw.hibernate.TestLog;
import com.jlyw.hibernate.TestLogDAO;
import com.jlyw.hibernate.Vehicle;
import com.jlyw.util.KeyValueWithOperator;

public class TestLogManager {
	private TestLogDAO m_dao = new TestLogDAO();
	/**
	 * ����TestLog Id ���� TestLog����
	 * 
	 * @param id
	 *            TestLog Id
	 * @return TestLog����
	 */
	public TestLog findById(int id) {
		return m_dao.findById(id);
	}

	/**
	 * ����һ��TestLog��¼
	 * 
	 * @param TestLog
	 *            TestLog����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(TestLog TestLog) {
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			m_dao.save(TestLog);
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
	 * ����һ��TestLog��¼
	 * 
	 * @param TestLog
	 *            TestLog����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(TestLog TestLog) {
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			m_dao.update(TestLog);
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
	 * ����TestLog Id,ɾ��TestLog����
	 * 
	 * @param id
	 *            TestLog id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id) {
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			TestLog u = m_dao.findById(id);
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
	 * @return ��ҳ���TestLog�б�
	 */
	public List<TestLog> findPagedAll(int currentPage, int pageSize,
			KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("TestLog", currentPage, pageSize, arr);
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
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return �����б�- List
	*/
	public List findByHQL(String queryString, Object...arr){
		try{
			return m_dao.findByHQL(queryString, arr);
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
	public List findByHQL(String queryString, List<Object> ...arr){
		try{
			return m_dao.findByHQL(queryString, arr);
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
	 * 
	 * @param arr
	 * @return
	 */
	public List<TestLog> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("TestLog", arr);
		}
		catch(Exception e){
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
	 * �õ�����TestLog��¼��
	 * 
	 * @return TestLog��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr) {
		return m_dao.getTotalCount("TestLog",arr);
	}

	/**
	 * ��������ϲ�ѯ
	 * 
	 * @param instance
	 *            ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(TestLog instance) {
		return m_dao.findByExample(instance);

	}
	
	/**
	 * ������Դ��¼�������¶�Ӧ��׼���ߵ�֤��ź���Ч��
	 * @param testlog
	 * @return
	 * @throws Exception
	 */
	public boolean saveTestLog(TestLog testlog) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			m_dao.save(testlog);
			
			StandardApplianceDAO stdAppDao = new StandardApplianceDAO();
			List<TestLog> retList = m_dao.findByPropertyBySort("TestLog", "validDate", false, new KeyValueWithOperator("standardAppliance.id", testlog.getStandardAppliance().getId(), "="), new KeyValueWithOperator("status", Integer.valueOf(0), "="));
			if(retList.size() > 0){
				StandardAppliance stdApp = stdAppDao.findById(testlog.getStandardAppliance().getId());
				stdApp.setValidDate(retList.get(0).getValidDate());
				stdApp.setSeriaNumber(retList.get(0).getCertificateId());
				stdAppDao.update(stdApp);
			}
			tran.commit();
			return true;
		} catch (Exception e) {
			tran.rollback();
			throw new Exception(String.format("�������ݿ�ʧ�ܣ���Ϣ��%s", e.getMessage()==null?"��":e.getMessage()));
		} finally {
			m_dao.closeSession();
		}
	}
	
	/**
	 * ������Դ��¼�������¶�Ӧ��׼���ߵ���Ч��
	 * @param testlog
	 * @return
	 * @throws Exception
	 */
	public boolean updateTestLog(TestLog testlog) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			m_dao.update(testlog);
			
			StandardApplianceDAO stdAppDao = new StandardApplianceDAO();
			List<TestLog> retList = m_dao.findByPropertyBySort("TestLog", "validDate", false, new KeyValueWithOperator("standardAppliance.id", testlog.getStandardAppliance().getId(), "="), new KeyValueWithOperator("status", Integer.valueOf(0), "="));
			if(retList.size() > 0){
				StandardAppliance stdApp = stdAppDao.findById(testlog.getStandardAppliance().getId());
				stdApp.setValidDate(retList.get(0).getValidDate());
				stdApp.setSeriaNumber(retList.get(0).getCertificateId());
				stdAppDao.update(stdApp);
			}
			tran.commit();
			return true;
		} catch (Exception e) {
			tran.rollback();
			throw new Exception(String.format("�������ݿ�ʧ�ܣ���Ϣ��%s", e.getMessage()==null?"��":e.getMessage()));
		} finally {
			m_dao.closeSession();
		}
	}
	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		
		result.add(((TestLog)obj).getId().toString());
		result.add(((TestLog)obj).getStandardAppliance().getName().toString());
		result.add(((TestLog)obj).getStandardAppliance().getLocaleCode().toString());
		result.add(((TestLog)obj).getStandardAppliance().getModel().toString());
		result.add(((TestLog)obj).getStandardAppliance().getRange().toString());
		result.add(((TestLog)obj).getStandardAppliance().getUncertain().toString());
		result.add(((TestLog)obj).getTestDate().toString());
		result.add(((TestLog)obj).getValidDate().toString());
		result.add(((TestLog)obj).getTester().toString());
		result.add(((TestLog)obj).getCertificateId().toString());
		result.add(((TestLog)obj).getConfirmMeasure()==null?"":((TestLog)obj).getConfirmMeasure().toString());
		result.add(((TestLog)obj).getSysUser()==null?"":((TestLog)obj).getSysUser().getName().toString());
		result.add(((TestLog)obj).getConfirmDate()==null?"":((TestLog)obj).getConfirmDate().toString());
		result.add(((TestLog)obj).getDurationCheck()==null?"":((TestLog)obj).getDurationCheck().toString());
		result.add(((TestLog)obj).getMaintenance()==null?"":((TestLog)obj).getMaintenance().toString());
		result.add(((TestLog)obj).getStatus().toString());
		result.add(((TestLog)obj).getRemark()==null?"":((TestLog)obj).getRemark().toString());
				
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("���");
		result.add("��׼��������");
		result.add("��׼�������ڱ��");
		result.add("��׼�����ͺ�");
		result.add("��׼���߲�����Χ");
		result.add("��׼���߲�ȷ����");
		result.add("�춨����");	
		result.add("��Ч����");
		result.add("�춨��λ");
		result.add("�춨֤����");
		result.add("��Դ���ȷ���������ʩ");
		result.add("��Դ���ȷ����");
		result.add("��Դ���ȷ������");
		result.add("�ڼ�˲�");
		result.add("ά������");
		result.add("״̬");
		result.add("��ע");
		
		return result;
	}
	
}
