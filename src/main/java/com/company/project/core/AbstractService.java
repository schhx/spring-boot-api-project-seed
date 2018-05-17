package com.company.project.core;


import com.company.project.core.utils.ReflectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 基于通用MyBatis Mapper插件的Service接口的实现
 */
public abstract class AbstractService<T> implements Service<T> {

    @Autowired
    protected Mapper<T> mapper;

    @Override
    public T create(T model) {
        mapper.insertSelective(model);
        return getById(ReflectUtil.getId(model)).get();
    }

    @Override
    public void deleteById(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteByIds(Set<String> ids) {
        if (!ids.isEmpty()) {
            StringJoiner joiner = new StringJoiner(",");
            ids.stream().forEach(id -> joiner.add(id));
            mapper.deleteByIds(joiner.toString());
        }
    }

    @Override
    public Optional<T> update(T model) {
        mapper.updateByPrimaryKeySelective(model);
        return getById(ReflectUtil.getId(model));
    }

    @Override
    public Optional<T> getById(Integer id) {
        T t = mapper.selectByPrimaryKey(id);
        return Optional.ofNullable(t);
    }

    @Override
    public List<T> getByIds(Set<String> ids) {
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }
        StringJoiner joiner = new StringJoiner(",");
        ids.stream().forEach(id -> joiner.add(id));
        return mapper.selectByIds(joiner.toString());
    }

    @Override
    public ListResult<T> page(PageFilter<T> filter) {
        PageHelper.startPage(filter.getPage(), filter.getPageSize());
        PageHelper.orderBy(filter.exportOrderByToString());
        List<T> list = mapper.select(filter.getT());
        return new ListResult<>(new PageInfo<>(list));
    }
}
