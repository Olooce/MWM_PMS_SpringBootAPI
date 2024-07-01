package oloo.mwm_pms.controllers;

import oloo.mwm_pms.entinties.ContactInfo;
import oloo.mwm_pms.services.ContactInfoService;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contactinfo")
public class ContactInfoController {
    public final ContactInfoService contactInfoService;

    public ContactInfoController(ContactInfoService contactInfoService) {
        this.contactInfoService = contactInfoService;
    }

    @GetMapping
    public PagedModel<ContactInfo> getAllContactInfos(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        return contactInfoService.getAllContactInfos(page, size);
    }


}
