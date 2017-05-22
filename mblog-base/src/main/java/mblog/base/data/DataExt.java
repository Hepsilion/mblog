/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.base.data;

import mtons.modules.pojos.Data;

import java.util.ArrayList;

/**
 * @author langhsu on 2015/8/15.
 */
public class DataExt {

    private int code; // 处理状态：0: 成功
    private String message;
    private Object data; // 返回数据
    private ArrayList<Button> links = new ArrayList<>();

    private DataExt(int code, String message, Object data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 处理成功，并返回数据
     * @param data
     * @return
     */
    public static final DataExt success(Object data){
        return new DataExt(Data.CODE_SUCCESS, "操作成功", data);
    }

    /**
     *
     * @param message
     * @return
     * @deprecated with 1.0.3
     */
    public static final DataExt success(String message){
        return new DataExt(Data.CODE_SUCCESS, message, Data.NOOP);
    }

    public static final DataExt success(String message, Object data){
        return new DataExt(Data.CODE_SUCCESS, message, data);
    }

    /**
     * 处理失败，并返回数据（一般为错误信息）
     * @param code
     * @return
     */
    public static final DataExt failure(int code, String message){
        return new DataExt(code, message, Data.NOOP);
    }

    public static final DataExt failure(String message){
        return failure(Data.CODE_FAILURED, message);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }

    public DataExt addLink(String link, String text) {
        links.add(new Button(link, text));
        return this;
    }

    public ArrayList<Button> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Button> links) {
        this.links = links;
    }

    public String toString() {
        return "{code:\"" + code + "\", message:\"" + message + "\", data:\"" + data.toString() + "\"}";
    }

    public class Button {
        private String text;
        private String link;

        public Button(String link, String text) {
            this.link = link;
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
