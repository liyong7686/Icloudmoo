package com.icloudmoo.common.vo;

import java.util.List;

/**
  * @ClassName: SystemUserVo
  * @Description: TODO（用一句话描述这个类的用途）
  * @author 信息管理部-gengchong
  * @date 2015年8月20日 上午10:55:55
  *
  */
public class SystemUserVo extends ValueObject{

	//@Fields serialVersionUID : TODO（用一句话描述这个变量表示什么）
	  
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String password;
	private String validateCode;
	private String account;
	private String projectCode;
	private String dataRight;
	private boolean isAdmin;
	
	private List<Integer> funcRoleIds;
	private List<Integer> orgRoleIds;
	
	private String passwordNew; //新密码
	private String mobile; //手机号
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the validateCode
	 */
	public String getValidateCode() {
		return validateCode;
	}
	/**
	 * @param validateCode the validateCode to set
	 */
	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}
	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}
	/**
	 * @return the projectCode
	 */
	public String getProjectCode() {
		return projectCode;
	}
	/**
	 * @param projectCode the projectCode to set
	 */
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	/**
	 * @return the dataRight
	 */
	public String getDataRight() {
		return dataRight;
	}
	/**
	 * @param dataRight the dataRight to set
	 */
	public void setDataRight(String dataRight) {
		this.dataRight = dataRight;
	}
	/**
	 * @return the isAdmin
	 */
	public boolean isAdmin() {
		return isAdmin;
	}
	/**
	 * @param isAdmin the isAdmin to set
	 */
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	/**
	 * @return the funcRoleIds
	 */
	public List<Integer> getFuncRoleIds() {
		return funcRoleIds;
	}
	/**
	 * @param funcRoleIds the funcRoleIds to set
	 */
	public void setFuncRoleIds(List<Integer> funcRoleIds) {
		this.funcRoleIds = funcRoleIds;
	}
	/**
	 * @return the orgRoleIds
	 */
	public List<Integer> getOrgRoleIds() {
		return orgRoleIds;
	}
	/**
	 * @param orgRoleIds the orgRoleIds to set
	 */
	public void setOrgRoleIds(List<Integer> orgRoleIds) {
		this.orgRoleIds = orgRoleIds;
	}
    /**
     * @return the passwordNew
     */
    public String getPasswordNew() {
        return passwordNew;
    }
    /**
     * @param passwordNew the passwordNew to set
     */
    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }
    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }
    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
	
	

}
