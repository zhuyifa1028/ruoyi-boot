package com.ruoyi.common.utils;

import com.ruoyi.common.core.domain.TreeSelect;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TreeSelectUtils {

    public static <T> List<TreeSelect> build(List<T> voList, Function<T, TreeSelect> mapper) {
        if (CollectionUtils.isEmpty(voList)) {
            return Collections.emptyList();
        }

        List<TreeSelect> collect = voList.stream().map(mapper).collect(Collectors.toList());

        Map<Object, List<TreeSelect>> childrenListMap = new HashMap<>();
        Map<Object, TreeSelect> nodeIds = new HashMap<>();
        List<TreeSelect> tree = new ArrayList<>();

        collect.forEach(item -> {
            if (childrenListMap.containsKey(item.getParentId())) {
                childrenListMap.get(item.getParentId()).add(item);
            } else {
                childrenListMap.put(item.getParentId(), new ArrayList<TreeSelect>() {{
                    add(item);
                }});
            }
            nodeIds.put(item.getId(), item);
        });

        collect.forEach(item -> {
            if (nodeIds.containsKey(item.getParentId())) {
                return;
            }
            tree.add(item);
        });

        tree.forEach(item -> adaptToChildrenList(item, childrenListMap));

        return tree;
    }

    public static void adaptToChildrenList(TreeSelect item, Map<Object, List<TreeSelect>> childrenListMap) {
        if (childrenListMap.containsKey(item.getId())) {
            item.setChildren(childrenListMap.get(item.getId()));
        }

        for (TreeSelect child : item.getChildren()) {
            adaptToChildrenList(child, childrenListMap);
        }
    }

}
