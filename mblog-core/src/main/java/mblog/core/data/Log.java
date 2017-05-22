/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.core.data;

import java.util.Date;

/**
 * 
 * 事件日志
 * 
 * @author langhsu
 *
 */
public class Log {
	private long id;
	
	/*
	 * 操作用户
	 */
	private long userId;
	
	/*
	 * 日志类型, @see LogType
	 */
	private int type;
	
	/*
	 * 目标ID
	 */
	private long targetId;
	
	/*
	 * IP 地址
	 */
	private String ip;
	
	/*
	 * 操作时间
	 */
	private Date created;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
}
