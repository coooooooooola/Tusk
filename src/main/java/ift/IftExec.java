package ift;

import dispatch.DispatchConf;
import dispatch.ExecTask;
import dispatch.run.TestRunInfo;
import dispatch.testcase.ICase;
import ift.autocreate.IftDataFileCase;
import toolkit.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by swtywang on 9/28/23 2:59 PM
 */
public class IftExec {

    private ExecTask exec;
    private TestRunInfo runInfo;
    private List<ICase> caseList;
    private IftDataFileCase dataCase;
    private LogUtil log= LogUtil.getLogger(IftExec.class);//日志记录

    /**
     * 构造函数
     */
    public IftExec() {
        //清空临时目录
        DispatchConf.delTmpPath();
        IftConf.delTmpPath();
        exec = new ExecTask();
        runInfo = new TestRunInfo();
        caseList = new ArrayList<>();//用例列表
        dataCase = new IftDataFileCase();
        dataCase.setIftTaskName("ITest");
        dataCase.setListener(IftConf.AnnotationListenerClassName);
        dataCase.setListener(IftConf.CaseListenerClassName);
        dataCase.setListener(IftConf.MethodEnvFilterClassName);
    }

}
