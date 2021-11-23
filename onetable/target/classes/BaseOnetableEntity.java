package jp.tapf.vttktd.common.entity;

import java.util.Date;

/**
 * onetableの更新用排他カラムのクラス
 */
public class BaseOnetableEntity {
	
    /**
     * onetableの更新専用排他カラム：更新者ID
     */
	private String checkUpdateUserId;
	
    /**
     * onetableの更新専用排他カラム：更新日時
     */
    private Date checkUpdateDt;
    
    /**
     * onetableの更新専用：ロジック削除以外フラグ（true:ロジック削除以外 / false:ロジック削除も含めている）
     */
    private boolean checkUpdateExcludeDelete = true;   

    /**
     * onetableの更新専用排他カラム：更新者ID
     */
	public String getCheckUpdateUserId() {
		return checkUpdateUserId;
	}

    /**
     * onetableの更新専用排他カラム：更新者ID
     */
	public void setCheckUpdateUserId(String checkUpdateUserId) {
		this.checkUpdateUserId = checkUpdateUserId;
	}

    /**
     * onetableの更新専用排他カラム：update_dt
     */
	public Date getCheckUpdateDt() {
		return checkUpdateDt;
	}

    /**
     * onetableの更新専用排他カラム：update_dt
     */
	public void setCheckUpdateDt(Date checkUpdateDt) {
		this.checkUpdateDt = checkUpdateDt;
	}

    /**
     * onetableの更新専用：ロジック削除以外フラグ（true:ロジック削除以外 / false:ロジック削除も含めている）
     */
	public boolean isCheckUpdateExcludeDelete() {
		return checkUpdateExcludeDelete;
	}

    /**
     * onetableの更新専用：ロジック削除以外フラグ（true:ロジック削除以外 / false:ロジック削除も含めている）
     */
	public void setCheckUpdateExcludeDelete(boolean checkUpdateExcludeDelete) {
		this.checkUpdateExcludeDelete = checkUpdateExcludeDelete;
	}

}
