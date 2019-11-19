package ${packageName};
import org.ibase4j.core.base.BaseModel;
<#if (table.hasDateField==true)>
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
</#if>
/**
 *   @author ${author} 
 */
@SuppressWarnings("serial")
public class ${table.beanName} extends BaseModel{
    <#list table.fields as item>
    <#if (item.code=="id" || item.code=="enable"|| item.code=="remark"|| item.code=="createBy"|| item.code=="createTime"|| item.code=="updateBy"|| item.code=="updateTime"|| item.code=="deleteFlag")>
    <#else>
    <#if (item.jdbcType=="DATE")>
    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    @JSONField (format="yyyy-MM-dd")
    <#elseif (item.jdbcType=="TIMESTAMP")>
    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    </#if>
    private ${item.javaType} ${item.code};
    </#if>
    </#list>
    
    <#list table.fields as item>
    <#if (item.code=="id" || item.code=="enable"|| item.code=="remark"|| item.code=="createBy"|| item.code=="createTime"|| item.code=="updateBy"|| item.code=="updateTime"|| item.code=="deleteFlag")>
    <#else>
    public void set${item.code?cap_first}(${item.javaType} ${item.code}){
        this.${item.code} = ${item.code};
    }
    public ${item.javaType} get${item.code?cap_first}(){
        return this.${item.code};
    }
    </#if>
    </#list>
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}