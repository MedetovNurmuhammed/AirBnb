package enjoying.service.impl;

import enjoying.dto.pagination.UserPagination;
import enjoying.dto.request.EditAnnouncementReq;
import enjoying.dto.request.PaginationRequest;
import enjoying.dto.request.announcement.SaveAnnouncementRequest;
import enjoying.dto.response.ForPagination;
import enjoying.dto.response.ResultPaginationAnnouncement;
import enjoying.dto.response.SimpleResponse;
import enjoying.entities.Announcement;
import enjoying.entities.User;
import enjoying.enums.HouseType;
import enjoying.enums.Region;
import enjoying.enums.Role;
import enjoying.exceptions.BedRequestException;
import enjoying.exceptions.ForbiddenException;
import enjoying.repositories.AnnouncementRepository;
import enjoying.repositories.UserRepository;
import enjoying.repositories.jdbcTamplate.AnnouncementJDBCTemplateRepository;
import enjoying.repositories.jdbcTamplate.AnnouncementRepo;
import enjoying.service.AnnouncementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementRepository announcementRepo;
    private final AnnouncementJDBCTemplateRepository templateRepository;
    private final AnnouncementRepo repo;
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
        announcement.setBlock(false);
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
        return templateRepository.getAll(paginationRequest);
    }


    @Override @Transactional
    public SimpleResponse editMyAnnouncement(Long anId, EditAnnouncementReq req) {
        User user = currentUser.getCurrenUser();
        Announcement announcement = announcementRepo.getAnnouncementById(anId);
        if (!user.equals(announcement.getUser())){
            throw new ForbiddenException("no access");
        }
        announcement.setImages(req.images());
        announcement.setHouseType(req.houseType());
        announcement.setMaxGuests(req.maxOfQuests());
        announcement.setPrice(BigDecimal.valueOf(req.price()));
        announcement.setTitle(req.title());
        announcement.setDescription(req.description());
        announcement.setRegion(req.region());
        announcement.setTown(req.town());
        announcement.setAddress(req.address());
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("successfully edited")
                .build();
    }
    @Override
    public SimpleResponse deleteMyAnnouncement(Long anId) {
        User user = currentUser.getCurrenUser();
        Announcement announcement = announcementRepo.getAnnouncementById(anId);
        if (!user.equals(announcement.getUser())){
            throw new ForbiddenException("no access");
        }
        announcementRepo.delete(announcement);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("successfully deleted")
                .build();
    }
}
