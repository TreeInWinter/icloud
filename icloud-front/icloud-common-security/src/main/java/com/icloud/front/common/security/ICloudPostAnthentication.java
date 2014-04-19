package com.icloud.front.common.security;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Component;

@Component("icloudPostAnthentication")
public class ICloudPostAnthentication implements UserDetailsChecker {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(ICloudPostAnthentication.class);

	// @Resource(name = "pRoleService")
	// private RoleService roleService;
	//
	// @Resource(name = "pCustomerService")
	// private CustomerService customerService;
	//
	// @Resource(name = "pDepartmentService")
	// private DepartmentService departmentService;

	private List<String> topDepartments;

	@Override
	public void check(UserDetails toCheck) {
		// completeUserData(toCheck);
	}

	// private void completeUserData(UserDetails ud) {
	// TopsUserDetails tud = (TopsUserDetails) ud;
	// fillCustomerData(tud); // must execute before fillPermissionData
	// fillPermissionData(tud);
	// if (PlatformUtils.getPlatform() == Platform.OPERATOR) {
	// fillDepartmentInfo(tud);
	// }
	// }

	// private void fillDepartmentInfo(TopsUserDetails tud) {
	// if (topDepartments == null) {
	// initTopDepartments(tud.getCustomerKey());
	// }
	// if (TZUtil.isEmpty(tud.getUserData().getAllDepartmentsKey()) ||
	// TZUtil.isEmpty(topDepartments)) {
	// return;
	// }
	// for (String topDepart : topDepartments) {
	// for (String depart : tud.getUserData().getAllDepartmentsKey()) {
	// if (topDepart.equals(depart)) {
	// tud.setTopDepartmentKey(topDepart);
	// return;
	// }
	// }
	// }
	// }

	// private synchronized void initTopDepartments(String customerKey) {
	// if (topDepartments != null) {
	// return;
	// }
	// List<Department> departs =
	// departmentService.getTopDepartments(customerKey);
	// topDepartments = new ArrayList<>();
	// if (!TZUtil.isEmpty(departs)) {
	// for (Department dpt : departs) {
	// topDepartments.add(dpt.getKey());
	// }
	// }
	// }
	//
	// private void fillCustomerData(TopsUserDetails tud) {
	// Customer cus =
	// customerService.getCustomerByKey(tud.getUserData().getCustomerKey());
	// if (cus == null) {
	// throw new RuntimeException("customer missing");
	// }
	// if (cus.getKind() != CustomerKind.PROVIDER && cus.getStatus() ==
	// CustomerStatus.SUSPEND) {
	// throw new LockedException("customer suspend");
	// }
	// tud.setCustomerData(cus);
	// if (tud.getUserData().getKey().equals(cus.getTopUserKey())) {
	// tud.setPrincipal(true);
	// }
	// }
	//
	// private void fillPermissionData(TopsUserDetails tud) {
	// // Generating authorities information.
	// Set<String> perms = new HashSet<>();
	// Collection<GrantedAuthority> authorities = new HashSet<>();
	// if (TopsSecurityUtils.isGod(tud)) {
	// if (!TZUtil.isEmpty(TopsSecurityUtils.getAllPermissions())) {
	// for (Permission perm : TopsSecurityUtils.getAllPermissions()) {
	// perms.add(perm.getName());
	// authorities.add(new SimpleGrantedAuthority(perm.getName()));
	// }
	// }
	// } else if (!TZUtil.isEmpty(tud.getUserData().getRolesKey())) {
	// List<Role> roles =
	// roleService.getRolesByKeys(tud.getUserData().getRolesKey());
	//
	// // merge permissions
	// for (Role r : roles) {
	// perms.addAll(r.getPermissions());
	// }
	// List<String> userPerms = tud.getUserData().getAdditionalPermissions();
	// if (!TZUtil.isEmpty(userPerms)) {
	// perms.addAll(userPerms);
	// }
	// userPerms = tud.getUserData().getBereftPermissions();
	// if (!TZUtil.isEmpty(userPerms)) {
	// perms.removeAll(userPerms);
	// }
	//
	// for (String auth : perms) {
	// authorities.add(new SimpleGrantedAuthority(auth));
	// }
	// }
	// tud.setPermissions(perms);
	// tud.setAuthorities(authorities);
	// }

}
