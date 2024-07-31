package org.zero.swagger.doc.xxl;

import io.swagger.config.Scanner;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author yzx
 * @since  2024/7/16
 */
public class XxlJobScanner implements Scanner {

	@Resource
	private XxlJobManager xxlJobManager;

	@Override
	public Set<Class<?>> classes() {
		return xxlJobManager.getJobClasses();
	}
	
	@Override
	public boolean getPrettyPrint() {
		return false;
	}

	@Override
	public void setPrettyPrint(boolean shouldPrettyPrint) {
	}

}
