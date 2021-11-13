package com.firenze;

import com.firenze.resource.TeamResource;
import com.firenze.resource.UserResource;
import java.util.Objects;

public class ApplicationContextStub extends ApplicationContext {
    @Override
    public Object getBean(Class<?> aClass) {
        if (Objects.equals(aClass, TeamResource.class)) {
            return new TeamResource();
        }
        if (Objects.equals(aClass, UserResource.class)) {
            return new UserResource();
        }
        return null;
    }
}
