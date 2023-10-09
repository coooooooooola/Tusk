package ift.autocreate;

/**
 * Created by liushanshan on 2018/3/5.
 */


import ift.IftConf;
import toolkit.utils.FreeMakerUtil;
import toolkit.utils.LogUtil;

import java.util.Map;

public class CreateThriftJavaFile {

    private LogUtil log=LogUtil.getLogger(CreateThriftJavaFile.class);//日志记录

    public boolean createThriftJava(String clsName, Map<String,Object> data) {
        String thriftJavaFilePathTmp = IftConf.ThriftJavaPath + clsName+".java";
        FreeMakerUtil creatjava=new FreeMakerUtil();
        if (creatjava.CreateJavaFile(IftConf.TemplateThriftJavaPath, data, thriftJavaFilePathTmp)) {
            log.info("创建"+clsName+"对应的.java文件成功："+thriftJavaFilePathTmp);
            return true;
        }
        log.error("创建"+clsName+"对应的.java文件失败:"+thriftJavaFilePathTmp);
        return false;
    }
}
