package github.com.worldmaomao.vo;

import lombok.Data;

/**
 */
@Data
public class DiskItemVo {

    private String id;
    private String diskId;
    private String diskName;
    private String fileName;
    private String fileType;
    private String description;
    private long created;
    private long modified;

}
