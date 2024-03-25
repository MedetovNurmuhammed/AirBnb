package enjoying.api;

import enjoying.dto.pagination.UserPagination;
import enjoying.dto.response.AnnouncementBookingResponse;
import enjoying.dto.response.FindAnnouncementAdminRes;
import enjoying.dto.response.MyAnnouncementResponses;
import enjoying.dto.response.SimpleResponse;
import enjoying.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminAPI {
    private final AdminService adminService;

    @Secured("ADMIN")
    @GetMapping
    @Operation(description = "FindAll no active Announcements")
    public UserPagination findAllAcceptedAnnouncement(@RequestParam int page,
                                                      @RequestParam int size){
        return adminService.findAllAcceptedAnnouncement(page, size);
    }

    @Secured("ADMIN")
    @GetMapping("/find/{anId}")
    public FindAnnouncementAdminRes findById(@PathVariable Long anId){
        return adminService.findById(anId);
    }

    @Secured("ADMIN")
    @PostMapping("/{anId}")
    public SimpleResponse announcementAccepted(@PathVariable Long anId){
        return adminService.announcementAccepted(anId);
    }

    @Secured("ADMIN")
    @PostMapping("/reject/{anId}")
    public SimpleResponse announcementRejected(@PathVariable Long anId){
        return adminService.announcementRejected(anId);
    }

    @Secured("ADMIN")
    @PostMapping("/reject/{userId}")
    public SimpleResponse announcementBlocked(@PathVariable Long userId){
        return adminService.announcementBlocked(userId);
    }

    @Secured("ADMIN")
    @GetMapping("/user-announcements/{userId}")
    public List<MyAnnouncementResponses> userAnnouncements(@PathVariable Long userId){
        return adminService.userAnnouncements(userId);
    }

    @Secured("ADMIN")
    @GetMapping("/booking/{userId}")
    public List<AnnouncementBookingResponse> bookingAnnouncementByUser(@PathVariable Long userId){
        return adminService.bookingAnnouncementByUser(userId);
    }

    @Secured("ADMIN")
    @PostMapping("/block/{anId}")
    public SimpleResponse blockAnnouncement(@PathVariable Long anId){
        return adminService.blockAnnouncement(anId);
    }

    @Secured("ADMIN")
    @DeleteMapping("/delete/{anId}")
    public SimpleResponse deleteAnnouncement(@PathVariable Long anId){
        return adminService.deleteAnnouncement(anId);
    }
}
