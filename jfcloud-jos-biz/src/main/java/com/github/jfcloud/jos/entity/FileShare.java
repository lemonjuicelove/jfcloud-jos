package com.github.jfcloud.jos.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 文件分享表
 * </p>
 *
 * @author lemon
 * @since 2021-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("jos_file_share")
@ApiModel(value="FileShare对象", description="文件分享表")
public class FileShare implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "分享文件名")
    private String name;

    @ApiModelProperty(value = "有效期")
    private Date effectiveDate;

    @ApiModelProperty(value = "提取码")
    private String extractCode;

    @ApiModelProperty(value = "链接")
    private String linkAddress;

    @ApiModelProperty(value = "分享人名称")
    private String sharer;

    @ApiModelProperty(value = "分享人id")
    private Long sharerId;

    @ApiModelProperty(value = "分享时间")
    private Date shareTime;

    @ApiModelProperty(value = "失效标志 0/1 失效/未失效")
    private String isEffective;

    @ApiModelProperty(value = "浏览次数")
    private Long viewCount;

    @ApiModelProperty(value = "保存次数")
    private Long saveCount;

    @ApiModelProperty(value = "下载次数")
    private Long downloadCount;

    @ApiModelProperty(value = "创建人")
    private Long createdBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createdDate;

    @ApiModelProperty(value = "修改人")
    private Long lastModifiedBy;

    @ApiModelProperty(value = "上次修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date lastModifiedDate;

    @ApiModelProperty(value = "删除人")
    private Long deletedBy;

    @ApiModelProperty(value = "删除时间")
    private Date deletedDate;

    @ApiModelProperty(value = "删除标志 0/1 删除/未删除")
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private String deletedFlag;

    @ApiModelProperty(value = "分库id")
    private Long dbId;


}