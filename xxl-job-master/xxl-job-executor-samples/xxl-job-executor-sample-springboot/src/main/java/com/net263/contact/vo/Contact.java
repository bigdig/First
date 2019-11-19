package com.net263.contact.vo;

/**
 * Copyright (C), 2002-2018, 北京二六三企业通信有限公司
 * package : com.net263.contact.vo
 * FileName: Contact
 * Author:   xutao
 * Date:     2018/3/29 16:34
 * Description: 通讯录
 * History:
 * <author>          <time>          <version>          <desc>
 * xutao           修改时间           版本号              描述
 */

public class Contact {


    private String custsysid;


    private String custgrpid;


    private String name;


    private String email;


    private String telephone;
    private String telephone1;
    private String description;
    private String organization;
    

    public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCustsysid() {
        return custsysid;
    }

    public void setCustsysid(String custsysid) {
        this.custsysid = custsysid;
    }

    public String getCustgrpid() {
        return custgrpid;
    }

    public void setCustgrpid(String custgrpid) {
        this.custgrpid = custgrpid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

	public String getTelephone1() {
		return telephone1;
	}

	public void setTelephone1(String telephone1) {
		this.telephone1 = telephone1;
	}
    
}
