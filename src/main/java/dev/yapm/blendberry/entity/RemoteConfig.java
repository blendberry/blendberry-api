package dev.yapm.blendberry.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "configs")
@CompoundIndex(name = "appEnvVersionIndex", def = "{'appId': 1, 'env': 1, 'version': 1}", unique = true)
public class RemoteConfig {

    @Id
    private String id;

    @Indexed @NotNull
    private String appId;

    @Indexed @NotNull
    private String env;

    @Indexed @NotNull
    private String version;

    private Map<String, Object> configs;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yy", timezone = "UTC")
    private Date createdAt = new Date();
}