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
	 * 根据TestLog Id 查找 TestLog对象
	 * 
	 * @param id
	 *            TestLog Id
	 * @return TestLog对象
	 */
	public TestLog findById(int id) {
		return m_dao.findById(id);
	}

	/**
	 * 插入一条TestLog记录
	 * 
	 * @param TestLog
	 *            TestLog对象
	 * @return 插入成功，返回true；否则返回false
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
	 * 更新一条TestLog记录
	 * 
	 * @param TestLog
	 *            TestLog对象
	 * @return 更新成功，返回true；否则返回false
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
	 * 根据TestLog Id,删除TestLog对象
	 * 
	 * @param id
	 *            TestLog id
	 * @return 删除成功，返回true；否则返回false
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
	 * 分页函数
	 * 
	 * @param currentPage
	 *            当前页码
	 * @param pageSize
	 *            每页的记录数
	 * @return 分页后的TestLog列表
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
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
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
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 数据列表- List
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
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 数据列表- List
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
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
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
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
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
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
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
	 * 得到所有TestLog记录数
	 * 
	 * @return TestLog记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr) {
		return m_dao.getTotalCount("TestLog",arr);
	}

	/**
	 * 多条件组合查询
	 * 
	 * @param instance
	 *            条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(TestLog instance) {
		return m_dao.findByExample(instance);

	}
	
	/**
	 * 插入溯源记录，并更新对应标准器具的证书号和有效期
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
			throw new Exception(String.format("更新数据库失败！消息：%s", e.getMessage()==null?"无":e.getMessage()));
		} finally {
			m_dao.closeSession();
		}
	}
	
	/**
	 * 更新溯源记录，并更新对应标准器具的有效期
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
			throw new Exception(String.format("更新数据库失败！消息：%s", e.getMessage()==null?"无":e.getMessage()));
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
		result.add("序号");
		result.add("标准器具名称");
		result.add("标准器具所内编号");
		result.add("标准器具型号");
		result.add("标准器具测量范围");
		result.add("标准器具不确定度");
		result.add("检定日期");	
		result.add("有效日期");
		result.add("检定单位");
		result.add("检定证书编号");
		result.add("溯源结果确认意见及措施");
		result.add("溯源结果确认人");
		result.add("溯源结果确认日期");
		result.add("期间核查");
		result.add("维护保养");
		result.add("状态");
		result.add("备注");
		
		return result;
	}
	
}
