package org.ibase4j.core.utils;

import org.ibase4j.model.generator.SysUser;

public class UserUtils {

	public static boolean isAdmin(SysUser user) {
		if (user.getAccount().equals("admin")) {
			return true;
		} else {
			return false;
		}
	}
}
