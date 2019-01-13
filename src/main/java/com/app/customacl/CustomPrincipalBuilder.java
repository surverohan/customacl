package com.app.customacl;

import java.util.Map;

import javax.security.sasl.SaslServer;

import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.security.auth.AuthenticationContext;
import org.apache.kafka.common.security.auth.KafkaPrincipal;
import org.apache.kafka.common.security.auth.KafkaPrincipalBuilder;
import org.apache.kafka.common.security.auth.SaslAuthenticationContext;

import lombok.extern.slf4j.Slf4j;

/**
 * The CustomPrincipalBuilder program provide SASL PLAIN PlainLoginModule which read value 
 * from AuthenticationContext to create KafkaPrincipal..
 * @author Rohan Surve
 * @version 1.0
 * @since 2018-11-26
 */
@Slf4j
public class CustomPrincipalBuilder implements KafkaPrincipalBuilder {

	public void configure(Map<String, ?> map) {
		
		log.info("CustomPrincipalBuilder configuration --> " + map);

	}

	
	public void close() throws KafkaException {
		log.info("CustomPrincipalBuilder No Action to Take for CLOSE........ " );

	}

	@Override
	public KafkaPrincipal build(AuthenticationContext context) {

		try {
			log.info("AuthenticationContext value --> " + context);
			if (context instanceof SaslAuthenticationContext) {
				SaslAuthenticationContext saslAuthenticationContext = (SaslAuthenticationContext) context;
				log.info("listener name.." + saslAuthenticationContext.listenerName());
				SaslServer saslServer = saslAuthenticationContext.server();
				log.info("******  saslServer name.." + saslServer.getAuthorizationID());
				return new CustomPrincipal(KafkaPrincipal.USER_TYPE, saslServer.getAuthorizationID());

			}
			throw new KafkaException("Failed to build principal due to "+context+" is not of  type SaslAuthenticationContext ");

		} catch (Exception e) {
			throw new KafkaException("Failed to build principal due to: ", e);
		}
	}
}
