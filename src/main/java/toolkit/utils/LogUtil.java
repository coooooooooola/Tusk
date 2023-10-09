package toolkit.utils;

/**
 * Create by swtywang on 9/28/23 3:14 PM
 */

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import toolkit.TkConf;

/**
 * 自定义日志
 */
public class LogUtil {
    private Logger log ;
    private LogUtil(String name) {
        configLogProperties();
        log = Logger.getLogger(name);
    }
    private LogUtil(Class<?> clazz){
        configLogProperties();
        log = Logger.getLogger(clazz);
    }

    /**
     *
     * @param name
     * @return LogUtil
     */
    public static LogUtil getLogger(String name){
        return new LogUtil(name);
    }
    /**
     *
     * @param clazz
     * @return LogUtil
     */
    public static LogUtil getLogger(Class<?> clazz){
        return new LogUtil(clazz);
    }

    /**
     * error级别日志
     * @param message
     */
    public void error(Object message){
        log.error(message);
    }
    /**
     * info 级别日志
     * @param message
     */
    public void info(Object message){
        log.info(message);
    }
    /**
     * debug级别日志
     * @param message
     */
    public void debug(Object message){
        log.debug(message);
    }
    /**
     * warn级别日志
     * @param message
     */
    public void warn(Object message){
        log.warn(message);
    }

    /**
     * 读取日志配置文件
     */
    private  void configLogProperties(){
        try {
            TkConf.writeConf();
            PropertyConfigurator.configure(TkConf.Log4jConf);
        } catch (Exception e) {
//			e.printStackTrace();
        }
    }


}
