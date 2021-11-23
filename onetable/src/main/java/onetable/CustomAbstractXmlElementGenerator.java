package onetable;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.internal.DefaultCommentGenerator;

public class CustomAbstractXmlElementGenerator extends AbstractXmlElementGenerator {

	@Override
	public void addElements(XmlElement parentElement) {
		addSelectByPrimaryKeyOnLock(parentElement);
		addDeleteByPrimaryKey(parentElement);
		addUpdateByPrimaryKey(parentElement, true);
		addUpdateByPrimaryKey(parentElement, false);
	}
	
	private void addUpdateByPrimaryKey(XmlElement parentElement, boolean commonType) {
        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

        if(commonType) {
	        answer.addAttribute(new Attribute(
	                "id", introspectedTable.getUpdateByPrimaryKeySelectiveStatementId())); //$NON-NLS-1$
        } else {
        	answer.addAttribute(new Attribute(
	                "id", introspectedTable.getUpdateByPrimaryKeyStatementId())); //$NON-NLS-1$
        }
        String parameterType;

        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = introspectedTable.getRecordWithBLOBsType();
        } else {
            parameterType = introspectedTable.getBaseRecordType();
        }

        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                parameterType));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();

        sb.append("update "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement dynamicElement = new XmlElement("set"); //$NON-NLS-1$
        answer.addElement(dynamicElement);

        XmlElement xmlElement = dynamicElement;
        for (IntrospectedColumn introspectedColumn : ListUtilities.removeGeneratedAlwaysColumns(introspectedTable
                .getNonPrimaryKeyColumns())) {
        	if(commonType) {
	            sb.setLength(0);
	            sb.append(introspectedColumn.getJavaProperty());
	            sb.append(" != null"); //$NON-NLS-1$
	            xmlElement = new XmlElement("if"); //$NON-NLS-1$
	            xmlElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
	            dynamicElement.addElement(xmlElement);
        	}
            sb.setLength(0);
            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));
            sb.append(',');

            xmlElement.addElement(new TextElement(sb.toString()));
        }

        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  and "); //$NON-NLS-1$
            } else {
                sb.append("where "); //$NON-NLS-1$
                and = true;
            }

            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }
        
        // カストマイズ：排他条件
        XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
        isNotNullElement.addAttribute(new Attribute("test", CustomPlugin.PARAM_CHECK_UPDATEDT + " != null")); //$NON-NLS-1$
        isNotNullElement.addElement(new TextElement("and update_dt = #{"+ CustomPlugin.PARAM_CHECK_UPDATEDT +",jdbcType=TIMESTAMP}"));
        isNotNullElement.addElement(new TextElement("and update_user_id = #{"+ CustomPlugin.PARAM_CHECK_UPDATEUSERID +",jdbcType=VARCHAR}"));
        answer.addElement(isNotNullElement);
        
        // カストマイズ：論理削除条件
        for(IntrospectedColumn column : introspectedTable.getAllColumns())
        {
        	if("LOGIC_DELETE_FLG".equals(column.getActualColumnName().toUpperCase())) {
		        isNotNullElement = new XmlElement("if");
		        isNotNullElement.addAttribute(new Attribute("test", CustomPlugin.PARAM_CHECK_EXCLUDEDELETE)); //$NON-NLS-1$
		        isNotNullElement.addElement(new TextElement("and LOGIC_DELETE_FLG = '0'"));
		        answer.addElement(isNotNullElement);
		        break;
        	}
        }
        
        if (context.getPlugins()
                .sqlMapUpdateByPrimaryKeySelectiveElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
	
	
	private void addSelectByPrimaryKeyOnLock(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

        answer.addAttribute(new Attribute(
                "id", CustomPlugin.FUN_SELECT_PRIM)); //$NON-NLS-1$
        if (introspectedTable.getRules().generateResultMapWithBLOBs()) {
            answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
                    introspectedTable.getResultMapWithBLOBsId()));
        } else {
            answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
                    introspectedTable.getBaseResultMapId()));
        }

        String parameterType;
        parameterType = "map"; //$NON-NLS-1$

        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                parameterType));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("select "); //$NON-NLS-1$

        if (stringHasValue(introspectedTable
                .getSelectByPrimaryKeyQueryId())) {
            sb.append('\'');
            sb.append(introspectedTable.getSelectByPrimaryKeyQueryId());
            sb.append("' as QUERYID,"); //$NON-NLS-1$
        }
        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(getBaseColumnListElement());
        if (introspectedTable.hasBLOBColumns()) {
            answer.addElement(new TextElement(",")); //$NON-NLS-1$
            answer.addElement(getBlobColumnListElement());
        }

        sb.setLength(0);
        sb.append("from "); //$NON-NLS-1$
        sb.append(introspectedTable
                .getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  and "); //$NON-NLS-1$
            } else {
                sb.append("where "); //$NON-NLS-1$
                and = true;
            }

            sb.append(MyBatis3FormattingUtilities
                    .getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }
        
        // カストマイズ：排他条件
        XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
        isNotNullElement.addAttribute(new Attribute("test", CustomPlugin.PARAM_UPDATEDT + " != null")); //$NON-NLS-1$
        isNotNullElement.addElement(new TextElement("and update_dt = #{"+ CustomPlugin.PARAM_UPDATEDT +",jdbcType=TIMESTAMP}"));
        isNotNullElement.addElement(new TextElement("and update_user_id = #{"+ CustomPlugin.PARAM_UPDATEUSERID +",jdbcType=VARCHAR}"));
        answer.addElement(isNotNullElement);
        
        // カストマイズ：論理削除条件
        for(IntrospectedColumn column : introspectedTable.getAllColumns())
        {
        	if("LOGIC_DELETE_FLG".equals(column.getActualColumnName().toUpperCase())) {
		        isNotNullElement = new XmlElement("if");
		        isNotNullElement.addAttribute(new Attribute("test", CustomPlugin.PARAM_EXCLUDE_DELETE)); //$NON-NLS-1$
		        isNotNullElement.addElement(new TextElement("and LOGIC_DELETE_FLG = '0'"));
		        answer.addElement(isNotNullElement);
		        break;
        	}
        }
        
        // カストマイズ：getlock
        isNotNullElement = new XmlElement("if");
        isNotNullElement.addAttribute(new Attribute("test", CustomPlugin.PARAM_GETLOCK)); //$NON-NLS-1$
        isNotNullElement.addElement(new TextElement("FOR UPDATE NOWAIT"));
        answer.addElement(isNotNullElement);
   

        if (context.getPlugins()
                .sqlMapSelectByPrimaryKeyElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
        }
	}
	
	private void addDeleteByPrimaryKey(XmlElement parentElement) {

        XmlElement answer = new XmlElement("delete"); //$NON-NLS-1$

        answer.addAttribute(new Attribute(
                "id", introspectedTable.getDeleteByPrimaryKeyStatementId())); //$NON-NLS-1$
        String parameterClass;

        // PK fields are in the base class. If more than on PK
        // field, then they are coming in a map.
        parameterClass = "map"; //$NON-NLS-1$
        
        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                parameterClass));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("delete from "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  and "); //$NON-NLS-1$
            } else {
                sb.append("where "); //$NON-NLS-1$
                and = true;
            }

            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }
        
        // カストマイズ：排他条件
        XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
        isNotNullElement.addAttribute(new Attribute("test", CustomPlugin.PARAM_UPDATEDT + " != null")); //$NON-NLS-1$
        isNotNullElement.addElement(new TextElement("and update_dt = #{"+ CustomPlugin.PARAM_UPDATEDT +",jdbcType=TIMESTAMP}"));
        isNotNullElement.addElement(new TextElement("and update_user_id = #{"+ CustomPlugin.PARAM_UPDATEUSERID +",jdbcType=VARCHAR}"));
        answer.addElement(isNotNullElement);
        
        // カストマイズ：論理削除条件
        for(IntrospectedColumn column : introspectedTable.getAllColumns())
        {
        	if("LOGIC_DELETE_FLG".equals(column.getActualColumnName().toUpperCase())) {
		        isNotNullElement = new XmlElement("if");
		        isNotNullElement.addAttribute(new Attribute("test", CustomPlugin.PARAM_EXCLUDE_DELETE)); //$NON-NLS-1$
		        isNotNullElement.addElement(new TextElement("and LOGIC_DELETE_FLG = '0'"));
		        answer.addElement(isNotNullElement);
		        break;
        	}
        }

        if (context.getPlugins()
                .sqlMapDeleteByPrimaryKeyElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
        }
    
		
	}
}
