package cn.lico.geek.modules.news.dto;

import java.util.Date;

/**
 * @Author：linan
 * @Date：2022/11/30 20:33
 */
public class NewsListDto {
    //资讯uid
    private String uid;
    //资讯标题
    private String title;
    //资讯简介
    private String summary;
    //资讯内容
    private String content;
    //图片链接
    private String photoUrl;
    //状态
    private Integer status;
    //浏览量
    private Integer clickCount;

    private Date createTime;

    private Date updateTime;

}
