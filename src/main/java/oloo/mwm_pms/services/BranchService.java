package oloo.mwm_pms.services;

import oloo.mwm_pms.controllers.BranchController;
import oloo.mwm_pms.entinties.Branch;
import oloo.mwm_pms.repositories.BranchRepository;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService {
    private final BranchRepository branchRepository;

    public BranchService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public PagedModel<Branch> getAllBranches(int page, int size) {
        // Fetch one extra record to determine if there are more pages
        List<Branch> branches = branchRepository.findAll(page, size + 1);

        if (branches == null || branches.isEmpty()) {
            return null;
        }

        // Check if there is a next page
        boolean hasNext = branches.size() > size;
        if (hasNext) {
            branches = branches.subList(0, size);
        }

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, branches.size());
        WebMvcLinkBuilderFactory factory = new WebMvcLinkBuilderFactory();
        WebMvcLinkBuilder linkBuilder = factory.linkTo(WebMvcLinkBuilder.methodOn(BranchController.class).getAllBranches(page, size));
        PagedModel<Branch> pagedModel = PagedModel.of(branches, pageMetadata, linkBuilder.withSelfRel());

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
