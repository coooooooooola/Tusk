package dispatch.run;

import com.meituan.ep.leo.ift.IftConf;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by weiyinglin on 2019/4/23.
 */
public class MethodEnvFilter  implements IMethodInterceptor {

    private List<IMethodInstance> methodsToTest = null;

    public boolean isQualified(ITestNGMethod iTestNGMethod) {
        boolean isQualified = true;
        Method m = iTestNGMethod.getConstructorOrMethod().getMethod();
        Test testAnno = m.getAnnotation(Test.class);
        String[] groups = testAnno.groups();
        List<String> groupList = Arrays.asList(groups);
        if (canNotRunIncurrentEnv(groupList)) {
            isQualified = false;
            return  isQualified;
        }
        return isQualified;
    }

    private boolean canNotRunIncurrentEnv( List<String> groupList) {
        String currentEnv = IftConf.RunEnv;
        for(int i=0;i<groupList.size();i++){
            if(groupList.get(i).toLowerCase().contains(("notin"+currentEnv).toLowerCase())){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods,
                                           ITestContext iTestContext) {
        methodsToTest=null;
        if (methodsToTest == null) {
            SortedMap<String, IMethodInstance> sortedMap = new TreeMap<String, IMethodInstance>();
            List<String> ignoredMethods = new ArrayList<String>();
            for (Iterator<IMethodInstance> it = methods.iterator(); it.hasNext();) {
                IMethodInstance iMethodInstance = it.next();
                ITestNGMethod m = iMethodInstance.getMethod();
                String methodName = m.getConstructorOrMethod().getName();
                String className = m.getTestClass().getRealClass().getName();
                if (isQualified(iMethodInstance.getMethod())) {
                    String sortKey = className + "_" + methodName;
                    sortedMap.put(sortKey, iMethodInstance);
                } else {
                    ignoredMethods.add(methodName + "(" + className + ")");
                }
            }
            List<IMethodInstance> rtMethods = new ArrayList<IMethodInstance>(
                    sortedMap.values());

            methodsToTest = rtMethods;
        }
        return methodsToTest;
    }
}
