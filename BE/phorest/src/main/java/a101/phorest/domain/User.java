package a101.phorest.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

   @Id
   @Column(name = "user_id")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long usedId;

   @Column(name = "username", length = 50, unique = true)
   private String username;

   @Column(name = "password", length = 100)
   private String password;

   @Column(name = "nickname", length = 50)
   private String nickname;

   @Column(name = "activated")
   private boolean activated;

   @Enumerated(EnumType.STRING)
   @Column(name = "role")
   private Role role;

   @Column(name = "phone", length = 50, unique = true)
   private String phone;

//   @ManyToMany
//   @JoinTable(
//      name = "user_authority",
//      joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
//      inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
//   private Set<Authority> authorities;
}