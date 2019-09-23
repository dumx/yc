package com.pay.yc.model.order;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUnifiedOrder is a Querydsl query type for UnifiedOrder
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUnifiedOrder extends EntityPathBase<UnifiedOrder> {

    private static final long serialVersionUID = -550672334L;

    public static final QUnifiedOrder unifiedOrder = new QUnifiedOrder("unifiedOrder");

    public final org.springframework.data.jpa.domain.QAbstractPersistable _super = new org.springframework.data.jpa.domain.QAbstractPersistable(this);

    public final DateTimePath<java.util.Date> beginTime = createDateTime("beginTime", java.util.Date.class);

    public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);

    public final DateTimePath<java.util.Date> endTime = createDateTime("endTime", java.util.Date.class);

    public final DateTimePath<java.util.Date> finishTime = createDateTime("finishTime", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath openId = createString("openId");

    public final StringPath orderNo = createString("orderNo");

    public final StringPath orderType = createString("orderType");

    public final StringPath platform = createString("platform");

    public final NumberPath<Integer> seatNo = createNumber("seatNo", Integer.class);

    public final StringPath state = createString("state");

    public final EnumPath<com.pay.yc.common.enumpub.PaymentTradeStatus> status = createEnum("status", com.pay.yc.common.enumpub.PaymentTradeStatus.class);

    public final StringPath subject = createString("subject");

    public final NumberPath<Long> totalFee = createNumber("totalFee", Long.class);

    public final EnumPath<com.pay.yc.common.enumpub.PaymentOrderType> type = createEnum("type", com.pay.yc.common.enumpub.PaymentOrderType.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUnifiedOrder(String variable) {
        super(UnifiedOrder.class, forVariable(variable));
    }

    public QUnifiedOrder(Path<? extends UnifiedOrder> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUnifiedOrder(PathMetadata metadata) {
        super(UnifiedOrder.class, metadata);
    }

}

