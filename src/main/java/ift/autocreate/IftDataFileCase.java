package ift.autocreate;

import com.meituan.ep.leo.dispatch.testcase.CreateXmlFile;
import com.meituan.ep.leo.dispatch.testcase.SuperCase;
import com.meituan.ep.leo.ift.IftConf;
import com.meituan.ep.leo.ift.testcase.format.FormatCase;
import com.meituan.ep.leo.ift.testcase.format.ReadCaseFromExcel;
import com.meituan.ep.leo.toolkit.util.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 数据文件类型的测试用例
 *
 */
public class IftDataFileCase extends SuperCase{
	
	private LogUtil log =LogUtil.getLogger(IftDataFileCase.class);//日志记录
	//任务名称
	private String taskName;//测试任务名称
	//根据用例数据文件，创建java、xml文件相关配置信息
	private String allReportPath ;// html、excel测试报告存储的上级目录
	private String excelReportName;//excel格式测试报告的名称
    private String listenerPath;
	
	private CreateJavaFile createJavaFile;//生成java文件
	private CreateXmlFile createXmlFile;//生成xml文件
	private CreateThriftJavaFile createThriftJavaFile;//生成thrift java文件
	
	/**
	 * 构造函数
	 */
	public   IftDataFileCase() {
		super();
		createJavaFile = new CreateJavaFile();
		createXmlFile = new CreateXmlFile();
		createThriftJavaFile = new CreateThriftJavaFile();
		setIftTaskName("未命名测试任务");
		excelReportName="未命名接口测试";
        listenerPath = "";
	}
	
	/**
	 * 构造函数
	 * @param threadCont 线程数
	 * @param setParallel 设置多线程执行方式methods;tests;classes
	 */
	public   IftDataFileCase(String setParallel,int threadCont) {
		super();
		if(setParallel.equals("methods")||setParallel.equals("tests")||setParallel.equals("classes")){
			createJavaFile = new CreateJavaFile();
			createXmlFile = new CreateXmlFile(threadCont,setParallel);
            createXmlFile.addListener("");
			setIftTaskName("未命名测试任务");
			excelReportName="未命名接口测试";
            listenerPath = "";
		}else{
			log.error("多线程不支持："+setParallel+"方法");
		}


	}

	public List<String> getCaseList() {
		createXmlFile();
		return this.xmlPathNameList;
	}
	/**
	 * 添加用例
	 * @param casePath 用例路径 必填
	 * @param sheetName Excel的sheet表名 可选
	 * @param caseName  用例名称 必填
	 * @param cls		执行用例的类 必填
	 * @param method    类中的方法 必填
	 */
	public void addCase(String casePath, String sheetName, String caseName,
			Class<?> cls,String method) {
		if (StringUtil.IsNullOrEmpty(casePath) || StringUtil.IsNullOrEmpty(caseName)) {
			return;
		}//任一项空值或长度小于1时，不做处理
		JavaCaseInfo javaCaseInfo = new JavaCaseInfo();
        ReadCaseFromExcel readCase = new ReadCaseFromExcel(casePath,sheetName);
		//读取用例
		FormatCase formatcase=new FormatCase();
		formatcase.FormatCaseFromObj(casePath,sheetName);
		//存储用例实体列表信息
		javaCaseInfo.setAllCase(formatcase.getTestCase());
		//获取测试集名称作为输出的测试报告名称
		this.excelReportName="测试报告_"+formatcase.getCasesetName();
		//存储javaCaseInfo其余信息
		javaCaseInfo.setPackageName(IftConf.PackageName);
		javaCaseInfo.setJavaFileName(caseName.replace(".", "_"));
		javaCaseInfo.setJavaSavePath(IftConf.JavaPath);
		javaCaseInfo.setCaseDataPathName(casePath);
		javaCaseInfo.setCaseDataSheetName(sheetName);
		javaCaseInfo.setExcelReportSheetName(sheetName);
		javaCaseInfo.setExcelReportName(this.excelReportName);
		javaCaseInfo.setExcelReportPath(getReportPath());
		javaCaseInfo.setCls(cls);
		javaCaseInfo.setMethod(method);
        javaCaseInfo.setCtrlName(readCase.readCtrlName());
        javaCaseInfo.setApiName(readCase.readApiName());
        javaCaseInfo.setApiAuthor(readCase.readApiAuthor());
		//创建java文件 失败则返回
		if (!createJavaFile.creatJavaSrcFile(javaCaseInfo)){
			return;
		}
		//编译java文件为class 失败则返回
		if(!CompilerUtil.dynamicCompiler(javaCaseInfo.getJavaSavePath()+javaCaseInfo.getJavaFileName()+".java", 
				IftConf.DistPath, IftConf.LibPath,IftConf.JarFile)){
			return;
		}
		//添加到xmlSuite	
		try {
//            createXmlFile.addListener(IftConf.AnnotationListenerClassName);
			createXmlFile.addJavaCase(caseName.replace(".", "_"), Class.forName(javaCaseInfo.getPackageName()+"."+javaCaseInfo.getJavaFileName()));
			log.info("添加测试集："+javaCaseInfo.getJavaFileName()+"成功");
		} catch (ClassNotFoundException e) {
			log.error("添加测试集："+javaCaseInfo.getJavaFileName()+"失败");
			log.error(e.getMessage());
		}
	}
	
	/**
	 * 添加用例
	 * @param casePath 用例路径 必填
	 * @param caseName  用例名称 必填
	 * @param cls		执行用例的类 必填
	 * @param method    类中的方法 必填
	 */
	public void addCase(String casePath, String caseName,Class<?> cls,String method) {
		addCase(casePath,"TestCase",caseName,cls,method);
	}

    public void addCase(String pkgAndClsName){
        if(   pkgAndClsName == null || pkgAndClsName.equals("")){
            return;
        }
        String[] className = pkgAndClsName.split("\\.");
        try {
            createXmlFile.addJavaCase(className[className.length - 1], pkgAndClsName);
            log.info("添加测试集："+ pkgAndClsName +"成功");
        } catch (Exception e){
            log.error("添加测试集："+ pkgAndClsName +"失败");
            log.error(e.getMessage());
        }
//        createXmlFile.addClassToxml(pkgAndClsName, className[className.length - 1]);
//        createXmlFile.addClassToXmlTest("com.movie.bbb.TestMerge", "TestMerge");
    }

	public void addThriftCase(String pkgAndClsName) {
		try {
			//获取要执行的对象方法，按规定为executeAllMethod
			Class testClass = Class.forName(pkgAndClsName);
			Method mtd = testClass.getMethod("executeAllMethod");

			//使用invoke调用这个对象的executeAllMethod方法
			Object classInstance = testClass.newInstance();
			mtd.invoke(classInstance);
			//依次获取对应的参数，获取appKey
			mtd = testClass.getMethod("getAppKey");
			String appKey = (String)mtd.invoke(classInstance);
			//获取case name
			mtd = testClass.getMethod("getCaseName");
			ArrayList<String> caseName = (ArrayList<String>)mtd.invoke(classInstance);
			//获取case author
			mtd = testClass.getMethod("getCaseAuthor");
			ArrayList<String> caseAuthor = (ArrayList<String>)mtd.invoke(classInstance);
			//获取case description
			mtd = testClass.getMethod("getCaseDescription");
			ArrayList<String> caseDescription = (ArrayList<String>)mtd.invoke(classInstance);
			//获取case method
			mtd = testClass.getMethod("getCaseMethod");
			ArrayList<String> caseMethod = (ArrayList<String>)mtd.invoke(classInstance);
			//获取case expect
			mtd = testClass.getMethod("getCaseExpect");
			ArrayList<String> caseExpect = (ArrayList<String>)mtd.invoke(classInstance);
			//获取case parameters
			mtd = testClass.getMethod("getCaseParameter");
			ArrayList<ArrayList<Object[]>> caseParams = (ArrayList<ArrayList<Object[]>>)mtd.invoke(classInstance);

			if (null == caseName || null == caseAuthor || null == caseDescription || null == caseMethod || null == caseExpect || null == caseParams ) {
				log.error("获取生成thrift case的依赖参数失败: "+pkgAndClsName);
				return;
			}
			if (caseName.size() != caseAuthor.size() || caseAuthor.size() != caseDescription.size() || caseDescription.size() != caseMethod.size() || caseMethod.size() != caseExpect.size() ||
					caseExpect.size() != caseParams.size()) {
				log.error("生成thrift case的依赖参数各项数量不全部相等，请检查case: "+pkgAndClsName);
				return;
			}
			String[] appKeys = IftConf.appKey.split(",");
			int appKeyIndex = -1;
			for (int i = 0; i < appKeys.length; i++) {
				if (appKeys[i].equals(appKey)) {
					appKeyIndex = i;
				}
			}
			if (0 > appKeyIndex) {
				log.error("生成thrift case的appKey没有在ApiTestEntry中注册相应信息，请检查代码 "+appKey);
			}
			String thriftService = IftConf.thriftService.split(",")[appKeyIndex];
			String thriftPackageName = IftConf.thriftPackageName.split(",")[appKeyIndex];
			String thriftJavaFileName = testClass.getSimpleName() + "Test";
            //组装要生成thrift case的模板文件数据
			Map<String, Object> data = new TreeMap<String, Object>();
			data.put("packageName", IftConf.ThriftPackageName);
			data.put("javaFileName", thriftJavaFileName);
			List<String> importString = new ArrayList<>();
			importString.add(thriftPackageName + ".*");
			data.put("appKey", appKey);
			data.put("thriftService", thriftService);
			data.put("env", IftConf.RunEnv);
            //准备每个case要用到的具体生成信息
			List<Map<String, String>> configMap = new ArrayList<>();
			Map<String, ArrayList<Object[]>> caseParaMap = new HashMap();
			for (int i = 0; i < caseName.size(); i++) {
				Map<String, String> config = new TreeMap<>();
				config.put("ctrlName", thriftService+"."+caseMethod.get(i));
				config.put("apiAuthor", caseAuthor.get(i));
				config.put("description", caseDescription.get(i));
				config.put("caseName", caseName.get(i));
				config.put("caseMethod", caseMethod.get(i));
				config.put("caseExpect", caseExpect.get(i));
				//处理每个case要用到的parameter
				caseParaMap.put(caseName.get(i), caseParams.get(i));
				String para = "";
				String type = "";
				ArrayList<Object[]> Objects = caseParams.get(i);
				for (int j = 0; j < Objects.size(); j++) {
					type = Objects.get(j)[0].toString();
					if (Objects.get(j)[0].toString().contains(".")){
						//如果有.说明是全路径包名，加到import列表里
						importString.add(Objects.get(j)[0].toString());
						type = Objects.get(j)[0].toString().split(".")[Objects.get(j)[0].toString().split(".").length-1];
					}
					if (isDepePara(Objects.get(j)[1])) {
						para += getDepeParaString(type, Objects.get(j)[1])+",";
					}else {
					    para += "("+type+")getCasepara(\""+thriftJavaFileName+"\",\""+caseName.get(i)+"\","+j+")[1],";
					}
				}
				if (para.length() > 0) {
				para = para.substring(0, para.length()-1);
				}
				config.put("caseParameter", para);
				configMap.add(config);
			}
			data.put("configMaps", configMap);
			//importInfo去重
			HashSet h = new HashSet(importString);
			importString.clear();
			importString.addAll(h);
			data.put("importInfos", importString);
			IftConf.ThriftParameters.put(thriftJavaFileName, caseParaMap);
            //创建对应的thrift java case，如果失败就返回
			if (!createThriftJavaFile.createThriftJava(thriftJavaFileName, data)) {
				return;
			}
			//编译java文件为class 失败则返回
			if(!CompilerUtil.dynamicCompiler(IftConf.ThriftJavaPath + thriftJavaFileName + ".java",
					IftConf.DistPath, IftConf.LibPath, IftConf.JarFile)){
				return;
			}
			//添加到xmlSuite
			try {
//            createXmlFile.addListener(IftConf.AnnotationListenerClassName);
				createXmlFile.addJavaCase(thriftJavaFileName, Class.forName(IftConf.ThriftPackageName+"."+thriftJavaFileName));
				log.info("添加测试集："+thriftJavaFileName+"成功");
			} catch (ClassNotFoundException e) {
				log.error("添加测试集："+thriftJavaFileName+"失败");
				log.error(e.getMessage());
			}

		} catch (Exception e) {
			log.error("没有找到生成thrift case对应的class文件: "+pkgAndClsName);
			e.printStackTrace();
		}
	}

	public String getDepeParaString(String type, Object value) {
		String para;
		switch (type){
			case "int":
				para = "Integer.parseInt(thriftCasesUtils.getDepeValue(\""+value.toString()+"\"))";
				break;
			case "float":
				para = "Float.parseFloat(thriftCasesUtils.getDepeValue(\""+value.toString()+"\"))";
				break;
			case "long":
				para = "Long.parseLong(thriftCasesUtils.getDepeValue(\""+value.toString()+"\"))";
				break;
			case "short":
				para = "Short.parseShort(thriftCasesUtils.getDepeValue(\""+value.toString()+"\"))";
				break;
			case "double":
				para = "Double.parseDouble(thriftCasesUtils.getDepeValue(\""+value.toString()+"\"))";
				break;
			case "char":
				para = "thriftCasesUtils.getDepeValue(\""+value.toString()+"\").charAt(0)";
				break;
			case "boolean":
				para = "Boolean.valueOf(thriftCasesUtils.getDepeValue(\""+value.toString()+"\"))";
				break;
			case "String":
				para = "thriftCasesUtils.getDepeValue(\""+value.toString()+"\")";
				break;
			default:
				para = "("+type+")(Object)thriftCasesUtils.getDepeValue(\""+value.toString()+"\")";
				break;
		}
		return para;
	}

	public boolean isDepePara(Object object) {
		String tmpPara = object.toString();
		Pattern pattern = Pattern.compile("\\$\\{(.*)\\}"); //匹配${}前后可以有字符
		Matcher matcher ;
		matcher = pattern.matcher(tmpPara);
		return matcher.find();
	}

	/**
	 * 设置任务名称 
	 * @param setTaskName
	 */
	public void setIftTaskName(String setTaskName) {
		this.taskName =setTaskName;
		this.createXmlFile.setSuiteName(this.taskName);
		setReportPath(IftConf.ReportPath+this.taskName+"/");// 测试报告存储路径
		this.excelReportName = this.taskName;
		
	}

    public void setListener(String listenerPath){
        if(IftConf.ListenAnnotation == "N"){
            return;
        } else if(listenerPath == "" || listenerPath == null){
            log.error("listenerPath错误: " + listenerPath);
            return;
        }{
            createXmlFile.addListener(listenerPath);
        }
    }
	
	
	/**
	 * 如果路径无效 则测试报告默认保存在 [qtaf/ift/report/任务名称 ]目录下
	 * @param reportPath
	 */
	public void setReportPath(String reportPath){
		if (FileUtil.createDictory(reportPath)) {
			this.allReportPath=reportPath;
			//清空指定的测试报告目录
			FileUtil.delFolder(getReportPath());
		}
	}
	public String getReportPath(){
		return this.allReportPath;
	}
	public String getExcelReportPath() {
		return getReportPath()+this.excelReportName+".xlsx";
	}
	public String getHtmlReportPath() {
		return this.allReportPath+"html";
	}

	public String getTaskName() {
		return taskName;
	}

	/**
	 * 创建java、xml文件 更新xmlPathNameList列表
	 * @return boolean 设置成功返回true
	 */
	private boolean createXmlFile() {
		//创建java、xml文件
		String xmlFilePath = createXmlFile.getXmlFilePath();
		if (FileUtil.isExist(xmlFilePath)) {
			xmlPathNameList.add(xmlFilePath);
			log.info("添加xml文件成功："+xmlFilePath);
			return true;
		}else{
			log.error("添加xml文件失败："+xmlFilePath);
			return false;
		}
	}
}
