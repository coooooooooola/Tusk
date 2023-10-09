package ift.autocreate;

import java.util.ArrayList;

/**
 * Created by liushanshan on 2018/3/2.
 */
public class ThriftCaseInfo {
    private String appKey;  //对应的appkey
    private ArrayList<String> caseName = new ArrayList<>();
    private ArrayList<String> caseAuthor = new ArrayList<>();
    private ArrayList<String> caseDescription = new ArrayList<>();
    private ArrayList<String> caseMethod = new ArrayList<>();
    private ArrayList<ArrayList<Object[]>> caseParameter = new ArrayList<>();
    private ArrayList<String> caseExpect = new ArrayList<>();

    public void setAppKey(String appKey) {this.appKey = appKey;}
    public String getAppKey() {return this.appKey;}
    public void setCaseName(String caseName) {this.caseName.add(caseName);}
    public ArrayList<String> getCaseName() {return this.caseName;}
    public void setCaseAuthor(String caseAuthor) {this.caseAuthor.add(caseAuthor);}
    public ArrayList<String> getCaseAuthor() {return this.caseAuthor;}
    public void setCaseDescription(String caseDescription) {this.caseDescription.add(caseDescription);}
    public ArrayList<String> getCaseDescription() {return this.caseDescription;}
    public void setCaseMethod(String caseMethod) {this.caseMethod.add(caseMethod);}
    public ArrayList<String> getCaseMethod() {return this.caseMethod;}
    public void setCaseParameter(ArrayList<Object[]> Objects) {this.caseParameter.add(Objects);}
    public ArrayList<ArrayList<Object[]>> getCaseParameter() {return this.caseParameter;}
    public void setCaseExpect(String caseExpect) {this.caseExpect.add(caseExpect);}
    public ArrayList<String> getCaseExpect() {return this.caseExpect;}

    public void executeAllMethod() {}
}
