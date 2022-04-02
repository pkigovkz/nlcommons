package kz.gov.pki.osgi.layer.exception;

import lombok.Getter;

public enum CommonFailure implements Failure {

    GENERAL_ERROR("General error."),
    INVOCATION_ERROR("An invoked method threw an exception.");
    
    @Getter
    private String message;
    
    private CommonFailure(String message) {
        this.message = message;
    }
    
    public String getCode() {
       return this.name();
    }
    
}
