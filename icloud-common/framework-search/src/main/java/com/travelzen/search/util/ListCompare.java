/**
 * ListCompare.java
 *
 * Copyright 2011 easou, Inc. All Rights Reserved.
 *
 * created by jay 2011-5-31
 */
package com.travelzen.search.util;

import java.util.List;

public class ListCompare {

    final List<String> shortList;

    final List<String> longList;

    int offset = 0;

    public ListCompare(List<String> shortList, List<String> longList) {
        this.shortList = shortList;
        this.longList = longList;
    }

    public int isContains() {
        if (shortList.size() > longList.size() || null == shortList
                || null == longList) {
            return 0;
        }
        boolean ok = false;
        for (; offset < longList.size() - shortList.size() + 1; offset++) {
            if (longList.get(offset).equalsIgnoreCase(shortList.get(0))) {
                // 首相同字，进行逐term比较
                if (equalsAtOffset()) {
                    ok = true;
                    break;
                }
            }
        }
        if (!ok) {
            return 0;
        }
        if (offset == 0 && shortList.size() == longList.size()) {
            return 2;
        } else {
            return 1;
        }
    }

    /**
     * 短串是否在起始位与长串有相等的地方
     * 
     * @return
     */
    private boolean equalsAtOffset() {
        for (int i = 0; i < shortList.size(); i++) {
            if (!shortList.get(i).equals(longList.get(i + offset))) {
                return false;
            }
        }
        return true;
    }
}
