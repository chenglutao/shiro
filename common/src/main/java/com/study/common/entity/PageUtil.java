package com.study.common.entity;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class PageUtil {

    public static final String pageSize = "10";

    public static final String pageNum = "0";


    /****
     * 得到行
     *
     * @param req
     * @return
     */
    public static RowBounds getRowBounds(Map<String, Object> req) {

        // =====================================
        int limit = Integer.valueOf(pageSize);
        if (req.get("pageSize") != null && !req.get("pageSize").toString().isEmpty()) {
            try {
                limit = Integer.valueOf(req.get("pageSize").toString());
            } catch (NumberFormatException e) {
                log.error("输入的显示行数不是数字【" + req.get("pageSize") + "】", e);
            }
        }

        // ============
        int offset = Integer.valueOf(pageNum);
        if (req.get("curPage") != null && !req.get("curPage").toString().isEmpty()) {
            try {
                int curPage = Integer.valueOf(req.get("curPage").toString());
                offset = curPage;
            } catch (NumberFormatException e) {
                log.error("输入页号不是整数【" + req.get("curPage") + "】", e);
            }
        }

        return new RowBounds(offset, limit);
    }

    /*****
     * 填充对象
     *
     * @param data
     * @param totalCount
     * @return
     */
    public static Map<String, Object> populate(List<? extends Object> data, int totalCount) {
        Map<String, Object> retVal = new HashMap<String, Object>();
        retVal.put("data", data);
        retVal.put("totalCount", totalCount);
        return retVal;
    }

    /*****
     * 判断Map中是否存在KEY 值
     *
     * @param req
     * @param paramName
     * @return
     */
    public static boolean isEmpty(Map<String, Object> req, String paramName) {
        return req.get(paramName) == null || req.get(paramName).toString().isEmpty();
    }

    /*****
     * 判断Map中是否存在KEY 值
     *
     * @param req
     * @param paramName
     * @return
     */
    public static boolean isNotEmpty(Map<String, Object> req, String paramName) {
        return req.get(paramName) != null && !req.get(paramName).toString().isEmpty();
    }

    /***
     * 将属性添加到map中
     *
     * @param key
     * @param msg
     * @return
     */
    public static Map<String, String> populate(String key, String msg) {
        Map<String, String> repMap = new HashMap<String, String>();
        repMap.put("key", key);
        repMap.put("msg", msg);
        return repMap;
    }

    /****
     * 填充错误信息到map中
     *
     * @param msg
     * @return
     */
    public static Map<String, String> populateFail(String msg) {
        return populate("9999", msg);
    }

    /****
     * 填充成功信息到map中
     *
     * @param msg
     * @return
     */
    public static Map<String, String> populateSuccess(String msg) {
        return populate("0000", msg);
    }

}
