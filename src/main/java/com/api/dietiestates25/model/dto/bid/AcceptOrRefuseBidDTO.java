package com.api.dietiestates25.model.dto.bid;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcceptOrRefuseBidDTO {
    private int id;
    private double amount;
    private String agentMessage;
    private String offererMessage;
    private String status;
}