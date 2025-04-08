package com.ruoyi.system.repository;

import com.ruoyi.framework.jpa.repository.BaseRepository;
import com.ruoyi.system.entity.SysDept;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysDeptRepository extends BaseRepository<SysDept> {

    SysDept findByParentIdAndDeptName(String parentId, String deptName);

    List<SysDept> findAllByAncestorsContains(String ancestors);

    boolean existsByParentId(String parentId);

}
