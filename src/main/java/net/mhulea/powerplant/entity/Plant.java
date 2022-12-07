package net.mhulea.powerplant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "plant")
@Getter @Setter
public class Plant {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long externalId;

    private LocalDateTime firstReportedDate;
    private String plantName;
    private String plantState;

    @Embedded
    private PlantLocation plantLocation;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "plant")
    private List<Device> devices = new ArrayList<>();

    public void addDevice(Device d){
        if(devices == null)
            devices = new ArrayList<>();
        devices.add(d);
        d.setPlant(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Plant plant = (Plant) o;
        return id != null && Objects.equals(id, plant.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}