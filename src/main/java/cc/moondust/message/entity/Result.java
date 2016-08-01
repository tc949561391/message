package cc.moondust.message.entity;

/**
 * Created by j0 on 2016/8/1.
 */

public class Result {
    private String code;
    private String reasion;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReasion() {
        return reasion;
    }

    public void setReasion(String reasion) {
        this.reasion = reasion;
    }

    public Result(String code, String reasion) {
        this.code = code;
        this.reasion = reasion;
    }
}
