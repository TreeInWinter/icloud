package com.travelzen.framework.dao.util;

import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.travelzen.framework.dao.mongo.entity.CommonEntity;

@SuppressWarnings("unchecked")
public class SecurityContextUtil<T extends CommonEntity> {
	public T getSessionUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null)
			return null;
		Object principal = authentication.getPrincipal();
		if (principal != null)
			return (T) principal;
		return null;
	}

	public String getSessionUserName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication == null ? null : authentication.getName();
	}

	public ObjectId getSessionUserId() {
		T user = getSessionUser();
		return user == null ? null : user.getId();
	}
}
