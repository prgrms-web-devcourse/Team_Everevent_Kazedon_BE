package kdt.prgrms.kazedon.everevent.domain.like;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventRepository;
import kdt.prgrms.kazedon.everevent.domain.like.dto.response.SimpleEventLike;
import kdt.prgrms.kazedon.everevent.domain.like.repository.EventLikeRepository;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.market.repository.MarketRepository;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@DataJpaTest
public class EventLikeRepositoryTest {

  @Autowired
  EventLikeRepository eventLikeRepository;

  @Autowired
  EventRepository eventRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  MarketRepository marketRepository;

  @Autowired
  private TestEntityManager entityManager;

  private EventLike eventLike;

  private User user;

  private Event event;

  private Market market;

  private Pageable pageable;

  @BeforeEach
  void setUp() {
    user = User.builder()
        .email("email@gmail.com")
        .password("Pw123!")
        .nickname("에디")
        .location("경기도 남양주시 와부읍")
        .build();

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

    eventLike = EventLike.builder()
        .user(user)
        .event(event)
        .build();

    pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());

    userRepository.save(user);
    marketRepository.save(market);
    eventRepository.save(event);
  }

  @AfterEach
  void tearDown() {
    eventRepository.deleteAll();
    marketRepository.deleteAll();
    userRepository.deleteAll();
    eventLikeRepository.deleteAll();
  }

  @Test
  @DisplayName("좋아요 성공")
  void save() {
    // when
    EventLike savedEventLike = eventLikeRepository.save(eventLike);

    // then
    assertThat(savedEventLike.getEvent()).isEqualTo(eventLike.getEvent());
    assertThat(savedEventLike.getUser()).isEqualTo(eventLike.getUser());
  }

  @Test
  @DisplayName("좋아요 취소 성공")
  void deleteById() {
    // given
    entityManager.persist(eventLike);
    entityManager.clear();

    // when
    eventLikeRepository.deleteById(eventLike.getId());

    // then
    assertThat(eventLikeRepository.findAll()).isEmpty();
  }

  @Test
  @DisplayName("유저 pk와 이벤트 pk로 좋아요 여부 조회")
  void existsEventLikeByUserIdAndEventId() {
    // given
    entityManager.persist(eventLike);
    entityManager.clear();

    // when
    boolean actualBoolValue = eventLikeRepository.existsEventLikeByUserIdAndEventId(user.getId(),
        event.getId());

    // then
    assertThat(actualBoolValue).isTrue();
  }

  @Test
  @DisplayName("유저 pk와 이벤트 pk로 단일 좋아요 조회")
  void findByUserIdAndEventId() {
    // given
    entityManager.persist(eventLike);
    entityManager.clear();

    // when
    Optional<EventLike> searchedEventLike = eventLikeRepository.findByUserIdAndEventId(user.getId(),
        event.getId());

    // then
    assertThat(searchedEventLike).isPresent();
    searchedEventLike.ifPresent(
        actualEventLike -> assertThat(actualEventLike.getEvent().getId()).isEqualTo(
            eventLike.getEvent().getId()));
    searchedEventLike.ifPresent(
        actualEventLike -> assertThat(actualEventLike.getUser().getId()).isEqualTo(
            eventLike.getUser().getId()));
  }

  @Test
  @DisplayName("유저 pk로 SimpleLike 목록 페이지 조회")
  void findSimpleLikeByUserId() {
    // given
    entityManager.persist(eventLike);
    entityManager.clear();

    // when
    Page<SimpleEventLike> searchedSimpleEventLikes = eventLikeRepository.findSimpleLikeByUserId(
        user.getId(), pageable);

    // then
    assertThat(searchedSimpleEventLikes.getTotalElements()).isEqualTo(1L);
    searchedSimpleEventLikes.get()
        .findFirst()
        .ifPresent(
            actualSimpleEventLike -> assertThat(actualSimpleEventLike.getEventId()).isEqualTo(
                event.getId()));
    searchedSimpleEventLikes.get()
        .findFirst()
        .ifPresent(
            actualSimpleEventLike -> assertThat(actualSimpleEventLike.getMarketName()).isEqualTo(
                market.getName()));
  }

}
