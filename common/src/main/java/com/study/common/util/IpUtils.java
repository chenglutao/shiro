package com.study.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Method;

/**
 * @author chenglutao
 * @date 2019-12-27 17:19
 */
@Slf4j
public class IpUtils {
    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     * @param request
     * @return
     * @throws IOException
     */
    public final static String getIpAddress(HttpServletRequest request) throws IOException {

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    public static String getCityInfo(String ip){
        DbSearcher searcher = null;
        try {
            String dbPath = IpUtils.class.getResource("/ip2region.db").getPath();
            File file = new File(dbPath);
            if (!file.exists()) {
                String tmpDir = System.getProperties().getProperty("java.io.tmpdir");
                dbPath = tmpDir + File.separator + "ip.db";
                file = new File(dbPath);
                InputStream resourceAsStream = IpUtils.class.getClassLoader().getResourceAsStream("classpath:ip2region.db");
                if (resourceAsStream != null) {
                    FileUtils.copyInputStreamToFile(resourceAsStream, file);
                }
            }
            int algorithm = DbSearcher.BTREE_ALGORITHM;
            DbConfig config = new DbConfig();
            searcher = new DbSearcher(config, file.getPath());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            Method method = null;
            switch (algorithm) {
                case DbSearcher.BTREE_ALGORITHM:
                    method = searcher.getClass().getMethod("btreeSearch", String.class);
                    break;
                case DbSearcher.BINARY_ALGORITHM:
                    method = searcher.getClass().getMethod("binarySearch", String.class);
                    break;
                case DbSearcher.MEMORY_ALGORITYM:
                    method = searcher.getClass().getMethod("memorySearch", String.class);
                    break;
            }
            if (!Util.isIpAddress(ip)) {
                log.error("Error: Invalid ip address");
            }
            DataBlock dataBlock = (DataBlock) method.invoke(searcher, ip);
            reader.close();
            return dataBlock.getRegion();
        } catch (Exception e) {
            log.error("获取地址信息异常，{}", e.getMessage());
            return StringUtils.EMPTY;
        } finally {
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
