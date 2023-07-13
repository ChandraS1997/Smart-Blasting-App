package com.mineexcellence.sblastingapp.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayUtil {

    public static ArrayList<Integer> convertStringIntArray(String value) {
        if(TextUtils.isEmpty(value)){
            ArrayList<Integer> arrayList=new ArrayList<>();
            return arrayList;
        }
        ArrayList<String> items = new ArrayList<String>(Arrays.asList(value.split(",")));
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (String s : items) {
            integerArrayList.add(Integer.valueOf(s));
        }
        return integerArrayList;
    }

    public  static  ArrayList<String> searchArrayItem(ArrayList<String> list, String searchTxt){
        ArrayList<String> temp = new ArrayList();
        for(String item: list){
            if(item.contains(searchTxt)|| item.toLowerCase().contains(searchTxt) ){
                temp.add(item);
            }
        }
        return temp;
    }

    public static List<?> removeDuplicates(List<?> list)
    {
        int count = list.size();

        for (int i = 0; i < count; i++)
        {
            for (int j = i + 1; j < count; j++)
            {
                if (list.get(i).equals(list.get(j)))
                {
                    list.remove(j--);
                    count--;
                }
            }
        }
        return list;
    }

    public static String getStringFromIntArray(List<Integer> list){
        if(list==null)
            return "";
        if(list.size()==0)
            return "";
        String joinedList = list.stream().map(String::valueOf).collect(Collectors.joining(","));
        return joinedList;
    }
}
