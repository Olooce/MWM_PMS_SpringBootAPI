package oloo.mwm_pms.controllers;

import oloo.mwm_pms.entities.ContactInfo;
import oloo.mwm_pms.repositories.ContactInfoRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contactinfos")
public class ContactInfoController {

    private final ContactInfoRepository contactInfoRepository;

    public ContactInfoController(ContactInfoRepository contactInfoRepository) {
        this.contactInfoRepository = contactInfoRepository;
    }

    @GetMapping
    public PagedModel<ContactInfo> getAllContactInfos(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        List<ContactInfo> contactInfos = contactInfoRepository.findAll(page, size);
        int totalContactInfos = contactInfoRepository.count();
        Pageable pageable = PageRequest.of(page, size);
        PageImpl<ContactInfo> contactInfoPage = new PageImpl<>(contactInfos, pageable, totalContactInfos);

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, totalContactInfos);
        WebMvcLinkBuilderFactory factory = new WebMvcLinkBuilderFactory();
        WebMvcLinkBuilder linkBuilder = factory.linkTo(WebMvcLinkBuilder.methodOn(ContactInfoController.class).getAllContactInfos(page, size));
        return PagedModel.of(contactInfos, pageMetadata, linkBuilder.withSelfRel());
    }
}
