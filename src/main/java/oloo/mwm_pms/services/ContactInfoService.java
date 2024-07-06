package oloo.mwm_pms.services;

import oloo.mwm_pms.controllers.ContactInfoController;
import oloo.mwm_pms.entinties.ContactInfo;
import oloo.mwm_pms.repositories.ContactInfoRepository;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactInfoService {
    private final ContactInfoRepository contactInfoRepository;

    public ContactInfoService(ContactInfoRepository contactInfoRepository) {
        this.contactInfoRepository = contactInfoRepository;
    }

    public PagedModel<ContactInfo> getAllContactInfos(int page, int size) {
        // Fetch one extra record to determine if there are more pages
        List<ContactInfo> contactInfos = contactInfoRepository.findAll(page, size + 1);

        if (contactInfos == null || contactInfos.isEmpty()) {
            return null;
        }

        // Check if there is a next page
        boolean hasNext = contactInfos.size() > size;
        if (hasNext) {
            contactInfos = contactInfos.subList(0, size);
        }

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, contactInfos.size());
        WebMvcLinkBuilderFactory factory = new WebMvcLinkBuilderFactory();
        WebMvcLinkBuilder linkBuilder = factory.linkTo(WebMvcLinkBuilder.methodOn(ContactInfoController.class).getAllContactInfos(page, size));
        PagedModel<ContactInfo> pagedModel = PagedModel.of(contactInfos, pageMetadata, linkBuilder.withSelfRel());

        // Add links for next and previous pages, if applicable
        if (page > 0) {
            pagedModel.add(linkBuilder.slash("?page=" + (page - 1) + "&size=" + size).withRel("prev"));
        }
        if (hasNext) {
            pagedModel.add(linkBuilder.slash("?page=" + (page + 1) + "&size=" + size).withRel("next"));
        }

        return pagedModel;
    }
}
