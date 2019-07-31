package framework.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 带注解的字段必须是public
 *click 事件  method(View v)  或者method();
 *
 *longClick 事件 public boolean method(View v)或者method();
 * 
 *itemClick 事件 public void method(AdapterView<?> parent, View view,int position, long id) 或者method(int position, long id)
 * 
 *itemLongClick 事件  public boolean method(AdapterView<?> parent, View view,int position, long id)
 * 
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectView {
	
	//view 的id
	int id() default 0;
	
	//view 的 layout
	int layout() default 0;
	
	//view 在其他view view需要在 类中注入
	String inView() default "";
	
	//点击事件
	String click() default "onClick";
	
	//长按
	String longClick() default "";
	
	//adapterview 的单个item的点击事件
	String itemClick() default "";
	
	//adapterview 的单个对象的长点击
	String itemLongClick() default "";

}
