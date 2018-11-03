package com.pay.yc.model.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QPaymentItem is a Querydsl query type for PaymentItem
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPaymentItem extends EntityPathBase<PaymentItem> {

    private static final long serialVersionUID = 342001316L;

    public static final QPaymentItem paymentItem = new QPaymentItem("paymentItem");

    public final com.pay.yc.common.result.QAbstractModel _super = new com.pay.yc.common.result.QAbstractModel(this);

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final NumberPath<Long> balance = createNumber("balance", Long.class);

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

    public final NumberPath<Long> occAmount = createNumber("occAmount", Long.class);

    public final StringPath orderNo = createString("orderNo");

    public final EnumPath<PaymentItem.State> state = createEnum("state", PaymentItem.State.class);

    public final NumberPath<Long> tradeDetailId = createNumber("tradeDetailId", Long.class);

    public final EnumPath<PaymentItem.Type> type = createEnum("type", PaymentItem.Type.class);

    public final NumberPath<Long> UserId = createNumber("UserId", Long.class);

    //inherited
    public final NumberPath<Long> ver = _super.ver;

    public QPaymentItem(String variable) {
        super(PaymentItem.class, forVariable(variable));
    }

    public QPaymentItem(Path<? extends PaymentItem> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPaymentItem(PathMetadata metadata) {
        super(PaymentItem.class, metadata);
    }

}

