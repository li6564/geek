package cn.lico.geek.core.listener;

import cn.lico.geek.core.queue.message.DataItemChangeMessage;

/**
 * 数据更新监听器
 * 监听本系统所有的数据更新 操作：更新操作包括
 * @author linan
 */
public interface DataItemChangeListener {

    /**
     * 有新数据添加到数据库
     * @param dataItemChangeMessage
     */
    default void onDataItemAdd(DataItemChangeMessage dataItemChangeMessage) throws Exception {

    }

    /**
     * 有数据从数据库删除
     * @param dataItemChangeMessage
     */
    default void onDataItemDelete(DataItemChangeMessage dataItemChangeMessage) throws Exception {

    }

    /**
     * 数据库里面的数据更新
     * @param dataItemChangeMessage
     */
    default void onDataItemUpdate(DataItemChangeMessage dataItemChangeMessage) throws Exception {


    }

    /**
     * 用户昵称更新
     * @param dataItemChangeMessage
     */
    default void onUserNickUpdate(DataItemChangeMessage dataItemChangeMessage) {


    }

    /**
     * 用户头像更新
     * @param dataItemChangeMessage
     */
    default void onUserAvatarUpdate(DataItemChangeMessage dataItemChangeMessage) {

    }

    /**
     * 用户注册
     * @param dataItemChangeMessage
     * @throws Exception
     */
    default void onDataItemRegister(DataItemChangeMessage dataItemChangeMessage) throws Exception {


    }
}
