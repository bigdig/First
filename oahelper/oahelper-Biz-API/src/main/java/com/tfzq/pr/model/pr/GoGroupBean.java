package com.tfzq.pr.model.pr;
import com.tfzq.pr.model.generator.GoGroup;
import java.util.List;

/**
 *   @author yuzhitao 
 */
@SuppressWarnings("serial")
public class GoGroupBean extends GoGroup{
    private String groupTypeText;
    
    public void setGroupTypeText(String groupTypeText){
        this.groupTypeText = groupTypeText;
    }
    public String getGroupTypeText(){
        return this.groupTypeText;
    }
}