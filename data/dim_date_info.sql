
select * from dim_date_info;

drop table dim_date_info;
CREATE TABLE `dim_date_info` (
   `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
   `date` varchar(10) NOT NULL COMMENT '日(yyyy-mm-dd)',
   `year` int NOT NULL COMMENT '年',
   `quarter` int NOT NULL COMMENT '第几季度',
   `quarter_cn` varchar(10) NOT NULL COMMENT '所属季度(中文)',
   `month` int NOT NULL COMMENT '第几月',
   `month_cn` varchar(10) NOT NULL COMMENT '所属月份(中文)',
   `month_en` varchar(10) NOT NULL COMMENT '所属月份(英文)',
   `week` int NOT NULL COMMENT '周几',
   `week_cn` varchar(10) NOT NULL COMMENT '星期几(中文)',
   `week_en` varchar(10) NOT NULL COMMENT '星期几(英文)',
   `week_of_year` int NOT NULL COMMENT '本年第几周',
   `day_of_month` int NOT NULL COMMENT '每月的第几天',
   `day_of_year` int NOT NULL COMMENT '本年第几天',
   PRIMARY KEY (`id`),
   KEY `index_dim_date_info_date` USING BTREE (`date`) COMMENT '日期'
) ENGINE=InnoDB COMMENT='时间维度表';

drop PROCEDURE procedure_dim_date_info;

-- 按年生成时间维度表
CREATE PROCEDURE procedure_dim_date_info( IN start_date VARCHAR (10))
BEGIN
DECLARE
i,date_count INT;
SET i = 0,
    start_date = concat(start_date, '-01-01'),
    date_count = datediff(date_add(date_format(start_date, '%Y-%m-%d'), INTERVAL 1 YEAR ),
        date_format(start_date, '%Y-%m-%d'));
DELETE
FROM
    dim_date_info where year = left(start_date, 4);
WHILE
i < date_count DO
		INSERT INTO dim_date_info SELECT
            REPLACE(start_date, '-', '') AS 'id',-- 日期主健
                DATE_FORMAT( STR_TO_DATE( start_date, '%Y-%m-%d %H:%i:%s' ), '%Y-%m-%d' ) AS 'date',
                DATE_FORMAT( STR_TO_DATE( start_date, '%Y-%m-%d %H:%i:%s' ), '%Y' ) AS 'year',
                QUARTER ( STR_TO_DATE( start_date, '%Y-%m-%d %H:%i:%s' ) ) AS 'quarter',
                CASE
                    QUARTER ( STR_TO_DATE( start_date, '%Y-%m-%d %H:%i:%s' ) )
                    WHEN 1 THEN '一季度'
                    WHEN 2 THEN '二季度'
                    WHEN 3 THEN '三季度'
                    WHEN 4 THEN '四季度'
                    END AS 'quarter_cn',
                DATE_FORMAT( STR_TO_DATE( start_date, '%Y-%m-%d %H:%i:%s' ), '%c' ) AS 'month',
                CASE DATE_FORMAT( STR_TO_DATE( start_date, '%Y-%m-%d %H:%i:%s' ), '%c' )
                    WHEN 1 THEN '一月'
                    WHEN 2 THEN '二月'
                    WHEN 3 THEN '三月'
                    WHEN 4 THEN '四月'
                    WHEN 5 THEN '五月'
                    WHEN 6 THEN '六月'
                    WHEN 7 THEN '七月'
                    WHEN 8 THEN '八月'
                    WHEN 9 THEN '九月'
                    WHEN 10 THEN '十月'
                    WHEN 11 THEN '十一月'
                    WHEN 12 THEN '十二月'
                    END                                                             AS 'month_cn',
                DATE_FORMAT( STR_TO_DATE( start_date, '%Y-%m-%d %H:%i:%s' ), '%b' ) AS 'month_en',
            IF(DATE_FORMAT(STR_TO_DATE(start_date, '%Y-%m-%d %H:%i:%s'), '%w') + 0 = 0, 7,
               DATE_FORMAT(STR_TO_DATE(start_date, '%Y-%m-%d %H:%i:%s'), '%w'))     AS 'week',
                CASE DATE_FORMAT( STR_TO_DATE( start_date, '%Y-%m-%d %H:%i:%s' ), '%w' )
                    WHEN 1 THEN '星期一'
                    WHEN 2 THEN '星期二'
                    WHEN 3 THEN '星期三'
                    WHEN 4 THEN '星期四'
                    WHEN 5 THEN '星期五'
                    WHEN 6 THEN '星期六'
                    WHEN 0 THEN '星期天'
                    END                                                             AS 'week_cn',
                DATE_FORMAT( STR_TO_DATE( start_date, '%Y-%m-%d %H:%i:%s' ), '%W' ) AS 'week_en',
                DATE_FORMAT( STR_TO_DATE( start_date, '%Y-%m-%d %H:%i:%s' ), '%u' ) AS 'week_of_year',
                DATE_FORMAT( STR_TO_DATE( start_date, '%Y-%m-%d %H:%i:%s' ), '%e' ) AS 'day_of_month',
                DAYOFYEAR( STR_TO_DATE( start_date, '%Y-%m-%d %H:%i:%s' ) ) AS 'day_of_year'
            FROM
                DUAL;
SET i = i + 1;
	SET start_date = DATE_FORMAT( date_add( STR_TO_DATE( start_date, '%Y-%m-%d %H:%i:%s' ), INTERVAL 1 DAY ), '%Y-%m-%d' );
END WHILE;
END;

-- 调用函数如下
call procedure_dim_date_info('2020');