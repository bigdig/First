package ${packageName};
import ${parentPackageName}.${table.beanName};
import java.util.List;
<#if table.hasFileField>
import com.tfzq.pb.model.generator.PbFile;
</#if>

/**
 *   @author ${author} 
 */
@SuppressWarnings("serial")
public class ${table.beanName}Bean extends ${table.beanName}{
    <#list table.transFields as item>
    private String ${item.nameField};
    </#list>
    <#list table.filesFields as item>
    private List<PbFile> ${item.code}Bean;
    </#list>
    
    <#list table.transFields as item>
    public void set${item.nameField?cap_first}(${item.javaType} ${item.nameField}){
        this.${item.nameField} = ${item.nameField};
    }
    public ${item.javaType} get${item.nameField?cap_first}(){
        return this.${item.nameField};
    }
    </#list>
    <#list table.filesFields as item>
    public void set${item.code?cap_first}Bean(List<PbFile> ${item.code}Bean){
        this.${item.code}Bean = ${item.code}Bean;
    }
    public List<PbFile> get${item.code?cap_first}Bean(){
        return this.${item.code}Bean;
    }
    </#list>
}