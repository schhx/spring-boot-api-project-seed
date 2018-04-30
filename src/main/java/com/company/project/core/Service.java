package com.company.project.core;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service 层 基础接口，其他Service 接口 请继承该接口
 */
public interface Service<T> {
    /**
     * 新增
     * @param model
     * @return
     */
    T create(T model);

    /**
     * 通过主鍵刪除
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 通过主键批量删除
     * @param ids
     */
    void deleteByIds(Set<String> ids);

    /**
     * 更新
     * @param model
     * @return
     */
    Optional<T> update(T model);

    /**
     * 通过主键查找
     * @param id
     * @return
     */
    Optional<T> getById(Integer id);

    /**
     * 通过主键批量查找
     * @param ids
     * @return
     */
    List<T> getByIds(Set<String> ids);

    /**
     * 分页查询
     * @param filter
     * @return
     */
    ListResult<T> page(PageFilter<T> filter);

}
