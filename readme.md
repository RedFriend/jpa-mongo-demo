# 说明



### 遍历需要统计的集合

程序有个job定时器会遍历SP_AJXX集合,创建用于统计的集合SP_STATISTICS

1. SP_AJXX中字段LARQ需要加上索引
2. 导入TB_DICT_AY案由代码转化表,TB_DICT_FOCUS_AY争议焦点案由转换表,在resource/dictionary目录下
3. 需要字典表:TB_DISPUTEFOCUS,TB_LAW_INDEX_NATION,TB_LAW_ITEM_NATION,TB_LAW_SUBITEM_NATION,这些是总部有的

**相关代码位置**

1. 2个页面入口

   类名:KnowledgeBaseController

   /knowledge/caseDimensionPage

   /knowledge/retrievalPage

2. 定期器类

   类名:StatisticsJob

   方法:generateStatisticsCollection

**配置相关**

1. mongo配置
2. mysql 数据库td配置
3. td.storePath.template:文书模板的位置