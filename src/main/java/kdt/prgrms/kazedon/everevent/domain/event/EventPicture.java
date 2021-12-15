package kdt.prgrms.kazedon.everevent.domain.event;

import java.util.Objects;
import javax.persistence.*;
import kdt.prgrms.kazedon.everevent.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_picture")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventPicture extends BaseTimeEntity {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(referencedColumnName = "id")
  private Event event;

  @Column(nullable = false)
  private String url;

  @Builder
  public EventPicture(String url, Event event) {
    this.url = url;
    this.setEvent(event);
  }

  public void setEvent(Event event){
    if(Objects.nonNull(this.event)){
      event.getEventPictures().remove(this);
    }
    this.event = event;
    event.getEventPictures().add(this);
  }

}
