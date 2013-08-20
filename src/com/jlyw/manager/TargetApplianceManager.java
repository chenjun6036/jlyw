package com.jlyw.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.TargetAppliance;
import com.jlyw.hibernate.TargetApplianceDAO;
import com.jlyw.util.KeyValueWithOperator;

public class TargetApplianceManager {
private TargetApplianceDAO m_dao = new TargetApplianceDAO();
	
	/**
	 * 根据TargetAppliance Id 查找 TargetAppliance对象
	 * @param id: TargetAppliance Id
	 * @return TargetAppliance对象
	 */
	public TargetAppliance findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条TargetAppliance记录
	 * @param appSpecies TargetAppliance对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(TargetAppliance appSpecies){
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
	 * 更新一条TargetAppliance记录
	 * @param appSpecies TargetAppliance对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(TargetAppliance appSpecies){
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
	 * 根据TargetAppliance Id,删除TargetAppliance对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			TargetAppliance u = m_dao.findById(id);
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
	 * @return 分页后的TargetAppliance列表
	 */
	public List<TargetAppliance> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("TargetAppliance", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有TargetAppliance记录数
	 * @param arr 条件键值对
	 * @return TargetAppliance记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("TargetAppliance", arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<TargetAppliance> findByExample(TargetAppliance instance) {
		return m_dao.findByExample(instance);
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
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize,List<Object> arr){
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
	public List findByHQL(String queryString,List<Object> arr){
		try{
			return m_dao.findByHQL(queryString,arr);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 查找符合条件的受检器具
	 * @param speciesName：授权名称
	 * @param stanrdOrPopularName：器具标准名称或常用名称
	 * @param Model：型号规格
	 * @return
	 */
//	public Object findTargetAppliance(String speciesName, String stanrdOrPopularName, String Model){
//		try{
//			m_dao.getSession().createQuery("select * " +
//					" from ApplianceSpecies as spec,ApplianceStandardName as sname,TargetAppliance as tapp,AppliancePopularName as pname " +
//					" where spec.name=? and sname.name=? and tapp.");
//		}catch(Exception e){
//			return null;
//		}
//	}
	
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
	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		Object[] temp = (Object[]) obj;
		result.add(((TargetAppliance)temp[0]).getApplianceStandardName().getName().toString());
		result.add(((TargetAppliance)temp[0]).getName().toString());
		result.add(((TargetAppliance)temp[0]).getBrief().toString());
		result.add(((TargetAppliance)temp[0]).getNameEn().toString());
		result.add(((TargetAppliance)temp[0]).getCode().toString());
		result.add(((TargetAppliance)temp[0]).getFee()==null?"":((TargetAppliance)temp[0]).getFee().toString());
		result.add(((TargetAppliance)temp[0]).getSrfee()==null?"":((TargetAppliance)temp[0]).getSrfee().toString());
		result.add(((TargetAppliance)temp[0]).getMrfee()==null?"":((TargetAppliance)temp[0]).getMrfee().toString());
		result.add(((TargetAppliance)temp[0]).getLrfee()==null?"":((TargetAppliance)temp[0]).getLrfee().toString());
		result.add(((TargetAppliance)temp[0]).getPromiseDuration()==null?"":((TargetAppliance)temp[0]).getPromiseDuration().toString());
		result.add(((TargetAppliance)temp[0]).getTestCycle()==null?"":((TargetAppliance)temp[0]).getTestCycle().toString());
		if(((Long)temp[2])!=null&&((Long)temp[2]).equals(new Long(1)))
			result.add((String)temp[1]);
		else
			result.add("");
		if(((Long)temp[4])!=null&&((Long)temp[4]).equals(new Long(1)))
			result.add((String)temp[3]);
		else
			result.add("");
		if(((Long)temp[6])!=null&&((Long)temp[6]).equals(new Long(1)))
			result.add((String)temp[5]);
		else
			result.add("");
		String Certification="";
		String cer = Integer.toBinaryString(((TargetAppliance)temp[0]).getCertification());
		if(cer.length()==1)
			cer = "00" + cer;
		if(cer.length()==2)
			cer = "0" + cer;
		if(cer.charAt(0)=='1')
			Certification = "法定机构授权、";
		if(cer.charAt(1)=='1')
			Certification += "实验室认可、";
		if(cer.charAt(2)=='1')
			Certification += "计量认证、";
		if(Certification.length()!=0)
			Certification = Certification.substring(0, Certification.length()-1);
		result.add(Certification);
		result.add(((TargetAppliance)temp[0]).getStatus().toString());
		result.add(((TargetAppliance)temp[0]).getRemark()==null?"":((TargetAppliance)temp[0]).getRemark().toString());		
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("器具标准名称");
		result.add("受检器具名称");
		result.add("拼音简码");
		result.add("受检器具英文名称");
		result.add("器具编码");
		result.add("标准费用");
		result.add("小修费用");
		result.add("中修费用");
		result.add("大修费用");
		result.add("承诺检出期");
		result.add("检定周期");
		result.add("型号规格");
		result.add("测量范围");
		result.add("准确度等级/不确定度");
		result.add("认证");
		result.add("状态");
		result.add("备注");
		
		return result;
	}
	
}
