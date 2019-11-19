package com.tfzq.pr.model.pr;
import com.tfzq.pr.model.generator.GoVote;
import java.util.List;

/**
 *   @author yuzhitao 
 */
@SuppressWarnings("serial")
public class GoVoteBean extends GoVote{
    private String isCustomizableText;
    private String isMultiText;
    
    public void setIsCustomizableText(String isCustomizableText){
        this.isCustomizableText = isCustomizableText;
    }
    public String getIsCustomizableText(){
        return this.isCustomizableText;
    }
    public void setIsMultiText(String isMultiText){
        this.isMultiText = isMultiText;
    }
    public String getIsMultiText(){
        return this.isMultiText;
    }
}