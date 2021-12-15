package kdt.prgrms.kazedon.everevent.domain.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Builder
public class EventCreateRequest {
    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    private Long marketId;

    @NotNull
    private String description;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime expiredAt;

    @Min(0)
    private int maxParticipants;

}
