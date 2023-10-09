package dispatch.run;


import com.alibaba.fastjson.JSONObject;
import com.meituan.ep.leo.ift.IftConf;
import com.meituan.ep.leo.ift.core.CompareResult;
import com.meituan.ep.leo.toolkit.httpclient.HttpUtil;
import com.meituan.ep.leo.toolkit.httpclient.ResponseInfo;
import com.meituan.ep.leo.toolkit.util.LogUtil;
import org.testng.IAnnotationTransformer2;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.IDataProviderAnnotation;
import org.testng.annotations.IFactoryAnnotation;
import org.testng.annotations.ITestAnnotation;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by wanghaiqian on 17/2/21.
 */
public class TestngAnnotation implements IAnnotationTransformer2 {
    @Resource
    private static HttpUtil httpUtil = new HttpUtil();

    @Resource
    private ResponseInfo responseInfo;

    @Resource
    private CompareResult compareResult;

    private static String qdvApiReportUrl = "http://qdv.avatar.movie.test.sankuai.com/apiReport/apiInfo";

    private static String qdvApiReportUrlOnline = "http://myqdv.sankuai.com/apiReport/apiInfo";

    private LogUtil logUtil = LogUtil.getLogger(TestngAnnotation.class);

    @Override
    public void transform(IConfigurationAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {

    }

    @Override
    public void transform(IDataProviderAnnotation annotation, Method method) {

    }

    @Override
    public void transform(IFactoryAnnotation annotation, Method method) {

    }

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        // 兼容kumamon在entry中声明不上报接口信息
        if (IftConf.ListenAnnotation.equals("N")) {
            return;
        }
        //1. buildRequest
        int apiType = 1; //  1-http  2-rpc
        String apiAuthor = "";
        String apiName = "";
        String ctrlName = "";
        String env = "";
        if (testMethod.isAnnotationPresent(ApiInfoAnnotation.class)) {
            ApiInfoAnnotation apiInfoAnnotation =  testMethod.getAnnotation(ApiInfoAnnotation.class);
            apiAuthor = apiInfoAnnotation.apiAuthor();
            apiName = apiInfoAnnotation.apiName();
            ctrlName = apiInfoAnnotation.ctrlName();
            env = apiInfoAnnotation.env();
            // 兼容kumamon 在Entry中写env方式
            env = env.equals("") ? IftConf.RunEnv : env;
        }

        if (testMethod.isAnnotationPresent(ThriftInfoAnnotation.class)) {
            ThriftInfoAnnotation apiInfoAnnotation =  testMethod.getAnnotation(ThriftInfoAnnotation.class);
            apiAuthor = apiInfoAnnotation.apiAuthor();
            apiName = apiInfoAnnotation.appkey();
            ctrlName = apiInfoAnnotation.ctrlName();
            env = apiInfoAnnotation.env();
            // 兼容kumamon 在Entry中写env方式
            env = env.equals("") ? IftConf.RunEnv : env;
            apiType = 1;
        }
        if (ctrlName == null || ctrlName.equals("")) {
            return;
        }

        //2. post
        List<String> apiNameList = splitInfo(apiName);
        /**
         * 通常情况下，apiName-ctrlName是1对1，多对1的关系
         * 目前看到的例外：testUserCreateMovieOrderReturnData，多对多，但是不能指出那个apiName对应哪个ctrlName，需要特殊梳理
         */
        for (String apiNameOne : apiNameList) {
            String formPara = "apiAuthor=" + apiAuthor + "&apiName=" + apiNameOne + "&ctrlMethod=" + ctrlName + "&env=" + env + "&apiType=" + apiType;

            //上报api数据到可视化报表平台
            JSONObject jsonPara = new JSONObject();
            jsonPara.put("apiName", apiNameOne);
            jsonPara.put("ctrlName", ctrlName);
            jsonPara.put("apiAuthor", apiAuthor);
            jsonPara.put("env", env);
            try {
                responseInfo = httpUtil.post(qdvApiReportUrl, jsonPara);
                if (!responseInfo.getStatus().matches(".*200.*") || !responseInfo.getResBodyInfo().matches(".*true.*")) {
                    logUtil.error("接口上报qdv失败：" + ctrlName);
                    logUtil.error("上报qdv信息：" + jsonPara);
                    logUtil.error(responseInfo.getResBodyInfo());
                }
                responseInfo = httpUtil.post(qdvApiReportUrlOnline, jsonPara);
                if (!responseInfo.getStatus().matches(".*200.*") || !responseInfo.getResBodyInfo().matches(".*true.*")) {
                    logUtil.error("接口上报qdv失败：" + ctrlName);
                    logUtil.error("上报qdv信息：" + jsonPara);
                    logUtil.error(responseInfo.getResBodyInfo());
                }
                else{
                    logUtil.info("接口上报qdv成功：" + ctrlName);
                }
            } catch (Exception e) {
                logUtil.error(e.getMessage());
            }
        }
    }

    private List<String> splitInfo(String str) {
        str = str.replaceAll(" ", "");
        List<String> arrayList = new ArrayList<>();
        if (str.contains(",")) {
            str = str.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "");
            arrayList = Arrays.asList(str.split(","));
        } else {
            arrayList.add(str);
        }
        return arrayList;
    }

}
