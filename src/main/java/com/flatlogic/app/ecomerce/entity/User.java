package com.flatlogic.app.ecomerce.entity;

import com.flatlogic.app.ecomerce.type.RoleType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends AbstractEntity<UUID> {

    @Column(name = "`firstName`", columnDefinition = "text")
    private String firstName;

    @Column(name = "`lastName`", columnDefinition = "text")
    private String lastName;

    @Column(name = "`phoneNumber`", columnDefinition = "text")
    private String phoneNumber;

    @Column(name = "email", unique = true, columnDefinition = "text")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleType role;

    @Column(name = "disabled", nullable = false, columnDefinition = "boolean default false")
    private Boolean disabled;

    @Column(name = "password", columnDefinition = "text")
    private String password;

    @Column(name = "`emailVerified`", nullable = false, columnDefinition = "boolean default false")
    private Boolean emailVerified;

    @Column(name = "`emailVerificationToken`", unique = true, columnDefinition = "text")
    private String emailVerificationToken;

    @Column(name = "`emailVerificationTokenExpiresAt`")
    private LocalDateTime emailVerificationTokenExpiresAt;

    @Column(name = "`passwordResetToken`", unique = true, columnDefinition = "text")
    private String passwordResetToken;

    @Column(name = "`passwordResetTokenExpiresAt`")
    private LocalDateTime passwordResetTokenExpiresAt;

    @Column(name = "provider", columnDefinition = "text")
    private String provider;

    @Column(name = "`importHash`", unique = true)
    private String importHash;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "`usersWishlistProducts`",
            joinColumns = {@JoinColumn(name = "`userId`")},
            inverseJoinColumns = {@JoinColumn(name = "`productId`")}
    )
    @Where(clause = "`deletedAt` is null")
    private List<Product> products = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "`belongsToId`", foreignKey = @ForeignKey(name = "none"))
    @Where(clause = "`deletedAt` is null")
    private List<File> files = new ArrayList<>();

}
