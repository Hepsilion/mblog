package mblog.velocity.directive;

import java.io.IOException;

import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import mblog.velocity.BaseDirective;
import mblog.velocity.handler.RenderHandler;

/**
 * 数字处理
 * - 大于1000的显示1k
 * - 大于10000的显示1m
 * Created by langhsu on 2015/10/8.
 */
public class NumberDirective extends BaseDirective {
    @Override
    public String getName() {
        return "num";
    }

    @Override
    public int getType() {
        return LINE;
    }

	@Override
	public void initBean() {
	}

	@Override
	public boolean render(RenderHandler handler)
			throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException {
		long value = handler.getLongParameter(0);
		Object out = value;

        if (value > 1000) {
            out = value / 1000 + "k";
        } else if (value > 10000) {
            out = value / 10000 + "m";
        }
        handler.write(out.toString());
		return true;
	}
}
