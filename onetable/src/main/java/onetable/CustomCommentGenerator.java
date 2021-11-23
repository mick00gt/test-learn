package onetable;

import java.util.Set;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.DefaultCommentGenerator;

public class CustomCommentGenerator extends DefaultCommentGenerator{

	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		field.addJavaDocLine("/**"); //$NON-NLS-1$

        StringBuilder sb = new StringBuilder();
        sb.append(" * "); //$NON-NLS-1$
        sb.append(introspectedColumn.getRemarks());
        
        field.addJavaDocLine(sb.toString());
        field.addJavaDocLine(" */"); //$NON-NLS-1$
	}

	@Override
	public void addGetterComment(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		method.addJavaDocLine("/**"); //$NON-NLS-1$

        StringBuilder sb = new StringBuilder();
        sb.append(" * "); //$NON-NLS-1$
        sb.append(introspectedColumn.getRemarks());
        
        method.addJavaDocLine(sb.toString());
        method.addJavaDocLine(" */"); //$NON-NLS-1$
	}

	@Override
	public void addSetterComment(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		method.addJavaDocLine("/**"); //$NON-NLS-1$

        StringBuilder sb = new StringBuilder();
        sb.append(" * "); //$NON-NLS-1$
        sb.append(introspectedColumn.getRemarks());
        
        method.addJavaDocLine(sb.toString());
        method.addJavaDocLine(" */"); //$NON-NLS-1$
	}

	@Override
	public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		StringBuilder sb = new StringBuilder();
        topLevelClass.addJavaDocLine("/**");
        sb.append(" * 「"+ introspectedTable.getRemarks() +"」　Entityクラス");
        topLevelClass.addJavaDocLine(sb.toString());
        topLevelClass.addJavaDocLine(" */"); //$NON-NLS-1$
	}

	/**
	 * myBatis設定クラス
	 * <p>
	 * myBatisで使用するDataSpurceなどを設定。
	 * <p>
	 * @author IIJ Kato-ya
	 */
	@Override
	public void addJavaFileComment(CompilationUnit compilationUnit) {
		compilationUnit.addFileCommentLine("/**");
		compilationUnit.addFileCommentLine(" * mybatis-generator 自動作成");
		compilationUnit.addFileCommentLine(" *");
		compilationUnit.addFileCommentLine(" * @author IIJ 官誠");
		compilationUnit.addFileCommentLine(" *");
		compilationUnit.addFileCommentLine(" */");
	}

	@Override
	public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
		method.addJavaDocLine("/**");
		method.addJavaDocLine("　*　" + introspectedTable.getRemarks() + " : " + method.getName());
		if ("insert".equals(method.getName())) {
			method.addJavaDocLine("　*　" + "新規:　カラム値がNULLの場合、NULLに新規する");
		} else if ("insertSelective".equals(method.getName())) {
			method.addJavaDocLine("　*　" + "新規:　カラム値がNULLの場合、新規項目から除外する");
		} else if ("selectByPrimaryKey".equals(method.getName())) {
			method.addJavaDocLine("　*　" + "単純なKEY検索");
		} else if ("selectByPrimaryKeyOnLock".equals(method.getName())) {
			method.addJavaDocLine("　*　" + "KEY検索:　レコードのロック取得、論理削除除外、楽観排他");
		} else if ("deleteByPrimaryKey".equals(method.getName())) {
			method.addJavaDocLine("　*　" + "削除:　論理削除除外、楽観排他");
		} else if ("updateByPrimaryKeySelective".equals(method.getName())) {
			method.addJavaDocLine("　*　" + "更新:　カラム値がNULLの場合、更新項目から除外する");
			method.addJavaDocLine("　*　" + "　　　　　論理削除除外: record.checkUpdateExcludeDelete（true:論理削除除外 false:論理削除も対象とする）");
			method.addJavaDocLine("　*　" + "　　　　　楽観排他　　　:　record.checkUpdateDt（NULL以外：楽観排他有効　NULL：楽観排他無効）");
			method.addJavaDocLine("　*　" + "　　　　　　　　　　　　　　:　record.checkUpdateUserId");
		} else if ("updateByPrimaryKey".equals(method.getName())) {
			method.addJavaDocLine("　*　" + "更新:　カラム値がNULLの場合、NULLに更新する");
			method.addJavaDocLine("　*　" + "　　　　　論理削除除外、楽観排他");
		}
		
		StringBuilder sb =null;
		for(Parameter p : method.getParameters()) {
			sb = new StringBuilder();
			sb.append(" * @param " + p.getName());
			if (CustomPlugin.PARAM_EXCLUDE_DELETE.equals(p.getName())) {
				sb.append(" true:論理削除除外 false（NULL）:論理削除も対象とする");
			} else if (CustomPlugin.PARAM_UPDATEDT.equals(p.getName())) {
				sb.append(" 楽観排他用更新日時　NULL以外：楽観排他有効　NULL：楽観排他無効");
			} else if (CustomPlugin.PARAM_UPDATEUSERID.equals(p.getName())) {
				sb.append(" 楽観排他用更新者ID");
			} else if (CustomPlugin.PARAM_GETLOCK.equals(p.getName())) {
				sb.append(" レコードのロック取得　true:ロック取得 false:ロック取得しない");
			}
			
			
			method.addJavaDocLine(sb.toString());
		}
		method.addJavaDocLine(" * @return " + method.getReturnType());
		method.addJavaDocLine(" */");
	}
	
	
}
