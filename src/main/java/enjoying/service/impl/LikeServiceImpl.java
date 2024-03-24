package enjoying.service.impl;

import enjoying.dto.response.AnnouncementResponses;
import enjoying.dto.response.MyAnnouncementResponses;
import enjoying.dto.response.SimpleResponse;
import enjoying.entities.FeedBack;
import enjoying.entities.User;
import enjoying.enums.HouseType;
import enjoying.repositories.AnnouncementRepository;
import enjoying.repositories.FeedBackRepository;
import enjoying.repositories.LikeRepository;
import enjoying.service.LikeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepo;
    private final FeedBackRepository feedBackRepo;
    private final AnnouncementRepository announcementRepo;
    private final CurrentUser currentUser;

    @Override @Transactional
    public SimpleResponse likeToFeedBack(Long feedId) {
        Long userId = currentUser.getCurrenUser().getId();
        FeedBack feedBack = feedBackRepo.getFeedBackById(feedId);
        if (!feedBack.getLike().getLikes().contains(userId)){
            feedBack.getLike().getDisLikes().remove(userId);
            feedBack.getLike().getLikes().add(userId);
        }else {
            feedBack.getLike().getLikes().remove(userId);
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Success")
                .build();
    }

    @Override @Transactional
    public SimpleResponse disLikeToFeedBack(Long feedId) {
        Long userId = currentUser.getCurrenUser().getId();
        FeedBack feedBack = feedBackRepo.getFeedBackById(feedId);
        if (!feedBack.getLike().getDisLikes().contains(userId)){
            feedBack.getLike().getLikes().remove(userId);
            feedBack.getLike().getDisLikes().add(userId);
        }else {
            feedBack.getLike().getDisLikes().remove(userId);
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Success")
                .build();
    }

    @Override
    public List<MyAnnouncementResponses> myAnnouncements() {
        Long userId = currentUser.getCurrenUser().getId();
        return announcementRepo.myAnnouncements(userId);
    }

    @Override
    public List<MyAnnouncementResponses> myAnnouncementsWithHouseType(HouseType type) {
        Long userId = currentUser.getCurrenUser().getId();
        return announcementRepo.myAnnouncementsWIthHouseType(userId, type);
    }

    @Override
    public List<MyAnnouncementResponses> myAnnouncementsHigh() {
        Long userId = currentUser.getCurrenUser().getId();
        return announcementRepo.myAnnouncementsHigh(userId);
    }

    @Override
    public List<MyAnnouncementResponses> myAnnouncementsLow() {
        Long userId = currentUser.getCurrenUser().getId();
        return announcementRepo.myAnnouncementsLow(userId);
    }
}
