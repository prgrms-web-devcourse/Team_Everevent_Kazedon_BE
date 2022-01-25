package kdt.prgrms.kazedon.everevent.domain.review;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventRepository;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.market.repository.MarketRepository;
import kdt.prgrms.kazedon.everevent.domain.review.dto.response.UserReview;
import kdt.prgrms.kazedon.everevent.domain.review.repository.ReviewRepository;
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
class ReviewRepositoryTest {

  @Autowired
  ReviewRepository reviewRepository;

  @Autowired
  EventRepository eventRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  MarketRepository marketRepository;

  @Autowired
  private TestEntityManager entityManager;

  private Review review;

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

    review = Review.builder()
        .user(user)
        .event(event)
        .description("정말 좋은 이벤트!")
        .pictureUrl("pictureUrl")
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
    reviewRepository.deleteAll();
  }

  @Test
  @DisplayName("리뷰 저장 성공")
  void save() {
    // when
    Review savedReview = reviewRepository.save(review);

    // then
    assertThat(savedReview.getDescription()).isEqualTo(review.getDescription());
    assertThat(savedReview.getPictureUrl()).isEqualTo(review.getPictureUrl());
  }

  @Test
  @DisplayName("리뷰 목록 이벤트로 페이지 조회 성공")
  void findByEvent() {
    // given
    entityManager.persist(review);
    entityManager.clear();

    // when
    Page<Review> searchedReviews = reviewRepository.findByEvent(event, pageable);

    // then
    assertThat(searchedReviews.getTotalElements()).isEqualTo(1L);
    assertThat(searchedReviews.get().findFirst()).isPresent();

  }

  @Test
  @DisplayName("UserReview 목록 유저로 페이지 조회 성공")
  void findByUser() {
    // given
    entityManager.persist(review);
    entityManager.clear();

    // when
    Page<UserReview> searchedUserReviews = reviewRepository.findByUser(user.getId(), pageable);

    // then
    assertThat(searchedUserReviews.getTotalElements()).isEqualTo(1L);
    assertThat(searchedUserReviews.get().findFirst()).isPresent();
  }

  @Test
  @DisplayName("리뷰 갯수 유저로 조회")
  void countByUser() {
    // given
    entityManager.persist(review);
    entityManager.clear();

    // when
    long reviewCount = reviewRepository.countByUser(user);

    // then
    assertThat(reviewCount).isEqualTo(1L);
  }

}
