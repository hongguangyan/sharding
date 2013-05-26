package com.simple.sharding.exception;

/**
 * Sharding组件的异常类
 *
 * @author: wuyu
 * @since: 12-8-11 下午4:12
 * @version: 1.0.0
 */
public class ShardingException  extends Exception {

	private static final long serialVersionUID = 1793760050084714190L;
    
	public ShardingException() {
		super();
	}

	public ShardingException(String msg) {
		super(msg);
	}

    public ShardingException(String msg,String errorMeg) {
		super(msg+errorMeg);
	}

	public ShardingException(String msg, Throwable t) {
		super(msg, t);
	}

	public ShardingException(Throwable t) {
		super(t);
	}

    
}
