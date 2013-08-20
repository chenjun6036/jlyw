package com.jlyw.manager;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Information;
import com.jlyw.hibernate.InformationDAO;
import com.jlyw.hibernate.SysUser;
import com.jlyw.util.KeyValueWithOperator;

public class InformationManager {
	private static InformationManager instance = null;
	/**
	 * 增加一条信息通知
	 * @param number
	 * @param content
	 * @param receiver
	 * @param type
	 * @return
	 */
	public static boolean AddInformation(String Url, String content, SysUser receiver, int type){
		try{
			if(instance == null){
				instance = new InformationManager();
			}
			Information s = new Information();
			s.setUrl(Url);
			s.setContents(content);
			s.setSysUser(receiver);//接收人
			s.setType(type);
			s.setStatus(0);	//状态：未发送
			s.setCreateDate(new Timestamp(System.currentTimeMillis()));
			return instance.save(s);
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 按条件查找
	 * @param arr
	 * @return 成功返回List，失败返回null
	 */
	public static List<Information> GetInformation(KeyValueWithOperator...arr){
		try{
			if(instance == null){
				instance = new InformationManager();
			}
			return instance.findByVarProperty(arr);
		}catch(Exception e){
			return null;
		}
	}
	
	public static List<Information> findPageAllInformationBySort(int currentPage, int pageSize, String orderby, boolean asc, KeyValueWithOperator...arr){
		try{
			if(instance == null){
				instance = new InformationManager();
			}
			return instance.findPagedAllBySort(currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	public static int getTotalCountInformarion(KeyValueWithOperator...arr){
		try{
			if(instance == null){
				instance = new InformationManager();
			}
			return instance.getTotalCount(arr);
		}catch(Exception e){
			return 0;
		}
	}
	
	/**
	 * 设置一条消息已读
	 * @param info
	 * @return
	 */
	public static boolean setInformationReaded(Information info){
		try{
			if(instance == null){
				instance = new InformationManager();
			}
			info.setStatus(1);
			return instance.update(info);
		}catch(Exception e){
			return false;
		}
	}
	/**
	 * 按条件删除
	 * @param sms
	 * @return
	 */
	public static int deleteInformation(List<KeyValueWithOperator> condList){
		try{
			if(instance == null){
				instance = new InformationManager();
			}
			return instance.delete(condList);
		}catch(Exception e){
			return -1;
		}
	}
	
	private InformationDAO m_dao = new InformationDAO();
	/**
	 * 根据Information Id 查找 Information对象
	 * @param id: Information Id
	 * @return Information对象
	 */
	public Information findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条Information记录
	 * @param appSpecies Information对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(Information appSpecies){
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
	 * 更新一条Information记录
	 * @param appSpecies Information对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(Information appSpecies){
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
	 * 根据Information Id,删除Information对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Information u = m_dao.findById(id);
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
	public int delete(List<KeyValueWithOperator> condList){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			int iRet = m_dao.delete("Information", condList);	
			tran.commit();
			return iRet;
		} catch (Exception e) {
			tran.rollback();
			return -1;
		} finally {
			m_dao.closeSession();
		}
	}

	/**
	 * 按条件查找
	 * @param arr
	 * @return
	 */
	public List<Information> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Information", arr);
		}
		catch(Exception e){
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
	* @param arr:为查询条件的(键-值)对数组
	* @return 分页后的数据列表- List<Student>
	*/
	public List<Information> findPagedAllBySort(int currentPage, int pageSize, String orderby, boolean asc, KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("Information", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 获取符合条件的记录个数
	 * @param 变参arr：查询的条件（键-值-算符）
	 * @return 符合条件的记录个数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		try{
			return m_dao.getTotalCount("Information", arr);
		}catch(Exception e){
			return 0;
		}
	}
}
