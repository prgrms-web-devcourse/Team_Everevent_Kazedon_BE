package kdt.prgrms.kazedon.everevent.domain.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailEventReadResponse {

    private String eventName;
    private LocalDateTime expriedAt;
    private String marketName;
    private String marketDescription;
    private String eventDescription;
    private List<String> pictures;

    @JsonProperty(value = "isLike")
    private boolean like;

    @JsonProperty(value = "isParticipated")
    private boolean participated;

    @JsonProperty(value = "isFavorite")
    private boolean favorite;

}
