/**
 * 
 */
package com.ppx.cloud.example.demo.dynamic;

import javassist.ClassPool;
import javassist.CtClass;

/**
 * @author mark
 * @date 2020年2月12日
 */

public class ReClassPool extends ClassPool {
	
	
	@Override
    public CtClass removeCached(String classname) {
        return (CtClass)classes.remove(classname);
    }
}
