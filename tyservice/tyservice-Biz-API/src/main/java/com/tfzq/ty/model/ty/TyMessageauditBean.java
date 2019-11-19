package com.tfzq.ty.model.ty;
import com.tfzq.ty.model.generator.TyMessageaudit;
import java.util.List;

/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyMessageauditBean extends TyMessageaudit{
    private List<TyMessagelogBean> tyMessagelogList;
    private String createName;
    private List<String> receiverNames;

	public List<TyMessagelogBean> getTyMessagelogList() {
		return tyMessagelogList;
	}

	public void setTyMessagelogList(List<TyMessagelogBean> tyMessagelogList) {
		this.tyMessagelogList = tyMessagelogList;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public List<String> getReceiverNames() {
		return receiverNames;
	}

	public void setReceiverNames(List<String> receiverNames) {
		this.receiverNames = receiverNames;
	}
    
}