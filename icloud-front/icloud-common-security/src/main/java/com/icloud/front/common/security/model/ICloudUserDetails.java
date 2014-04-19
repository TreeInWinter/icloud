package com.icloud.front.common.security.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class ICloudUserDetails implements UserDetails {

	private static final long serialVersionUID = -8325343495164224189L;

	private String password;
	private String username;

	// private boolean accountNonExpired;
	// private boolean accountNonLocked;
	// private boolean credentialsNonExpired;
	// private boolean enabled;
	//
	// /** 是否是主账号 */
	// private boolean isPrincipal;
	// private int level;
	//
	// private Collection<GrantedAuthority> authorities;
	// private Collection<String> permissions;
	//
	// private String customerKey;
	// private User userData;
	// private Customer customerData;
	// private String topDepartmentKey;

	public ICloudUserDetails() {
		// this.userData = usr;
		// this.password = usr.getPassword();
		// this.username = usr.getUsername();
		// accountNonLocked = usr.getStatus() != UserStatus.SUSPEND;
		// credentialsNonExpired = true;
		// enabled = true;
		// DateTime deadline = usr.getDeadline();
		// if (deadline == null) {
		// accountNonExpired = true;
		// } else {
		// accountNonExpired = deadline.withMillisOfDay(0).getMillis() >= System
		// .currentTimeMillis();
		// }
		// customerKey = usr.getCustomerKey();
		// initLevel();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	// private void initLevel() {
	// PredefinedUserLevel userLevel = PredefinedUserLevel.NORMAL_EMPLOYEE;
	// if (this.userData.getOperatorUserType() != null) {
	// switch (this.userData.getOperatorUserType()) {
	// case MANAGER:
	// userLevel = PredefinedUserLevel.GENERAL_MANAGER;
	// break;
	//
	// case CHIEF:
	// userLevel = PredefinedUserLevel.DEPARTMENT_LEADER;
	// break;
	//
	// case ADMINISTRATOR:
	// userLevel = PredefinedUserLevel.GROUP_LEADER;
	// break;
	//
	// default:
	// userLevel = PredefinedUserLevel.NORMAL_EMPLOYEE;
	// break;
	// }
	// }
	// this.level = userLevel.level();
	// }

	// @Override
	// public Collection<GrantedAuthority> getAuthorities() {
	// return this.authorities;
	// }
	//
	// public void setAuthorities(Collection<GrantedAuthority> auths) {
	// this.authorities = auths;
	// }
	//
	// @Override
	// public String getPassword() {
	// return this.password;
	// }
	//
	// @Override
	// public String getUsername() {
	// return this.username;
	// }

	// @Override
	// public boolean isAccountNonExpired() {
	// return this.accountNonExpired;
	// }
	//
	// @Override
	// public boolean isAccountNonLocked() {
	// return this.accountNonLocked;
	// }
	//
	// @Override
	// public boolean isCredentialsNonExpired() {
	// return this.credentialsNonExpired;
	// }
	//
	// @Override
	// public boolean isEnabled() {
	// return this.enabled;
	// }
	//
	// public String getCustomerKey() {
	// return customerKey;
	// }
	//
	// public boolean isPrincipal() {
	// return isPrincipal;
	// }
	//
	// public void setPrincipal(boolean isPrincipal) {
	// this.isPrincipal = isPrincipal;
	// }
	//
	// public int getLevel() {
	// return this.level;
	// }
	//
	// public String getTopDepartmentKey() {
	// return topDepartmentKey;
	// }
	//
	// public void setTopDepartmentKey(String topDepartmentKey) {
	// this.topDepartmentKey = topDepartmentKey;
	// }

}
