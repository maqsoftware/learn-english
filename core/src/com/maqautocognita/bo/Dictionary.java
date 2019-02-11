package com.maqautocognita.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class Dictionary {

    public String wordType;
    public String synonyms;
    public List<String> definitionList;
    public List<String> exampleList;

    public void addExample(String example) {
        if (null == exampleList) {
            exampleList = new ArrayList<String>();
        }
        exampleList.add(example);
    }

    public void addDefinition(String definition) {
        if (null == definitionList) {
            definitionList = new ArrayList<String>();
        }
        definitionList.add(definition);
    }
}
