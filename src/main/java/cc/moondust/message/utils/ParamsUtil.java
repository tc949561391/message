package cc.moondust.message.utils;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.HashMap;

/**
 * Created by j0 on 2016/8/1.
 */
public class ParamsUtil {
    public static HashMap<String, String> buidParams(FullHttpRequest req) {
        QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
        HashMap<String, String> params = new HashMap<String, String>();
        decoder.parameters().entrySet().forEach(entry -> {
            // entry.getValue()是一个List, 只取第一个元素
            params.put(entry.getKey(), entry.getValue().get(0));
        });
        return params;
    }



}
