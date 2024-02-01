package com.flatlogic.app.ecomerce.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category extends AbstractAuditable {

    @Column(name = "title", columnDefinition = "text")
    private String title;

    @Column(name = "`importHash`", unique = true)
    private String importHash;

}
