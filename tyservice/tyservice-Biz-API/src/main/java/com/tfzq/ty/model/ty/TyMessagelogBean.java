package com.tfzq.ty.model.ty;
import com.tfzq.ty.model.generator.TyMessageaudit;
import com.tfzq.ty.model.generator.TyMessagelog;

/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyMessagelogBean extends TyMessagelog{
    private TyMessageaudit audit;

	public TyMessageaudit getAudit() {
		return audit;
	}

	public void setAudit(TyMessageaudit audit) {
		this.audit = audit;
	}
    
    
}