package com.simple.payment.sharding.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 *
 * @author:
 * @since: 12-8-10 下午2:38
 * @Version: 1.0.0
 */
public class TradePaymentEntity {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 交易请求id
     */
    private Long tradeRequestId = 0l;

    /**
     * 原交易支付流水号（组）
     */
    private Long pid = 0l;

    /**
     * 交易唯一标识
     */
    private String tradeDN = "";

    /**
     * 外部交易唯一标识号
     */
    private String extTradeDN = "";

    /**
     * 交易类型
     */
    private Integer tradeType=0;

    /**
     * 商户编号
     */
    private String merchantNo = "";

    /**
     * 终端号
     */
    private String terminalNo = "";

    /**
     * 结算商户编号
     */
    private String settleMerchantNo = "";

    /**
     * 通知地址
     */
    private String notifyUrl = "";

    /**
     * 业务类型
     */
    private Integer businessType=0;

    /**
     * 交易金额
     */
    private BigDecimal tradeAmount = new BigDecimal(0.00);

    /**
     * 已完成金额
     */
    private BigDecimal tradeCompAmount = new BigDecimal(0.00);

    /**
     * 商户费率
     */
    private BigDecimal merchantFee = new BigDecimal(0.00);

    /**
     * 银行费率
     */
    private BigDecimal bankFee = new BigDecimal(0.00);

    /**
     * 交易币种
     */
    private Integer currencyType;

    /**
     * 收单行标识
     */
    private Long settlementBankId = 0l;

    /**
     * 信用卡号
     */
    private String bankCardNo = "";

    /**
     * 信用卡有效期
     */
    private String cardExpireDate = "";

    /**
     * 信用卡安全检验值
     */
    private String cvv = "";

    /**
     * 证件类型
     */
    private Integer idCardType=0;

    /**
     * 证件编码
     */
    private String idCardNo = "";

    /**
     * 持卡人
     */
    private String cardHolderName = "";

    /**
     * 密匙版本
     */
    private String keyVersion = "";

    /**
     * 授权码
     */
    private String authorizeNo = "";

    /**
     * 处理方式
     */
    private int handleType;

    /**
     * 授权金额
     */
    private BigDecimal authorizeAmount = new BigDecimal(0.00);

    /**
     * 授权日期
     */
    private String authorizeDate = "";

    /**
     * 分账类型
     */
    private Integer splitType=0;

    /**
     * 分账内容
     */
    private String splitInfo = "";

    /**
     * 交易来源
     */
    private Integer tradeSource=0;

    /**
     * 交易支付状态,未支付，消费，消费撤销等
     */
    private Integer tradePaymentStatus;

    /**
     * 失败原因
     */
    private String failReason = "";

    /**
     * 外部交易请求时间
     */
    private Date extTradeReqTime = new Date(System.currentTimeMillis());

    /**
     * 创建交易请求时间
     */
    private Date createTradeReqTime = new Date(System.currentTimeMillis());

    /**
     * 创建交易支付时间
     */
    private Date createTradePayTime = new Date(System.currentTimeMillis());

    /**
     * 确认交易支付时间
     */
    private Date confirmTradePayTime = new Date(System.currentTimeMillis());

    /**
     * 备注
     */
    private String backUp = "";

    /**
     * 修改人
     */
    private String modified = "";

    /**
     * 修改时间
     */
    private Date modifiedTime = new Date();

    /**
     * 修改人ip
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

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        pid = pid;
    }

    public String getTradeDN() {
        return tradeDN;
    }

    public void setTradeDN(String tradeDN) {
        this.tradeDN = tradeDN;
    }

    public String getExtTradeDN() {
        return extTradeDN;
    }

    public void setExtTradeDN(String extTradeDN) {
        this.extTradeDN = extTradeDN;
    }

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }

    public String getSettleMerchantNo() {
        return settleMerchantNo;
    }

    public void setSettleMerchantNo(String settleMerchantNo) {
        this.settleMerchantNo = settleMerchantNo;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public BigDecimal getTradeCompAmount() {
        return tradeCompAmount;
    }

    public void setTradeCompAmount(BigDecimal tradeCompAmount) {
        this.tradeCompAmount = tradeCompAmount;
    }

    public BigDecimal getMerchantFee() {
        return merchantFee;
    }

    public void setMerchantFee(BigDecimal merchantFee) {
        this.merchantFee = merchantFee;
    }

    public BigDecimal getBankFee() {
        return bankFee;
    }

    public void setBankFee(BigDecimal bankFee) {
        this.bankFee = bankFee;
    }

    public Integer getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(Integer currencyType) {
        this.currencyType = currencyType;
    }

    public Long getSettlementBankId() {
        return settlementBankId;
    }

    public void setSettlementBankId(Long settlementBankId) {
        this.settlementBankId = settlementBankId;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getCardExpireDate() {
        return cardExpireDate;
    }

    public void setCardExpireDate(String cardExpireDate) {
        this.cardExpireDate = cardExpireDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Integer getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(Integer idCardType) {
        this.idCardType = idCardType;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getKeyVersion() {
        return keyVersion;
    }

    public void setKeyVersion(String keyVersion) {
        this.keyVersion = keyVersion;
    }

    public String getAuthorizeNo() {
        return authorizeNo;
    }

    public void setAuthorizeNo(String authorizeNo) {
        this.authorizeNo = authorizeNo;
    }

    public int getHandleType() {
        return handleType;
    }

    public void setHandleType(int handleType) {
        this.handleType = handleType;
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

    public Integer getSplitType() {
        return splitType;
    }

    public void setSplitType(Integer splitType) {
        this.splitType = splitType;
    }

    public String getSplitInfo() {
        return splitInfo;
    }

    public void setSplitInfo(String splitInfo) {
        this.splitInfo = splitInfo;
    }

    public Integer getTradeSource() {
        return tradeSource;
    }

    public void setTradeSource(Integer tradeSource) {
        this.tradeSource = tradeSource;
    }

    public Integer getTradePaymentStatus() {
        return tradePaymentStatus;
    }

    public void setTradePaymentStatus(Integer tradePaymentStatus) {
        this.tradePaymentStatus = tradePaymentStatus;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public Date getExtTradeReqTime() {
        return extTradeReqTime;
    }

    public void setExtTradeReqTime(Date extTradeReqTime) {
        this.extTradeReqTime = extTradeReqTime;
    }

    public Date getCreateTradeReqTime() {
        return createTradeReqTime;
    }

    public void setCreateTradeReqTime(Date createTradeReqTime) {
        this.createTradeReqTime = createTradeReqTime;
    }

    public Date getCreateTradePayTime() {
        return createTradePayTime;
    }

    public void setCreateTradePayTime(Date createTradePayTime) {
        this.createTradePayTime = createTradePayTime;
    }

    public Date getConfirmTradePayTime() {
        return confirmTradePayTime;
    }

    public void setConfirmTradePayTime(Date confirmTradePayTime) {
        this.confirmTradePayTime = confirmTradePayTime;
    }

    public String getBackUp() {
        return backUp;
    }

    public void setBackUp(String backUp) {
        this.backUp = backUp;
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

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
