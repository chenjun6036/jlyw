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
	 * 根据Standard Id 查找 Standard对象
	 * 
	 * @param id
	 *            Standard Id
	 * @return Standard对象
	 */
	public Standard findById(int id) {
		return m_dao.findById(id);
	}

	/**
	 * 插入一条Standard记录
	 * 
	 * @param Standard
	 *            Standard对象
	 * @return 插入成功，返回true；否则返回false
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
	 * 更新一条Standard记录
	 * 
	 * @param Standard
	 *            Standard对象
	 * @return 更新成功，返回true；否则返回false
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
	 * 根据Standard Id,删除Standard对象
	 * 
	 * @param id
	 *            Standard id
	 * @return 删除成功，返回true；否则返回false
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
	 * 分页函数
	 * 
	 * @param currentPage
	 *            当前页码
	 * @param pageSize
	 *            每页的记录数
	 * @return 分页后的Standard列表
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
		return m_dao.getTotalCountByHQL(queryString, arr);
	}
	
	/**
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,List<Object> arr) {
		return m_dao.getTotalCountByHQL(queryString, arr);
	}

	/**
	 * 得到所有Standard记录数
	 * 
	 * @return Standard记录数
	 */
	public int getTotalCount() {
		return m_dao.getTotalCount("Standard");
	}

	/**
	 * 得到所有Standard记录数
	 * @return Standard记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Standard",arr);		
	}
	
	/**
	 * 多条件组合查询
	 * 
	 * @param instance
	 *            条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(Standard instance) {
		return m_dao.findByExample(instance);

	}
	
	/**
	* 查询数据
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
	
	/**
	 * 按条件查找
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
		result.add("序号");
		result.add("标准名称");
		result.add("标准名称英文");
		result.add("拼音简码");
		result.add("计量标准证书号");
		result.add("标准代码");
		result.add("项目编号");
		result.add("状态");
		result.add("建标单位");
		result.add("发证单位");
		result.add("发证日期");
		result.add("有效期");
		result.add("测量范围");
		result.add("不确定度误差");
		result.add("社会证发证机关");
		result.add("社会证发证日期");
		result.add("社会证有效日期");
		result.add("社会证证书号");
		
		result.add("社会证有效区域");
		result.add("社会证授权证书号");
		result.add("所内编号");
		result.add("计量标准扫描件");
		result.add("社会证扫描件");
		result.add("备注");
		result.add("过期提醒时长");
		result.add("标准负责人");
		result.add("标准类型");
		
		
		return result;
	}

}
