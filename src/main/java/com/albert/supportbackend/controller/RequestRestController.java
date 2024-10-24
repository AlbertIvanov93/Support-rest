package com.albert.supportbackend.controller;

import com.albert.supportbackend.dto.RequestDTO;
import com.albert.supportbackend.errors.ErrorsPresentation;
import com.albert.supportbackend.model.Role;
import com.albert.supportbackend.model.User;
import com.albert.supportbackend.model.Request;
import com.albert.supportbackend.model.RequestStatus;
import com.albert.supportbackend.service.RequestService;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Locale;


@RestController
@RequestMapping(path = "/api/requests")
public class RequestRestController {

    private final RequestService requestService;

    private final MessageSource messageSource;


    public RequestRestController(RequestService requestService,
                                 MessageSource messageSource) {
        this.requestService = requestService;
        this.messageSource = messageSource;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> createRequest(@RequestBody RequestDTO requestDTO,
                                           @AuthenticationPrincipal User user,
                                           UriComponentsBuilder uriBuilder,
                                           Locale locale) {
        if (requestDTO.getRequest() == null || requestDTO.getRequest().isEmpty()) {
            final String message = messageSource.getMessage("request.create.body.errors.not_set",
                    new Object[]{}, locale);
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorsPresentation(
                            Collections.singletonList(message)));
        } else {
            Request request = new Request(user, requestDTO.getRequest());
            request = requestService.create(request);
            return ResponseEntity.created(uriBuilder
                            .path("api/request/{requestId}")
                            .build(Collections.singletonMap("requestId", request.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request);
        }
    }

    @PutMapping("/send/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> sendRequestToOperator(@PathVariable Long id,
                                                   Locale locale) {
        if (!requestService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            Request request = requestService.findById(id).get();
            if (request.getStatus() != RequestStatus.DRAFT) {
                final String message = messageSource.getMessage("request.send.errors.not_draft_status",
                        new Object[]{}, locale);
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ErrorsPresentation(
                                Collections.singletonList(message)));
            } else {
                return new ResponseEntity<>(requestService.updateStatus(request, RequestStatus.SENT), HttpStatus.OK);
            }
        }
    }

    @PutMapping("/accept/{id}")
    @PreAuthorize("hasAuthority('OPERATOR')")
    public ResponseEntity<?> acceptRequest(@PathVariable Long id,
                                           Locale locale) {
        if (!requestService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            Request request = requestService.findById(id).get();
            if (request.getStatus() != RequestStatus.SENT) {
                final String message = messageSource.getMessage("request.accept.errors.not_sent_status",
                        new Object[]{}, locale);
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ErrorsPresentation(
                                Collections.singletonList(message)));
            } else {
                return new ResponseEntity<>(requestService.updateStatus(request, RequestStatus.ACCEPTED), HttpStatus.OK);
            }
        }
    }

    @PutMapping("/reject/{id}")
    @PreAuthorize("hasAuthority('OPERATOR')")
    public ResponseEntity<?> rejectRequest(@PathVariable Long id,
                                           Locale locale) {
        if (!requestService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            Request request = requestService.findById(id).get();
            if (request.getStatus() != RequestStatus.SENT) {
                final String message = messageSource.getMessage("request.reject.errors.not_sent_status",
                        new Object[]{}, locale);
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ErrorsPresentation(
                                Collections.singletonList(message)));
            } else {
                return new ResponseEntity<>(requestService.updateStatus(request, RequestStatus.REJECTED), HttpStatus.OK);
            }
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> updateRequest(@RequestBody RequestDTO requestDTO,
                                           @PathVariable Long id,
                                           Locale locale) {
        if (!requestService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            Request request = requestService.findById(id).get();
            if (request.getStatus() != RequestStatus.DRAFT) {
                final String message = messageSource.getMessage("request.update.errors.not_draft_status",
                        new Object[]{}, locale);
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ErrorsPresentation(
                                Collections.singletonList(message)));
            } else {
                request.setBody(requestDTO.getRequest());
                return new ResponseEntity<>(requestService.update(request, id), HttpStatus.OK);
            }
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'OPERATOR')")
    public ResponseEntity<Page<Request>> getRequests(Pageable pageable,
                                                     @AuthenticationPrincipal User user) {
        Page<Request> requestPage;
        if (user.getRoles().contains(Role.OPERATOR)) {
            requestPage = requestService.findPaginatedAndSorted(pageable);
        } else {
            requestPage = requestService.findPaginatedAndSortedByUserId(pageable, user.getId());

        }
        return new ResponseEntity<>(requestPage, HttpStatus.OK);
    }

    @GetMapping(params = "name")
    @PreAuthorize("hasAuthority('OPERATOR')")
    public ResponseEntity<Page<Request>> getRequestsByName(@RequestParam(name = "name") String name,
                                                               Pageable pageable) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestService.findPaginatedAndSortedByName(pageable, name));
    }

}
