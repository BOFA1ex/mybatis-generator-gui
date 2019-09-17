package ${packageValue};

import lombok.Data;

/**
* @author
* @version 1.0
* @since ${date}
*/
@Data
public class JsonBean {

    private String code;
    private String message;
    private Object data;

    public static final JsonBean success = new JsonBean("200");

    public JsonBean() {
        this("200", null);
    }

    public JsonBean(String code) {
        this(code, null);
    }

    public JsonBean(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public JsonBean fail(String message) {
        return new JsonBean("500", message);
    }
}