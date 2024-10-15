package com.ruoyi.system.entity.path;

import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.PrimaryKey;
import com.querydsl.sql.RelationalPathBase;
import com.ruoyi.system.entity.SysDept;

import javax.annotation.Generated;
import java.sql.Types;
import java.time.LocalDateTime;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QSysDept extends RelationalPathBase<SysDept> {

    public final NumberPath<Long> deptId = createNumber("deptId", Long.class);

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    public final StringPath ancestor = createString("ancestor");

    public final StringPath deptName = createString("deptName");

    public final NumberPath<Integer> orderNum = createNumber("orderNum", Integer.class);

    public final StringPath leader = createString("leader");

    public final StringPath phone = createString("phone");

    public final StringPath email = createString("email");

    public final StringPath status = createString("status");

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<LocalDateTime> createdDate = createDateTime("createdDate", LocalDateTime.class);

    public final StringPath lastModifiedBy = createString("lastModifiedBy");

    public final DateTimePath<LocalDateTime> lastModifiedDate = createDateTime("lastModifiedDate", LocalDateTime.class);

    public final PrimaryKey<SysDept> primary = createPrimaryKey(deptId);

    public QSysDept(String variable) {
        super(SysDept.class, forVariable(variable), "ruoyi", "sys_dept");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(deptId, ColumnMetadata.named("dept_id").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(parentId, ColumnMetadata.named("parent_id").withIndex(2).ofType(Types.BIGINT).withSize(20));
        addMetadata(ancestor, ColumnMetadata.named("ancestor").withIndex(3).ofType(Types.VARCHAR).withSize(200));
        addMetadata(deptName, ColumnMetadata.named("dept_name").withIndex(4).ofType(Types.VARCHAR).withSize(30));
        addMetadata(orderNum, ColumnMetadata.named("order_num").withIndex(5).ofType(Types.BIGINT).withSize(11));
        addMetadata(leader, ColumnMetadata.named("leader").withIndex(6).ofType(Types.VARCHAR).withSize(20));
        addMetadata(phone, ColumnMetadata.named("phone").withIndex(7).ofType(Types.VARCHAR).withSize(11));
        addMetadata(email, ColumnMetadata.named("email").withIndex(8).ofType(Types.VARCHAR).withSize(50));
        addMetadata(status, ColumnMetadata.named("status").withIndex(9).ofType(Types.VARCHAR).withSize(1));
        addMetadata(createdBy, ColumnMetadata.named("created_by").withIndex(10).ofType(Types.VARCHAR).withSize(30));
        addMetadata(createdDate, ColumnMetadata.named("created_date").withIndex(11).ofType(Types.TIMESTAMP).withSize(26));
        addMetadata(lastModifiedBy, ColumnMetadata.named("last_modified_by").withIndex(12).ofType(Types.VARCHAR).withSize(30));
        addMetadata(lastModifiedDate, ColumnMetadata.named("last_modified_date").withIndex(13).ofType(Types.TIMESTAMP).withSize(26));
    }

}

