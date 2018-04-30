package ${basePackage}.controller;

import ${basePackage}.core.ListResult;
import ${basePackage}.core.PageFilter;
import ${basePackage}.model.User;
import ${basePackage}.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
* @author ${author}
* @date ${date}
*/
@RestController
@RequestMapping("${baseRequestMapping}")
public class ${modelNameUpperCamel}Controller {
    @Autowired
    private ${modelNameUpperCamel}Service ${modelNameLowerCamel}Service;

    @PostMapping
    public ${modelNameUpperCamel} create${modelNameUpperCamel}(@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        return ${modelNameLowerCamel}Service.create(${modelNameLowerCamel});
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        ${modelNameLowerCamel}Service.deleteById(id);
    }

    @PatchMapping("/{id}")
    public ${modelNameUpperCamel} update${modelNameUpperCamel}(@PathVariable Integer id, @RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        user.setId(id);
        return ${modelNameLowerCamel}Service.update(${modelNameLowerCamel}).orElse(null);
    }

    @GetMapping("/{id}")
    public ${modelNameUpperCamel} get${modelNameUpperCamel}(@PathVariable Integer id) {
        return ${modelNameLowerCamel}Service.getById(id).orElse(null);
    }

    @PostMapping("/page")
    public ListResult<${modelNameUpperCamel}> page${modelNameUpperCamel}s(@RequestBody PageFilter<${modelNameUpperCamel}> filter) {
        return ${modelNameLowerCamel}Service.page(filter);
    }
}
