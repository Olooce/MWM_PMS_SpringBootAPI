package oloo.mwm_pms.services;

import oloo.mwm_pms.controllers.BranchController;
import oloo.mwm_pms.entinties.Branch;
import oloo.mwm_pms.repositories.BranchRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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


    public PagedModel<Branch> getAllBranches( int page, int size) {
        List<Branch> branches = branchRepository.findAll(page, size);
        int totalBranches = branchRepository.count();
        Pageable pageable = PageRequest.of(page, size);
        PageImpl<Branch> branchPage = new PageImpl<>(branches, pageable, totalBranches);

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, totalBranches);
        WebMvcLinkBuilderFactory factory = new WebMvcLinkBuilderFactory();
        WebMvcLinkBuilder linkBuilder = factory.linkTo(WebMvcLinkBuilder.methodOn(BranchController.class).getAllBranches(page, size));
        return PagedModel.of(branches, pageMetadata, linkBuilder.withSelfRel());
    }
}
