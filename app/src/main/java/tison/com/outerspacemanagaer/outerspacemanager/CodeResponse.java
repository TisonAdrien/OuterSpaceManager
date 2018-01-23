package tison.com.outerspacemanagaer.outerspacemanager;

/**
 * Created by atison on 23/01/2018.
 */

public class CodeResponse {
    private String code;
    private String internalCode;
    private String attackTime;

    public void setAttackTime(String attackTime) {
        this.attackTime = attackTime;
    }

    public String getAttackTime() {
        return attackTime;
    }

    public void setInternalCode(String internalCode) {
        this.internalCode = internalCode;
    }

    public String getInternalCode() {
        return internalCode;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
