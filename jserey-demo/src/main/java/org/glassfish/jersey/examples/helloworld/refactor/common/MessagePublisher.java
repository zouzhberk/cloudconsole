package org.glassfish.jersey.examples.helloworld.refactor.common;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import rx.Observable;

/**
 * Created by berk (zouzhberk@163.com)) on 4/20/16.
 */
public class MessagePublisher<T> implements Publisher<Message<T>>
{

    private Observable<Message<T>> observable;

    @Override
    public void subscribe(Subscriber<? super Message<T>> s)
    {

    }
}
