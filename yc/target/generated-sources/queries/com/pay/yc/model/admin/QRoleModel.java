package com.pay.yc.model.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QRoleModel is a Querydsl query type for RoleModel
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRoleModel extends EntityPathBase<RoleModel> {

    private static final long serialVersionUID = -979346562L;

    public static final QRoleModel roleModel = new QRoleModel("roleModel");

    public final com.pay.yc.common.result.QAbstractModel _super = new com.pay.yc.common.result.QAbstractModel(this);

    public final StringPath authority = createString("authority");

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

    public final StringPath roleName = createString("roleName");

    //inherited
    public final NumberPath<Long> ver = _super.ver;

    public QRoleModel(String variable) {
        super(RoleModel.class, forVariable(variable));
    }

    public QRoleModel(Path<? extends RoleModel> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRoleModel(PathMetadata metadata) {
        super(RoleModel.class, metadata);
    }

}

