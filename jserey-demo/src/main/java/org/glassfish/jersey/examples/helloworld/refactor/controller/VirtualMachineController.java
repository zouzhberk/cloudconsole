package org.glassfish.jersey.examples.helloworld.refactor.controller;

import org.glassfish.jersey.examples.helloworld.refactor.common.Message;
import org.glassfish.jersey.examples.helloworld.refactor.service
        .VirtualMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import rx.schedulers.Schedulers;

/**
 * Created by berk (zouzhberk@163.com)) on 4/20/16.
 */
@Controller
public class VirtualMachineController
{
    @Autowired
    VirtualMachineService virtualMachineService;

    @RequestMapping(value = "/vm/createvm", method = RequestMethod.POST)
    public ModelAndView deployVM(String username, String oaid, String
            displayName, String datacenter)
    {
        ModelMap model = new ModelMap();
        virtualMachineService.deployVM(displayName, datacenter, oaid)
                .subscribeOn(Schedulers.computation())
                .subscribe(entity -> {
                    Message message = Message.Builder.from(entity
                            .getDisplayName())
                            .action("vm.deploy")
                            .username(username)
                            .build();
                    //保存异步消息
                });
        model.put("SUCCESS", true);
        return new ModelAndView(new MappingJackson2JsonView(), model);
    }
}
