package com.jlyw.manager;



import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.DrivingVehicle;
import com.jlyw.hibernate.DrivingVehicleDAO;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.SysUserDAO;
import com.jlyw.hibernate.UserRole;
import com.jlyw.hibernate.VehicleMission;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.KeyValueWithOperator;


public class DrivingVehicleManager {
	private DrivingVehicleDAO m_dao = new DrivingVehicleDAO();
	
	/**
	 * ����User Id ���� User����
	 * @param id User Id
	 * @return User����
	 */
	public DrivingVehicle findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��User��¼
	 * @param user User����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(DrivingVehicle user){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(user);
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
	 * ����һ��User��¼
	 * @param user User����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(DrivingVehicle user){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(user);
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
	 * ����User Id,ɾ��User����
	 * @param id User id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			DrivingVehicle u = m_dao.findById(id);
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
	 * @return ��ҳ���User�б�
	 */
	public List<DrivingVehicle> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("DrivingVehicle", currentPage,pageSize, arr);
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
	public List<DrivingVehicle> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("DrivingVehicle", currentPage, pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * �õ�����User��¼��
	 * @return User��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("DrivingVehicle", arr);		
	}
	
	/**
	 * �õ�����User��¼��
	 * @return User��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("DrivingVehicle",arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(DrivingVehicle instance) {
		return m_dao.findByExample(instance);
	}
	

	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<DrivingVehicle> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("DrivingVehicle", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	/**
	
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List<DrivingVehicle> findAllByHQL(String queryString,List<Object> arr){
		try{
			return m_dao.findByHQL(queryString,arr);
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
	* ��ҳ��ʾ����
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param orderby�������ĸ��ֶ�����
	* @param asc��true ���� false ����
	* @param arr:Ϊ��ѯ������(��-ֵ)������
	* @return ��ҳ��������б�- List<TaskAssign>
	*/
	public List<DrivingVehicle> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator> condList){
		try{
			return m_dao.findPagedAllBySort("DrivingVehicle", currentPage, pageSize, orderby, asc, condList);
		}catch(Exception e){
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
	 *����һ��DrivingVehicle��¼
	 * @param DrivingVehicleList DrivingVehicle����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean updateByBatch(List<DrivingVehicle> DrivingVehicleList){
		if(DrivingVehicleList == null || DrivingVehicleList.size() == 0){
			return false;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			for(DrivingVehicle drivingVehicle : DrivingVehicleList){
				m_dao.update(drivingVehicle);
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
		
		result.add(((DrivingVehicle)obj).getId().toString());
		result.add(((DrivingVehicle)obj).getVehicle().getLicence().toString());
		result.add(((DrivingVehicle)obj).getSysUserByDriverId().getName().toString());
		result.add(DateTimeFormatUtil.DateFormat.format(((DrivingVehicle)obj).getBeginDate()));
		//result.add(DateTimeFormatUtil.DateTimeFormat.format(((DrivingVehicle)obj).getEndDate()));
		result.add(((DrivingVehicle)obj).getPeople()==null?"":((DrivingVehicle)obj).getPeople().toString());
		result.add(((DrivingVehicle)obj).getAssemblingPlace()==null?"":((DrivingVehicle)obj).getAssemblingPlace().toString());
		result.add(((DrivingVehicle)obj).getKilometers()==null?"0":((DrivingVehicle)obj).getKilometers().toString());
		result.add(((DrivingVehicle)obj).getTotalFee()==null?"0":((DrivingVehicle)obj).getTotalFee().toString());
		result.add(((DrivingVehicle)obj).getStatus()==0?"δ����":"�ѽ���");
		result.add(((DrivingVehicle)obj).getSysUserBySettlementId()==null?"":((DrivingVehicle)obj).getSysUserBySettlementId().getName().toString());
		result.add(((DrivingVehicle)obj).getSettlementTime()==null?"":DateTimeFormatUtil.DateFormat.format(((DrivingVehicle)obj).getSettlementTime()));
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("���");
		result.add("���ƺ�");
		result.add("˾��");
		result.add("��������");
		result.add("�˳���Ա");
		result.add("���ϵص�");
		result.add("��ʻ������");
		result.add("�ܷ���");
		result.add("�Ƿ����");
		result.add("������");
		result.add("����ʱ��");
		
		return result;
	}
	
}

