package com.bitmaster;

import java.util.LinkedList;

public class StaticData {
    public static StaticData staticData;
    public static StaticData getInstance(){
        if(staticData == null){
            staticData = new StaticData();
            exchangeData = new LinkedList<>();
            listSize = 0;
        }

        return staticData;
    }

    public static LinkedList<ExchangeData> exchangeData;
    public static int listSize;
}
