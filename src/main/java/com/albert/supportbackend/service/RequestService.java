package com.albert.supportbackend.service;

import com.albert.supportbackend.model.Request;
import com.albert.supportbackend.model.RequestStatus;
import com.albert.supportbackend.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class RequestService {

    private final RequestRepository requestRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public Request create(Request request) {
        return requestRepository.save(request);
    }

    public Request updateStatus(Request request, RequestStatus requestStatus) {
        request.setStatus(requestStatus);
        return requestRepository.save(request);
    }

    public Optional<Request> findById(Long id) {
        return requestRepository.findById(id);
    }

    public Request update(Request request, Long requestId) {
        request.setId(requestId);
        return requestRepository.save(request);
    }

    public Page<Request> findPaginatedAndSorted(Pageable pageable) {
        Page<Request> requests = requestRepository.findAllByStatusSent(pageable);
        updateRequestBodyForOperator(requests);
        return requests;
    }

    public Page<Request> findPaginatedAndSortedByUserId(Pageable pageable, Long userId) {
        return requestRepository.findAllByUserId(pageable, userId);
    }

    public Page<Request> findPaginatedAndSortedByName(Pageable pageable, String username) {
        Page<Request> requests = requestRepository.findAllByNameAndStatusSent(pageable,username);
        updateRequestBodyForOperator(requests);
        return requests;
    }

    private void updateRequestBodyForOperator(Page<Request> requests) {
        requests.stream().forEach(r -> {
            String body = r.getBody();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < body.length(); i++) {
                builder.append(body.charAt(i));
                if (i != body.length() - 1) {
                    builder.append("-");
                }
            }
            r.setBody(builder.toString());
        });
    }
}
