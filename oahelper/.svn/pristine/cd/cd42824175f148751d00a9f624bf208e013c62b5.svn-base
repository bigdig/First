package org.ibase4j.core.shiro;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.ibase4j.core.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdHelper {
	private static Logger logger = LoggerFactory.getLogger(AdHelper.class);

	public static boolean check(String userName,String password) {

//		String userName = "sjzxtest";
//		String password = "TF!@#tfwh";
		// String password = "xxxxx";
		String host = PropertiesUtil.getString("ad.host");// "192.168.101.102";
		String port = PropertiesUtil.getString("ad.port");//"389";
		String domain = PropertiesUtil.getString("ad.domain");//"@tfzq.com";

		String url = new String("ldap://" + host + ":" + port);
		String user = userName.indexOf(domain) > 0 ? userName : userName + domain;

		Hashtable<String, String> env = new Hashtable<String, String>();
		DirContext ctx;
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, user);
		env.put(Context.SECURITY_CREDENTIALS, password);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, url);

		try {
			ctx = new InitialDirContext(env);
			ctx.close();
			logger.debug("验证成功！");
			return true;
		} catch (NamingException err) {
			err.printStackTrace();
			logger.debug("验证失败！");
			return false;
		}
	}
}