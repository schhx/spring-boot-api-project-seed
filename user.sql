CREATE SCHEMA IF NOT EXISTS `spring_boot_api_project_seed` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `spring_boot_api_project_seed`.`user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` VARCHAR(128) NOT NULL COMMENT '用户名',
  `age` INT(10) unsigned NOT NULL COMMENT '年龄',
  PRIMARY KEY (`id`)  COMMENT ''
  )
ENGINE = InnoDB
COMMENT = '用户表';