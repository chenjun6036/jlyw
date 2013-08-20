package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.CertificateFeeAssign;
import com.jlyw.hibernate.CertificateFeeAssignDAO;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.Discount;
import com.jlyw.hibernate.DiscountComSheet;
import com.jlyw.hibernate.DiscountComSheetDAO;
import com.jlyw.hibernate.DiscountDAO;
import com.jlyw.util.KeyValueWithOperator;

public class DiscountManager {
	//HQL语句：根据委托单Id查询该委托单下的所有原始费用之和
	public static final String queryStringAllOldFeeByCommissionSheetId = CertificateFeeAssignManager.queryStringAllOldFeeByCommissionSheetId;
	//HQL语句：根据委托单Id查询该委托单下的所有费用记录
	public static final String queryString_CertificateFeeAssignByCommissionSheetId = CertificateFeeAssignManager.queryString_CertificateFeeAssignByCommissionSheetId;
	private DiscountDAO m_dao = new DiscountDAO();
	
	/**
	 * 根据Discount Id 查找 Discount对象
	 * @param id: Discount Id
	 * @return Discount对象
	 */
	public Discount findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条Discount记录
	 * @param appSpecies Discount对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(Discount appSpecies) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(appSpecies);
			tran.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tran.rollback();
			throw e;
		} finally {
			m_dao.closeSession();
		}
	}
	
	/**
	 * 更新一条Discount记录
	 * @param appSpecies Discount对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(Discount appSpecies) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(appSpecies);
			tran.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tran.rollback();
			throw e;
		} finally {
			m_dao.closeSession();
		}
	}
	
	/**
	 * 根据Discount Id,删除Discount对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Discount u = m_dao.findById(id);
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
			throw e;
		} finally {
			m_dao.closeSession();
		}
	}	
	
	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @param arr 条件键值对
	 * @return 分页后的Discount列表
	 */
	public List<Discount> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		return m_dao.findPagedAll("Discount", currentPage,pageSize, arr);
	}
	
	/**
	 * 得到所有Discount记录数
	 * @param arr 条件键值对
	 * @return Discount记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Discount", arr);
	}
	/**
	 * 得到所有Discount记录数
	 * @param arr 查询条件列表
	 * @return Discount记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("Discount", arr);
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
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<Discount> findByExample(Discount instance) {
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
	* @return 分页后的数据列表- List<Discount>
	*/
	public List<Discount> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		return m_dao.findPagedAllBySort("Discount", currentPage, pageSize, orderby, asc, arr);
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
	* @return 分页后的数据列表- List<Discount>
	*/
	public List<Discount> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		return m_dao.findPagedAllBySort("Discount", currentPage, pageSize, orderby, asc, arr);
	}
	
	/**
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findByHQL(String queryString, Object...arr){
		return m_dao.findByHQL(queryString, arr);
	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<Discount> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("Discount", arr);
	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<Discount> findByVarProperty(List<KeyValueWithOperator> arr){
		return m_dao.findByVarProperty("Discount", arr);
	}
	/**
	 * 按任意条件对查找，可指定排序字段
	 * @param TableName:从哪个表里查询
	 * @param orderby：按照哪个字段排序
	 * @param asc：true 增序 false 减序
	 * @param arr ：条件与值对
	 */
	public List<Discount> findByPropertyBySort(String orderby, boolean asc, KeyValueWithOperator...arr){
		return m_dao.findByPropertyBySort("Discount", orderby, asc, arr);
	}
	
	/**
	 * 执行打折：对已通过的折扣申请单里的委托单进行打折
	 * @param discount:折扣单
	 * @return
	 */
	public boolean discountExecute(Discount discount) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			DiscountComSheetDAO dCSheetDAO = new DiscountComSheetDAO();
			CertificateFeeAssignDAO feeAssignDAO = new CertificateFeeAssignDAO();
			List<DiscountComSheet> dCSheetList = dCSheetDAO.findByVarProperty("DiscountComSheet", 
					new KeyValueWithOperator("discount.id", discount.getId(), "="));	//查找所有委托单的折扣信息
			for(DiscountComSheet dCSheet : dCSheetList){
				CommissionSheet cSheet = dCSheet.getCommissionSheet();
				if(cSheet.getCheckOutDate() != null && cSheet.getDetailListCode() != null){	//委托单已结帐
					throw new Exception(String.format("委托单%s已经结账（结账清单号：%s），不能执行折扣改费操作！", cSheet.getCode(), cSheet.getDetailListCode()));
				}
				
				List<Object[]> feeList = feeAssignDAO.findByHQL(queryStringAllOldFeeByCommissionSheetId, cSheet.getId());	//委托单的各项总费用的原价
				if(feeList.isEmpty()){
			    	throw new Exception(String.format("委托单%s没有任何费用记录，不能执行折扣操作！", cSheet.getCode()));
			    }else{	//平摊至每份证书上
				    Object []tempObjArray = feeList.get(0);
				    Double TestFeeOld = tempObjArray[0]==null?0:(Double)tempObjArray[0];	//该委托单原始的各项总费用
				    Double RepairFeeOld = tempObjArray[1]==null?0:(Double)tempObjArray[1];
				    Double MaterialFeeOld = tempObjArray[2]==null?0:(Double)tempObjArray[2];
				    Double CarFeeOld = tempObjArray[3]==null?0:(Double)tempObjArray[3];
				    Double DebugFeeOld = tempObjArray[4]==null?0:(Double)tempObjArray[4];
				    Double OtherFeeOld = tempObjArray[5]==null?0:(Double)tempObjArray[5];
				    Double TotalFeeOld = tempObjArray[6]==null?0:(Double)tempObjArray[6];
				   
				    List<CertificateFeeAssign> feeAssignList = feeAssignDAO.findByHQL(queryString_CertificateFeeAssignByCommissionSheetId, cSheet.getId());
				    if(feeAssignList.size() == 0){
				    	throw new Exception(String.format("委托单%s没有任何费用记录，不能执行折扣操作！", cSheet.getCode()));
				    }
				    int indexMaxTestFee = -1, indexMaxRepairFee = -1, indexMaxMaterialFee = -1, indexMaxCarFee = -1, indexMaxDebugFee = -1, indexMaxOtherFee = -1;	//各种费用最大值的索引项
				    Double maxTestFee = null, maxRepairFee = null, maxMaterialFee = null, maxCarFee = null, maxDebugFee = null, maxOtherFee = null;	//各种费用的最大值
				    Double countTestFee = 0.0, countRepairFee = 0.0, countMaterialFee = 0.0, countCarFee = 0.0, countDebugFee = 0.0, countOtherFee = 0.0;	//各种费用的总计
				    Double dRateTestFee = (TestFeeOld == null || TestFeeOld <= 0 || dCSheet.getTestFee() == null)?0:dCSheet.getTestFee()/TestFeeOld;	//折扣率
				    Double dRateRepairFee = (RepairFeeOld == null || RepairFeeOld <= 0 || dCSheet.getRepairFee() == null)?0:dCSheet.getRepairFee()/RepairFeeOld;
				    Double dRateMaterialFee = (MaterialFeeOld == null || MaterialFeeOld <= 0 || dCSheet.getMaterialFee() == null)?0:dCSheet.getMaterialFee()/MaterialFeeOld;
				    Double dRateCarFee = (CarFeeOld == null || CarFeeOld == 0 || dCSheet.getCarFee() == null)?0:dCSheet.getCarFee()/CarFeeOld;
				    Double dRateDebugFee = (DebugFeeOld == null || DebugFeeOld == 0 || dCSheet.getDebugFee() == null)?0:dCSheet.getDebugFee()/DebugFeeOld;
				    Double dRateOtherFee = (OtherFeeOld == null || OtherFeeOld == 0 || dCSheet.getOtherFee() == null)?0:dCSheet.getOtherFee()/OtherFeeOld;
				    
				    for(int i = 0; i < feeAssignList.size(); i++){
				    	CertificateFeeAssign feeAssign = feeAssignList.get(i);
				    	feeAssign.setTestFee(new Double(Math.round((feeAssign.getTestFeeOld()==null?0:feeAssign.getTestFeeOld())*dRateTestFee)));	//更新费用
				    	feeAssign.setRepairFee(new Double(Math.round((feeAssign.getRepairFeeOld()==null?0:feeAssign.getRepairFeeOld())*dRateRepairFee)));
				    	feeAssign.setMaterialFee(new Double(Math.round((feeAssign.getMaterialFeeOld()==null?0:feeAssign.getMaterialFeeOld())*dRateMaterialFee)));
				    	feeAssign.setCarFee(new Double(Math.round((feeAssign.getCarFeeOld()==null?0:feeAssign.getCarFeeOld())*dRateCarFee)));
				    	feeAssign.setDebugFee(new Double(Math.round((feeAssign.getDebugFeeOld()==null?0:feeAssign.getDebugFeeOld())*dRateDebugFee)));
				    	feeAssign.setOtherFee(new Double(Math.round((feeAssign.getOtherFeeOld()==null?0:feeAssign.getOtherFeeOld())*dRateOtherFee)));
				    	feeAssign.setTotalFee(new Double(feeAssign.getTestFee().intValue() + feeAssign.getRepairFee().intValue() + feeAssign.getMaterialFee().intValue() + feeAssign.getCarFee().intValue() + feeAssign.getDebugFee().intValue() + feeAssign.getOtherFee().intValue()));
				    	
				    	//判断最大费用项
				    	if(maxTestFee == null || maxTestFee <= feeAssign.getTestFee()){
				    		maxTestFee = feeAssign.getTestFee();
				    		indexMaxTestFee = i;
				    	}
				    	if(maxRepairFee == null || maxRepairFee <= feeAssign.getRepairFee()){
				    		maxRepairFee = feeAssign.getRepairFee();
				    		indexMaxRepairFee = i;
				    	}
				    	if(maxMaterialFee == null || maxMaterialFee <= feeAssign.getMaterialFee()){
				    		maxMaterialFee = feeAssign.getMaterialFee();
				    		indexMaxMaterialFee = i;
				    	}
				    	if(maxCarFee == null || maxCarFee <= feeAssign.getCarFee()){
				    		maxCarFee = feeAssign.getCarFee();
				    		indexMaxCarFee = i;
				    	}
				    	if(maxDebugFee == null || maxDebugFee <= feeAssign.getDebugFee()){
				    		maxDebugFee = feeAssign.getDebugFee();
				    		indexMaxDebugFee = i;
				    	}
				    	if(maxOtherFee == null || maxOtherFee <= feeAssign.getOtherFee()){
				    		maxOtherFee = feeAssign.getOtherFee();
				    		indexMaxOtherFee = i;
				    	}
				    	//计算各种费用之和
				    	countTestFee += feeAssign.getTestFee().intValue();
				    	countRepairFee += feeAssign.getRepairFee().intValue();
				    	countMaterialFee += feeAssign.getMaterialFee().intValue();
				    	countCarFee += feeAssign.getCarFee().intValue();
				    	countDebugFee += feeAssign.getDebugFee().intValue();
				    	countOtherFee += feeAssign.getOtherFee().intValue();
				    	
				    }//end for
				    
				    //计算差额：分摊至最大项
				    if(indexMaxTestFee >= 0){
				    	CertificateFeeAssign f = feeAssignList.get(indexMaxTestFee);
				    	Double balance = (dCSheet.getTestFee()==null?0:dCSheet.getTestFee()) - countTestFee;
				    	f.setTestFee(new Double(Math.round(f.getTestFee() + balance)));
				    	f.setTotalFee(new Double(f.getTestFee().intValue() + f.getRepairFee().intValue() + f.getMaterialFee().intValue() + f.getCarFee().intValue() + f.getDebugFee().intValue() + f.getOtherFee().intValue()));
				    }
				    if(indexMaxRepairFee >= 0){
				    	CertificateFeeAssign f = feeAssignList.get(indexMaxRepairFee);
				    	Double balance = (dCSheet.getRepairFee()==null?0:dCSheet.getRepairFee()) - countRepairFee;
				    	f.setRepairFee(new Double(Math.round(f.getRepairFee() + balance)));
				    	f.setTotalFee(new Double(f.getTestFee().intValue() + f.getRepairFee().intValue() + f.getMaterialFee().intValue() + f.getCarFee().intValue() + f.getDebugFee().intValue() + f.getOtherFee().intValue()));
				    }
				    if(indexMaxMaterialFee >= 0){
				    	CertificateFeeAssign f = feeAssignList.get(indexMaxMaterialFee);
				    	Double balance = (dCSheet.getMaterialFee()==null?0:dCSheet.getMaterialFee()) - countMaterialFee;
				    	f.setMaterialFee(new Double(Math.round(f.getMaterialFee() + balance)));
				    	f.setTotalFee(new Double(f.getTestFee().intValue() + f.getRepairFee().intValue() + f.getMaterialFee().intValue() + f.getCarFee().intValue() + f.getDebugFee().intValue() + f.getOtherFee().intValue()));
				    }
				    if(indexMaxCarFee >= 0){
				    	CertificateFeeAssign f = feeAssignList.get(indexMaxCarFee);
				    	Double balance = (dCSheet.getCarFee()==null?0:dCSheet.getCarFee()) - countCarFee;
				    	f.setCarFee(new Double(Math.round(f.getCarFee() + balance)));
				    	f.setTotalFee(new Double(f.getTestFee().intValue() + f.getRepairFee().intValue() + f.getMaterialFee().intValue() + f.getCarFee().intValue() + f.getDebugFee().intValue() + f.getOtherFee().intValue()));
				    }
				    if(indexMaxDebugFee >= 0){
				    	CertificateFeeAssign f = feeAssignList.get(indexMaxDebugFee);
				    	Double balance = (dCSheet.getDebugFee()==null?0:dCSheet.getDebugFee()) - countDebugFee;
				    	f.setDebugFee(new Double(Math.round(f.getDebugFee() + balance)));
				    	f.setTotalFee(new Double(f.getTestFee().intValue() + f.getRepairFee().intValue() + f.getMaterialFee().intValue() + f.getCarFee().intValue() + f.getDebugFee().intValue() + f.getOtherFee().intValue()));
				    }
				    if(indexMaxOtherFee >= 0){
				    	CertificateFeeAssign f = feeAssignList.get(indexMaxOtherFee);
				    	Double balance = (dCSheet.getOtherFee()==null?0:dCSheet.getOtherFee()) - countOtherFee;
				    	f.setOtherFee(new Double(Math.round(f.getOtherFee() + balance)));
				    	f.setTotalFee(new Double(f.getTestFee().intValue() + f.getRepairFee().intValue() + f.getMaterialFee().intValue() + f.getCarFee().intValue() + f.getDebugFee().intValue() + f.getOtherFee().intValue()));
				    }
				    
				    //更新费用记录
				    for(CertificateFeeAssign feeAssign : feeAssignList){
				    	feeAssignDAO.update(feeAssign);
				    } //end 更新费用记录
			    }//end else
			}//end for：处理一个委托单
			m_dao.update(discount);			
			tran.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tran.rollback();
			throw e;
		} finally {
			m_dao.closeSession();
		}
	}
}
