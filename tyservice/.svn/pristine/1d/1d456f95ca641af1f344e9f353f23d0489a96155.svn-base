<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packageName}.${table.beanName}Mapper">
  <resultMap id="BaseResultMap" type="${beanGenPackage}.${table.beanName}">
    <#list table.fields as item> 
      <result column="${item.column}" jdbcType="${item.jdbcType}" property="${item.code}" />
    </#list>
  </resultMap>
  
  <resultMap id="ExResultMap" type="${beanCusPackage}.${table.beanName}Bean">
    <#list table.fields as item> 
      <result column="${item.column}" jdbcType="${item.jdbcType}" property="${item.code}" />
    </#list>
  </resultMap>

  <delete id="deleteByPrimaryKey" parameterType="java.lang.String">
  <#assign me = "#">
    delete from ${table.tableName} where ID = ${me}{id,jdbcType=VARCHAR}
  </delete>
  
  <insert id="insert" parameterType="${beanGenPackage}.${table.beanName}">
    insert into ${table.tableName} (
    <#list table.fields as item>
    <#if item_index==0>
    ${item.column}
	<#else>
	,${item.column}
    </#if>
    </#list>
    )values (
    <#list table.fields as item>
    <#if item_index==0>
    ${me}{${item.code},jdbcType=${item.jdbcType}}
	<#else>
	,${me}{${item.code},jdbcType=${item.jdbcType}}
    </#if>
    </#list>)
  </insert>
  <update id="updateByPrimaryKey" parameterType="${beanGenPackage}.${table.beanName}">
    update ${table.tableName}
    <trim prefix="set" suffixOverrides=",">
    <#list table.fields as item>
      <if test="${item.code} != null ">
        ${item.column} = ${me}{${item.code},jdbcType=${item.jdbcType}},
      </if>
    </#list>
    </trim>
    where ID = ${me}{id,jdbcType=VARCHAR}
  </update>
  <select id="selectByPrimaryKey" parameterType="java.lang.String" resultMap="BaseResultMap">
    select <#list table.fields as item><#if item_index==0>${item.column}<#else>,${item.column}</#if></#list>
    from ${table.tableName}
    where ID = ${me}{id,jdbcType=VARCHAR}
  </select>
  <select id="selectAll" resultMap="BaseResultMap">
    select <#list table.fields as item><#if item_index==0>${item.column}<#else>,${item.column}</#if></#list>
    from ${table.tableName}
    order by id
  </select>
  <select id="selectByCondition" resultMap="ExResultMap">
    select <#list table.fields as item><#if item_index==0>${item.column}<#else>,${item.column}</#if></#list>
    from ${table.tableName}
    where 1=1
	<#list table.searchFields as item>
	<#if item.dictFlag || item.jdbcType=='INTEGER'>
	<if test="${item.code} != null and ${item.code} != ''">and ${item.column}=${me}{${item.code}}</if>
	<#else>
	<if test="${item.code} != null and ${item.code} != ''">and ${item.column} like '%'||${me}{${item.code}}||'%'</if>
	</#if>
	</#list>
	<#if table.hasDelFlag>
	<if test="deleteFlag != null">and delete_flag=${me}{deleteFlag}</if>
	</#if>
      order by id
  </select>
  <#assign me = "#">
  <select id="queryIds" parameterType="java.util.Map" resultType="java.lang.String">
	select id from ${table.tableName} where 1=1
	<#list table.searchFields as item>
	<#if item.dictFlag || item.jdbcType=='INTEGER'>
	<if test="${item.code} != null and ${item.code} != ''">and ${item.column}=${me}{${item.code}}</if>
	<#else>
	<if test="${item.code} != null and ${item.code} != ''">and ${item.column} like '%'||${me}{${item.code}}||'%'</if>
	</#if>
	</#list>
	<#if table.hasDelFlag>
	<if test="deleteFlag != null">and delete_flag=${me}{deleteFlag}</if>
	</#if>
	<if test="keyword != null">
		and (<#list table.searchFields as item><#if item_index==0>${item.column} like '%'||${me}{keyword}||'%' <#else> or ${item.column} like '%'||${me}{keyword}||'%'</#if></#list>)
	</if>
  </select>
</mapper>