package com.ruoyi.system.entity.dsl;

import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.PrimaryKey;
import com.querydsl.sql.RelationalPathBase;
import com.ruoyi.system.entity.SysConfig;

import javax.annotation.Generated;
import java.sql.Types;
import java.time.LocalDateTime;

@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QSysConfig extends RelationalPathBase<SysConfig> {

    public final NumberPath<Long> configId = createNumber("configId", Long.class);

    public final StringPath configKey = createString("configKey");

    public final StringPath configName = createString("configName");

    public final StringPath configType = createString("configType");

    public final StringPath configValue = createString("configValue");

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<LocalDateTime> createdDate = createDateTime("createdDate", LocalDateTime.class);

    public final StringPath lastModifiedBy = createString("lastModifiedBy");

    public final DateTimePath<LocalDateTime> lastModifiedDate = createDateTime("lastModifiedDate", LocalDateTime.class);

    public final StringPath remark = createString("remark");

    public final PrimaryKey<SysConfig> primary = createPrimaryKey(configId);

    public QSysConfig(String variable) {
        super(SysConfig.class, variable, "ruoyi", "sys_config");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(configId, ColumnMetadata.named("config_id").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(configType, ColumnMetadata.named("config_type").withIndex(2).ofType(Types.VARCHAR).withSize(1));
        addMetadata(configKey, ColumnMetadata.named("config_key").withIndex(3).ofType(Types.VARCHAR).withSize(100));
        addMetadata(configName, ColumnMetadata.named("config_name").withIndex(4).ofType(Types.VARCHAR).withSize(100));
        addMetadata(configValue, ColumnMetadata.named("config_value").withIndex(5).ofType(Types.VARCHAR).withSize(500));
        addMetadata(createdBy, ColumnMetadata.named("created_by").withIndex(6).ofType(Types.VARCHAR).withSize(20));
        addMetadata(createdDate, ColumnMetadata.named("created_date").withIndex(7).ofType(Types.TIMESTAMP).withSize(26));
        addMetadata(lastModifiedBy, ColumnMetadata.named("last_modified_by").withIndex(8).ofType(Types.VARCHAR).withSize(20));
        addMetadata(lastModifiedDate, ColumnMetadata.named("last_modified_date").withIndex(9).ofType(Types.TIMESTAMP).withSize(26));
        addMetadata(remark, ColumnMetadata.named("remark").withIndex(10).ofType(Types.VARCHAR).withSize(500));
    }

}

