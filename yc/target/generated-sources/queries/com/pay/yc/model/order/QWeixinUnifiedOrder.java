package com.pay.yc.model.order;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QWeixinUnifiedOrder is a Querydsl query type for WeixinUnifiedOrder
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWeixinUnifiedOrder extends EntityPathBase<WeixinUnifiedOrder> {

    private static final long serialVersionUID = 317612852L;

    public static final QWeixinUnifiedOrder weixinUnifiedOrder = new QWeixinUnifiedOrder("weixinUnifiedOrder");

    public final com.pay.yc.common.result.QAbstractModel _super = new com.pay.yc.common.result.QAbstractModel(this);

    public final StringPath appId = createString("appId");

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdDate = _super.createdDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.util.Date> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath nonceStr = createString("nonceStr");

    public final StringPath openId = createString("openId");

    public final StringPath orderNo = createString("orderNo");

    public final StringPath prepayId = createString("prepayId");

    public final StringPath raw = createString("raw");

    public final StringPath sign = createString("sign");

    public final StringPath spbill_create_ip = createString("spbill_create_ip");

    public final EnumPath<com.pay.yc.common.enumpub.PaymentTradeStatus> status = createEnum("status", com.pay.yc.common.enumpub.PaymentTradeStatus.class);

    public final StringPath subject = createString("subject");

    public final DateTimePath<java.util.Date> timeEnd = createDateTime("timeEnd", java.util.Date.class);

    public final StringPath timestamp = createString("timestamp");

    public final NumberPath<Long> totalFee = createNumber("totalFee", Long.class);

    public final StringPath tradeNo = createString("tradeNo");

    public final EnumPath<com.pay.yc.common.enumpub.PaymentOrderType> type = createEnum("type", com.pay.yc.common.enumpub.PaymentOrderType.class);

    //inherited
    public final NumberPath<Long> ver = _super.ver;

    public QWeixinUnifiedOrder(String variable) {
        super(WeixinUnifiedOrder.class, forVariable(variable));
    }

    public QWeixinUnifiedOrder(Path<? extends WeixinUnifiedOrder> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWeixinUnifiedOrder(PathMetadata metadata) {
        super(WeixinUnifiedOrder.class, metadata);
    }

}

