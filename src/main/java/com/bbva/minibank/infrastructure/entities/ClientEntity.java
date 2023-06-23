package com.bbva.minibank.infrastructure.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientEntity {

  @Id
  @Column(nullable = false, unique = true, updatable = false)
  private UUID id;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false, unique = true)
  @Email
  private String email;

  @Nullable
  private String phone;

  @Nullable
  private String address;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
        name = "client_account",
        joinColumns = @JoinColumn(name = "client_id"),
        inverseJoinColumns = @JoinColumn(name = "account_number")
  )
  private Set<AccountEntity> accounts;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDate createdAt;
  
  @UpdateTimestamp
  private LocalDate updatedAt;
  
  private Boolean isActive;
  
  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserEntity user;

}
