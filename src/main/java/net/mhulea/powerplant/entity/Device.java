package net.mhulea.powerplant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "device")
@NamedEntityGraph(name = "graph.Device.measurements",
        attributeNodes = @NamedAttributeNode("telemetryTimeseriesData"))
@Getter @Setter
public class Device {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long externalId;

    @Embedded
    private DeviceDetails deviceDetails;

    @ManyToOne
    @JoinColumn(name = "plant_id")
    private Plant plant;

    @OneToMany(cascade =CascadeType.ALL, mappedBy = "device")
    private List<TelemetryTimeseriesData> telemetryTimeseriesData = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Device device = (Device) o;
        return id != null && Objects.equals(id, device.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}