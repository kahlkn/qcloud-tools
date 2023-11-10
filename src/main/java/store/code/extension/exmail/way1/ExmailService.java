package store.code.extension.exmail.way1;

import store.code.extension.exmail.way1.contact.*;

/**
 * 腾讯企业邮箱服务类。
 * @see <a href="https://exmail.qq.com/qy_mng_logic/doc#10001">接口文档</a>
 * @author Kahle
 */
public interface ExmailService {

    DepartmentService getDepartmentService();

    UserService getUserService();

    TagService getTagService();

    GroupService getGroupService();

    PublicMailService getPublicMailService();

    LaddrService getLaddrService();

}
