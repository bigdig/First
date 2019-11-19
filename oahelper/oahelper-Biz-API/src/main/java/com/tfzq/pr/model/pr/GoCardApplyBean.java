package com.tfzq.pr.model.pr;
import com.tfzq.pr.model.generator.GoCardApply;
import java.util.List;

/**
 *   @author yuzhitao 
 */
@SuppressWarnings("serial")
public class GoCardApplyBean extends GoCardApply{
    private String cardStatusText;
    
    public void setCardStatusText(String cardStatusText){
        this.cardStatusText = cardStatusText;
    }
    public String getCardStatusText(){
        return this.cardStatusText;
    }
}