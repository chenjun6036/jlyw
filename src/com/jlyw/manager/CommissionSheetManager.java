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
	 * ����CommissionSheet Id ���� CommissionSheet����
	 * @param id: CommissionSheet Id
	 * @return CommissionSheet����
	 */
	public CommissionSheet findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��CommissionSheet��¼
	 * @param appSpecies CommissionSheet����
	 * @return ����ɹ�������true�����򷵻�false
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
	 * ����һ��CommissionSheet��¼
	 * @param appSpecies CommissionSheet����
	 * @return ���³ɹ�������true�����򷵻�false
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
	 * ����CommissionSheet Id,ɾ��CommissionSheet����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
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
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param arr ������ֵ��
	 * @return ��ҳ���CommissionSheet�б�
	 */
	public List<CommissionSheet> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("CommissionSheet", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����CommissionSheet��¼��
	 * @param arr ������ֵ��
	 * @return CommissionSheet��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("CommissionSheet", arr);
	}
	/**
	 * �õ�����CommissionSheet��¼��
	 * @param arr ��ѯ�����б�
	 * @return CommissionSheet��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("CommissionSheet", arr);
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<CommissionSheet> findByExample(CommissionSheet instance) {
		return m_dao.findByExample(instance);
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
	* @return ��ҳ��������б�- List<CommissionSheet>
	*/
	public List<CommissionSheet> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		return m_dao.findPagedAllBySort("CommissionSheet", currentPage, pageSize, orderby, asc, arr);
	}
	
	/**
	* ��ҳ��ʾ����
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param orderby�������ĸ��ֶ�����
	* @param asc��true ���� false ����
	* @param arr:Ϊ��ѯ������(��-ֵ)���б�
	* @return ��ҳ��������б�- List<CommissionSheet>
	*/
	public List<CommissionSheet> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		return m_dao.findPagedAllBySort("CommissionSheet", currentPage, pageSize, orderby, asc, arr);
	}
	public List<CommissionSheet> findByPropertyBySort(String orderby,boolean asc,List<KeyValueWithOperator>arr){
		return m_dao.findByPropertyBySort("CommissionSheet",orderby, asc, arr);
	}
	
	/**
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findByHQL(String queryString, Object...arr){
		return m_dao.findByHQL(queryString, arr);
	}
	
	/**
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findBySQL(String queryString, Object...arr){
		return m_dao.findBySQL(queryString, arr);
	}
	
	/**
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findBySQL(String queryString, List<Object> arr){
		return m_dao.findBySQL(queryString, arr);
	}
	
	/**
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findByHQL(String queryString, List<Object> arr){
		return m_dao.findByHQL(queryString, arr);
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
		return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
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
		return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
	}
	
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<CommissionSheet> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("CommissionSheet", arr);
	}
	
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<CommissionSheet> findByVarProperty(List<KeyValueWithOperator> arr){
		return m_dao.findByVarProperty("CommissionSheet", arr);
	}
	
	
	/**
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,Object...arr) {
		return m_dao.getTotalCountByHQL(queryString, arr);
	}
	
	/**
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,List<Object> arr) {
		return m_dao.getTotalCountByHQL(queryString, arr);
	}
	
	/**
	 * ����һ��CommissionSheet��¼
	 * @param cSheetList CommissionSheet����
	 * @param subConList ί�е��б��Ӧ��ת��������
	 * @param alloteeList �ɶ����б�
	 * @param subConCreateUser ת����¼������
	 * @param taskAssigner ��������ˣ�����Ϊnull����ʾϵͳ�Զ����䣩
	 * @param nowTime ������¼��ʱ��
	 * @param ComSheetType �ж����ֳ�����Ļ��Ǵ��½�ί�е��½��ġ�
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean saveByBatch(List<CommissionSheet> cSheetList, List<SubContractor> subConList, List<SysUser> alloteeList, SysUser subConCreateUser, SysUser taskAssigner, Timestamp nowTime,String ComSheetType){
		if(cSheetList == null || subConList == null || alloteeList == null || cSheetList.size() != subConList.size() || cSheetList.size() != alloteeList.size()){
			return false;
		}
		
		TaskAssignDAO taskAssignDAO = new TaskAssignDAO();	//�������DAO
		SubContractDAO subContractDAO = new SubContractDAO();	//ת����¼DAO
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
//			ViewApplianceSpecialStandardNameProjectDAO vDAO = new ViewApplianceSpecialStandardNameProjectDAO();	//�������-��׼����-��Ŀ���ϵDAO 
			AppStdNameProTeamDAO tDAO = new AppStdNameProTeamDAO();	//��׼����-��Ŀ���ϵDAO
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
			Timestamp tYear = new Timestamp(c.getTimeInMillis()); //����Ŀ�ʼʱ��
			for(int i = 0; i < cSheetList.size(); i++){
				CommissionSheet cSheet = cSheetList.get(i);
				m_dao.save(cSheet);
				SubContractor subContractor = subConList.get(i);
				if(subContractor != null){
					SubContract subConObj = new SubContract();	//ת����¼����
					subConObj.setCommissionSheet(cSheet);
					subConObj.setSysUserByLastEditorId(subConCreateUser);
					subConObj.setSubContractor(subContractor);
					subConObj.setStatus(0);
					subConObj.setLastEditTime(nowTime);
					subContractDAO.save(subConObj);
				}
				SysUser allotee = alloteeList.get(i);
				if(allotee != null){
					TaskAssign taskAssignObj = new TaskAssign();	//����������
					taskAssignObj.setCommissionSheet(cSheet);
					taskAssignObj.setSysUserByAlloteeId(allotee);
					taskAssignObj.setSysUserByAssignerId(taskAssigner);
					taskAssignObj.setAssignTime(nowTime);
					taskAssignObj.setStatus(0);
					//���ҡ����߱�׼����_��Ŀ��Id��
					if(cSheet.getSpeciesType() == false){	//��׼����
						List<AppStdNameProTeam> rList = tDAO.findByVarProperty("AppStdNameProTeam", new KeyValueWithOperator("applianceStandardName.id", cSheet.getApplianceSpeciesId(), "="));
						if(rList != null && rList.size() == 1){
							taskAssignObj.setAppStdNameProTeam(rList.get(0));
						}
					}else{	//��������:���ü����ĿΪnull
//						List<ViewApplianceSpecialStandardNameProject> rList = vDAO.findByVarProperty("ViewApplianceSpecialStandardNameProject", new KeyValueWithOperator("id.appSpeId", cSheet.getApplianceSpeciesId(), "="));
//						if(rList != null && rList.size() == 1){
//							AppStdNameProTeam temp = new AppStdNameProTeam();
//							temp.setId(rList.get(0).getId().getAppStdProId());
//							taskAssignObj.setAppStdNameProTeam(temp);
//						}
					}
					taskAssignDAO.save(taskAssignObj);
				}else{	//����������
					List<Object[]> retList = tMgr.getInspectQualifyUsersByRule(cSheet.getApplianceSpeciesId(), cSheet.getSpeciesType()?1:0, typeList, SystemCfgUtil.getTaskAllotRule(), tYear);
					for(Object []obj : retList){
						TaskAssign taskAssignObj = new TaskAssign();	//����������:�����ü�����Ŀ���ƣ������߱�׼����_��Ŀ��Id����
						SysUser alloteeTemp = new SysUser();
						alloteeTemp.setId((Integer)obj[0]);
						taskAssignObj.setCommissionSheet(cSheet);
						taskAssignObj.setSysUserByAlloteeId(alloteeTemp);
						taskAssignObj.setSysUserByAssignerId(null);	//��Ϊ�գ�˵��ϵͳ�Զ�����
						taskAssignObj.setAssignTime(nowTime);
						taskAssignObj.setStatus(0);
						taskAssignDAO.save(taskAssignObj);
					}
				}
			}
			//���Ϊ�ֳ���⣬������ֳ�ҵ���״̬Ϊ����ɣ�2��
			if(cSheetList.size() > 0 && cSheetList.get(0).getCommissionType() == 2&&ComSheetType!=null&&ComSheetType.length()>0){
				LocaleApplianceItemDAO locAppItemDAO = new LocaleApplianceItemDAO();
				LocaleApplianceItem locAppItem = locAppItemDAO.findById(cSheetList.get(0).getLocaleApplianceItemId());
				LocaleMission locMission = locAppItem.getLocaleMission();
				locMission.setStatus(2);	//�����
				new LocaleMissionDAO().update(locMission);
			}
			tran.commit();
			return true;
		} catch (Exception e) {
			
			if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
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
	 * ����һ��CommissionSheet��¼(Ԥ��ί�е�ʱʹ��)
	 * @param cSheetList CommissionSheet����
	 * @param subConList ί�е��б��Ӧ��ת��������
	 * @param alloteeList �ɶ����б�
	 * @param subConCreateUser ת����¼������
	 * @param taskAssigner ��������ˣ�����Ϊnull����ʾϵͳ�Զ����䣩
	 * @param nowTime ������¼��ʱ��
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean saveByBatchYL(List<CommissionSheet> cSheetList, SysUser taskAssigner, Timestamp nowTime){
		if(cSheetList == null ){
			return false;
		}
		
		TaskAssignDAO taskAssignDAO = new TaskAssignDAO();	//�������DAO
		SubContractDAO subContractDAO = new SubContractDAO();	//ת����¼DAO
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
//			ViewApplianceSpecialStandardNameProjectDAO vDAO = new ViewApplianceSpecialStandardNameProjectDAO();	//�������-��׼����-��Ŀ���ϵDAO 
			AppStdNameProTeamDAO tDAO = new AppStdNameProTeamDAO();	//��׼����-��Ŀ���ϵDAO
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
			Timestamp tYear = new Timestamp(c.getTimeInMillis()); //����Ŀ�ʼʱ��
			for(int i = 0; i < cSheetList.size(); i++){
				CommissionSheet cSheet = cSheetList.get(i);
				m_dao.save(cSheet);
			}
			
			tran.commit();
			return true;
		} catch (Exception e) {
			
			if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
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
	 * ����CommissionSheet��¼(Ԥ��ί�е��޸�ʱʹ��)
	 * @param cSheetList CommissionSheet����
	 * @param subConList ί�е��б��Ӧ��ת��������
	 * @param alloteeList �ɶ����б�
	 * @param subConCreateUser ת����¼������
	 * @param taskAssigner ��������ˣ�����Ϊnull����ʾϵͳ�Զ����䣩
	 * @param nowTime ������¼��ʱ��
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean updateByBatch(List<CommissionSheet> cSheetList, List<SubContractor> subConList, List<SysUser> alloteeList, SysUser subConCreateUser, SysUser taskAssigner, Timestamp nowTime){
		if(cSheetList == null || subConList == null || alloteeList == null || cSheetList.size() != subConList.size() || cSheetList.size() != alloteeList.size()){
			return false;
		}
		
		TaskAssignDAO taskAssignDAO = new TaskAssignDAO();	//�������DAO
		SubContractDAO subContractDAO = new SubContractDAO();	//ת����¼DAO
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
//			ViewApplianceSpecialStandardNameProjectDAO vDAO = new ViewApplianceSpecialStandardNameProjectDAO();	//�������-��׼����-��Ŀ���ϵDAO 
			AppStdNameProTeamDAO tDAO = new AppStdNameProTeamDAO();	//��׼����-��Ŀ���ϵDAO
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
			Timestamp tYear = new Timestamp(c.getTimeInMillis()); //����Ŀ�ʼʱ��
			for(int i = 0; i < cSheetList.size(); i++){
				CommissionSheet cSheet = cSheetList.get(i);
				m_dao.save(cSheet);
				SubContractor subContractor = subConList.get(i);
				if(subContractor != null){
					SubContract subConObj = new SubContract();	//ת����¼����
					subConObj.setCommissionSheet(cSheet);
					subConObj.setSysUserByLastEditorId(subConCreateUser);
					subConObj.setSubContractor(subContractor);
					subConObj.setStatus(0);
					subConObj.setLastEditTime(nowTime);
					subContractDAO.save(subConObj);
				}
				SysUser allotee = alloteeList.get(i);
				if(allotee != null){
					TaskAssign taskAssignObj = new TaskAssign();	//����������
					taskAssignObj.setCommissionSheet(cSheet);
					taskAssignObj.setSysUserByAlloteeId(allotee);
					taskAssignObj.setSysUserByAssignerId(taskAssigner);
					taskAssignObj.setAssignTime(nowTime);
					taskAssignObj.setStatus(0);
					//���ҡ����߱�׼����_��Ŀ��Id��
					if(cSheet.getSpeciesType() == false){	//��׼����
						List<AppStdNameProTeam> rList = tDAO.findByVarProperty("AppStdNameProTeam", new KeyValueWithOperator("applianceStandardName.id", cSheet.getApplianceSpeciesId(), "="));
						if(rList != null && rList.size() == 1){
							taskAssignObj.setAppStdNameProTeam(rList.get(0));
						}
					}else{	//��������:���ü����ĿΪnull
//						List<ViewApplianceSpecialStandardNameProject> rList = vDAO.findByVarProperty("ViewApplianceSpecialStandardNameProject", new KeyValueWithOperator("id.appSpeId", cSheet.getApplianceSpeciesId(), "="));
//						if(rList != null && rList.size() == 1){
//							AppStdNameProTeam temp = new AppStdNameProTeam();
//							temp.setId(rList.get(0).getId().getAppStdProId());
//							taskAssignObj.setAppStdNameProTeam(temp);
//						}
					}
					taskAssignDAO.save(taskAssignObj);
				}else{	//����������
					List<Object[]> retList = tMgr.getInspectQualifyUsersByRule(cSheet.getApplianceSpeciesId(), cSheet.getSpeciesType()?1:0, typeList, SystemCfgUtil.getTaskAllotRule(), tYear);
					for(Object []obj : retList){
						TaskAssign taskAssignObj = new TaskAssign();	//����������:�����ü�����Ŀ���ƣ������߱�׼����_��Ŀ��Id����
						SysUser alloteeTemp = new SysUser();
						alloteeTemp.setId((Integer)obj[0]);
						taskAssignObj.setCommissionSheet(cSheet);
						taskAssignObj.setSysUserByAlloteeId(alloteeTemp);
						taskAssignObj.setSysUserByAssignerId(null);	//��Ϊ�գ�˵��ϵͳ�Զ�����
						taskAssignObj.setAssignTime(nowTime);
						taskAssignObj.setStatus(0);
						taskAssignDAO.save(taskAssignObj);
					}
				}
			}
			//���Ϊ�ֳ���⣬������ֳ�ҵ���״̬Ϊ����ɣ�2��
			if(cSheetList.size() > 0 && cSheetList.get(0).getCommissionType() == 2){
				LocaleApplianceItemDAO locAppItemDAO = new LocaleApplianceItemDAO();
				LocaleApplianceItem locAppItem = locAppItemDAO.findById(cSheetList.get(0).getLocaleApplianceItemId());
				LocaleMission locMission = locAppItem.getLocaleMission();
				locMission.setStatus(2);	//�����
				new LocaleMissionDAO().update(locMission);
			}
			tran.commit();
			return true;
		} catch (Exception e) {
			
			if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
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
	 * ����CommissionSheet��¼���޸�ί�е�ʱʹ�ã�
	 * @param cSheetList CommissionSheet����
	 * @param subConList ί�е��б��Ӧ��ת��������
	 * @param alloteeList �ɶ����б�
	 * @param subConCreateUser ת����¼������
	 * @param taskAssigner ��������ˣ�����Ϊnull����ʾϵͳ�Զ����䣩
	 * @param nowTime ������¼��ʱ��
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean updateByBatch2(List<CommissionSheet> cSheetList, List<SubContractor> subConList, List<SysUser> alloteeList, SysUser subConCreateUser, SysUser taskAssigner, Timestamp nowTime){
		if(cSheetList == null || subConList == null || alloteeList == null || cSheetList.size() != subConList.size() || cSheetList.size() != alloteeList.size()){
			return false;
		}
		
		TaskAssignDAO taskAssignDAO = new TaskAssignDAO();	//�������DAO
		SubContractDAO subContractDAO = new SubContractDAO();	//ת����¼DAO
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
//			ViewApplianceSpecialStandardNameProjectDAO vDAO = new ViewApplianceSpecialStandardNameProjectDAO();	//�������-��׼����-��Ŀ���ϵDAO 
			AppStdNameProTeamDAO tDAO = new AppStdNameProTeamDAO();	//��׼����-��Ŀ���ϵDAO
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
			Timestamp tYear = new Timestamp(c.getTimeInMillis()); //����Ŀ�ʼʱ��
			
			for(int i = 0; i < cSheetList.size(); i++){
				CommissionSheet cSheet = cSheetList.get(i);
				StringBuilder remark = new StringBuilder("");
				if(cSheet.getRemark()!=null&&cSheet.getRemark().length()>0){
					remark.append(cSheet.getRemark());
					remark.append(";");
				}
				
				remark.append(subConCreateUser.getName());
				remark.append("��");
				remark.append(DateTimeFormatUtil.DateTimeFormat.format(nowTime));
				remark.append("�޸��˸�ί�е�");
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
					SubContract subConObj = new SubContract();	//ת����¼����
					subConObj.setCommissionSheet(cSheet);
					subConObj.setSysUserByLastEditorId(subConCreateUser);
					subConObj.setSubContractor(subContractor);
					subConObj.setStatus(0);
					subConObj.setLastEditTime(nowTime);
					subContractDAO.save(subConObj);
				}
				SysUser allotee = alloteeList.get(i);
				if(allotee != null){
					TaskAssign taskAssignObj = new TaskAssign();	//����������
					taskAssignObj.setCommissionSheet(cSheet);
					taskAssignObj.setSysUserByAlloteeId(allotee);
					taskAssignObj.setSysUserByAssignerId(taskAssigner);
					taskAssignObj.setAssignTime(nowTime);
					taskAssignObj.setStatus(0);
					//���ҡ����߱�׼����_��Ŀ��Id��
					if(cSheet.getSpeciesType() == false){	//��׼����
						List<AppStdNameProTeam> rList = tDAO.findByVarProperty("AppStdNameProTeam", new KeyValueWithOperator("applianceStandardName.id", cSheet.getApplianceSpeciesId(), "="));
						if(rList != null && rList.size() == 1){
							taskAssignObj.setAppStdNameProTeam(rList.get(0));
						}
					}else{	//��������:���ü����ĿΪnull
//						List<ViewApplianceSpecialStandardNameProject> rList = vDAO.findByVarProperty("ViewApplianceSpecialStandardNameProject", new KeyValueWithOperator("id.appSpeId", cSheet.getApplianceSpeciesId(), "="));
//						if(rList != null && rList.size() == 1){
//							AppStdNameProTeam temp = new AppStdNameProTeam();
//							temp.setId(rList.get(0).getId().getAppStdProId());
//							taskAssignObj.setAppStdNameProTeam(temp);
//						}
					}
					taskAssignDAO.save(taskAssignObj);
				}else{	//����������
					List<Object[]> retList = tMgr.getInspectQualifyUsersByRule(cSheet.getApplianceSpeciesId(), cSheet.getSpeciesType()?1:0, typeList, SystemCfgUtil.getTaskAllotRule(), tYear);
					for(Object []obj : retList){
						TaskAssign taskAssignObj = new TaskAssign();	//����������:�����ü�����Ŀ���ƣ������߱�׼����_��Ŀ��Id����
						SysUser alloteeTemp = new SysUser();
						alloteeTemp.setId((Integer)obj[0]);
						taskAssignObj.setCommissionSheet(cSheet);
						taskAssignObj.setSysUserByAlloteeId(alloteeTemp);
						taskAssignObj.setSysUserByAssignerId(null);	//��Ϊ�գ�˵��ϵͳ�Զ�����
						taskAssignObj.setAssignTime(nowTime);
						taskAssignObj.setStatus(0);
						taskAssignDAO.save(taskAssignObj);
					}
				}
			}
			
			tran.commit();
			return true;
		} catch (Exception e) {
			
			if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
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
	 * �깤ȷ��
	 * @param cSheet
	 * @param confirmUser
	 * @param finishLocation
	 * @param confirmTime
	 * @param quantity:��Ч������Ŀ����ʱ���ã�
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean commissionSheetFinishConfirm(CommissionSheet cSheet, SysUser confirmUser, String finishLocation, Timestamp confirmTime, int quantity) throws Exception{
		if(cSheet == null || confirmUser == null || confirmTime == null){
			return false;
		}
		CertificateFeeAssignDAO feeAssignDAO = new CertificateFeeAssignDAO();	//���÷���DAO
		TaskAssignDAO taskAssignDAO = new TaskAssignDAO();	//�������DAO
		OriginalRecordDAO oRecordDAO = new OriginalRecordDAO();	//ԭʼ��¼DAO
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			//Ϊû���ֶ�������õ�ԭʼ��¼�������
			List<OriginalRecord> oRecordList = oRecordDAO.findByVarProperty("OriginalRecord", 
					new KeyValueWithOperator("commissionSheet.id", cSheet.getId(), "="),
					new KeyValueWithOperator("status", 1, "<>"),	//ԭʼ��¼δע��
					new KeyValueWithOperator("taskAssign.status", 1, "<>"),	//���������δע��
					new KeyValueWithOperator("certificate.pdf", null, "is not null"));	//����֤����ʽ�汾
			for(OriginalRecord oRecord : oRecordList){
				int iRet = feeAssignDAO.getTotalCount("CertificateFeeAssign", 
						new KeyValueWithOperator("originalRecord.id", oRecord.getId(), "="),
						new KeyValueWithOperator("certificate.id", oRecord.getCertificate().getId(), "="));
				if(iRet == 0){	//û�м�¼�����������÷����¼
					CertificateFeeAssign feeAssign = new CertificateFeeAssign();
					feeAssign.setSysUserByFeeAlloteeId(oRecord.getSysUserByStaffId());
					feeAssign.setTestFee(oRecord.getTestFee()==null?0:new Double(Math.round(oRecord.getTestFee())));	//�ּ�
					feeAssign.setTestFeeOld(feeAssign.getTestFee());	//ԭ��
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
			
			//ע��û��������ʽ�汾֤���ԭʼ��¼
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
			
			//������������
			List<TaskAssign> tList = taskAssignDAO.findByVarProperty("TaskAssign", new KeyValue("commissionSheet.id", cSheet.getId()), new KeyValue("status",0));
			for(TaskAssign t : tList){
				t.setFinishTime(confirmTime);
				taskAssignDAO.update(t);
			}
			
			//ɾ����Ч���������(û��ԭʼ��¼��û��Ϊ����������˷������)
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
			
			//������Ч��������䣨ֻ����Ч��ԭʼ��¼��û�з�����ã�
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
			
			//����ί�е�
			cSheet.setStatus(3);	//3�깤ȷ��
			cSheet.setFinishStaffId(confirmUser.getId()); 
			cSheet.setFinishLocation(finishLocation);
			cSheet.setFinishDate(confirmTime);
			m_dao.update(cSheet);
			
			LocaleApplianceItemDAO locAppItemDAO = new LocaleApplianceItemDAO();
			DiscountComSheetDAO dCSheetDAO = new DiscountComSheetDAO();
			//�Զ��ۿ����̣����ж��Ƿ����ֳ����ί���飬���ж��Ƿ���Э��
			if(cSheet.getCommissionType() == 2 && 
					cSheet.getLocaleCommissionCode() != null && 
					cSheet.getLocaleApplianceItemId() != null && 
					locAppItemDAO.findById(cSheet.getLocaleApplianceItemId()) != null){	//�۸�����ֳ����ί����ļ۸�
//				Discount discount = null;
//				//�����Ƿ��иü��ί�����µ�ί�е����Զ��ۿ������¼�����У�������һ����Ŀ����������һ���Զ��ۿ������¼��
//				List<Discount> discountList = dCSheetDAO.findByHQL("select model.discount from DiscountComSheet as model " +
//						" where model.commissionSheet.localeCommissionCode = ? and " +
//						" model.discount.customer.id = ? and " +
//						" model.discount.applyTime = model.discount.executeTime ",	//����ʱ������׼ʱ����ͬ���ֳ�������Զ��ۿ����̣�
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
//					discount.setExecuteMsg("ϵͳ�Զ�����ͨ����");
//					discount.setExecuteResult(true);	//���ͨ��
//					discount.setExecuteTime(confirmTime);	//���ʱ�䣺������ʱ����ͬ
//					
//					//��������ԭ��
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
//					//���������˺������ˣ���Ϊ��ϵͳ����Ա��
//					SysUser applyUser = null;
//					try{
//						applyUser = (SysUser)new SysUserDAO().findByHQL("from SysUser as model where model.userName = ? ", SystemCfgUtil.SystemManagerUserName).get(0);
//					}catch(Exception e){
//						throw new Exception(String.format("�Ҳ����û���Ϊ'%s'(ϵͳ����Ա)���û��˺ţ��Զ��ۿ����̴���ʧ�ܣ�", SystemCfgUtil.SystemManagerUserName));
//					}
//					discount.setSysUserByRequesterId(applyUser);
//					discount.setSysUserByExecutorId(applyUser);
//				}else{
//					discount = discountList.get(0);
//				}
//				
//				//ִ���ۿ۲���
//				DiscountComSheet dCSheet = null;
//				if(discount.getId() == null){	//�µ��ۿ�
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
//				List<Object[]> feeList = feeAssignDAO.findByHQL(CertificateFeeAssignManager.queryStringAllAllFeeByCommissionSheetId, cSheet.getId());	//��ȡί�е������з���
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
//					dCSheet.setTestFee(locAppItem.getTestCost()==null?TestFee:locAppItem.getTestCost());	//����
//					dCSheet.setRepairFee(locAppItem.getRepairCost()==null?RepairFee:locAppItem.getRepairCost());	//�����
//					dCSheet.setMaterialFee(locAppItem.getMaterialCost()==null?MaterialFee:locAppItem.getMaterialCost());	//���Ϸ�
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
//						new DiscountDAO().save(discount);	//�洢�ۿ�
//					}
//					if(dCSheet.getId() == null){
//						dCSheetDAO.save(dCSheet);
//					}else{
//						dCSheetDAO.update(dCSheet);
//					}
//					discountExecute(feeAssignDAO, dCSheet);
//				}
//				//����ԭʼ��¼���õ�HQL
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
			}else{	/***************  �ж��Ƿ���Э��     ***************/
				//��ѯ��������ʽ֤���ԭʼ��¼
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(confirmTime.getTime());
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				java.sql.Date dealValidaty = new java.sql.Date(calendar.getTimeInMillis());
				String queryStringDeqlItem = "from DealItem as model where model.deal.status<>1 and model.deal.customer.id = ? and " +	//ί�е�λ
						" ( model.deal.validity is null or model.deal.validity >= ? )  and " +	//Э����Ч��
						" model.applianceStandardName = ? " + //���߱�׼����  
						" order by model.deal.signDate desc, model.id desc";	//����Э��ǩ�����ڽ�������
				DealItemDAO dealItemDAO = new DealItemDAO();
				boolean bExecuteDealDiscount = false;	//�ж��Ƿ��и���Э����ۣ�true���У�false��û�У�
				for(OriginalRecord oRecord : oRecordList){
					List<DealItem> dItemList = dealItemDAO.findByHQL(queryStringDeqlItem, 
							cSheet.getCustomerId(), dealValidaty, oRecord.getTargetAppliance().getApplianceStandardName());
					DealItem dItemSelected = null;
					for(DealItem dItem : dItemList){	//��ȡһ������������Э����Ŀ
						if(dItem.getModel() != null && dItem.getModel().trim().length() > 0){
							if(!dItem.getModel().equalsIgnoreCase(oRecord.getModel())){	//��ԭʼ��¼�е��ͺŲ����
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
						if(dItem.getDealPrice() == null){	//����Ϊ��
							continue;
						}
						dItemSelected = dItem;
						break;
					}//end for
					
					if(dItemSelected != null){	//�з���������Э����Ŀ
						List<Object[]> feeList = feeAssignDAO.findByHQL(CertificateFeeAssignManager.queryStringAllAllFeeByOriginalRecordIdAndCertificateId, 
								oRecord.getId(), oRecord.getCertificate().getId());	//��ȡ��֤������з���
						if(feeList.size() > 0){
							Object[] fee = feeList.get(0);
							Double TestFee = fee[0]==null?0.0:(Double)fee[0];	//��֤��ļ���(�ּ�)
							Double OldTestFee = fee[7]==null?0.0:(Double)fee[7];	//��֤��ļ���(ԭ��)
							Double dealPrice = dItemSelected.getDealPrice() * (oRecord.getQuantity()==null?1:oRecord.getQuantity());
							if(oRecord.getQuantity() != null && !dealPrice.equals(TestFee)){	//���ò���ȣ������ۿ۲���
								discountExecuteByCertificate(feeAssignDAO, dealPrice, OldTestFee, oRecord, oRecord.getCertificate());
								bExecuteDealDiscount = true;
							}
						}
					} //end if
				}//end for
				
				if(bExecuteDealDiscount){	//����Э����ۣ��洢�ۿ���Ϣ
					Discount discount = new Discount();
					discount.setApplyTime(confirmTime);
					Customer customer = new Customer();
					customer.setId(cSheet.getCustomerId());
					discount.setContector("");
					discount.setContectorTel("");
					discount.setCustomer(customer);
					discount.setExecuteMsg("ϵͳ�Զ�����ͨ����");
					discount.setExecuteResult(true);	//���ͨ��
					
					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(confirmTime.getTime());
					c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 5);
					discount.setExecuteTime(new Timestamp(c.getTimeInMillis()));	//���ʱ�䣺����ʱ��+5����
					
					//��������ԭ��
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
					
					//���������˺������ˣ���Ϊ��ϵͳ����Ա��
					SysUser applyUser = null;
					try{
						applyUser = (SysUser)new SysUserDAO().findByHQL("from SysUser as model where model.userName = ? ", SystemCfgUtil.SystemManagerUserName).get(0);
					}catch(Exception e){
						throw new Exception(String.format("�Ҳ����û���Ϊ'%s'(ϵͳ����Ա)���û��˺ţ��Զ��ۿ����̴���ʧ�ܣ�", SystemCfgUtil.SystemManagerUserName));
					}
					discount.setSysUserByRequesterId(applyUser);
					discount.setSysUserByExecutorId(applyUser);
					
					//ִ���ۿ۲���
					DiscountComSheet dCSheet = new DiscountComSheet();
					List<Object[]> feeList = feeAssignDAO.findByHQL(CertificateFeeAssignManager.queryStringAllAllFeeByCommissionSheetId, cSheet.getId());	//��ȡί�е������з���
					if(feeList.isEmpty()){
						throw new Exception(String.format("����Э�����ݽ����Զ��ۿ�ʱ�����Ҳ���ί�е���%s���µķ��ü�¼��", cSheet.getCode()));
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
					dCSheet.setTestFee(TestFee);	//����
					dCSheet.setRepairFee(RepairFee);	//�����
					dCSheet.setMaterialFee(MaterialFee);	//���Ϸ�
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
						new DiscountDAO().save(discount);	//�洢�ۿ�
					}
					if(dCSheet.getId() == null){
						dCSheetDAO.save(dCSheet);
					}else{
						dCSheetDAO.update(dCSheet);
					}
					//����ԭʼ��¼���õ�HQL
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
			
			}//end else(�ж��Ƿ���Э��)
			
			//�Լ�ҵ���Զ�������ֵ��Դ��¼��by Kimi��
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
			if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
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
	 * ���ɷ����嵥�������嵥 �� ί�е���
	 * 
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean saveByBatch(List<CommissionSheet> comSheetList,
			DetailList deList) {
		if(comSheetList == null || deList == null){
			return false;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			DetailListDAO dDAO = new DetailListDAO();	//�嵥���DAO 
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
	 * ����һ��CommissionSheet��¼
	 * @param CommissionSheetList CommissionSheet����
	 * @return ���³ɹ�������true�����򷵻�false
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
	 * ί�е����˵��أ����ѽ��ʡ������������깤��״̬��
	 * @param cSheet
	 * @return
	 */
	public boolean checkOutReject(CommissionSheet cSheet, SysUser handleUser) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			String detailListCode = cSheet.getDetailListCode();	//�����嵥��
			if(detailListCode != null && detailListCode.length() > 0){ //���ҽ����嵥�����п۳���ί�е�����Ӧ���á��۷ѹ����ȿ��ֽ��ٿ�֧Ʊ������Ԥ���ֱ���۹�Ϊֹ
				DetailListDAO dListDao = new DetailListDAO();
				List<DetailList> dlList = dListDao.findByVarProperty("DetailList", 
						new KeyValueWithOperator("code", detailListCode, "="),	//�嵥��
						new KeyValueWithOperator("status", 2, "="));	//�ѽ��ʵ��嵥
				DetailList dl = null;
				if(dlList.size() == 1){
					dl = dlList.get(0);
				}
				if(dl != null){
					//����ί�е��ķ���
					Double dTotalFee = cSheet.getTotalFee()==null?0.0:cSheet.getTotalFee();	//���˵��ܶ�
					
					Timestamp nowTime = new Timestamp(System.currentTimeMillis());
					CustomerAccountDAO cusAccountDao = new CustomerAccountDAO();
					CustomerDAO customerDao = new CustomerDAO();
					
					if(dTotalFee > 0){
						double balance = dTotalFee;	//���
						if(dl.getCashPaid() != null && dl.getCashPaid() > 0){	//�۳��ֽ�
							double cash = dl.getCashPaid();
							dl.setCashPaid((cash - balance >= 0)?cash - balance:0);
							balance -= (cash - balance >= 0)?balance : cash;
						}
						if(balance > 0 && dl.getChequePaid() != null && dl.getChequePaid() > 0){	//�۳�֧Ʊ����
							double money = dl.getChequePaid();
							dl.setCashPaid((money - balance >= 0)?money - balance:0);
							balance -= (money - balance >= 0)?balance : money;
						}
						if(balance > 0 && dl.getAccountPaid() != null && dl.getAccountPaid() > 0){	//�۳�֧Ʊ����
							double money = dl.getAccountPaid();
							dl.setCashPaid((money - balance >= 0)?money - balance:0);
							
							/*** ����ί�е�λ�˻���ȡ��ϸ���� **/
							CustomerAccount cusAccount = new CustomerAccount();// ί�е�λ�˻���ȡ��ϸ
							Customer c = customerDao.findById(cSheet.getCustomerId());
							cusAccount.setCustomer(c);
							cusAccount.setHandleType(0);	//��ֵ
							cusAccount.setHandleTime(nowTime);
							cusAccount.setSysUser(handleUser);
							cusAccount.setHandlerName(handleUser==null?"":handleUser.getName());
							cusAccount.setAmount((money - balance >= 0)?balance:money);
							cusAccount.setRemark(String.format("[%s��%s��ί�е�%s���н��˵���,�˻�Ԥ���˻���� %sԪ]", 
											cusAccount.getHandlerName(), 
											DateTimeFormatUtil.DateTimeFormat.format(nowTime),
											cSheet.getCode(),
											cusAccount.getAmount()
							));
							cusAccountDao.save(cusAccount);
							//����ί�е�λ�˻����
							c.setBalance((c.getBalance()==null?0:c.getBalance()) + ((money - balance >= 0)?balance:money));
							customerDao.update(c);
							
						}
						
						//�жϸý����嵥�Ƿ���������ί�е������û�У�����嵥ע��
						String HQLStr = "from CommissionSheet as a where a.detailListCode = ? and a.status <> 10 ";
						List<CommissionSheet> comList = dListDao.findByHQL(HQLStr, dl.getCode());
						if(comList != null && comList.size() == 1){
							dl.setStatus(1);
						}
						
						dListDao.update(dl);
					}//end if
					
				}
				
			}
			
			//����ί�е�
			cSheet.setCheckOutDate(null);	//����ʱ��
			cSheet.setCheckOutStaffId(null);	//������
			cSheet.setInvoiceCode(null);	//��Ʊ��
			cSheet.setDetailListCode(null);	//�嵥��
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
	 * ί�е�ע��
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
			
			TaskAssignDAO tAssignDao = new TaskAssignDAO();	//���������������������ʱ����Ϊ��
			tAssignDao.updateByHQL("update TaskAssign set finishTime = null where commissionSheet.id = ? ", cSheet.getId());			
			
			cSheet.setDetailListCode(null);
			cSheet.setFinishDate(null);
			cSheet.setFinishStaffId(null);
			cSheet.setFinishLocation(null);
			m_dao.update(cSheet);
			
			//�Լ�ҵ���Զ�ɾ����ֵ��Դ��¼��by Kimi��
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
	 * �깤ȷ��ʱ�����ֳ����ί�е��ϵ����߽����Զ��ۿ۴���
	 * @param feeAssignDAO
	 * @param dCSheet
	 * @throws Exception
	 */
	private void discountExecute(CertificateFeeAssignDAO feeAssignDAO, DiscountComSheet dCSheet) throws Exception{
		CommissionSheet cSheet = dCSheet.getCommissionSheet();
		List<Object[]>feeList = feeAssignDAO.findByHQL(CertificateFeeAssignManager.queryStringAllOldFeeByCommissionSheetId, cSheet.getId());	
		if(feeList.isEmpty()){
			throw new Exception(String.format("ί�е�%sû���κη��ü�¼������ִ���ۿ۲�����", cSheet.getCode()));
		}else{	//ƽ̯��ÿ��������
			Object []tempObjArray = feeList.get(0);
		    Double TestFeeOld = tempObjArray[0]==null?0:(Double)tempObjArray[0];	//��ί�е�ԭʼ�ĸ����ܷ���
		    Double RepairFeeOld = tempObjArray[1]==null?0:(Double)tempObjArray[1];
		    Double MaterialFeeOld = tempObjArray[2]==null?0:(Double)tempObjArray[2];
		    Double CarFeeOld = tempObjArray[3]==null?0:(Double)tempObjArray[3];
		    Double DebugFeeOld = tempObjArray[4]==null?0:(Double)tempObjArray[4];
		    Double OtherFeeOld = tempObjArray[5]==null?0:(Double)tempObjArray[5];
//		    Double TotalFeeOld = tempObjArray[6]==null?0:(Double)tempObjArray[6];
		   
		    List<CertificateFeeAssign> feeAssignList = feeAssignDAO.findByHQL(CertificateFeeAssignManager.queryString_CertificateFeeAssignByCommissionSheetId, cSheet.getId());
		    if(feeAssignList.size() == 0){
		    	throw new Exception(String.format("ί�е�%sû���κη��ü�¼������ִ���ۿ۲�����", cSheet.getCode()));
		    }
		    int indexMaxTestFee = -1, indexMaxRepairFee = -1, indexMaxMaterialFee = -1, indexMaxCarFee = -1, indexMaxDebugFee = -1, indexMaxOtherFee = -1;	//���ַ������ֵ��������
		    Double maxTestFee = null, maxRepairFee = null, maxMaterialFee = null, maxCarFee = null, maxDebugFee = null, maxOtherFee = null;	//���ַ��õ����ֵ
		    Double countTestFee = 0.0, countRepairFee = 0.0, countMaterialFee = 0.0, countCarFee = 0.0, countDebugFee = 0.0, countOtherFee = 0.0;	//���ַ��õ��ܼ�
		    Double dRateTestFee = (TestFeeOld == null || TestFeeOld <= 0 || dCSheet.getTestFee() == null)?0:dCSheet.getTestFee()/TestFeeOld;	//�ۿ���
		    Double dRateRepairFee = (RepairFeeOld == null || RepairFeeOld <= 0 || dCSheet.getRepairFee() == null)?0:dCSheet.getRepairFee()/RepairFeeOld;
		    Double dRateMaterialFee = (MaterialFeeOld == null || MaterialFeeOld <= 0 || dCSheet.getMaterialFee() == null)?0:dCSheet.getMaterialFee()/MaterialFeeOld;
		    Double dRateCarFee = (CarFeeOld == null || CarFeeOld == 0 || dCSheet.getCarFee() == null)?0:dCSheet.getCarFee()/CarFeeOld;
		    Double dRateDebugFee = (DebugFeeOld == null || DebugFeeOld == 0 || dCSheet.getDebugFee() == null)?0:dCSheet.getDebugFee()/DebugFeeOld;
		    Double dRateOtherFee = (OtherFeeOld == null || OtherFeeOld == 0 || dCSheet.getOtherFee() == null)?0:dCSheet.getOtherFee()/OtherFeeOld;
		    
		    for(int i = 0; i < feeAssignList.size(); i++){
		    	CertificateFeeAssign feeAssign = feeAssignList.get(i);
		    	feeAssign.setTestFee(new Double(Math.round((feeAssign.getTestFeeOld()==null?0:feeAssign.getTestFeeOld())*dRateTestFee)));	//���·���
		    	feeAssign.setRepairFee(new Double(Math.round((feeAssign.getRepairFeeOld()==null?0:feeAssign.getRepairFeeOld())*dRateRepairFee)));
		    	feeAssign.setMaterialFee(new Double(Math.round((feeAssign.getMaterialFeeOld()==null?0:feeAssign.getMaterialFeeOld())*dRateMaterialFee)));
		    	feeAssign.setCarFee(new Double(Math.round((feeAssign.getCarFeeOld()==null?0:feeAssign.getCarFeeOld())*dRateCarFee)));
		    	feeAssign.setDebugFee(new Double(Math.round((feeAssign.getDebugFeeOld()==null?0:feeAssign.getDebugFeeOld())*dRateDebugFee)));
		    	feeAssign.setOtherFee(new Double(Math.round((feeAssign.getOtherFeeOld()==null?0:feeAssign.getOtherFeeOld())*dRateOtherFee)));
		    	feeAssign.setTotalFee(new Double(feeAssign.getTestFee().intValue() + feeAssign.getRepairFee().intValue() + feeAssign.getMaterialFee().intValue() + feeAssign.getCarFee().intValue() + feeAssign.getDebugFee().intValue() + feeAssign.getOtherFee().intValue()));
		    	
		    	//�ж���������
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
		    	//������ַ���֮��
		    	countTestFee += feeAssign.getTestFee().intValue();
		    	countRepairFee += feeAssign.getRepairFee().intValue();
		    	countMaterialFee += feeAssign.getMaterialFee().intValue();
		    	countCarFee += feeAssign.getCarFee().intValue();
		    	countDebugFee += feeAssign.getDebugFee().intValue();
		    	countOtherFee += feeAssign.getOtherFee().intValue();
		    	
		    }//end for
		    
		    //�������̯�������
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
		    
		    //���·��ü�¼
		    for(CertificateFeeAssign feeAssign : feeAssignList){
		    	feeAssignDAO.update(feeAssign);
		    } //end ���·��ü�¼
	    }//end else
	}
	
	
	
	/**
	 * �깤ȷ��ʱ����Э���ϵ����ߣ�����֤�飩�����Զ��ۿ۴���
	 * @param feeAssignDAO
	 * @param newTestFee ��Э���
	 * @param oldTestFee ��֤��ԭ�ۣ���ԭʼ�۸�
	 * @param oRecord
	 * @param certificate
	 * @throws Exception
	 */
	private void discountExecuteByCertificate(CertificateFeeAssignDAO feeAssignDAO, Double newTestFee, Double oldTestFee, OriginalRecord oRecord, Certificate certificate) throws Exception{
		String queryString = "from CertificateFeeAssign as a where a.originalRecord.id = ? and a.certificate.id = ? ";
	    List<CertificateFeeAssign> feeAssignList = feeAssignDAO.findByHQL(queryString, oRecord.getId(), certificate.getId());
	    if(feeAssignList.size() == 0){
	    	throw new Exception(String.format("�Ҳ���֤����ü�¼(ԭʼ��¼ID:%s,֤��ID:%s)�����ܰ���Э������ִ���ۿ۲�����", oRecord.getId(), certificate.getId()));
	    }
	    
	    int indexMaxTestFee = -1;	//���������ֵ��������
	    Double maxTestFee = null;	//���ѵ����ֵ
	    Double countTestFee = 0.0;	//���ѵ��ܼ�
	    Double dRateTestFee = (oldTestFee == null || oldTestFee <= 0 || newTestFee == null)?0:newTestFee/oldTestFee;	//�ۿ���
	   
	    for(int i = 0; i < feeAssignList.size(); i++){
	    	CertificateFeeAssign feeAssign = feeAssignList.get(i);
	    	feeAssign.setTestFee(new Double(Math.round((feeAssign.getTestFeeOld()==null?0:feeAssign.getTestFeeOld())*dRateTestFee)));	//���·���
	    	feeAssign.setTotalFee((feeAssign.getTestFee()==null?0:feeAssign.getTestFee()) + 
	    			(feeAssign.getRepairFee()==null?0:feeAssign.getRepairFee()) + 
	    			(feeAssign.getMaterialFee()==null?0:feeAssign.getMaterialFee()) + 
	    			(feeAssign.getCarFee()==null?0:feeAssign.getCarFee()) + 
	    			(feeAssign.getDebugFee()==null?0:feeAssign.getDebugFee()) + 
	    			(feeAssign.getOtherFee()==null?0:feeAssign.getOtherFee()) );
	    	
	    	//�ж���������
	    	if(maxTestFee == null || maxTestFee <= feeAssign.getTestFee()){
	    		maxTestFee = feeAssign.getTestFee();
	    		indexMaxTestFee = i;
	    	}
	    	//������ַ���֮��
	    	countTestFee += feeAssign.getTestFee().intValue();
	    	
	    }//end for
	    
	    //�������̯�������
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
	    //���·��ü�¼
	    for(CertificateFeeAssign feeAssign : feeAssignList){
	    	feeAssignDAO.update(feeAssign);
	    } //end ���·��ü�¼
			
	}

	public List<CommissionSheet> findByPropertyBySort(String orderby,
			boolean asc, KeyValueWithOperator... keyValueWithOperator) {
		// TODO Auto-generated method stub
		return m_dao.findByPropertyBySort("CommissionSheet",orderby, asc, keyValueWithOperator);
	}
	
}
