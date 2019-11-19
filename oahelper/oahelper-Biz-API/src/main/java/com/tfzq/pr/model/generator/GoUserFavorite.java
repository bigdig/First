package com.tfzq.pr.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author yuzhitao 
 */
@SuppressWarnings("serial")
public class GoUserFavorite extends BaseModel{
    private String userId;
    private String userName;
    private String favoriteType;
    private String favoriteId;
    
    public void setUserId(String userId){
        this.userId = userId;
    }
    public String getUserId(){
        return this.userId;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getUserName(){
        return this.userName;
    }
    public void setFavoriteType(String favoriteType){
        this.favoriteType = favoriteType;
    }
    public String getFavoriteType(){
        return this.favoriteType;
    }
    public void setFavoriteId(String favoriteId){
        this.favoriteId = favoriteId;
    }
    public String getFavoriteId(){
        return this.favoriteId;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}