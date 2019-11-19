package com.tfzq.ty.model.ty;
import java.util.List;

import com.tfzq.ty.model.generator.TyLabel;

/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyLabelBean extends TyLabel{
	private String preName;
	private String nextName;
	private String isSelected;
	private String isExists;
	private List<TyLabelBean> subList;

	public String getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}

	public List<TyLabelBean> getSubList() {
		return subList;
	}

	public void setSubList(List<TyLabelBean> subList) {
		this.subList = subList;
	}

	public String getIsExists() {
		return isExists;
	}

	public void setIsExists(String isExists) {
		this.isExists = isExists;
	}

	public String getPreName() {
		return preName;
	}

	public void setPreName(String preName) {
		this.preName = preName;
	}

	public String getNextName() {
		return nextName;
	}

	public void setNextName(String nextName) {
		this.nextName = nextName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TyLabelBean other = (TyLabelBean) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
	
	
	
}