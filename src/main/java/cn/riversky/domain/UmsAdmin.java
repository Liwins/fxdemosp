package cn.riversky.domain;

import java.time.LocalDate;
import java.util.Date;
import lombok.Data;

import javax.persistence.*;

/**
    * 后台用户表
    */
@Entity
@Table(name="UmsAdmin")
@Data
public class UmsAdmin {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String username;

    private String password;

    /**
    * 头像
    */
    private String icon;

    /**
    * 邮箱
    */
    private String email;

    /**
    * 昵称
    */
    private String nickName;

    /**
    * 备注信息
    */
    private String note;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 最后登录时间
    */
    private LocalDate loginTime;

    /**
    * 帐号启用状态：0->禁用；1->启用
    */
    private Integer status;
}