package com.example.school.silverproductivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/15.
 */

public class AttractionLoader {
    static List<Attraction> attractionList = new ArrayList<Attraction>();

    public static void loadAttraction(Attraction a)
    {
        if(attractionList != null)
        {
            attractionList.add(a);
        }
    }

    public static void clearAttraction()
    {
        attractionList.clear();
    }

    public static List<Attraction> getAttractionList()
    {
        return attractionList;
    }
}
