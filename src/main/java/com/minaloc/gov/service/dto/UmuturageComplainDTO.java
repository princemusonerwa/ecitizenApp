package com.minaloc.gov.service.dto;

import com.minaloc.gov.domain.Category;
import com.minaloc.gov.domain.Organization;
import com.minaloc.gov.domain.User;
import com.minaloc.gov.domain.Village;
import com.minaloc.gov.domain.enumeration.Gender;
import com.minaloc.gov.domain.enumeration.Priority;
import com.minaloc.gov.domain.enumeration.Status;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class UmuturageComplainDTO {

    private Long id;
    private String indangamuntu;
    private String amazina;
    private Instant dob;
    private Gender gender;
    private String ubudeheCategory;
    private String phone;
    private String email;
    private User user;
    private Village village;

    private String ikibazo;
    private String icyakozwe;
    private String icyakorwa;
    private String umwanzuro;
    private Status status;
    private Priority priority;
    private Instant createdAt;
    private Instant updatedAt;
    private Category category;
    private Set<Organization> organizations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIndangamuntu() {
        return indangamuntu;
    }

    public void setIndangamuntu(String indangamuntu) {
        this.indangamuntu = indangamuntu;
    }

    public String getAmazina() {
        return amazina;
    }

    public void setAmazina(String amazina) {
        this.amazina = amazina;
    }

    public Instant getDob() {
        return dob;
    }

    public void setDob(Instant dob) {
        this.dob = dob;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getUbudeheCategory() {
        return ubudeheCategory;
    }

    public void setUbudeheCategory(String ubudeheCategory) {
        this.ubudeheCategory = ubudeheCategory;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Village getVillage() {
        return village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

    public String getIkibazo() {
        return ikibazo;
    }

    public void setIkibazo(String ikibazo) {
        this.ikibazo = ikibazo;
    }

    public String getIcyakozwe() {
        return icyakozwe;
    }

    public void setIcyakozwe(String icyakozwe) {
        this.icyakozwe = icyakozwe;
    }

    public String getIcyakorwa() {
        return icyakorwa;
    }

    public void setIcyakorwa(String icyakorwa) {
        this.icyakorwa = icyakorwa;
    }

    public String getUmwanzuro() {
        return umwanzuro;
    }

    public void setUmwanzuro(String umwanzuro) {
        this.umwanzuro = umwanzuro;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Set<Organization> organizations) {
        this.organizations = organizations;
    }
}
