package com.palette.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Order
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {

    /**
     * Order status: 1=pending payment, 2=pending confirmation, 3=confirmed,
     *               4=out for delivery, 5=completed, 6=cancelled
     */
    public static final Integer PENDING_PAYMENT = 1;
    public static final Integer TO_BE_CONFIRMED = 2;
    public static final Integer CONFIRMED = 3;
    public static final Integer DELIVERY_IN_PROGRESS = 4;
    public static final Integer COMPLETED = 5;
    public static final Integer CANCELLED = 6;

    /**
     * Payment status: 0=pending payment, 1=paid, 2=refund
     */
    public static final Integer UN_PAID = 0;
    public static final Integer PAID = 1;
    public static final Integer REFUND = 2;

    private static final long serialVersionUID = 1L;

    private Long id;

    //Order number
    private String number;

    //Order status: 1=pending payment, 2=pending confirmation, 3=confirmed, 4=out for delivery,
    //              5=completed, 6=cancelled
    private Integer status;

    //ID of user who placed order
    private Long userId;

    //Address ID
    private Long addressBookId;

    //Order time
    private LocalDateTime orderTime;

    //Checkout time
    private LocalDateTime checkoutTime;

    //Payment method (currently unused — Stripe is the only payment integration)
    private Integer payMethod;

    //Payment status: 0=pending payment, 1=paid, 2=refunded
    private Integer payStatus;

    //Amount received
    private BigDecimal amount;

    //Remarks
    private String remark;

    //Username
    private String userName;

    //Phone number
    private String phone;

    //Address
    private String address;

    //Recipient
    private String consignee;

    //Order cancellation reason
    private String cancelReason;

    //Order rejection reason
    private String rejectionReason;

    //Order cancellation time
    private LocalDateTime cancelTime;

    //Estimated delivery time
    private LocalDateTime estimatedDeliveryTime;

    //Delivery status: 1=delivery now, 0=choose a time
    private Integer deliveryStatus;

    //Actual delivery time
    private LocalDateTime deliveryTime;

    //Packaging fee
    private int packAmount;

    //Number of utensils
    private int tablewareNumber;

    //Utensil quantity status: 1=provide based on meal quantity, 0=choose a quantity
    private Integer tablewareStatus;
}
