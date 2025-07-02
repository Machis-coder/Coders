package com.codingtrainers.duocoding.dto.output;

import com.codingtrainers.duocoding.entities.Role;
import com.codingtrainers.duocoding.entities.UserSubject;

public class UserSubjectResponseDTO {

        private Long userId;
        private String userName;
        private Long subjectId;
        private String subjectName;
        private Role role;
        private String surname;
        private String email;
        private String dni;
        private String name;

        public UserSubjectResponseDTO(UserSubject us) {
            this.userId = us.getUser().getId();
            this.userName = us.getUser().getUsername();
            this.subjectId = us.getSubject().getId();
            this.subjectName = us.getSubject().getName();
            this.role = us.getUser().getRole();
            this.surname = us.getUser().getSurname();
            this.email = us.getUser().getEmail();
            this.dni = us.getUser().getDni();
            this.name = us.getUser().getName();
        }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
