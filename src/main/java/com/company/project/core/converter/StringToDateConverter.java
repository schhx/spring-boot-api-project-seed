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
public class StringToDateConverter implements Converter<String, Date> {

    @Nullable
    @Override
    public Date convert(String s) {
        return new Date(Long.parseLong(s));
    }
}
