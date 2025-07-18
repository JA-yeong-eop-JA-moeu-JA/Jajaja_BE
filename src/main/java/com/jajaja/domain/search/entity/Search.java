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

    @Column(nullable = false)
    private Integer searchCount;

    public void increaseCount() {
        if (this.searchCount == null) {
            this.searchCount = 1;
        } else {
            this.searchCount += 1;
        }
    }
}
