package com.acumen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/*.
{
        "u":"akum",
        "t":"4",
        "r":"1",
        "n":"1",
        "d":{
        "t":"27.05",
        "h":"29.32",
        "p":"100677.27"
        }
        }
*/


@Getter
@Setter
@Builder
public class InputValueDto {
    @JsonProperty("u")
    String user;
    @JsonProperty("t")
    int type;
    @JsonProperty("r")
    int rommNumber;
    @JsonProperty("n")
    int number;
    @JsonProperty("d")
    Data data;

    @Getter
    @Setter
    @Builder
    public static class Data {
        @JsonProperty("t")
        double temperature;
        @JsonProperty("h")
        double humidity;
        @JsonProperty("p")
        double pressure;
    }
}
