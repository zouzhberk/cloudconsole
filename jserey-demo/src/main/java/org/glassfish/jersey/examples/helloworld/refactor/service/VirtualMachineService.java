package org.glassfish.jersey.examples.helloworld.refactor.service;

import org.glassfish.jersey.examples.helloworld.domain.VirtualMachineEntity;
import rx.Observable;

/**
 * Created by berk (zouzhberk@163.com)) on 4/15/16.
 */

public class VirtualMachineService
{
    public Observable<VirtualMachineEntity> deployVM(String displayName,
                                                     String datacenterId,
                                                     String oaid)
    {
        return Observable.create(f -> {
            try
            {
                //TODO：资源限额检测，
                f.onNext(new VirtualMachineEntity());

                //TODO：創建成功，
                f.onNext(new VirtualMachineEntity());

                f.onCompleted();

            }
            catch (Exception e)
            {
                f.onError(e);
            }
        });
    }


}
