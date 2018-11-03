package com.pay.yc.controller.admin;

import com.pay.yc.common.annotation.CurrentUserId;
import com.pay.yc.common.result.dto.PageResultDTO;
import com.pay.yc.common.result.dto.ResultDTO;
import com.pay.yc.convertor.UserConvertor;
import com.pay.yc.dto.admin.UserDTO;
import com.pay.yc.model.admin.PaymentAccount;
import com.pay.yc.model.admin.User;
import com.pay.yc.repository.admin.PaymentAccountRepository;
import com.pay.yc.repository.admin.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 用户管理 WEB端
 */
@Api(tags = {"用户管理 WEB端"})
@RestController
@RequestMapping(value = "/u")
@Slf4j
public class WebUserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConvertor UserConvertor;
    @Autowired
    private PaymentAccountRepository paymentAccountRepository;

    /**
     * 获取用户列表
     *
     * @param pageable
     * @return
     */
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表", httpMethod = "GET")
    @RequestMapping(value = "/getUserList", method = RequestMethod.GET)
    public PageResultDTO<UserDTO> getUserList(Pageable pageable,@CurrentUserId Long userId) {
        if(!userId.equals(1L)){
            return null;
        }
        Page<User> model = this.userRepository.findAll(pageable);
        PageResultDTO<UserDTO> result = this.UserConvertor.toResultDTO(model);
        log.info("获取用户列表成功！");
        return result;
    }


    @ApiOperation(value = "添加用户", notes = "添加用户", httpMethod = "POST")
    @PostMapping(value = "/saveUser")
    public Object saveUser(@RequestBody UserDTO dto,@CurrentUserId Long userId) {
        if(!userId.equals(1L)){
            return ResultDTO.failure("非管理员不能操作");
        }
        //密码加密
        if ("".equals(dto.getPassword())) {
            return ResultDTO.failure("密码不能为空!");
        }
//        User isUser = this.userRepository.findByMobile(user.getMobile());
//        if (isUser != null) {
//            return ResultDTO.failure("不能重复注册!");
//        }
        User model=this.UserConvertor.toModel(dto,null);
        User User=this.userRepository.save(model);
        log.info("用户添加成功!");

        //初始化用户金额
        PaymentAccount paymentAccount=new PaymentAccount();
        paymentAccount.setBalance(0L);
        paymentAccount.setUserId(User.getId());
        paymentAccount.setCreatedDate(new Date());
        this.paymentAccountRepository.save(paymentAccount);
        log.info("新增用户初始化余额成功!");
        return ResultDTO.success();
    }

//    @ApiOperation(value = "添加商户配置", notes = "添加用户", httpMethod = "POST")
//    @PostMapping(value = "/saveConfig")
//    public Object saveConfig(@RequestBody AliPayConfigDTO dto,@CurrentUserId Long userId) {
//        if(!userId.equals(1L)){
//            return ResultDTO.failure("非管理员不能操作");
//        }
//        AliPayConfig model=this.aliPayConfigConvertor.toModel(dto,null);
//        this.aliPayConfigRepository.save(model);
//        log.info("用户添加成功!");
//        return ResultDTO.success();
//    }
//
//    @ApiOperation(value = "获取商户配置列表", notes = "获取商户配置列表", httpMethod = "GET")
//    @RequestMapping(value = "/getAliPayConfigList", method = RequestMethod.GET)
//    public PageResultDTO<AliPayConfigDTO> getAliPayConfigList(Pageable pageable,@CurrentUserId Long userId) {
//        if(!userId.equals(1L)){
//            return null;
//        }
//        Page<AliPayConfig> model = this.aliPayConfigRepository.findAll(pageable);
//        PageResultDTO<AliPayConfigDTO> result = this.aliPayConfigConvertor.toResultDTO(model);
//        log.info("获取商户配置列表！");
//        return result;
//    }
//
//
//    @ApiOperation(value = "获取商户配置下拉框数据", notes = "获取商户配置下拉框数据", httpMethod = "GET")
//    @RequestMapping(value = "/getAliPayConfig", method = RequestMethod.GET)
//    public List<AliPayConfigDTO> getAliPayConfig() {
//        List<AliPayConfig> model = this.aliPayConfigRepository.findAll();
//        List<AliPayConfigDTO> result = this.aliPayConfigConvertor.toListDTO(model);
//        log.info("获取商户配置列表！");
//        return result;
//    }

//    @ApiOperation(value = "根据关键字查询用户", notes = "根据关键字查询用户", httpMethod = "GET")
//    @RequestMapping(value = "/searchUsers", method = RequestMethod.GET)
//    @ResponseBody
//    public PageResultDTO<UserDTO> searchUsersByKeyWord(@RequestParam String keyword, @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC)
//            Pageable pageable) {
//        log.info("根据关键字查询用户");
//        return PageResultDTO.success(
//                UserConvertor.toPageDTO(
//                        userRepository.findByUsernameLikeOrMobileLikeAndStatus(
//                                "%" + keyword + "%", "%" + keyword+ "%", 1,pageable)));
//    }

    /**
     * 修改或上传头像
     */
    @ApiOperation(value = "修改或上传头像", notes = "修改或上传头像", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户编号", required = true),
            @ApiImplicitParam(name = "portrait", value = "头像地址", required = true)
    })
    @RequestMapping(value = "/updateOrUploadPortrait", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultDTO<UserDTO> headPortrait(@RequestParam Long uid, @RequestParam String portrait) {
        User user = this.userRepository.findOneById(uid);
        User model = this.userRepository.save(user);
        ResultDTO<UserDTO> resultDTO = this.UserConvertor.toResultDTO(model);
        return resultDTO;
    }
    /**
     * 编辑商户
     */
    @ApiOperation(value = "编辑商户", notes = "编辑商户", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "商户编号", required = true),
            @ApiImplicitParam(name = "UserDTO", value = "商户信息", required = true)
    })
    @RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultDTO<?> edit(@RequestParam Long uid,@RequestBody UserDTO dto) {
        User user = this.userRepository.findOneById(uid);
        if(dto.getPassword().equals("")){
            dto.setPassword(user.getPassword());
        }else{
            BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
            String hashPass = encode.encode(dto.getPassword());
            dto.setPassword(hashPass);
        }
        User model = this.UserConvertor.toModel(dto,user);
        this.userRepository.save(model);
        return ResultDTO.success();
    }

    @RequestMapping(value = "/queryById", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO<?> queryById(@RequestParam Long uid) {
        User user = this.userRepository.findOneById(uid);
        UserDTO resultDTO = this.UserConvertor.toDTO(user);
        return ResultDTO.success(resultDTO);
    }


}
