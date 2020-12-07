package com.gitee.sop.adminserver;

import junit.framework.TestCase;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author tanghc
 */
public class AccountTest extends TestCase {

    /*
    生成密码
     */
    public void testGen() {
        String username = "admin";
        String password = "123456";
        String save_to_db = DigestUtils.md5Hex(username + DigestUtils.md5Hex(password) + username);
        System.out.println(save_to_db);
    }

}
