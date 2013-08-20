/**
 * 
 */
package com.jlyw.manager.crm;

import java.util.List;

import org.hibernate.Transaction;


import com.jlyw.hibernate.crm.InsideContactorFeeAssign;
import com.jlyw.hibernate.crm.InsideContactorFeeAssignDAO;
import com.jlyw.util.KeyValueWithOperator;

/**
 * @author xx
 *
 */
public class InsideContactorFeeAssignManager {
private InsideContactorFeeAssignDAO m_dao=new InsideContactorFeeAssignDAO();
		/**
		 * 
		 */
		public InsideContactorFeeAssign FindById(int id)
		{
			return m_dao.findById(id);
		}
		
		public boolean save(InsideContactorFeeAssign c){
			Transaction tran = m_dao.getSession().beginTransaction();
			try {	
				m_dao.save(c);
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
		 * ����һ��CustomerServiceFee��¼
		 * @param CustomerServiceFee CustomerServiceFee����
		 * @return ���³ɹ�������true�����򷵻�false
		 */
		public boolean update(InsideContactorFeeAssign c){
			Transaction tran = m_dao.getSession().beginTransaction();
			try {			
				m_dao.update(c);
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
		public boolean deleteById(int id){
			Transaction tran = m_dao.getSession().beginTransaction();
			try {			
				InsideContactorFeeAssign u = m_dao.findById(id);
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
		 * ��������ϲ�ѯ
		 * @param instance ���������
		 * @return ���������ļ�¼
		 */
		public List findByExample(InsideContactorFeeAssign instance) {
			return m_dao.findByExample(instance);
			
		}
		
		/**
		 * ��������ѯ
		 * @param arr
		 * @return
		 */
		public List<InsideContactorFeeAssign> findByVarProperty(KeyValueWithOperator...arr){
			return m_dao.findByVarProperty("InsideContactorFeeAssign", arr);
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
		public int getTotalCountByHQL(String queryString,List<Object> arr) {
			try{
				return m_dao.getTotalCountByHQL(queryString, arr);
			}catch(Exception ex){
				return 0;
			}
		}
		
		public InsideContactorFeeAssignManager() {
			// TODO Auto-generated constructor stub


	}


}
