package com.gitee.sop.websiteserver.mapper;

import com.gitee.fastmybatis.core.mapper.CrudMapper;
import com.gitee.sop.websiteserver.bean.IsvDetailDTO;
import com.gitee.sop.websiteserver.bean.LoginUser;
import com.gitee.sop.websiteserver.entity.IsvInfo;
import com.gitee.sop.websiteserver.entity.IsvPortal;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @author tanghc
 */
public interface IsvInfoMapper extends CrudMapper<IsvInfo, Long> {
    /**
     * 获取isv详细信息
     * @param username username
     * @param password password
     * @return 返回登录信息，没有返回null
     */
    @Select("SELECT  " +
            "  t.id userId " +
            "  ,t2.app_key appKey " +
            "  ,t3.private_key_isv privateKeyIsv " +
            "FROM user_account t " +
            "LEFT JOIN isv_info t2 ON t.id = t2.user_id " +
            "LEFT JOIN isv_keys t3 ON t2.app_key = t3.app_key " +
            "WHERE t.username = #{username} AND t.password=#{password} ")
    LoginUser getLoginUser(@Param("username") String username, @Param("password")String password);


    @Select("SELECT  " +
            "  t.id userId " +
            "  ,t2.app_key appId " +
            "  ,t3.public_key_isv publicKeyIsv " +
            "FROM user_account t " +
            "LEFT JOIN isv_info t2 ON t.id = t2.user_id " +
            "LEFT JOIN isv_keys t3 ON t2.app_key = t3.app_key " +
            "WHERE t.id = #{userId}  ")
    IsvPortal getIsvPortal(@Param("userId") long userId);

    /**
     * 获取isv详细信息
     * @param appKey appKey
     * @return 返回详细信息，没有返回null
     */
    @Select("SELECT  " +
            "  t.app_key appKey " +
            "  ,t.status " +
            "  ,t2.sign_type signType " +
            "  ,t2.secret " +
            "  ,t2.public_key_isv publicKeyIsv " +
            "  ,t2.private_key_platform privateKeyPlatform " +
            "FROM isv_info t " +
            "INNER JOIN isv_keys t2 ON t.app_key = t2.app_key " +
            "WHERE t.app_key = #{appKey}")
    IsvDetailDTO getIsvDetail(@Param("appKey") String appKey);
}
