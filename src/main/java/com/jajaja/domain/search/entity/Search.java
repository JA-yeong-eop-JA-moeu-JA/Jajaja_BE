package com.jajaja.domain.search.entity;

import com.jajaja.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Search extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Builder.Default
    @Column(nullable = false)
    private Integer searchCount = 1;

    public void increaseCount() {
        this.searchCount += 1;
    }
}
