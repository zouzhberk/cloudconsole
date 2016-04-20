package org.glassfish.jersey.examples.helloworld.refactor.common;

import rx.Observable;

/**
 * Created by berk (zouzhberk@163.com)) on 4/20/16.
 */
public class MangoObservable<T>
{
    private Observable<T> observable;

    public static <T> MangoObservable<T> create(Observable.OnSubscribe<T> f)
    {
        return null;
    }

}
