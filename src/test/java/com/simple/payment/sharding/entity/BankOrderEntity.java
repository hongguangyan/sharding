package com.simple.payment.sharding.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author:
 * @since: 12-8-10 下午3:42
 * @version: 1.0.0
 */
public class BankOrderEntity {

    /**
     * 主键Id
     */
    private Long id;
    /**
     * 交易请求表id
     */
    private Long tradeRequestId;
    /**
     * 支付表Id
     */
    private Long tradePaymentId;
    /**
     * 交易唯一标识号
     */
    private String tradeDn = "";
    /**
     * 通知地址(商户通知地址)
     */
    private String notifyUrl = "";
    /**
     * 支付请求流水号，本系统生成发往银行的唯一标识
     */
    private String bankOrderNo;
    /**
     * 银行接口ID
     */
    private String bankInterfaceId;

    /**
     * 交易金额
     */
    private BigDecimal tradeAmount;

    /**
     * 币种
     */
    private Integer currencyType;
    /**
     * 银行返回--银行卡号（缩位）
     */
    private String bankCardNo = "";

    /**
     * 交易类型
     */
    private Integer tradeType = 0;
    /**
     * 业务类型
     */
    private Integer businessType = 0;

    /**
     * 银行返回流水号
     */
    private String bankReturnNo = "";
    /**
     * 授权码
     */
    private String authorizeNo = "";
    /**
     * 授权金额
     */
    private BigDecimal authorizeAmount = new BigDecimal(0.00);

    /**
     * 授权日期
     */
    private String authorizeDate = "";
    /**
     * 错误码
     */
    private String errorCode = "";
    /**
     * 错误说明
     */
    private String errorDescription = "";
    /**
     * 银行受理金额
     */
    private BigDecimal bankAcceptedAmount = new BigDecimal(0.00);
    /**
     * 银行订单状态
     */
    private Integer bankOrderStatus = 0;
    /**
     * 创建交易支付时间
     */
    private Date createTradePayTime = new Date();
    /**
     * 银行订单请求时间
     */
    private Date bankOrderReqTime = new Date();
    /**
     * 银行订单返回时间(同步报文中的时间)
     */
    private String bankOrderReturnTime = "";
    /**
     * 接受回调时间（异步回调时间）
     */
    private Date acceptCallBackTime = new Date();
    /**
     * 银行确认时间（异步报文中的时间）
     */
    private String bankConfirmTime = "";
    /**
     * 备注
     */
    private String backup = "";
    /**
     * 修改人
     */
    private String modified = "";
    /**
     * 修改时间
     */
    private Date modifiedTime = new Date();
    /**
     * 修改Ip
     */
    private String modifiedIp = "";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTradeRequestId() {
        return tradeRequestId;
    }

    public void setTradeRequestId(Long tradeRequestId) {
        this.tradeRequestId = tradeRequestId;
    }

    public Long getTradePaymentId() {
        return tradePaymentId;
    }

    public void setTradePaymentId(Long tradePaymentId) {
        this.tradePaymentId = tradePaymentId;
    }

    public String getTradeDn() {
        return tradeDn;
    }

    public void setTradeDn(String tradeDn) {
        this.tradeDn = tradeDn;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public void setBankOrderNo(String bankOrderNo) {
        this.bankOrderNo = bankOrderNo;
    }

    public String getBankInterfaceId() {
        return bankInterfaceId;
    }

    public void setBankInterfaceId(String bankInterfaceId) {
        this.bankInterfaceId = bankInterfaceId;
    }


    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }



    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getBankReturnNo() {
        return bankReturnNo;
    }

    public void setBankReturnNo(String bankReturnNo) {
        this.bankReturnNo = bankReturnNo;
    }

    public String getAuthorizeNo() {
        return authorizeNo;
    }

    public void setAuthorizeNo(String authorizeNo) {
        this.authorizeNo = authorizeNo;
    }

    public BigDecimal getAuthorizeAmount() {
        return authorizeAmount;
    }

    public void setAuthorizeAmount(BigDecimal authorizeAmount) {
        this.authorizeAmount = authorizeAmount;
    }

    public String getAuthorizeDate() {
        return authorizeDate;
    }

    public void setAuthorizeDate(String authorizeDate) {
        this.authorizeDate = authorizeDate;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public BigDecimal getBankAcceptedAmount() {
        return bankAcceptedAmount;
    }

    public void setBankAcceptedAmount(BigDecimal bankAcceptedAmount) {
        this.bankAcceptedAmount = bankAcceptedAmount;
    }

    public Integer getBankOrderStatus() {
        return bankOrderStatus;
    }

    public void setBankOrderStatus(Integer bankOrderStatus) {
        this.bankOrderStatus = bankOrderStatus;
    }

    public Date getCreateTradePayTime() {
        return createTradePayTime;
    }

    public void setCreateTradePayTime(Date createTradePayTime) {
        this.createTradePayTime = createTradePayTime;
    }

    public Date getBankOrderReqTime() {
        return bankOrderReqTime;
    }

    public void setBankOrderReqTime(Date bankOrderReqTime) {
        this.bankOrderReqTime = bankOrderReqTime;
    }

    public String getBankOrderReturnTime() {
        return bankOrderReturnTime;
    }

    public void setBankOrderReturnTime(String bankOrderReturnTime) {
        this.bankOrderReturnTime = bankOrderReturnTime;
    }

    public Date getAcceptCallBackTime() {
        return acceptCallBackTime;
    }

    public void setAcceptCallBackTime(Date acceptCallBackTime) {
        this.acceptCallBackTime = acceptCallBackTime;
    }

    public String getBankConfirmTime() {
        return bankConfirmTime;
    }

    public void setBankConfirmTime(String bankConfirmTime) {
        this.bankConfirmTime = bankConfirmTime;
    }

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getModifiedIp() {
        return modifiedIp;
    }

    public void setModifiedIp(String modifiedIp) {
        this.modifiedIp = modifiedIp;
    }

    public Integer getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(Integer currencyType) {
        this.currencyType = currencyType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}