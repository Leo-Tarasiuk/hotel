package com.tarasiuk.project.hotel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comfort")
public class Comfort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comfort")
    private List<Room> rooms;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "comfort_convenience",
            joinColumns = @JoinColumn(name = "comfort_id"),
            inverseJoinColumns = @JoinColumn(name = "convenience_id"))
    private List<Convenience> conveniences;
}
