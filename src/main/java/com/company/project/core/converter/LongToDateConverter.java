package com.company.project.core.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author shanchao
 * @date 2018-05-22
 */
@Component
public class LongToDateConverter implements Converter<Long, Date> {

    @Nullable
    @Override
    public Date convert(Long aLong) {
        return new Date(aLong);
    }
}
