package com.ruoyi.system.repository.table;

import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;
import com.ruoyi.system.entity.SysConfig;

import java.sql.Types;
import java.time.LocalDateTime;

public class SysConfigTable extends com.querydsl.sql.RelationalPathBase<SysConfig> {

    public final NumberPath<Long> configId = createNumber("configId", Long.class);

    public final StringPath configKey = createString("configKey");

    public final StringPath configName = createString("configName");

    public final StringPath configType = createString("configType");

    public final StringPath configValue = createString("configValue");

    public final StringPath remark = createString("remark");

    public final StringPath createBy = createString("createBy");

    public final DateTimePath<LocalDateTime> createTime = createDateTime("createTime", LocalDateTime.class);

    public final StringPath updateBy = createString("updateBy");

    public final DateTimePath<LocalDateTime> updateTime = createDateTime("updateTime", LocalDateTime.class);

    public SysConfigTable(String variable) {
        super(SysConfig.class, variable, null, "sys_config");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(configId, ColumnMetadata.named("config_id").ofType(Types.BIGINT));
        addMetadata(configName, ColumnMetadata.named("config_name").ofType(Types.VARCHAR));
        addMetadata(configKey, ColumnMetadata.named("config_key").ofType(Types.VARCHAR));
        addMetadata(configValue, ColumnMetadata.named("config_value").ofType(Types.VARCHAR));
        addMetadata(configType, ColumnMetadata.named("config_type").ofType(Types.CHAR));
        addMetadata(createBy, ColumnMetadata.named("create_by").ofType(Types.VARCHAR));
        addMetadata(createTime, ColumnMetadata.named("create_time").ofType(Types.TIMESTAMP));
        addMetadata(updateBy, ColumnMetadata.named("update_by").ofType(Types.VARCHAR));
        addMetadata(updateTime, ColumnMetadata.named("update_time").ofType(Types.TIMESTAMP));
        addMetadata(remark, ColumnMetadata.named("remark").ofType(Types.VARCHAR));
    }

}

