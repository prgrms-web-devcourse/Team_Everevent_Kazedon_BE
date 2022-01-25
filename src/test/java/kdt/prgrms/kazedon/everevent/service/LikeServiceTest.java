package kdt.prgrms.kazedon.everevent.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventRepository;
import kdt.prgrms.kazedon.everevent.domain.like.EventLike;
import kdt.prgrms.kazedon.everevent.domain.like.dto.response.SimpleEventLike;
import kdt.prgrms.kazedon.everevent.domain.like.dto.response.SimpleEventLikeReadResponse;
import kdt.prgrms.kazedon.everevent.domain.like.repository.EventLikeRepository;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.service.converter.EventLikeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {

  @Mock
  private EventLikeRepository eventLikeRepository;

  @Mock
  private EventRepository eventRepository;

  @Mock
  private EventLikeConverter eventLikeConverter;

  @InjectMocks
  private LikeService likeService;

  private User user;

  private Market market;

  private Event event;

  private Pageable pageable;

  private SimpleEventLikeReadResponse simpleEventLikeReadResponse;

  private SimpleEventLike simpleEventLike;

  private Page<SimpleEventLike> simpleEventLikes;

  private EventLike eventLike;

  private Event likedEvent;

  private EventLike likedEventLike;

  @BeforeEach
  void setUp() {
    user = User.builder()
        .email("email@gmail.com")
        .password("Pw123!")
        .nickname("에디")
        .location("경기도 남양주시 와부읍")
        .build();

    ReflectionTestUtils.setField(user, "id", 1L);

    market = Market.builder()
        .user(user)
        .name("햄찌네 가게")
        .description("햄스터 말고 커피팝니다!")
        .address("경기도 남양주시 와부읍 석실로")
        .build();

    event = Event.builder()
        .market(market)
        .name("커피 10퍼센트 할인 이벤트")
        .expiredAt(LocalDateTime.now())
        .description("1주일 동안 진행합니다! 선착순 5명!")
        .maxParticipants(5)
        .build();

    ReflectionTestUtils.setField(event, "id", 1L);

    likedEvent = Event.builder()
        .market(market)
        .name("커피 10퍼센트 할인 이벤트")
        .expiredAt(LocalDateTime.now())
        .description("1주일 동안 진행합니다! 선착순 5명!")
        .maxParticipants(5)
        .build();

    ReflectionTestUtils.setField(likedEvent, "id", 2L);

    eventLike = EventLike.builder()
        .event(event)
        .user(user)
        .build();

    ReflectionTestUtils.setField(eventLike, "id", 1L);

    likedEventLike = EventLike.builder()
        .event(event)
        .user(user)
        .build();

    ReflectionTestUtils.setField(likedEventLike, "id", 2L);

    likedEvent.plusOneLike();

    simpleEventLike = SimpleEventLike.builder()
        .eventId(1L)
        .name("커피 10퍼센트 할인 이벤트")
        .expiredAt(LocalDateTime.now())
        .marketName("햄찌네 가게")
        .reviewCount(1)
        .likeCount(1)
        .build();

    simpleEventLikes = new PageImpl<>(List.of(simpleEventLike));

    pageable = PageRequest.of(0, 20, Sort.by("createdAt").descending());
  }

  @Test
  @DisplayName("좋아요 등록 성공")
  void addLike() {
    // given
    Long eventId = 1L;
    Long userId = 1L;
    given(eventRepository.findById(eventId)).willReturn(Optional.of(event));
    given(eventLikeRepository.existsEventLikeByUserIdAndEventId(userId, eventId)).willReturn(false);
    given(eventLikeConverter.convertToEventLike(user, event)).willReturn(eventLike);
    given(eventLikeRepository.save(eventLike)).willReturn(eventLike);

    // when
    likeService.addLike(user, eventId);

    // then
    verify(eventRepository, times(1)).findById(eventId);
    verify(eventLikeRepository, times(1)).existsEventLikeByUserIdAndEventId(userId, eventId);
    verify(eventLikeConverter, times(1)).convertToEventLike(user, event);
    verify(eventLikeRepository, times(1)).save(eventLike);
    assertThat(event.getLikeCount()).isEqualTo(1L);
  }

  @Test
  @DisplayName("좋아요 취소 성공")
  void deleteLike() {
    // given
    Long eventId = 2L;
    Long userId = 1L;
    given(eventRepository.findById(eventId)).willReturn(Optional.of(likedEvent));
    given(eventLikeRepository.findByUserIdAndEventId(userId, eventId)).willReturn(
        Optional.of(likedEventLike));
    willDoNothing().given(eventLikeRepository).deleteById(likedEventLike.getId());

    // when
    likeService.deleteLike(user, eventId);

    // then
    verify(eventRepository, times(1)).findById(eventId);
    verify(eventLikeRepository, times(1)).findByUserIdAndEventId(userId, eventId);
    verify(eventLikeRepository, times(1)).deleteById(likedEventLike.getId());
    assertThat(event.getLikeCount()).isZero();

  }

  @Test
  @DisplayName("좋아요 누른 이벤트 조회 성공")
  void getLikes() {
    // given
    Long memberId = 1L;
    given(eventLikeRepository.findSimpleLikeByUserId(memberId, pageable)).willReturn(
        simpleEventLikes);
    given(eventLikeConverter.convertToSimpleEventLikeReadResponse(simpleEventLikes)).willReturn(
        simpleEventLikeReadResponse);

    // when
    likeService.getLikes(memberId, pageable);

    // then
    verify(eventLikeRepository, times(1)).findSimpleLikeByUserId(memberId, pageable);
    verify(eventLikeConverter, times(1)).convertToSimpleEventLikeReadResponse(simpleEventLikes);
  }


}
