package org.ibase4j.core.shiro;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class AdTest {

	public static void main(String[] args) {

		String userName = "wangduande";
		String password = "wanhuxiao2@";
		// String password = "xxxxx";
		String host = "172.19.19.38";
		String port = "389";
		String domain = "@tfzq.com";

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
			System.out.println("成功");
		} catch (NamingException err) {
			err.printStackTrace();
			System.out.println("失败");
		}
	}
}