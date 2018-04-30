package ${basePackage}.service.impl;

import ${basePackage}.dao.${modelNameUpperCamel}Mapper;
import ${basePackage}.model.${modelNameUpperCamel};
import ${basePackage}.service.${modelNameUpperCamel}Service;
import ${basePackage}.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


/**
* @author ${author}
* @date ${date}
*/
@Service
public class ${modelNameUpperCamel}ServiceImpl extends AbstractService<${modelNameUpperCamel}> implements ${modelNameUpperCamel}Service {
    @Autowired
    private ${modelNameUpperCamel}Mapper ${modelNameLowerCamel}Mapper;

}
