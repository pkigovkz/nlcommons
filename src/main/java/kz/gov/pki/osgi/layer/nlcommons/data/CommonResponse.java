package kz.gov.pki.osgi.layer.nlcommons.data;

import kz.gov.pki.osgi.layer.exception.Failure;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author aslan
 * 
 */
@Data
@Builder
public class CommonResponse<T> {
    
    @Builder.Default
    @Setter(AccessLevel.PRIVATE)
    private boolean status = false;
    @Getter(AccessLevel.NONE)
    private Failure failure;
    private String details;
    private T body;
    
    public String getCode() {
        return failure.getCode();
    }
    
    public String getMessage() {
        return failure.getMessage();
    }
    
}
