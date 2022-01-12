package kdt.prgrms.kazedon.everevent.service;

import static org.mockito.BDDMockito.*;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventRepository;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.review.Review;
import kdt.prgrms.kazedon.everevent.domain.review.repository.ReviewRepository;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.repository.UserRepository;
import kdt.prgrms.kazedon.everevent.domain.userevent.UserEvent;
import kdt.prgrms.kazedon.everevent.domain.userevent.repository.UserEventRepository;
import kdt.prgrms.kazedon.everevent.service.converter.ReviewConverter;
import kdt.prgrms.kazedon.everevent.service.global.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

  @Mock
  private ReviewRepository reviewRepository;

  @Mock
  private EventRepository eventRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserEventRepository userEventRepository;

  @Mock
  private FileService fileService;

  @Mock
  private ReviewConverter reviewConverter;

  @Mock
  private Pageable pageable;

  @Mock
  private MultipartFile file;

  @InjectMocks
  private ReviewService reviewService;

  private User anotherUser;

  private User user;

  private Market market;

  private Event event;

  private UserEvent userEvent;

  private Review review;

  @BeforeEach
  void setUp() {
    anotherUser = User.builder()
        .email("email2@gmail.com")
        .password("Pw123!")
        .nickname("디에")
        .location("경기도 남양주시 와부읍")
        .build();

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

    userEvent = UserEvent.builder()
        .user(user)
        .event(event)
        .build();

    review = Review.builder()
        .user(user)
        .event(event)
        .description("정말 좋은 이벤트!")
        .pictureUrl("pictureUrl")
        .build();
  }

  @Test
  @DisplayName("리뷰 등록 성공")
  void createReviewTest() {
    // Given
    Long eventId = 1L;
    Long userId = 1L;
    given(eventRepository.findById(eventId)).willReturn(Optional.of(event));
    given(userEventRepository.findByUserIdAndEventId(userId, eventId)).willReturn(
        Optional.of(userEvent));
    userEvent = mock(UserEvent.class);
    given(userEvent.isCompleted()).willReturn(true);
    given(fileService.uploadImage(file)).willReturn(any());
    given(reviewConverter.convertToReview(user, event, any(), any())).willReturn(review);

    // When
    reviewService.createReview(user, eventId, any(), file);

    // Then
    verify(eventRepository).findById(eventId);
    verify(userEventRepository).findByUserIdAndEventId(userId, eventId);
    verify(userEvent).isCompleted();
    verify(fileService).uploadImage(file);
    verify(reviewConverter).convertToReview(user, event, any(), any());
    verify(reviewRepository).save(review);
  }

  @Test
  @DisplayName("리뷰 목록 조회 성공")
  void getReviewsTest() {
    // Given
    Long eventId = 1L;
    given(eventRepository.findById(eventId)).willReturn(Optional.of(event));
    given(reviewRepository.findByEvent(event, pageable)
        .map(reviewConverter::convertToSimpleReview)).willReturn(mock(Page.class));
    // When
    reviewService.getReviews(eventId, pageable);
    // Then
    verify(eventRepository, times(1)).findById(eventId);
    verify(
        reviewRepository).findByEvent(event, pageable).map(reviewConverter::convertToSimpleReview);
    verify(reviewConverter, times(1)).convertToSimpleReviewReadResponse(any());
  }

  @Test
  @DisplayName("로그인 유저의 리뷰 목록 조회 성공")
  void getUserReviewsTest() throws Exception {
    // Given
    Long reviewerId = 1L;
    Method getReviewerMethod = reviewService.getClass()
        .getDeclaredMethod("getReviewer", User.class, Long.class);
    getReviewerMethod.setAccessible(true);

    given(getReviewerMethod.invoke(reviewService, user, reviewerId)).willReturn(user);

    given(reviewRepository.findByUser(user.getId(), pageable)).willReturn(mock(Page.class));
    given(userEventRepository.countByUser(user)).willReturn(any());
    given(userEventRepository.countByUser(user)).willReturn(any());

    // When
    reviewService.getUserReviews(user, reviewerId, pageable);

    // Then
    verify(getReviewerMethod).invoke(reviewService, user, reviewerId);
    verify(reviewRepository).findByUser(user.getId(), pageable);
    verify(userEventRepository).countByUser(user);
    verify(userEventRepository).countByUser(user);
    verify(reviewConverter, times(1)).convertToUserReviewReadResponse(any(), any(),
        any());
  }

  @Test
  @DisplayName("다른 유저의 리뷰 목록 조회 성공")
  void getMyReviewsTest() throws Exception {
    // Given
    Long reviewerId = 1L;
    Method getReviewerMethod = reviewService.getClass()
        .getDeclaredMethod("getReviewer", User.class, Long.class);
    getReviewerMethod.setAccessible(true);

    given(getReviewerMethod.invoke(reviewService, anotherUser, reviewerId)).willReturn(anotherUser);

    given(reviewRepository.findByUser(anotherUser.getId(), pageable)).willReturn(mock(Page.class));
    given(userEventRepository.countByUser(anotherUser)).willReturn(any());
    given(userEventRepository.countByUser(anotherUser)).willReturn(any());

    // When
    reviewService.getUserReviews(anotherUser, reviewerId, pageable);

    // Then
    verify(getReviewerMethod).invoke(reviewService, anotherUser, reviewerId);
    verify(reviewRepository).findByUser(anotherUser.getId(), pageable);
    verify(userEventRepository).countByUser(anotherUser);
    verify(userEventRepository).countByUser(anotherUser);
    verify(reviewConverter, times(1)).convertToUserReviewReadResponse(any(), any(),
        any());
  }


}
