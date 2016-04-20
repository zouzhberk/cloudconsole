package org.glassfish.jersey.examples.helloworld.refactor.common;

/**
 * Created by berk (zouzhberk@163.com)) on 4/20/16.
 */
public class Message<T>
{
    private T entity;
    private String action;
    private String status;
    private String user;
    private String oaid;


    public static class Builder<T>
    {
        T entity;
        private String action;
        private String username;

        private Builder(T entity)
        {
            this.entity = entity;
        }

        public static <T> Builder<T> from(T entity)
        {
            return new Builder<T>(entity);
        }

        public Builder<T> action(String action)
        {
            this.action = action;
            return this;
        }

        public Builder<T> username(String username)
        {

            this.username = username;
            return this;
        }

        public Message<T> build()
        {
            return new Message<>();
        }


    }

}
