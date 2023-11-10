package store.code.extension.exmail.way1.contact;

import store.code.extension.exmail.way1.contact.bean.DepartmentCreateModel;
import store.code.extension.exmail.way1.contact.bean.DepartmentCreateResult;

/**
 * 管理部门。
 * @see <a href="https://exmail.qq.com/qy_mng_logic/doc#10008">管理部门</a>
 * @author Kahle
 */
public interface DepartmentService {

    DepartmentCreateResult create(DepartmentCreateModel model);

}
