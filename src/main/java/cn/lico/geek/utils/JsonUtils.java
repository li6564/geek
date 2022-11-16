package cn.lico.geek.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Json转换工具
 * @author linan
 */
public class JsonUtils {
    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 将输入的Java对象转换为Json字符串
     * @param obj
     * @return
     * @throws IOException
     */
    public static String toJson(Object obj)  {
        try {
            StringWriter stringWriter = new StringWriter();
            JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter);
            mapper.writeValue(jsonGenerator, obj);
            jsonGenerator.close();
            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException("Json转换失败", e);
        }
    }

    /**
     * 将输入的Json字符串转换为Java对象
     * @param jsonStr 待转换的Json字符串
     * @param type 要转换成的Java对象的类型
     * @return
     */
    public static <T> T toBean(String jsonStr, Class<T> type) {
        try {
            return mapper.readValue(jsonStr, type);
        } catch (Exception e) {
            throw new RuntimeException("Json转换失败", e);
        }
    }

}
