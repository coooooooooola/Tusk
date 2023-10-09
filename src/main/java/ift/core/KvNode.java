package ift.core;

/**
 * Create by swtywang on 9/28/23 4:00 PM
 */
public class KvNode {
    private String key;
    private String value;

    public KvNode(){
        key = "";
        value = "";
    }

    public KvNode(String nodeKey, String nodeValue){
        key = nodeKey;
        value = nodeValue;
    }

    public String getKey(){
        return key;
    }

    public String getValue(){
        return value;
    }

    public void setKey(String nodeKey){
        key = nodeKey;
    }

    public void setValue(String nodeValue){
        value = nodeValue;
    }
}
