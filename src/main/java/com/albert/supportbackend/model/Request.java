package com.albert.supportbackend.model;

import javax.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private Date date;
    private String body;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    public Request() {
    }

    public Request(User user, String body) {
        this.user = user;
        this.body = body;
        date = new Date();
        status = RequestStatus.DRAFT;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request)) return false;
        Request request = (Request) o;
        return Objects.equals(user, request.user) && Objects.equals(date, request.date) && Objects.equals(body, request.body) && status == request.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, date, body, status);
    }
}
