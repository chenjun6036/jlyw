package com.jlyw.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Transaction;

import com.jlyw.hibernate.AppStdNameProTeam;
import com.jlyw.hibernate.AppStdNameProTeamDAO;
import com.jlyw.hibernate.Certificate;
import com.jlyw.hibernate.CertificateFeeAssign;
import com.jlyw.hibernate.CertificateFeeAssignDAO;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.CommissionSheetDAO;
import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerAccount;
import com.jlyw.hibernate.CustomerAccountDAO;
import com.jlyw.hibernate.CustomerDAO;
import com.jlyw.hibernate.DealItem;
import com.jlyw.hibernate.DealItemDAO;
import com.jlyw.hibernate.DetailList;
import com.jlyw.hibernate.DetailListDAO;
import com.jlyw.hibernate.Discount;
import com.jlyw.hibernate.DiscountComSheet;
import com.jlyw.hibernate.DiscountComSheetDAO;
import com.jlyw.hibernate.DiscountDAO;
import com.jlyw.hibernate.LocaleApplianceItem;
import com.jlyw.hibernate.LocaleApplianceItemDAO;
import com.jlyw.hibernate.LocaleMission;
import com.jlyw.hibernate.LocaleMissionDAO;
import com.jlyw.hibernate.OriginalRecord;
import com.jlyw.hibernate.OriginalRecordDAO;
import com.jlyw.hibernate.Reason;
import com.jlyw.hibernate.ReasonDAO;
import com.jlyw.hibernate.StandardAppliance;
import com.jlyw.hibernate.SubContract;
import com.jlyw.hibernate.SubContractDAO;
import com.jlyw.hibernate.SubContractor;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.SysUserDAO;
import com.jlyw.hibernate.TaskAssign;
import com.jlyw.hibernate.TaskAssignDAO;
import com.jlyw.hibernate.TestLog;
import com.jlyw.hibernate.TestLogDAO;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValue;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.SysStringUtil;
import com.jlyw.util.SystemCfgUtil;

public class CommissionSheetManager {
	private static final Log log = LogFactory.getLog(CommissionSheetManager.class);
	private CommissionSheetDAO m_dao = new CommissionSheetDAO();
	
	/**
	 * 根据CommissionSheet Id 查找 CommissionSheet对象
	 * @param id: CommissionSheet Id
	 * @return CommissionSheet对象
	 */
	public CommissionSheet findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条CommissionSheet记录
	 * @param appSpecies CommissionSheet对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(CommissionSheet appSpecies){
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
	 * 更新一条CommissionSheet记录
	 * @param appSpecies CommissionSheet对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(CommissionSheet appSpecies){
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
	 * 根据CommissionSheet Id,删除CommissionSheet对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			CommissionSheet u = m_dao.findById(id);
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
	 * @return 分页后的CommissionSheet列表
	 */
	public List<CommissionSheet> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("CommissionSheet", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有CommissionSheet记录数
	 * @param arr 条件键值对
	 * @return CommissionSheet记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("CommissionSheet", arr);
	}
	/**
	 * 得到所有CommissionSheet记录数
	 * @param arr 查询条件列表
	 * @return CommissionSheet记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("CommissionSheet", arr);
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<CommissionSheet> findByExample(CommissionSheet instance) {
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
	* @return 分页后的数据列表- List<CommissionSheet>
	*/
	public List<CommissionSheet> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		return m_dao.findPagedAllBySort("CommissionSheet", currentPage, pageSize, orderby, asc, arr);
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
	* @return 分页后的数据列表- List<CommissionSheet>
	*/
	public List<CommissionSheet> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		return m_dao.findPagedAllBySort("CommissionSheet", currentPage, pageSize, orderby, asc, arr);
	}
	public List<CommissionSheet> findByPropertyBySort(String orderby,boolean asc,List<KeyValueWithOperator>arr){
		return m_dao.findByPropertyBySort("CommissionSheet",orderby, asc, arr);
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
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findBySQL(String queryString, Object...arr){
		return m_dao.findBySQL(queryString, arr);
	}
	
	/**
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findBySQL(String queryString, List<Object> arr){
		return m_dao.findBySQL(queryString, arr);
	}
	
	/**
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findByHQL(String queryString, List<Object> arr){
		return m_dao.findByHQL(queryString, arr);
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
		return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
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
		return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<CommissionSheet> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("CommissionSheet", arr);
	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<CommissionSheet> findByVarProperty(List<KeyValueWithOperator> arr){
		return m_dao.findByVarProperty("CommissionSheet", arr);
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
	 * 插入一批CommissionSheet记录
	 * @param cSheetList CommissionSheet对象
	 * @param subConList 委托单列表对应的转包方对象
	 * @param alloteeList 派定人列表
	 * @param subConCreateUser 转包记录创建人
	 * @param taskAssigner 任务分配人（可以为null，表示系统自动分配）
	 * @param nowTime 创建记录的时间
	 * @param ComSheetType 判断是现场导入的还是从新建委托单新建的、
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean saveByBatch(List<CommissionSheet> cSheetList, List<SubContractor> subConList, List<SysUser> alloteeList, SysUser subConCreateUser, SysUser taskAssigner, Timestamp nowTime,String ComSheetType){
		if(cSheetList == null || subConList == null || alloteeList == null || cSheetList.size() != subConList.size() || cSheetList.size() != alloteeList.size()){
			return false;
		}
		
		TaskAssignDAO taskAssignDAO = new TaskAssignDAO();	//任务分配DAO
		SubContractDAO subContractDAO = new SubContractDAO();	//转包记录DAO
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
//			ViewApplianceSpecialStandardNameProjectDAO vDAO = new ViewApplianceSpecialStandardNameProjectDAO();	//器具类别-标准名称-项目组关系DAO 
			AppStdNameProTeamDAO tDAO = new AppStdNameProTeamDAO();	//标准名称-项目组关系DAO
			TaskAssignManager tMgr = new TaskAssignManager();
			List<Integer> typeList = new ArrayList<Integer>();
			typeList.add(FlagUtil.QualificationType.Type_Jianding);
			typeList.add(FlagUtil.QualificationType.Type_Jianyan);
			typeList.add(FlagUtil.QualificationType.Type_Jiaozhun);
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_YEAR, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			Timestamp tYear = new Timestamp(c.getTimeInMillis()); //今年的开始时间
			for(int i = 0; i < cSheetList.size(); i++){
				CommissionSheet cSheet = cSheetList.get(i);
				m_dao.save(cSheet);
				SubContractor subContractor = subConList.get(i);
				if(subContractor != null){
					SubContract subConObj = new SubContract();	//转包记录对象
					subConObj.setCommissionSheet(cSheet);
					subConObj.setSysUserByLastEditorId(subConCreateUser);
					subConObj.setSubContractor(subContractor);
					subConObj.setStatus(0);
					subConObj.setLastEditTime(nowTime);
					subContractDAO.save(subConObj);
				}
				SysUser allotee = alloteeList.get(i);
				if(allotee != null){
					TaskAssign taskAssignObj = new TaskAssign();	//任务分配对象
					taskAssignObj.setCommissionSheet(cSheet);
					taskAssignObj.setSysUserByAlloteeId(allotee);
					taskAssignObj.setSysUserByAssignerId(taskAssigner);
					taskAssignObj.setAssignTime(nowTime);
					taskAssignObj.setStatus(0);
					//查找“器具标准名称_项目组Id”
					if(cSheet.getSpeciesType() == false){	//标准名称
						List<AppStdNameProTeam> rList = tDAO.findByVarProperty("AppStdNameProTeam", new KeyValueWithOperator("applianceStandardName.id", cSheet.getApplianceSpeciesId(), "="));
						if(rList != null && rList.size() == 1){
							taskAssignObj.setAppStdNameProTeam(rList.get(0));
						}
					}else{	//分类名称:设置检测项目为null
//						List<ViewApplianceSpecialStandardNameProject> rList = vDAO.findByVarProperty("ViewApplianceSpecialStandardNameProject", new KeyValueWithOperator("id.appSpeId", cSheet.getApplianceSpeciesId(), "="));
//						if(rList != null && rList.size() == 1){
//							AppStdNameProTeam temp = new AppStdNameProTeam();
//							temp.setId(rList.get(0).getId().getAppStdProId());
//							taskAssignObj.setAppStdNameProTeam(temp);
//						}
					}
					taskAssignDAO.save(taskAssignObj);
				}else{	//分配所有人
					List<Object[]> retList = tMgr.getInspectQualifyUsersByRule(cSheet.getApplianceSpeciesId(), cSheet.getSpeciesType()?1:0, typeList, SystemCfgUtil.getTaskAllotRule(), tYear);
					for(Object []obj : retList){
						TaskAssign taskAssignObj = new TaskAssign();	//任务分配对象:不设置检验项目名称（“器具标准名称_项目组Id”）
						SysUser alloteeTemp = new SysUser();
						alloteeTemp.setId((Integer)obj[0]);
						taskAssignObj.setCommissionSheet(cSheet);
						taskAssignObj.setSysUserByAlloteeId(alloteeTemp);
						taskAssignObj.setSysUserByAssignerId(null);	//设为空，说明系统自动分配
						taskAssignObj.setAssignTime(nowTime);
						taskAssignObj.setStatus(0);
						taskAssignDAO.save(taskAssignObj);
					}
				}
			}
			//如果为现场检测，则更新现场业务的状态为已完成（2）
			if(cSheetList.size() > 0 && cSheetList.get(0).getCommissionType() == 2&&ComSheetType!=null&&ComSheetType.length()>0){
				LocaleApplianceItemDAO locAppItemDAO = new LocaleApplianceItemDAO();
				LocaleApplianceItem locAppItem = locAppItemDAO.findById(cSheetList.get(0).getLocaleApplianceItemId());
				LocaleMission locMission = locAppItem.getLocaleMission();
				locMission.setStatus(2);	//已完成
				new LocaleMissionDAO().update(locMission);
			}
			tran.commit();
			return true;
		} catch (Exception e) {
			
			if(e.getClass() == java.lang.Exception.class){	//自定义的消息
				log.debug("exception in CommissionSheetManager-->saveByBatch", e);
			}else{
				log.error("error in CommissionSheetManager-->saveByBatch", e);
			}
			tran.rollback();
			return false;
		} finally {
			m_dao.closeSession();
		}
	}
	/**
	 * 插入一批CommissionSheet记录(预留委托单时使用)
	 * @param cSheetList CommissionSheet对象
	 * @param subConList 委托单列表对应的转包方对象
	 * @param alloteeList 派定人列表
	 * @param subConCreateUser 转包记录创建人
	 * @param taskAssigner 任务分配人（可以为null，表示系统自动分配）
	 * @param nowTime 创建记录的时间
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean saveByBatchYL(List<CommissionSheet> cSheetList, SysUser taskAssigner, Timestamp nowTime){
		if(cSheetList == null ){
			return false;
		}
		
		TaskAssignDAO taskAssignDAO = new TaskAssignDAO();	//任务分配DAO
		SubContractDAO subContractDAO = new SubContractDAO();	//转包记录DAO
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
//			ViewApplianceSpecialStandardNameProjectDAO vDAO = new ViewApplianceSpecialStandardNameProjectDAO();	//器具类别-标准名称-项目组关系DAO 
			AppStdNameProTeamDAO tDAO = new AppStdNameProTeamDAO();	//标准名称-项目组关系DAO
			TaskAssignManager tMgr = new TaskAssignManager();
			List<Integer> typeList = new ArrayList<Integer>();
			typeList.add(FlagUtil.QualificationType.Type_Jianding);
			typeList.add(FlagUtil.QualificationType.Type_Jianyan);
			typeList.add(FlagUtil.QualificationType.Type_Jiaozhun);
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_YEAR, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			Timestamp tYear = new Timestamp(c.getTimeInMillis()); //今年的开始时间
			for(int i = 0; i < cSheetList.size(); i++){
				CommissionSheet cSheet = cSheetList.get(i);
				m_dao.save(cSheet);
			}
			
			tran.commit();
			return true;
		} catch (Exception e) {
			
			if(e.getClass() == java.lang.Exception.class){	//自定义的消息
				log.debug("exception in CommissionSheetManager-->saveByBatch", e);
			}else{
				log.error("error in CommissionSheetManager-->saveByBatch", e);
			}
			tran.rollback();
			return false;
		} finally {
			m_dao.closeSession();
		}
	}
	/**
	 * 更新CommissionSheet记录(预留委托单修改时使用)
	 * @param cSheetList CommissionSheet对象
	 * @param subConList 委托单列表对应的转包方对象
	 * @param alloteeList 派定人列表
	 * @param subConCreateUser 转包记录创建人
	 * @param taskAssigner 任务分配人（可以为null，表示系统自动分配）
	 * @param nowTime 创建记录的时间
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean updateByBatch(List<CommissionSheet> cSheetList, List<SubContractor> subConList, List<SysUser> alloteeList, SysUser subConCreateUser, SysUser taskAssigner, Timestamp nowTime){
		if(cSheetList == null || subConList == null || alloteeList == null || cSheetList.size() != subConList.size() || cSheetList.size() != alloteeList.size()){
			return false;
		}
		
		TaskAssignDAO taskAssignDAO = new TaskAssignDAO();	//任务分配DAO
		SubContractDAO subContractDAO = new SubContractDAO();	//转包记录DAO
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
//			ViewApplianceSpecialStandardNameProjectDAO vDAO = new ViewApplianceSpecialStandardNameProjectDAO();	//器具类别-标准名称-项目组关系DAO 
			AppStdNameProTeamDAO tDAO = new AppStdNameProTeamDAO();	//标准名称-项目组关系DAO
			TaskAssignManager tMgr = new TaskAssignManager();
			List<Integer> typeList = new ArrayList<Integer>();
			typeList.add(FlagUtil.QualificationType.Type_Jianding);
			typeList.add(FlagUtil.QualificationType.Type_Jianyan);
			typeList.add(FlagUtil.QualificationType.Type_Jiaozhun);
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_YEAR, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			Timestamp tYear = new Timestamp(c.getTimeInMillis()); //今年的开始时间
			for(int i = 0; i < cSheetList.size(); i++){
				CommissionSheet cSheet = cSheetList.get(i);
				m_dao.save(cSheet);
				SubContractor subContractor = subConList.get(i);
				if(subContractor != null){
					SubContract subConObj = new SubContract();	//转包记录对象
					subConObj.setCommissionSheet(cSheet);
					subConObj.setSysUserByLastEditorId(subConCreateUser);
					subConObj.setSubContractor(subContractor);
					subConObj.setStatus(0);
					subConObj.setLastEditTime(nowTime);
					subContractDAO.save(subConObj);
				}
				SysUser allotee = alloteeList.get(i);
				if(allotee != null){
					TaskAssign taskAssignObj = new TaskAssign();	//任务分配对象
					taskAssignObj.setCommissionSheet(cSheet);
					taskAssignObj.setSysUserByAlloteeId(allotee);
					taskAssignObj.setSysUserByAssignerId(taskAssigner);
					taskAssignObj.setAssignTime(nowTime);
					taskAssignObj.setStatus(0);
					//查找“器具标准名称_项目组Id”
					if(cSheet.getSpeciesType() == false){	//标准名称
						List<AppStdNameProTeam> rList = tDAO.findByVarProperty("AppStdNameProTeam", new KeyValueWithOperator("applianceStandardName.id", cSheet.getApplianceSpeciesId(), "="));
						if(rList != null && rList.size() == 1){
							taskAssignObj.setAppStdNameProTeam(rList.get(0));
						}
					}else{	//分类名称:设置检测项目为null
//						List<ViewApplianceSpecialStandardNameProject> rList = vDAO.findByVarProperty("ViewApplianceSpecialStandardNameProject", new KeyValueWithOperator("id.appSpeId", cSheet.getApplianceSpeciesId(), "="));
//						if(rList != null && rList.size() == 1){
//							AppStdNameProTeam temp = new AppStdNameProTeam();
//							temp.setId(rList.get(0).getId().getAppStdProId());
//							taskAssignObj.setAppStdNameProTeam(temp);
//						}
					}
					taskAssignDAO.save(taskAssignObj);
				}else{	//分配所有人
					List<Object[]> retList = tMgr.getInspectQualifyUsersByRule(cSheet.getApplianceSpeciesId(), cSheet.getSpeciesType()?1:0, typeList, SystemCfgUtil.getTaskAllotRule(), tYear);
					for(Object []obj : retList){
						TaskAssign taskAssignObj = new TaskAssign();	//任务分配对象:不设置检验项目名称（“器具标准名称_项目组Id”）
						SysUser alloteeTemp = new SysUser();
						alloteeTemp.setId((Integer)obj[0]);
						taskAssignObj.setCommissionSheet(cSheet);
						taskAssignObj.setSysUserByAlloteeId(alloteeTemp);
						taskAssignObj.setSysUserByAssignerId(null);	//设为空，说明系统自动分配
						taskAssignObj.setAssignTime(nowTime);
						taskAssignObj.setStatus(0);
						taskAssignDAO.save(taskAssignObj);
					}
				}
			}
			//如果为现场检测，则更新现场业务的状态为已完成（2）
			if(cSheetList.size() > 0 && cSheetList.get(0).getCommissionType() == 2){
				LocaleApplianceItemDAO locAppItemDAO = new LocaleApplianceItemDAO();
				LocaleApplianceItem locAppItem = locAppItemDAO.findById(cSheetList.get(0).getLocaleApplianceItemId());
				LocaleMission locMission = locAppItem.getLocaleMission();
				locMission.setStatus(2);	//已完成
				new LocaleMissionDAO().update(locMission);
			}
			tran.commit();
			return true;
		} catch (Exception e) {
			
			if(e.getClass() == java.lang.Exception.class){	//自定义的消息
				log.debug("exception in CommissionSheetManager-->saveByBatch", e);
			}else{
				log.error("error in CommissionSheetManager-->saveByBatch", e);
			}
			tran.rollback();
			return false;
		} finally {
			m_dao.closeSession();
		}
	}
	/**
	 * 更新CommissionSheet记录（修改委托单时使用）
	 * @param cSheetList CommissionSheet对象
	 * @param subConList 委托单列表对应的转包方对象
	 * @param alloteeList 派定人列表
	 * @param subConCreateUser 转包记录创建人
	 * @param taskAssigner 任务分配人（可以为null，表示系统自动分配）
	 * @param nowTime 创建记录的时间
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean updateByBatch2(List<CommissionSheet> cSheetList, List<SubContractor> subConList, List<SysUser> alloteeList, SysUser subConCreateUser, SysUser taskAssigner, Timestamp nowTime){
		if(cSheetList == null || subConList == null || alloteeList == null || cSheetList.size() != subConList.size() || cSheetList.size() != alloteeList.size()){
			return false;
		}
		
		TaskAssignDAO taskAssignDAO = new TaskAssignDAO();	//任务分配DAO
		SubContractDAO subContractDAO = new SubContractDAO();	//转包记录DAO
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
//			ViewApplianceSpecialStandardNameProjectDAO vDAO = new ViewApplianceSpecialStandardNameProjectDAO();	//器具类别-标准名称-项目组关系DAO 
			AppStdNameProTeamDAO tDAO = new AppStdNameProTeamDAO();	//标准名称-项目组关系DAO
			TaskAssignManager tMgr = new TaskAssignManager();
			SubContractorManager sMgr = new SubContractorManager();
			List<Integer> typeList = new ArrayList<Integer>();
			typeList.add(FlagUtil.QualificationType.Type_Jianding);
			typeList.add(FlagUtil.QualificationType.Type_Jianyan);
			typeList.add(FlagUtil.QualificationType.Type_Jiaozhun);
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_YEAR, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			Timestamp tYear = new Timestamp(c.getTimeInMillis()); //今年的开始时间
			
			for(int i = 0; i < cSheetList.size(); i++){
				CommissionSheet cSheet = cSheetList.get(i);
				StringBuilder remark = new StringBuilder("");
				if(cSheet.getRemark()!=null&&cSheet.getRemark().length()>0){
					remark.append(cSheet.getRemark());
					remark.append(";");
				}
				
				remark.append(subConCreateUser.getName());
				remark.append("在");
				remark.append(DateTimeFormatUtil.DateTimeFormat.format(nowTime));
				remark.append("修改了该委托单");
				cSheet.setRemark(remark.toString());
				m_dao.save(cSheet);
				
				List<TaskAssign> taskAssignList = tMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet.id",cSheet.getId(),"="));
				if(taskAssignList!=null){
					for(TaskAssign taskAssign:taskAssignList){
						taskAssign.setStatus(1);
						taskAssignDAO.update(taskAssign);
					}
				}
				List<SubContractor> subContractorList = sMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet.id",cSheet.getId(),"="));
				if(subContractorList!=null){
					for(SubContractor subContractor:subContractorList){
						subContractor.setStatus(1);
						subContractDAO.update(subContractor);
					}
				}
				
				SubContractor subContractor = subConList.get(i);
				if(subContractor != null){
					SubContract subConObj = new SubContract();	//转包记录对象
					subConObj.setCommissionSheet(cSheet);
					subConObj.setSysUserByLastEditorId(subConCreateUser);
					subConObj.setSubContractor(subContractor);
					subConObj.setStatus(0);
					subConObj.setLastEditTime(nowTime);
					subContractDAO.save(subConObj);
				}
				SysUser allotee = alloteeList.get(i);
				if(allotee != null){
					TaskAssign taskAssignObj = new TaskAssign();	//任务分配对象
					taskAssignObj.setCommissionSheet(cSheet);
					taskAssignObj.setSysUserByAlloteeId(allotee);
					taskAssignObj.setSysUserByAssignerId(taskAssigner);
					taskAssignObj.setAssignTime(nowTime);
					taskAssignObj.setStatus(0);
					//查找“器具标准名称_项目组Id”
					if(cSheet.getSpeciesType() == false){	//标准名称
						List<AppStdNameProTeam> rList = tDAO.findByVarProperty("AppStdNameProTeam", new KeyValueWithOperator("applianceStandardName.id", cSheet.getApplianceSpeciesId(), "="));
						if(rList != null && rList.size() == 1){
							taskAssignObj.setAppStdNameProTeam(rList.get(0));
						}
					}else{	//分类名称:设置检测项目为null
//						List<ViewApplianceSpecialStandardNameProject> rList = vDAO.findByVarProperty("ViewApplianceSpecialStandardNameProject", new KeyValueWithOperator("id.appSpeId", cSheet.getApplianceSpeciesId(), "="));
//						if(rList != null && rList.size() == 1){
//							AppStdNameProTeam temp = new AppStdNameProTeam();
//							temp.setId(rList.get(0).getId().getAppStdProId());
//							taskAssignObj.setAppStdNameProTeam(temp);
//						}
					}
					taskAssignDAO.save(taskAssignObj);
				}else{	//分配所有人
					List<Object[]> retList = tMgr.getInspectQualifyUsersByRule(cSheet.getApplianceSpeciesId(), cSheet.getSpeciesType()?1:0, typeList, SystemCfgUtil.getTaskAllotRule(), tYear);
					for(Object []obj : retList){
						TaskAssign taskAssignObj = new TaskAssign();	//任务分配对象:不设置检验项目名称（“器具标准名称_项目组Id”）
						SysUser alloteeTemp = new SysUser();
						alloteeTemp.setId((Integer)obj[0]);
						taskAssignObj.setCommissionSheet(cSheet);
						taskAssignObj.setSysUserByAlloteeId(alloteeTemp);
						taskAssignObj.setSysUserByAssignerId(null);	//设为空，说明系统自动分配
						taskAssignObj.setAssignTime(nowTime);
						taskAssignObj.setStatus(0);
						taskAssignDAO.save(taskAssignObj);
					}
				}
			}
			
			tran.commit();
			return true;
		} catch (Exception e) {
			
			if(e.getClass() == java.lang.Exception.class){	//自定义的消息
				log.debug("exception in CommissionSheetManager-->saveByBatch", e);
			}else{
				log.error("error in CommissionSheetManager-->saveByBatch", e);
			}
			tran.rollback();
			return false;
		} finally {
			m_dao.closeSession();
		}
	}
	/**
	 * 完工确认
	 * @param cSheet
	 * @param confirmUser
	 * @param finishLocation
	 * @param confirmTime
	 * @param quantity:有效器具数目（暂时无用）
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean commissionSheetFinishConfirm(CommissionSheet cSheet, SysUser confirmUser, String finishLocation, Timestamp confirmTime, int quantity) throws Exception{
		if(cSheet == null || confirmUser == null || confirmTime == null){
			return false;
		}
		CertificateFeeAssignDAO feeAssignDAO = new CertificateFeeAssignDAO();	//费用分配DAO
		TaskAssignDAO taskAssignDAO = new TaskAssignDAO();	//任务分配DAO
		OriginalRecordDAO oRecordDAO = new OriginalRecordDAO();	//原始记录DAO
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			//为没有手动分配费用的原始记录分配费用
			List<OriginalRecord> oRecordList = oRecordDAO.findByVarProperty("OriginalRecord", 
					new KeyValueWithOperator("commissionSheet.id", cSheet.getId(), "="),
					new KeyValueWithOperator("status", 1, "<>"),	//原始记录未注销
					new KeyValueWithOperator("taskAssign.status", 1, "<>"),	//任务分配尚未注销
					new KeyValueWithOperator("certificate.pdf", null, "is not null"));	//已有证书正式版本
			for(OriginalRecord oRecord : oRecordList){
				int iRet = feeAssignDAO.getTotalCount("CertificateFeeAssign", 
						new KeyValueWithOperator("originalRecord.id", oRecord.getId(), "="),
						new KeyValueWithOperator("certificate.id", oRecord.getCertificate().getId(), "="));
				if(iRet == 0){	//没有记录，则新增费用分配记录
					CertificateFeeAssign feeAssign = new CertificateFeeAssign();
					feeAssign.setSysUserByFeeAlloteeId(oRecord.getSysUserByStaffId());
					feeAssign.setTestFee(oRecord.getTestFee()==null?0:new Double(Math.round(oRecord.getTestFee())));	//现价
					feeAssign.setTestFeeOld(feeAssign.getTestFee());	//原价
					feeAssign.setRepairFee(oRecord.getRepairFee()==null?0:new Double(Math.round(oRecord.getRepairFee())));
					feeAssign.setRepairFeeOld(feeAssign.getRepairFee());
					feeAssign.setMaterialFee(oRecord.getMaterialFee()==null?0:new Double(Math.round(oRecord.getMaterialFee())));
					feeAssign.setMaterialFeeOld(feeAssign.getMaterialFee());
					feeAssign.setCarFee(oRecord.getCarFee()==null?0:new Double(Math.round(oRecord.getCarFee())));
					feeAssign.setCarFeeOld(feeAssign.getCarFee());
					feeAssign.setDebugFee(oRecord.getDebugFee()==null?0:new Double(Math.round(oRecord.getDebugFee())));
					feeAssign.setDebugFeeOld(feeAssign.getDebugFee());
					feeAssign.setOtherFee(oRecord.getOtherFee()==null?0:new Double(Math.round(oRecord.getOtherFee())));
					feeAssign.setOtherFeeOld(feeAssign.getOtherFee());
					feeAssign.setTotalFee(feeAssign.getTestFee() + feeAssign.getRepairFee() + feeAssign.getMaterialFee() + feeAssign.getCarFee() + feeAssign.getDebugFee() + feeAssign.getOtherFee());
					feeAssign.setTotalFeeOld(feeAssign.getTotalFee());
					
					feeAssign.setCertificate(oRecord.getCertificate());
					feeAssign.setOriginalRecord(oRecord);
					feeAssign.setSysUserByLastEditorId(null);
					feeAssign.setLastEditTime(confirmTime);
					feeAssign.setCommissionSheet(cSheet);
					
					feeAssignDAO.save(feeAssign);
				}
			}
			
			//注销没有生成正式版本证书的原始记录
			String queryString  = "from OriginalRecord as o where o.status<>1 and o.commissionSheet.id = ? and o.certificate is null ";
			List<OriginalRecord> toDelOriginalRecordList = oRecordDAO.findByHQL(queryString, cSheet.getId());
			for(OriginalRecord o : toDelOriginalRecordList){
				o.setStatus(1);
				oRecordDAO.update(o);
			}
			queryString  = "from OriginalRecord as o where o.status<>1 and o.commissionSheet.id = ? and o.certificate.pdf is null ";
			toDelOriginalRecordList = oRecordDAO.findByHQL(queryString, cSheet.getId());
			for(OriginalRecord o : toDelOriginalRecordList){
				o.setStatus(1);
				oRecordDAO.update(o);
			}			
			
			//更新任务分配表
			List<TaskAssign> tList = taskAssignDAO.findByVarProperty("TaskAssign", new KeyValue("commissionSheet.id", cSheet.getId()), new KeyValue("status",0));
			for(TaskAssign t : tList){
				t.setFinishTime(confirmTime);
				taskAssignDAO.update(t);
			}
			
			//删除无效的任务分配(没有原始记录且没有为该任务接收人分配费用)
			String deleteString = "delete TaskAssign where commissionSheet.id=? and " +
					" id not in (select o.taskAssign.id from OriginalRecord o where o.commissionSheet.id=?) and " +
					" sysUserByAlloteeId.id not in (select cfa.sysUserByFeeAlloteeId.id from CertificateFeeAssign cfa " +
					"		where cfa.commissionSheet.id=? and cfa.sysUserByFeeAlloteeId is not null and " +
					"			( (cfa.originalRecord is null and cfa.certificate is null) or " +
					"			  (cfa.originalRecord in (from OriginalRecord as o where o.certificate = cfa.certificate))" +
					"			) " +
					" )";
			Query deleteObj = m_dao.getSession().createQuery(deleteString);
			int j = 0;
			deleteObj.setParameter(j++, cSheet.getId());
			deleteObj.setParameter(j++, cSheet.getId());
			deleteObj.setParameter(j++, cSheet.getId());
			deleteObj.executeUpdate();
			
			//设置无效的任务分配（只有无效的原始记录且没有分配费用）
			String updateString = "update TaskAssign set status=1 where commissionSheet.id=? and " +
					" id not in (select o.taskAssign.id from OriginalRecord o where o.commissionSheet.id=? and o.status<>1) and " +
//					" sysUserByAlloteeId.id not in (select cfa.sysUserByFeeAlloteeId.id from CertificateFeeAssign cfa where cfa.commissionSheet.id=? and (cfa.originalRecord.certificate=cfa.certificate or (cfa.originalRecord is null and cfa.certificate is null)))";
					" sysUserByAlloteeId.id not in (select cfa.sysUserByFeeAlloteeId.id from CertificateFeeAssign cfa " +
					"		where cfa.commissionSheet.id=? and cfa.sysUserByFeeAlloteeId is not null and " +
					"			( (cfa.originalRecord is null and cfa.certificate is null) or " +
					"			  (cfa.originalRecord in (from OriginalRecord as o where o.certificate = cfa.certificate))" +
					"			) " +
					" )";
			Query updateObj = m_dao.getSession().createQuery(updateString);
			int k = 0;
			updateObj.setParameter(k++, cSheet.getId());
			updateObj.setParameter(k++, cSheet.getId());
			updateObj.setParameter(k++, cSheet.getId());
			updateObj.executeUpdate();
			
			//更新委托单
			cSheet.setStatus(3);	//3完工确认
			cSheet.setFinishStaffId(confirmUser.getId()); 
			cSheet.setFinishLocation(finishLocation);
			cSheet.setFinishDate(confirmTime);
			m_dao.update(cSheet);
			
			LocaleApplianceItemDAO locAppItemDAO = new LocaleApplianceItemDAO();
			DiscountComSheetDAO dCSheetDAO = new DiscountComSheetDAO();
			//自动折扣流程：先判断是否有现场检测委托书，再判断是否有协议
			if(cSheet.getCommissionType() == 2 && 
					cSheet.getLocaleCommissionCode() != null && 
					cSheet.getLocaleApplianceItemId() != null && 
					locAppItemDAO.findById(cSheet.getLocaleApplianceItemId()) != null){	//价格根据现场检测委托书的价格
//				Discount discount = null;
//				//查找是否有该检测委托书下的委托单的自动折扣申请记录，若有，则增加一个条目；否则，新增一个自动折扣申请记录。
//				List<Discount> discountList = dCSheetDAO.findByHQL("select model.discount from DiscountComSheet as model " +
//						" where model.commissionSheet.localeCommissionCode = ? and " +
//						" model.discount.customer.id = ? and " +
//						" model.discount.applyTime = model.discount.executeTime ",	//申请时间与批准时间相同（现场检测书自动折扣流程）
//						cSheet.getLocaleCommissionCode(),
//						cSheet.getCustomerId());
//				if(discountList.size() == 0){
//					discount = new Discount();
//					discount.setApplyTime(confirmTime);
//					Customer customer = new Customer();
//					customer.setId(cSheet.getCustomerId());
//					discount.setContector("");
//					discount.setContectorTel("");
//					discount.setCustomer(customer);
//					discount.setExecuteMsg("系统自动审批通过。");
//					discount.setExecuteResult(true);	//审核通过
//					discount.setExecuteTime(confirmTime);	//审核时间：与申请时间相同
//					
//					//设置申请原因
//					ReasonDAO rDAO = new ReasonDAO();
//					List<Reason> reasonList = rDAO.findByHQL("from Reason as model where model.type=? and model.reason=? ", 
//							FlagUtil.ReasonType.Type_Discount,
//							SysStringUtil.ReasonString.Reason_Discount_LocalMission);
//					if(reasonList.size() > 0){
//						Reason reason = reasonList.get(0);
//						reason.setCount(reason.getCount()+1);
//						reason.setLastUse(confirmTime);
//						rDAO.update(reason);
//						discount.setReason(reason);
//					}else{
//						Reason reason = new Reason();
//						reason.setCount(1);
//						reason.setLastUse(confirmTime);
//						reason.setReason(SysStringUtil.ReasonString.Reason_Discount_LocalMission);
//						reason.setStatus(0);
//						reason.setType(FlagUtil.ReasonType.Type_Discount);
//						rDAO.save(reason);
//						discount.setReason(reason);
//					}
//					
//					//设置申请人和审批人：均为‘系统管理员’
//					SysUser applyUser = null;
//					try{
//						applyUser = (SysUser)new SysUserDAO().findByHQL("from SysUser as model where model.userName = ? ", SystemCfgUtil.SystemManagerUserName).get(0);
//					}catch(Exception e){
//						throw new Exception(String.format("找不到用户名为'%s'(系统管理员)的用户账号，自动折扣流程处理失败！", SystemCfgUtil.SystemManagerUserName));
//					}
//					discount.setSysUserByRequesterId(applyUser);
//					discount.setSysUserByExecutorId(applyUser);
//				}else{
//					discount = discountList.get(0);
//				}
//				
//				//执行折扣操作
//				DiscountComSheet dCSheet = null;
//				if(discount.getId() == null){	//新的折扣
//					dCSheet = new DiscountComSheet();
//				}else{
//					List<DiscountComSheet> dCSheetList = dCSheetDAO.findByHQL("from DiscountComSheet as model " +
//							" where model.commissionSheet = ? and " +
//							" model.discount = ? ",
//							cSheet, discount);
//					if(dCSheetList.size() == 0){
//						dCSheet = new DiscountComSheet();
//					}else{
//						dCSheet = dCSheetList.get(0);
//					}
//				}
//				List<Object[]> feeList = feeAssignDAO.findByHQL(CertificateFeeAssignManager.queryStringAllAllFeeByCommissionSheetId, cSheet.getId());	//获取委托单的所有费用
//				if(!feeList.isEmpty()){
//					Object[] fee = feeList.get(0);
//					Double TestFee = fee[0]==null?0.0:(Double)fee[0];
//					Double RepairFee = fee[1]==null?0.0:(Double)fee[1];
//					Double MaterialFee = fee[2]==null?0.0:(Double)fee[2];
//					Double CarFee = fee[3]==null?0.0:(Double)fee[3];
//					Double DebugFee = fee[4]==null?0.0:(Double)fee[4];
//					Double OtherFee = fee[5]==null?0.0:(Double)fee[5];
////					Double TotalFee = fee[6]==null?0.0:(Double)fee[6];
//					Double OldTestFee = fee[7]==null?0.0:(Double)fee[7];
//					Double OldRepairFee = fee[8]==null?0.0:(Double)fee[8];
//					Double OldMaterialFee = fee[9]==null?0.0:(Double)fee[9];
//					Double OldCarFee = fee[10]==null?0.0:(Double)fee[10];
//					Double OldDebugFee = fee[11]==null?0.0:(Double)fee[11];
//					Double OldOtherFee = fee[12]==null?0.0:(Double)fee[12];
//					Double OldTotalFee = fee[13]==null?0.0:(Double)fee[13];
//					
//					LocaleApplianceItem locAppItem = locAppItemDAO.findById(cSheet.getLocaleApplianceItemId());
//					dCSheet.setCommissionSheet(cSheet);
//					dCSheet.setDiscount(discount);
//					dCSheet.setTestFee(locAppItem.getTestCost()==null?TestFee:locAppItem.getTestCost());	//检测费
//					dCSheet.setRepairFee(locAppItem.getRepairCost()==null?RepairFee:locAppItem.getRepairCost());	//修理费
//					dCSheet.setMaterialFee(locAppItem.getMaterialCost()==null?MaterialFee:locAppItem.getMaterialCost());	//材料费
//					dCSheet.setDebugFee(DebugFee);
//					dCSheet.setCarFee(CarFee);
//					dCSheet.setOtherFee(OtherFee);
//					dCSheet.setTotalFee(dCSheet.getTestFee()+dCSheet.getRepairFee()+dCSheet.getMaterialFee()+dCSheet.getDebugFee()+dCSheet.getCarFee()+dCSheet.getOtherFee());
//					dCSheet.setOldTestFee(OldTestFee);
//					dCSheet.setOldRepairFee(OldRepairFee);
//					dCSheet.setOldMaterialFee(OldMaterialFee);
//					dCSheet.setOldCarFee(OldCarFee);
//					dCSheet.setOldDebugFee(OldDebugFee);
//					dCSheet.setOldOtherFee(OldOtherFee);
//					dCSheet.setOldTotalFee(OldTotalFee);
//					
//					if(discount.getId() == null){
//						new DiscountDAO().save(discount);	//存储折扣
//					}
//					if(dCSheet.getId() == null){
//						dCSheetDAO.save(dCSheet);
//					}else{
//						dCSheetDAO.update(dCSheet);
//					}
//					discountExecute(feeAssignDAO, dCSheet);
//				}
//				//更新原始记录费用的HQL
//				int Quantity = dCSheet.getCommissionSheet().getQuantity();
//				List<Long> wQuantityList = (new WithdrawManager()).findByHQL("select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?", dCSheet.getCommissionSheet().getId(), true);
//				if(wQuantityList != null && wQuantityList.size() > 0 && wQuantityList.get(0) != null){
//					Quantity = Quantity - wQuantityList.get(0).intValue();
//				}
//				String ORecordupdateString = "update OriginalRecord as o set " +
//						" testFee= o.quantity*" + dCSheet.getTestFee()/Quantity + "," +
//						" repairFee= o.quantity*" + dCSheet.getRepairFee()/Quantity + ", " +
//						" materialFee= o.quantity*" + dCSheet.getMaterialFee()/Quantity + ", " +
//						" carFee= o.quantity*" + dCSheet.getCarFee()/Quantity + ", " +
//						" debugFee= o.quantity*" + dCSheet.getDebugFee()/Quantity + ", " +
//						" otherFee= o.quantity*" + dCSheet.getOtherFee()/Quantity + ", " +
//						" totalFee= o.quantity*" + dCSheet.getTotalFee()/Quantity + " " +
//						" where o.commissionSheet.id=? ";
//			    	
//						m_dao.updateByHQL(ORecordupdateString, dCSheet.getCommissionSheet().getId());
			}else{	/***************  判断是否有协议     ***************/
				//查询所有有正式证书的原始记录
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(confirmTime.getTime());
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				java.sql.Date dealValidaty = new java.sql.Date(calendar.getTimeInMillis());
				String queryStringDeqlItem = "from DealItem as model where model.deal.status<>1 and model.deal.customer.id = ? and " +	//委托单位
						" ( model.deal.validity is null or model.deal.validity >= ? )  and " +	//协议有效期
						" model.applianceStandardName = ? " + //器具标准名称  
						" order by model.deal.signDate desc, model.id desc";	//根据协议签署日期降序排列
				DealItemDAO dealItemDAO = new DealItemDAO();
				boolean bExecuteDealDiscount = false;	//判断是否有根据协议打折（true：有，false：没有）
				for(OriginalRecord oRecord : oRecordList){
					List<DealItem> dItemList = dealItemDAO.findByHQL(queryStringDeqlItem, 
							cSheet.getCustomerId(), dealValidaty, oRecord.getTargetAppliance().getApplianceStandardName());
					DealItem dItemSelected = null;
					for(DealItem dItem : dItemList){	//获取一个符合条件的协议条目
						if(dItem.getModel() != null && dItem.getModel().trim().length() > 0){
							if(!dItem.getModel().equalsIgnoreCase(oRecord.getModel())){	//与原始记录中的型号不相等
								continue;
							}
						}
						if(dItem.getRange() != null && dItem.getRange().trim().length() > 0){
							if(!dItem.getRange().equalsIgnoreCase(oRecord.getRange())){
								continue;
							}
						}
						if(dItem.getAccuracy() != null && dItem.getAccuracy().trim().length() > 0){
							if(!dItem.getAccuracy().equalsIgnoreCase(oRecord.getAccuracy())){
								continue;
							}
						}
						if(dItem.getAppFactoryCode() != null && dItem.getAppFactoryCode().trim().length() > 0){
							if(!dItem.getAppFactoryCode().equalsIgnoreCase(oRecord.getApplianceCode())){
								continue;
							}
						}
						if(dItem.getAppManageCode() != null && dItem.getAppManageCode().trim().length() > 0){
							if(!dItem.getAppManageCode().equalsIgnoreCase(oRecord.getManageCode())){
								continue;
							}
						}
						if(dItem.getDealPrice() == null){	//费用为空
							continue;
						}
						dItemSelected = dItem;
						break;
					}//end for
					
					if(dItemSelected != null){	//有符合条件的协议条目
						List<Object[]> feeList = feeAssignDAO.findByHQL(CertificateFeeAssignManager.queryStringAllAllFeeByOriginalRecordIdAndCertificateId, 
								oRecord.getId(), oRecord.getCertificate().getId());	//获取该证书的所有费用
						if(feeList.size() > 0){
							Object[] fee = feeList.get(0);
							Double TestFee = fee[0]==null?0.0:(Double)fee[0];	//该证书的检测费(现价)
							Double OldTestFee = fee[7]==null?0.0:(Double)fee[7];	//该证书的检测费(原价)
							Double dealPrice = dItemSelected.getDealPrice() * (oRecord.getQuantity()==null?1:oRecord.getQuantity());
							if(oRecord.getQuantity() != null && !dealPrice.equals(TestFee)){	//费用不相等，进行折扣操作
								discountExecuteByCertificate(feeAssignDAO, dealPrice, OldTestFee, oRecord, oRecord.getCertificate());
								bExecuteDealDiscount = true;
							}
						}
					} //end if
				}//end for
				
				if(bExecuteDealDiscount){	//根据协议打折：存储折扣信息
					Discount discount = new Discount();
					discount.setApplyTime(confirmTime);
					Customer customer = new Customer();
					customer.setId(cSheet.getCustomerId());
					discount.setContector("");
					discount.setContectorTel("");
					discount.setCustomer(customer);
					discount.setExecuteMsg("系统自动审批通过。");
					discount.setExecuteResult(true);	//审核通过
					
					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(confirmTime.getTime());
					c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 5);
					discount.setExecuteTime(new Timestamp(c.getTimeInMillis()));	//审核时间：申请时间+5分钟
					
					//设置申请原因
					ReasonDAO rDAO = new ReasonDAO();
					List<Reason> reasonList = rDAO.findByHQL("from Reason as model where model.type=? and model.reason=? ", 
							FlagUtil.ReasonType.Type_Discount,
							SysStringUtil.ReasonString.Reason_Discount_Deal);
					if(reasonList.size() > 0){
						Reason reason = reasonList.get(0);
						reason.setCount(reason.getCount()+1);
						reason.setLastUse(confirmTime);
						rDAO.update(reason);
						discount.setReason(reason);
					}else{
						Reason reason = new Reason();
						reason.setCount(1);
						reason.setLastUse(confirmTime);
						reason.setReason(SysStringUtil.ReasonString.Reason_Discount_Deal);
						reason.setStatus(0);
						reason.setType(FlagUtil.ReasonType.Type_Discount);
						rDAO.save(reason);
						discount.setReason(reason);
					}
					
					//设置申请人和审批人：均为‘系统管理员’
					SysUser applyUser = null;
					try{
						applyUser = (SysUser)new SysUserDAO().findByHQL("from SysUser as model where model.userName = ? ", SystemCfgUtil.SystemManagerUserName).get(0);
					}catch(Exception e){
						throw new Exception(String.format("找不到用户名为'%s'(系统管理员)的用户账号，自动折扣流程处理失败！", SystemCfgUtil.SystemManagerUserName));
					}
					discount.setSysUserByRequesterId(applyUser);
					discount.setSysUserByExecutorId(applyUser);
					
					//执行折扣操作
					DiscountComSheet dCSheet = new DiscountComSheet();
					List<Object[]> feeList = feeAssignDAO.findByHQL(CertificateFeeAssignManager.queryStringAllAllFeeByCommissionSheetId, cSheet.getId());	//获取委托单的所有费用
					if(feeList.isEmpty()){
						throw new Exception(String.format("根据协议内容进行自动折扣时错误：找不到委托单‘%s’下的费用记录！", cSheet.getCode()));
					}
					Object[] fee = feeList.get(0);
					Double TestFee = fee[0]==null?0.0:(Double)fee[0];
					Double RepairFee = fee[1]==null?0.0:(Double)fee[1];
					Double MaterialFee = fee[2]==null?0.0:(Double)fee[2];
					Double CarFee = fee[3]==null?0.0:(Double)fee[3];
					Double DebugFee = fee[4]==null?0.0:(Double)fee[4];
					Double OtherFee = fee[5]==null?0.0:(Double)fee[5];
					Double TotalFee = fee[6]==null?0.0:(Double)fee[6];
					Double OldTestFee = fee[7]==null?0.0:(Double)fee[7];
					Double OldRepairFee = fee[8]==null?0.0:(Double)fee[8];
					Double OldMaterialFee = fee[9]==null?0.0:(Double)fee[9];
					Double OldCarFee = fee[10]==null?0.0:(Double)fee[10];
					Double OldDebugFee = fee[11]==null?0.0:(Double)fee[11];
					Double OldOtherFee = fee[12]==null?0.0:(Double)fee[12];
					Double OldTotalFee = fee[13]==null?0.0:(Double)fee[13];
					
					dCSheet.setCommissionSheet(cSheet);
					dCSheet.setDiscount(discount);
					dCSheet.setTestFee(TestFee);	//检测费
					dCSheet.setRepairFee(RepairFee);	//修理费
					dCSheet.setMaterialFee(MaterialFee);	//材料费
					dCSheet.setDebugFee(DebugFee);
					dCSheet.setCarFee(CarFee);
					dCSheet.setOtherFee(OtherFee);
					dCSheet.setTotalFee(TotalFee);
					dCSheet.setOldTestFee(OldTestFee);
					dCSheet.setOldRepairFee(OldRepairFee);
					dCSheet.setOldMaterialFee(OldMaterialFee);
					dCSheet.setOldCarFee(OldCarFee);
					dCSheet.setOldDebugFee(OldDebugFee);
					dCSheet.setOldOtherFee(OldOtherFee);
					dCSheet.setOldTotalFee(OldTotalFee);
					
					if(discount.getId() == null){
						new DiscountDAO().save(discount);	//存储折扣
					}
					if(dCSheet.getId() == null){
						dCSheetDAO.save(dCSheet);
					}else{
						dCSheetDAO.update(dCSheet);
					}
					//更新原始记录费用的HQL
					int Quantity = dCSheet.getCommissionSheet().getQuantity();
					List<Long> wQuantityList = (new WithdrawManager()).findByHQL("select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?", dCSheet.getCommissionSheet().getId(), true);
					if(wQuantityList != null && wQuantityList.size() > 0 && wQuantityList.get(0) != null){
						Quantity = Quantity - wQuantityList.get(0).intValue();
					}
					String ORecordupdateString = "update OriginalRecord as o set " +
							" testFee= o.quantity*" + dCSheet.getTestFee()/Quantity + "," +
							" repairFee= o.quantity*" + dCSheet.getRepairFee()/Quantity + ", " +
							" materialFee= o.quantity*" + dCSheet.getMaterialFee()/Quantity + ", " +
							" carFee= o.quantity*" + dCSheet.getCarFee()/Quantity + ", " +
							" debugFee= o.quantity*" + dCSheet.getDebugFee()/Quantity + ", " +
							" otherFee= o.quantity*" + dCSheet.getOtherFee()/Quantity + ", " +
							" totalFee= o.quantity*" + dCSheet.getTotalFee()/Quantity + " " +
							" where o.commissionSheet.id=? ";
				    	
							m_dao.updateByHQL(ORecordupdateString, dCSheet.getCommissionSheet().getId());
				}
			
			}//end else(判断是否有协议)
			
			//自检业务自动加入量值溯源记录（by Kimi）
			if(cSheet.getStatus()==6){
				TestLogDAO testLogDAO = new TestLogDAO();
				CertificateManager cerMgr = new CertificateManager();
				String querySql = " select a from Certificate as a " +
								" where a.commissionSheet.id = ? and ( " +
								" 	(a.originalRecord is null and a.certificate is null) or " +
								"   (a.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = a)) " +	
								" ) ";
				List<Certificate> cerList = cerMgr.findByHQL(querySql, cSheet.getId());
				for(Certificate cer : cerList){
					TestLog testLog = new TestLog();
					OriginalRecord o = cer.getOriginalRecord();
					List<StandardAppliance> stdAppList = (new StandardApplianceManager()).findByVarProperty(new KeyValueWithOperator("localeCode", o.getManageCode(), "="));
					if(stdAppList == null||stdAppList.size() == 0)
						continue;
					testLog.setStandardAppliance(stdAppList.get(0));
					testLog.setCertificateId(cer.getCertificateCode());
					testLog.setTestDate(o.getWorkDate());
					testLog.setValidDate(o.getValidity());
					testLog.setTester(cSheet.getHeadName());
					testLog.setConfirmMeasure(o.getConclusion());
					testLog.setStatus(0);
					testLogDAO.save(testLog);
				}
			}
			
			tran.commit();
			return true;
		}catch (Exception e) {
			if(e.getClass() == java.lang.Exception.class){	//自定义的消息
				log.debug("exception in CommissionSheetManager-->commissionSheetFinishConfirm", e);
			}else{
				log.error("error in CommissionSheetManager-->commissionSheetFinishConfirm", e);
			}
			tran.rollback();
			throw e;
		} finally {
			m_dao.closeSession();
		}
	}
	
	/**
	 * 生成费用清单：保存清单 和 委托单表
	 * 
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean saveByBatch(List<CommissionSheet> comSheetList,
			DetailList deList) {
		if(comSheetList == null || deList == null){
			return false;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			DetailListDAO dDAO = new DetailListDAO();	//清单表的DAO 
			dDAO.save(deList);
			for(int i = 0; i < comSheetList.size(); i++){
				CommissionSheet Com = comSheetList.get(i);
				
				m_dao.save(Com);
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
	 * 更新一批CommissionSheet记录
	 * @param CommissionSheetList CommissionSheet对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean updateByBatch(List<CommissionSheet> CommissionSheetList){
		if(CommissionSheetList == null || CommissionSheetList.size() == 0){
			return false;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			for(CommissionSheet CommissionSheet : CommissionSheetList){
				m_dao.update(CommissionSheet);
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
	 * 委托单结账挡回（‘已结帐’挡回至‘已完工’状态）
	 * @param cSheet
	 * @return
	 */
	public boolean checkOutReject(CommissionSheet cSheet, SysUser handleUser) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			String detailListCode = cSheet.getDetailListCode();	//结账清单号
			if(detailListCode != null && detailListCode.length() > 0){ //查找结账清单，从中扣除该委托单的相应费用。扣费规则：先扣现金，再扣支票，最后扣预付款，直至扣够为止
				DetailListDAO dListDao = new DetailListDAO();
				List<DetailList> dlList = dListDao.findByVarProperty("DetailList", 
						new KeyValueWithOperator("code", detailListCode, "="),	//清单号
						new KeyValueWithOperator("status", 2, "="));	//已结帐的清单
				DetailList dl = null;
				if(dlList.size() == 1){
					dl = dlList.get(0);
				}
				if(dl != null){
					//查找委托单的费用
					Double dTotalFee = cSheet.getTotalFee()==null?0.0:cSheet.getTotalFee();	//结账的总额
					
					Timestamp nowTime = new Timestamp(System.currentTimeMillis());
					CustomerAccountDAO cusAccountDao = new CustomerAccountDAO();
					CustomerDAO customerDao = new CustomerDAO();
					
					if(dTotalFee > 0){
						double balance = dTotalFee;	//差额
						if(dl.getCashPaid() != null && dl.getCashPaid() > 0){	//扣除现金
							double cash = dl.getCashPaid();
							dl.setCashPaid((cash - balance >= 0)?cash - balance:0);
							balance -= (cash - balance >= 0)?balance : cash;
						}
						if(balance > 0 && dl.getChequePaid() != null && dl.getChequePaid() > 0){	//扣除支票费用
							double money = dl.getChequePaid();
							dl.setCashPaid((money - balance >= 0)?money - balance:0);
							balance -= (money - balance >= 0)?balance : money;
						}
						if(balance > 0 && dl.getAccountPaid() != null && dl.getAccountPaid() > 0){	//扣除支票费用
							double money = dl.getAccountPaid();
							dl.setCashPaid((money - balance >= 0)?money - balance:0);
							
							/*** 创建委托单位账户存取明细对象 **/
							CustomerAccount cusAccount = new CustomerAccount();// 委托单位账户存取明细
							Customer c = customerDao.findById(cSheet.getCustomerId());
							cusAccount.setCustomer(c);
							cusAccount.setHandleType(0);	//充值
							cusAccount.setHandleTime(nowTime);
							cusAccount.setSysUser(handleUser);
							cusAccount.setHandlerName(handleUser==null?"":handleUser.getName());
							cusAccount.setAmount((money - balance >= 0)?balance:money);
							cusAccount.setRemark(String.format("[%s于%s对委托单%s进行结账挡回,退回预留账户金额 %s元]", 
											cusAccount.getHandlerName(), 
											DateTimeFormatUtil.DateTimeFormat.format(nowTime),
											cSheet.getCode(),
											cusAccount.getAmount()
							));
							cusAccountDao.save(cusAccount);
							//更新委托单位账户余额
							c.setBalance((c.getBalance()==null?0:c.getBalance()) + ((money - balance >= 0)?balance:money));
							customerDao.update(c);
							
						}
						
						//判断该结账清单是否还有其他的委托单，如果没有，则该清单注销
						String HQLStr = "from CommissionSheet as a where a.detailListCode = ? and a.status <> 10 ";
						List<CommissionSheet> comList = dListDao.findByHQL(HQLStr, dl.getCode());
						if(comList != null && comList.size() == 1){
							dl.setStatus(1);
						}
						
						dListDao.update(dl);
					}//end if
					
				}
				
			}
			
			//更新委托单
			cSheet.setCheckOutDate(null);	//结账时间
			cSheet.setCheckOutStaffId(null);	//结账人
			cSheet.setInvoiceCode(null);	//发票号
			cSheet.setDetailListCode(null);	//清单号
			cSheet.setStatus(FlagUtil.CommissionSheetStatus.Status_YiWanGong);
			m_dao.update(cSheet);
			
			tran.commit();
			return true;
		} catch (Exception e) {
			tran.rollback();
			throw e;
		} finally {
			m_dao.closeSession();
		}
	}
	
	/**
	 * 委托单注销
	 * @param cSheet
	 * @return
	 * @throws Exception
	 */
	public boolean cancel(CommissionSheet cSheet) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			DetailListDAO deDAO = new DetailListDAO();
			if(cSheet.getDetailListCode()!=null){
				String HQLStr="from CommissionSheet as a where a.detailListCode = ? and a.status <> 10 ";
				List<CommissionSheet> comList=m_dao.findByHQL(HQLStr, cSheet.getDetailListCode());
				if(comList != null && comList.size() == 1){
					for(CommissionSheet com : comList){								
						List<DetailList> dlList = deDAO.findByVarProperty("DetailList", new KeyValueWithOperator("code",com.getDetailListCode(),"="),
								new KeyValueWithOperator("status",0,"="));
						if(dlList.size() > 0){
							DetailList delist = dlList.get(0);
							delist.setStatus(1);
							deDAO.update(delist);
						}
					}
				}
			}
			
			TaskAssignDAO tAssignDao = new TaskAssignDAO();	//更新任务分配表：将任务完成时间置为空
			tAssignDao.updateByHQL("update TaskAssign set finishTime = null where commissionSheet.id = ? ", cSheet.getId());			
			
			cSheet.setDetailListCode(null);
			cSheet.setFinishDate(null);
			cSheet.setFinishStaffId(null);
			cSheet.setFinishLocation(null);
			m_dao.update(cSheet);
			
			//自检业务自动删除量值溯源记录（by Kimi）
			if(cSheet.getStatus()==6){
				TestLogDAO testLogDAO = new TestLogDAO();
				CertificateManager cerMgr = new CertificateManager();
				String querySql = " select a from Certificate as a " +
								" where a.commissionSheet.id = ? and ( " +
								" 	(a.originalRecord is null and a.certificate is null) or " +
								"   (a.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = a)) " +	
								" ) ";
				List<Certificate> cerList = cerMgr.findByHQL(querySql, cSheet.getId());
				for(Certificate cer : cerList){
					TestLog testLog = new TestLog();
					OriginalRecord o = cer.getOriginalRecord();
					List<TestLog> testLogList = (new TestLogManager()).findByVarProperty(new KeyValueWithOperator("certificateId", cer.getCertificateCode(), "="), new KeyValueWithOperator("standardAppliance.localeCode", o.getManageCode(), "="));
					if(testLogList == null||testLogList.size() == 0)
						continue;
					testLogDAO.delete(testLogList.get(0));
				}
			}
			
			tran.commit();
			return true;
		} catch (Exception e) {
			tran.rollback();
			throw e;
		} finally {
			m_dao.closeSession();
		}
	}
	
	/**
	 * 完工确认时，对现场检测委托单上的器具进行自动折扣处理
	 * @param feeAssignDAO
	 * @param dCSheet
	 * @throws Exception
	 */
	private void discountExecute(CertificateFeeAssignDAO feeAssignDAO, DiscountComSheet dCSheet) throws Exception{
		CommissionSheet cSheet = dCSheet.getCommissionSheet();
		List<Object[]>feeList = feeAssignDAO.findByHQL(CertificateFeeAssignManager.queryStringAllOldFeeByCommissionSheetId, cSheet.getId());	
		if(feeList.isEmpty()){
			throw new Exception(String.format("委托单%s没有任何费用记录，不能执行折扣操作！", cSheet.getCode()));
		}else{	//平摊至每个费用上
			Object []tempObjArray = feeList.get(0);
		    Double TestFeeOld = tempObjArray[0]==null?0:(Double)tempObjArray[0];	//该委托单原始的各项总费用
		    Double RepairFeeOld = tempObjArray[1]==null?0:(Double)tempObjArray[1];
		    Double MaterialFeeOld = tempObjArray[2]==null?0:(Double)tempObjArray[2];
		    Double CarFeeOld = tempObjArray[3]==null?0:(Double)tempObjArray[3];
		    Double DebugFeeOld = tempObjArray[4]==null?0:(Double)tempObjArray[4];
		    Double OtherFeeOld = tempObjArray[5]==null?0:(Double)tempObjArray[5];
//		    Double TotalFeeOld = tempObjArray[6]==null?0:(Double)tempObjArray[6];
		   
		    List<CertificateFeeAssign> feeAssignList = feeAssignDAO.findByHQL(CertificateFeeAssignManager.queryString_CertificateFeeAssignByCommissionSheetId, cSheet.getId());
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
	}
	
	
	
	/**
	 * 完工确认时，对协议上的器具（单张证书）进行自动折扣处理
	 * @param feeAssignDAO
	 * @param newTestFee ：协议价
	 * @param oldTestFee ：证书原价（最原始价格）
	 * @param oRecord
	 * @param certificate
	 * @throws Exception
	 */
	private void discountExecuteByCertificate(CertificateFeeAssignDAO feeAssignDAO, Double newTestFee, Double oldTestFee, OriginalRecord oRecord, Certificate certificate) throws Exception{
		String queryString = "from CertificateFeeAssign as a where a.originalRecord.id = ? and a.certificate.id = ? ";
	    List<CertificateFeeAssign> feeAssignList = feeAssignDAO.findByHQL(queryString, oRecord.getId(), certificate.getId());
	    if(feeAssignList.size() == 0){
	    	throw new Exception(String.format("找不到证书费用记录(原始记录ID:%s,证书ID:%s)，不能按照协议内容执行折扣操作！", oRecord.getId(), certificate.getId()));
	    }
	    
	    int indexMaxTestFee = -1;	//检测费用最大值的索引项
	    Double maxTestFee = null;	//检测费的最大值
	    Double countTestFee = 0.0;	//检测费的总计
	    Double dRateTestFee = (oldTestFee == null || oldTestFee <= 0 || newTestFee == null)?0:newTestFee/oldTestFee;	//折扣率
	   
	    for(int i = 0; i < feeAssignList.size(); i++){
	    	CertificateFeeAssign feeAssign = feeAssignList.get(i);
	    	feeAssign.setTestFee(new Double(Math.round((feeAssign.getTestFeeOld()==null?0:feeAssign.getTestFeeOld())*dRateTestFee)));	//更新费用
	    	feeAssign.setTotalFee((feeAssign.getTestFee()==null?0:feeAssign.getTestFee()) + 
	    			(feeAssign.getRepairFee()==null?0:feeAssign.getRepairFee()) + 
	    			(feeAssign.getMaterialFee()==null?0:feeAssign.getMaterialFee()) + 
	    			(feeAssign.getCarFee()==null?0:feeAssign.getCarFee()) + 
	    			(feeAssign.getDebugFee()==null?0:feeAssign.getDebugFee()) + 
	    			(feeAssign.getOtherFee()==null?0:feeAssign.getOtherFee()) );
	    	
	    	//判断最大费用项
	    	if(maxTestFee == null || maxTestFee <= feeAssign.getTestFee()){
	    		maxTestFee = feeAssign.getTestFee();
	    		indexMaxTestFee = i;
	    	}
	    	//计算各种费用之和
	    	countTestFee += feeAssign.getTestFee().intValue();
	    	
	    }//end for
	    
	    //计算差额：分摊至最大项
	    if(indexMaxTestFee >= 0){
	    	CertificateFeeAssign f = feeAssignList.get(indexMaxTestFee);
	    	Double balance = (newTestFee==null?0:newTestFee) - countTestFee;
	    	f.setTestFee(new Double(Math.round(f.getTestFee() + balance)));
	    	f.setTotalFee((f.getTestFee()==null?0:f.getTestFee()) + 
	    			(f.getRepairFee()==null?0:f.getRepairFee()) + 
	    			(f.getMaterialFee()==null?0:f.getMaterialFee()) + 
	    			(f.getCarFee()==null?0:f.getCarFee()) + 
	    			(f.getDebugFee()==null?0:f.getDebugFee()) + 
	    			(f.getOtherFee()==null?0:f.getOtherFee()));
	    }
	    //更新费用记录
	    for(CertificateFeeAssign feeAssign : feeAssignList){
	    	feeAssignDAO.update(feeAssign);
	    } //end 更新费用记录
			
	}

	public List<CommissionSheet> findByPropertyBySort(String orderby,
			boolean asc, KeyValueWithOperator... keyValueWithOperator) {
		// TODO Auto-generated method stub
		return m_dao.findByPropertyBySort("CommissionSheet",orderby, asc, keyValueWithOperator);
	}
	
}
