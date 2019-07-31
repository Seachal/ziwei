package framework.ioc;

/**
 * 一些系统常量的定义
 */
public class FWConst {

	//防重复点击的间隔时间
	public static long intervalTime = 500;
	//App的英文名称（建议，当名字过长时用简称，例如：wallpaper）
	public static String appEnName= "BeiKeLive";
	//打印日志
	public static boolean logable=true;
	//打印日志时打印行号
	public static boolean logline=true;

	public static boolean net_error_try=false;
	
	public static boolean auto_inject=true;
	
	public static int DATABASE_VERSION=1;
	
	//adapter 的分页相关
	// adapter 的分页相关
	public static String netadapter_page_no = "index";
	public static int netadapter_step_default = 10;
	public static int page_no_start = 1;
	public static String netadapter_no_more = "没有更多了";
	public static String netadapter_step = "step";
	public static String netadapter_timeline="timeline";
	public static String netadapter_json_timeline="id";
	
	public static String[] ioc_instal_pkg=null;
	
	//图片缓存相关
	public static String image_cache_dir="dhcache";
	public static int image_cache_num=12;
	public static Boolean image_cache_is_in_sd=false;
	public static long image_cache_time=100000l;
	
	//网络访问返回数据的格式定义	
	public static String response_success = "success";
	public static String response_msg = "msg";
	public static String response_data = "data";
	public static String response_status = "status";
	public static String response_error = "error";
	public static int net_pool_size=10;

	/**
	 * 用于绑定控件时的修复（fix方法）返回，表示值已被处理，bindView的时候就直接跳过
	 */
	public static final String ALREAD_DEAL_THE_VALUE = "**AlreadDealTheValue**";
}
