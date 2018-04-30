package com.company.project;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author shanchao
 * @date 2018-04-29
 */
public class GeneratorConstant {

    protected static final String BASE_PACKAGE = "com.company.project";//项目基础包名称，根据自己公司的项目修改

    protected static final String MODEL_PACKAGE = BASE_PACKAGE + ".model";//Model所在包
    protected static final String MAPPER_PACKAGE = BASE_PACKAGE + ".dao";//Mapper所在包
    protected static final String SERVICE_PACKAGE = BASE_PACKAGE + ".service";//Service所在包
    protected static final String SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";//ServiceImpl所在包
    protected static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".controller";//Controller所在包

    public static final String MAPPER_INTERFACE_REFERENCE = BASE_PACKAGE + ".core.Mapper";//Mapper插件基础接口的完全限定名


    //JDBC配置，请修改为你项目的实际配置
    protected static final String JDBC_URL = "jdbc:mysql://localhost:3306/spring_boot_api_project_seed";
    protected static final String JDBC_USERNAME = "root";
    protected static final String JDBC_PASSWORD = "root";
    protected static final String JDBC_DIVER_CLASS_NAME = "com.mysql.jdbc.Driver";


    protected static final String PROJECT_PATH = System.getProperty("user.dir");//项目在硬盘上的基础路径
    protected static final String TEMPLATE_FILE_PATH = PROJECT_PATH + "/src/test/resources/template";//模板位置

    protected static final String JAVA_PATH = "/src/main/java"; //java文件路径
    protected static final String RESOURCES_PATH = "/src/main/resources";//资源文件路径

    protected static final String PACKAGE_PATH_SERVICE = packageConvertPath(SERVICE_PACKAGE);//生成的Service存放路径
    protected static final String PACKAGE_PATH_SERVICE_IMPL = packageConvertPath(SERVICE_IMPL_PACKAGE);//生成的Service实现存放路径
    protected static final String PACKAGE_PATH_CONTROLLER = packageConvertPath(CONTROLLER_PACKAGE);//生成的Controller存放路径

    protected static final String AUTHOR = "CodeGenerator";//@author
    protected static final String DATE = new SimpleDateFormat("yyyy-MM-dd").format(new Date());//@date

    protected static String packageConvertPath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }
}
