package com.icloud.user.business.manager;

import org.springframework.stereotype.Service;

import com.icloud.framework.util.ICloudUtils;
import com.icloud.framework.util.StringEncoder;
import com.icloud.stock.model.User;
import com.icloud.user.business.UserBusiness;

@Service("userAdminBusiness")
public class UserAdminBusiness extends UserBusiness {

	public User addUser(String userName, String password) {
		if (ICloudUtils.isNotNull(userName) && ICloudUtils.isNotNull(password)) {
			password = StringEncoder.encrypt(password);// 加密

		}
		return null;
	}

}
