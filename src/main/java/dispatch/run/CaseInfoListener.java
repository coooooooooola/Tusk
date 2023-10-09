package dispatch.run;

import com.alibaba.fastjson.JSONObject;
import com.meituan.ep.leo.dispatch.monitor.ArgosMonitor;
import com.meituan.ep.leo.ift.IftConf;
import com.meituan.ep.leo.toolkit.httpclient.HttpUtil;
import com.meituan.ep.leo.toolkit.httpclient.ResponseInfo;
import com.meituan.ep.leo.toolkit.util.LogUtil;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import javax.annotation.Resource;
import java.lang.reflect.Field;

/**
 * Created by zll on 17/7/16.
 */
public class CaseInfoListener extends TestListenerAdapter {

    @Resource
    private static HttpUtil httpUtil = new HttpUtil();

    private static String qdvCaseReportUrl = "http://qdv.avatar.movie.test.sankuai.com/apiReport/caseInfo";

    private static String qdvCaseReportUrlOnline = "http://myqdv.sankuai.com/apiReport/caseInfo";
    @Resource
    private ResponseInfo responseInfo;

    private LogUtil logUtil = LogUtil.getLogger(CaseInfoAnnotation.class);

    String caseAuthor = "";
    String caseIdentify = "";
    String env = "";

    @Override
    public void onTestStart(ITestResult tr) {
        if (tr.getMethod().getConstructorOrMethod().getMethod().isAnnotationPresent(CaseInfoAnnotation.class)) {
            CaseInfoAnnotation caseInfoAnnotation = tr.getMethod().getConstructorOrMethod().getMethod().getAnnotation(CaseInfoAnnotation.class);
            caseAuthor = caseInfoAnnotation.caseAuthor();
            caseIdentify = caseInfoAnnotation.caseIdentify();
            env = caseInfoAnnotation.env();
            // 兼容kumamon 在Entry中写env方式
            env = env.equals("") ? IftConf.RunEnv : env;
        }
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        super.onTestSuccess(tr);

        String status = "true";
        String failTag = "";
        String failInfo = "";
        String testPoint = "";

        ITestNGMethod method = tr.getMethod();
        testPoint = this.getTestPoint(method);

        //上报case数据到可视化报表平台
        JSONObject jsonPara = new JSONObject();
        jsonPara.put("caseIdentify", caseIdentify);
        jsonPara.put("env", env);
        jsonPara.put("testPoint", testPoint);
        jsonPara.put("env", env);
        jsonPara.put("caseAuthor", caseAuthor);
        jsonPara.put("status", status);
        jsonPara.put("failTag", failTag);
        jsonPara.put("failInfo", failInfo);

        caseReportQdv(jsonPara, "test");
        caseReportQdv(jsonPara, "online");

    }

    @Override
    public void onTestFailure(ITestResult tr) {

        super.onTestFailure(tr);
        if (ArgosMonitor.argosSwitchOn) {
            ArgosMonitor argosMonitor = new ArgosMonitor();

            String classStr = tr.getTestClass().getName();
            int index = classStr.lastIndexOf(".");
            classStr = classStr.substring(0, index) + "/" + classStr.substring(index + 1);


            // 触发接口错误大象报警
            argosMonitor.argosAlert(this.getTestPoint(tr.getMethod()), tr.getThrowable().getMessage(), classStr, tr.getMethod().getMethodName().toString());
        }

        // 上报case 错误原因
        this.uploadCaseErrorInfo(tr);


    }

    private String getTestPoint(ITestNGMethod method) {
        String testPoint = "";
        if (method.getDescription() != null) {
            if (method.getDescription().length() >= 2 && method.getDescription().endsWith("()")) {
                testPoint = method.getDescription().substring(0, method.getDescription().length() - 2);
            } else if (method.getDescription().length() > 0) {
                testPoint = method.getDescription();
            }
        }
        return testPoint;
    }

    private void uploadCaseErrorInfo(ITestResult tr) {
        String status = "false";
        String failTag = "";
        String failInfo = "";
        String testPoint = "";

        ITestNGMethod method = tr.getMethod();
        testPoint = this.getTestPoint(method);

        Throwable cause = tr.getThrowable();
        failInfo = cause.getMessage();

        if (failInfo.contains("code=") || (failInfo.length() <= 500)) {

        } else {
            int index = failInfo.indexOf("【responseBody解析结果：】");
            failInfo = failInfo.substring(0, index);
        }

        try {
            Class testClass = Class.forName(tr.getInstanceName());
            Object testCase = testClass.newInstance();
            Field field = testClass.getDeclaredField("status");
            String value = (String) field.get(testCase);

            if (value != null && value.substring(9, 12).matches("^[4,5][0-9]{2}$")) {
                failTag = "接口" + value.substring(9, 12);
            } else {
                failTag = "断言失败";
            }
        } catch (Exception e) {
            logUtil.error(e.getMessage());
            failTag = "断言失败";
        }

        //上报case数据到可视化报表平台
        JSONObject jsonPara = new JSONObject();
        jsonPara.put("caseIdentify", caseIdentify);
        jsonPara.put("env", env);
        jsonPara.put("testPoint", testPoint);
        jsonPara.put("env", env);
        jsonPara.put("caseAuthor", caseAuthor);
        jsonPara.put("status", status);
        jsonPara.put("failTag", failTag);
        jsonPara.put("failInfo", failInfo);

        caseReportQdv(jsonPara, "test");
        caseReportQdv(jsonPara, "online");
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext tc) {

        super.onFinish(tc);
    }

    private void caseReportQdv(JSONObject mapPara, String env) {
        try {
            String postUrl = (env.equals("test")) ? qdvCaseReportUrl : qdvCaseReportUrlOnline;
            responseInfo = httpUtil.post(postUrl, mapPara);
            if (!responseInfo.getStatus().matches(".*200.*") || !responseInfo.getResBodyInfo().matches(".*true.*")) {
                logUtil.error("case上报qdv-online失败：" + caseIdentify);
                logUtil.error("上报qdv-online信息：" + mapPara);
                logUtil.error("上报qdv-online失败原因：" + responseInfo.getResBodyInfo().split("\"resMessage\":\"")[1].split("\".*")[0]);
            }
        } catch (Exception e) {
            logUtil.error(e.getMessage());
        }
    }
}
