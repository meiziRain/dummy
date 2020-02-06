package com.meizi.admin.constants;

/**
 * @program: dummy
 * @description:
 * @author: Meizi
 * @create: 2020-01-07 08:49
 **/

public interface AdminConstants {
    /**
     * 默认非超级管理员用户密码
     **/
    String DEF_USER_PASSWORD = "111111";

    /**
     * 默认超级管理员用户密码
     **/
    String DEF_ADMIN_PASSWORD = "gwi123456";

    /**
     * 超级管理员角色id
     */
    Long ADMIN_ROLE_ID = 1L;

    /**
     * 超级管理员的用户id
     */
    Long ADMIN_USER_ID = 1L;


    /**
     * 树的最高层数
     */
    Integer TREE_MAX_DEEP = 5;

    /**
     * pid的key值
     */
    String ADMIN_PID_KEY = "pid";

    /**
     * ID键值
     */
    String ADMIN_ID_KEY = "id";

    /**
     * 标签键值
     */
    String ADMIN_LABEL_KEY = "label";

    /**
     * 子节点键值
     */
    String ADMIN_CHILDREN_KEY = "children";

    /**
     * 默认上级ID
     */
    Long DEFAULT_PARENT_ID = 0L;
}
