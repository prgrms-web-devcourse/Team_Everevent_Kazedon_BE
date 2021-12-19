package kdt.prgrms.kazedon.everevent.domain.event.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailEventReadResponse {

    private String name;
    private LocalDateTime expriedAt;
    private String marketName;
    private String marketDescription;
    private String eventDescription;
    private List<String> pictures;
    private String participateStatus;
    private boolean isLike;
    private boolean isFavorite;

    @JsonProperty("isLike")
    public boolean isLike() {
        return isLike;
    }

    @JsonProperty("isFavorite")
    public boolean isFavorite() {
        return isFavorite;
    }
}
