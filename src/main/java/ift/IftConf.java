package ift;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Create by swtywang on 9/28/23 4:03 PM
 */

/**
 * 接口全局配置文件
 */
public class IftConf {
    /**
     * 自定义依赖参数
     */
    public static Map DependPara = new HashMap();
    /**
     * 项目根目录
     */
    public static final String RootPath = getRootPath();
    /**
     * 接口ift配置等文件默认保存目录
     */
    public static String IftPath = RootPath + "leo/ift/";
    /**
     * 默认编译输出目录
     */
    public static String DistPath = getDistPath();
    /**
     * 默认lib目录
     */
    public static String LibPath = getLibPath();
    /**
     * jar文件目录  maven
     */
    public static String JarFile = "";
    /**
     * 默认生成的java文件存放目录
     */
    public static String JavaPath = IftPath + "javaCase/";
    /**
     * 生成thrift case的参数
     */
    public static Map ThriftParameters = new HashMap();
    /**
     * 默认生成的thrift java文件存放目录
     */
    public static String ThriftJavaPath = IftPath + "thriftJavaCase/";
    public static String ThriftPackageName = "com.meituan.ep.leo.ift.thriftJavaCase";

    /**
     * 默认生成java文件时的包名
     */
    public static String PackageName = "com.meituan.ep.leo.ift.testcase";

    /**
     * 默认的模板文件
     */
    public static String TemplatePath = IftPath + "template/Template.ftl";

    /**
     * Thrift xml模板文件
     */
    public static String TemplateThriftXmlPath = IftPath + "template/TemplateThriftXml.ftl";

    /**
     * Thrift xml模板文件
     */
    public static String TemplateThriftJavaPath = IftPath + "template/TemplateThriftCase.ftl";

    /**
     * 默认测试报告输出路径
     */
    public static String ReportPath = IftPath + "report/";
    /**
     * 默认的配置文件
     */
    public static String ConfFile = IftPath + "config/IftConf.properties";

    //代理相关配置信息
    /**
     * 是否启用代理配置
     */
    public static String ProxyEnable = getPropValue("ProxyEnable", "N");
    /**
     * 代理IP
     */
    public static String ProxyIp = getPropValue("ProxyIp", "127.0.0.1");
    /**
     * 代理端口
     */
    public static int PROXY_PORT = Integer.parseInt(getPropValue("ProxyPort", "8888"));

    //https协议配置
    /**
     * 是否使用本地认证信息
     */
    public static String SSL = getPropValue("SSL", "N");
    /**
     * 本地认证信息路径
     */
    public static String KeyPath = getPropValue("keyPath", IftConf.RootPath);
    /**
     * 密匙库的密码
     */
    public static String KeyPassword = getPropValue("keyPassword", "");

    //时间样式设置
    public static String DateFormat = getPropValue("dateFormat", "yyyMMddHHmmss");

    //结果比对参数
    /**
     * json默认解析方式 单层解析
     */
    public static final int parseJson = 1;

    //测试用例Excel文件读取 相关配置信息
    /**
     * url所在行数
     */
    public static final int urlRow = 0;
    /**
     * url所在列数
     */
    public static final int urlCol = 1;
    /**
     * httpMethod所在行数
     */
    public static final int methodRow = 1;
    /**
     * httpMethod所在列数
     */
    public static final int methodCol = 1;
    /**
     * 全局cookie所在行数
     */
    public static final int cookieRow = 2;
    /**
     * 全局cookie所在列数
     */
    public static final int cookieCol = 1;
    /**
     * 参数签名计算参数个数所在行数
     */
    public static final int argCountRow = 3;
    /**
     * 参数签名计算参数个数所在列数
     */
    public static final int argCountCol = 1;
    /**
     * 标题所在行
     */
    public static final int typeRow = 4;
    /**
     * 用例数据开始行数
     */
    public static final int paramStartRow = 5;
    /**
     * 用例数据开始列表
     */
    public static final int paramStartCol = 3;
    /**
     * caseId所在列数
     */
    public static final int caseIdCol = 1;
    /**
     * 是否执行属性所在列数
     */
    public static final int isRunCol = 1;
    /**
     * secondUrl所在列数
     */
    public static final int secondUrlCol = 2;


    /**
     * 方法名称所在列
     */
    public static final int ctrlNameCol = 3;

    /**
     * 方法名称所在行
     */
    public static final int ctrlNameRow = 1;

    /**
     * api名称所在列
     */
    public static final int apiNameCol = 3;

    /**
     * api名称所在行
     */
    public static final int apiNameRow = 2;

    /**
     * 接口文件编写人所在列
     */
    public static final int apiAuthorCol = 3;

    /**
     * 接口文件编写人所在行
     */
    public static final int apiAuthorRow = 3;

    /**
     * 是否上报接口信息
     */
    public static String ListenAnnotation = getLisTenAnnotation("ListenEnable", "Y");

    public static String RunEnv = getPropValue("RunEnv", "prod");

    public static int HttpTimeOutLimit = 10000;

    /**
     * Testng annotation listener className ，用来监听并上报接口测试信息
     */
    public static final String AnnotationListenerClassName = "com.meituan.ep.leo.dispatch.run.TestngAnnotation";
    /**
     * Testng annotation listener className ，用来监听根据环境执行的方法
     */
    public static final String MethodEnvFilterClassName = "com.meituan.ep.leo.dispatch.run.MethodEnvFilter";

    /**
     * Testng case run info listener className ，用来监听并上报接口详情
     */
    public static final String CaseListenerClassName = "com.meituan.ep.leo.dispatch.run.CaseInfoListener";

    /**
     * thrift接口remote appkey
     */
    public static String appKey = "";

    /**
     * thrift接口文件对应package名称
     */
    public static String thriftPackageName = "";

    /**
     * thrift接口对应注入service名称
     */
    public static String thriftService = "";

    /**
     * thrift接口对应ip:port
     */
    public static String thriftServerIpPort = "";

    /**
     * thrift xml path
     */
    public static String thriftXmlPath = getDistPath();

    /**
     * thrift xml name
     */
    public static String thriftXmlName = "thrift-config.xml";


    /**
     * 删除默认临时目录
     */
    public static void delTmpPath() {
        try {
            // 清空java文件生成目录
            FileUtils.deleteDirectory(new File(JavaPath));
        } catch (IOException e) {
//			e.printStackTrace();
        }
    }


    /**
     * 如果配置文件不存在，写入
     *
     * @return boolean 已存在或创建失败时返回false  创建成功返回true
     */
    public static boolean writeConf() {
        //相应配置文件如果不存在，则创建
        boolean flag = false;
        if (!new File(ConfFile).exists()) {
            flag = copyFile(IftConf.class.getResourceAsStream("/IftConf.properties"), new File(ConfFile));
        }
        if (!new File(TemplatePath).exists()) {
            flag = copyFile(IftConf.class.getResourceAsStream("/Template.ftl"), new File(TemplatePath));
        }
        if (!new File(TemplateThriftXmlPath).exists()) {
            flag = copyFile(IftConf.class.getResourceAsStream("/TemplateThriftXml.ftl"), new File(TemplateThriftXmlPath));
        }
        if (!new File(TemplateThriftJavaPath).exists()) {
            flag = copyFile(IftConf.class.getResourceAsStream("/TemplateThriftCase.ftl"), new File(TemplateThriftJavaPath));
        }
        return flag;
    }

    /**
     * 设置依赖的jar文件路径信息 maven
     *
     * @param args
     * @return boolean 设置成功返回true
     */
    public static boolean updateJarFile(String[] args) {

        if (IftConf.JarFile.length() > 0) {
            return true;//JarFile已设置
        }
        try {
            if (null != args && args.length > 0) {//设置JarFile，同时写入文件
                JarFile = args[0];
                FileUtils.writeStringToFile(new File(IftConf.IftPath + "JarFile"), JarFile, "UTF-8");
            } else {
                if (new File(IftPath + "JarFile").exists()) {//已存在则读取
                    FileUtils.readFileToString(new File(IftPath + "JarFile"), "UTF-8");
                    IftConf.JarFile = FileUtils.readFileToString(new File(IftPath + "JarFile"), "UTF-8");
                } else {
                    System.out.print("在eclipse中，第一次需要以maven方式执行");
                    return false;
                }
            }
        } catch (IOException e) {
//			e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 获取项目根目录，如果是在Tomcat中运行，则返回部署根目录
     *
     * @return String
     */
    private static String getRootPath() {
        String path = "";
        if (IftConf.class.getClassLoader().getResource("").toString().indexOf("WEB-INF") != -1) {
            path = IftConf.class.getClassLoader().getResource("").toString().substring(6, IftConf.class.getClassLoader().getResource("").toString().indexOf("WEB-INF"));
            path = path + "/";
        } else {
            String temp = System.getProperty("user.dir");
            temp = temp.replace("\\", "/");
            path = temp + "/";
        }
        return path;
    }

    /**
     * 获取编译输出目录，如果是在Tomcat中运行，则返回部署目录下的WEB-INF/classes/  否则返回bin/
     *
     * @return String
     */
    private static String getDistPath() {
        String path = "";
        if (IftConf.class.getClassLoader().getResource("").toString().indexOf("WEB-INF") != -1) {
            path = IftConf.class.getClassLoader().getResource("").toString().substring(6, IftConf.class.getClassLoader().getResource("").toString().indexOf("WEB-INF"));
            path = path + "/WEB-INF/classes/";
        } else {
//			path = GlobalSettings.class.getClassLoader().getResource("").toString();
            String temp = System.getProperty("user.dir");
            temp = temp.replace("\\", "/");
            path = temp + "/target/classes/";
        }
        return path;
    }

    /**
     * 获取jar包存放的lib目录，如果是在Tomcat中运行，则返回部署目录下的WEB-INF/lib/  否则返回lib/
     *
     * @return String
     */
    private static String getLibPath() {
        String path = "";
        if (IftConf.class.getClassLoader().getResource("").toString().indexOf("WEB-INF") != -1) {
            path = IftConf.class.getClassLoader().getResource("").toString().substring(6, IftConf.class.getClassLoader().getResource("").toString().indexOf("WEB-INF"));
            path = path + "/WEB-INF/lib/";
        } else {
//			path = GlobalSettings.class.getClassLoader().getResource("").toString();
            String temp = System.getProperty("user.dir");
            temp = temp.replace("\\", "/");
            path = temp + "/lib/";
        }
        return path;
    }

    private static String getPropValue(String key, String defaultValue) {
        return getProperties().getProperty(key, defaultValue);
    }

    private static String getLisTenAnnotation(String key, String defaultValue) {
        return getProperties().getProperty(key, defaultValue);
    }

    private static Properties getProperties() {
        Properties prop = new Properties();
        try {
            //配置文件不存在则创建
            IftConf.writeConf();
            FileInputStream file = new FileInputStream(ConfFile);
            prop.load(new InputStreamReader(file, "UTF-8"));
            file.close();
        } catch (Exception e) {
//			e.printStackTrace();
        }
        return prop;
    }

    private static boolean copyFile(InputStream from, File to) {
        try {
            if (!to.getParentFile().exists()) {
                to.getParentFile().mkdirs();
            }
            OutputStream os = new FileOutputStream(to);
            byte[] buffer = new byte[65536];
            int count = from.read(buffer);
            while (count > 0) {
                os.write(buffer, 0, count);
                count = from.read(buffer);
            }
            os.close();
            return true;
        } catch (IOException e) {
            return false;
        }

    }
}


