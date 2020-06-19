package com.app.lzq.testbase;

/**
 * @ProjectName: TestBase
 * @Package: com.app.lzq.testbase
 * @ClassName: MobileAddress
 * @Description: java类作用描述
 * @Author: 刘智强
 * @CreateDate: 2019/1/18 10:45
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/18 10:45
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MobileAddress {

    private String error_code;
    private String reason;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "MobileAddress{" +
                "error_code='" + error_code + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
