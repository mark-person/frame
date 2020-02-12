/**
 * 
 */
package com.ppx.cloud.example.demo.dynamic;

import org.springframework.stereotype.Component;

/**
 * @author mark
 * @date 2020年2月11日
 */

@Component
public class MyAppcation implements IActivityAppcation {
	
	
	
	@Override
	public void doRun(ApplicationView applicationView) {
		
		
		System.out.println("000000001" + Test.ABC);
		System.out.println("0009" + applicationView.getId());
	}
	
	@Override
    public String getInterfaceCHName() {
        return "自定义接口名称";
    }
}
