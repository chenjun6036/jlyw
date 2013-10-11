package com.jlyw.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.jlyw.hibernate.RemakeCertificate;
import com.jlyw.hibernate.RemakeCertificateDAO;
import com.jlyw.util.KeyValueWithOperator;

public class RemakeCertificateManager {
	private RemakeCertificateDAO m_dao = new RemakeCertificateDAO();
	/**
	 * 根据RemakeCertificate Id 查找 RemakeCertificate对象
	 * @param id: RemakeCertificate Id
	 * @return RemakeCertificate对象
	 */
	public RemakeCertificate findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条RemakeCertificate记录
	 * @param appSpecies RemakeCertificate对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(RemakeCertificate appSpecies){
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
	 * 更新一条RemakeCertificate记录
	 * @param appSpecies RemakeCertificate对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(RemakeCertificate appSpecies){
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
	 * 根据RemakeCertificate Id,删除RemakeCertificate对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			RemakeCertificate u = m_dao.findById(id);
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
	 * @param arr 条件键值对
	 * @return 分页后的RemakeCertificate列表
	 */
	public List<RemakeCertificate> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("RemakeCertificate", currentPage,pageSize, arr);
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
	 * 得到所有RemakeCertificate记录数
	 * @param arr 条件键值对
	 * @return RemakeCertificate记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("RemakeCertificate", arr);
	}
	/**
	 * 得到所有RemakeCertificate记录数
	 * @param arr 查询条件列表
	 * @return RemakeCertificate记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("RemakeCertificate", arr);
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
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<RemakeCertificate> findByExample(RemakeCertificate instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	* 分页显示数据
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param orderby：按照哪个字段排序
	* @param asc：true 增序 false 减序
	* @param arr:为查询条件的(键-值)对数组
	* @return 分页后的数据列表- List<RemakeCertificate>
	*/
	public List<RemakeCertificate> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("RemakeCertificate", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	* 分页显示数据
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param orderby：按照哪个字段排序
	* @param asc：true 增序 false 减序
	* @param arr:为查询条件的(键-值)对列表
	* @return 分页后的数据列表- List<RemakeCertificate>
	*/
	public List<RemakeCertificate> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("RemakeCertificate", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findByHQL(String queryString, Object...arr) throws Exception{
		try{
			return m_dao.findByHQL(queryString, arr);
		}catch(Exception e){
			throw e;
		}
	}
	
	/**
	 * 按任意条件对查找，可指定排序字段
	 * @param TableName:从哪个表里查询
	 * @param orderby：按照哪个字段排序
	 * @param asc：true 增序 false 减序
	 * @param arr ：条件与值对
	 */
	public List<RemakeCertificate> findByPropertyBySort(String orderby, boolean asc, KeyValueWithOperator...arr){
		try{
			return m_dao.findByPropertyBySort("RemakeCertificate", orderby, asc, arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 按任意条件对查找，可指定排序字段
	 * @param TableName:从哪个表里查询
	 * @param orderby：按照哪个字段排序
	 * @param asc：true 增序 false 减序
	 * @param arr ：条件与值对
	 */
	public List<RemakeCertificate> findByPropertyBySort(String orderby, boolean asc, List<KeyValueWithOperator> arr){
		try{
			return m_dao.findByPropertyBySort("RemakeCertificate", orderby, asc, arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 按任意条件对查找，可指定排序字段
	 * @param TableName:从哪个表里查询

	 * @param arr ：条件与值对
	 */
	public List<RemakeCertificate> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("RemakeCertificate", arr);
	}
	
	
	public List<String> formatExcel(Object obj) throws JSONException{
		List<String> result = new ArrayList<String>();
		JSONObject jsonObj = (JSONObject)obj;
		result.add(jsonObj.has("CommissionCode")?jsonObj.getString("CommissionCode"):"");
		result.add(jsonObj.has("ApplianceName")?jsonObj.getString("ApplianceName"):"");
		result.add(jsonObj.has("CustomerName")?jsonObj.getString("CustomerName"):"");
		result.add(jsonObj.has("CustomerContactor")?jsonObj.getString("CustomerContactor"):"");
		result.add(jsonObj.has("CustomerContactorTel")?jsonObj.getString("CustomerContactorTel"):"");
		result.add(jsonObj.has("CommissionDate")?jsonObj.getString("CommissionDate"):"");
		result.add(jsonObj.has("CertificateCode")?jsonObj.getString("CertificateCode"):"");
		result.add(jsonObj.has("CreatorName")?jsonObj.getString("CreatorName"):"");
		result.add(jsonObj.has("CreateTime")?jsonObj.getString("CreateTime"):"");
		result.add(jsonObj.has("CreateRemark")?jsonObj.getString("CreateRemark"):"");
		result.add(jsonObj.has("ReceiverName")?jsonObj.getString("ReceiverName"):"");
		result.add(jsonObj.has("FinishTime")?jsonObj.getString("FinishTime"):"");
		result.add(jsonObj.has("FinishRemark")?jsonObj.getString("FinishRemark"):"");
		result.add(jsonObj.has("PassedTime")?jsonObj.getString("PassedTime"):"");
		
		return result;
	}

	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("委托单号");
		result.add("器具名称");
		result.add("委托单位");
		result.add("联系人");
		result.add("联系人电话");
		result.add("委托日期");
		result.add("原证书编号");
		result.add("创建人");
		result.add("创建时间");
		result.add("备注");
		result.add("编制人");
		result.add("完成时间");
		result.add("备注");
		result.add("通过时间");
		
		return result;
	}
}
