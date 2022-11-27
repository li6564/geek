package cn.lico.geek.core.queue.message;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author：linan
 * @Date：2022/11/17 21:05
 */
@Data
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class DataItemChangeMessage {
    /**
     * 条目ID
     */
    private String itemId;


    /**
     * 操作者ID
     */
    private String operatorId;


    /**
     * 条目类型
     */
    private DataItemType itemType;

    /**
     * 条目改变类型
     */
    private DataItemChangeType changeType;

    /**
     * 被操作者ID
     */
    private String userUid;

    private String businessUid;

    public DataItemChangeMessage(DataItemChangeType changeType, DataItemType itemType, String itemId) {
        this.itemId = itemId;
        this.itemType = itemType;
        this.changeType = changeType;
    }

    public static DataItemChangeMessage addMessage(DataItemType itemType, String itemId) {
        return new DataItemChangeMessage(DataItemChangeType.ADD, itemType, itemId);
    }

    public DataItemChangeMessage(String operatorId,DataItemChangeType changeType, DataItemType itemType, String itemId) {
        this.itemId = itemId;
        this.itemType = itemType;
        this.changeType = changeType;
        this.operatorId = operatorId;
    }

    public DataItemChangeMessage(String userUid,String operatorId,DataItemChangeType changeType, DataItemType itemType, String itemId) {
        this.itemId = itemId;
        this.itemType = itemType;
        this.changeType = changeType;
        this.operatorId = operatorId;
        this.userUid = userUid;
    }
}
