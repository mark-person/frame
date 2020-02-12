package com.ppx.cloud.example.demo;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.auth.annotation.ActionAuth;
import com.ppx.cloud.base.jdbc.page.Page;
import com.ppx.cloud.base.mvc.ControllerReturn;
import com.ppx.cloud.example.demo.dynamic.ApplicationView;
import com.ppx.cloud.example.demo.dynamic.IActivityAppcation;
import com.ppx.cloud.example.demo.dynamic.MyAppcation;
import com.ppx.cloud.example.demo.dynamic.MyConfigImpl;

import javassist.ClassPool;
import javassist.CtClass;


@ActionAuth
@Controller
public class DemoController {
	
	@Autowired
	private DemoImpl impl;
	
	@Autowired
    private WebApplicationContext context;
	
	
	@Autowired
	private MyConfigImpl myConfigImpl;
	
//	@Autowired
//	private MyAppcation myAppcation;
	
	
	public ModelAndView demo(ModelAndView mv, HttpServletRequest request) {
		// mv.setViewName("demo/demo/demo");
		IActivityAppcation appcation = null;
//		try {
//			ReClassPool pool = new ReClassPool();
//			pool.appendSystemPath();
//			pool.removeCached("com.ppx.cloud.example.demo.dynamic.MyAppcation");
//			
//			pool.insertClassPath("E:\\Git\\frame\\example\\target\\classes");
//			 
//			pool.appendSystemPath();
//			
//			ClassPool pool = ClassPool.getDefault();
//			pool.appendSystemPath();
//			
//			pool.insertClassPath("E:\\Git\\frame\\example\\target\\classes");
//			
//			CtClass cc = pool.get("com.ppx.cloud.example.demo.dynamic.MyAppcation");
//	
//		  	cc.defrost();
//			CtMethod cm = cc.getDeclaredMethod("doRun");
//		    
//		    System.out.println("xxx:" + cm.getName());
//		    
//		    cm.setBody("System.out.println(\"00010\" + Test.ABC);");
//		    
//			
//			// cc1.writeFile();
//			cc.defrost();
//			cc.setName("Pair10"); 
//			
//			appcation = (IActivityAppcation)cc.toClass().newInstance();
//			ApplicationView applicationView = new ApplicationView();
//			applicationView.setId("我是一个值");
//			appcation.doRun(applicationView);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		System.out.println("--------------------------");
		
		
		mv.addObject("data", list(new Page(), new Demo()));
		
		
		
		// >>>>>>>>>>>>test001
		try {

			System.out.println(System.getProperty("user.dir"));
			// 动态编译
			JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
//			int status = javac.run(null, null, null, "-d", "E:\\Git\\frame\\example\\target\\classes",
//			        "D:/temp/MyAppcation.java");
			int status = javac.run(null, null, null, "-d", "E:\\Git\\frame\\example\\target\\classes",
			        "D:/temp/MyAppcation.java");
			System.out.println("xxxxxxxxxoutstatusxx:" + status);
			if (status != 0) {
				System.out.println("没有编译成功！");
			}
			
			
			ClassPool pool = ClassPool.getDefault();
			pool.appendSystemPath();
			
			// pool.insertClassPath("E:\\Git\\frame\\example\\target\\classes");
			CtClass cc = pool.get("com.ppx.cloud.example.demo.dynamic.MyAppcation");
			
			
//			cc.defrost();
			cc.setName("Testxx998");
			
			appcation = (IActivityAppcation)cc.toClass().newInstance();
			ApplicationView applicationView = new ApplicationView();
			applicationView.setId("我是一个值");
			appcation.doRun(applicationView);
			
			
//			Class clz = Class.forName("com.ppx.cloud.example.demo.MyAppcation8", true, this.getClass().getClassLoader());// 返回与带有给定字符串名的类 或接口相关联的 Class 对象。
//			IActivityAppcation a = (IActivityAppcation)clz.newInstance();
//			a.doRun(null);
			
			
			myConfigImpl.postProcessBeforeInitialization(appcation, "myAppcationx");
			
			System.out.println("99999999999999>>>>");
//			IActivityAppcation aaa = (IActivityAppcation)context.getBean(MyAppcation.class);
			IActivityAppcation aaa = (IActivityAppcation)context.getBean("myAppcation");
			aaa.doRun(new ApplicationView());
			
			
			// myAppcation.doRun(null);
			
			
			
//			MyAppcation myAppcation = (MyAppcation)clz.newInstance();
//			myAppcation.doRun(null);
//			Object o = clz.newInstance();
//			Method method = clz.getDeclaredMethod("doRun", ApplicationView.class);// 返回一个 Method 对象，该对象反映此 Class 对象所表示的类或接口的指定已声明方法
//			method.invoke(o, new ApplicationView());// 静态方法第一个参数可为null,第二个参数为实际传参
			
			
//			String n[] = context.getBeanDefinitionNames();
//			for (String string : n) {
//				System.out.println("n:" + string);
//			}
			
			// Object obj = context.getBean("myAppcation");
			
			// localProcessor.postProcessBeforeInitialization(appcation, "myAppcation");
			
			
			
			
			 // myAppcation.doRun(null);
			// myConfigImpl.postProcessBeforeInitialization(clz.newInstance(), "myAppcation");
			
			// System.out.println("xxxxout:" + obj);
			 
			
			
			// 动态执行
//			Class clz = Class.forName("com.redsea.deploy.test.MyTest");// 返回与带有给定字符串名的类 或接口相关联的 Class 对象。
//			Object o = clz.newInstance();
//			Method method = clz.getDeclaredMethod("sayHello");// 返回一个 Method 对象，该对象反映此 Class 对象所表示的类或接口的指定已声明方法
//			String result = (String) method.invoke(o);// 静态方法第一个参数可为null,第二个参数为实际传参
//			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// mv.addObject("insertOrUpdate");
		// myAppcation.doRun(null);
		
		
		return mv;
	}
	
	public Map<String, Object> list(Page page, Demo pojo) {
		
		
		List<Demo> list = impl.list(page, pojo);
		return ControllerReturn.page(page, list);
	}
	 
    public Map<String, Object> insert(Demo pojo) {
		

		
        return impl.insert(pojo);
    }
    
    public Map<String, Object> update(Demo pojo) {
//      try {
//      Thread.sleep(1000);
//  } catch (Exception e) {
//      // TODO: handle exception
//  }
        
        return impl.update(pojo);
    }
    
    public Map<String, Object> get(@RequestParam Integer id) {
        return ControllerReturn.of("pojo", impl.get(id));
    }

    public Map<String, Object> delete(@RequestParam Integer id) {
        return impl.delete(id);
    }

    
    public Map<String, Object> test() {
        return ControllerReturn.of("list", impl.test());
    }
}
