package com.pay.yc.model.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QPaymentAccount is a Querydsl query type for PaymentAccount
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPaymentAccount extends EntityPathBase<PaymentAccount> {

    private static final long serialVersionUID = 1900316924L;

    public static final QPaymentAccount paymentAccount = new QPaymentAccount("paymentAccount");

    public final com.pay.yc.common.result.QAbstractModel _super = new com.pay.yc.common.result.QAbstractModel(this);

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

    public final NumberPath<Long> UserId = createNumber("UserId", Long.class);

    //inherited
    public final NumberPath<Long> ver = _super.ver;

    public QPaymentAccount(String variable) {
        super(PaymentAccount.class, forVariable(variable));
    }

    public QPaymentAccount(Path<? extends PaymentAccount> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPaymentAccount(PathMetadata metadata) {
        super(PaymentAccount.class, metadata);
    }

}

