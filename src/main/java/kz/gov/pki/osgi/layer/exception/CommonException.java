package kz.gov.pki.osgi.layer.exception;

import lombok.Getter;

public class CommonException extends RuntimeException {

    private static final long serialVersionUID = -8856272478481113600L;

    @Getter
    private final Failure failure;

    public CommonException(Failure failure) {
        super(failure.getMessage());
        this.failure = failure;
    }

    public CommonException(Failure failure, Throwable cause) {
        super(failure.getMessage(), cause);
        this.failure = failure;
    }

}
