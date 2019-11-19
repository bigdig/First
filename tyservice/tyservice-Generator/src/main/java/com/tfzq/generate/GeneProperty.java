package com.tfzq.generate;

import java.util.Properties;
/**
 * 
 * @author pengtao
 *
 */
public class GeneProperty extends Properties {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getModelGeneratePackageName() {
		return get("package").toString() + "." + get("module").toString()
				+ ".model.generator";
	}

	public String getModelExpandPackageName() {
		return get("package").toString() + "." + get("module").toString()
				+ ".model." + get("module");
	}

	public String getProviderApiPackageName() {
		return get("package").toString() + "." + get("module").toString()
				+ ".provider";
	}

	public String getProviderImplPackageName() {
		return get("package").toString() + ".provider";
	}

	public String getMapperGeneratePackageName() {
		return get("package").toString() + "." + get("module").toString()
				+ ".dao.generator";
	}

	public String getMapperExpandPackageName() {
		return get("package").toString() + "." + get("module").toString()
				+ ".dao." + get("module");
	}

	public String getServicePackageName() {
		return get("package").toString() + ".service";
	}

	public String getControllerPackageName() {
		return get("package").toString() + ".web";
	}

	public String getAuthor() {
		return get("author").toString() ;
	}
}
