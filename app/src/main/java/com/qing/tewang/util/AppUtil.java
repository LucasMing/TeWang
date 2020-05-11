package com.qing.tewang.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public abstract class AppUtil {

    private static final String APP_KEY = "A%Ah8Peq&y##a^uR";


    // MARK: - 生成签名
    protected static String createSign(String characterEncoding, SortedMap<String, Object> parameters) {
        StringBuffer sb = new StringBuffer();


        Set es = parameters.entrySet();

        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v);
            }
        }
        sb.append("appKey=" + APP_KEY);
        String sign = StringUtils.getSha1(sb.toString());
        return sign;
    }


    public abstract Map<String, Object> signParam(Map<String, Object> params);
}
