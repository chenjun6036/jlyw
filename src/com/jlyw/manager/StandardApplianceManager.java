package com.jlyw.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.StandardAppliance;
import com.jlyw.hibernate.StandardApplianceDAO;
import com.jlyw.util.KeyValueWithOperator;

public class StandardApplianceManager {
private StandardApplianceDAO m_dao = new StandardApplianceDAO();
	
	/**
	 * 根据User Id 查找 User对象
	 * @param id User Id
	 * @return StandardAppliance对象
	 */
	public StandardAppliance findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条StandardAppliance记录
	 * @param StandardAppliance StandardAppliance对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(StandardAppliance StandardAppliance){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(StandardAppliance);
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
	 * 更新一条StandardAppliance记录
	 * @param StandardAppliance StandardAppliance对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(StandardAppliance StandardAppliance){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(StandardAppliance);
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
	 * 根据StandardAppliance Id,删除StandardAppliance对象
	 * @param id StandardAppliance id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			StandardAppliance u = m_dao.findById(id);
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
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @return 分页后的StandardAppliance列表
	 */
	public List<StandardAppliance> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("StandardAppliance", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @return 分页后的StandardAppliance列表
	 */
	public List<StandardAppliance> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("StandardAppliance", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有StandardAppliance记录数
	 * @return StandardAppliance记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("StandardAppliance",arr);		
	}
	
	/**
	 * 得到所有StandardAppliance记录数
	 * @return StandardAppliance记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("StandardAppliance",arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(StandardAppliance instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<StandardAppliance> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("StandardAppliance", arr);
		}
		catch(Exception e){
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
	* 显示数据
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findByHQL(String queryString, Object...arr){
		try{
			return m_dao.findByHQL(queryString, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		result.add(((StandardAppliance)obj).getId().toString());
		result.add(((StandardAppliance)obj).getName().toString());
		result.add(((StandardAppliance)obj).getLocaleCode().toString());
		result.add(((StandardAppliance)obj).getBrief().toString());
		result.add(((StandardAppliance)obj).getModel().toString());
		result.add(((StandardAppliance)obj).getRange().toString());
		result.add(((StandardAppliance)obj).getUncertain().toString());
		result.add(((StandardAppliance)obj).getTestCycle().toString());
		result.add(((StandardAppliance)obj).getSeriaNumber().toString());
		result.add(((StandardAppliance)obj).getManufacturer().toString());
		result.add(((StandardAppliance)obj).getReleaseDate()==null?"":((StandardAppliance)obj).getReleaseDate().toString());
		result.add(((StandardAppliance)obj).getReleaseNumber().toString());
		result.add(((StandardAppliance)obj).getNum().toString());
		result.add(((StandardAppliance)obj).getStatus().toString());
		result.add(((StandardAppliance)obj).getPrice().toString());
		result.add(((StandardAppliance)obj).getSysUser().getName().toString());
		result.add(((StandardAppliance)obj).getPermanentCode().toString());
		result.add(((StandardAppliance)obj).getProjectCode().toString());
		result.add(((StandardAppliance)obj).getBudget().toString());
		result.add(((StandardAppliance)obj).getInspectMonth()==null?"":((StandardAppliance)obj).getInspectMonth().toString());
		result.add(((StandardAppliance)obj).getWarnSlot()==null?"":((StandardAppliance)obj).getWarnSlot().toString());
		result.add(((StandardAppliance)obj).getValidDate()==null?"":((StandardAppliance)obj).getValidDate().toString());
		result.add(((StandardAppliance)obj).getRemark()==null?"":((StandardAppliance)obj).getRemark().toString());		
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("序号");
		result.add("器具名称");
		result.add("所内编号");
		result.add("拼音简码");
		result.add("型号规格");
		result.add("测量范围");
		result.add("不确定度");
		result.add("检定周期");
		result.add("证书号");
		result.add("生产厂商");
		result.add("出厂日期");
		result.add("出厂编号");
		result.add("器件数量");
		result.add("器具状态");
		result.add("器具价格");
		result.add("保管人");
		result.add("固定资产编号");
		result.add("项目编号");
		result.add("预算资金");
		result.add("受检月份");
		result.add("有效期预警天数");
		result.add("有效期");
		result.add("备注");
		
		return result;
	}
	
}
