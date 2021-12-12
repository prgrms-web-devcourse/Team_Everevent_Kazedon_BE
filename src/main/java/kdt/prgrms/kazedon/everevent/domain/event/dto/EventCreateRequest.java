package kdt.prgrms.kazedon.everevent.domain.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
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

    private List<String> pictures;

    @Builder
    public EventCreateRequest(String name, long marketId, String description, LocalDateTime expiredAt, int maxParticipants, List<String> pictures) {
        this.name = name;
        this.marketId = marketId;
        this.description = description;
        this.expiredAt = expiredAt;
        this.maxParticipants = maxParticipants;
        this.pictures = pictures;
    }
}
