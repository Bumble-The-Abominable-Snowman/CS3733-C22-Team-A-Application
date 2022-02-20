package edu.wpi.cs3733.c22.teamA.entities.servicerequests;

import java.sql.Timestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConsultationSR extends SR {
    private String consultation;

    public ConsultationSR() {
        super();
        this.srType = SRType.CONSULTATION;
    }

    public ConsultationSR(
            String requestID,
            String startLocation,
            String endLocation,
            String employeeRequested,
            String employeeAssigned,
            Timestamp requestTime,
            Status requestStatus,
            Priority requestPriority,
            String comments) {
        super(
                requestID,
                startLocation,
                endLocation,
                employeeRequested,
                employeeAssigned,
                requestTime,
                requestStatus,
                requestPriority,
                comments);
        this.srType = SRType.CONSULTATION;
    }
}

