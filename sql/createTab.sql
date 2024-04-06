-- 建库
create database  if not exists EasyBuy;
use EasyBuy;


-- 建表

CREATE TABLE `tb_user`
(
    `id`       int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
    `username` varchar(32) NOT NULL DEFAULT '' COMMENT '用户名',
    `password` varchar(32) NOT NULL DEFAULT '' COMMENT '密码',
    `role`     tinyint NOT NULL DEFAULT 0 COMMENT '角色',
    `deleted`  tinyint     NOT NULL DEFAULT 0 comment '软删标识，0：未删除，1：已删除',
    `ctime`    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `mtime`    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    INDEX      `ix_mtime` (`mtime`)
) ENGINE = InnoDB COMMENT ='用户表';


CREATE TABLE `tb_item`
(
    `id`          int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
    `title`       varchar(64)    NOT NULL DEFAULT '' COMMENT '商品标题',
    `description` varchar(255)   NOT NULL DEFAULT '' COMMENT '商品描述',
    `seller_id`   int            NOT NULL DEFAULT 0 COMMENT '卖家id',
    `category_id` int            not null default 0 comment '商品所属分类的id',
    `price`       decimal(12, 2) not null default 0.00 comment '定价',
    `stock`       int            not null default 0 comment '商品库存',
    `deleted`     tinyint        NOT NULL DEFAULT 0 comment '软删标识，0：未删除，1：已删除',
    `ctime`       DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `mtime`       DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    INDEX         `ix_mtime` (`mtime`)
) ENGINE = InnoDB COMMENT ='商品表';

-- 一款商品会有多张描述图片，因此分出商品图片表
Create TABLE `tb_item_image`
(
    `id`      int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
    `item_id` int          NOT NULL DEFAULT 0 COMMENT '对应的商品id',
    `url`     varchar(255) NOT NULL DEFAULT '' COMMENT '图片url',
    `deleted` tinyint      NOT NULL DEFAULT 0 comment '软删标识，0：未删除，1：已删除',
    `ctime`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `mtime`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    INDEX     `ix_mtime` (`mtime`)
) ENGINE = InnoDB COMMENT ='商品图片表';


create Table `tb_category`
(
    `id`            int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
    `category_name` varchar(32) NOT NULL DEFAULT '' COMMENT '类型名',
    `parent_id`     int         not null default 0 comment '父分类名，为0表示为顶级分类',
    `deleted`       tinyint     NOT NULL DEFAULT 0 comment '软删标识，0：未删除，1：已删除',
    `ctime`         DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `mtime`         DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    INDEX           `ix_mtime` (`mtime`)
) ENGINE = InnoDB COMMENT ='商品分类表';

create table `tb_address`
(
    `id`           int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
    `user_id`      int          not null default 0 comment '该地址对应的用户账号id',
    `addr_desc`    varchar(255) not null default '' comment '地址描述',
    `addr_username` varchar (32) NOT NULL DEFAULT '' comment '地址对应的姓名',
    `addr_phone`   varchar(32)  NOT NULL DEFAULT '' COMMENT '手机号',
    `default_flag` tinyint      not null default 0 comment '标识是否为默认地址，0：非默认，1：默认',
    `deleted`      tinyint      NOT NULL DEFAULT 0 comment '软删标识，0：未删除，1：已删除',
    `ctime`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `mtime`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    INDEX          `ix_mtime` (`mtime`)
) ENGINE = InnoDB COMMENT ='地址表';


create table `tb_seller`
(
    `id`      int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
    `user_id` int          not null default 0 comment '该卖家对应的用户账号id',
    `name`    varchar(255) not null default '' comment '店铺名称',
    `address` varchar(255) NOT NULL DEFAULT '' comment '店铺地址',
    `deleted` tinyint      NOT NULL DEFAULT 0 comment '软删标识，0：未删除，1：已删除',
    `ctime`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `mtime`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    INDEX     `ix_mtime` (`mtime`)
) ENGINE = InnoDB COMMENT ='店铺表';


create table `tb_order`
(
    `id`           int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
    `user_id`      int            not null default 0 comment '下单用户id',
    `status`       tinyint        not null default 0 comment '状态',
    `address_id`   int            NOT NULL DEFAULT 0 comment '地址id',
    `total_amount` decimal(12, 2) not null default 0.00 comment '订单总额',
    `deleted`      tinyint        NOT NULL DEFAULT 0 comment '软删标识，0：未删除，1：已删除',
    `ctime`        DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `mtime`        DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    INDEX          `ix_mtime` (`mtime`)
) ENGINE = InnoDB COMMENT ='订单表';


create table `tb_order_detail`
(
    `id`         int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
    `order_id`   int            not null default 0 comment '对应的订单id',
    `item_id`    int            not null default 0 comment '商品id',
    `quantity`   int            NOT NULL DEFAULT 0 comment '商品数量',
    `unit_price` decimal(12, 2) not null default 0.00 comment '购买该商品的单价',
    `subtotal`   decimal(12, 2) not null default 0.00 comment '购买该商品的小计总额',
    `deleted`    tinyint        NOT NULL DEFAULT 0 comment '软删标识，0：未删除，1：已删除',
    `ctime`      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `mtime`      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    INDEX        `ix_mtime` (`mtime`)
) ENGINE = InnoDB COMMENT ='订单明细表';

