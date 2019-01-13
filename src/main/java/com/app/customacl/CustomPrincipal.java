package com.app.customacl;

import java.security.Principal;

import org.apache.kafka.common.security.auth.KafkaPrincipal;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
/**
 * The CustomPrincipal program provide SASL PLAIN PlainLoginModule 
 * with ACL approval from Zookeeper ACL Lists this class is option where 
 * you can add external ACL Lists to be compared.....
 * @author Rohan Surve
 * @version 1.0
 * @since 2018-11-25
 */
@Slf4j
@ToString
@Getter
public class CustomPrincipal extends KafkaPrincipal  {
    private static final String SEPARATOR = ":";

    private String principalType;
    private String name;

    public CustomPrincipal(String principalType, String name) {
        super(principalType, name);
        // This is option .....
        this.principalType = principalType;
        this.name = name;
        log.info(" CustomPrincipal --> "+this.toString());
    }

    // This is option .....
    // use this only if you have separate comparsion process...
    @Override
    public boolean equals(Object o) {
        log.info(" Value to be compared with --> "+this.toString() );

        if (this == o){
        	return true;
        }
        if (!(o instanceof CustomPrincipal)){
        	return false;
        }

        CustomPrincipal that = (CustomPrincipal) o;
        log.info(" Value to be compared to --> "+that.toString());

        if (!principalType.equals(that.principalType)){
        	return false;
        }

        return name.equals(that.name);
    }

    @Override
    public String toString() {
        return principalType + SEPARATOR + name;
    }

    @Override
    public int hashCode() {
        int result = principalType.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

   
}
