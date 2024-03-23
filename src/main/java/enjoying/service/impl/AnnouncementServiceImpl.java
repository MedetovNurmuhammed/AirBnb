package enjoying.service.impl;

import enjoying.dto.request.PaginationRequest;
import enjoying.dto.request.announcement.SaveAnnouncementRequest;
import enjoying.dto.response.ForPagination;
import enjoying.dto.response.ResultPaginationAnnouncement;
import enjoying.dto.response.SimpleResponse;
import enjoying.entities.Announcement;
import enjoying.entities.User;
import enjoying.enums.Role;
import enjoying.exceptions.BedRequestException;
import enjoying.repositories.AnnouncementRepository;
import enjoying.repositories.UserRepository;
import enjoying.service.AnnouncementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementRepository announcementRepo;
    private final CurrentUser currentUser;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SimpleResponse save(SaveAnnouncementRequest saveAnnouncementRequest) {
        User user = currentUser.getCurrenUser();
        if(user.getMoney().intValue() < 200) {
            throw  new BedRequestException("You don't have enough money. To publish a post, you need to pay 200 som");
        }
        user.setMoney(BigDecimal.valueOf(user.getMoney().intValue() - 200));
        User admin = userRepository.findByRole(Role.ADMIN);
        admin.setMoney(BigDecimal.valueOf(admin.getMoney().intValue() + 200));
        Announcement announcement = new Announcement();
        announcement.setImages(saveAnnouncementRequest.images());
        announcement.setTitle(saveAnnouncementRequest.title());
        announcement.setHouseType(saveAnnouncementRequest.houseType());
        announcement.setPrice(BigDecimal.valueOf(saveAnnouncementRequest.price()));
        announcement.setMaxGuests(saveAnnouncementRequest.maxOfQuests());
        announcement.setDescription(saveAnnouncementRequest.descriptionOfListing());
        announcement.setRegion(saveAnnouncementRequest.region());
        announcement.setTown(saveAnnouncementRequest.town());
        announcement.setAddress(saveAnnouncementRequest.address());
        user.setRole(Role.VENDOR);
        announcementRepo.save(announcement);
        user.getAnnouncements().add(announcement);
        announcement.setUser(user);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully to publish a post!")
                .build();
    }

    @Override
    public ResultPaginationAnnouncement getAll(PaginationRequest paginationRequest) {
        Pageable pageable = PageRequest.of(paginationRequest.page()-1 , paginationRequest.size());
        Page<Announcement> announcementPage = announcementRepo.findAll(pageable);

        List<ForPagination> resultPagination = new ArrayList<>();

        return ResultPaginationAnnouncement.builder()
                .page(announcementPage.getNumber())
                .size(announcementPage.getTotalPages())
                .paginations(resultPagination)
                .build();
    }
}
