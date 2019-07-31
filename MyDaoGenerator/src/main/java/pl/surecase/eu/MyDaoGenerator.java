package pl.surecase.eu;

//import de.greenrobot.daogenerator.DaoGenerator;
//import de.greenrobot.daogenerator.Entity;
//import de.greenrobot.daogenerator.Schema;
//greendao 3.1通过注解 makegroject自动生成数据库 此类废弃
public class MyDaoGenerator {
//    public static void main(String args[]) throws Exception {
//        Schema schema = new Schema(6, "laka.live.bean");
//        // 1: 数据库版本号
//        // com.xxx.bean:自动生成的Bean对象会放到/java-gen/com/xxx/bean中
//        schema.setDefaultJavaPackageDao("laka.live.dao");
//        // DaoMaster.java、DaoSession.java、BeanDao.java会放到/java-gen/com/xxx/dao中
//
//        // 上面这两个文件夹路径都可以自定义，也可以不设置
//        initSessionBean(schema);
//        initChatBean(schema); // 初始化Bean了
//        initMusicBean(schema);
//
//        new DaoGenerator().generateAll(schema,"../LaKaTV-Android/app/src/main/java-gen");// 自动创建
//    }
//
//    // 会话表
//    private static void initSessionBean(Schema schema) {
//        Entity bean = schema.addEntity("ChatSession");// 表名
//        bean.addStringProperty("id").primaryKey().index();// 主键，索引
//        bean.addStringProperty("userId");//对方userid
//        bean.addStringProperty("nickName");//对方昵称
//        bean.addStringProperty("avatar");//对方头像
//        bean.addIntProperty("level");//对方用户等级
//        bean.addIntProperty("gender");//对方性别
//        bean.addShortProperty("auth");//对方权限
//        bean.addStringProperty("content");//内容
//        bean.addLongProperty("date");//日期
//        bean.addIntProperty("unreadCnt");//未读条数
//        bean.addIntProperty("type");// 0:未关注的人 1：已关注的人 2：黑名单
//    }
//
//    // 消息表
//    private static void initChatBean(Schema schema) {
//        Entity bean = schema.addEntity("ChatMsg");// 表名
//        bean.setTableName("ChatMsg"); // 可以对表重命名
//        bean.addLongProperty("id").primaryKey().autoincrement().index();// 主键，索引,自增长
//        bean.addStringProperty("sessionId");//会话id
//        bean.addStringProperty("userId");//对方userid
//        bean.addStringProperty("nickName");//对方昵称
//        bean.addStringProperty("avatar");//对方头像
//        bean.addStringProperty("content");//内容
//        bean.addLongProperty("date");//日期
//        bean.addIntProperty("level");//对方用户等级
//        bean.addIntProperty("type"); // 0-text  1-gift
//        bean.addIntProperty("state"); // 0-sending | 1-success | 2-fail
//        bean.addBooleanProperty("isSend");//是否我发送的
//        bean.addBooleanProperty("isUnread");//是否未读
//        bean.addIntProperty("giftId");//礼物Id
//    }
//
//    private static void initMusicBean(Schema schema) {
//        Entity bean = schema.addEntity("MusicInfo");// 表名
//        bean.setTableName("MusicInfo"); // 可以对表重命名
//        bean.addLongProperty("id").primaryKey().index();//音乐id
//        bean.addStringProperty("title");//音乐标题
//        bean.addStringProperty("singer");//歌手名字
//        bean.addStringProperty("url");//音乐下载url
//        bean.addStringProperty("code");//校验码
//        bean.addLongProperty("totalBytes");//文件总字节数
//        bean.addLongProperty("bytesWritten");//文件已经写入的字节数
//        bean.addStringProperty("musicFilePath");//音乐文件地址
//        bean.addStringProperty("lyricsFilePath");//歌词文件地址
//        bean.addIntProperty("downloadState");//下载状态
//        bean.addLongProperty("lastPlayTime");//最后播放时间
//    }
}
