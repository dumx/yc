package com.pay.yc.common.result;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAbstractModel is a Querydsl query type for AbstractModel
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QAbstractModel extends EntityPathBase<AbstractModel> {

    private static final long serialVersionUID = -1376403256L;

    public static final QAbstractModel abstractModel = new QAbstractModel("abstractModel");

    public final org.springframework.data.jpa.domain.QAbstractPersistable _super = new org.springframework.data.jpa.domain.QAbstractPersistable(this);

    public final NumberPath<Long> createdBy = createNumber("createdBy", Long.class);

    public final DateTimePath<java.util.Date> createdDate = createDateTime("createdDate", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> lastModifiedBy = createNumber("lastModifiedBy", Long.class);

    public final DateTimePath<java.util.Date> lastModifiedDate = createDateTime("lastModifiedDate", java.util.Date.class);

    public final NumberPath<Long> ver = createNumber("ver", Long.class);

    public QAbstractModel(String variable) {
        super(AbstractModel.class, forVariable(variable));
    }

    public QAbstractModel(Path<? extends AbstractModel> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAbstractModel(PathMetadata metadata) {
        super(AbstractModel.class, metadata);
    }

}

