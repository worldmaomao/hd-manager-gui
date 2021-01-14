package github.com.worldmaomao.vo;

import lombok.Data;

/**
 * 硬盘信息
 *
 */
@Data
public class DiskVo {

    private String id;
    private String name;
    private String description;
    private long created;
    private long modified;
}
