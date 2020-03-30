package com.sjw.file.io.all.oniondb.request;

import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author shijiawei
 * @version BatchSetRequest.java -> v 1.0
 * @date 2020/3/27
 */
@Data
public class BatchSetRequest {
    private String key;
    private String value;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
