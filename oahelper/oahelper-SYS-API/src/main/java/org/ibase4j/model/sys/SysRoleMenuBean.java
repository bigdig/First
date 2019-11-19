package org.ibase4j.model.sys;

import org.ibase4j.model.generator.SysRoleMenu;


@SuppressWarnings("serial")
public class SysRoleMenuBean extends SysRoleMenu {
	private String menuName;
    private String parentId;
	private String isSelected;

	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}

	
}
