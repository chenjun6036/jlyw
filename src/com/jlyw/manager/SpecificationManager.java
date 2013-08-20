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
	 * 根据User Id 查找 User对象
	 * @param id User Id
	 * @return Specification对象
	 */
	public Specification findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条Specification记录
	 * @param Specification Specification对象
	 * @return 插入成功，返回true；否则返回false
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
	 * 更新一条Specification记录
	 * @param Specification Specification对象
	 * @return 更新成功，返回true；否则返回false
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
	 * 根据Specification Id,删除Specification对象
	 * @param id Specification id
	 * @return 删除成功，返回true；否则返回false
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
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @return 分页后的Specification列表
	 */
	public List<Specification> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("Specification", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @return 分页后的Specification列表
	 */
	public List<Specification> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("Specification", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有Specification记录数
	 * @return Specification记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Specification",arr);		
	}
	
	/**
	 * 得到所有Specification记录数
	 * @return Specification记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("Specification",arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
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
	 * 替换规程
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
	 * 批量替换规程，在后台定时服务中使用。
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
		result.add("序号");
		result.add("技术规范编号");
		result.add("单位名称");
		result.add("单位名称英文");
		result.add("拼音简码");
		result.add("类别");
		result.add("是否受控");		
		result.add("状态");
		
		result.add("所内编号");
		result.add("发布日期");
		result.add("实施日期");
		result.add("废止日期");
		result.add("替代何技术规范");
		result.add("技术规范扫描件");
		result.add("方法确认表扫描件");
	
		result.add("备注");
		result.add("过期提醒时长");
		
		return result;
	}
	
	
}
