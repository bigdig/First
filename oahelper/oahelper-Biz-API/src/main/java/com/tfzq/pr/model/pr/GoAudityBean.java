package com.tfzq.pr.model.pr;
import com.tfzq.pr.model.generator.GoAudity;
import java.util.List;

/**
 *   @author yuzhitao 
 */
@SuppressWarnings("serial")
public class GoAudityBean extends GoAudity{
    private String audityStatusText;
    
    public void setAudityStatusText(String audityStatusText){
        this.audityStatusText = audityStatusText;
    }
    public String getAudityStatusText(){
        return this.audityStatusText;
    }
}