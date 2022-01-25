package kdt.prgrms.kazedon.everevent.service;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kdt.prgrms.kazedon.everevent.domain.event.Event;
import kdt.prgrms.kazedon.everevent.domain.event.repository.EventRepository;
import kdt.prgrms.kazedon.everevent.domain.market.Market;
import kdt.prgrms.kazedon.everevent.domain.review.Review;
import kdt.prgrms.kazedon.everevent.domain.review.dto.request.ReviewWriteRequest;
import kdt.prgrms.kazedon.everevent.domain.review.dto.response.SimpleReview;
import kdt.prgrms.kazedon.everevent.domain.review.dto.response.SimpleReviewReadResponse;
import kdt.prgrms.kazedon.everevent.domain.review.dto.response.UserReview;
import kdt.prgrms.kazedon.everevent.domain.review.dto.response.UserReviewReadResponse;
import kdt.prgrms.kazedon.everevent.domain.review.repository.ReviewRepository;
import kdt.prgrms.kazedon.everevent.domain.user.User;
import kdt.prgrms.kazedon.everevent.domain.user.UserType;
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
import org.springframework.data.domain.*;
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
  private MultipartFile file;

  @InjectMocks
  private ReviewService reviewService;

  private User anotherUser;

  private User user;

  private Market market;

  private Event event;

  private UserEvent userEvent;

  private Review review;

  private SimpleReview simpleReview;

  private ReviewWriteRequest reviewWriteRequest;

  private UserReview userReview;

  private UserReviewReadResponse userReviewReadResponse;

  private Pageable pageable;

  private Page<SimpleReview> simpleReviews;

  private Page<Review> reviews;

  Page<UserReview> userReviews;

  SimpleReviewReadResponse simpleReviewReadResponse;

  @BeforeEach
  void setUp() {
    user = new User(
        1L,
        "email@gmail.com",
        "Pw123!",
        "에디",
        "경기도 남양주시 와부읍",
        UserType.ROLE_USER.name()
    );

    anotherUser = new User(
        2L,
        "email2@gmail.com",
        "Pw123!",
        "디에",
        "경기도 남양주시 와부읍",
        UserType.ROLE_USER.name()
    );

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

    simpleReview = SimpleReview.builder()
        .reviewId(1L)
        .description("정말 좋은 이벤트!")
        .pictureUrls(List.of("pictureUrl"))
        .memberId(1L)
        .memberNickname("에디")
        .build();

    userReview = UserReview.builder()
        .reviewId(1L)
        .description("정말 좋은 이벤트!")
        .marketName("햄찌네 가게")
        .pictureUrl("pictureUrl")
        .build();

    simpleReviews = new PageImpl<>(List.of(simpleReview));

    reviews = new PageImpl<>(List.of(review));

    userReviews = new PageImpl<>(List.of(userReview));

    simpleReviewReadResponse = SimpleReviewReadResponse.builder()
        .reviews(simpleReviews)
        .build();

    userReviewReadResponse = UserReviewReadResponse.builder()
        .reviews(userReviews)
        .reviewerEventCount(1L)
        .reviewerReviewCount(1L)
        .build();

    reviewWriteRequest = new ReviewWriteRequest("정말 좋은 이벤트!");

    pageable = PageRequest.of(0, 20, Sort.by("createdAt").descending());
  }

  @Test
  @DisplayName("리뷰 등록 성공")
  void createReviewTest() {
    // Given
    Long eventId = 1L;
    String s3PictureUrl = "s3" + "pictureUrl";
    given(eventRepository.findById(eventId)).willReturn(Optional.of(event));
    given(userEventRepository.findByUserIdAndEventId(user.getId(), eventId)).willReturn(
        Optional.of(userEvent));
    userEvent.completeByBusiness();
    given(fileService.uploadImage(file)).willReturn(s3PictureUrl);
    given(
        reviewConverter.convertToReview(user, event, reviewWriteRequest, s3PictureUrl)).willReturn(
        review);

    // When
    reviewService.createReview(user, eventId, reviewWriteRequest, file);

    // Then
    verify(eventRepository).findById(eventId);
    verify(userEventRepository).findByUserIdAndEventId(user.getId(), eventId);
    verify(fileService).uploadImage(file);
    verify(reviewConverter).convertToReview(user, event, reviewWriteRequest, s3PictureUrl);
    verify(reviewRepository).save(review);
  }

  @Test
  @DisplayName("리뷰 목록 조회 성공")
  void getReviewsTest() {
    // Given
    Long eventId = 1L;
    given(eventRepository.findById(eventId)).willReturn(Optional.of(event));
    given(reviewRepository.findByEvent(event, pageable)).willReturn(reviews);
    given(reviewConverter.convertToSimpleReview(review)).willReturn(simpleReview);
    given(reviewConverter.convertToSimpleReviewReadResponse(simpleReviews)).willReturn(
        simpleReviewReadResponse);

    // When
    reviewService.getReviews(eventId, pageable);

    // Then
    verify(eventRepository, times(1)).findById(eventId);
    verify(reviewRepository, times(1)).findByEvent(event, pageable);
    verify(reviewConverter, times(1)).convertToSimpleReview(review);
    verify(reviewConverter, times(1)).convertToSimpleReviewReadResponse(simpleReviews);
  }

  @Test
  @DisplayName("로그인 유저의 리뷰 목록 조회 성공")
  void getUserReviewsTest() throws Exception {
    // Given
    Long reviewerId = 1L;
    User reviewer = user;
    given(reviewRepository.findByUser(reviewer.getId(), pageable)).willReturn(userReviews);
    given(userEventRepository.countByUser(reviewer)).willReturn(1L);
    given(reviewRepository.countByUser(reviewer)).willReturn(1L);
    given(reviewConverter.convertToUserReviewReadResponse(userReviews, 1L, 1L)).willReturn(
        userReviewReadResponse);

    // When
    reviewService.getUserReviews(user, reviewerId, pageable);

    // Then
    verify(reviewRepository, times(1)).findByUser(reviewer.getId(), pageable);
    verify(userEventRepository, times(1)).countByUser(reviewer);
    verify(userEventRepository, times(1)).countByUser(reviewer);
    verify(reviewConverter, times(1)).convertToUserReviewReadResponse(userReviews, 1L, 1L);
  }

  @Test
  @DisplayName("다른 유저의 리뷰 목록 조회 성공")
  void getMyReviewsTest() throws Exception {
    // Given
    Long reviewerId = 1L;
    User reviewer = anotherUser;

    given(userRepository.findById(reviewerId)).willReturn(Optional.of(user));
    given(reviewRepository.findByUser(user.getId(), pageable)).willReturn(userReviews);
    given(userEventRepository.countByUser(user)).willReturn(1L);
    given(reviewRepository.countByUser(user)).willReturn(1L);
    given(reviewConverter.convertToUserReviewReadResponse(userReviews, 1L, 1L)).willReturn(
        userReviewReadResponse);

    // When
    reviewService.getUserReviews(reviewer, reviewerId, pageable);

    // Then
    verify(userRepository, times(1)).findById(reviewerId);
    verify(reviewRepository, times(1)).findByUser(user.getId(), pageable);
    verify(userEventRepository, times(1)).countByUser(user);
    verify(userEventRepository, times(1)).countByUser(user);
    verify(reviewConverter, times(1)).convertToUserReviewReadResponse(userReviews, 1L, 1L);
  }

}
