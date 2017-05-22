/**
 * 
 */
package mblog.velocity.handler;

import org.apache.commons.lang.StringUtils;

/**
 * @author langhsu
 *
 */
public class BaseHandler {
	public int toInt(Object obj) {
		int i = 0;
		if (obj != null && StringUtils.isNumeric(obj.toString())) {
			i = ((Number) obj).intValue();
		}
		return i;
	}

	public long toLong(Object obj) {
		long i = 0;
		if (obj != null && StringUtils.isNumeric(obj.toString())) {
			i = ((Number) obj).longValue();
		}
		return i;
	}
}
