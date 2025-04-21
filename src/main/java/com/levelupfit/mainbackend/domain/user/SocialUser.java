package com.levelupfit.mainbackend.domain.user;

import com.levelupfit.mainbackend.domain.enums.ProviderTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.relational.core.sql.In;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "social_user")
public class SocialUser {


    @Id
    @Column(name="user_id")
    private Integer userId;  // user_id는 INT니까 Integer 사용

    @Column(name="provider_type", columnDefinition = "provider_type_enum")
    @Enumerated(EnumType.STRING) //enum을 String으로 저장하기 위함 근데 type 에러나서 DB에 CAST 따로 만들어줌
    private ProviderTypeEnum providerType;

    private String email;

    @OneToOne
    @MapsId // user_id를 외래키 + PK로 동시에 사용하는 경우
    @JoinColumn(name = "user_id")
    private User user;  // users 테이블과 1:1 관계
}
