package onetable;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

public class CustomJavaMapperMethodGenerator extends AbstractJavaMapperMethodGenerator{

	@Override
	public void addInterfaceElements(Interface interfaze) {
		addSelectByPrimaryKeyOnLock(interfaze);
		addDeleteByPrimaryKey(interfaze);
		addUpdateByPrimaryKey(interfaze, true);
		addUpdateByPrimaryKey(interfaze, false);
	}
	
	private void addUpdateByPrimaryKey(Interface interfaze, boolean commonType) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        FullyQualifiedJavaType parameterType;

        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = new FullyQualifiedJavaType(introspectedTable
                    .getRecordWithBLOBsType());
        } else {
            parameterType = new FullyQualifiedJavaType(introspectedTable
                    .getBaseRecordType());
        }

        importedTypes.add(parameterType);

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        if(commonType) {
        	method.setName(introspectedTable
        			.getUpdateByPrimaryKeySelectiveStatementId());
        } else {
        	method.setName(introspectedTable
        			.getUpdateByPrimaryKeyStatementId());
        }
        method.addParameter(new Parameter(parameterType, "record")); //$NON-NLS-1$

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        addMapperAnnotations(method);
        
        if (context.getPlugins()
                .clientUpdateByPrimaryKeySelectiveMethodGenerated(method,
                        interfaze, introspectedTable)) {
            addExtraImports(interfaze);
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
	}

    private void addSelectByPrimaryKeyOnLock(Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        interfaze.addAnnotation("@Mapper");
        importedTypes.add(new FullyQualifiedJavaType(
                "org.apache.ibatis.annotations.Mapper")); //$NON-NLS-1$
        
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        method.setReturnType(returnType);
        method.setName(CustomPlugin.FUN_SELECT_PRIM);

        List<IntrospectedColumn> introspectedColumns = introspectedTable
                .getPrimaryKeyColumns();
        boolean annotate = introspectedColumns.size() >= 1;
        if (annotate) {
            importedTypes.add(new FullyQualifiedJavaType(
                    "org.apache.ibatis.annotations.Param")); //$NON-NLS-1$
        }
        StringBuilder sb = new StringBuilder();
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            FullyQualifiedJavaType type = introspectedColumn
                    .getFullyQualifiedJavaType();
            importedTypes.add(type);
            Parameter parameter = new Parameter(type, introspectedColumn
                    .getJavaProperty());
            if (annotate) {
                sb.setLength(0);
                sb.append("@Param(\""); //$NON-NLS-1$
                sb.append(introspectedColumn.getJavaProperty());
                sb.append("\")"); //$NON-NLS-1$
                parameter.addAnnotation(sb.toString());
            }
            method.addParameter(parameter);
        }
        // ?????????????????????getlock
        FullyQualifiedJavaType type = new FullyQualifiedJavaType("java.lang.Boolean");
        Parameter parameter = new Parameter(type, CustomPlugin.PARAM_GETLOCK);
        parameter.addAnnotation("@Param(\""+CustomPlugin.PARAM_GETLOCK+"\")");
        method.addParameter(parameter);
        
        // ???????????????????????????????????????
        for(IntrospectedColumn column : introspectedTable.getAllColumns())
        {
        	if("LOGIC_DELETE_FLG".equals(column.getActualColumnName().toUpperCase())) {
		        type = new FullyQualifiedJavaType("java.lang.Boolean");
		        parameter = new Parameter(type, CustomPlugin.PARAM_EXCLUDE_DELETE);
		        parameter.addAnnotation("@Param(\""+CustomPlugin.PARAM_EXCLUDE_DELETE+"\")");
		        method.addParameter(parameter);
		        break;
        	}
        }

        // ?????????????????????????????????
        type = new FullyQualifiedJavaType("java.lang.String");
        parameter = new Parameter(type, CustomPlugin.PARAM_UPDATEUSERID);
        parameter.addAnnotation("@Param(\""+ CustomPlugin.PARAM_UPDATEUSERID +"\")");
        method.addParameter(parameter);
        
        type = new FullyQualifiedJavaType("java.util.Date");
        parameter = new Parameter(type, CustomPlugin.PARAM_UPDATEDT);
        parameter.addAnnotation("@Param(\""+ CustomPlugin.PARAM_UPDATEDT +"\")");
        method.addParameter(parameter);
        
        addMapperAnnotations(interfaze, method);
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
        
    }
    
    private void addDeleteByPrimaryKey(Interface interfaze) {

        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName(introspectedTable.getDeleteByPrimaryKeyStatementId());


        // no primary key class - fields are in the base class
        // if more than one PK field, then we need to annotate the
        // parameters
        // for MyBatis
        List<IntrospectedColumn> introspectedColumns = introspectedTable
                .getPrimaryKeyColumns();
        boolean annotate = introspectedColumns.size() >= 1;
        if (annotate) {
            importedTypes.add(new FullyQualifiedJavaType(
                    "org.apache.ibatis.annotations.Param")); //$NON-NLS-1$
        }
        StringBuilder sb = new StringBuilder();
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            FullyQualifiedJavaType type = introspectedColumn
                    .getFullyQualifiedJavaType();
            importedTypes.add(type);
            Parameter parameter = new Parameter(type, introspectedColumn
                    .getJavaProperty());
            if (annotate) {
                sb.setLength(0);
                sb.append("@Param(\""); //$NON-NLS-1$
                sb.append(introspectedColumn.getJavaProperty());
                sb.append("\")"); //$NON-NLS-1$
                parameter.addAnnotation(sb.toString());
            }
            method.addParameter(parameter);
        }
        
        // ???????????????????????????????????????
        for(IntrospectedColumn column : introspectedTable.getAllColumns())
        {
        	if("LOGIC_DELETE_FLG".equals(column.getActualColumnName().toUpperCase())) {
		        FullyQualifiedJavaType type = new FullyQualifiedJavaType("java.lang.Boolean");
		        Parameter parameter = new Parameter(type, CustomPlugin.PARAM_EXCLUDE_DELETE);
		        parameter.addAnnotation("@Param(\""+CustomPlugin.PARAM_EXCLUDE_DELETE+"\")");
		        method.addParameter(parameter);
		        break;
        	}
        }

        // ?????????????????????????????????
        FullyQualifiedJavaType type = new FullyQualifiedJavaType("java.lang.String");
        Parameter parameter = new Parameter(type, CustomPlugin.PARAM_UPDATEUSERID);
        parameter.addAnnotation("@Param(\""+ CustomPlugin.PARAM_UPDATEUSERID +"\")");
        method.addParameter(parameter);
        
        type = new FullyQualifiedJavaType("java.util.Date");
        parameter = new Parameter(type, CustomPlugin.PARAM_UPDATEDT);
        parameter.addAnnotation("@Param(\""+ CustomPlugin.PARAM_UPDATEDT +"\")");
        method.addParameter(parameter);
        

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        addMapperAnnotations(method);
        
        if (context.getPlugins().clientDeleteByPrimaryKeyMethodGenerated(
                method, interfaze, introspectedTable)) {
            addExtraImports(interfaze);
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }

    }
    
    public void addMapperAnnotations(Interface interfaze, Method method) {
    }
    
    public void addMapperAnnotations(Method method) {
    }

    public void addExtraImports(Interface interfaze) {
    }
}
