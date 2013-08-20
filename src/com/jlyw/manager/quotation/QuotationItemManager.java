package com.jlyw.manager.quotation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Quotation;
import com.jlyw.hibernate.QuotationDAO;
import com.jlyw.hibernate.QuotationItem;
import com.jlyw.hibernate.QuotationItemDAO;
import com.jlyw.util.KeyValueWithOperator;


public class QuotationItemManager {
private QuotationItemDAO m_dao = new QuotationItemDAO();
	
	/**
	 * 根据QuotationItem Id 查找 QuotationItem对象
	 * @param id: QuotationItem Id
	 * @return QuotationItem对象
	 */
	public QuotationItem findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条QuotationItem记录
	 * @param appSpecies QuotationItem对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(QuotationItem appSpecies){
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
	 * 更新一条QuotationItem记录
	 * @param appSpecies QuotationItem对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(QuotationItem appSpecies){
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
	 * 根据QuotationItem Id,删除QuotationItem对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			QuotationItem u = m_dao.findById(id);
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
	 * @return 分页后的QuotationItem列表
	 */
	public List<QuotationItem> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("QuotationItem", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有QuotationItem记录数
	 * @param arr 条件键值对
	 * @return QuotationItem记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("QuotationItem", arr);
	}
	/**
	 * 得到所有QuotationItem记录数
	 * @param arr 查询条件列表
	 * @return QuotationItem记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("QuotationItem", arr);
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<QuotationItem> findByExample(QuotationItem instance) {
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
	* @return 分页后的数据列表- List<QuotationItem>
	*/
	public List<QuotationItem> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("QuotationItem", currentPage, pageSize, orderby, asc, arr);
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
	* @return 分页后的数据列表- List<QuotationItem>
	*/
	public List<QuotationItem> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("QuotationItem", currentPage, pageSize, orderby, asc, arr);
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
	public List findByHQL(String queryString, Object...arr){
		try{
			return m_dao.findByHQL(queryString, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<QuotationItem> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("QuotationItem", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<QuotationItem> findByPropertyBySort(String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findByPropertyBySort("QuotationItem",orderby,asc,arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<QuotationItem> findByVarProperty(List<KeyValueWithOperator> arr){
		try{
			return m_dao.findByVarProperty("QuotationItem", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 新建Quotation，并添加报价单条目信息
	 * 
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean saveByBatch(List<QuotationItem> quotationItemList,Quotation quotation) {
		if(quotationItemList == null ||quotation==null){
			return false;
		}
		QuotationDAO dDAO = new QuotationDAO();	//报价单的DAO 
		
		Transaction tran = m_dao.getSession().beginTransaction();		
		try {			
			dDAO.save(quotation);			
			for(int i = 0; i < quotationItemList.size(); i++){
				QuotationItem quotationItem = quotationItemList.get(i);
				quotationItem.setQuotation(quotation);
				m_dao.save(quotationItem);				
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
		
		//result.add(((QuotationItem)obj).getId().toString());
		result.add(((QuotationItem)obj).getQuotation().getNumber());
		result.add(((QuotationItem)obj).getCertificateName()==null?"":((QuotationItem)obj).getCertificateName().toString());
		result.add(((QuotationItem)obj).getStandardName()==null?"":((QuotationItem)obj).getStandardName().toString());
		result.add(((QuotationItem)obj).getModel()==null?"":((QuotationItem)obj).getModel().toString());
		result.add(((QuotationItem)obj).getRange()==null?"":((QuotationItem)obj).getRange().toString());
		result.add(((QuotationItem)obj).getAccuracy()==null?"":((QuotationItem)obj).getAccuracy().toString());
		String certType=((QuotationItem)obj).getCertType()==null?"":((QuotationItem)obj).getCertType().toString();
		if(certType.equals("1"))
			certType="检定";
		else if(certType.equals("2"))
			certType="校准";
		else if(certType.equals("3"))
			certType="检测";
		else if(certType.equals("4"))
			certType="检验";
		result.add(certType);
		String siteTest=((QuotationItem)obj).getSiteTest()==null?"":((QuotationItem)obj).getSiteTest().toString();
		if(siteTest.equals("true"))
			siteTest="是";
		else
			siteTest="否";
		result.add(siteTest);
		
		result.add(((QuotationItem)obj).getAppFactoryCode()==null?"":((QuotationItem)obj).getAppFactoryCode().toString());
		result.add(((QuotationItem)obj).getAppManageCode()==null?"":((QuotationItem)obj).getAppManageCode().toString());
		result.add(((QuotationItem)obj).getManufacturer()==null?"":((QuotationItem)obj).getManufacturer().toString());	
		result.add(((QuotationItem)obj).getQuantity()==null?"1":((QuotationItem)obj).getQuantity().toString());	
		String cost="";
		if(((QuotationItem)obj).getMinCost().equals(((QuotationItem)obj).getMaxCost()))
			cost=((QuotationItem)obj).getMinCost();
		else
			cost=String.format("%s~%s", ((QuotationItem)obj).getMinCost(),((QuotationItem)obj).getMaxCost());
		
		String totalcost="";
		if(((QuotationItem)obj).getMinCost().equals(((QuotationItem)obj).getMaxCost()))
			totalcost=String.format("%s",Double.parseDouble(((QuotationItem)obj).getMinCost())*((QuotationItem)obj).getQuantity());
		else
			totalcost=String.format("%s~%s", Double.parseDouble(((QuotationItem)obj).getMinCost())*((QuotationItem)obj).getQuantity(),Double.parseDouble(((QuotationItem)obj).getMaxCost())*((QuotationItem)obj).getQuantity());
		result.add(cost);
		result.add(totalcost);
		
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("报价单号");
		result.add("受检器具证书名称");
		result.add("受检器具标准名称");
		result.add("型号规格");
		result.add("测量范围");
		result.add("准确度等级");
		result.add("证书类型");
		result.add("是否现场检测");
		
		result.add("出厂编号");
		result.add("管理编号");
		result.add("制造厂");
		result.add("台件数");
		result.add("元/台件");
		result.add("总检测费");
	
		return result;
	}
}
