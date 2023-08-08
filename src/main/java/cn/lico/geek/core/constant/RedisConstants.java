package cn.lico.geek.core.constant;

/**
 * @Author：linan
 * @Date：2023/2/25 12:55
 */
public class RedisConstants {
    /**
     * redis中缓存博客分类Key
     */
    public static final String BLOG_SORT = "Blog:Sort";

    /**
     * redis中缓存热门标签Key
     */
    public static final String HOT_TAGS = "Hot_Tags";

    /**
     * redis中缓存系统配置Key
     */
    public static final String WEB_NAVBAR = "Web_Navbar";

    /**
     * redis中缓存系统配置标签锁的key
     */
    public static final String LOCK_TAG = "lock:tag";

    /**
     * 标签锁的过期时间
     */
    public static final Long LOCK_TAG_TTL = 20L;


}
