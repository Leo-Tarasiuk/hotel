package com.tarasiuk.project.hotel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Byte placement;

    @ManyToOne(optional = false)
    @JoinColumn(name = "comfort_id")
    private Comfort comfort;

    @Column(nullable = false)
    private Short price;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "room")
    private List<Photo> photos;

    @Column
    private String description;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "room")
    private Booking booking;
}
