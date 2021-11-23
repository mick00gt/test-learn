package onetable.impl;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

public class ImplCustomPlugin extends PluginAdapter {
	
	public static String FUN_SELECT_PRIM = "selectByPrimaryKeyOnLock";
	public static String PARAM_GETLOCK = "getLock";
	public static String PARAM_EXCLUDE_DELETE = "excludeDelete";
	public static String PARAM_UPDATEUSERID = "updateUserId";
	public static String PARAM_UPDATEDT = "updateDt";
	public static String PARAM_CHECK_UPDATEDT = "checkUpdateDt";
	public static String PARAM_CHECK_UPDATEUSERID = "checkUpdateUserId";
	public static String PARAM_CHECK_EXCLUDEDELETE = "checkUpdateExcludeDelete";

	public boolean validate(List<String> warnings) {
		return true;
	}
	 
    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        AbstractJavaMapperMethodGenerator methodGenerator = new CustomImplJavaMapperMethodGenerator();
        methodGenerator.setContext(context);
        methodGenerator.setIntrospectedTable(introspectedTable);
        methodGenerator.addInterfaceElements(interfaze);
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }
}
