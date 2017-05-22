package mblog.base.lang;

/**
 * @author langhsu on 2015/8/19.
 */
public enum EnumPrivacy {
    ALL(-1, "忽略"),
    OPEN(0, "公开"),
    SECRECY(1, "保密");
//	FRIEND(2, "好友"),

    private EnumPrivacy(int index, String text) {
        this.index = index;
        this.text = text;
    }

    private int index;
    private String text;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
