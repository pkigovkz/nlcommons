package kz.gov.pki.osgi.layer.exception;

public class CommonException extends Exception {
	
	private static final long serialVersionUID = -8856272478481113600L;

	public static final String COMMON_INVOKER_EXCEPTION = "COMMON_INVOKER_EXCEPTION";
	
	private String errorCode = COMMON_INVOKER_EXCEPTION;

	public CommonException() {
		super();
	}
	
	public CommonException(String errorCode) {
		super();
		this.errorCode = errorCode;
	}
	
	public CommonException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public CommonException(String errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	public CommonException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public CommonException(String errorCode, String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.errorCode = errorCode;
	}
	
	public String getErrorCode() {
		return this.errorCode;
	}

}
