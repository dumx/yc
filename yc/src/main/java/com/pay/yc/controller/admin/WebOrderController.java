package com.pay.yc.controller.admin;

import com.pay.yc.common.annotation.CurrentUserId;
import com.pay.yc.common.enumpub.PaymentTradeStatus;
import com.pay.yc.common.result.dto.PageResultDTO;
import com.pay.yc.common.result.dto.ResultDTO;
import com.pay.yc.convertor.order.UnifiedOrderConvertor;
import com.pay.yc.dto.order.UnifiedOrderDTO;
import com.pay.yc.model.order.UnifiedOrder;
import com.pay.yc.repository.admin.UserRepository;
import com.pay.yc.repository.order.OrderRepositoryExecutor;
import com.pay.yc.repository.order.UnifiedOrderRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


/**
* 订单管理(WEB端)
* @author dumingxin
* @Date 2017/12/1 18:27
*/
@Api(tags = {"订单管理WEB端"})
@RestController
@RequestMapping(value = "/webOrder")
@Slf4j
public class WebOrderController {

    @Autowired
    private UnifiedOrderRepository unifiedOrderRepository;

    @Autowired
    private UnifiedOrderConvertor unifiedOrderConvertor;

    @Autowired
    private OrderRepositoryExecutor repositoryExecutor;

    @Autowired
    private UserRepository UserRepository;

    /**
     * 获取已完成订单
     *
     * @param pageable
     * @return
     */
    @ApiOperation(value =   "获取已完成订单", notes = "获取已完成订单", httpMethod = "GET")
    @RequestMapping(value = "/getCompleteOrder", method = RequestMethod.GET)
    public PageResultDTO<UnifiedOrderDTO> getIsCompleteOrder(@CurrentUserId Long UserId,@RequestParam String state,@RequestParam(required=false) String orderNo, Pageable pageable) {
        if(UserId==1){
            UserId = null;
        }
        Specification<UnifiedOrder> specification=this.getWhereClause(UserId,orderNo,state,PaymentTradeStatus.SUCCESS);
        Page<UnifiedOrder> model = this.repositoryExecutor.findAll(specification,pageable);
        PageResultDTO<UnifiedOrderDTO> result = this.unifiedOrderConvertor.toResultDTO(model);

        log.info("查询全部订单!");
        return result;
    }

    /**
     * 获取未完成订单包含FAIL WATING
     * @param orderNo
     * @param pageable
     * @return
     */

    @RequestMapping(value = "/getIsCompleteOrder", method = RequestMethod.GET)
    public PageResultDTO<UnifiedOrderDTO> getIsOrder(@CurrentUserId Long UserId,@RequestParam(required=false) String orderNo, Pageable pageable) {
        if(UserId==1){
            UserId = null;
        }
//        Specification<UnifiedOrder> specification = new Specification<UnifiedOrder>() {
//            @Override
//            public Predicate toPredicate(Root<UnifiedOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                Path<String> subjectPath = root.get("subject");
//                Path<String> orderNoPath = root.get("orderNo");
//                /**
//                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
//                 */
//                Predicate p1=cb.like(subjectPath, "%"+subject+"%");
//                Predicate p2=cb.like(orderNoPath, "%"+orderNo+"%");
//
//                if(p1==null && p2==null){
//                    return null;
//                }else{
//                    return cb.and(p1,p2);
//                }
//
//            };
//        };
        Specification<UnifiedOrder> specification=this.getWhereClause(UserId,orderNo,null,PaymentTradeStatus.WATING);
        Page<UnifiedOrder> model = this.repositoryExecutor.findAll(specification,pageable);
        PageResultDTO<UnifiedOrderDTO> result = this.unifiedOrderConvertor.toResultDTO(model);

        log.info("查询全部订单!");
        return result;
    }

    @RequestMapping(value = "/getAllOrder", method = RequestMethod.GET)
    public PageResultDTO<UnifiedOrderDTO> getAllOrder(@CurrentUserId Long UserId,@RequestParam(required=false) String orderNo,@RequestParam(required = false) PaymentTradeStatus status, Pageable pageable) {
        if(UserId==1){
            UserId = null;
        }
        Specification<UnifiedOrder> specification=this.getWhereClause(UserId,orderNo,null,status);
        Page<UnifiedOrder> model = this.repositoryExecutor.findAll(specification,pageable);
        PageResultDTO<UnifiedOrderDTO> result = this.unifiedOrderConvertor.toResultDTO(model);
        log.info("查询全部订单!");
        return result;
    }

    /**
     * 获取未支付订单
     *
     * @param pageable
     * @return
     */
//    @ApiOperation(value = "获取待支付订单", notes = "获取待支付订单", httpMethod = "GET")
//    @RequestMapping(value = "/getUnPayOrderList", method = RequestMethod.GET)
//    public PageResultDTO<UnifiedOrderDTO> getOrderList(@RequestParam(required=false) String subject, @RequestParam(required=false) String orderNo, Pageable pageable) {
//        Specification<UnifiedOrder> specification=this.getWhereClause(subject,orderNo,null,PaymentTradeStatus.WATING);
//        Page<UnifiedOrder> model = this.repositoryExecutor.findAll(specification,pageable);
//        PageResultDTO<UnifiedOrderDTO> result = this.unifiedOrderConvertor.toResultDTO(model);
//        log.info("获取待支付订单!");
//        return result;
//    }


    /**
     * 查看订单详情
     *
     * @param orderId
     * @return
     */
    @ApiOperation(value = "查看订单详情", notes = "查看订单详情", httpMethod = "GET")
    @RequestMapping(value = "/getOrderDetail", method = RequestMethod.GET)
    public ResultDTO<UnifiedOrderDTO> getOrderDetail(@RequestParam Long orderId) {
        UnifiedOrder model = this.unifiedOrderRepository.findOneById(orderId);
        ResultDTO<UnifiedOrderDTO> result = this.unifiedOrderConvertor.toResultDTO(model);
        log.info("查看订单详情,订单编号:{}", orderId);
        return result;
    }







    /**
     * 动态生成where语句
     * @param UserId
     * @param orderNo
     * @param state
     * @return
     */
    private Specification<UnifiedOrder> getWhereClause(final Long UserId,String orderNo,String state,PaymentTradeStatus status){
        return new Specification<UnifiedOrder>() {
            @Override
            public Predicate toPredicate(Root<UnifiedOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicate = new ArrayList<>();
                if(UserId!=null){
                    predicate.add(cb.equal(root.get("UserId"), UserId));
                }
                if(orderNo!=null){
                    predicate.add(cb.like(root.get("orderNo"), "%"+orderNo+"%"));
                }
                if(state!=null){
                    predicate.add(cb.equal(root.get("state"), state));
                }
                if(status!=null&& !status.equals(PaymentTradeStatus.SUCCESS)){
//                    predicate.add(cb.equal(root.get("status"), status));
                    Predicate p=cb.equal(root.get("status"), status);
                    Predicate p2=cb.equal(root.get("status"), PaymentTradeStatus.FAIL);
                    predicate.add(cb.or(p,p2));
                }else if(status!=null){
                    predicate.add(cb.equal(root.get("status"), status));
                }
                //根据时间范围查询
//                if(searchArticle.getRecTimeStart()!=null){
//                    predicate.add(cb.greaterThanOrEqualTo(root.get("recommendTime").as(Date.class), searchArticle.getRecTimeStart()));
//                }
//                if (searchArticle.getRecTimeEnd()!=null){
//                    predicate.add(cb.lessThanOrEqualTo(root.get("recommendTime").as(Date.class), searchArticle.getRecTimeEnd()));
//                }
//                if (StringUtils.isNotBlank(searchArticle.getNickname())){
//                    //两张表关联查询
//                    Join<Article,User> userJoin = root.join(root.getModel().getSingularAttribute("user",User.class),JoinType.LEFT);
//                    predicate.add(cb.like(userJoin.get("nickname").as(String.class), "%" + searchArticle.getNickname() + "%"));
//                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }
}
