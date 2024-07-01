package oloo.mwm_pms.services;

import oloo.mwm_pms.controllers.ContactInfoController;
import oloo.mwm_pms.entinties.ContactInfo;
import oloo.mwm_pms.repositories.ContactInfoRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactInfoService {
    private final ContactInfoRepository contactInfoRepository;

    public ContactInfoService (ContactInfoRepository contactInfoRepository) {
        this.contactInfoRepository = contactInfoRepository;
    }


    public PagedModel<ContactInfo> getAllContactInfos(int page, int size) {
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
