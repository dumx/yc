package com.pay.yc.controller.weixin;

import com.pay.yc.common.enumpub.PaymentTradeStatus;
import com.pay.yc.common.result.dto.ResultDTO;
import com.pay.yc.convertor.ConfigConvertor;
import com.pay.yc.convertor.order.UnifiedOrderConvertor;
import com.pay.yc.dto.admin.ConfigDTO;
import com.pay.yc.dto.order.UnifiedOrderDTO;
import com.pay.yc.model.admin.Config;
import com.pay.yc.model.admin.User;
import com.pay.yc.model.order.UnifiedOrder;
import com.pay.yc.repository.admin.ConfigRepository;
import com.pay.yc.repository.admin.UserRepository;
import com.pay.yc.repository.order.OrderRepositoryExecutor;
import com.pay.yc.repository.order.UnifiedOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 微信端业务接口
 */
@RestController
@RequestMapping(value = "/weixin")
@Slf4j
public class WxPublicController {


    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private ConfigConvertor configConvertor;

    @Autowired
    private OrderRepositoryExecutor repositoryExecutor;

    @Autowired
    private UnifiedOrderRepository unifiedOrderRepository;


    @Autowired
    private UnifiedOrderConvertor unifiedOrderConvertor;




    @GetMapping(value = "/getIndexInfo")
    public ResultDTO<?> getIndexInfo(@RequestParam String[] type) {
        List<Config> config=this.configRepository.findByTypeIn(type);
        List<ConfigDTO> dto=this.configConvertor.toListDTO(config);
        log.info("获取首页配置成功!----------------------------------");
        return ResultDTO.success(dto);
    }


    @GetMapping(value = "/getStartOrderList")
    public ResultDTO<?> getStartOrderList(@RequestParam Long userId) {
        User user=this.userRepository.findOneById(userId);
        Specification<UnifiedOrder> specification=this.getWhereClause(userId,user.getOpenId(),null,PaymentTradeStatus.SUCCESS,new Date());
        List<UnifiedOrder> model = this.repositoryExecutor.findAll(specification);
        List<UnifiedOrderDTO> result = this.unifiedOrderConvertor.toListDTO(model);
        log.info("获取我的已开始订单成功!,用户:"+userId);
        return ResultDTO.success(result);
    }


    @GetMapping(value = "/getUnStartOrderList")
    public ResultDTO<?> getUnStartOrderList(@RequestParam Long userId) {
        User user=this.userRepository.findOneById(userId);
        Specification<UnifiedOrder> specification=this.getUnStartWhereClause(userId,user.getOpenId(),null,PaymentTradeStatus.SUCCESS,new Date());
        List<UnifiedOrder> model = this.repositoryExecutor.findAll(specification);
        List<UnifiedOrderDTO> result = this.unifiedOrderConvertor.toListDTO(model);
        log.info("获取我的未开始订单成功!,用户:"+userId);
        return ResultDTO.success(result);
    }

    /**
     * 动态生成where语句
     * @param UserId
     * @param state
     * @return
     */
    private Specification<UnifiedOrder> getWhereClause(final Long UserId, String openId, String state, PaymentTradeStatus status,Date time){
        return new Specification<UnifiedOrder>() {
            @Override
            public Predicate toPredicate(Root<UnifiedOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if(openId!=null){
                    predicate.add(cb.equal(root.get("openId"), openId));
                }
                if(state!=null){
                    predicate.add(cb.equal(root.get("state"), state));
                }
                if(status!=null&& !status.equals(PaymentTradeStatus.SUCCESS)){
                    Predicate p=cb.equal(root.get("status"), status);
                    Predicate p2=cb.equal(root.get("status"), PaymentTradeStatus.FAIL);
                    predicate.add(cb.or(p,p2));
                }else if(status!=null){
                    predicate.add(cb.equal(root.get("status"), status));
                }
                //根据时间范围查询
                if(time!=null){
                    Predicate d=cb.lessThan(root.get("beginTime"), new Date());
                    predicate.add(d);

                }

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

    private Specification<UnifiedOrder> getUnStartWhereClause(final Long UserId, String openId, String state, PaymentTradeStatus status,Date time){
        return new Specification<UnifiedOrder>() {
            @Override
            public Predicate toPredicate(Root<UnifiedOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if(openId!=null){
                    predicate.add(cb.equal(root.get("openId"), openId));
                }
                if(state!=null){
                    predicate.add(cb.equal(root.get("state"), state));
                }
                if(status!=null&& !status.equals(PaymentTradeStatus.SUCCESS)){
                    Predicate p=cb.equal(root.get("status"), status);
                    Predicate p2=cb.equal(root.get("status"), PaymentTradeStatus.FAIL);
                    predicate.add(cb.or(p,p2));
                }else if(status!=null){
                    predicate.add(cb.equal(root.get("status"), status));
                }
                //根据时间范围查询
                if(time!=null){
                    Predicate d=cb.greaterThan(root.get("beginTime"), new Date());
                    predicate.add(d);

                }

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

    private Date getNeedTime(int hour,int minute,int second,int addDay,int ...args){
        Calendar calendar = Calendar.getInstance();
        if(addDay != 0){
            calendar.add(Calendar.DATE,addDay);
        }
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,second);
        if(args.length==1){
            calendar.set(Calendar.MILLISECOND,args[0]);
        }
        return calendar.getTime();
    }

}
