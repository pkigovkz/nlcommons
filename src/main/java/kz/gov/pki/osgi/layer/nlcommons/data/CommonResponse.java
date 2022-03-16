package kz.gov.pki.osgi.layer.nlcommons.data;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * 
 * @author aslan
 * 
 */
@Data
public class CommonResponse<T> {

    public CommonResponse(boolean status) {
        this.status = status;
    }
	
	private @Setter(AccessLevel.PRIVATE) boolean status;
    private String errorCode;
    private String message;
    private T body;
    
}
