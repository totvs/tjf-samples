package i18n.custom.bundle.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppDto {

    private String id;

    @NotBlank(message = "{AppDto.code.NotBlank}")
    //@Size(max = 50, message = "{AppDto.code.Size}")
    @Size(max = 50)
    private String code;

    @NotBlank(message = "{AppDto.name.NotBlank}")
    @Size(max = 100, message = "{AppDto.name.Size}")
    private String name;

    public String getId() {
        return id;
    }

    public AppDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public AppDto setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public AppDto setName(String name) {
        this.name = name;
        return this;
    }
}