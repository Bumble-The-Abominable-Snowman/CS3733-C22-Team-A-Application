// package edu.wpi.cs3733.c22.teamA.entities.servicerequests;
//
// import java.sql.Timestamp;
// import lombok.Data;
// import lombok.EqualsAndHashCode;
// import lombok.ToString;
//
// @Data
// @EqualsAndHashCode(callSuper = true)
// @ToString(callSuper = true)
// public class FloralDeliverySR extends SR {
//
//  private String flower;
//  private String bouquetType;
//
//  public FloralDeliverySR() {
//    super();
//    this.srType = SRType.FLORAL_DELIVERY;
//  }
//
//  public FloralDeliverySR(
//      String requestID,
//      String startLocation,
//      String endLocation,
//      String employeeRequested,
//      String employeeAssigned,
//      Timestamp requestTime,
//      Status requestStatus,
//      Priority requestPriority,
//      String comments) {
//    super(
//        requestID,
//        startLocation,
//        endLocation,
//        employeeRequested,
//        employeeAssigned,
//        requestTime,
//        requestStatus,
//        requestPriority,
//        comments);
//    this.srType = SRType.FLORAL_DELIVERY;
//  }
// }
