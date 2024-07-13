package school.kku.repellingserver.farm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import school.kku.repellingserver.farm.constant.FarmType;
import school.kku.repellingserver.farm.dto.FarmRequest;
import school.kku.repellingserver.gateway.domain.Gateway;
import school.kku.repellingserver.member.domain.Member;
import school.kku.repellingserver.repellent.repellentDevice.domain.RepellentDevice;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Farm {
    @Id @GeneratedValue
    private Long id;

    private String name;

    private String address;

    @Enumerated(EnumType.STRING)
    private FarmType farmType;

    @OneToOne
    private Gateway gateway;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "farm", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RepellentDevice> repellentDevice = new ArrayList<>();

    @Builder
    public Farm(Long id, String name, String address, FarmType farmType, Gateway gateway, Member member, List<RepellentDevice> repellentDevice) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.farmType = farmType;
        this.gateway = gateway;
        this.member = member;
        this.repellentDevice = repellentDevice;
    }

    public void addRepellentDevice(RepellentDevice repellentDevice) {
        if (this.repellentDevice == null) {
            this.repellentDevice = new ArrayList<>();
        }
        this.repellentDevice.add(repellentDevice);
        repellentDevice.setFarm(this);
    }



    public static Farm toEntity(FarmRequest request, Gateway gateway, Member member) {
        return Farm.builder()
                .name(request.farmName())
                .address(request.farmAddress())
                .farmType(request.farmType())
                .gateway(gateway)
                .member(member)
                .build();
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

}
