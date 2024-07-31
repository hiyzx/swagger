package org.zero.swagger.doc.kafka;

import io.swagger.config.Scanner;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author yzx
 * @since  2024/7/16
 */
public class KafkaConsumerScanner implements Scanner {

	@Resource
	private KafkaConsumerManager kafkaConsumerManager;

	@Override
	public Set<Class<?>> classes() {
		return kafkaConsumerManager.getKafkaClasses();
	}
	
	@Override
	public boolean getPrettyPrint() {
		return false;
	}

	@Override
	public void setPrettyPrint(boolean shouldPrettyPrint) {
	}

}
