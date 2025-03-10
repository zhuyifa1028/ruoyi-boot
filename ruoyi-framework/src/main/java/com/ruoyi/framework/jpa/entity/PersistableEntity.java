package com.ruoyi.framework.jpa.entity;

import cn.hutool.core.util.IdUtil;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Getter;
import org.springframework.data.domain.Persistable;

@MappedSuperclass
public abstract class PersistableEntity implements Persistable<String> {

    @Getter
    @Transient
    private String id;

    @Transient
    private Boolean isNew = false;

    @Transient
    public abstract void setId(String id);

    @Transient
    public boolean isNew() {
        return isNew;
    }

    @Transient
    public void markCreated() {
        this.isNew = true;
        this.setId(IdUtil.getSnowflakeNextIdStr());
    }

}
